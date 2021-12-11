package gaosi.com.learn.studentapp.qrcode

import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.alibaba.fastjson.JSON
import com.gaosi.homework.jump.HomeworkOptions
import com.gaosi.homework.jump.JumpDoAnswerCardBuilder
import com.gaosi.homework.jump.JumpDoHomeworkBuilder
import com.gaosi.homework.jump.JumpDoScoreBuilder
import com.gaosi.specialcourse.jump.JumpDoSpecialCourseBuilder
import com.gaosi.specialcourse.jump.SpecialCourseOptions
import com.gaosi.webresource_uploader.WebResourceListDialog
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.InitBaseLib
import com.gsbaselib.base.GSBaseConstants
import com.gsbaselib.base.inject.GSAnnotation
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.LOGGER
import com.gsbaselib.utils.StatusBarUtil
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.StatisticsDictionary
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.SystemUtil
import com.gstudentlib.util.entity.PlayType
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import gaosi.com.learn.application.AppApi
import gaosi.com.learn.bean.qr.QrHomeworkInfoBean
import gaosi.com.learn.bean.video.VideoAnalysisBean
import kotlinx.android.synthetic.main.activity_scan.*
import org.json.JSONObject
import java.util.HashMap

@Router("qrCode")
@GSAnnotation(pageId = StatisticsDictionary.qrCode)
class ScanActivity : BaseActivity(), QRCodeView.Delegate {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        title_bar.setLeftListener(this)
        zxingview.setDelegate(this)
    }

    override fun onStart() {
        super.onStart()
        try {
            //打开后置摄像头开始预览，但是并未开始识别
            zxingview.startCamera()
            //显示扫描框，并开始识别
            zxingview.startSpotAndShowRect()
        } catch (e : Exception) {
            LOGGER.log(e)
        }
    }

    override fun onScanQRCodeSuccess(result: String?) {
        try {
            if (result?.contains("t=1&wid=") == true) {
                val data = Uri.parse(result)
                val wid = data.getQueryParameter("wid")
                getVideoInfo(wid)
            } else if(result?.contains("t=2&lid=") == true) {
                val data = Uri.parse(result)
                val lid = data.getQueryParameter("lid")
                gotoSpecialCoursePage(lid)
                finish()
            }else {
                val split = result?.split("-")
                split?.let {
                    if(it.size == 2) {
                        val classTypeId = it[0]
                        val lessonId = it[1]
                        getQRHomeworkInfo(classTypeId , lessonId)
                        return
                    }
                }
                if(STBaseConstants.isDebug && result != null
                        && (result.contains("react") || result.contains("prod"))) {
                    val split = result.split("/")
                    val name = split[split.size - 1]
                    WebResourceListDialog(this).localRequest(result, name, pb, this)
                    ToastUtil.showToast("下载中...")
                }else {
                    ToastUtil.showToast("请扫描纸质错题本上解析视频二维码")
                    //开始识别
                    startSpot()
                }
            }
        } catch (e: Exception) {
            LOGGER.log(e)
            ToastUtil.showToast("二维码错误")
            //开始识别
            startSpot()
        }
    }
    private fun gotoSpecialCoursePage(lessonId:String){
        val doSpecialCourseBuilder= JumpDoSpecialCourseBuilder()
                .setStudentId(STBaseConstants.userId)
                .setClassId("0")
                .setLessonId(lessonId)
                .setActionType("1")
        SpecialCourseOptions.instance
                .with(this)
                .pad(pageId)
                .start(doSpecialCourseBuilder)
    }

    override fun onScanQRCodeOpenCameraError() {
        ToastUtil.showToast("打开相机出错啦！")
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        //检测光线是否过暗
    }

    /**
     * 查询视频、题目解析
     */
    private fun getVideoInfo(topicId: String) {
        val params = HashMap<String, String>()
        params["topicId"] = topicId
        GSRequest.startRequest(GSAPI.getErrorQuestionsVideo, params, object : GSJsonCallback<VideoAnalysisBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<VideoAnalysisBean>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                if (result.status == 1) {
                    if (result.body != null) {
                        val videoAnalysisBean = result.body
                        if (videoAnalysisBean.contentType == 1) {
                            //解析视频
                            val vUrl = videoAnalysisBean.videoUrl
                            val title = videoAnalysisBean.videoName
                            var url = "axx://playBack?vUrl=%s&title=%s&pad=%s&playType=%s"
                            url = String.format(url, vUrl, title, pageId , PlayType.analysisVideo.toString())
                            SchemeDispatcher.jumpPage(this@ScanActivity, url)
                        } else {
                            //进错题解析页
                            val suffix = "correctionQrcode.web.js"
                            val jsonObject = JSONObject()
                            jsonObject.put("topicType", videoAnalysisBean.topicType)
                            jsonObject.put("parsingContent", JSON.toJSONString(videoAnalysisBean.topicParsingContentList))
                            SchemeDispatcher.gotoWebPage(this@ScanActivity, InitBaseLib.getInstance().configManager.h5ServerUrl + suffix, SystemUtil.generateDefautJsonStr(jsonObject, suffix))
                        }
                        finish()
                    }
                } else {
                    ToastUtil.showToast(result.message)
                    //开始识别
                    startSpot()
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (isFinishing || isDestroyed) {
                    return
                }
                ToastUtil.showToast(message)
                //开始识别
                startSpot()
            }
        })
    }

    /**
     * 扫码交作业参数
     */
    private fun getQRHomeworkInfo(classTypeId: String , lessonId: String) {
        val params = HashMap<String, String>()
        params["studentCode"] = GSBaseConstants.userId
        params["classTypeId"] = classTypeId
        params["lessonId"] = lessonId
        params["studentId"] = GSBaseConstants.userId
        GSRequest.startRequest(AppApi.getQR_HomeworkInfo, params, object : GSJsonCallback<QrHomeworkInfoBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<QrHomeworkInfoBean>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                if(showResponseErrorMessage(result) == 1) {
                    if (result.body != null) {
                        val qrHomeworkInfoBean = result.body
                        doHomework(qrHomeworkInfoBean)
                    }else {
                        //开始识别
                        startSpot()
                    }
                }else {
                    //开始识别
                    startSpot()
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (isFinishing || isDestroyed) {
                    return
                }
                ToastUtil.showToast(message)
                //开始识别
                startSpot()
            }
        })
    }

    /**
     * 去交作业
     */
    private fun doHomework(qrHomeworkInfoBean: QrHomeworkInfoBean?) {
        //状态值 0没有扫码权限 1 正常 2 没有加入班级 3存在两个班级 4作业未开放 5作业已提交
        when(qrHomeworkInfoBean?.status) {
            0 -> { //没有扫码权限
                showDoHomeworkDialog("没有扫码权限")
            }
            1 -> {
                collectClickEvent("XSD_350")
                if(qrHomeworkInfoBean?.questionsCount != 0) {
                    //0不需要删除 1需要删除重交 2已重判 3已重交
                    if(qrHomeworkInfoBean?.stars?:0 == 0) {
                        if ((qrHomeworkInfoBean?.haveAnswerCount ?: 0) >= qrHomeworkInfoBean?.questionsCount?:0) {
                            //题目已经答完(跳转答题卡页面)
                            HomeworkOptions.instance
                                    .with(this)
                                    .pad(pageId)
                                    .start(
                                            JumpDoAnswerCardBuilder()
                                                    .setClassId(qrHomeworkInfoBean?.classId)
                                                    .setLessonId(qrHomeworkInfoBean?.lessonId)
                                                    .setFlag(qrHomeworkInfoBean?.flag.toString())
                                                    .setLessonTitle(qrHomeworkInfoBean?.lessonName)
                                                    .setSource("2")//1 来自交作业页面 2 来自列表
                                                    .setAction("1")) //交作业动作 0 正常sdk 交作业 1 扫码sdk交作业
                        }else {
                            HomeworkOptions.instance
                                    .with(this)
                                    .pad(pageId)
                                    .start(
                                            JumpDoHomeworkBuilder()
                                                    .setClassId(qrHomeworkInfoBean?.classId)
                                                    .setLessonId(qrHomeworkInfoBean?.lessonId)
                                                    .setLessonTitle(qrHomeworkInfoBean?.lessonName)
                                                    .setFlag(qrHomeworkInfoBean?.flag.toString())
                                                    .setNum(qrHomeworkInfoBean?.lessonNum.toString())
                                                    .setAction("1")) //交作业动作 0 正常sdk 交作业 1 扫码sdk交作业
                        }
                    }
                    finish()
                }
            }
            2 -> { //没有加入班级
                showDoHomeworkDialog("未加入对应班级")
            }
            3 -> {//存在两个班级
                showDoHomeworkDialog("您有两个相同班级，请返回首页找到对应班级提交")
            }
            4 -> {//作业未开放
                showDoHomeworkDialog("本讲次自我巩固没有开放")
            }
            5 -> {
                collectClickEvent("XSD_351")
                HomeworkOptions.instance
                        .with(this)
                        .start(
                                JumpDoScoreBuilder()
                                        .setClassId(qrHomeworkInfoBean?.classId)
                                        .setLessonId(qrHomeworkInfoBean?.lessonId)
                                        .setShowReward(0)
                        )
                finish()
            }
            6 -> {//已经结课
                showDoHomeworkDialog("已结课")
            }
            7 -> {//没有作业
                showDoHomeworkDialog("本讲次自我巩固没有开放")
            }
        }
    }

    /**
     * 展示扫码交作业提示弹框
     */
    private fun showDoHomeworkDialog(message: String) {
        DialogUtil.getInstance().create(this , R.layout.ui_dialog_style_1)
                .show(object : AbsAdapter() {
                    override fun bindListener(listener: View.OnClickListener?) {
                        this.bindText(R.id.tvTop , message)
                        this.bindText(R.id.tvConfirm , "确定")
                        this.findViewById<TextView>(R.id.tvBottom).visibility = View.GONE
                        this.bindListener(listener , R.id.tvConfirm)
                    }
                    override fun onClick(v: View?, dialog: DialogUtil?) {
                        super.onClick(v, dialog)
                        when(v?.id) {
                            R.id.tvConfirm -> {
                                dialog?.dismiss()
                            }
                        }
                    }
                    override fun onDismiss() {
                        super.onDismiss()
                        //开始识别
                        startSpot()
                    }
                })
    }

    /**
     * 开始识别
     */
    private fun startSpot() {
        //做延迟，防止二维码不对引起的疯狂toast
        mHandler.postDelayed({
            zxingview.startSpot()
        }, 1000)
    }

    override fun setStatusBar() {
        this.setStatusBar(ContextCompat.getColor(mContext, R.color.white), 0)
        StatusBarUtil.setLightMode(this)
    }

    override fun onStop() {
        //关闭摄像头预览，并且隐藏扫描框
        zxingview.stopCamera()
        super.onStop()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
        }
    }

    override fun onDestroy() {
        //销毁二维码扫描控件
        zxingview.onDestroy()
        super.onDestroy()
    }
}
