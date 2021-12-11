package com.haoke91.a91edu.adapter

import android.app.Activity
import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.ObjectUtils
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.ui.address.AddressManagerActivity
import com.haoke91.a91edu.ui.course.CourseDetailActivity
import com.haoke91.a91edu.ui.order.AllOrderFragment
import com.haoke91.a91edu.ui.order.ChangeClassActivity
import com.haoke91.a91edu.ui.order.ChangeCourseActivity
import com.haoke91.a91edu.ui.order.ChoiceChangeClassActivity
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.a91edu.widget.NoDoubleClickListener
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.MultiItemTypeSupport
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter
import java.util.logging.LoggingMXBean

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/20 6:58 PM
 * 修改人：weiyimeng
 * 修改时间：2018/11/20 6:58 PM
 * 修改备注：
 * @version
 */

class ChangeClassAdapter(context: Context, dates: List<Any>) : QuickWithPositionAdapter<Any>(context, object : MultiItemTypeSupport<Any> {
    override fun getItemViewType(position: Int, item: Any): Int {
        if (item is OrderItemDomain) {
            return 0
        } else if (item is Int) {
            return 1
        }
        return 2
    }
    
    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.item_change_class_tittle
            2 -> R.layout.item_change_class
            else -> R.layout.item_change_class
        }
    }
}, dates) {
    override fun convert(helper: BaseAdapterHelper, item: Any?, position: Int) {
        when (getItemViewType(position)) {
            0 -> setCourseInfo(helper, item)
            1 -> setTittleInfo(helper, item)
        }
    }
    
    private fun setTittleInfo(helper: BaseAdapterHelper, item: Any?) {
        helper.itemView.setOnClickListener(object : NoDoubleClickListener() {
            override fun onDoubleClick(v: View) {
                ChoiceChangeClassActivity.start(context, item as Int)
            }
        })
    }
    
    private fun setCourseInfo(helper: BaseAdapterHelper, item: Any?) {
        val course = (item as OrderItemDomain).course
        helper.getTextView(R.id.tv_course_count).text = context.getString(R.string.course_left_count, course.knowlageCount, course.completedCount)
        helper.getTextView(R.id.tv_order_tag).text = course.courseSubjectNames?.substring(0, 1)
        //        helper.getTextView(R.id.tv_order_holiday).text = Utils.getHolidayByNumber(course.term)
        var tvHoliday = helper.getTextView(R.id.tv_order_holiday)
        if (TextUtils.isEmpty(Utils.getHolidayByNumber(course.term, tvHoliday))) {
            helper.getTextView(R.id.tv_order_holiday).visibility = View.GONE
        } else {
            helper.getTextView(R.id.tv_order_holiday).visibility = View.VISIBLE
            helper.getTextView(R.id.tv_order_holiday).text = Utils.getHolidayByNumber(course.term, tvHoliday)
        }
        helper.getTextView(R.id.tv_order_course_name).text = Html.fromHtml(course.name)
        helper.getTextView(R.id.tv_order_course_time).text = course.timeremark
        helper.getTextView(R.id.tv_order_money).text = "¥" + item.price
        if (!ObjectUtils.isEmpty(course.teachers)) {
            GlideUtils.loadHead(context, course.teachers[0].headUrl, helper.getImageView(R.id.iv_order_teacher_icon))
            helper.getTextView(R.id.tv_order_teacher_name).text = course.teachers[0].realname
        }
        if (!ObjectUtils.isEmpty(course.courseClassDomain)) {
            helper.getImageView(R.id.iv_order_assistant_icon).visibility = View.VISIBLE
            helper.getTextView(R.id.tv_assistant_name).visibility = View.VISIBLE
            helper.getView<View>(R.id.tv_assistant).visibility = View.VISIBLE
            GlideUtils.loadHead(context, course.courseClassDomain.headUrl, helper.getImageView(R.id.iv_order_assistant_icon))
            helper.getTextView(R.id.tv_assistant_name).text = course.courseClassDomain.teacherName
        } else {
            helper.getImageView(R.id.iv_order_assistant_icon).visibility = View.INVISIBLE
            helper.getTextView(R.id.tv_assistant_name).visibility = View.INVISIBLE
            helper.getView<View>(R.id.tv_assistant).visibility = View.INVISIBLE
        }
        
        if (item.course.teachers?.size ?: 0 >= 2) {
            GlideUtils.load(context, item.course.teachers[1].headUrl, helper.getImageView(R.id.iv_teacher_two_icon))
            helper.getTextView(R.id.tv_order_teacher_name_two).text = item.course.teachers[1].realname
            if (item.course.teachers.size > 2) {
                8
                helper.getView<View>(R.id.iv_more).visibility = View.VISIBLE
            } else {
                helper.getView<View>(R.id.iv_more).visibility = View.GONE
            }
        } else {
            helper.getView<View>(R.id.iv_more).visibility = View.GONE
            helper.getImageView(R.id.iv_teacher_two_icon).visibility = View.GONE
            helper.getView<View>(R.id.tv_order_teacher_name_two).visibility = View.GONE
            helper.getView<View>(R.id.tv_teacher_main).visibility = View.GONE
        }
        
        
        helper.itemView.setOnClickListener { CourseDetailActivity.start(context, item.courseId) }
        
    }
    
}
