package com.haoke91.a91edu.fragment.main

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.ConvertUtils

import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SpanUtils
import com.gaosiedu.scc.sdk.android.api.user.course.last.LiveSccUserCourseLastRequest
import com.gaosiedu.scc.sdk.android.api.user.course.last.LiveSccUserCourseLastResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.TodayCourseAdapter
import com.haoke91.a91edu.entities.UserInfo
import com.haoke91.a91edu.fragment.BaseFragment
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.homework.MyHomeworkActivity
import com.haoke91.a91edu.ui.learn.CalendarCourseActivity
import com.haoke91.a91edu.ui.learn.DailyWorkActivity
import com.haoke91.a91edu.ui.learn.MyCourseActivity
import com.haoke91.a91edu.ui.learn.WrongExamBookActivity
import com.haoke91.a91edu.ui.login.LoginActivity
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.baselibrary.event.MessageItem
import com.haoke91.baselibrary.event.RxBus
import com.lzy.okgo.cache.CacheMode

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_study.*


/**
 * 项目名称：MyHaoke1
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/6/4 15:26
 */
class StudyFragment : BaseFragment() {
    private var currentUser: UserInfo? = null
    
    private val OnClickListener = object : NoDoubleClickListener() {
        override fun onDoubleClick(v: View) {
            if (!UserManager.getInstance().isLogin) {
                LoginActivity.start(mContext)
                return
            }
            when (v.id) {
                R.id.tv_gotoCalendarCourse -> CalendarCourseActivity.start(mContext)
                R.id.tv_daily_work -> DailyWorkActivity.start(mContext)
                R.id.tv_my_class -> MyCourseActivity.start(mContext)
                R.id.tv_my_homework -> MyHomeworkActivity.start(mContext)
                R.id.tv_my_wrong -> startActivity(WrongExamBookActivity::class.java)
            }
        }
    }
    
    override fun getLayout(): Int {
        return R.layout.fragment_study
    }
    
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        //        isDataInitiated = false
        super.setUserVisibleHint(isVisibleToUser)
        if (isViewInitiated)
            tv_date?.text = SimpleDateFormat("MM月dd日", Locale.CHINA).format(Date())
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mRxBus = RxBus.getIntanceBus()
        mRxBus.doSubscribe(mContext.javaClass, MessageItem::class.java, Consumer { messageItem ->
            if (ObjectUtils.isEmpty(messageItem)) {
                return@Consumer
            }
            if (messageItem.type == MessageItem.action_login) {
                fetchData()
            } else if (messageItem.type == MessageItem.change_head) {
                GlideUtils.loadHead(mContext, messageItem.message, iv_head)
            }
        })
        tv_gotoCalendarCourse.setOnClickListener(OnClickListener)
        tv_daily_work.setOnClickListener(OnClickListener)
        tv_my_class.setOnClickListener(OnClickListener)
        tv_my_homework.setOnClickListener(OnClickListener)
        tv_my_wrong.setOnClickListener(OnClickListener)
        iv_head.setOnClickListener(OnClickListener)
        tv_motto.setOnClickListener(OnClickListener)
        tv_login_more.setOnClickListener(OnClickListener)
        
        refreshLayout?.setOnRefreshListener {
            getCourseToday()
        }
        
        var errorButtonText = SpanUtils().append("请检查网络设置，或尝试").append("刷新页面").setClickSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                fetchData()
            }
            
            override fun updateDrawState(ds: TextPaint) {
                ds.color = Color.parseColor("#75C82B")
                ds.bgColor = Color.WHITE
                //                ds.isUnderlineText = true
            }
        }).create()
        emptylayout.builder().setErrorButtonText(errorButtonText)
    }
    
    
    override fun fetchData() {
        if (UserManager.getInstance().isLogin) {
            val linearLayoutManager = LinearLayoutManager(mContext)
            wr_today_course.layoutManager = linearLayoutManager
            val searchResultAdapter = TodayCourseAdapter(mContext, null)
            wr_today_course.adapter = searchResultAdapter
            emptylayout?.showLoading()
            getCourseToday()
        } else {
            emptylayout?.showEmpty()
        }
        setLoginStatus(UserManager.getInstance().isLogin)
    }
    
    
    private fun getCourseToday() {
        
        val request = LiveSccUserCourseLastRequest()
        request.userId = UserManager.getInstance().userId.toString()
        request.start = Utils.getStartTime().time
        request.end = Utils.getEndTime().time
        
        Api.getInstance().postScc(request, LiveSccUserCourseLastResponse::class.java, object : ResponseCallback<LiveSccUserCourseLastResponse>() {
            override fun onResponse(date: LiveSccUserCourseLastResponse, isFromCache: Boolean) {
                val data = date.data
                if (data == null || data.knowledges == null) {
                    emptylayout?.showEmpty()
                    return
                }
                val list = data.knowledges
                tv_countOfLive.text = String.format("共%s场直播", list.size)
                (wr_today_course.adapter as? TodayCourseAdapter)?.setData(list)
                emptylayout?.showContent()
                refreshLayout?.finishRefresh()
            }
            
            override fun onEmpty(date: LiveSccUserCourseLastResponse, isFromCache: Boolean) {
                emptylayout?.showEmpty()
                refreshLayout?.finishRefresh()
            }
            
            override fun onFail(date: LiveSccUserCourseLastResponse?, isFromCache: Boolean) {
                super.onFail(date, isFromCache)
                refreshLayout?.finishRefresh()
            }
            
            override fun onError() {
                refreshLayout?.finishRefresh()
                if ((wr_today_course.adapter as? TodayCourseAdapter)?.all?.isEmpty() != false) {
                    emptylayout?.showError()
                }
            }
        }, CacheMode.FIRST_CACHE_THEN_REQUEST, ConvertUtils.timeSpan2Millis(1, TimeConstants.HOUR), "")
    }
    
    private fun setLoginStatus(isLogin: Boolean) {
        if (isLogin) {
            currentUser = UserManager.getInstance().loginUser
            tv_user_level.text = currentUser?.levelName
            tv_goldCount.text = currentUser?.gold.toString()
            tv_growValue.text = currentUser?.totalGrowth.toString()
        }
        GlideUtils.loadHead(mContext, if (isLogin) currentUser?.smallHeadimg else "", iv_head)
        
        tv_motto.text = if (isLogin) currentUser?.loginName else "登录/注册"
        
        tv_user_level.visibility = if (isLogin) View.VISIBLE else View.GONE
        tv_goldCount.visibility = if (isLogin) View.VISIBLE else View.GONE
        tv_grow.visibility = if (isLogin) View.VISIBLE else View.GONE
        tv_growValue.visibility = if (isLogin) View.VISIBLE else View.GONE
        tv_login_more.visibility = if (isLogin) View.INVISIBLE else View.VISIBLE
    }
}
