package com.haoke91.a91edu.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SpanUtils
import com.gaosiedu.scc.sdk.android.api.user.course.list.LiveSccUserCourseListResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.R.id.mCourseName
import com.haoke91.a91edu.ui.learn.CourseOrderActivity
import com.haoke91.a91edu.utils.LogUtil
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.a91edu.widget.dialog.BottomAssistantDialog
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.MultiItemTypeSupport
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter
import kotlinx.android.synthetic.main.activity_coursedetail.*

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/16 下午4:56
 * 修改人：weiyimeng
 * 修改时间：2018/7/16 下午4:56
 * 修改备注：
 */
class MyCourseAdapter(context: Context, dates: List<LiveSccUserCourseListResponse.ListData>) : QuickWithPositionAdapter<LiveSccUserCourseListResponse.ListData>(context, object : MultiItemTypeSupport<LiveSccUserCourseListResponse.ListData> {
    override fun getLayoutId(viewType: Int): Int {
        
        return R.layout.item_my_course
    }
    
    override fun getItemViewType(position: Int, s: LiveSccUserCourseListResponse.ListData): Int {
        return 0
    }
}, dates) {
    
    override fun convert(helper: BaseAdapterHelper, item: LiveSccUserCourseListResponse.ListData, position: Int) {
        setBaseDate(helper, item, position)
    }
    
    private fun setBaseDate(helper: BaseAdapterHelper, item: LiveSccUserCourseListResponse.ListData, position: Int) {
        //        helper.getView(R.id.tv_course_seeAll).setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v){
        //            }
        //        });
        var wxUrl = ""
        var assistantName = ""
        val tv_seeAll = helper.getView<TextView>(R.id.tv_course_seeAll)
        tv_seeAll.setOnClickListener { v -> CourseOrderActivity.start(context, v.tag as String) }
        val tv_holiday = helper.getTextView(R.id.tv_order_holiday)
        val tv_subject = helper.getTextView(R.id.tv_order_tag)
        val tv_time = helper.getTextView(R.id.tv_order_course_time)
        val tv_outOfDate = helper.getTextView(R.id.tv_course_outofDate)
        val view_teacher1 = helper.getView<View>(R.id.layout_teacher1)
        val view_teacher2 = helper.getView<View>(R.id.layout_teacher2)
        val view_assistant = helper.getView<View>(R.id.layout_assistant)
        val view_spots = helper.getView<View>(R.id.v_spots)
        val iv_teacher1_head = view_teacher1.findViewById<ImageView>(R.id.iv_order_teacher_icon)
        val tv_teacher1_name = view_teacher1.findViewById<TextView>(R.id.tv_order_teacher_name)
        val iv_teacher2_head = view_teacher2.findViewById<ImageView>(R.id.iv_order_teacher_icon)
        val tv_teacher2_name = view_teacher2.findViewById<TextView>(R.id.tv_order_teacher_name)
        val iv_assistant_head = helper.getImageView(R.id.iv_order_assistant_icon)
        val tv_assistant_name = helper.getTextView(R.id.tv_assistant_name)
        var d = ContextCompat.getDrawable(context, R.drawable.ic_mine_icon_front)
        d?.setBounds(0, 0, d.intrinsicWidth * 5 / 6, d.intrinsicHeight * 5 / 6)
        tv_seeAll.setCompoundDrawables(null, null, d, null)
        tv_seeAll.tag = item.id.toString() + "," + item.userCourseId
        val term1 = item.term
        if (term1 == null) {
            tv_holiday.visibility = View.GONE
        } else {
            tv_holiday.visibility = View.VISIBLE
            tv_holiday.text = Utils.getHolidayByNumber(term1, tv_holiday)
        }
        if (ObjectUtils.isEmpty(item.courseSubjectNames)) {
            tv_subject.visibility = View.GONE
        } else {
            tv_subject.text = item.courseSubjectNames.substring(0, 1)
        }
        var num = if (tv_holiday.visibility == View.VISIBLE && tv_subject.visibility == View.VISIBLE) {
            helper.getTextView(R.id.tv_order_course_name).textSize * 3
        } else {
            helper.getTextView(R.id.tv_order_course_name).textSize * 2 - ConvertUtils.dp2px(5f)
        }
        val sp = SpanUtils()
        sp.append(Html.fromHtml(item.name)).setLeadingMargin(num.toInt(), 0)
        helper.getTextView(R.id.tv_order_course_name).text = sp.create()
        
        
        tv_time.text = Html.fromHtml(item.timeremark)
        val userCourseStatus = item.userCourseStatus //-1 已退课；2 回放
        when {
            userCourseStatus == null -> tv_outOfDate.visibility = View.GONE
            userCourseStatus.toInt() == -1 -> {
                tv_outOfDate.visibility = View.VISIBLE
                tv_outOfDate.text = "已退课"
                tv_outOfDate.setBackgroundResource(R.drawable.bg_gray_radius50)
            }
            userCourseStatus.toInt() == 2 -> {
                tv_outOfDate.visibility = View.VISIBLE
                tv_outOfDate.setBackgroundResource(R.drawable.bg_pay)
                tv_outOfDate.text = "回放"
            }
            else -> tv_outOfDate.visibility = View.GONE
        }
        val teachers = item.teachers
        if (teachers != null && teachers.size > 0) {
            GlideUtils.loadHead(getContext(), teachers[0].headUrl, iv_teacher1_head)
            tv_teacher1_name.text = teachers[0].realname
            if (teachers.size > 1) {
                view_teacher2.visibility = View.VISIBLE
                GlideUtils.loadHead(getContext(), teachers[1].headUrl, iv_teacher2_head)
                tv_teacher2_name.text = teachers[1].realname
            } else {
                view_teacher2.visibility = View.GONE
            }
        } else {
            view_teacher1.visibility = View.GONE
            view_teacher2.visibility = View.GONE
        }
        val assistants = item.assistants
        var wxId = ""
        if (assistants != null && assistants.size > 0) {
            if (assistants[0] != null) {
                view_assistant.visibility = View.VISIBLE
                GlideUtils.loadHead(getContext(), assistants[0].headUrl, iv_assistant_head)
                tv_assistant_name.text = assistants[0].realname
                assistantName = assistants[0].realname
                wxUrl = if (assistants[0].wxUrl != null) {
                    assistants[0].wxUrl
                } else {
                    ""
                }
                wxId = assistants[0].teacherWxId
            }
        } else {
            view_assistant.visibility = View.GONE
        }
        val icon = helper.getView<View>(R.id.iv_qrcode)
        val finalWxUrl = wxUrl
        val finalAssistantName = assistantName
        iv_assistant_head.setOnClickListener { BottomAssistantDialog.showDialog(context as AppCompatActivity, finalWxUrl, finalAssistantName, wxId) }
        iv_assistant_head.isClickable = !wxUrl.isNullOrBlank()
        icon.visibility = if (wxUrl.isNullOrBlank()) View.GONE else View.VISIBLE
        val status = item.userCourseStatus //-1：退班，1：未完结，2：已完结,3:冻结（冻结状态取消）
        changeTeacherNums(view_spots, status!!.toInt() == 2, teachers != null && teachers.size > 2)
    }
    
    private fun changeTeacherNums(v_spots: View, isOutDate: Boolean, showSpot: Boolean) {
        if (isOutDate) {
            if (showSpot) {
                v_spots.visibility = View.VISIBLE
            } else {
                v_spots.visibility = View.GONE
            }
        } else {
            v_spots.visibility = View.GONE
        }
    }
}
