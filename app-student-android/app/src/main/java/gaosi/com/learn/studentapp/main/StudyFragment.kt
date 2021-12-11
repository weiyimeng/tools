package gaosi.com.learn.studentapp.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.gaosi.axxdtlive.jump.DTLiveOptions
import com.gaosi.axxdtlive.jump.JumpDoDTLiveBuilder
import com.gaosi.englishhomework.jump.EnglishHomeworkOptions
import com.gaosi.englishhomework.jump.JumpDoEnglishHomeworkBuilder
import com.gaosi.homework.jump.HomeworkOptions
import com.gaosi.homework.jump.JumpDoAnswerCardBuilder
import com.gaosi.homework.jump.JumpDoHomeworkBuilder
import com.gaosi.passport.PassportAPI
import com.gaosi.preclass.jump.JumpDoPreExerciseBuilder
import com.gaosi.preclass.jump.JumpDoPreVideoBuilder
import com.gaosi.preclass.jump.PreExerciseOptions
import com.gaosi.specialcourse.jump.JumpDoSpecialCourseBuilder
import com.gaosi.specialcourse.jump.SpecialCourseOptions
import com.gaosi.teacheronline.TeacherOnLineApi
import com.gaosi.teacheronline.TeacherOnLineSDKConfig
import com.gaosi.teacheronline.entity.LiveEntranceBean
import com.gaosi.teacheronline.entity.PlayBackBean
import com.gaosi.teacheronline.entity.PreviewBean
import com.gaosi.teacheronline.jump.*
import com.gsbaselib.base.inject.GSAnnotation
import com.gsbaselib.net.GSRequest
import com.gsbaselib.net.callback.GSHttpResponse
import com.gsbaselib.net.callback.GSJsonCallback
import com.gsbaselib.net.callback.GSStringCallback
import com.gsbaselib.utils.LOGGER
import com.gsbaselib.utils.ToastUtil
import com.gsbaselib.utils.TypeValue
import com.gsbaselib.utils.dialog.AbsAdapter
import com.gsbaselib.utils.dialog.DialogUtil
import com.gsbaselib.utils.net.NetworkUtil
import com.gsbiloglib.log.GSLogUtil
import com.gstudentlib.GSAPI
import com.gstudentlib.StatisticsDictionary
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
import gaosi.com.learn.bean.main.*
import gaosi.com.learn.bean.raw.student_studentInfoBean
import gaosi.com.learn.studentapp.classlesson.LessonDetailPresenter
import gaosi.com.learn.studentapp.main.adapter.TodoTaskAdapter
import gaosi.com.learn.studentapp.qrcode.ScanActivity
import gaosi.com.learn.view.AxxEmptyView
import gaosi.com.learn.view.AxxLoadMoreView
import gaosi.com.learn.view.StudyTaskCardView
import kotlinx.android.synthetic.main.fragment_study.*
import org.json.JSONObject
import java.util.*

/**
 * description: 首页中的学习tab
 * author: zhengpeng
 * date: 18/5/24 上午11:54
 */
@GSAnnotation(pageId = StatisticsDictionary.homePage)
class StudyFragment : STBaseFragment(), SwipeRefreshLayout.OnRefreshListener, StudyTaskCardView.CallBack {

    companion object {
        //页面page索引
        const val PAGE_INDEX = 0
        const val PERMISSION_REQUEST_CAMERA = 110
        var mIsShowBackToTop = false
    }

    private var mIsFirstVisiable = true

    private var mTaskEveryDay: ArrayList<TaskEveryDayBean>? = null
    private var mCurrentTaskEveryDayBean: TaskEveryDayBean? = null
    private var mStudyTaskInfoBean: StudyTaskInfoBean? = null

    //header
    private lateinit var mHeaderView: View
    private var mStudyTaskCard: StudyTaskCardView? = null
    private var mLlEnglishStudy: LinearLayout? = null
    private var mIvForeignEnglish: ImageView? = null
    private var mIvSceneEnglish: ImageView? = null
    private var mNullView: View? = null
    private var mLlLiving: LinearLayout? = null
    private var mLlMoreLesson: LinearLayout? = null

    private var mHeaderHeight = 0
    private var mTotalDy = 0

    //个人信息
    private var studentInfoBean: student_studentInfoBean? = null

    //每周任务的显示
    private var mPageNum = 1
    private var mTotalPageNum = 1
    private var mTodoTaskAdapter: TodoTaskAdapter? = null

    //空页面点击类型  1网络错误 2未加入班级 3任务已完成
    private var mEmptyType = 1
    private lateinit var mEmptyLayout: View
    private var mEmptyView: AxxEmptyView? = null

    override fun initView(inflater: LayoutInflater?, container: ViewGroup?): View {
        return inflater?.inflate(R.layout.fragment_study, container, false) as View
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initAdapter()
        initData()
    }

    /**
     * 初始化view
     */
    private fun initView() {
        adjustStatusBarMargin(vStatusBar2)
        ivScan.setOnClickListener(this)
        ivMsg.setOnClickListener(this)
        mEmptyLayout = View.inflate(activity, R.layout.main_fragment_empty, null)
        mEmptyView = mEmptyLayout.findViewById(R.id.emptyView)
        mEmptyView?.setClickCallBackListener(object : AxxEmptyView.OnClickCallBackListener {
            override fun onClick() {
                when (mEmptyType) {
                    1 -> {
                        mPageNum = 1
                        requestTodoTask()
                    }
                    2 -> {
                        SchemeDispatcher.jumpPage(activity, "axx://activeClassRome")
                    }
                }
            }
        })
    }

    /**
     * 初始化adapter
     */
    private fun initAdapter() {
        mTodoTaskAdapter = TodoTaskAdapter()
        mTodoTaskAdapter?.run {
            addHeaderView(headerView)
            setLoadMoreView(AxxLoadMoreView())
            setOnLoadMoreListener({
                if (mPageNum > mTotalPageNum) {
                    //数据加载完毕
                    loadMoreEnd()
                } else {
                    requestTodoTask()
                }
            }, rv_study)
            disableLoadMoreIfNotFullPage()
            setOnItemChildClickListener { adapter, view, position ->
                val data = adapter.getItem(position) as TodoTaskListBean
                when (view.id) {
                    R.id.rlLessonInfo -> {
                        var url = "axx://lessonDetail?classId=%s&lessonId=%s"
                        url = String.format(url, data.classId, data.lessonId)
                        SchemeDispatcher.jumpPage(activity, url)
                        collectClickEvent("XSD_559")
                    }
                    R.id.rlPreStudy -> {
                        data.tasks?.forEach {
                            if (it.type == 6) {
                                LessonDetailPresenter.checkCourseActivation(data.classId.toString(), data.lessonId.toString(), 1, data.isActivation
                                        ?: 1, object : LessonDetailPresenter.OnCheckCourseActivationListener {
                                    override fun onCheckCourseStatus(status: Int, msg: String) {
                                        if (1 == status) {
                                            data.isActivation = 1
                                            // 1：旧 2：单词 3：都有，需要弹框
                                            when (it.preview) {
                                                1 -> {
                                                    requestEnglishPreview(data.classId.toString(), data.lessonId.toString(), data.lessonNum.toString(), data.lessonName)
                                                }
                                                2 -> {
                                                    EnglishHomeworkOptions.instance
                                                            .with(activity)
                                                            .pad(pageId)
                                                            .start(STBaseConstants.userId
                                                                    , data.classId.toString()
                                                                    , data.lessonId.toString())
                                                }
                                                3 -> {
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
                                                                            requestEnglishPreview(data.classId.toString(), data.lessonId.toString(), data.lessonNum.toString(), data.lessonName)
                                                                            dialog.dismiss()
                                                                        }
                                                                        R.id.tv_read -> {
                                                                            EnglishHomeworkOptions.instance
                                                                                    .with(activity)
                                                                                    .pad(pageId)
                                                                                    .start(STBaseConstants.userId
                                                                                            , data.classId.toString()
                                                                                            , data.lessonId.toString())
                                                                            dialog.dismiss()
                                                                        }
                                                                    }
                                                                }
                                                            })
                                                }
                                            }
                                        } else {
                                            ToastUtil.showToast(msg)
                                        }
                                    }
                                })
                                collectClickEvent("XSD_547")
                                return@forEach
                            }
                        }
                    }
                    R.id.rlPreCourseware -> {
                        data.tasks?.forEach {
                            if (it.type == 10) {
                                requestSiGaoPreview(data.classId.toString(), data.lessonNum.toString(), data.lessonId.toString(), it.gold)
                                collectClickEvent("XSD_602")
                            }
                            return@forEach
                        }
                    }
                    R.id.rlSpecialClass -> {
                        data.tasks?.forEach {
                            if (it.type == 5) {
                                LessonDetailPresenter.checkCourseActivation(data.classId.toString(), data.lessonId.toString(), 2, data.isActivation
                                        ?: 1, object : LessonDetailPresenter.OnCheckCourseActivationListener {
                                    override fun onCheckCourseStatus(status: Int, msg: String) {
                                        if (1 == status) {
                                            data.isActivation = 1
                                            if (it.hasNewVersion == 0) {
                                                gotoSpecialCoursePage("0", data.classId.toString(), data.lessonId.toString())
                                            } else {
                                                showSpecialClassUpdateDialog(data.classId.toString(), data.lessonId.toString())
                                            }
                                            collectClickEvent("XSD_550")
                                        } else {
                                            ToastUtil.showToast(msg)
                                        }
                                    }
                                })
                                return@forEach
                            }
                        }
                    }
                    R.id.rlHomework -> {
                        data.tasks?.forEach {
                            if (it.type == 1) {
                                if (it.finishStatus == 1) {
                                    //答题卡
                                    HomeworkOptions.instance
                                            .with(activity)
                                            .pad(pageId)
                                            .start(
                                                    JumpDoAnswerCardBuilder()
                                                            .setClassId(data.classId.toString())
                                                            .setLessonId(data.lessonId.toString())
                                                            .setFlag("0")
                                                            .setLessonTitle(data.lessonNum.toString())
                                                            .setSource("2")//1 来自交作业页面 2 来自列表
                                                            .setAction("0") //交作业动作 0 正常sdk 交作业 1 扫码sdk交作业
                                            )
                                } else {
                                    //交作业
                                    val jumpDoHomeworkBuilder = JumpDoHomeworkBuilder()
                                            .setClassId(data.classId.toString())
                                            .setLessonId(data.lessonId.toString())
                                            .setLessonTitle(data.lessonName)
                                            .setFlag("0")
                                            .setNum(data.lessonNum.toString())
                                            .setGoldNum(it.gold)
                                            .setAction("0") //交作业动作 0 正常sdk 交作业 1 扫码sdk交作业
                                    HomeworkOptions.instance
                                            .with(activity)
                                            .pad(pageId)
                                            .start(jumpDoHomeworkBuilder)
                                }
                                collectClickEvent("XSD_546")
                                return@forEach
                            }
                        }
                    }
                    R.id.rlEnglishOral -> {
                        data.tasks?.forEach {
                            if (it.type == 2) {
                                val doEnglishHomeworkBuilder = JumpDoEnglishHomeworkBuilder()
                                        .setClassId(data.classId.toString())
                                        .setLessonId(data.lessonId.toString())
                                        .setLessonName(data.lessonName)
                                        .setNum(data.lessonNum.toString())
                                        .setFlag("0")
                                EnglishHomeworkOptions.instance
                                        .with(activity)
                                        .pad(pageId)
                                        .start(doEnglishHomeworkBuilder)
                                collectClickEvent("XSD_548")
                                return@forEach
                            }
                        }
                    }
                    R.id.rlEnglishRecite -> {
                        data.tasks?.forEach {
                            if (it.type == 8) {
                                var url = "axx://AiEnglishRecite?classId=%s&lessonId=%s&version=%s&num=%s"
                                url = String.format(url, data.classId.toString(), data.lessonId.toString(), "", data.lessonNum.toString())
                                SchemeDispatcher.jumpPage(activity, url)
                                collectClickEvent("XSD_549")
                                return@forEach
                            }
                        }
                    }
                }
            }
        }
        rv_study.adapter = mTodoTaskAdapter
        rv_study.layoutManager = LinearLayoutManager(activity)
        rv_study.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mHeaderHeight = mHeaderView.height
                if (mHeaderHeight == 0) {
                    return
                }
                mHeaderHeight -= llTop.height
                val alphaHeight = llTop.height + TypeValue.dp2px(120F)
                mTotalDy -= dy

                val scale = (-mTotalDy.toFloat() / alphaHeight)
                val alpha = scale * 255
                if (-mTotalDy == 0) {
                    llTop.setBackgroundColor(Color.argb(0, 255, 255, 255))
                    topLine.visibility = View.GONE
                } else {
                    if (-mTotalDy <= alphaHeight) {
                        llTop.setBackgroundColor(Color.argb(alpha.toInt(), 255, 255, 255))
                        topLine.visibility = View.GONE
                    } else {
                        llTop.setBackgroundColor(Color.argb(255, 255, 255, 255))
                        topLine.visibility = View.VISIBLE
                    }
                }
                if (-mTotalDy < mHeaderHeight) {
                    tvTitle.text = getString(R.string.study_task)
                    //学习
                    mIsShowBackToTop = false
                    (activity as MainActivity).changeStudyTabStyle(R.drawable.icon_study_light, "学习")
                } else {
                    tvTitle.text = getString(R.string.lesson_task)
                    //回到顶部
                    mIsShowBackToTop = true
                    (activity as MainActivity).changeStudyTabStyle(R.drawable.tab_back_to_top, "回到顶部")
                }
            }
        })
        srlayout_study.setOnRefreshListener(this)
    }

    /***************************************************布局 ************************************************/

    /**
     * header模块
     */
    private val headerView: View
        get() {
            val view = View.inflate(activity, R.layout.header_main_fragment, null)
            mStudyTaskCard = view.findViewById(R.id.studyTaskCard)
            mStudyTaskCard?.setStudyCardListener(this)
            val tvMyClass = view.findViewById<TextView>(R.id.tvMyClass)
            val rlLessonInfo = view.findViewById<RelativeLayout>(R.id.rlLessonInfo)
            mLlEnglishStudy = view.findViewById(R.id.llEnglishStudy)
            mIvForeignEnglish = view.findViewById(R.id.ivForeignEnglish)
            mIvSceneEnglish = view.findViewById(R.id.ivSceneEnglish)
            mNullView = view.findViewById(R.id.nullView)

            mLlLiving = view.findViewById(R.id.llLiving)
            mLlMoreLesson = view.findViewById(R.id.llMoreLesson)

            tvMyClass?.setOnClickListener(this)
            rlLessonInfo?.setOnClickListener(this)
            mLlLiving?.setOnClickListener(this)
            mLlMoreLesson?.setOnClickListener(this)
            mIvForeignEnglish?.setOnClickListener(this)
            mIvSceneEnglish?.setOnClickListener(this)
            adjustStatusBarMargin(view.findViewById(R.id.vStatusBar))
            mHeaderView = view
            return view
        }

    private fun setEmptyStatus(emptyType: Int) {
        mEmptyType = emptyType
        mTodoTaskAdapter?.setNewData(null)
        mTodoTaskAdapter?.removeAllFooterView()
        mTodoTaskAdapter?.addFooterView(mEmptyLayout)
        when (mEmptyType) {
            1 -> {
                mEmptyView?.setEmptyTitleText("网络加载失败")
                mEmptyView?.setEmptyText("点击按钮重新加载或者查看网络是否连接")
                mEmptyView?.setButtonText("点击刷新")
                mEmptyView?.setEmptyVisibility(showImg = false, showText = true, showButton = true, showEmptyTitle = true)
            }
            2 -> {
                mEmptyView?.setEmptyTitleText("您尚未加入任何班级")
                mEmptyView?.setEmptyText("请联系机构老师索要班级码")
                mEmptyView?.setButtonText("加入班级")
                mEmptyView?.setEmptyVisibility(showImg = false, showText = true, showButton = true, showEmptyTitle = true)
            }
            3 -> {
                mEmptyView?.setEmptyTitleText("任务都完成了")
                mEmptyView?.setEmptyText("真棒！再接再厉哟~")
                mEmptyView?.setEmptyVisibility(showImg = false, showText = true, showButton = false, showEmptyTitle = true)
            }
        }
    }

    /**
     * 数据刷新
     * 在父页面的onResume方法执行
     */
    override fun onRefresh() {
        mPageNum = 1
        mTodoTaskAdapter?.setEnableLoadMore(false)
        mStudyTaskCard?.refreshData()
        requestStudyTask()
        requestTodoTask()
        requestFunctionEntrance()
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        srlayout_study.isRefreshing = true
        requestStudyTask()
        requestTodoTask()
        requestFunctionEntrance()
        requestHeaderInfo()
        requestTeacherMessageCount()
    }

    /**
     * 完成任务
     */
    fun completeTodoTask() {
        mPageNum = 1
        mTodoTaskAdapter?.setEnableLoadMore(false)
        requestTodoTask()
        scrollToTop()
    }

    /***************************************************网络请求 ******************************************************/

    /**
     * 获取首页 外教、情景对话权限
     */
    private fun requestFunctionEntrance() {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["type"] = "1" //类型 1 首页
        GSRequest.startRequest(AppApi.functionEntrance, paramMap, object : GSJsonCallback<FunctionEntranceBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<FunctionEntranceBean>) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                //收集异常日志
                if (result.body == null) {
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                val functionEntranceBean = result.body
                val foreignStatus = functionEntranceBean?.foreignStatus
                val englishWhiteList = functionEntranceBean?.englishWhiteList
                when (foreignStatus) {
                    0 -> {
                        mIvForeignEnglish?.visibility = View.GONE
                    }
                    1 -> {
                        mLlEnglishStudy?.visibility = View.VISIBLE
                        mIvForeignEnglish?.visibility = View.VISIBLE
                        mIvForeignEnglish?.setImageResource(R.drawable.icon_foreign_english)
                    }
                    2 -> {
                        mLlEnglishStudy?.visibility = View.VISIBLE
                        mIvForeignEnglish?.visibility = View.VISIBLE
                        mIvForeignEnglish?.setImageResource(R.drawable.icon_book_foreign_english)
                    }
                }
                when (englishWhiteList) {
                    0 -> {
                        mIvSceneEnglish?.visibility = View.GONE
                    }
                    1 -> {
                        mLlEnglishStudy?.visibility = View.VISIBLE
                        mIvSceneEnglish?.visibility = View.VISIBLE
                    }
                }
                if (mIvForeignEnglish?.visibility == View.VISIBLE && mIvSceneEnglish?.visibility == View.VISIBLE) {
                    mNullView?.visibility = View.GONE
                } else {
                    mNullView?.visibility = View.VISIBLE
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
            }
        })
    }

    /**
     * 请求学习任务
     */
    private fun requestStudyTask() {
        //处理请求 时间范围
        val startCalendar = mStudyTaskCard?.mWeekCalendars?.first()
        val startYear = startCalendar?.year ?: 2015
        val startMonth = startCalendar?.month ?: 8
        val startDay = startCalendar?.day ?: 1
        val startTime = startYear.toString() + "-" + (if (startMonth < 10) "0$startMonth" else startMonth) + "-" + if (startDay < 10) "0$startDay" else startDay
        val endCalendar = mStudyTaskCard?.mWeekCalendars?.last()
        val endYear = endCalendar?.year ?: 2015
        val endMonth = endCalendar?.month ?: 8
        val endDay = endCalendar?.day ?: 1
        val endTime = endYear.toString() + "-" + (if (endMonth < 10) "0$endMonth" else endMonth) + "-" + if (endDay < 10) "0$endDay" else endDay

        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["theStartTime"] = startTime
        paramMap["theEndOfTime"] = endTime
        paramMap["taskInfoType"] = "1"
        GSRequest.startRequest(AppApi.studyTask, paramMap, object : GSJsonCallback<StudyTaskData>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<StudyTaskData>) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                if (showResponseErrorMessage(result) == 0) {
                    mStudyTaskCard?.showStudyTask()
                    return
                }
                if (result.body == null) {
                    mStudyTaskCard?.showStudyTask()
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                val studyTaskData = result.body
                mTaskEveryDay = studyTaskData.taskEveryDay
                mStudyTaskCard?.setData(mTaskEveryDay)
                //绘制日期圆点
                mStudyTaskCard?.initCalendarData()
                //找到当前选中日期的数据，并展示
                mStudyTaskCard?.showStudyTask()
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                mStudyTaskCard?.showStudyTask()
                message ?: return
                ToastUtil.showToast(message)
            }
        })
    }

    /**
     * 请求讲次任务
     */
    private fun requestTodoTask() {
        if (!NetworkUtil.isConnected(context)) {
            ToastUtil.showToast("网络未连接")
            srlayout_study.isRefreshing = false
            if (mPageNum != 1) {
                mTodoTaskAdapter?.loadMoreFail()
            } else {
                //网络错误
                setEmptyStatus(1)
            }
            return
        }
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["pageNum"] = mPageNum.toString()
        GSRequest.startRequest(AppApi.todoTaskPage, paramMap, object : GSJsonCallback<TodoTaskData>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<TodoTaskData>) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                srlayout_study.isRefreshing = false
                mTodoTaskAdapter?.setEnableLoadMore(true)
                if (showResponseErrorMessage(result) == 0) {
                    if (mPageNum != 1) {
                        mTodoTaskAdapter?.loadMoreFail()
                    } else {
                        //网络错误
                        setEmptyStatus(1)
                    }
                    return
                }
                if (result.body == null) {
                    if (mPageNum != 1) {
                        mTodoTaskAdapter?.loadMoreFail()
                    } else {
                        //网络错误
                        setEmptyStatus(1)
                    }
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                val todoTaskData = result.body
                mTotalPageNum = todoTaskData.pageTotal ?: 0
                mPageNum = todoTaskData.pageNum ?: 1
                if (todoTaskData.hasClass == 1) {
                    val todoTaskList = todoTaskData.list
                    if (todoTaskList.isNullOrEmpty()) {
                        if (mPageNum == 1) {
                            //任务已完成
                            setEmptyStatus(3)
                        } else {
                            mTodoTaskAdapter?.loadMoreFail()
                        }
                    } else {
                        if (mPageNum == 1) {
                            mTodoTaskAdapter?.setNewData(todoTaskList)
                        } else {
                            mTodoTaskAdapter?.addData(todoTaskList)
                        }
                        mTodoTaskAdapter?.removeAllFooterView()
                        if (mPageNum >= mTotalPageNum) {
                            mTodoTaskAdapter?.loadMoreEnd()
                        } else {
                            mPageNum++
                            mTodoTaskAdapter?.loadMoreComplete()
                        }
                    }
                } else {
                    //未加入班级
                    setEmptyStatus(2)
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                if (mPageNum != 1) {
                    mTodoTaskAdapter?.loadMoreFail()
                } else {
                    //网络错误
                    setEmptyStatus(1)
                }
                srlayout_study.isRefreshing = false
                mTodoTaskAdapter?.setEnableLoadMore(true)
                message ?: return
                ToastUtil.showToast(message)
            }
        })
    }

    /**
     * 获取学生信息接口
     */
    private fun requestHeaderInfo() {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        GSRequest.startRequest(GSAPI.student_studentInfo, paramMap, object : GSJsonCallback<student_studentInfoBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<student_studentInfoBean>) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                //收集异常日志
                if (result.body == null) {
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse)
                    return
                }
                studentInfoBean = result.body
                STBaseConstants.userInfo?.run {
                    institutionName = studentInfoBean?.institutionName
                    path = studentInfoBean?.avatarUrl
                    truthName = studentInfoBean?.name
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {}
        })
    }

    /**
     * 首页消息
     */
    private fun requestTeacherMessageCount() {
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        GSRequest.startRequest(GSAPI.Report_teacherMessageCount, paramMap, object : GSStringCallback() {
            override fun onResponseSuccess(response: Response<*>, code: Int, result: String) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                if (TextUtils.isEmpty(result)) {
                    tvMsgCount?.visibility = View.GONE
                    return
                }
                try {
                    val body = JSONObject(result)
                    val data = body.optJSONObject("data")
                    val messageCount = data.optInt("messageCount")
                    if (messageCount == 0) {
                        tvMsgCount?.visibility = View.GONE
                    } else {
                        tvMsgCount?.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                tvMsgCount?.visibility = View.GONE
            }
        })
    }

    /**
     * 获取英语预习地址
     */
    private fun requestEnglishPreview(classId: String?, lessonId: String?, lessonNum: String?, lessonName: String?) {
        showLoadingProcessDialog()
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["classId"] = classId ?: ""
        paramMap["lessonId"] = lessonId ?: ""
        GSRequest.startRequest(AppApi.getPreEnglishResource, paramMap, object : GSJsonCallback<EnglishPreviewBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<EnglishPreviewBean>) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                dismissLoadingProcessDialog()
                result.body ?: return
                if (result.body.type == 0) {
                    PreExerciseOptions.instance
                            .with(activity)
                            .pad(pageId)
                            .start(
                                    JumpDoPreExerciseBuilder()
                                            .setUrl(result.body.url)
                                            .setClassId(classId)
                                            .setLessonId(lessonId)
                            )
                } else {
                    val title = "第${lessonNum}讲 $lessonName"
                    PreExerciseOptions.instance
                            .with(activity)
                            .pad(pageId)
                            .start(
                                    JumpDoPreVideoBuilder()
                                            .setUrl(result.body.url)
                                            .setTitle(title)
                                            .setClassId(classId)
                                            .setLessonId(lessonId)
                                            .setPlayType(PlayType.preEnglishExerciseVideo.toString())
                            )
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                dismissLoadingProcessDialog()
                ToastUtil.showToast(message)
            }
        })
    }

    /**
     * 获取外教预习内容
     */
    private fun requestSiGaoPreview(classId: String?, lessonNum: String?, lessonId: String?, gold: Int?) {
        showLoadingProcessDialog()
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["classId"] = classId ?: ""
        paramMap["lessonNum"] = lessonNum ?: ""
        paramMap["lessonId"] = lessonId ?: ""
        GSRequest.startRequest(TeacherOnLineApi.getSiGaoPreview, GSRequest.GET, paramMap, object : GSJsonCallback<PreviewBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<PreviewBean>) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                dismissLoadingProcessDialog()
                if (result.status == 1) {
                    if (result.body.url.isNullOrEmpty()) {
                        ToastUtil.showToast("暂无课件")
                    } else {
                        TeacherOnLineOptions.instance
                                .with(activity)
                                .pad(pageId)
                                .start(
                                        JumpDoShowPPTBuilder()
                                                .setUrl(result.body.url)
                                                .setClassId(classId)
                                                .setLessonId(lessonId)
                                                .setGold(gold)
                                )
                    }
                } else {
                    ToastUtil.showToast("获取预习地址失败")
                }
            }

            override fun onResponseError(response: Response<*>?, code: Int, message: String?) {
                if (activity == null || activity?.isFinishing == true) {
                    return
                }
                dismissLoadingProcessDialog()
                message ?: return
                ToastUtil.showToast(message)
            }
        })
    }

    /**
     * 跳转专题课
     * @param useNewVersion 是否使用新版本视频 0不使用 1使用
     */
    private fun gotoSpecialCoursePage(useNewVersion: String?, classId: String?, lessonId: String?) {
        val doSpecialCourseBuilder = JumpDoSpecialCourseBuilder()
                .setStudentId(STBaseConstants.userId)
                .setClassId(classId)
                .setLessonId(lessonId)
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
    private fun showSpecialClassUpdateDialog(classId: String?, lessonId: String?) {
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
                                gotoSpecialCoursePage("1", classId, lessonId)
                            }
                            R.id.tvCancel -> {
                                dialog.dismiss()
                                gotoSpecialCoursePage("0", classId, lessonId)
                            }
                        }
                    }
                })
    }

    /**
     * 首页弹框结束
     */
    fun onMainActivityDialogCompleted() {
    }

    /**
     * 点击监听事件
     */
    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.ivScan -> {
                toScanActivity()
                GSLogUtil.collectClickLog(pageId, "as101_clk_saoyisao", "")
            }
            R.id.ivMsg -> {
                val json = JSONObject()
                json.put("userId", STBaseConstants.userId)
                val s = "axx://rnPage?url=%s&param=%s"
                val url = String.format(s, "classMessage", json.toString())
                SchemeDispatcher.jumpPage(activity, url)
                GSLogUtil.collectClickLog(pageId, "as_clk_study_teacher_say", "")
            }
            R.id.tvMyClass -> {
                val url = "axx://myClass"
                SchemeDispatcher.jumpPage(activity, url)
                GSLogUtil.collectClickLog(pageId, "as101_clk_wodebanji", "")
            }
            R.id.rlLessonInfo -> {
                mCurrentTaskEveryDayBean = mTaskEveryDay?.find {
                    it.taskYear == mStudyTaskCard?.mYear && it.taskMonth == mStudyTaskCard?.mMonth && it.taskDay == mStudyTaskCard?.mDay
                }
                mStudyTaskInfoBean = mCurrentTaskEveryDayBean?.studyTaskInfoList?.firstOrNull() ?: return
                if (mStudyTaskInfoBean?.type == 1) {
                    //在线 课程学习
                    var url = "axx://courseDetailedPage?courseId=%s&gsId=%s&courseTypeFlag=%s&lectureId=%s&lecturePackageId=%s&lectureType=%s"
                    url = String.format(url, mStudyTaskInfoBean?.axxOnlineTol?.courseId, mStudyTaskInfoBean?.axxOnlineTol?.gaoSiId, mStudyTaskInfoBean?.axxOnlineTol?.courseTypeFlag, mStudyTaskInfoBean?.axxOnlineTol?.lectureId, mStudyTaskInfoBean?.axxOnlineTol?.lecturePackageId, mStudyTaskInfoBean?.axxOnlineTol?.lectureType)
                    SchemeDispatcher.jumpPage(activity, url)
                } else {
                    var url = "axx://lessonDetail?classId=%s&lessonId=%s"
                    url = String.format(url, mStudyTaskInfoBean?.classId, mStudyTaskInfoBean?.lessonId)
                    SchemeDispatcher.jumpPage(activity, url)
                }
                collectClickEvent("XSD_558")
            }
            R.id.ivForeignEnglish -> {
                TeacherOnLineOptions.instance
                        .with(activity)
                        .pad(pageId)
                        .start(JumpDoMainActivityBuilder()
                                .setUserId(STBaseConstants.userId)
                        )
                collectClickEvent("XSD_545")
            }
            R.id.ivSceneEnglish -> {
                val url = "axx://aiSceneList"
                SchemeDispatcher.jumpPage(activity, url)
                collectClickEvent("as101_clk_Dialogue")
            }
            R.id.llLiving -> {
                //直播、回放
                mCurrentTaskEveryDayBean = mTaskEveryDay?.find {
                    it.taskYear == mStudyTaskCard?.mYear && it.taskMonth == mStudyTaskCard?.mMonth && it.taskDay == mStudyTaskCard?.mDay
                }
                mStudyTaskInfoBean = mCurrentTaskEveryDayBean?.studyTaskInfoList?.firstOrNull() ?: return
                when (mStudyTaskInfoBean?.type) {
                    1 -> {
                        //在线课TOL
                        if (mStudyTaskCard?.mIsPlayBack == true) {
                            collectClickEvent("XSD_566")
                        } else {
                            collectClickEvent("XSD_565")
                        }
                        requestPermissionThenLiveIn()
                    }
                    2 -> {
                        //在线小班
                        setAxxLiveListener()
                    }
                    3, 4 -> {
                        //双师课、AI好课
                        setDtLiveListener()
                    }
                    5 -> {
                        //在线外教课
                        if (mStudyTaskCard?.mIsPlayBack == true) {
                            requestSiGaoRecord()
                        } else {
                            requestSiGaoEntrance()
                        }
                    }
                }
            }
            R.id.llMoreLesson -> {
                mCurrentTaskEveryDayBean = mTaskEveryDay?.find {
                    it.taskYear == mStudyTaskCard?.mYear && it.taskMonth == mStudyTaskCard?.mMonth && it.taskDay == mStudyTaskCard?.mDay
                }
                val intent = Intent(activity, MoreLessonActivity::class.java)
                intent.putExtra("data", mCurrentTaskEveryDayBean)
                startActivity(intent)
                collectClickEvent("XSD_514")
            }
        }
    }

    /**
     * 爱学习在线直播点击事件
     */
    public fun setAxxLiveListener() {
        mStudyTaskCard ?: return
        if (mStudyTaskCard?.mIsPlayBack == true) {
            if (TextUtils.isEmpty(mStudyTaskInfoBean?.axxOnline?.replayUrl)) { //可能是录制间
                enterRoom(2)
            } else {
                var url = "axx://playBack?vUrl=%s&title=%s&classId=%s&jcId=%s&pad=%s&playType=%s"
                val title = "第${mStudyTaskInfoBean?.lessonNum}讲 ${mStudyTaskInfoBean?.lessonName}"
                url = String.format(url, mStudyTaskInfoBean?.axxOnline?.replayUrl, title, mStudyTaskInfoBean?.classId, mStudyTaskInfoBean?.lessonId, pageId, PlayType.axxOnLineVideo.toString())
                SchemeDispatcher.jumpPage(activity, url)
            }
            requestAxxOnLineJoin("1")
            collectClickEvent("XSD_560")
        } else {
            enterAxxOnlineRoom()
        }
    }

    /**
     * 双师在线点击事件
     */
    public fun setDtLiveListener() {
        mStudyTaskCard ?: return
        if (mStudyTaskCard?.mIsPlayBack == true) {
            if (mStudyTaskInfoBean?.dtLive?.aiLesson == 1) {
                collectClickEvent("XSD_564")
            } else {
                collectClickEvent("XSD_562")
            }
            var url = "axx://playBack?vUrl=%s&title=%s&classId=%s&jcId=%s&pad=%s&playType=%s"
            val title = "第${mStudyTaskInfoBean?.lessonNum}讲 ${mStudyTaskInfoBean?.lessonName}"
            url = String.format(url, mStudyTaskInfoBean?.dtLive?.replayUrl, title, mStudyTaskInfoBean?.classId, mStudyTaskInfoBean?.lessonId, pageId, "7")
            SchemeDispatcher.jumpPage(activity, url)
            requestSaveDtReplay()
        } else {
            val endTime = if ((mStudyTaskInfoBean?.dtLive?.lessonEndTime
                            ?: 0L) == 0L) {
                (mStudyTaskInfoBean?.dtLive?.lessonBeginTime
                        ?: 0L) + 120 * 60 * 1000L
            } else {
                (mStudyTaskInfoBean?.dtLive?.lessonEndTime ?: 0L)
            }
            if (mStudyTaskInfoBean?.dtLive?.aiLesson == 1) {
                DTLiveOptions.instance
                        .with(activity)
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
                        .with(activity)
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
                        LiveManager.newInstance().enterRoom(activity!!
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
        showLoadingProcessDialog()
        TeacherOnLineSDKConfig.requestGetSiGaoEntrance(STBaseConstants.userId
                , mStudyTaskInfoBean?.foreignCourse?.fbClassId.toString()
                , object : TeacherOnLineSDKConfig.LiveCallBackListener {
            override fun onSuccess(liveEntranceBean: LiveEntranceBean) {
                if (activity?.isFinishing == true || activity?.isDestroyed == true) {
                    return
                }
                dismissLoadingProcessDialog()
                val map = JSON.parseObject(JSON.toJSONString(liveEntranceBean)) as Map<String, Any>
                val params = mutableMapOf<String, Any>()
                params.putAll(map)
                params["serial"] = liveEntranceBean.roomId ?: ""
                params["nickname"] = STBaseConstants.userInfo.truthName
                params["userid"] = liveEntranceBean.uid ?: ""
                params["clientType"] = "2"
                TeacherOnLineOptions.instance
                        .with(activity)
                        .pad(pageId).start(JumpDoEnterRoomBuilder().setParams(params))
                collectClickEvent("XSD_567")
            }

            override fun onFail(message: String?) {
                if (activity?.isFinishing == true || activity?.isDestroyed == true) {
                    return
                }
                dismissLoadingProcessDialog()
                message ?: return
                ToastUtil.showToast(message)
            }
        })
    }

    /**
     * 获取外教回放信息
     */
    private fun requestSiGaoRecord() {
        showLoadingProcessDialog()
        val paramMap = HashMap<String, String>()
        paramMap["studentId"] = STBaseConstants.userId
        paramMap["fbClassId"] = mStudyTaskInfoBean?.foreignCourse?.fbClassId.toString()
        GSRequest.startRequest(TeacherOnLineApi.getSiGaoRecord, GSRequest.GET, paramMap, object : GSJsonCallback<PlayBackBean>() {
            override fun onResponseSuccess(response: Response<*>?, code: Int, result: GSHttpResponse<PlayBackBean>) {
                if (activity?.isFinishing == true || activity?.isDestroyed == true) {
                    return
                }
                dismissLoadingProcessDialog()
                if (result.status == 1) {
                    if (result.body.status == 1) {
                        TeacherOnLineOptions.instance
                                .with(activity)
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
                if (activity?.isFinishing == true || activity?.isDestroyed == true) {
                    return
                }
                dismissLoadingProcessDialog()
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

    private fun toScanActivity() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(Intent(activity, ScanActivity::class.java))
                } else {
                    val perms = arrayOf(Manifest.permission.CAMERA)
                    requestPermissions(perms, PERMISSION_REQUEST_CAMERA)
                }
            } else {
                startActivity(Intent(activity, ScanActivity::class.java))
            }
        } catch (e: Exception) {
            LOGGER.log(e)
        }
    }

    private fun requestPermissionThenLiveIn(){
        val doPrepareResult = "axx://newGroupTransfer?gsId=%s&courseId=%s&lessonId=%s&isBackPlay=%s&isPrivacyShow=%s&pageId=%s&diyLessonId=%s&liveRoomType=%s&lecturePackageId=%s&lectureId=%s&lectureType=%s&courseTypeFlag=%s"
        val url = String.format(doPrepareResult, mStudyTaskInfoBean?.axxOnlineTol?.gaoSiId.toString(), mStudyTaskInfoBean?.axxOnlineTol?.courseId, mStudyTaskInfoBean?.lessonId, mStudyTaskCard?.mIsPlayBack,
                mStudyTaskInfoBean?.axxOnlineTol?.privacyShow, pageId, mStudyTaskInfoBean?.axxOnlineTol?.diyLessonId, mStudyTaskInfoBean?.axxOnlineTol?.liveRoomType, mStudyTaskInfoBean?.axxOnlineTol?.lecturePackageId, mStudyTaskInfoBean?.axxOnlineTol?.lectureId,
                mStudyTaskInfoBean?.axxOnlineTol?.lectureType, mStudyTaskInfoBean?.axxOnlineTol?.courseTypeFlag)
        SchemeDispatcher.jumpPage(activity, url)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(Intent(activity, ScanActivity::class.java))
            } else {
                ToastUtil.showToast("以保证您能正常使用应用，请前往“设置→应用权限”中打开相机权限")
            }
        }
    }

    /**
     * 滚动到顶部
     */
    fun scrollToTop() {
        rv_study?.scrollToPosition(0)
        mTotalDy = 0
    }

    /**
     * 停止滚动
     */
    fun stopScroll() {
        rv_study?.stopScroll()
    }

    override fun onRequestData() {
        requestStudyTask()
    }

    override fun onResume() {
        super.onResume()
        if (!mIsFirstVisiable) {
            mStudyTaskCard?.updateCurrentDate()
            mStudyTaskCard?.refreshData()
            requestStudyTask()
        }
        mIsFirstVisiable = false
    }
}
