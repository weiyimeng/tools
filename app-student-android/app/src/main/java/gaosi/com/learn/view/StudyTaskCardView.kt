package gaosi.com.learn.view

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import com.bumptech.glide.Glide
import com.gsbaselib.utils.LangUtil
import com.gsbiloglib.builder.GSConstants
import com.gsbiloglib.log.GSLogUtil
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import gaosi.com.learn.R
import gaosi.com.learn.bean.main.ClassInfo
import gaosi.com.learn.bean.main.StudyTaskInfoBean
import gaosi.com.learn.bean.main.TaskEveryDayBean
import gaosi.com.learn.studentapp.classlesson.status.SubjectId
import kotlinx.android.synthetic.main.include_study_task_layout.view.*
import kotlinx.android.synthetic.main.study_task_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * description: 学习任务卡片
 * created by huangshan on 2020/6/10 上午11:40
 */
class StudyTaskCardView : CardView, CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener, CalendarView.OnMonthChangeListener, CalendarView.OnWeekChangeListener, View.OnClickListener {

    var mYear = 0
    var mMonth = 0
    var mDay = 0

    //是否回放
    var mIsPlayBack = false
    //是否月份切换，月份切换会触发onCalendarSelect
    private var mIsMonthChange = false
    public var mWeekCalendars = ArrayList<Calendar>()
    private var mTaskEveryDay: ArrayList<TaskEveryDayBean>? = null
    private var mCurrentTaskEveryDayBean: TaskEveryDayBean? = null
    private var mStudyTaskInfoBean: StudyTaskInfoBean? = null

    private var mCallBack: CallBack? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }
    rv_study
    private fun initView() {
        View.inflate(context, R.layout.study_task_layout, this)
        mWeekCalendars = calendarView.currentWeekCalendars as ArrayList<Calendar>
        mYear = calendarView.curYear
        mMonth = calendarView.curMonth
        mDay = calendarView.curDay
        tvCurrentDate.text = "" + mYear + "年" + mMonth + "月"
        calendarView.setOnCalendarSelectListener(this)
        calendarView.setOnYearChangeListener(this)
        calendarView.setOnMonthChangeListener(this)
        calendarView.setOnWeekChangeListener(this)

        tvToday.setOnClickListener(this)
        ivPreMonth?.setOnClickListener(this)
        ivNextMonth?.setOnClickListener(this)
        tvMyClass?.setOnClickListener(this)
    }

    public fun setData(list: ArrayList<TaskEveryDayBean>?) {
        mTaskEveryDay = list
    }

    /**
     * 绘制日历圆点
     */
    fun initCalendarData() {
        mTaskEveryDay ?: return
        val map = HashMap<String, Calendar?>()
        val calendar = Calendar()
        calendar.year = calendarView?.curYear ?: 0
        calendar.month = calendarView?.curMonth ?: 0
        calendar.day = calendarView?.curDay ?: 0
        mWeekCalendars = calendarView?.currentWeekCalendars as ArrayList<Calendar>
        val calendarList = ArrayList<Calendar>()
        mTaskEveryDay?.let {
            it.forEach { data ->
                val bean = Calendar()
                bean.year = data.taskYear ?: 0
                bean.month = data.taskMonth ?: 0
                bean.day = data.taskDay ?: 0
                calendarList.add(bean)
            }
        }
        calendarList.forEach { ca ->
            if (ca >= calendar) {
                val s = getSchemeCalendar(ca.year, ca.month, ca.day, Color.parseColor("#1ED278"), "")
                map[s.toString()] = s
            } else {
                val s = getSchemeCalendar(ca.year, ca.month, ca.day, Color.parseColor("#D8DCE2"), "")
                map[s.toString()] = s
            }
        }
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        calendarView?.setSchemeDate(map)
    }

    private fun getSchemeCalendar(year: Int, month: Int, day: Int, color: Int, text: String): Calendar? {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color //如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text
        return calendar
    }

    /**
     * 显示学习任务
     */
    fun showStudyTask() {
        tvTime.stop()
        if (mTaskEveryDay == null) {
            //显示网络加载失败
            llEmptyView.visibility = View.VISIBLE
            ivEmpty.setImageResource(R.drawable.icon_net_error)
            tvEmpty.text = context.getString(R.string.error_text_net)
        } else {
            mCurrentTaskEveryDayBean = mTaskEveryDay?.find {
                it.taskYear == mYear && it.taskMonth == mMonth && it.taskDay == mDay
            }
            if (mCurrentTaskEveryDayBean == null) {
                //本日暂无课程
                llMoreLesson.visibility = View.GONE
                studyTaskInfoLayout.visibility = View.INVISIBLE

                llEmptyView.visibility = View.VISIBLE
                ivEmpty.setImageResource(R.drawable.icon_today_no_course)
                tvEmpty.text = context.getString(R.string.error_text_no_courses_today)
            } else {
                if (mCurrentTaskEveryDayBean?.studyTaskInfoList.isNullOrEmpty()) {
                    if (mCurrentTaskEveryDayBean?.todayTaskStatus == 1) {
                        //显示任务完成
                        llMoreLesson.visibility = View.GONE
                        studyTaskInfoLayout.visibility = View.INVISIBLE

                        llEmptyView.visibility = View.VISIBLE
                        ivEmpty.setImageResource(R.drawable.icon_today_course_complete)
                        tvEmpty.text = context.getString(R.string.error_text_today_course_is_completed)
                    } else {
                        //本日暂无课程
                        llMoreLesson.visibility = View.GONE
                        studyTaskInfoLayout.visibility = View.INVISIBLE

                        llEmptyView.visibility = View.VISIBLE
                        ivEmpty.setImageResource(R.drawable.icon_today_no_course)
                        tvEmpty.text = context.getString(R.string.error_text_no_courses_today)
                    }
                } else {
                    llEmptyView.visibility = View.GONE
                    studyTaskInfoLayout.visibility = View.VISIBLE
                    val size = mCurrentTaskEveryDayBean?.studyTaskInfoList?.size ?: 0
                    mStudyTaskInfoBean = mCurrentTaskEveryDayBean?.studyTaskInfoList?.first()
                    if (mStudyTaskInfoBean?.subjectId == 0) {
                        //多学科不显示
                        ivSubject.visibility = View.GONE
                    } else {
                        ivSubject.visibility = View.VISIBLE
                        ivSubject.setImageResource(SubjectId.getSubjectLabel(mStudyTaskInfoBean?.subjectId))
                    }
                    tvLessonName.text = "第" + mStudyTaskInfoBean?.lessonNum + "讲 " + mStudyTaskInfoBean?.lessonName
                    tvClassName.text = mStudyTaskInfoBean?.className

                    var classInfo: ClassInfo? = null
                    when (mStudyTaskInfoBean?.type) {
                        1 -> classInfo = mStudyTaskInfoBean?.axxOnlineTol
                        2 -> classInfo = mStudyTaskInfoBean?.axxOnline
                        3, 4 -> classInfo = mStudyTaskInfoBean?.dtLive
                        5 -> classInfo = mStudyTaskInfoBean?.foreignCourse
                        6 -> classInfo = mStudyTaskInfoBean?.axxOffline
                    }
                    tvTimeTitle.text = "上课时间"
                    tvTimeTitle.visibility = View.VISIBLE
                    tvTime.visibility = View.VISIBLE
                    val beginTimeDate = Date(classInfo?.lessonBeginTime ?: 0)
                    val endTimeDate = Date(classInfo?.lessonEndTime ?: 0)
                    val beginHHmmTimeString = LangUtil.date2String("HH:mm", beginTimeDate)
                    val endHHmmTimeString = LangUtil.date2String("HH:mm", endTimeDate)
                    tvTime.text = "$beginHHmmTimeString-$endHHmmTimeString"
                    if (mStudyTaskInfoBean?.type == 6) {
                        //线下课
                        when (classInfo?.lessonStatus) {
                            1 -> setUnStartStatus("线下课未开始")
                            3 -> setUnStartStatus("线下课进行中")
                            6 -> {
                                tvLessonStatus.text = "线下课已结束"
                                tvLessonStatus.setTextColor(Color.parseColor("#939CB6"))
                                tvLessonStatus.visibility = View.VISIBLE
                                llLiving.visibility = View.GONE
                            }
                            else -> {
                                studyTaskInfoLayout.visibility = View.INVISIBLE
                            }
                        }
                    } else {
                        when (classInfo?.lessonStatus) {
                            1 -> {
                                when (mStudyTaskInfoBean?.type) {
                                    1,2 -> {
                                        //在线课TOL 在线小班
                                        setUnStartStatus("直播未开始")
                                    }
                                    3 -> {
                                        //双师课
                                        setUnStartStatus("双师课未开始")
                                    }
                                    4 -> {
                                        //AI好课
                                        setUnStartStatus("AI好课未开始")
                                    }
                                    5 -> {
                                        //外教课
                                        setUnStartStatus("外教课未开始")
                                    }
                                }
                            }
                            2 -> {
                                mIsPlayBack = false
                                tvLessonStatus.visibility = View.GONE
                                llLiving.visibility = View.VISIBLE
                                Glide.with(context).asGif().load(R.drawable.app_icon_living).into(ivLiveStatus)
                                tvLiveStatus.text = "进入直播"
                                when (mStudyTaskInfoBean?.type) {
                                    4 -> {
                                        //AI好课
                                        tvTimeTitle.text = "即将开课"
                                    }
                                    1,2,3,5 -> {
                                        //在线课TOL 在线小班 双师课 外教课
                                        tvTimeTitle.text = "距离上课"
                                        val elapseTime = classInfo.lessonBeginTime?.minus(System.currentTimeMillis())
                                                ?: 0L
                                        tvTime.start(elapseTime, object : CountDownTimeTextView.OnFinishListener {
                                            override fun onFinish() {
                                                tvTime.stop()
                                                tvTimeTitle.text = "上课时间"
                                                tvTime.text = "$beginHHmmTimeString-$endHHmmTimeString"
                                            }
                                        })
                                        tvTime.start()
                                    }
                                }
                            }
                            3 -> {
                                setLivingStatus()
                            }
                            4 -> {
                                setPlayBackingStatus()
                            }
                            5 -> {
                                setPlayBackStatus()
                            }
                        }
                    }
                    if (size > 1) {
                        llMoreLesson.visibility = View.VISIBLE
                    } else {
                        llMoreLesson.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setUnStartStatus(text: String) {
        tvLessonStatus.text = text
        tvLessonStatus.setTextColor(Color.parseColor("#FAAD14"))
        tvLessonStatus.visibility = View.VISIBLE
        llLiving.visibility = View.GONE
    }

    private fun setLivingStatus() {
        mIsPlayBack = false
        tvTimeTitle.text = "正在直播"
        tvLessonStatus.visibility = View.GONE
        llLiving.visibility = View.VISIBLE
        Glide.with(context).asGif().load(R.drawable.app_icon_living).into(ivLiveStatus)
        tvLiveStatus.text = "进入直播"
    }

    private fun setPlayBackingStatus() {
        tvLessonStatus.text = "回放生成中"
        tvLessonStatus.setTextColor(Color.parseColor("#939CB6"))
        tvLessonStatus.visibility = View.VISIBLE
        llLiving.visibility = View.GONE
    }

    private fun setPlayBackStatus() {
        mIsPlayBack = true
        tvLessonStatus.visibility = View.GONE
        llLiving.visibility = View.VISIBLE
        ivLiveStatus.setImageResource(R.drawable.app_icon_live_play)
        tvLiveStatus.text = "观看回放"
    }

    /**
     * 刷新日历数据
     */
    fun refreshData() {
        //刷新前日期
        val calendar = Calendar()
        calendar.year = mYear
        calendar.month = mMonth
        calendar.day = mDay

        calendarView.scrollToCurrent()
        mYear = calendarView.curYear
        mMonth = calendarView.curMonth
        mDay = calendarView.curDay

        mWeekCalendars = calendarView.currentWeekCalendars as ArrayList<Calendar>
        //刷新前的日期不在当周内，隐藏内容区域
        if (!(calendar >= mWeekCalendars.first() && calendar <= mWeekCalendars.last())) {
            llMoreLesson.visibility = View.GONE
            studyTaskInfoLayout.visibility = View.INVISIBLE
            llEmptyView.visibility = View.GONE
        }
    }

    /**
     * 更新当前日期
     */
    fun updateCurrentDate() {
        calendarView.updateCurrentDate()
    }

    override fun onClick(v: View?) {
        when (v) {
            tvToday -> {
                val calendar = Calendar()
                calendar.year = calendarView.curYear
                calendar.month = calendarView.curMonth
                calendar.day = calendarView.curDay

                calendarView.scrollToCurrent()
                mYear = calendarView.curYear
                mMonth = calendarView.curMonth
                mDay = calendarView.curDay
                if (calendar >= mWeekCalendars.first() && calendar <= mWeekCalendars.last()) {
                    mWeekCalendars = calendarView.currentWeekCalendars as ArrayList<Calendar>
                    showStudyTask()
                } else {
                    mWeekCalendars = calendarView.currentWeekCalendars as ArrayList<Calendar>
                    mCallBack?.onRequestData()
                }
                GSLogUtil.collectClickLog(GSConstants.P?.getCurrRefer(), "XSD_504", "")
            }
            ivPreMonth -> {
                if (mMonth - 1 < 1) {
                    mMonth = 12
                    mYear -= 1
                } else {
                    mMonth -= 1
                }
                calendarView.scrollToCalendar(mYear, mMonth, 1)
                GSLogUtil.collectClickLog(GSConstants.P?.getCurrRefer(), "XSD_505", "")
            }
            ivNextMonth -> {
                if (mMonth + 1 > 12) {
                    mMonth = 1
                    mYear += 1
                } else {
                    mMonth += 1
                }
                calendarView.scrollToCalendar(mYear, mMonth, 1)
                GSLogUtil.collectClickLog(GSConstants.P?.getCurrRefer(), "XSD_506", "")
            }
        }
    }


    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        calendar ?: return
        //设置AxxWeekView当前选中月份
        AxxWeekView.mSelectMonth = calendar.month
        mYear = calendar.year
        mMonth = calendar.month
        mDay = calendar.day
        tvCurrentDate?.text = "" + mYear + "年" + mMonth + "月"
        if (mYear == calendarView.curYear && mMonth == calendarView.curMonth && mDay == calendarView.curDay) {
            tvToday.visibility = View.GONE
        } else {
            tvToday.visibility = View.VISIBLE
        }
        if (!mIsMonthChange) {
            showStudyTask()
        } else {
            mIsMonthChange = false
        }
        if (isClick) {
            GSLogUtil.collectClickLog(GSConstants.P?.getCurrRefer(), "XSD_507", "")
        }
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {
    }

    override fun onYearChange(year: Int) {
        mWeekCalendars = calendarView.currentWeekCalendars as ArrayList<Calendar>
    }

    override fun onMonthChange(year: Int, month: Int) {
        //当前选中日期
        val calendar = Calendar()
        calendar.year = mYear
        calendar.month = mMonth
        calendar.day = mDay
        //如果月份切换回调 不在当周内，执行网络请求
        if (!(calendar >= mWeekCalendars.first() && calendar <= mWeekCalendars.last())) {
            mIsMonthChange = true
            mWeekCalendars = calendarView.currentWeekCalendars as ArrayList<Calendar>
            llMoreLesson.visibility = View.GONE
            studyTaskInfoLayout.visibility = View.INVISIBLE
            llEmptyView.visibility = View.GONE
            mCallBack?.onRequestData()
        }
    }

    override fun onWeekChange(weekCalendars: MutableList<Calendar>?) {
        mWeekCalendars = weekCalendars as ArrayList<Calendar>
        llMoreLesson.visibility = View.GONE
        studyTaskInfoLayout.visibility = View.INVISIBLE
        llEmptyView.visibility = View.GONE
        mCallBack?.onRequestData()
    }

    fun setStudyCardListener(callBack: CallBack) {
        mCallBack = callBack
    }

    interface CallBack {
        fun onRequestData()
    }

}
