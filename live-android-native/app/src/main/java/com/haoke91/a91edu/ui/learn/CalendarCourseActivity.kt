package com.haoke91.a91edu.ui.learn

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.gaosiedu.scc.sdk.android.api.user.course.calendar.month.LiveSccUserCourseCalendarMonthRequest
import com.gaosiedu.scc.sdk.android.api.user.course.calendar.month.LiveSccUserCourseCalendarMonthResponse
import com.gaosiedu.scc.sdk.android.api.user.course.last.LiveSccUserCourseLastRequest
import com.gaosiedu.scc.sdk.android.api.user.course.last.LiveSccUserCourseLastResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.TodayCourseAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.manager.UserManager
import com.lzy.okgo.OkGo
import kotlinx.android.synthetic.main.activity_calendarcourse.*
import java.util.*

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/7/13 15:59
 */
class CalendarCourseActivity : BaseActivity() {
    //    private lateinit var mCalendarLayout: CalendarLayout
    //    private lateinit var mCalendarView: CalendarView
    //    private lateinit var mRecyclerView: WrapRecyclerView
    //    private lateinit var mtv_selectedDate: TextView?
    private val SEARCHMONTH = "searchMonth"
    private val SEARCHDAY = "searchDay"
    private var mLiveAdapter: TodayCourseAdapter? = null
    private var mToday: com.haibin.calendarview.Calendar? = null
    //    private var mEmptyView: EmptyView? = null
    
    override fun getLayout(): Int {
        return R.layout.activity_calendarcourse
    }
    
    override fun initialize() {
        initCalendar()
    }
    
    /**
     * calendar init
     */
    private fun initCalendar() {
        //        mtv_selectedDate = id(R.id.tv_selectedDate)
        //        mCalendarLayout = id(R.id.calendarLayout)
        //        mCalendarView = id(R.id.calendarView)
        //        mRecyclerView = id(R.id.recyclerView)
        calendarLayout.expand()
        val date = Calendar.getInstance(Locale.CHINA)
        mToday = com.haibin.calendarview.Calendar()
        mToday!!.year = date.get(Calendar.YEAR)
        mToday!!.month = date.get(Calendar.MONTH) + 1
        mToday!!.day = date.get(Calendar.DAY_OF_MONTH)
        tv_selectedDate.text = date.get(Calendar.YEAR).toString() + " - " + date.get(Calendar.MONTH)
        calendarView.setOnMonthChangeListener { year, month ->
            tv_selectedDate.text = year.toString() + " - " + month
            OkGo.getInstance().cancelTag(SEARCHMONTH)
            networkForCalendarStatus(year, month)
        }
        calendarView.setOnYearChangeListener { year -> tv_selectedDate.text = year.toString() + " - " + calendarView!!.curMonth }
        calendarView.setOnDateSelectedListener { calendar, isClick ->
            OkGo.getInstance().cancelTag(SEARCHDAY)
            networkForListDataOfDay(calendar.year, calendar.month, calendar.day)
        }
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        //        mEmptyView = View.inflate(this, R.layout.layout_emptyview, null) as EmptyView
        //        recyclerView.addHeaderView(mEmptyView)
        //        recyclerView.setEmptyView(mEmptyView)
        mLiveAdapter = TodayCourseAdapter(this, null)
        recyclerView!!.adapter = mLiveAdapter
        mToday!!.scheme="03"
        calendarView.setSchemeDate(arrayListOf(mToday))
    }
    
    fun clickEvent(view: View) {
        val id = view.id
        if (id == R.id.iv_back) {
            finish()
        } else if (id == R.id.iv_preDate) {
            calendarView.scrollToPre()
        } else if (id == R.id.iv_nextDate) {
            calendarView.scrollToNext()
        }
    }
    
    private fun networkForCalendarStatus(year: Int, month: Int) {
        val selectedDate = Calendar.getInstance(Locale.CHINA)
        selectedDate.set(Calendar.YEAR, year)
        selectedDate.set(Calendar.MONTH, month - 1)
        val request = LiveSccUserCourseCalendarMonthRequest()
        request.userId = UserManager.getInstance().userId.toString() + ""
        request.start = selectedDate.timeInMillis
        Api.getInstance().postScc(request, LiveSccUserCourseCalendarMonthResponse::class.java, object : ResponseCallback<LiveSccUserCourseCalendarMonthResponse>() {
            override fun onResponse(date: LiveSccUserCourseCalendarMonthResponse, isFromCache: Boolean) {
                mToday!!.scheme="03"
                val calendars = ArrayList<com.haibin.calendarview.Calendar>()
                calendars.add(mToday!!)
                if (date.data == null) {
                    return
                }
                val list = date.data.list
                if (list != null && list.size > 0) {
                    for (bean in list) {
                        val calendar = com.haibin.calendarview.Calendar()
                        calendar.year = year
                        calendar.month = month
                        calendar.day = bean.hasDay!!
                        if (calendar.compareTo(mToday!!) < 0) { //之前
                            if (bean.joinNum > 0) { //进入过直播间
                                calendar.scheme = "02"
                            } else {
                                calendar.scheme = "01"
                            }
                        } else if (calendar.compareTo(mToday!!) == 0) { //当天
                            calendar.scheme = "03"
                        } else {
                            calendar.scheme = "04" //未直播
                        }
                        calendars.add(calendar)
                    }
//                    calendarView!!.setSchemeDate(calendars)
                }
                calendarView!!.setSchemeDate(calendars)
            }
    
            override fun onError() {
                super.onError()
            }
    
            override fun onEmpty(date: LiveSccUserCourseCalendarMonthResponse?, isFromCache: Boolean) {
                super.onEmpty(date, isFromCache)
            }
        }, SEARCHMONTH)
    }
    
    /**
     * 某天数据
     */
    private fun networkForListDataOfDay(year: Int, month: Int, day: Int) {
        //        mEmptyView.showLoading();
        //        updateUIStatus(1)
        emptyView.showLoading()
        mLiveAdapter!!.setData(null)
        val request = LiveSccUserCourseLastRequest()
        request.userId = UserManager.getInstance().userId.toString() + ""
        request.start = Utils.getStartTime(year, month, day).time
        request.end = Utils.getEndTime(year, month, day).time
        Api.getInstance().postScc(request, LiveSccUserCourseLastResponse::class.java, object : ResponseCallback<LiveSccUserCourseLastResponse>() {
            override fun onResponse(date: LiveSccUserCourseLastResponse, isFromCache: Boolean) {
                if (date.data == null) {
                    //                    mEmptyView.showEmpty();
                    return
                }
                //                emptyView.showContent()
                //                updateUIStatus(0)
                emptyView.showContent()
                val list = date.data.knowledges
                if (list != null && list.size > 0) {
                    mLiveAdapter!!.setData(list)
                } else {
                    //                    emptyView.showEmpty()
                    mLiveAdapter?.setData(listOf())
                }
            }
            
            override fun onEmpty(date: LiveSccUserCourseLastResponse, isFromCache: Boolean) {
                super.onEmpty(date, isFromCache)
                //                mEmptyView.showEmpty();
                //                emptyView.showEmpty()
                //                mLiveAdapter?.setData(listOf())
                //                updateUIStatus(2)
                emptyView.showEmpty()
            }
            
            override fun onError() {
                super.onError()
                //                emptyView.showEmpty()
                //                mLiveAdapter?.setData(listOf())
                //                updateUIStatus(3)
                emptyView.showError()
            }
        }, SEARCHDAY)
    }
    
    /**
     * 0 :显示内容  1：loading  2：empty  3 :net error
     */
    //    private fun updateUIStatus(status: Int) {
    //        when (status) {
    //            0 -> {
    //                recyclerView.visibility = View.VISIBLE
    //                layoutLoading.visibility = View.GONE
    //                layoutEmpty.visibility = View.GONE
    //                layoutNetError.visibility = View.GONE
    //            }
    //            1 -> {
    //                recyclerView.visibility = View.GONE
    //                layoutLoading.visibility = View.VISIBLE
    //                layoutEmpty.visibility = View.GONE
    //                layoutNetError.visibility = View.GONE
    //            }
    //            2 -> {
    //                recyclerView.visibility = View.GONE
    //                layoutLoading.visibility = View.GONE
    //                layoutEmpty.visibility = View.VISIBLE
    //                layoutNetError.visibility = View.GONE
    //            }
    //            3 -> {
    //                recyclerView.visibility = View.GONE
    //                layoutLoading.visibility = View.GONE
    //                layoutEmpty.visibility = View.GONE
    //                layoutNetError.visibility = View.VISIBLE
    //            }
    //        }
    //    }
    
    companion object {
        
        fun start(context: Context) {
            val intent = Intent(context, CalendarCourseActivity::class.java)
            context.startActivity(intent)
        }
    }
}
