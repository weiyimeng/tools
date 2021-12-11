package gaosi.com.learn.studentapp.classlesson

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gaosi.englishhomework.jump.EnglishHomeworkOptions
import com.gaosi.homework.jump.HomeworkOptions
import com.gaosi.homework.jump.JumpDoClassVideoBuilder
import com.gaosi.homework.jump.JumpDoDTReportBuilder
import com.gaosi.homework.jump.JumpDoLearnReportBuilder
import com.gaosi.preclass.jump.JumpDoPreExerciseBuilder
import com.gaosi.preclass.jump.JumpDoPreVideoBuilder
import com.gaosi.preclass.jump.PreExerciseOptions
import com.gaosi.specialcourse.jump.JumpDoSpecialCourseBuilder
import com.gaosi.specialcourse.jump.SpecialCourseOptions
import com.gaosi.teacheronline.TeacherOnLineApi
import com.gaosi.teacheronline.entity.PlayBackBean
import com.gaosi.teacheronline.entity.PreviewBean
import com.gaosi.teacheronline.jump.JumpDoPlayBackBuilder
import com.gaosi.teacheronline.jump.JumpDoShowPPTBuilder
import com.gaosi.teacheronline.jump.TeacherOnLineOptions
import com.gsbaselib.InitBaseLib
import com.gsbaselib.base.log.LogUtil
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.net.callback.GSStringCallback
import com.gsbaselib.utils.DateUtil
import com.gsbaselib.utils.LOGGER
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.base.STBaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.SystemUtil
import com.gstudentlib.util.entity.PlayType
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import gaosi.com.learn.application.AppApi
import gaosi.com.learn.bean.classlesson.lessondetail.ForeignCourse
import gaosi.com.learn.bean.raw.Lesson
import gaosi.com.learn.studentapp.classlesson.status.CourseTypeId
import gaosi.com.learn.view.CountDownTimeTextView
import kotlinx.android.synthetic.main.item_fragment_lession_detail2.*
import kotlinx.android.synthetic.main.item_fragment_lession_detail2.ivClassReportIcon
import kotlinx.android.synthetic.main.item_fragment_lession_detail2.llHomeworkStar
import kotlinx.android.synthetic.main.item_fragment_lession_detail2.rlHandout
import kotlinx.android.synthetic.main.item_fragment_lession_detail2.tvClassReportName
import kotlinx.android.synthetic.main.item_fragment_lession_detail2.tvClassReportRatio
import kotlinx.android.synthetic.main.item_fragment_lession_detail2.tvClassReportStatus
import kotlinx.android.synthetic.main.item_fragment_lession_detail2.tvHomeworkFlag
import kotlinx.android.synthetic.main.item_fragment_lession_detail2.tvHomeworkStatus
import kotlinx.android.synthetic.main.item_fragment_lession_detail2.tvLessonName
import kotlinx.android.synthetic.main.item_fragment_lession_detail2.tvSpecialClassFlag
import kotlinx.android.synthetic.main.item_fragment_lession_detail2.tvSpecialClassStatus
import org.json.JSONObject
import java.lang.Exception
import kotlin.collections.HashMap

/**
 * 课程学习列表
 */
class LessonDetailFragment : BaseLessonDetailFragment() {

    companion object {
        fun instance(): LessonDetailFragment {
            return LessonDetailFragment()
        }
    }

    override fun showData() {
        super.showData()
        mLessonDetailBean?.let {
            showLive()
            showEnglishExercise()
            if (it.subjectId == 3 || it.subjectId == 4) {
                showSpecialClass2()
            } else {
                showSpecialClass()
            }
            showHomework()
            showEnglish()
            showAiEnglishRecite()
            showClassVideo()
            showHandout()
            showClassReport()
            showSiGaoEnglish()
        }
    }

    /**
     * 显示直播
     */
    private fun showLive() {
        mLessonDetailBean?.let {
            tvTimeTitle.text = "上课时间"
            tvTimeTitle.visibility = View.VISIBLE
            tvTime.visibility = View.VISIBLE
            rlLive.visibility = View.VISIBLE
            when(it.courseTypeId) {
                CourseTypeId.SMALL_CLASS_ONLINE -> { //在线小班课 TSM
                    val liveBean = it.axxOnline
                    if(liveBean == null) {
                        rlLive.visibility = View.GONE
                        return
                    }
                    val beginHHmmTimeString = DateUtil.longToString(liveBean?.lessonBeginTime ?: 0 , "HH:mm")
                    val endHHmmTimeString = DateUtil.longToString(liveBean?.lessonEndTime ?: 0 , "HH:mm")
                    tvTime.text = "$beginHHmmTimeString-$endHHmmTimeString"
                    when (liveBean?.lessonStatus) {//0-未开放，1-未开课，2-马上开课，3-正在直播，4-直播完成回放生成中，5-回放已生成
                        1 -> {
                            setUnStartStatus("直播未开始")
                        }
                        2 -> {
                            mIsPlayBack = false
                            tvTimeTitle.text = "距离上课"
                            tvLessonStatus.visibility = View.GONE
                            llLiving.visibility = View.VISIBLE
                            Glide.with(activity!!).asGif().load(R.drawable.app_icon_living).into(ivLiveStatus)
                            tvLiveStatus.text = "进入直播"
                            val elapseTime = liveBean.lessonBeginTime?.minus(System.currentTimeMillis())
                                    ?: 0L
                            tvTime.start(elapseTime, object : CountDownTimeTextView.OnFinishListener {
                                override fun onFinish() {
                                    if (activity == null || activity?.isFinishing == true) { //修复页面退出后可能存在tvTime无引用的情况
                                        return
                                    }
                                    tvTime.stop()
                                    tvTimeTitle.text = "上课时间"
                                    tvTime.text = "$beginHHmmTimeString-$endHHmmTimeString"
                                }
                            })
                            tvTime.start()
                        }
                        3 -> setLivingStatus()
                        4 -> setPlayBackingStatus()
                        5 -> setPlayBackStatus()
                    }
                }
                CourseTypeId.DT_LIVE -> {//双师课 CTS
                    val liveBean = it.dtLive
                    if(liveBean == null) {
                        rlLive.visibility = View.GONE
                        return
                    }
                    val beginHHmmTimeString = DateUtil.longToString(liveBean?.lessonBeginTime ?: 0 , "HH:mm")
                    val endHHmmTimeString = DateUtil.longToString(liveBean?.lessonEndTime ?: 0 , "HH:mm")
                    tvTime.text = "$beginHHmmTimeString-$endHHmmTimeString"
                    when (liveBean?.lessonStatus) {
                        1 -> setUnStartStatus("双师课未开始")
                        2 -> {
                            mIsPlayBack = false
                            tvTimeTitle.text = "距离上课"
                            tvLessonStatus.visibility = View.GONE
                            llLiving.visibility = View.VISIBLE
                            Glide.with(activity!!).asGif().load(R.drawable.app_icon_living).into(ivLiveStatus)
                            tvLiveStatus.text = "进入直播"
                            val elapseTime = liveBean.lessonBeginTime?.minus(System.currentTimeMillis())
                                    ?: 0L
                            tvTime.start(elapseTime, object : CountDownTimeTextView.OnFinishListener {
                                override fun onFinish() {
                                    if (activity == null || activity?.isFinishing == true) { //修复页面退出后可能存在tvTime无引用的情况
                                        return
                                    }
                                    tvTime.stop()
                                    tvTimeTitle.text = "上课时间"
                                    tvTime.text = "$beginHHmmTimeString-$endHHmmTimeString"
                                }
                            })
                            tvTime.start()
                        }
                        3 -> setLivingStatus()
                        4 -> setPlayBackingStatus()
                        5 -> setPlayBackStatus()
                    }
                }
                CourseTypeId.AI_DT_LIVE -> {//AI好课 AIT
                    val liveBean = it.dtLive
                    if(liveBean == null) {
                        rlLive.visibility = View.GONE
                        return
                    }
                    if(liveBean?.lessonBeginTime == null || liveBean?.lessonEndTime == null) {
                        tvTime.text = "暂未开课"
                    }else {
                        tvTime.visibility = View.VISIBLE
                        val beginHHmmTimeString = DateUtil.longToString(liveBean?.lessonBeginTime ?: 0 , "HH:mm")
                        val endHHmmTimeString = DateUtil.longToString(liveBean?.lessonEndTime ?: 0 , "HH:mm")
                        tvTime.text = "$beginHHmmTimeString-$endHHmmTimeString"
                    }
                    when (liveBean?.lessonStatus) {
                        1 -> setUnStartStatus("AI好课未开始")
                        2 -> {
                            tvTimeTitle.text = "即将开课"
                            tvLessonStatus.visibility = View.GONE
                            llLiving.visibility = View.VISIBLE
                            Glide.with(activity!!).asGif().load(R.drawable.app_icon_living).into(ivLiveStatus)
                            tvLiveStatus.text = "进入直播"
                        }
                        3 -> setLivingStatus()
                        4 -> setPlayBackingStatus()
                        5 -> setPlayBackStatus()
                    }
                }
                CourseTypeId.OFFLINE_CLASS -> {//线下课 TSM
                    val liveBean = it.axxOffline
                    if(liveBean == null) {
                        rlLive.visibility = View.GONE
                        return
                    }
                    val beginHHmmTimeString = DateUtil.longToString(liveBean?.lessonBeginTime ?: 0 , "HH:mm")
                    val endHHmmTimeString = DateUtil.longToString(liveBean?.lessonEndTime ?: 0 , "HH:mm")
                    tvTime.text = "$beginHHmmTimeString-$endHHmmTimeString"
                    when (liveBean?.lessonStatus) {
                        1 -> setUnStartStatus("线下课未开始")
                        3 -> setUnStartStatus("线下课进行中")
                        6 -> {
                            tvLessonStatus.text = "线下课已结束"
                            tvLessonStatus.setTextColor(Color.parseColor("#939CB6"))
                            tvLessonStatus.visibility = View.VISIBLE
                            llLiving.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    /**
     * 课前预习
     */
    private fun showEnglishExercise() {
        mLessonDetailBean?.let { it ->
            if (null == it.englishPreview) {
                rlPreStudy.visibility = View.GONE
            } else {
                this.showTask()
                rlPreStudy.visibility = View.VISIBLE
                tvPreStudyGold.visibility = View.GONE
                //未开放-0，去预习-10，已完成-100
                when (it.englishPreview?.processStatus) {
                    10 -> {
                        tvPreStudyStatus.text = "未开放"
                        tvPreStudyStatus.setTextColor(Color.parseColor("#A3B3C2"))
                        tvPreStudyStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                    }
                    20 -> {
                        tvPreStudyStatus.text = "去预习"
                        tvPreStudyStatus.setTextColor(Color.parseColor("#FFFFFF"))
                        tvPreStudyStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                        it.englishPreview?.goldCoinsNum?.let {
                            tvPreStudyGold.visibility = View.VISIBLE
                            tvPreStudyGold.text = "+ $it"
                        }
                    }
                    100 -> {
                        tvPreStudyStatus.text = "已完成"
                        tvPreStudyStatus.setTextColor(Color.parseColor("#A3B3C2"))
                        tvPreStudyStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                    }
                }
                if ((it.status == 3 || it.status == 5) && it.englishPreview?.operationalStatus != 100) {
                    tvPreStudyStatus.text = "未开放"
                    tvPreStudyStatus.setTextColor(Color.parseColor("#A3B3C2"))
                    tvPreStudyStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                }
                if (it.englishPreview?.operationalStatus == 30) {
                    tvPreStudyStatus.text = "已过期"
                    tvPreStudyStatus.setTextColor(Color.parseColor("#A3B3C2"))
                    tvPreStudyStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                }
            }
            rlPreStudy.setOnClickListener {
                when (mLessonDetailBean?.englishPreview?.operationalStatus) {
                    0 -> {
                        if (mLessonDetailBean?.num == 1) {
                            ToastUtil.showToast("课前预习暂未开放")
                        } else {
                            ToastUtil.showToast("课前预习暂未开放")
                        }
                        return@setOnClickListener
                    }
                    30 -> {
                        ToastUtil.showToast("有效期到期")
                        return@setOnClickListener
                    }
                    20 -> {
                        ToastUtil.showToast("已退班，不能看预习")
                        return@setOnClickListener
                    }
                }
                LessonDetailPresenter.checkCourseActivation(mLessonDetailBean?.classId.toString()
                        , mLessonDetailBean?.lessonId.toString(), 1 , mLessonDetailBean?.isActivation?:1 ,object : LessonDetailPresenter.OnCheckCourseActivationListener {
                    override fun onCheckCourseStatus(status: Int, msg: String) {
                        if(1 == status) {
                            mLessonDetailBean?.isActivation = 1 //已经激活的情况下本地赋值更换
                            mLessonDetailBean?.let {
                                if(it.englishPreview?.processStatus == 20) {//去预习
                                    collectClickEvent("as103_clk_go_preview" , "0")
                                }else if(it.englishPreview?.processStatus == 100) {//已完成
                                    collectClickEvent("as103_clk_go_preview" , "1")
                                }
                            }
                            // 1：旧 2：单词 3：都有，需要弹框
                            when (mLessonDetailBean?.englishPreview?.preview) {
                                1 -> goPlay()
                                2 -> goRead()
                                3 -> showPreviewDialog()
                            }
                        }else {
                            ToastUtil.showToast(msg)
                        }
                    }
                })
            }
        }
    }

    /**
     * 英语预习执行播放
     */
    private fun goPlay() {
        if (mLessonDetailBean?.englishPreview?.resourceType == 0) {
            PreExerciseOptions.instance
                    .with(activity)
                    .pad(pageId)
                    .start(
                            JumpDoPreExerciseBuilder()
                                    .setUrl(mLessonDetailBean?.englishPreview?.resourceUrl)
                                    .setClassId(mLessonDetailBean?.classId.toString())
                                    .setLessonId(mLessonDetailBean?.lessonId.toString())
                    )
        } else {
            val title = "${tvLessonName.text}"
            PreExerciseOptions.instance
                    .with(activity)
                    .pad(pageId)
                    .start(
                            JumpDoPreVideoBuilder()
                                    .setUrl(mLessonDetailBean?.englishPreview?.resourceUrl)
                                    .setTitle(title)
                                    .setClassId(mLessonDetailBean?.classId.toString())
                                    .setLessonId(mLessonDetailBean?.lessonId.toString())
                                    .setPlayType(PlayType.preEnglishExerciseVideo.toString())

                    )
        }
    }

    /**
     * 英语预习走read
     */
    private fun goRead() {
        EnglishHomeworkOptions.instance
                .with(activity)
                .pad(pageId)
                .start(STBaseConstants.userId
                        , mLessonDetailBean?.classId.toString()
                        , mLessonDetailBean?.lessonId.toString())
    }

    /**
     * 英语预习弹窗
     */
    private fun showPreviewDialog() {
        DialogUtil.getInstance().create(activity, R.layout.ui_preview_dialog, true, true)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener) {
                        bindListener(onClickListener, R.id.tv_play)
                        bindListener(onClickListener, R.id.tv_read)
                    }

                    override fun onClick(v: View, dialog: DialogUtil) {
                        super.onClick(v, dialog)
                        when (v.id) {
                            R.id.tv_play -> {
                                goPlay()
                                dialog.dismiss()
                            }
                            R.id.tv_read -> {
                                goRead()
                                dialog.dismiss()
                            }
                        }
                    }
                })
    }

    /**
     * 在线外教
     */
    private fun showSiGaoEnglish() {
        mLessonDetailBean?.let {
            if (it.havaForeignCourse == 1) {
                if (null == it.foreignCourse) {
                    rlPreCourseware.visibility = View.GONE
                    rlForeiginPlayback.visibility = View.GONE
                } else {
                    when (it.foreignCourse?.processStatus) {
                        //未预约
                        30 -> {
                            rlPreCourseware.visibility = View.GONE
                            rlForeiginPlayback.visibility = View.GONE
                        }
                        //预习
                        40 -> {
                            showPreCourse(it.foreignCourse)
                        }
                        //上课
                        50 -> {
                            showPreCourse(it.foreignCourse)
                            rlForeiginPlayback.visibility = View.GONE
                        }
                        //看回放
                        100 -> {
                            showPreCourse(it.foreignCourse)
                            this.showLearned()
                            rlForeiginPlayback.visibility = View.VISIBLE
                            tvForeiginPlaybackIconStatus.text = "看回放"
                            tvForeiginPlaybackIconStatus.setTextColor(Color.parseColor("#FFFFFF"))
                            tvForeiginPlaybackIconStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                            rlForeiginPlayback.setOnClickListener {
                                requestSiGaoRecord()
                            }
                        }
                    }
                }
            } else {
                rlPreCourseware.visibility = View.GONE
                rlForeiginPlayback.visibility = View.GONE
            }
        }
    }

    /**
     * 展示预习课件
     * processStatus==40||50||100 展示预习课件，当processStatus==40&&previewStatus==0显示：去预习，其他显示：去查看
     * processStatus==100 同时展示本讲收获
     */
    private fun showPreCourse(data: ForeignCourse?) {
        this.showTask()
        rlPreCourseware.visibility = View.VISIBLE
        tvPreCourseware.visibility = View.GONE
        if (data?.previewStatus == 0 && data.processStatus == 40) {
            tvPreCoursewareStatus.text = "去预习"
            data.goldCoinsNum?.let {
                tvPreCourseware.visibility = View.VISIBLE
                tvPreCourseware.text = "+ $it"
            }
        } else {
            tvPreCoursewareStatus.text = "去查看"
        }

        tvPreCoursewareStatus.setTextColor(Color.parseColor("#FFFFFF"))
        tvPreCoursewareStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)

        rlPreCourseware.setOnClickListener {
            requestSiGaoPreview()
        }
    }

    /**
     * 获取外教预习内容
     */
    private fun requestSiGaoPreview() {
        (activity as STBaseActivity).showLoadingProgressDialog()
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["classId"] = mLessonDetailBean?.classId.toString()
        paramMap["lessonNum"] = mLessonDetailBean?.num.toString()
        paramMap["lessonId"] = mLessonDetailBean?.lessonId.toString()
        GSRequest.startRequest(TeacherOnLineApi.getSiGaoPreview, GSRequest.GET, paramMap, object : GSJsonCallback<PreviewBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<PreviewBean>) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                (activity as STBaseActivity).dismissLoadingProgressDialog()
                if (result.status == 1) {
                    if (result.body.url.isNullOrEmpty()) {
                        ToastUtil.showToast("暂无课件")
                    } else {
                        val builder: JumpDoShowPPTBuilder
                        if (mLessonDetailBean?.foreignCourse?.previewStatus == 0 && mLessonDetailBean?.foreignCourse?.processStatus == 40) {
                            builder = JumpDoShowPPTBuilder()
                                    .setUrl(result.body.url)
                                    .setClassId(mLessonDetailBean?.classId.toString())
                                    .setLessonId(mLessonDetailBean?.lessonId.toString())
                                    .setGold(mLessonDetailBean?.foreignCourse?.goldCoinsNum)
                            collectClickEvent("XSD_569")
                        } else {
                            builder = JumpDoShowPPTBuilder()
                                    .setUrl(result.body.url)
                                    .setClassId(mLessonDetailBean?.classId.toString())
                                    .setLessonId(mLessonDetailBean?.lessonId.toString())
                            collectClickEvent("XSD_609")
                        }
                        TeacherOnLineOptions.instance
                                .with(activity)
                                .pad(pageId)
                                .start(builder)
                    }
                } else {
                    ToastUtil.showToast("获取预习地址失败")
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                (activity as STBaseActivity).dismissLoadingProgressDialog()
                message ?: return
                ToastUtil.showToast(message)
            }
        })
    }

    /**
     * 获取外教回放信息
     */
    private fun requestSiGaoRecord() {
        (activity as STBaseActivity).showLoadingProgressDialog()
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["fbClassId"] = mLessonDetailBean?.foreignCourse?.fbClassId.toString()
        GSRequest.startRequest(TeacherOnLineApi.getSiGaoRecord, GSRequest.GET, paramMap, object : GSJsonCallback<PlayBackBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<PlayBackBean>) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                (activity as STBaseActivity).dismissLoadingProgressDialog()
                if (result.status == 1) {
                    if (result.body.status == 1) {
                        collectClickEvent("XSD_570")
                        TeacherOnLineOptions.instance
                                .with(activity)
                                .pad(pageId)
                                .start(
                                        JumpDoPlayBackBuilder()
                                                .setUrl(result.body.content)
                                                .setTitle(mLessonDetailBean?.lessonName.toString())
                                                .setClassId(mLessonDetailBean?.classId.toString())
                                                .setLessonId(mLessonDetailBean?.lessonId.toString())
                                )
                    } else {
                        ToastUtil.showToast("视频还未生成哦！")
                    }
                } else {
                    ToastUtil.showToast("获取回放地址失败")
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                (activity as STBaseActivity).dismissLoadingProgressDialog()
                message ?: return
                ToastUtil.showToast(message)
            }
        })
    }

    /**
     * 其他学科使用的专题课
     */
    private fun showSpecialClass() {
        mLessonDetailBean?.let {
            if (null == it.specialCourse) {
                rlSpecialClass.visibility = View.GONE
            } else {
                this.showTask()
                rlSpecialClass.visibility = View.VISIBLE
                tvSpecialClassFlag.visibility = View.GONE
                tvSpecialClassGold.visibility = View.GONE
                //未开放-0，去上课-20
                when (it.specialCourse?.processStatus) {
                    10 -> {
                        tvSpecialClassStatus.text = "未开放"
                        tvSpecialClassStatus.setTextColor(Color.parseColor("#A3B3C2"))
                        tvSpecialClassStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                    }
                    20 -> {
                        tvSpecialClassStatus.text = "去上课"
                        tvSpecialClassStatus.setTextColor(Color.parseColor("#FFFFFF"))
                        tvSpecialClassStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                        it.specialCourse?.goldCoinsNum?.let {
                            tvSpecialClassGold.visibility = View.VISIBLE
                            tvSpecialClassGold.text = "+ $it"
                        }
                    }
                    100 -> {
                        if (it.specialCourse?.hasNewVersion == 1) {
                            tvSpecialClassFlag.visibility = View.VISIBLE
                        }
                        tvSpecialClassStatus.text = "已完成"
                        tvSpecialClassStatus.setTextColor(Color.parseColor("#A3B3C2"))
                        tvSpecialClassStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                    }
                }
                if ((it.status == 3 || it.status == 5) && it.specialCourse?.operationalStatus != 100) {
                    tvSpecialClassStatus.text = "未开放"
                    tvSpecialClassStatus.setTextColor(Color.parseColor("#A3B3C2"))
                    tvSpecialClassStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                }
                if (it.specialCourse?.operationalStatus == 30) {
                    tvSpecialClassStatus.text = "已过期"
                    tvSpecialClassStatus.setTextColor(Color.parseColor("#A3B3C2"))
                    tvSpecialClassStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                }
            }
        }
        rlSpecialClass.setOnClickListener {
            when (mLessonDetailBean?.specialCourse?.operationalStatus) {
                0 -> {
                    ToastUtil.showToast("暂未开放，本讲结课后开放")
                    return@setOnClickListener
                }
                30 -> {
                    ToastUtil.showToast("有效期到期")
                    return@setOnClickListener
                }
                20 -> {
                    ToastUtil.showToast("已退班，不能看专题课")
                    return@setOnClickListener
                }
            }
            LessonDetailPresenter.checkCourseActivation(mLessonDetailBean?.classId.toString()
                    , mLessonDetailBean?.lessonId.toString(), 2 ,mLessonDetailBean?.isActivation?:1 ,object : LessonDetailPresenter.OnCheckCourseActivationListener {
                override fun onCheckCourseStatus(status: Int, msg: String) {
                    if(1 == status) {
                        mLessonDetailBean?.isActivation = 1 //已经激活的情况下本地赋值更换
                        mLessonDetailBean?.let {
                            if(it.specialCourse?.processStatus == 20) {//未完课
                                collectClickEvent("as103_clk_go_class" , "0")
                                gotoSpecialCoursePage(it.specialCourse?.hasNewVersion.toString())

                            }else if(it.specialCourse?.processStatus == 100) {//已完成
                                collectClickEvent("as103_clk_go_class" , "1")
                                if (it.specialCourse?.hasNewVersion == 1) {
                                    showSpecialClassUpdateDialog()
                                    GSLogUtil.collectPageLog("as620", mPageId)
                                } else {
                                    gotoSpecialCoursePage("0")
                                }
                            }
                        }

                    }else {
                        ToastUtil.showToast(msg)
                    }
                }
            })
        }
    }

    /**
     * 英语和语文使用的专题课
     */
    private fun showSpecialClass2() {
        mLessonDetailBean?.let {
            if (null == it.specialCourse) {
                rlSpecialClass2.visibility = View.GONE
            } else {
                this.showTask()
                rlSpecialClass2.visibility = View.VISIBLE
                tvSpecialClassFlag2.visibility = View.GONE
                tvSpecialClassGold2.visibility = View.GONE
                //未开放-0，去上课-20
                when (it.specialCourse?.processStatus) {
                    10 -> {
                        tvSpecialClassStatus2.text = "未开放"
                        tvSpecialClassStatus2.setTextColor(Color.parseColor("#A3B3C2"))
                        tvSpecialClassStatus2.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                    }
                    20 -> {
                        tvSpecialClassStatus2.text = "去上课"
                        tvSpecialClassStatus2.setTextColor(Color.parseColor("#FFFFFF"))
                        tvSpecialClassStatus2.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                        it.specialCourse?.goldCoinsNum?.let {
                            tvSpecialClassGold2.visibility = View.VISIBLE
                            tvSpecialClassGold2.text = "+ $it"
                        }
                    }
                    100 -> {
                        if (it.specialCourse?.hasNewVersion == 1) {
                            tvSpecialClassFlag2.visibility = View.VISIBLE
                        }
                        tvSpecialClassStatus2.text = "已完成"
                        tvSpecialClassStatus2.setTextColor(Color.parseColor("#A3B3C2"))
                        tvSpecialClassStatus2.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                    }
                }
                if ((it.status == 3 || it.status == 5) && it.specialCourse?.operationalStatus != 100) {
                    tvSpecialClassStatus2.text = "未开放"
                    tvSpecialClassStatus2.setTextColor(Color.parseColor("#A3B3C2"))
                    tvSpecialClassStatus2.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                }
                if (it.specialCourse?.operationalStatus == 30) {
                    tvSpecialClassStatus2.text = "已过期"
                    tvSpecialClassStatus2.setTextColor(Color.parseColor("#A3B3C2"))
                    tvSpecialClassStatus2.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                }
            }
        }
        rlSpecialClass2.setOnClickListener {
            when (mLessonDetailBean?.specialCourse?.operationalStatus) {
                0 -> {
                    ToastUtil.showToast("暂未开放，本讲结课后开放")
                    return@setOnClickListener
                }
                30 -> {
                    ToastUtil.showToast("有效期到期")
                    return@setOnClickListener
                }
                20 -> {
                    ToastUtil.showToast("已退班，不能看专题课")
                    return@setOnClickListener
                }
            }
            LessonDetailPresenter.checkCourseActivation(mLessonDetailBean?.classId.toString()
                    , mLessonDetailBean?.lessonId.toString(), 2, mLessonDetailBean?.isActivation
                    ?: 1, object : LessonDetailPresenter.OnCheckCourseActivationListener {
                override fun onCheckCourseStatus(status: Int, msg: String) {
                    if (1 == status) {
                        mLessonDetailBean?.isActivation = 1 //已经激活的情况下本地赋值更换
                        mLessonDetailBean?.let {
                            if (it.specialCourse?.processStatus == 20) {//未完课
                                collectClickEvent("as103_clk_go_class", "0")
                                gotoSpecialCoursePage(it.specialCourse?.hasNewVersion.toString())

                            } else if (it.specialCourse?.processStatus == 100) {//已完成
                                collectClickEvent("as103_clk_go_class", "1")
                                if (it.specialCourse?.hasNewVersion == 1) {
                                    showSpecialClassUpdateDialog()
                                    GSLogUtil.collectPageLog("as620", mPageId)
                                } else {
                                    gotoSpecialCoursePage("0")
                                }
                            }
                        }

                    } else {
                        ToastUtil.showToast(msg)
                    }
                }
            })
        }
    }

    /**
     * 跳转专题课
     * @param useNewVersion 是否使用新版本视频 0不使用 1使用
     */
    private fun gotoSpecialCoursePage(useNewVersion: String?){
        val doSpecialCourseBuilder = JumpDoSpecialCourseBuilder()
                .setStudentId(STBaseConstants.userId)
                .setClassId(mLessonDetailBean?.classId.toString())
                .setLessonId(mLessonDetailBean?.lessonId.toString())
                .setActionType("0")
                .setUseNewVersion(useNewVersion)
        activity?.let {
            SpecialCourseOptions.instance
                .with(it)
                .pad(pageId)
                .start(doSpecialCourseBuilder)
        }
    }

    /**
     * 专题课视频更新弹框
     */
    private fun showSpecialClassUpdateDialog() {
        DialogUtil.getInstance().create(activity, R.layout.back_dialog)
                .show(object : AbsAdapter() {
                    override fun bindListener(onClickListener: View.OnClickListener) {
                        bindText(R.id.tvTips, "专题课视频更新啦，观看最新视频需要重新答题，是否更新并观看？")
                        val tvSubTips = findViewById<TextView>(R.id.tvSubTips)
                        tvSubTips.visibility = View.INVISIBLE
                        bindText(R.id.tvCancel, "取消")
                        bindText(R.id.tvConfirm, "立即更新")
                        this.bindListener(onClickListener, R.id.tvCancel)
                        this.bindListener(onClickListener, R.id.tvConfirm)
                    }

                    override fun onClick(v: View, dialog: DialogUtil) {
                        super.onClick(v, dialog)
                        when (v.id) {
                            R.id.tvConfirm -> {
                                dialog.dismiss()
                                gotoSpecialCoursePage("1")
                                GSLogUtil.collectClickLog("as620", "as620_clk_update_immediately", "")
                            }
                            R.id.tvCancel -> {
                                dialog.dismiss()
                                gotoSpecialCoursePage("0")
                                GSLogUtil.collectClickLog("as620", "as620_clk_cancel", "")
                            }
                        }
                    }
                })
    }

    /**
     * 显示自我巩固
     */
    private fun showHomework() {
        mLessonDetailBean?.let {
            if (null != it.lessonHomeworkInfo) {
                if (it.lessonHomeworkInfo?.questionsCount != 0) {
                    //配置是否重交重判
                    when (it.lessonHomeworkInfo?.flag) {
                        2 -> {
                            tvHomeworkFlag.visibility = View.VISIBLE
                            tvHomeworkFlag.text = "已重判"
                            tvHomeworkFlag.setTextColor(Color.parseColor("#3AAFFF"))
                            tvHomeworkFlag.setBackgroundResource(R.drawable.app_bg_detail_lesson_retribution)
                        }
                        1 -> {
                            tvHomeworkFlag.visibility = View.VISIBLE
                            tvHomeworkFlag.text = "有更新"
                            tvHomeworkFlag.setTextColor(Color.parseColor("#52CC52"))
                            tvHomeworkFlag.setBackgroundResource(R.drawable.app_bg_detail_lesson_update)
                        }
                        else -> {
                            tvHomeworkFlag.visibility = View.GONE
                        }
                    }
                    //星星个数
                    val star = if (it.lessonHomeworkInfo?.processStatus == 10) {
                        -1
                    } else {
                        it.lessonHomeworkInfo?.stars
                    }
                    ivHomeworkStar1.setImageResource(R.drawable.app_icon_lessondetail_star_gray)
                    ivHomeworkStar2.setImageResource(R.drawable.app_icon_lessondetail_star_gray)
                    ivHomeworkStar3.setImageResource(R.drawable.app_icon_lessondetail_star_gray)
                    when (star) {
                        1 -> {
                            ivHomeworkStar1.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                        }
                        2 -> {
                            ivHomeworkStar1.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                            ivHomeworkStar2.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                        }
                        3 -> {
                            ivHomeworkStar1.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                            ivHomeworkStar2.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                            ivHomeworkStar3.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                        }
                    }
                    //按钮提交状态
                    tvHomeworkGold.visibility = View.GONE
                    when (star) {
                        -1 -> {
                            tvHomeworkStatus.text = "未开放"
                            tvHomeworkStatus.setTextColor(Color.parseColor("#A3B3C2"))
                            tvHomeworkStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                        }
                        0 -> {
                            tvHomeworkStatus.text = "去提交"
                            tvHomeworkStatus.setTextColor(Color.parseColor("#FFFFFF"))
                            tvHomeworkStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                            it.lessonHomeworkInfo?.goldCoinsNum?.let {
                                tvHomeworkGold.visibility = View.VISIBLE
                                tvHomeworkGold.text = "+ $it"
                            }
                        }
                        3 -> {
                            tvHomeworkStatus.text = "去查看"
                            tvHomeworkStatus.setTextColor(Color.parseColor("#FFFFFF"))
                            tvHomeworkStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                        }
                        else -> {
                            if (mLessonDetailBean?.isActivation == 1) {
                                tvHomeworkStatus.text = "去订正"
                            } else {
                                tvHomeworkStatus.text = "去查看"
                            }
                            tvHomeworkStatus.setTextColor(Color.parseColor("#FFFFFF"))
                            tvHomeworkStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                        }
                    }
                    tvHomeworkStatus.visibility = View.VISIBLE
                    llHomeworkStar.visibility = View.VISIBLE
                } else {
                    llHomeworkStar.visibility = View.GONE
                    tvHomeworkFlag.visibility = View.GONE
                    tvHomeworkStatus.visibility = View.GONE
                    tvHomeworkGold.visibility = View.GONE
                }
                this.showTask()
                rlHomework.visibility = View.VISIBLE
            } else {
                rlHomework.visibility = View.GONE
                tvHomeworkFlag.visibility = View.GONE
                llHomeworkStar.visibility = View.GONE
                tvHomeworkStatus.visibility = View.GONE
                tvHomeworkGold.visibility = View.GONE
            }
        }
        rlHomework.setOnClickListener {
            setHomeworkClickListener()
        }
    }

    /**
     * 自我巩固点击事件
     * 已退班或者已经结课的讲次不能再次去交作业
     */
    private fun setHomeworkClickListener() {
        mLessonDetailBean?.let {
            if (null == it.lessonHomeworkInfo) {
                return
            }
            //0.未开课，1.开课, 2.结课未超过7天，3.已退班未超过7天，4.结课超过7天，5.退班超过7天
            when (it.lessonHomeworkInfo?.operationalStatus) {
                0 -> {
                    ToastUtil.showToast("暂未开放，本讲结课后开放")
                    return
                }
                20 -> {
                    ToastUtil.showToast("已退班")
                    return
                }
                30 -> {
                    ToastUtil.showToast("有效期到期")
                    return
                }
            }
            if (0 == it.lessonHomeworkInfo?.submitStar && 4 == it.status) { //0.未开课，1.开课, 2.结课未超过7天，3.已退班未超过7天，4.结课超过7天，5.退班超过7天
                ToastUtil.showToast("已结课60天，不能再提交")
                return
            } else if (0 == it.lessonHomeworkInfo?.submitStar && 5 == it.status) {
                ToastUtil.showToast("已退班，不能再提交")
                return
            }
            val lesson = Lesson()
            lesson.lessonId = it.lessonId.toString()
            lesson.lessonName = it.lessonName
            lesson.flag = it.lessonHomeworkInfo?.flag.toString()
            lesson.num = it.num.toString()
            lesson.status = it.status ?: 1
            lesson.topicType = it.lessonHomeworkInfo?.topicType.toString()
            lesson.questionsCount = it.lessonHomeworkInfo?.questionsCount.toString()
            lesson.haveAnswerCount = it.lessonHomeworkInfo?.haveAnswerCount ?: 0
            lesson.myselfCorrect = it.lessonHomeworkInfo?.myselfCorrect.toString()
            lesson.stars = it.lessonHomeworkInfo?.stars.toString()
            lesson.aiHomeworkCorrection = it.lessonHomeworkInfo?.aiHomeworkCorrection?:0
            lesson.submitSource = it.lessonHomeworkInfo?.submitSource?:0
            lesson.classTypeId = it.classTypeId.toString()
            lesson.goldCoinsNum = it.lessonHomeworkInfo?.goldCoinsNum?: 0
            if (it.lessonHomeworkInfo?.stars is Int) {
                LessonDetailPresenter.setHomeworkOnclickListener(lesson, activity!!, it.classId.toString())
            } else {
                ToastUtil.showToast("数据错误")
            }
        }
    }

    /**
     * 显示口语练习
     */
    private fun showEnglish() {
        mLessonDetailBean?.let {
            if (null != it.englishSpeech) {
                if (it.englishSpeech?.englishQuestionsCount != 0) {
                    //配置是否重交重判
                    when (it.englishSpeech?.flag) {
                        1 -> {
                            tvEnglishOralPracticeFlag.visibility = View.VISIBLE
                            tvEnglishOralPracticeFlag.text = "有更新"
                        }
                        else -> {
                            tvEnglishOralPracticeFlag.visibility = View.GONE
                        }
                    }
                    //配置星星状态
                    ivEnglishOralPracticeStar1.setImageResource(R.drawable.app_icon_lessondetail_star_gray)
                    ivEnglishOralPracticeStar2.setImageResource(R.drawable.app_icon_lessondetail_star_gray)
                    ivEnglishOralPracticeStar3.setImageResource(R.drawable.app_icon_lessondetail_star_gray)
                    when (it.englishSpeech?.englishSpeechStar) {
                        1 -> {
                            ivEnglishOralPracticeStar1.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                        }
                        2 -> {
                            ivEnglishOralPracticeStar1.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                            ivEnglishOralPracticeStar2.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                        }
                        3 -> {
                            ivEnglishOralPracticeStar1.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                            ivEnglishOralPracticeStar2.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                            ivEnglishOralPracticeStar3.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                        }
                    }
                    //按钮提交状态
                    tvEnglishOralPracticeGold.visibility = View.GONE
                    when (it.englishSpeech?.englishSpeechStar) {
                        0 -> {
                            tvEnglishOralPracticeStatus.text = "去练习"
                            tvEnglishOralPracticeStatus.setTextColor(Color.parseColor("#FFFFFF"))
                            tvEnglishOralPracticeStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                            it.englishSpeech?.goldCoinsNum?.let {
                                tvEnglishOralPracticeGold.visibility = View.VISIBLE
                                tvEnglishOralPracticeGold.text = "+ $it"
                            }
                        }
                        3 -> {
                            tvEnglishOralPracticeStatus.text = "去查看"
                            tvEnglishOralPracticeStatus.setTextColor(Color.parseColor("#FFFFFF"))
                            tvEnglishOralPracticeStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                        }
                        else -> {
                            tvEnglishOralPracticeStatus.text = "去订正"
                            tvEnglishOralPracticeStatus.setTextColor(Color.parseColor("#FFFFFF"))
                            tvEnglishOralPracticeStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                        }
                    }
                    if (it.englishSpeech?.processStatus == 10) {
                        tvEnglishOralPracticeStatus.text = "未开放"
                        tvEnglishOralPracticeStatus.setTextColor(Color.parseColor("#A3B3C2"))
                        tvEnglishOralPracticeStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                    }
                    llEnglishOralPracticeStar.visibility = View.VISIBLE
                    tvEnglishOralPracticeStatus.visibility = View.VISIBLE
                } else {
                    tvEnglishOralPracticeFlag.visibility = View.GONE
                    llEnglishOralPracticeStar.visibility = View.GONE
                    tvEnglishOralPracticeStatus.visibility = View.GONE
                    tvEnglishOralPracticeGold.visibility = View.GONE
                }
                this.showTask()
                rlEnglishOralPractice.visibility = View.VISIBLE
            } else {
                rlEnglishOralPractice.visibility = View.GONE
            }
        }
        rlEnglishOralPractice.setOnClickListener {
            setEnglishClickListener()
        }
    }

    /**
     * 口语练习点击事件
     * 已退班或者已经结课的讲次不能再次去交作业
     */
    private fun setEnglishClickListener() {
        mLessonDetailBean?.let {
            if (null == it.englishSpeech || 0 == it.englishSpeech?.englishQuestionsCount) {
                return
            }
            when (it.englishSpeech?.operationalStatus) {
                0 -> {
                    ToastUtil.showToast("暂未开放，本讲结课后开放")
                    return
                }
                30 -> {
                    ToastUtil.showToast("有效期到期")
                    return
                }
                20 -> {
                    ToastUtil.showToast("已退班")
                    return
                }
            }

            if (0 == it.englishSpeech?.englishSpeechStar && 4 == it.status) { //0.未开课，1.开课, 2.结课未超过7天，3.已退班未超过7天，4.结课超过7天，5.退班超过7天
                ToastUtil.showToast("已结课60天，不能再提交")
                return
            } else if (0 == it.englishSpeech?.englishSpeechStar && 5 == it.status) {
                ToastUtil.showToast("已退班，不能再提交")
                return
            }
            val lesson = Lesson()
            lesson.lessonId = it.lessonId.toString()
            lesson.lessonName = it.lessonName
            lesson.flag = it.lessonHomeworkInfo?.flag.toString()
            lesson.num = it.num.toString()
            lesson.status = it.status ?: 1
            lesson.englishSpeechStar = it.englishSpeech?.englishSpeechStar ?: 0
            lesson.englishFlag = it.englishSpeech?.flag.toString()
            lesson.goldCoinsNum = it.englishSpeech?.goldCoinsNum?: 0
            LessonDetailPresenter.setEnglishOnclickListener(lesson, activity!!, it.classId.toString())
        }
    }

    /**
     * 展示AI口语背诵
     */
    private fun showAiEnglishRecite() {
        mLessonDetailBean?.let {
            if (null != it.englishRecite) {
                if (it.englishRecite?.englishQuestionsCount != 0) {
                    //配置是否重交重判
                    when (it.englishRecite?.flag) {
                        1 -> {
                            tvEnglishOralReciteFlag.visibility = View.VISIBLE
                            tvEnglishOralReciteFlag.text = "有更新"
                        }
                        else -> {
                            tvEnglishOralReciteFlag.visibility = View.GONE
                        }
                    }
                    //配置星星状态
                    ivEnglishOralReciteStar1.setImageResource(R.drawable.app_icon_lessondetail_star_gray)
                    ivEnglishOralReciteStar2.setImageResource(R.drawable.app_icon_lessondetail_star_gray)
                    ivEnglishOralReciteStar3.setImageResource(R.drawable.app_icon_lessondetail_star_gray)
                    when (it.englishRecite?.englishSpeechStar) {
                        1 -> {
                            ivEnglishOralReciteStar1.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                        }
                        2 -> {
                            ivEnglishOralReciteStar1.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                            ivEnglishOralReciteStar2.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                        }
                        3 -> {
                            ivEnglishOralReciteStar1.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                            ivEnglishOralReciteStar2.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                            ivEnglishOralReciteStar3.setImageResource(R.drawable.app_icon_lessondetail_star_light)
                        }
                    }
                    //按钮提交状态
                    tvEnglishOralReciteGold.visibility = View.GONE
                    when (it.englishRecite?.englishSpeechStar) {
                        0 -> {
                            tvEnglishOralReciteStatus.text = "去背诵"
                            tvEnglishOralReciteStatus.setTextColor(Color.parseColor("#FFFFFF"))
                            tvEnglishOralReciteStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                            it.englishRecite?.goldCoinsNum?.let {
                                tvEnglishOralReciteGold.visibility = View.VISIBLE
                                tvEnglishOralReciteGold.text = "+ $it"
                            }
                        }
                        3 -> {
                            tvEnglishOralReciteStatus.text = "去查看"
                            tvEnglishOralReciteStatus.setTextColor(Color.parseColor("#FFFFFF"))
                            tvEnglishOralReciteStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                        }
                        else -> {
                            tvEnglishOralReciteStatus.text = "去订正"
                            tvEnglishOralReciteStatus.setTextColor(Color.parseColor("#FFFFFF"))
                            tvEnglishOralReciteStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                        }
                    }
                    if (it.englishRecite?.processStatus == 10) {
                        tvEnglishOralReciteStatus.text = "未开放"
                        tvEnglishOralReciteStatus.setTextColor(Color.parseColor("#A3B3C2"))
                        tvEnglishOralReciteStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                    }
                    llEnglishOralReciteStar.visibility = View.VISIBLE
                    tvEnglishOralReciteStatus.visibility = View.VISIBLE
                } else {
                    tvEnglishOralReciteFlag.visibility = View.GONE
                    llEnglishOralReciteStar.visibility = View.GONE
                    tvEnglishOralReciteStatus.visibility = View.GONE
                    tvEnglishOralReciteGold.visibility = View.GONE
                }
                this.showTask()
                rlEnglishOralRecite.visibility = View.VISIBLE
            } else {
                rlEnglishOralRecite.visibility = View.GONE
            }
        }
        rlEnglishOralRecite.setOnClickListener {
            setAiEnglishReciteClickListener()
        }
    }

    /**
     * AI口语背诵点击事件
     * 已退班或者已经结课的讲次不能再次去交作业
     */
    private fun setAiEnglishReciteClickListener() {
        mLessonDetailBean?.let {
            if (null == it.englishRecite || 0 == it.englishRecite?.englishQuestionsCount) {
                return
            }
            when (it.englishRecite?.operationalStatus) {
                0 -> {
                    ToastUtil.showToast("暂未开放，本讲结课后开放")
                    return
                }
                30 -> {
                    ToastUtil.showToast("有效期到期")
                    return
                }
                20 -> {
                    ToastUtil.showToast("已退班")
                    return
                }
            }

            if (0 == it.englishRecite?.englishSpeechStar && 4 == it.status) { //0.未开课，1.开课, 2.结课未超过7天，3.已退班未超过7天，4.结课超过7天，5.退班超过7天
                ToastUtil.showToast("已结课60天，不能再提交")
                return
            } else if (0 == it.englishRecite?.englishSpeechStar && 5 == it.status) {
                ToastUtil.showToast("已退班，不能再提交")
                return
            }
            if (it.englishRecite?.englishSpeechStar?:0 > 0) {
                var url = "axx://AiEnglishReciteResult?classId=%s&lessonId=%s"
                url = String.format(url , mLessonDetailBean?.classId.toString()
                        , mLessonDetailBean?.lessonId.toString(), it.englishRecite?.flag.toString())
                SchemeDispatcher.jumpPage(activity, url)
                collectClickEvent("XSD_479")
            }else{
                var url = "axx://AiEnglishRecite?classId=%s&lessonId=%s&version=%s&num=%s&flag=%s"
                url = String.format(url , mLessonDetailBean?.classId.toString()
                        , mLessonDetailBean?.lessonId.toString() , "", mLessonDetailBean?.num.toString(), it.englishRecite?.flag.toString())
                SchemeDispatcher.jumpPage(activity, url)
                collectClickEvent("XSD_478")
            }
        }
    }

    /**
     * 显示课堂回放
     */
    private fun showClassVideo() {
        mLessonDetailBean?.let {
            if (0 != it.havaClassReport && null != it.dtClassReport) {
                when (it.dtClassReport?.haveBuyLesson) {
                    0 -> { //未购买
                        tvClassPlaybackStatus.text = "未购买"
                        tvClassPlaybackStatus.setTextColor(Color.parseColor("#A3B3C2"))
                        tvClassPlaybackStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                    }
                    1 -> { //购买
                        if (it.dtClassReport?.status != 2 && it.dtClassReport?.status != 4) { //0未开课//1待上传//3已下架//2已上架//5未观看//6已观看//4已过期
                            tvClassPlaybackStatus.text = "未生成"
                            tvClassPlaybackStatus.setTextColor(Color.parseColor("#A3B3C2"))
                            tvClassPlaybackStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                        } else if (it.dtClassReport?.status == 2) {
                            tvClassPlaybackStatus.text = "看回放"
                            tvClassPlaybackStatus.setTextColor(Color.parseColor("#FFFFFF"))
                            tvClassPlaybackStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                        } else if (it.dtClassReport?.status == 4) {
                            tvClassPlaybackStatus.text = "已过期"
                            tvClassPlaybackStatus.setTextColor(Color.parseColor("#A3B3C2"))
                            tvClassPlaybackStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                        }
                    }
                    2 -> { //已退课
                        tvClassPlaybackStatus.text = "已退课"
                        tvClassPlaybackStatus.setTextColor(Color.parseColor("#A3B3C2"))
                        tvClassPlaybackStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                    }
                }
                this.showLearned()
                tvClassPlaybackStatus.visibility = View.VISIBLE
                rlClassPlayback.visibility = View.VISIBLE
                rlClassReport.visibility = View.VISIBLE
            } else {
                rlClassPlayback.visibility = View.GONE
                rlClassReport.visibility = View.GONE
            }
        }
        rlClassPlayback.setOnClickListener {
            setClassVideoClickListener()
        }
    }

    /**
     * 课堂回放点击事件
     */
    private fun setClassVideoClickListener() {
        mLessonDetailBean?.let {
            if (0 != it.havaClassReport && null != it.dtClassReport) {
                if (1 == it.dtClassReport?.haveBuyLesson && it.dtClassReport?.status == 2 && !TextUtils.isEmpty(it.dtClassReport?.videoUrl)) { //看回放
                    HomeworkOptions.instance
                            .with(activity)
                            .pad(pageId)
                            .start(
                                    JumpDoClassVideoBuilder()
                                            .setUrl(it.dtClassReport?.videoUrl)
                                            .setTitle(it.lessonName)
                                            .setClassId(mLessonDetailBean?.classId.toString())
                                            .setLessonId(mLessonDetailBean?.lessonId.toString())
                                            .setPlayType(PlayType.doubleTeacherVideo.toString())

                            )
                }
            }
        }
    }

    /**
     * 电子讲义
     */
    private fun showHandout() {
        mLessonDetailBean?.let {
            if(it.haveOnlineTeachingMaterial == 1) {
                this.showLearned()
                rlHandout.visibility = View.VISIBLE
                if (it.status == 3 || it.status == 5) {
                    tvHandoutStatus.text = "未开放"
                    tvHandoutStatus.setTextColor(Color.parseColor("#A3B3C2"))
                    tvHandoutStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                } else {
                    tvHandoutStatus.text = "去查看"
                    tvHandoutStatus.setTextColor(Color.parseColor("#FFFFFF"))
                    tvHandoutStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                    rlHandout.setOnClickListener {
                        setHandoutListener()
                        collectClickEvent("XSD_400")
                    }
                }
            }else if (it.haveOnlineTeachingMaterial == 2) {
                this.showLearned()
                rlHandout.visibility = View.VISIBLE
                tvHandoutStatus.text = "未开放"
                tvHandoutStatus.setTextColor(Color.parseColor("#A3B3C2"))
                tvHandoutStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
            } else {
                rlHandout.visibility = View.GONE
            }
        }
    }

    /**
     * 电子讲义点击事件
     */
    private fun setHandoutListener() {
        mLessonDetailBean?.let {
            (activity as STBaseActivity).showLoadingProgressDialog()
            val paramMap = HashMap<String, String>()
            paramMap["studentId"] = STBaseConstants.userId
            paramMap["classId"] = mLessonDetailBean?.classId.toString()
            paramMap["lessonId"] = mLessonDetailBean?.lessonId.toString() //1127436084  1127441200 1127441199 1127441198
            GSRequest.startRequest(AppApi.teachingMaterial, paramMap, object : GSStringCallback() {
                override fun onResponseSuccess(response: Response<*>?, code: Int, result: String) {
                    if (activity == null || activity?.isFinishing == true) {
                        return
                    }
                    (activity as STBaseActivity).dismissLoadingProgressDialog()
                    val json = JSONObject(result)
                    val status = json.optInt("status")
                    val data = json.optJSONObject("data")
                    if (status == 1) {
                        val type = data.optInt("type")
                        if (type == 1) {
                            val params = JSONObject()
                            val suffix = "eleTextbook.web.js"
                            params.put("title", mLessonDetailBean?.lessonName.toString())
                            params.put("studentId", STBaseConstants.userId)
                            params.put("classId", mLessonDetailBean?.classId.toString())
                            params.put("lessonId", mLessonDetailBean?.lessonId.toString())
                            SystemUtil.gotoWebPage(activity, InitBaseLib.getInstance().configManager.h5ServerUrl + suffix, SystemUtil.generateDefautJsonStr(params, suffix))
                        } else {
                            var url = "axx://PDFViewer?pdfUrl=%s&waterMarkType=%s"
                            url = String.format(url, data.optString("pdfUrl") , data.optInt("waterMarkType" , 0))
                            LogUtil.d("url: $url")
                            SchemeDispatcher.jumpPage(activity, url)
                        }
                    } else {
                        ToastUtil.showToast(json.optString("message") + "")
                    }
                }

                override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                    if (activity == null || activity?.isFinishing == true) {
                        return
                    }
                    (activity as STBaseActivity).dismissLoadingProgressDialog()
                }
            })
        }
    }

    /**
     * 课堂报告展示
     */
    private fun showClassReport() {
        mLessonDetailBean?.let {
            if (0 != it.havaClassReport && null != it.dtClassReport) {
                if (it.dtClassReport?.haveStudyReport == true) { //学情报告
                    tvClassReportName.text = "学情报告"
                    ivClassReportIcon.setImageResource(R.drawable.icon_study_report)
                    tvClassReportRatio.visibility = View.GONE
                } else {
                    tvClassReportName.text = "课堂报告"
                    ivClassReportIcon.setImageResource(R.drawable.icon_class_report)
                    tvClassReportRatio.visibility = View.VISIBLE
                    tvClassReportRatio.text = ""
                }
                when (it.dtClassReport?.haveBuyLesson) {
                    0 -> { //未购买
                        tvClassReportStatus.text = "未购买"
                        tvClassReportStatus.setTextColor(Color.parseColor("#A3B3C2"))
                        tvClassReportStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                    }
                    1 -> { //购买
                        if (it.dtClassReport?.haveReport == true) {
                            tvClassReportStatus.text = "看报告"
                            tvClassReportRatio.text = "正确率" + it.dtClassReport?.getRatio()?.toString() + "%"
                            tvClassReportStatus.setTextColor(Color.parseColor("#FFFFFF"))
                            tvClassReportStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_green)
                        } else {
                            tvClassReportStatus.text = "未生成"
                            tvClassReportStatus.setTextColor(Color.parseColor("#A3B3C2"))
                            tvClassReportStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                        }
                    }
                    2 -> { //已退课
                        tvClassReportStatus.text = "已退课"
                        tvClassReportStatus.setTextColor(Color.parseColor("#A3B3C2"))
                        tvClassReportStatus.setBackgroundResource(R.drawable.app_bg_detail_circle_gray)
                    }
                }
                this.showLearned()
                tvClassReportStatus.visibility = View.VISIBLE
                rlClassPlayback.visibility = View.VISIBLE
                rlClassReport.visibility = View.VISIBLE
            } else {
                rlClassPlayback.visibility = View.GONE
                rlClassReport.visibility = View.GONE
            }
        }
        rlClassReport.setOnClickListener {
            setClassReportClickListener()
        }
    }

    /**
     * 学情报告，课堂报告点击事件
     */
    private fun setClassReportClickListener() {
        mLessonDetailBean?.let {
            if (0 != it.havaClassReport && null != it.dtClassReport) {
                if (1 == it.dtClassReport?.haveBuyLesson && it.dtClassReport?.haveReport == true) {
                    if (it.dtClassReport?.haveStudyReport == true) { //学情报告
                        try {
                            HomeworkOptions.instance
                                    .with(activity)
                                    .pad(pageId)
                                    .start(
                                            JumpDoLearnReportBuilder()
                                                    .setClassId(it.classId.toString())
                                                    .setLessonId(it.lessonId.toString())
                                                    .setLessonName(it.lessonName)
                                                    .setLessonNameNum(it.num.toString())
                                    )
                        } catch (e: Exception) {
                            LOGGER.log(e)
                        }
                    } else { //课堂报告
                        try {
                            HomeworkOptions.instance
                                    .with(activity)
                                    .pad(pageId)
                                    .start(
                                            JumpDoDTReportBuilder()
                                                    .setClassId(it.classId.toString())
                                                    .setLessonId(it.lessonId.toString())
                                    )
                        } catch (e: Exception) {
                            LOGGER.log(e)
                        }
                    }
                }
            }
        }
    }
}
