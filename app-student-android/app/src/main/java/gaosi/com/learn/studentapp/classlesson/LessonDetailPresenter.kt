package gaosi.com.learn.studentapp.classlesson

import android.app.Activity
import android.app.AlertDialog
import com.gaosi.englishhomework.jump.EnglishHomeworkOptions
import com.gaosi.englishhomework.jump.JumpDoEnglishHomeworkBuilder
import com.gaosi.englishhomework.jump.JumpDoEnglishHomeworkScoreBuilder
import com.gaosi.homework.jump.HomeworkOptions
import com.gaosi.homework.jump.JumpDoAnswerCardBuilder
import com.gaosi.homework.jump.JumpDoHomeworkBuilder
import com.gaosi.homework.jump.JumpDoScoreBuilder
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.utils.LangUtil
import com.gsbiloglib.builder.GSConstants.Companion.P
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.util.SchemeDispatcher
import com.lzy.okgo.model.Response
import gaosi.com.learn.application.AppApi
import gaosi.com.learn.application.WeexApplication
import gaosi.com.learn.bean.classlesson.lessondetail.CourseActivationBean
import gaosi.com.learn.bean.raw.Lesson

/**
 * 讲次详情工具
 */
object LessonDetailPresenter {

    /**
     * 获取pageId
     * @return
     */
    private val pageId: String
        get() {
            val activity = WeexApplication.getApplication().currentActivity
            return if (activity != null && activity is BaseActivity) {
                activity.pageId
            } else ""
        }

    /**
     * 设置课后作业监听  自我巩固
     * @param lesson
     */
    fun setHomeworkOnclickListener(lesson: Lesson?, context: Activity, classId: String) {
        lesson ?: return
        //题量大于0，存在作业
        if ("0" != lesson.questionsCount) {
            val jumpDoHomeworkBuilder = JumpDoHomeworkBuilder()
                    .setClassId(classId)
                    .setLessonId(lesson.lessonId)
                    .setLessonTitle(lesson.lessonName)
                    .setFlag(lesson.flag)
                    .setNum(lesson.num)
                    .setGoldNum(lesson?.goldCoinsNum)
                    .setAction("0") //交作业动作 0 正常sdk 交作业 1 扫码sdk交作业
            if ("0" == lesson.stars) {
                //0不需要删除 1需要删除重交 2已重判 3已重交
                if ("1" == lesson.flag) {
                    showAlertDialog(lesson, context, jumpDoHomeworkBuilder)
                } else {
                    //交作业/答题卡
                    requestHomeworkFloatLayer(lesson, classId, context, jumpDoHomeworkBuilder)
                }
            } else {
                //结果页
                if(null == lesson?.submitSource || 0 == lesson?.submitSource) {
                    GSLogUtil.collectClickLog(P!!.getCurrRefer(), "XSD_571", "")
                    HomeworkOptions.instance
                            .with(context)
                            .start(
                                    JumpDoScoreBuilder()
                                            .setClassId(classId)
                                            .setLessonId(lesson.lessonId)
                                            .setShowReward(0)
                            )
                }else {
                    var url = "axx://aiCorrectionResultPhoto?classId=%s&lessonId=%s&classTypeId=%s&action=%s"
                    url = String.format(url , jumpDoHomeworkBuilder.options.classId
                            , jumpDoHomeworkBuilder.options.lessonId
                            , lesson?.classTypeId
                            , "1") //0 首页拍照 1 讲次拍照
                    SchemeDispatcher.jumpPage(context, url)
                }
            }
        }
    }

    /**
     * 设置英语语音监听
     */
    fun setEnglishOnclickListener(lesson: Lesson?, context: Activity, classId: String) {
        lesson ?: return
        if (lesson.englishSpeechStar > 0) {
            //口语结果页
            gotoEnglishHomeworkScore(classId,lesson.lessonId,context)
        } else {
            //口语作业页
            gotoEnglishHomework(lesson,context,classId)
        }
    }

    /**
     * 检查三阶课是否激活
     * status
     * -1 body == null
     * 0 返回后台message
     * 1 通过激活测试
     * 2 未通过激活测试'
     * type 1 英语预习 2 专题课 3 订正 4解析视频
     */
    private var mIsCheckingCourseStatus: Boolean = false
    fun checkCourseActivation(classId: String , lessonId: String , type: Int , courseActivation: Int ,onCheckCourseActivationListener: OnCheckCourseActivationListener?) {
        if(courseActivation == 1) {
            onCheckCourseActivationListener?.onCheckCourseStatus(1 , "")
            return
        }
        if(mIsCheckingCourseStatus) {
            LogUtil.d("正在请求中...")
            return
        }
        mIsCheckingCourseStatus = true
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["classId"] = classId
        paramMap["lessonId"] = lessonId
        paramMap["type"] = type.toString()
        GSRequest.startRequest(AppApi.courseActivation, paramMap, object : GSJsonCallback<CourseActivationBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<CourseActivationBean>) {
                if (result.status == 1) {
                    if(null == result.body) {
                        onCheckCourseActivationListener?.onCheckCourseStatus(-1 , "body == null")
                    }else {
                        if(1 == result.body.isActivation) {
                            onCheckCourseActivationListener?.onCheckCourseStatus(1 , "")
                        }else {
                            onCheckCourseActivationListener?.onCheckCourseStatus(2, result.body.comment?:"")
                        }
                    }
                }else {
                    onCheckCourseActivationListener?.onCheckCourseStatus(0 , result.message)
                }
                mIsCheckingCourseStatus = false
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                onCheckCourseActivationListener?.onCheckCourseStatus(0 , message?:"")
                mIsCheckingCourseStatus = false
            }
        })
    }

    /**
     * 跳转至口语结果页
     */
    private fun gotoEnglishHomeworkScore(classId: String,lessonId:String,context: Activity){
        GSLogUtil.collectClickLog(P!!.getCurrRefer(), "XSD_572", "")
        val doEnglishHomeworkScoreBuilder = JumpDoEnglishHomeworkScoreBuilder()
                .setClassId(classId)
                .setLessonId(lessonId)
                .setShowReward(0)
        EnglishHomeworkOptions.instance
                .with(context)
                .start(doEnglishHomeworkScoreBuilder)
    }

    /**
     * 跳转至口语作业
     * flag== 1 有更新
     */
    private fun gotoEnglishHomework(lesson: Lesson?, context: Activity, classId: String){
        val doEnglishHomeworkBuilder=JumpDoEnglishHomeworkBuilder()
                .setClassId(classId)
                .setLessonId(lesson?.lessonId)
                .setLessonName(lesson?.lessonName)
                .setNum(lesson?.num)
                .setFlag(lesson?.englishFlag)
        EnglishHomeworkOptions.instance
                .with(context)
                .pad(pageId)
                .start(doEnglishHomeworkBuilder)
    }

    /**
     * 弹框（刪除重交）
     */
    private fun showAlertDialog(lesson: Lesson, context: Activity, jumpDoHomeworkBuilder: JumpDoHomeworkBuilder) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("第" + lesson.num + "讲有更新,需要重新提交,将额外补偿您30积分")
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(true)
        //设置正面按钮
        builder.setPositiveButton("去提交") { dialog, _ ->
            toDoHomework(context, lesson ,jumpDoHomeworkBuilder)
            dialog.dismiss()
        }
        val dialog = builder.create()
        //显示对话框
        dialog.show()
    }

    /**
     * 判断跳转答题卡/交作业
     */
    private fun requestHomeworkFloatLayer(lesson: Lesson?, classId: String? , context: Activity, jumpDoHomeworkBuilder: JumpDoHomeworkBuilder) {
        if ((lesson?.haveAnswerCount ?: 0) >= LangUtil.parseInt(lesson?.questionsCount)) {
            //题目已经答完(跳转答题卡页面)
            HomeworkOptions.instance
                    .with(context)
                    .pad(pageId)
                    .start(
                            JumpDoAnswerCardBuilder()
                                    .setClassId(classId)
                                    .setLessonId(lesson?.lessonId)
                                    .setFlag(lesson?.flag)
                                    .setLessonTitle(lesson?.lessonName)
                                    .setSource("2")//1 来自交作业页面 2 来自列表
                                    .setAction("0") //交作业动作 0 正常sdk 交作业 1 扫码sdk交作业
                    )
        } else {
            toDoHomework(context, lesson ,jumpDoHomeworkBuilder)
        }
    }

    /**
     * 去做作业
     */
    private fun toDoHomework(context: Activity, lesson: Lesson? ,jumpDoHomeworkBuilder: JumpDoHomeworkBuilder) {
//        if(null == lesson?.aiHomeworkCorrection || 0 == lesson?.aiHomeworkCorrection) {
            HomeworkOptions.instance
                    .with(context)
                    .pad(pageId)
                    .start(jumpDoHomeworkBuilder)
//        }else {
//            DialogUtil.getInstance().create(context, R.layout.aicorrection_dialog)
//                    .show(object : AbsAdapter() {
//                        override fun bindListener(onClickListener: View.OnClickListener) {
//                            bindText(R.id.tvTips, "新上线拍照批改功能，拍照纸质作业可一键判对错，来试试拍照批改吧~")
//                            bindText(R.id.tvCancel, "取消")
//                            bindText(R.id.tvConfirm, "拍照批改")
//                            this.bindListener(onClickListener, R.id.tvCancel)
//                            this.bindListener(onClickListener, R.id.tvConfirm)
//                        }
//
//                        override fun onClick(v: View, dialog: DialogUtil) {
//                            super.onClick(v, dialog)
//                            when (v.id) {
//                                R.id.tvConfirm -> {
//                                    dialog.dismiss()
//                                    var url = "axx://aiCorrection?classId=%s&lessonId=%s&flag=%s&lessonName=%s&haveAnswerCount=%s&questionsCount=%s&num=%s&action=%s"
//                                    url = String.format(url , jumpDoHomeworkBuilder.options.classId
//                                            , jumpDoHomeworkBuilder.options.lessonId
//                                            , jumpDoHomeworkBuilder.options.flag
//                                            , jumpDoHomeworkBuilder.options.lessonTitle
//                                            , lesson?.haveAnswerCount
//                                            , lesson?.questionsCount
//                                            , lesson?.num
//                                            , "1")
//                                    SchemeDispatcher.jumpPage(context, url)
//                                    GSLogUtil.collectClickLog(GSConstants.P?.getCurrRefer(), "XSD_261", "")
//                                }
//                                R.id.tvCancel -> {
//                                    dialog.dismiss()
//                                    HomeworkOptions.instance
//                                            .with(context)
//                                            .pad(pageId)
//                                            .start(jumpDoHomeworkBuilder)
//                                    GSLogUtil.collectClickLog(GSConstants.P?.getCurrRefer(), "XSD_260", "")
//                                }
//                            }
//                        }
//
//                    })
//            GSLogUtil.collectPageLog(StatisticsApi.aiCorrectionTakePhotoDialog, GSConstants.P?.getPreviousRefer())
//            GSConstants.P?.setCurrRefer(StatisticsApi.aiCorrectionTakePhotoDialog)
//        }
    }

    /**
     * 检查三阶课是否激活回调
     */
    interface OnCheckCourseActivationListener {
        fun onCheckCourseStatus(status: Int , msg: String)
    }

}
