package com.haoke91.a91edu.adapter

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.TimeUtils
import com.gaosiedu.live.sdk.android.api.course.list.LiveCourseListResponse
import com.gaosiedu.live.sdk.android.domain.TeacherDomain
import com.google.gson.Gson
import com.haoke91.a91edu.R
import com.haoke91.a91edu.R.id.invisible
import com.haoke91.a91edu.R.id.tv_course_endTime
import com.haoke91.a91edu.ui.course.CourseDetailActivity
import com.haoke91.a91edu.utils.AnimationTool
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.MultiItemTypeSupport
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/14 上午11:17
 * 修改人：weiyimeng
 * 修改时间：2018/8/14 上午11:17
 * 修改备注：
 */
class SpecialClassAdapter(context: Context, dates: List<LiveCourseListResponse.ListData>?) : QuickWithPositionAdapter<LiveCourseListResponse.ListData>(context, object : MultiItemTypeSupport<LiveCourseListResponse.ListData> {
    override fun getLayoutId(viewType: Int): Int {
        if (viewType == 1) {
            return R.layout.item_special_class_type_one
        }
        return if (viewType == 2) {
            R.layout.item_special_class_type_two
        } else R.layout.item_special_class_type_three
        
    }
    
    override fun getItemViewType(position: Int, item: LiveCourseListResponse.ListData): Int {
        return if (ObjectUtils.isEmpty(item.teachers)) {
            1
        } else item.teachers.size
        
    }
}, dates) {
    
    private var lastExpandPosition = -1
    
    override fun convert(helper: BaseAdapterHelper, item: LiveCourseListResponse.ListData, position: Int) {
        setBaseDate(helper, item, position)
        when {
            getItemViewType(position) == 1 -> setDateOne(helper, item, position)
            getItemViewType(position) == 2 -> setDateTwo(helper, item, position)
            else -> setDateThree(helper, item, position)
        }
    }
    
    private fun setDateOne(helper: BaseAdapterHelper, item: LiveCourseListResponse.ListData, position: Int) {
        //        GlideUtils.loadHead(context, item.getTeachers().get(0).getHeadUrl(), helper.getImageView(R.id.iv_order_teacher_icon));
        //        GlideUtils.loadHead(context, item.getClassDomainList().get(0).getHeadUrl(), helper.getImageView(R.id.iv_order_assistant_icon));
        //        helper.getTextView(R.id.tv_order_teacher_name).setText(item.getTeachers().get(0).getRealname());
        //        helper.getTextView(R.id.tv_assistant_name).setText(item.getClassDomainList().get(0).getTeacherName());
    }
    
    private fun setDateTwo(helper: BaseAdapterHelper, item: LiveCourseListResponse.ListData, position: Int) {
        
        GlideUtils.loadHead(context, item.teachers[1].headUrl, helper.getImageView(R.id.iv_teacher_two_icon))
        helper.getTextView(R.id.tv_order_teacher_name_two).text = item.teachers[1].realname
        
    }
    
    
    /**
     * 超过两个个教师类型
     *
     * @param helper
     * @param item
     * @param position
     */
    private fun setDateThree(helper: BaseAdapterHelper, item: LiveCourseListResponse.ListData, position: Int) {
        
        GlideUtils.loadHead(context, item.teachers[1].headUrl, helper.getImageView(R.id.iv_teacher_two_icon))
        helper.getTextView(R.id.tv_order_teacher_name_two).text = item.teachers[1].realname
        
        val iv_more = helper.getView<View>(R.id.iv_more)
        val ll_container = helper.getView<LinearLayout>(R.id.ll_container)
        ll_container.visibility = View.VISIBLE
        iv_more.visibility = View.VISIBLE
        val iv_close = helper.getView<View>(R.id.iv_small)
        iv_close.visibility = View.INVISIBLE
        iv_more.setOnClickListener {
            if (lastExpandPosition != -1 && lastExpandPosition != position) {
                notifyItemChanged(lastExpandPosition)
            }
            //  ll_container.setVisibility(View.VISIBLE);
            AnimationTool.showSlideLeft(ll_container, { iv_close.visibility = View.VISIBLE }, helper.getView<View>(R.id.cl_assistant).width)
            iv_more.visibility = View.GONE
            lastExpandPosition = position
        }
        var downX = 0f
        var endX = 0f
        
        helper.getView<View>(R.id.scrollView).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.rawX
                }
                MotionEvent.ACTION_MOVE -> {
                    endX = event.rawX
                }
                MotionEvent.ACTION_UP -> {
                    if (Math.abs(endX - downX) < 20) {
                        helper.itemView.performClick()
                    }
                }
            }
            false
        }
        
        iv_close.setOnClickListener {
            helper.getView<View>(R.id.scrollView).scrollX = 0
            AnimationTool.hideSlideLeft(ll_container, {
                //     ll_container.setVisibility(View.GONE);
            }, helper.getView<View>(R.id.cl_assistant).width)
            iv_close.visibility = View.INVISIBLE
            iv_more.visibility = View.VISIBLE
            lastExpandPosition = -1
        }
        if (ll_container.childCount <= 1) {
            for (i in 2 until item.teachers.size) {
                val view = View.inflate(context, R.layout.item_teacher, null)
                ll_container.addView(view, 0)
                
                GlideUtils.loadHead(context, item.teachers[i].headUrl, view.findViewById<View>(R.id.iv_assistant) as ImageView)
                (view.findViewById<View>(R.id.tv_head_two) as TextView).text = item.teachers[i].realname
            }
        }
        ll_container.post { ll_container.translationX = (-ll_container.width + helper.getView<View>(R.id.cl_assistant).width).toFloat() }
    }
    
    /**
     * 公共部分
     *
     * @param helper
     * @param item
     * @param position
     */
    private fun setBaseDate(helper: BaseAdapterHelper, item: LiveCourseListResponse.ListData, position: Int) {
        if (!ObjectUtils.isEmpty(item.teachers)) {
            GlideUtils.loadHead(context, item.teachers[0].headUrl, helper.getImageView(R.id.iv_order_teacher_icon))
            helper.getTextView(R.id.tv_order_teacher_name).text = item.teachers[0].realname
        }
        if (!ObjectUtils.isEmpty(item.courseClassDomain) && !TextUtils.isEmpty(item.courseClassDomain.teacherName)) {
            helper.getView<View>(R.id.tv_course_left)?.visibility = View.VISIBLE
            helper.getImageView(R.id.iv_order_assistant_icon).visibility = View.VISIBLE
            helper.getTextView(R.id.tv_assistant_name).visibility = View.VISIBLE
            helper.getTextView(R.id.tv_assistant).visibility = View.VISIBLE
            GlideUtils.loadHead(context, item.courseClassDomain.headUrl, helper.getImageView(R.id.iv_order_assistant_icon))
            helper.getTextView(R.id.tv_assistant_name).text = item.courseClassDomain.teacherName
            helper.getTextView(R.id.tv_course_left).text = context.getString(R.string.class_left_count, item.courseClassDomain.count - item.courseClassDomain.factCount)
        } else {
            helper.getView<View>(R.id.tv_course_left)?.visibility = View.GONE
            helper.getImageView(R.id.iv_order_assistant_icon).visibility = View.GONE
            helper.getTextView(R.id.tv_assistant_name).visibility = View.GONE
            helper.getTextView(R.id.tv_assistant).visibility = View.GONE
        }
        
        //        item.getDeadline()
        val date = TimeUtils.string2Date(item.deadline ?: "")
        if (date == null) {
            helper.getTextView(R.id.tv_course_endTime).visibility = View.INVISIBLE
        } else {
            helper.getTextView(R.id.tv_course_endTime).visibility = View.VISIBLE
            val calendar = Calendar.getInstance()
            val currentYear = calendar.time.year
            val endYear = date.year
            if (currentYear != endYear) {
                helper.getTextView(R.id.tv_course_endTime).text = TimeUtils.date2String(date, SimpleDateFormat("yyyy年MM月dd日")) + "停售"
            } else {
                helper.getTextView(R.id.tv_course_endTime).text = TimeUtils.date2String(date, SimpleDateFormat("MM月dd日")) + "停售"
            }
        }
        
        helper.getView<View>(R.id.tv_course_active).visibility = if (ObjectUtils.isEmpty(item.activityList)) View.GONE else View.VISIBLE
        helper.getTextView(R.id.tv_order_tag).text = item.courseSubjectNames?.substring(0, 1)
        var tvHoliday = helper.getTextView(R.id.tv_order_holiday)
        if (TextUtils.isEmpty(Utils.getHolidayByNumber(item.term, tvHoliday))) {
            helper.getTextView(R.id.tv_order_holiday).visibility = View.GONE
        } else {
            helper.getTextView(R.id.tv_order_holiday).visibility = View.VISIBLE
            helper.getTextView(R.id.tv_order_holiday).text = Utils.getHolidayByNumber(item.term, tvHoliday)
        }
        helper.getTextView(R.id.tv_order_course_name).text = Html.fromHtml(item.name)
        helper.getTextView(R.id.tv_order_course_time).text = Html.fromHtml(item.timeremark)
        val spannableStringBuilder = SpanUtils().append("¥").setFontSize(12, true).setForegroundColor(context.resources.getColor(R.color.FF4F00))
                .appendSpace(4).append(item.price.toString()).setFontSize(20, true).setForegroundColor(context.resources.getColor(R.color.FF4F00))
                .create()
        helper.getTextView(R.id.tv_course_price).text = spannableStringBuilder
        //价格
        var tvOldPrice = helper.getTextView(R.id.tv_oldPrice)
        tvOldPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        if (item.price.compareTo(item.oldPrice) == 0) {
            tvOldPrice.visibility = View.GONE
        } else {
            tvOldPrice.visibility = View.VISIBLE
            tvOldPrice.text = "¥ ${item.oldPrice}"
        }
        //        helper.itemView.setOnClickListener { CourseDetailActivity.start(context, item.id) }
        helper.itemView.setOnClickListener(object : NoDoubleClickListener() {
            override fun onDoubleClick(v: View) {
                CourseDetailActivity.start(context, item.id)
            }
        })
    }
}
