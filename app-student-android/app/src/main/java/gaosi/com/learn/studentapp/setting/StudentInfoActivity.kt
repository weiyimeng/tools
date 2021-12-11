package gaosi.com.learn.studentapp.setting

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import com.gaosi.rn.student.event.RNStudentEventType
import com.github.mzule.activityrouter.annotation.Router
import com.gsbaselib.InitBaseLib
import com.gsbaselib.base.event.EventBean
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.StatusBarUtil
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.glide.ImageLoader
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.SDKConfig
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.SystemUtil
import com.lzy.okgo.model.Response
import com.r0adkll.slidr.Slidr
import gaosi.com.learn.R
import gaosi.com.learn.application.AppApi
import gaosi.com.learn.application.event.AppEventType
import gaosi.com.learn.bean.login.UpdateNameStatusEntity
import gaosi.com.learn.bean.raw.student_studentInfoBean
import kotlinx.android.synthetic.main.activity_student_info.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import org.json.JSONObject

/**
 * 我的信息
 * Created by dingyz on 2018/12/18.
 */
@Router("studentInfo")
class StudentInfoActivity : BaseActivity() {

    private var mAvatarUrl: String? = ""
    private var mName: String? = ""
    private var mSchoolName: String? = ""
    private var mSex: Int = -1 // 1 男 0 女
    private var mGradeName: String? = null //年级名称

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置右滑动返回
        Slidr.attach(this)
        setContentView(R.layout.activity_student_info)
        EventBus.getDefault().register(this)
    }

    override fun initView() {
        super.initView()
        title_bar.setLeftListener(this)
        llName.setOnClickListener(this)
        llHeader.setOnClickListener(this)
        llReadClass.setOnClickListener(this)
    }

    /**
     * 展示用户信息
     */
    private fun showStudentInfo() {
        if (!TextUtils.isEmpty(mAvatarUrl)) {
            ImageLoader.setCircleImageViewResource(ivHeader, "$mAvatarUrl?imageView2/2/w/300", R.drawable.icon_default_header)
        }
        if (mSex != -1) {
            tvSex.text = if (mSex == 1) "男" else "女"
        }
        if (!TextUtils.isEmpty(mName)) {
            tvName.text = mName
        }
        if (!TextUtils.isEmpty(mSchoolName)) {
            tvSchool.text = mSchoolName
        }else {
            tvSchool.text = "未完善"
        }
        if(!TextUtils.isEmpty(mGradeName)) {
            tvReadClass.text = mGradeName
        }else {
            tvReadClass.text = "未完善"
        }
        if(!TextUtils.isEmpty(STBaseConstants.userInfo?.phone)) {
            if(STBaseConstants.userInfo?.phone?.length == 11) {
                tvPhone.text = "${STBaseConstants.userInfo?.phone?.subSequence(0 , 3)}****${STBaseConstants.userInfo?.phone?.subSequence(7 , 11)}"
            }else {
                tvPhone.text = STBaseConstants.userInfo?.phone
            }
        }else {
            tvPhone.text = "未完善"
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
            R.id.llName -> {
                updateName()
                collectClickEvent("XSD_442")
            }
            R.id.llHeader -> {
                goMyHeader()
                collectClickEvent("as302_clk_myxingxiang")
            }
            R.id.llReadClass -> {
                goSelectGrade()
            }
        }
    }

    private fun updateName() {
        if (TextUtils.isEmpty(mName)) {
            return
        }
        showLoadingProgressDialog()
        val params = java.util.HashMap<String, String>()
        params["studentId"] = STBaseConstants.userId
        GSRequest.startRequest(AppApi.changeStudentNameStatus, params, object : GSJsonCallback<UpdateNameStatusEntity>() {
            override fun onResponseSuccess(response: Response<*>, code: Int, result: GSHttpResponse<UpdateNameStatusEntity>) {
                if (isFinishing) {
                    return
                }
                dismissLoadingProgressDialog()
                if (result.isSuccess) {
                    if (result.body != null) {
                        val updateNameStatusEntity = result.body
                        var url = "axx://updateName?changeNameStatus=%s&changeNameTimeIntervalContent=%s&lastChangeNameTimeContent=%s&name=%s"
                        url = String.format(url , updateNameStatusEntity?.changeNameStatus , updateNameStatusEntity?.changeNameTimeIntervalContent , updateNameStatusEntity?.lastChangeNameTimeContent , mName)
                        SchemeDispatcher.jumpPage(this@StudentInfoActivity, url)
                    }
                }
            }

            override fun onResponseError(response: Response<*>, code: Int, message: String) {
                if (isFinishing) {
                    return
                }
                dismissLoadingProgressDialog()
                ToastUtil.showToast(message + "")
            }
        })
    }

    /**
     * 修改头像
     */
    private fun goMyHeader() {
        val params = JSONObject()
        val suffix = "myHead.web.js"
        try {
            params.put("callback", "0")
            params.put("coinCount", 1)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        SystemUtil.gotoWebPage(this, InitBaseLib.getInstance().configManager.h5ServerUrl + suffix, SystemUtil.generateDefautJsonStr(params, suffix))
    }

    private fun requestStudentInfo() {
        val params = HashMap<String, String>()
        params["studentId"] = STBaseConstants.userId
        GSRequest.startRequest(GSAPI.getStudentInfo, params, object : GSJsonCallback<student_studentInfoBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<student_studentInfoBean>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                if (result.body == null) {
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                val studentInfoBean = result.body
                studentInfoBean?.let {
                    mName = it.name
                    mAvatarUrl = it.avatarUrl
                    mSchoolName = it.schoolName
                    mSex = it.sex
                    mGradeName = it.gradeName
                    showStudentInfo()
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (isFinishing || isDestroyed) {
                    return
                }
                ToastUtil.showToast("网络未连接")
            }
        })
    }

    /**
     * 我的积分与金币
     */
    private fun goSelectGrade() {
        val json = JSONObject()
        json.put("userId", STBaseConstants.userId)
        val s = "axx://rnPage?url=%s&param=%s"
        val url = String.format(s, "SelectGrade", json.toString())
        SchemeDispatcher.jumpPage(this, url)
    }

    /**
     * 修改姓名成功
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateNameSuccess(event: EventBean) {
        if (AppEventType.UPDATE_NAME_SUCCESS != event.what) {
            return
        }
        mName = event.obj?.toString()
        STBaseConstants.userInfo?.truthName = mName
        SDKConfig.updateInfo(STBaseConstants.userInfo , STBaseConstants.Token)
        tvName.text = mName
    }

    override fun setStatusBar() {
        setStatusBar(Color.parseColor("#F8FAFD"), 0)
        StatusBarUtil.setLightMode(this)
    }

    override fun onResume() {
        super.onResume()
        requestStudentInfo()
    }

    override fun onDestroy() {
        //刷新上个页面
        val event = EventBean()
        event.what = RNStudentEventType.REFRESH
        EventBus.getDefault().post(event)
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
