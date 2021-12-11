package com.haoke91.a91edu.ui.course

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.TextUtils
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.gaosiedu.live.sdk.android.api.cart.add.LiveCartCreateRequest
import com.gaosiedu.live.sdk.android.api.cart.add.LiveCartCreateResponse
import com.gaosiedu.live.sdk.android.api.course.detail.LiveCourseDetailRequest
import com.gaosiedu.live.sdk.android.api.course.detail.LiveCourseDetailResponse
import com.gaosiedu.live.sdk.android.api.order.pre.LivePreCreateOrderRequest
import com.gaosiedu.live.sdk.android.api.order.pre.LivePreCreateOrderResponse
import com.gaosiedu.live.sdk.android.api.user.collection.add.LiveUserCollectionAddRequest
import com.gaosiedu.live.sdk.android.api.user.collection.add.LiveUserCollectionAddResponse
import com.gaosiedu.live.sdk.android.domain.TeacherDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.adapter.CoursePlanAdapter
import com.haoke91.a91edu.adapter.TeacherIntroduceAdapter
import com.haoke91.a91edu.adapter.TeacherOrAssistantAdapter
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.BaseActivity
import com.haoke91.a91edu.ui.learn.CourseOrderActivity
import com.haoke91.a91edu.ui.login.LoginActivity
import com.haoke91.a91edu.ui.order.ConfirmOrderActivity
import com.haoke91.a91edu.ui.order.ShoppingCartActivity
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.a91edu.widget.CanLocationScrollView
import com.haoke91.a91edu.widget.dialog.FavoriteActivityDialog
import com.haoke91.a91edu.widget.flowlayout.FlowLayout
import com.haoke91.a91edu.widget.flowlayout.TagAdapter
import com.haoke91.baselibrary.utils.DensityUtil
import com.haoke91.baselibrary.utils.ICallBack
import com.haoke91.baselibrary.views.TipDialog
import com.zzhoujay.richtext.RichText
import kotlinx.android.synthetic.main.activity_coursedetail.*
import kotlinx.android.synthetic.main.layout_course_plan.*
import kotlinx.android.synthetic.main.layout_teacher_introduce.*
import java.text.SimpleDateFormat

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/7/13 15:39
 */
class CourseDetailActivity : BaseActivity() {
    private var mCourseId = 0
    private var mUserCourseId = 0 //用户课程id
    lateinit var mCanLocationScrollView: CanLocationScrollView
    lateinit var mTabLayoutIn: TabLayout
    //    TabLayout tabLayoutOut;
    //    private var mFlow_courseTags: TagFlowLayout? = null
    //课程介绍
    //课程安排
    private lateinit var mWbCourseInfo: TextView
    private lateinit var mCoursePlanAdapter: CoursePlanAdapter
    lateinit var fl_replace: View
    var richText: RichText? = null
    
    //教师简介
    //底部按钮
    //    private var mBottomBtn_left: TextView? = null //购物车
    //    private var mBottomBtn_right: TextView? = null //报名
    private lateinit var mTeacherOrAssistantAdapter: TeacherOrAssistantAdapter
    
    override fun getLayout(): Int {
        return R.layout.activity_coursedetail
    }
    
    override fun initialize() {
        mCourseId = intent.getIntExtra("id", 0)
        mCanLocationScrollView = findViewById(R.id.scrollView)
        mWbCourseInfo = findViewById(R.id.wbCourseInfo)
        mTabLayoutIn = findViewById(R.id.tabLayout)
        fl_replace = findViewById(R.id.fl_replaceTab)
        //        tabLayoutOut = findViewById(R.id.tabLayoutOut);
        rv_coursePlan.setHasFixedSize(true)
        rv_coursePlan.isNestedScrollingEnabled = false
        mCoursePlanAdapter = CoursePlanAdapter(this)
        rv_coursePlan.layoutManager = LinearLayoutManager(this)
        rv_coursePlan.adapter = mCoursePlanAdapter
        //        mCanLocationScrollView.canScollView(tabLayoutOut, mTabLayoutIn);
        var tab1 = newTextView()
        tab1.text = "课程介绍"
        var tab2 = newTextView()
        tab2.text = "课程安排"
        var tab3 = newTextView()
        tab3.text = "教师简介"
        tab1.setOnClickListener {
            mCanLocationScrollView.isFlying = false
            mTabLayoutIn.getTabAt(0)!!.select()
        }
        tab2.setOnClickListener {
            mCanLocationScrollView.isFlying = false
            mTabLayoutIn.getTabAt(1)!!.select()
        }
        tab3.setOnClickListener {
            mCanLocationScrollView.isFlying = false
            mTabLayoutIn.getTabAt(2)!!.select()
        }
        mTabLayoutIn.addTab(mTabLayoutIn.newTab().setCustomView(tab1))
        mTabLayoutIn.addTab(mTabLayoutIn.newTab().setCustomView(tab2))
        mTabLayoutIn.addTab(mTabLayoutIn.newTab().setCustomView(tab3))
        //        val tags = Arrays.asList("直播授课", "纸质讲义", "在线答题", "入学考试", "直播授课", "纸质讲义", "在线答题", "入学考试")
        //        mFlow_courseTags!!.adapter = object : TagAdapter<String>(tags) {
        //            override fun getView(parent: FlowLayout, position: Int, _s: String): View {
        //                val v = LayoutInflater.from(parent.context).inflate(R.layout.item_search_content_text, mFlow_courseTags, false) as TextView
        //                v.text = _s
        //                return v
        //            }
        //        }
        //        mTabLayoutIn.setOnTouchListener { v, event ->
        //            Log.e("tag", "setOnTouchListener=====")
        //            mCanLocationScrollView.isFlying = false
        //            true
        //        }
        //
        //        mTabLayoutIn.getTabAt(0)?.customView?.setOnTouchListener { v, event ->
        //            Log.e("tag", "setOnTou11111111chListener=====")
        //            mCanLocationScrollView.isFlying = false
        //            true
        //        }
        mTabLayoutIn.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //                TabLayout.Tab t = tabLayoutOut.getTabAt(tab.getPosition());
                //                if (!t.isSelected()) {
                //                    t.select();
                //                }
                var p = 0
                val height = mTabLayoutIn.height
                when {
                    tab.position == 0 -> p = area_courseIntroduce!!.top
                    tab.position == 1 -> p = area_coursePlan!!.top
                    tab.position == 2 -> p = area_teacherIntroduce!!.top
                }
                if (mCanLocationScrollView.isFlying) {
                    return
                    
                }
                mCanLocationScrollView!!.smoothScrollToSlow(0, p - height, 1000)
                mCanLocationScrollView!!.canScrollChoose = false
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab) {
            
            }
            
            override fun onTabReselected(tab: TabLayout.Tab) {
            
            }
        })
        mCanLocationScrollView!!.viewTreeObserver!!.addOnGlobalLayoutListener {
            val marginTop = Math.max(mCanLocationScrollView!!.scrollY, fl_replace.top)
            mTabLayoutIn.layout(0, marginTop, mTabLayoutIn.width, marginTop + mTabLayoutIn.height)
        }
        
        //        mCanLocationScrollView.setOnTouchListener(
        //                { v, ev ->
        //                    if (ev.action == MotionEvent.ACTION_DOWN) isClickScroll = false
        //                    true
        //                })
        mCanLocationScrollView?.setOnScrollListener(CanLocationScrollView.OnScrollListener { _, y, _, _ ->
            //                int marginTop = Math.max(mCanLocationScrollView.getScrollY(), mTabLayoutIn.getTop());
            //                mTabLayoutIn.layout(0, marginTop, mTabLayoutIn.getWidth(), marginTop + mtab.getHeight());
            //            LogUtil.log("current location y===" + y)
            val marginTop = Math.max(mCanLocationScrollView!!.scrollY, fl_replace.top)
            mTabLayoutIn.layout(0, marginTop, mTabLayoutIn.width, marginTop + mTabLayoutIn.height)
            val currentHeight = y + mCanLocationScrollView!!.height
            val top1 = area_courseIntroduce.top
            val top2 = area_coursePlan.top
            val top3 = area_teacherIntroduce.top
            if (!mCanLocationScrollView.canScrollChoose) {
                return@OnScrollListener
            }
            //                if (y == top1) {
            //                    if (!mTabLayoutIn.getTabAt(0).isSelected()) {
            //                        mTabLayoutIn.getTabAt(0).select();
            //                    }
            //                } else if (y == top2) {
            //                    if (!mTabLayoutIn.getTabAt(1).isSelected()) {
            //                        mTabLayoutIn.getTabAt(1).select();
            //                    }
            //                } else if (y == top3) {
            //                    if (!mTabLayoutIn.getTabAt(2).isSelected()) {
            //                        mTabLayoutIn.getTabAt(2).select();
            //                    }
            //                }
            if (top1 in (y + 1)..(currentHeight - 1) || (y > top1 && currentHeight < area_courseIntroduce!!.bottom)) {
                if (!mTabLayoutIn.getTabAt(0)!!.isSelected) {
                    mTabLayoutIn.getTabAt(0)!!.select()
                }
            } else if (top2 in (y + 1)..(currentHeight - 1) || (y > top2 && currentHeight < area_coursePlan!!.bottom)) {
                if (!mTabLayoutIn.getTabAt(1)!!.isSelected) {
                    mTabLayoutIn.getTabAt(1)!!.select()
                }
            } else if (top3 in (y + 1)..(currentHeight - 1) || (y > top3 && currentHeight < area_teacherIntroduce!!.bottom)) {
                if (!mTabLayoutIn.getTabAt(2)!!.isSelected) {
                    mTabLayoutIn.getTabAt(2)!!.select()
                }
            }
        })
        //教师,助教列表
        mTeachersAndAssistantRecyclerView.layoutManager = LinearLayoutManager(this, 0, false)
        mTeacherOrAssistantAdapter = TeacherOrAssistantAdapter(this)
        mTeachersAndAssistantRecyclerView.adapter = mTeacherOrAssistantAdapter
        
        networkForDetail(mCourseId)
    }
    
    fun clickEvent(v: View) {
        val id = v.id
        when (id) {
            R.id.iv_back -> finish()
            R.id.mBottomBtn_right -> {
                var tag = mBottomBtn_right.tag
                if (!UserManager.getInstance().isLogin) {
                    LoginActivity.start(this)
                    return
                }
                when (tag) {
                    "0" -> networkForShoppingCar(mCourseId, true)
                    else -> CourseOrderActivity.start(this@CourseDetailActivity, "$mCourseId,$mUserCourseId")
                }
            }
            R.id.mBottomBtn_left -> if (UserManager.getInstance().isLogin) networkForShoppingCar(mCourseId, false) else LoginActivity.start(this)
            R.id.mIsCollected -> if (UserManager.getInstance().isLogin) networkForCollect(mCourseId, mIsCollected) else LoginActivity.start(this)
        }
        
    }
    
    /**
     * 修改底部右侧购物车，报名或学习的状态
     *
     * @param status
     */
    private fun correctBottomButtomsStatus(status: Int) {
        mBottomBtn_right.tag = status
        when (status) {
            1 -> {
                mBottomBtn_left?.setBackgroundResource(R.drawable.bg_leftcircle_gradientyellow)
                mBottomBtn_left?.text = "加入购物车"
                mBottomBtn_right?.setBackgroundResource(R.drawable.bg_rightcircle_gradientred)
                mBottomBtn_right?.text = "考试后报名"
            }
            2 -> {
                mBottomBtn_left?.setBackgroundResource(R.drawable.bg_leftcircle_gradientyellow)
                mBottomBtn_left?.text = "加入购物车"
                mBottomBtn_right?.setBackgroundResource(R.drawable.bg_rightcircle_gradientgray)
                mBottomBtn_right?.text = "报名未开始"
            }
            3 -> {
                mBottomBtn_left?.setBackgroundResource(R.drawable.bg_leftcircle_gradientyellow)
                mBottomBtn_left?.text = "加入购物车"
                mBottomBtn_right?.setBackgroundResource(R.drawable.bg_rightcircle_gradientgray)
                mBottomBtn_right!!.text = "已报满"
                var w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                var h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                mBottomBtn_left!!.measure(w, h)
                mBottomBtn_right!!.width = mBottomBtn_left!!.measuredWidth
            }
            4 -> {
                mBottomBtn_left?.setBackgroundResource(R.drawable.bg_leftcircle_gradientyellow)
                mBottomBtn_left?.text = getString(R.string.entershopcar)
                mBottomBtn_right?.setBackgroundResource(R.drawable.bg_rightcircle_gradientred)
                mBottomBtn_right?.text = getString(R.string.gotostudy)
            }
            else -> {
                mBottomBtn_left?.setBackgroundResource(R.drawable.bg_leftcircle_gradientyellow)
                mBottomBtn_left?.text = "加入购物车"
                mBottomBtn_right?.setBackgroundResource(R.drawable.bg_rightcircle_gradientred)
                mBottomBtn_right!!.text = " 免费报名 "
                mBottomBtn_right!!.width = mBottomBtn_left!!.measuredWidth
            }
        }
    }
    
    private fun networkForDetail(id: Int) {
        mEmptyView.showLoading()
        var request = LiveCourseDetailRequest()
        if (UserManager.getInstance().isLogin) {
            request.userId = UserManager.getInstance().userId.toString()
        }
        request.courseId = id
        Api.getInstance().postCourse(request, LiveCourseDetailResponse::class.java, object : ResponseCallback<LiveCourseDetailResponse>() {
            override fun onResponse(date: LiveCourseDetailResponse?, isFromCache: Boolean) {
                if (Utils.isSuccess(date!!.code)) {
                    handleData(date.data)
                }
                mUserCourseId = date.data.userCourseId ?: 0
                mEmptyView.showContent()
            }
            
            override fun onEmpty(date: LiveCourseDetailResponse?, isFromCache: Boolean) {
                super.onEmpty(date, isFromCache)
                mEmptyView.showEmpty()
            }
            
            override fun onError() {
                super.onError()
                mEmptyView.showError()
            }
        }, "")
        
    }
    
    private fun handleData(data: LiveCourseDetailResponse.ResultData) {
        if (data.term == null) {
            mTerm.visibility = View.GONE
        } else {
            mTerm.text = Utils.getHolidayByNumber(data.term ?: 1, mTerm)
        }
        if (null == data.courseSubjectNames) {
            mSubjectName.visibility = View.GONE
        } else {
            mSubjectName.visibility = View.VISIBLE
            mSubjectName.text = data.courseSubjectNames?.substring(0, 1)
        }
        //        mCourseName.text = Html.fromHtml(data.name)
        mCountOfKnowledge.text = String.format("共%s讲", data.knowledges?.size)
        //        mTimeRemark.text = String.format("课程安排：%s", data.timeremark)
        mTimeRemark.text = Html.fromHtml("课程安排：${data.timeremark}")
        mUsedRange.text = String.format("适用范围： %s", data.plan)
        val courseClassDomain = data.courseClassDomain
        if (!ObjectUtils.isEmpty(courseClassDomain)) {
            var teacherDomain = TeacherDomain()
            teacherDomain.headUrl = courseClassDomain.headUrl
            teacherDomain.realname = courseClassDomain.teacherName
            teacherDomain.type = 4
            if (data.teachers == null) {
                data.teachers = arrayListOf()
            }
            data.teachers.add(teacherDomain)
        }
        
        mTeacherOrAssistantAdapter.setData(data?.teachers, (data?.courseClassDomain?.count ?: 0) - (data?.courseClassDomain?.factCount ?: 0))
        mIsCollected.isSelected = data.isLike
        mTvTipLimitSale.text = String.format("%s停售，已有%s人报名", Utils.datetoStringJudge(TimeUtils.string2Date(data.deadline ?: "", SimpleDateFormat("yyyy-MM-dd HH:mm")), SimpleDateFormat("MM月dd日"), SimpleDateFormat("yyyy年MM月dd日")), data.userBuyCount + data.enrollCount)
        var serviceNames = data.courseServiceNames.split(",")
        if (serviceNames.isNotEmpty()) {
            mFlow_courseTags.adapter = object : TagAdapter<String>(serviceNames) {
                override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                    val v = LayoutInflater.from(parent!!.context).inflate(R.layout.item_circle_text, mFlow_courseTags, false) as TextView
                    v.text = t
                    return v
                }
                
            }
        }
        if (!TextUtils.isEmpty(data.info)) {
            richText = RichText.from(data.info).into(mWbCourseInfo)
        } else {
            area_courseIntroduce.visibility = View.GONE
        }
        //        mWbCourseInfo.loadData(data.info, "text/html", "utf-8")
        val activityList = data.activityList //活动列表
        if (activityList != null && activityList.size > 0) {
            for (ac in activityList) {
                var view = layoutInflater.inflate(R.layout.item_activitylist, null)
                var acInfo = view.findViewById<TextView>(R.id.tvActivityInfo)
                acInfo.text = "${ac.name} >"
                view.setOnClickListener {
                    FavoriteActivityDialog.showDialog(this@CourseDetailActivity, ac)
                }
                val params = LinearLayout.LayoutParams(-2, -2)
                params.topMargin = DensityUtil.dip2px(this, 4f)
                ll_favoriteActivity.addView(view, params)
            }
            
        }
        //课程安排
        mCoursePlanAdapter.setData(data.knowledges)
        
        mOldPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG //价格
        mNowPrice.text = "¥${data.payable}"
        if (data.oldPrice == null || data.oldPrice?.compareTo(data.payable) == 0) {
            mOldPrice.visibility = View.GONE
        } else {
            mOldPrice.text = "¥${data.oldPrice}"
        }
        //        data.isLike //是否收藏
        //        data.courseStatus //-1 已下线  0 未上线  1已上线
        //        data.isBuy // 已购买  未购买  已购买
        //        data.coursePurchaseStart //true 报名已开始 false 报名未开始
        //        data.coursePurchaseEnd //true 报名已结束  false 报名未结束
        correctBottomBtnStatus(data.isBuy, data.coursePurchaseStart, data.coursePurchaseEnd, data?.courseClassDomain?.count ?: 0 <= 0, data.status)
        //教师简介
        recyclerView_teacherIntro.layoutManager = LinearLayoutManager(this)
        var teacherAdapter = TeacherIntroduceAdapter(this)
        recyclerView_teacherIntro.setHasFixedSize(true)
        recyclerView_teacherIntro.isNestedScrollingEnabled = false
        recyclerView_teacherIntro.adapter = teacherAdapter
        teacherAdapter.setData(data.teachers.subList(0, data.teachers.size - 1))
        
        val sp = SpanUtils()
        sp.append(Html.fromHtml(data.name)).setLeadingMargin((mCourseName.textSize * 3).toInt(), 0)
        mCourseName.text = sp.create()
        
    }
    
    companion object {
        
        fun start(context: Context, courseId: Int) {
            val intent = Intent(context, CourseDetailActivity::class.java)
            intent.putExtra("id", courseId)
            context.startActivity(intent)
        }
    }
    
    /**
     * 收藏，取消收藏
     * @param collect 1收藏，0 取消收藏
     */
    private fun networkForCollect(courseId: Int, collect: View) {
        showLoadingDialog()
        var request = LiveUserCollectionAddRequest()
        request.userId = UserManager.getInstance().userId
        request.type = 1
        request.associateId = courseId
        request.status = if (!collect.isSelected) 1 else 0
        Api.getInstance().postCourse(request, LiveUserCollectionAddResponse::class.java, object : ResponseCallback<LiveUserCollectionAddResponse>() {
            override fun onResponse(date: LiveUserCollectionAddResponse, isFromCache: Boolean) {
                collect.isSelected = !collect.isSelected
                if (date.data.status == 1) {
                    ToastUtils.showShort("收藏成功")
                } else {
                    ToastUtils.showLong("取消收藏")
                }
                dismissLoadingDialog()
            }
            
            override fun onError() {
                dismissLoadingDialog()
            }
        }, "collect")
    }
    
    private fun networkForShoppingCar(courseId: Int, isGoJoin: Boolean) {
        showLoadingDialog()
        var request = LiveCartCreateRequest()
        request.associateId = courseId.toString()
        request.userId = UserManager.getInstance().userId
        Api.getInstance().postCourse(request, LiveCartCreateResponse::class.java, object : ResponseCallback<LiveCartCreateResponse>() {
            override fun onResponse(date: LiveCartCreateResponse?, isFromCache: Boolean) {
                if (isDestroyed) {
                    return
                }
                if ("FAIL" == date?.code) {
                    if (date.data.isFlag) {
                        //已经在购物车
                        if (isGoJoin) {
                            networkForJoin(courseId)
                        } else {
                            mBottomBtn_left.text = " 购物车 "  //isbuy !isOnline
                            mBottomBtn_left.isClickable = false
                            mBottomBtn_left.setBackgroundResource(R.drawable.bg_leftcircle_gradientgray)
                            showGotoCarTip()
                            dismissLoadingDialog()
                        }
                    } else {
                        //加入购物车失败
                        ToastUtils.showShort("加入购物车失败")
                    }
                    return
                }
                if (isGoJoin) {
                    networkForJoin(courseId)
                } else {
                    //                    ToastUtils.showShort("已加入购物车")
                    mBottomBtn_left.text = " 购物车 "  //isbuy !isOnline
                    mBottomBtn_left.isClickable = false
                    mBottomBtn_left.setBackgroundResource(R.drawable.bg_leftcircle_gradientgray)
                    showGotoCarTip()
                }
            }
        }, "shopping car")
    }
    
    private fun networkForJoin(courseId: Int) {
        showLoadingDialog()
        var request = LivePreCreateOrderRequest()
        request.userId = UserManager.getInstance().userId
        request.courseIdsStr = courseId.toString()
        Api.getInstance().postCourse(request, LivePreCreateOrderResponse::class.java, object : ResponseCallback<LivePreCreateOrderResponse>() {
            override fun onResponse(date: LivePreCreateOrderResponse?, isFromCache: Boolean) {
                //                ToastUtils.showShort("报名成功")
                if (isDestroyed) {
                    return
                }
                dismissLoadingDialog()
                ConfirmOrderActivity.start(this@CourseDetailActivity, date!!.data)
            }
            
            override fun onEmpty(date: LivePreCreateOrderResponse?, isFromCache: Boolean) {
                super.onEmpty(date, isFromCache)
                dismissLoadingDialog()
            }
            
            override fun onFail(date: LivePreCreateOrderResponse?, isFromCache: Boolean) {
                super.onFail(date, isFromCache)
                dismissLoadingDialog()
            }
            
            override fun onError() {
                super.onError()
                dismissLoadingDialog()
            }
        }, "join in ")
    }
    
    /**
     * 弹框提示
     */
    private fun showGotoCarTip() {
        var dialog = TipDialog(this)
        dialog.setTextDes("课程已在购物车中,是否进入购物车？")
        dialog.setButton1(getString(R.string.commit)) { _, _ ->
            ShoppingCartActivity.start(this@CourseDetailActivity)
        }
        dialog.setButton2(getString(R.string.cancel)) { _, dialog -> dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
        
        
    }
    
    /**
     * 无已报满 情况
     */
    private fun correctBottomBtnStatus(isBuy: Boolean, isStart: Boolean, isEnd: Boolean, isFull: Boolean, isOnline: Int) {
        mBottomBtn_right.tag = "-1"
        if (isOnline == 1) {
            if (isBuy) {
                mBottomBtn_left.text = "购物车"  //isbuy !isOnline
                mBottomBtn_left.isClickable = false
                mBottomBtn_left.setBackgroundResource(R.drawable.bg_leftcircle_gradientgray)
                mBottomBtn_right.text = " 去学习 "
                mBottomBtn_right.tag = "1"
                mBottomBtn_right.isClickable = true
                mBottomBtn_right.setBackgroundResource(R.drawable.bg_rightcircle_gradientgreen)
            }
            if (!isBuy && isStart) {
                mBottomBtn_left.text = "加入购物车"
                mBottomBtn_left.isClickable = true
                mBottomBtn_left.setBackgroundResource(R.drawable.bg_leftcircle_gradientyellow)
                mBottomBtn_right.text = "报名未开始"
                mBottomBtn_right.isClickable = false
                mBottomBtn_right.setBackgroundResource(R.drawable.bg_rightcircle_gradientgray)
            }
            if (!isBuy && isStart && !isEnd) { //报名，要先加入购物车
                mBottomBtn_left.text = "加入购物车"
                mBottomBtn_left.isClickable = true
                mBottomBtn_left.setBackgroundResource(R.drawable.bg_leftcircle_gradientyellow)
                mBottomBtn_right.text = "立即报名"
                mBottomBtn_right.tag = "0"
                mBottomBtn_right.isClickable = true
                mBottomBtn_right.setBackgroundResource(R.drawable.bg_rightcircle_gradientred)
            }
            if (!isBuy && !isStart) {
                mBottomBtn_left.text = "加入购物车"
                mBottomBtn_left.isClickable = true
                mBottomBtn_left.setBackgroundResource(R.drawable.bg_leftcircle_gradientyellow)
                mBottomBtn_right.text = "报名未开始"
                mBottomBtn_right.isClickable = false
                mBottomBtn_right.setBackgroundResource(R.drawable.bg_rightcircle_gradientgray)
            }
            if (!isBuy && isEnd) {
                mBottomBtn_left.text = "购物车"
                mBottomBtn_left.isClickable = false
                mBottomBtn_left?.setBackgroundResource(R.drawable.bg_leftcircle_gradientgray)
                mBottomBtn_right.text = "报名已结束"
                mBottomBtn_right.isClickable = false
                mBottomBtn_right.setBackgroundResource(R.drawable.bg_rightcircle_gradientgray)
            }
            //            if (!isBuy && isFull) {
            //                mBottomBtn_left.text = "购物车"
            //                mBottomBtn_left.isClickable = false
            //                mBottomBtn_left?.setBackgroundResource(R.drawable.bg_leftcircle_gradientgray)
            //                mBottomBtn_right.text = "已报满"
            //                mBottomBtn_right.isClickable = false
            //                mBottomBtn_right.setBackgroundResource(R.drawable.bg_rightcircle_gradientgray)
            //            }
        } else {
            mBottomBtn_left.text = "购物车"
            mBottomBtn_left.isClickable = false
            mBottomBtn_left.setBackgroundResource(R.drawable.bg_leftcircle_gradientgray)
            mBottomBtn_right.text = "已下架"
            mBottomBtn_right.isClickable = false
            mIsCollected.visibility = View.GONE
            mBottomBtn_right.setBackgroundResource(R.drawable.bg_rightcircle_gradientgray)
            if (isOnline == 0) {
                mBottomBtn_right.text = "未上线"
            }
            if (isBuy) {
                mBottomBtn_right.text = " 去学习 "
                mBottomBtn_right.tag = 1
                mBottomBtn_right.isClickable = true
                mBottomBtn_right.setBackgroundResource(R.drawable.bg_rightcircle_gradientgreen)
            }
        }
    }
    
    private fun newTextView(): TextView {
        //        var textView = TextView(this)
        //        var lp = ViewGroup.LayoutParams(-1, -1)
        //        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        //        textView.layoutParams = lp
        //        textView.setTextColor(ContextCompat.getColorStateList(this,R.color.select_color))
        //        textView.gravity = Gravity.CENTER
        //        //        textView.setBackgroundColor(getColor(R.color.Blue))
        var tab = View.inflate(this, R.layout.item_tab2, null) as TextView
        return tab
    }
    
    
    override fun onDestroy() {
        super.onDestroy()
        if (!ObjectUtils.isEmpty(richText)) {
            richText!!.clear()
        }
    }
}
