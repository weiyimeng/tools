package gaosi.com.learn.studentapp.main

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.alibaba.fastjson.JSON
import com.gaosi.axxdtlive.jump.DTLiveOptions
import com.gaosi.axxdtlive.jump.JumpDoDTLiveBuilder
import com.gaosi.passport.PassportAPI
import com.gaosi.teacheronline.TeacherOnLineApi
import com.gaosi.teacheronline.TeacherOnLineSDKConfig
import com.gaosi.teacheronline.entity.LiveEntranceBean
import com.gaosi.teacheronline.entity.PlayBackBean
import com.gaosi.teacheronline.jump.JumpDoEnterRoomBuilder
import com.gaosi.teacheronline.jump.JumpDoPlayBackBuilder
import com.gaosi.teacheronline.jump.TeacherOnLineOptions
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.net.callback.GSStringCallback
import com.gsbaselib.utils.StatusBarUtil
import com.gsbaselib.utils.ToastUtil
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.base.BaseActivity
import com.gstudentlib.base.STBaseConstants
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
import gaosi.com.learn.bean.main.StudyTaskData
import gaosi.com.learn.bean.main.StudyTaskInfoBean
import gaosi.com.learn.bean.main.TaskEveryDayBean
import gaosi.com.learn.studentapp.main.adapter.MoreLessonAdapter
import kotlinx.android.synthetic.main.activity_more_lesson.*
import java.util.*

/**
 * description:
 * created by huangshan on 2020/6/4 下午5:23
 */
public class MoreLessonActivity : BaseActivity() {

    private var mIsFirstVisiable = true
    private var mMoreLessonAdapter: MoreLessonAdapter? = null
    private var mTime = ""
    private var mTaskEveryDayBean: TaskEveryDayBean? = null
    private var mStudyTaskInfoBean: StudyTaskInfoBean? = null
    private var mIsPlayBack = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_lesson)
        title_bar.setLeftListener(this)
        getIntentData()
        initAdapter()
    }

    private fun getIntentData() {
        intent?.apply {
            mTaskEveryDayBean = getSerializableExtra("data") as TaskEveryDayBean?
        }
    }

    private fun initAdapter() {
        val mYear = mTaskEveryDayBean?.taskYear ?: 0
        val mMonth = mTaskEveryDayBean?.taskMonth ?: 0
        val mDay = mTaskEveryDayBean?.taskDay ?: 0
        val timeStr = mYear.toString() + "年" + (if (mMonth < 10) "0$mMonth" else mMonth) + "月" + (if (mDay < 10) "0$mDay" else mDay) + "日"
        mTime = mYear.toString() + "-" + (if (mMonth < 10) "0$mMonth" else mMonth) + "-" + (if (mDay < 10) "0$mDay" else mDay)
        tvDate.text = timeStr
        mMoreLessonAdapter = MoreLessonAdapter()
        recyclerView.adapter = mMoreLessonAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        mMoreLessonAdapter?.setNewData(mTaskEveryDayBean?.studyTaskInfoList)
        mMoreLessonAdapter?.setOnItemChildClickListener { adapter, view, position ->
            mStudyTaskInfoBean = adapter?.getItem(position) as StudyTaskInfoBean
            when (view.id) {
                R.id.rlLessonInfo -> {
                    if (mStudyTaskInfoBean?.type == 1) {
                        //在线 课程学习
                        var url = "axx://courseDetailedPage?courseId=%s&gsId=%s&courseTypeFlag=%s&lectureId=%s&lecturePackageId=%s&lectureType=%s"
                        url = String.format(url, mStudyTaskInfoBean?.axxOnlineTol?.courseId, mStudyTaskInfoBean?.axxOnlineTol?.gaoSiId, mStudyTaskInfoBean?.axxOnlineTol?.courseTypeFlag, mStudyTaskInfoBean?.axxOnlineTol?.lectureId, mStudyTaskInfoBean?.axxOnlineTol?.lecturePackageId, mStudyTaskInfoBean?.axxOnlineTol?.lectureType)
                        SchemeDispatcher.jumpPage(this, url)
                    } else {
                        var url = "axx://lessonDetail?classId=%s&lessonId=%s"
                        url = String.format(url, mStudyTaskInfoBean?.classId, mStudyTaskInfoBean?.lessonId)
                        SchemeDispatcher.jumpPage(this, url)
                    }
                    collectClickEvent("XSD_510")
                }
                R.id.llLiving -> {
                    when (mStudyTaskInfoBean?.type) {
                        1 -> {
                            //在线课TOL
                            val axxOnlineTol = mStudyTaskInfoBean?.axxOnlineTol
                            when (axxOnlineTol?.lessonStatus) {
                                2, 3 -> {
                                    mIsPlayBack = false
                                    collectClickEvent("XSD_565")
                                }
                                5 -> {
                                    mIsPlayBack = true
                                    collectClickEvent("XSD_566")
                                }
                            }
                            requestPermissionThenLiveIn()
                        }
                        2 -> {
                            //在线课
                            setAxxLiveListener()
                        }
                        3, 4 -> {
                            //双师课、AI好课
                            setDtLiveListener()
                        }
                        5 -> {
                            //在线外教课
                            val foreignCourse = mStudyTaskInfoBean?.foreignCourse
                            when (foreignCourse?.lessonStatus) {
                                2, 3 -> {
                                    mIsPlayBack = false
                                    requestSiGaoEntrance()
                                }
                                5 -> {
                                    mIsPlayBack = true
                                    requestSiGaoRecord()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mIsFirstVisiable) {
            requestStudyTask()
        }
        mIsFirstVisiable = false
    }

    private fun requestStudyTask() {
        showLoadingProgressDialog()
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["theStartTime"] = mTime
        paramMap["theEndOfTime"] = mTime
        paramMap["taskInfoType"] = "1"
        GSRequest.startRequest(AppApi.studyTask, paramMap, object : GSJsonCallback<StudyTaskData>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<StudyTaskData>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                if (result.body == null) {
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                val studyTaskData = result.body
                if (studyTaskData.taskEveryDay.isNullOrEmpty() || studyTaskData.taskEveryDay?.first()?.studyTaskInfoList.isNullOrEmpty()) {
                } else {
                    mTaskEveryDayBean = studyTaskData.taskEveryDay?.get(0)
                    mMoreLessonAdapter?.setNewData(mTaskEveryDayBean?.studyTaskInfoList)
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                message ?: return
                ToastUtil.showToast(message)
            }
        })
    }

    private fun requestPermissionThenLiveIn(){
        val doPrepareResult = "axx://newGroupTransfer?gsId=%s&courseId=%s&lessonId=%s&isBackPlay=%s&isPrivacyShow=%s&pageId=%s&diyLessonId=%s&liveRoomType=%s&lecturePackageId=%s&lectureId=%s&lectureType=%s&courseTypeFlag=%s"
        val url = String.format(doPrepareResult, mStudyTaskInfoBean?.axxOnlineTol?.gaoSiId.toString(), mStudyTaskInfoBean?.axxOnlineTol?.courseId, mStudyTaskInfoBean?.lessonId, mIsPlayBack,
                mStudyTaskInfoBean?.axxOnlineTol?.privacyShow, pageId, mStudyTaskInfoBean?.axxOnlineTol?.diyLessonId, mStudyTaskInfoBean?.axxOnlineTol?.liveRoomType, mStudyTaskInfoBean?.axxOnlineTol?.lecturePackageId, mStudyTaskInfoBean?.axxOnlineTol?.lectureId,
                mStudyTaskInfoBean?.axxOnlineTol?.lectureType, mStudyTaskInfoBean?.axxOnlineTol?.courseTypeFlag)
        SchemeDispatcher.jumpPage(this, url)
    }

    /**
     * 进入爱学习在线直播
     */
    private fun enterAxxOnlineRoom() {
        LiveManager.newInstance().setListener(object : RoomListener {
            override fun onError(code: Int, errMsg: String) {
            }

            override fun onKickOut(res: Int) {
                if (1 == res) {
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
     * 保存爱学习在线学生上课状态
     */
    private fun requestAxxOnLineJoin(joinType: String) {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["classId"] = mStudyTaskInfoBean?.classId.toString()
        paramMap["lessonId"] = mStudyTaskInfoBean?.lessonId.toString()
        paramMap["joinType"] = joinType
        GSRequest.startRequest(GSAPI.axxOnLineJoin, paramMap, object : GSJsonCallback<PlayBackBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<PlayBackBean>) {
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
            }
        })
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
                        LiveManager.newInstance().enterRoom(this@MoreLessonActivity
                                , mStudyTaskInfoBean?.classTypeId.toString()
                                , getClassId()
                                , mStudyTaskInfoBean?.lessonId.toString()
                                , STBaseConstants.userId
                                , getUserName()
                                , mStudyTaskInfoBean?.axxOnline?.liveBaseUrl ?: "")
                    }

                    override fun onCheckUnPass(iRxCheckStatus: IRxCheckStatus?) {
                        iRxCheckStatus?.let {
                            if (it is RxCheckNetStatus) {
                                ToastUtil.showToast("当前无网络，请检查网络后重试！")
                            }
                        }
                    }
                })
    }

    /**
     * 保存双师回放状态
     */
    private fun requestSaveDtReplay() {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["classId"] = mStudyTaskInfoBean?.classId.toString()
        paramMap["lessonId"] = mStudyTaskInfoBean?.lessonId.toString()
        paramMap["joinType"] = "1" //回放1
        GSRequest.startRequest(AppApi.saveDtReplay, paramMap, object : GSStringCallback() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: String) {
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
            }
        })
    }

    /**
     * 获取外教直播间信息
     */
    private fun requestSiGaoEntrance() {
        showLoadingProgressDialog()
        TeacherOnLineSDKConfig.requestGetSiGaoEntrance(STBaseConstants.userId
                , mStudyTaskInfoBean?.foreignCourse?.fbClassId.toString()
                , object : TeacherOnLineSDKConfig.LiveCallBackListener {
            override fun onSuccess(liveEntranceBean: LiveEntranceBean) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                val map = JSON.parseObject(JSON.toJSONString(liveEntranceBean)) as Map<String, Any>
                val params = mutableMapOf<String, Any>()
                params.putAll(map)
                params["serial"] = liveEntranceBean.roomId ?: ""
                params["nickname"] = STBaseConstants.userInfo.truthName
                params["clientType"] = "2"
                TeacherOnLineOptions.instance
                        .with(this@MoreLessonActivity)
                        .pad(pageId).start(JumpDoEnterRoomBuilder().setParams(params))
                collectClickEvent("XSD_567")
            }

            override fun onFail(message: String?) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                message ?: return
                ToastUtil.showToast(message)
            }
        })
    }

    /**
     * 获取外教回放信息
     */
    private fun requestSiGaoRecord() {
        showLoadingProgressDialog()
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["fbClassId"] = mStudyTaskInfoBean?.foreignCourse?.fbClassId.toString()
        GSRequest.startRequest(TeacherOnLineApi.getSiGaoRecord, GSRequest.GET, paramMap, object : GSJsonCallback<PlayBackBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<PlayBackBean>) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                if (result.status == 1) {
                    if (result.body.status == 1) {
                        TeacherOnLineOptions.instance
                                .with(this@MoreLessonActivity)
                                .pad(pageId)
                                .start(
                                        JumpDoPlayBackBuilder()
                                                .setUrl(result.body.content)
                                                .setTitle("第${mStudyTaskInfoBean?.lessonNum}讲 ${mStudyTaskInfoBean?.lessonName}")
                                                .setClassId(mStudyTaskInfoBean?.classId.toString())
                                                .setLessonId(mStudyTaskInfoBean?.lessonId.toString())
                                )
                        collectClickEvent("XSD_568")
                    } else {
                        ToastUtil.showToast("视频还未生成哦！")
                    }
                } else {
                    ToastUtil.showToast("获取回放地址失败")
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (isFinishing || isDestroyed) {
                    return
                }
                dismissLoadingProgressDialog()
                message ?: return
                ToastUtil.showToast(message)
            }
        })
    }

    /**
     * 获取用户名
     */
    private fun getUserName(): String {
        if (STBaseConstants.userInfo == null) {
            return "爱学习"
        }
        if (STBaseConstants.userInfo?.truthName != null) {
            return STBaseConstants.userInfo?.truthName ?: ""
        }
        if (STBaseConstants.userInfo?.parentTel1 != null) {
            return STBaseConstants.userInfo?.parentTel1 ?: ""
        }
        return "爱学习"
    }

    /**
     * 获取班级id 如果是武汉巨人,取大班id
     */
    private fun getClassId(): String {
        return if (mStudyTaskInfoBean?.isJrClass == 1) {
            mStudyTaskInfoBean?.mainClassId.toString()
        } else {
            mStudyTaskInfoBean?.classId.toString()
        }
    }

    /**
     * 爱学习在线直播点击事件
     */
    private fun setAxxLiveListener() {
        val axxOnline = mStudyTaskInfoBean?.axxOnline
        when (axxOnline?.lessonStatus) {
            2, 3 -> {
                mIsPlayBack = false
                enterAxxOnlineRoom()
            }
            5 -> {
                mIsPlayBack = true
                if (TextUtils.isEmpty(mStudyTaskInfoBean?.axxOnline?.replayUrl)) { //可能是录制间
                    enterRoom(2)
                } else {
                    var url = "axx://playBack?vUrl=%s&title=%s&classId=%s&jcId=%s&pad=%s&playType=%s"
                    val title = "第${mStudyTaskInfoBean?.lessonNum}讲 ${mStudyTaskInfoBean?.lessonName}"
                    url = String.format(url, mStudyTaskInfoBean?.axxOnline?.replayUrl, title, mStudyTaskInfoBean?.classId, mStudyTaskInfoBean?.lessonId, pageId, PlayType.axxOnLineVideo.toString())
                    SchemeDispatcher.jumpPage(this, url)
                }
                requestAxxOnLineJoin("1")
                collectClickEvent("XSD_560")
            }
        }
    }

    /**
     * 双师在线点击事件
     */
    private fun setDtLiveListener() {
        val dtLive = mStudyTaskInfoBean?.dtLive
        when (dtLive?.lessonStatus) {
            2, 3 -> {
                mIsPlayBack = false
                val endTime = if ((dtLive.lessonEndTime ?: 0L) == 0L) {
                    (dtLive.lessonBeginTime ?: 0L) + 120 * 60 * 1000L
                } else {
                    (dtLive.lessonEndTime ?: 0L)
                }
                if (dtLive.aiLesson == 1) {
                    DTLiveOptions.instance
                            .with(this)
                            .pad(pageId)
                            .start(JumpDoDTLiveBuilder().setSmallClassId(mStudyTaskInfoBean?.classId.toString())
                                    .setMainClassId(mStudyTaskInfoBean?.mainClassId?.toString())
                                    .setLessonId(mStudyTaskInfoBean?.lessonId.toString())
                                    .setEndTime(endTime)
                                    .setType(2)
                            )
                    collectClickEvent("XSD_380")
                } else {
                    DTLiveOptions.instance
                            .with(this)
                            .pad(pageId)
                            .start(JumpDoDTLiveBuilder().setSmallClassId(mStudyTaskInfoBean?.classId.toString())
                                    .setMainClassId(mStudyTaskInfoBean?.mainClassId?.toString())
                                    .setLessonId(mStudyTaskInfoBean?.lessonId.toString())
                                    .setEndTime(endTime)
                                    .setType(1)
                            )
                    collectClickEvent("XSD_373")
                }
            }
            5 -> {
                if (dtLive.aiLesson == 1) {
                    collectClickEvent("XSD_564")
                } else {
                    collectClickEvent("XSD_562")
                }
                mIsPlayBack = true
                var url = "axx://playBack?vUrl=%s&title=%s&classId=%s&jcId=%s&pad=%s&playType=%s"
                val title = "第${mStudyTaskInfoBean?.lessonNum}讲 ${mStudyTaskInfoBean?.lessonName}"
                url = String.format(url, dtLive.replayUrl, title, mStudyTaskInfoBean?.classId, mStudyTaskInfoBean?.lessonId, pageId, "7")
                SchemeDispatcher.jumpPage(this, url)
                requestSaveDtReplay()
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.llTitleLeft -> finish()
        }
    }

    override fun setStatusBar() {
        setStatusBar(Color.parseColor("#F8FAFD"), 0)
        StatusBarUtil.setLightMode(this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        super.onPermissionsGranted(requestCode, perms)
        if(requestCode == 101 && perms.contains(Manifest.permission.CAMERA) && perms.contains(Manifest.permission.RECORD_AUDIO)) {
            setAxxLiveListener()
        }else if(requestCode == 102 && perms.contains(Manifest.permission.CAMERA) && perms.contains(Manifest.permission.RECORD_AUDIO)) {
            setDtLiveListener()
        }
    }
}