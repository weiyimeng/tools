package gaosi.com.learn.studentapp.classlesson

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gaosi.axxdtlive.jump.DTLiveOptions
import com.gaosi.axxdtlive.jump.JumpDoDTLiveBuilder
import com.gaosi.passport.PassportAPI
import com.gaosi.teacheronline.entity.PlayBackBean
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.net.callback.GSStringCallback
import com.gsbaselib.utils.DateUtil
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.glide.ImageLoader
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.base.STBaseFragment
import com.gstudentlib.util.SchemeDispatcher
import com.gstudentlib.util.entity.PlayType
import com.gstudentlib.util.rxcheck.IRxCheckStatus
import com.gstudentlib.util.rxcheck.IRxCheckStatusListener
import com.gstudentlib.util.rxcheck.RxCheckNetStatus
import com.gstudentlib.util.rxcheck.RxCheckStatusMaster
import com.haoke91.room.LiveManager
import com.haoke91.room.interfaces.RoomListener
import com.lzy.okgo.model.Response
import gaosi.com.learn.R
import gaosi.com.learn.application.AppApi
import gaosi.com.learn.bean.classlesson.LessonBean
import gaosi.com.learn.bean.classlesson.lessondetail.LessonDetailBean
import gaosi.com.learn.studentapp.classlesson.status.CourseTypeId
import kotlinx.android.synthetic.main.item_fragment_lession_detail2.*
import java.util.*
import kotlin.collections.HashMap

/**
 * 作者：created by 逢二进一 on 2020/6/19 10:55
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
open class BaseLessonDetailFragment: STBaseFragment() {

    private var containerView: ViewGroup? = null
    protected var mCurrPosition: Int = 0
    protected var mLessonBean: LessonBean? = null
    protected var mLessonDetailBean: LessonDetailBean? = null
    //是否回放
    var mIsPlayBack = false
    //直播地址
    protected var liveBaseUrl: String = ""

    fun setCurrPosition(positon: Int) {
        this.mCurrPosition = positon
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup): View? {
        containerView = inflater.inflate(R.layout.item_fragment_lession_detail2, container, false) as ViewGroup
        return containerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mLessonBean = (activity as LessonDetailActivity).getData(mCurrPosition)
    }

    /**
     * 请求讲次详情
     */
    fun requestMyClassDetail() {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["classId"] = mLessonBean?.classId.toString()
        paramMap["lessonId"] = mLessonBean?.lessonId.toString()
        GSRequest.startRequest(GSAPI.myClassDetail, paramMap, object : GSJsonCallback<LessonDetailBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<LessonDetailBean>) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                (activity as BaseActivity).dismissLoadingProgressDialog()
                if (showResponseErrorMessage(result) == 0) {
                    return
                }
                //收集异常日志
                if (result.body == null) {
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                mLessonDetailBean = result.body as LessonDetailBean
                showData()
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                (activity as BaseActivity).dismissLoadingProgressDialog()
                message?: return
                ToastUtil.showToast(message)
            }
        })
    }

    open fun showData(){
        mLessonDetailBean?.let {
            tvLessonName.text = "第 ${it.num} 讲 ${it.lessonName}"

            if(it.showClassTime == 0) {
                tvLessonBeginTime.visibility = View.VISIBLE
                tvLessonBeginTime.text = "暂未开课"
            }else {
                if(it.beginTime == null) {
                    tvLessonBeginTime.visibility = View.VISIBLE
                    tvLessonBeginTime.text = "暂未开课"
                }else {
                    //判断是不是今天和明天
                    var isThisDay = false
                    if(isThisDay(Date(it.beginTime?:0))) {
                        tvLessonBeginTime.text = "今天"
                        isThisDay = true
                    }
                    if(isNextDay(Date(it.beginTime?:0))) {
                        tvLessonBeginTime.text = "明天"
                        isThisDay = true
                    }
                    if(!isThisDay) {
                        val beginDataTimeString = DateUtil.longToString(it.beginTime?:0, "MM月dd日")
                        val beginDataWeekString = DateUtil.getWeek(it.beginTime?:0)
                        tvLessonBeginTime.text = "$beginDataTimeString $beginDataWeekString"
                    }
                    tvLessonBeginTime.visibility = View.VISIBLE
                }
            }
            vLine.visibility = View.VISIBLE
            showTeacher()
            llLiving.setOnClickListener(this)
        }
    }

    /**
     * 显示助教主讲老师
     */
    private fun showTeacher() {
        mLessonDetailBean?.let {
            var speakerName = "" //主讲姓名
            var speakerAvatar = "" //主讲头像
            var assistant1Name = "" //助教1姓名
            var assistant1Avatar = "" //助教1头像
            var assistant2Name = "" //助教2姓名
            var assistant2Avatar = "" //助教2头像
            it.axxOnline?.let {
                speakerName = it.speakerName?:""
                speakerAvatar = it.speakerAvatar?:""
                assistant1Name = it.assistant1Name?:""
                assistant1Avatar = it.assistant1Avatar?:""
                assistant2Name = it.assistant2Name?:""
                assistant2Avatar = it.assistant2Avatar?:""
            }
            it.dtLive?.let {
                speakerName = it.speakerName?:""
                speakerAvatar = it.speakerAvatar?:""
                assistant1Name = it.assistant1Name?:""
                assistant1Avatar = it.assistant1Avatar?:""
                assistant2Name = it.assistant2Name?:""
                assistant2Avatar = it.assistant2Avatar?:""
            }
            it.axxOffline?.let {
                speakerName = it.speakerName?:""
                speakerAvatar = it.speakerAvatar?:""
                assistant1Name = it.assistant1Name?:""
                assistant1Avatar = it.assistant1Avatar?:""
                assistant2Name = it.assistant2Name?:""
                assistant2Avatar = it.assistant2Avatar?:""
            }
            var isHasTeacherHeader = false
            if(!TextUtils.isEmpty(speakerName)) {
                isHasTeacherHeader = true
                rlSpeaker.visibility = View.VISIBLE
                ivSpeakerName.text = speakerName
            }
            if(!TextUtils.isEmpty(speakerAvatar)) {
                isHasTeacherHeader = true
                rlSpeaker.visibility = View.VISIBLE
                ImageLoader.setCircleImageViewResource(ivSpeakerHeader , speakerAvatar , R.drawable.icon_default_teacher_header)
            }
            if(!TextUtils.isEmpty(assistant1Name)) {
                rlAssistant1.visibility = View.VISIBLE
                ivAssistant1Name.text = assistant1Name
            }
            if(!TextUtils.isEmpty(assistant1Avatar)) {
                rlAssistant1.visibility = View.VISIBLE
                ImageLoader.setCircleImageViewResource(ivAssistant1Avatar , assistant1Avatar , R.drawable.icon_default_teacher_header)
            }
            if(!TextUtils.isEmpty(assistant2Name)) {
                rlAssistant2.visibility = View.VISIBLE
                ivAssistant2Name.text = assistant2Name
            }
            if(!TextUtils.isEmpty(assistant2Avatar)) {
                rlAssistant2.visibility = View.VISIBLE
                ImageLoader.setCircleImageViewResource(ivAssistant2Avatar , assistant2Avatar , R.drawable.icon_default_teacher_header)
            }
            if(!isHasTeacherHeader) {
                rlSpeaker.visibility = View.VISIBLE
                ivSpeakerName.text = "暂无老师"
                rlSpeaker.visibility = View.VISIBLE
                ivSpeakerHeader.setImageResource(R.drawable.icon_default_teacher_header)
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id) {
            R.id.llLiving -> {
                mLessonDetailBean?.let {
                    when(it.courseTypeId) {
                        CourseTypeId.SMALL_CLASS_ONLINE -> { //在线小班课 TSM
                            val liveBean = it.axxOnline
                            liveBean?: return
                            liveBaseUrl = liveBean.liveBaseUrl.toString()
                            setAxxLiveListener()
                        }
                        CourseTypeId.DT_LIVE -> {//双师课 CTS
                            val liveBean = it.dtLive
                            liveBean?: return
                            setDtLiveListener()
                        }
                        CourseTypeId.AI_DT_LIVE -> {//AI好课 AIT
                            val liveBean = it.dtLive
                            liveBean?: return
                            setDtLiveListener()
                        }
                        CourseTypeId.OFFLINE_CLASS -> {//线下课 TSM
                            val liveBean = it.axxOffline
                            liveBean?: return
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    /**
     * 爱学习在线直播点击事件
     */
    private fun setAxxLiveListener() {
        mLessonDetailBean?.let {
            it.axxOnline ?: return@let
            when(it.axxOnline?.lessonStatus) {
                0 -> {//未开放
                    ToastUtil.showToast("您已退班，本讲次内容无法使用")
                }
                1 -> {//未开课
                }
                2,3 -> {//马上开课, 正在直播
                    enterAxxOnlineRoom()
                }
                4 -> {//直播完成回放生成中
                    ToastUtil.showToast("回放生成中，请耐心等候哦")
                }
                5 -> {//回放已生成
                    if(TextUtils.isEmpty(it.axxOnline?.replayUrl)) { //可能是录制间
                        enterRoom(2)
                    }else {
                        collectClickEvent("XSD_560")
                        var url = "axx://playBack?vUrl=%s&title=%s&classId=%s&jcId=%s&pad=%s&playType=%s"
                        val title = "${tvLessonName.text}"
                        url = String.format(url, it.axxOnline?.replayUrl, title, mLessonBean?.classId, mLessonBean?.lessonId, pageId, PlayType.axxOnLineVideo.toString())
                        SchemeDispatcher.jumpPage(activity, url)
                    }
                    requestAxxOnLineJoin("1")
                }
            }
        }
    }

    /**
     * 进入爱学习在线直播
     */
    private fun enterAxxOnlineRoom() {
        LiveManager.newInstance().setListener(object : RoomListener {
            override fun onError(code: Int, errMsg: String) {
            }
            override fun onKickOut(res: Int) {
                if(1 == res) {
                    PassportAPI.instance.refreshTokenCallback(PassportAPI.CODE_1003, "")
                }
            }
            override fun onWarning(code: Int) {
            }
            override fun onClassBegin() {
                requestAxxOnLineJoin("0")
            }
            override fun onClassDismiss() {
            }
            override fun onEnterRoom() {
            }
        })
        enterRoom(1)
    }

    /**
     * type 1直播  2回放
     */
    private fun enterRoom(type: Int) {
        RxCheckStatusMaster
                .addCheckStatus(RxCheckNetStatus())
                .check(object : IRxCheckStatusListener {
                    override fun onCheckPass() {
                        if (type == 1) {
                            collectClickEvent("as103_clk_zhibo_zhibo", "")
                        }
                        LiveManager.newInstance().enterRoom(activity!!
                                , mLessonDetailBean?.classTypeId.toString()
                                , getClassId()
                                , mLessonBean?.lessonId.toString()
                                , STBaseConstants.userId
                                , getUserName()
                                , liveBaseUrl)
                    }

                    override fun onCheckUnPass(iRxCheckStatus: IRxCheckStatus?) {
                        iRxCheckStatus?.let {
                            if(it is RxCheckNetStatus) {
                                ToastUtil.showToast("当前无网络，请检查网络后重试！")
                            }
                        }
                    }
                })
    }

    /**
     * 保存爱学习在线学生上课状态
     */
    private fun requestAxxOnLineJoin(joinType: String) {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["classId"] = mLessonBean?.classId.toString()
        paramMap["lessonId"] = mLessonBean?.lessonId.toString()
        paramMap["joinType"] = joinType
        GSRequest.startRequest(GSAPI.axxOnLineJoin, paramMap, object : GSJsonCallback<PlayBackBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<PlayBackBean>) {
            }
            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
            }
        })
    }

    /**
     * 双师在线点击事件
     */
    private fun setDtLiveListener() {
        mLessonDetailBean?.let {
            it.dtLive ?: return@let
            when(it.dtLive?.lessonStatus) {
                0 -> {//未开放
                    ToastUtil.showToast("您已退班，本讲次内容无法使用")
                }
                1 -> {//未开课
                }
                2,3 -> {//马上开课, 正在直播
                    var endTime = if((it.dtLive?.lessonEndTime?:0L) == 0L) {
                        (it.dtLive?.lessonBeginTime?:0L) + 120 * 60 * 1000L
                    } else {
                        (it.dtLive?.lessonEndTime?:0L)
                    }
                    if (it.dtLive?.aiLesson == 1) {
                        DTLiveOptions.instance
                                .with(activity)
                                .pad(pageId)
                                .start(JumpDoDTLiveBuilder().setSmallClassId(mLessonBean?.classId.toString())
                                        .setMainClassId(it.mainClassId?.toString())
                                        .setLessonId(mLessonBean?.lessonId.toString())
                                        .setEndTime(endTime)
                                        .setType(2)
                                )
                        collectClickEvent("XSD_380")
                    } else {
                        DTLiveOptions.instance
                                .with(activity)
                                .pad(pageId)
                                .start(JumpDoDTLiveBuilder().setSmallClassId(mLessonBean?.classId.toString())
                                        .setMainClassId(it.mainClassId?.toString())
                                        .setLessonId(mLessonBean?.lessonId.toString())
                                        .setEndTime(endTime)
                                        .setType(1)
                                )
                        collectClickEvent("XSD_373")
                    }
                }
                4 -> {//回放生成中
                    ToastUtil.showToast("回放正在生成，请耐心等待")
                }
                5 -> {//回放已生成
                    //双师在线回放
                    if (it.dtLive?.aiLesson == 1) {
                        collectClickEvent("XSD_564")
                    }else {
                        collectClickEvent("XSD_562")
                    }
                    var url = "axx://playBack?vUrl=%s&title=%s&classId=%s&jcId=%s&pad=%s&playType=%s"
                    val title = "${tvLessonName.text}"
                    url = String.format(url, it.dtLive?.replayUrl, title, mLessonBean?.classId.toString(), mLessonBean?.lessonId.toString(), pageId, "7")
                    SchemeDispatcher.jumpPage(activity, url)
                    collectClickEvent("XSD_375")
                    requestSaveDtReplay()
                }
            }
        }
    }

    /**
     * 保存双师回放状态
     */
    private fun requestSaveDtReplay() {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["classId"] = mLessonBean?.classId.toString()
        paramMap["lessonId"] = mLessonBean?.lessonId.toString()
        paramMap["joinType"] = "1" //回放1
        GSRequest.startRequest(AppApi.saveDtReplay, paramMap, object : GSStringCallback() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: String) {
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
            }
        })
    }

    /**
     * 权限执行
     */
    fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if(requestCode == 101 && perms.contains(Manifest.permission.CAMERA) && perms.contains(Manifest.permission.RECORD_AUDIO)) {
            setAxxLiveListener()
        }else if(requestCode == 102 && perms.contains(Manifest.permission.CAMERA) && perms.contains(Manifest.permission.RECORD_AUDIO)) {
            setDtLiveListener()
        }
    }

    /**
     * 获取班级id 如果是武汉巨人,取大班id
     */
    protected fun getClassId(): String {
        return if (mLessonDetailBean?.isJrClass == 1) {
            mLessonDetailBean?.mainClassId.toString()
        } else {
            mLessonBean?.classId.toString()
        }
    }

    /**
     * 获取用户名
     */
    protected fun getUserName(): String {
        if(STBaseConstants.userInfo == null) {
            return "爱学习"
        }
        if(STBaseConstants.userInfo?.truthName != null) {
            return STBaseConstants.userInfo?.truthName?: ""
        }
        if(STBaseConstants.userInfo?.parentTel1 != null) {
            return STBaseConstants.userInfo?.parentTel1?: ""
        }
        return "爱学习"
    }

    /**
     * 显示任务
     */
    protected fun showTask() {
        tvTask.visibility = View.VISIBLE
        llTask.visibility = View.VISIBLE
    }

    /**
     * 显示收获
     */
    protected fun showLearned() {
        tvLearned.visibility = View.VISIBLE
        llLearned.visibility = View.VISIBLE
    }

    protected fun setUnStartStatus(text: String) {
        tvLessonStatus.text = text
        tvLessonStatus.setTextColor(Color.parseColor("#FAAD14"))
        tvLessonStatus.visibility = View.VISIBLE
        llLiving.visibility = View.GONE
    }

    protected fun setLivingStatus() {
        mIsPlayBack = false
        tvTimeTitle.text = "正在直播"
        tvLessonStatus.visibility = View.GONE
        llLiving.visibility = View.VISIBLE
        Glide.with(activity!!).asGif().load(R.drawable.app_icon_living).into(ivLiveStatus)
        tvLiveStatus.text = "进入直播"
    }

    protected fun setPlayBackingStatus() {
        tvLessonStatus.text = "回放生成中"
        tvLessonStatus.setTextColor(Color.parseColor("#939CB6"))
        tvLessonStatus.visibility = View.VISIBLE
        llLiving.visibility = View.GONE
    }

    protected fun setPlayBackStatus() {
        mIsPlayBack = true
        tvLessonStatus.visibility = View.GONE
        llLiving.visibility = View.VISIBLE
        ivLiveStatus.setImageResource(R.drawable.app_icon_live_play)
        tvLiveStatus.text = "观看回放"
    }

    /**
     * 是否是今天
     */
    private fun isThisDay(date: Date): Boolean {
        val date = DateUtil.dateToString(date , "yyyy-MM-dd");
        val currentDate = DateUtil.dateToString(Date() , "yyyy-MM-dd")
        if(date == currentDate) {
            return true
        }
        return false
    }
    /**
     * 是否是明天
     */
    private fun isNextDay(date: Date): Boolean {
        val date = DateUtil.dateToString(date , "yyyy-MM-dd");
        val currentDate = DateUtil.dateToString(DateUtil.getNextDate(Date() , 1) , "yyyy-MM-dd")
        if(date == currentDate) {
            return true
        }
        return false
    }


}