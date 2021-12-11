package com.haoke91.a91edu.adapter

import android.app.Activity
import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout

import com.blankj.utilcode.util.ObjectUtils
import com.gaosiedu.live.sdk.android.api.user.address.list.LiveUserAddressListResponse
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.ui.course.CourseDetailActivity
import com.haoke91.a91edu.ui.order.AllOrderFragment
import com.haoke91.a91edu.ui.order.ChangeClassActivity
import com.haoke91.a91edu.ui.order.ChangeCourseActivity
import com.haoke91.a91edu.ui.order.ChoiceChangeClassActivity
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.MultiItemTypeSupport
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter
import com.haoke91.baselibrary.utils.DensityUtil

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/8 上午11:52
 * 修改人：weiyimeng
 * 修改时间：2018/8/8 上午11:52
 * 修改备注：
 */
class OrderDetailAdapter(context: Context, dates: List<OrderItemDomain>?) : QuickWithPositionAdapter<OrderItemDomain>(context, object : MultiItemTypeSupport<OrderItemDomain> {
    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.item_oder_detail
            else -> R.layout.item_oder_detail_two
        }
    }
    
    override fun getItemViewType(position: Int, item: OrderItemDomain): Int {
        return if (ObjectUtils.isEmpty(item.course.teachers)) {
            1
        } else item.course.teachers.size
    }
}, dates) {
    
    private var orderType: String? = null
    
    override fun convert(helper: BaseAdapterHelper, item: OrderItemDomain, position: Int) {
        val course = item.course
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
        helper.getTextView(R.id.tv_order_money).text = "¥${item.price}"
        if (!ObjectUtils.isEmpty(course.teachers)) {
            GlideUtils.loadHead(context, course.teachers[0].headUrl, helper.getImageView(R.id.iv_order_teacher_icon))
            helper.getTextView(R.id.tv_order_teacher_name).text = course.teachers[0].realname
        }
        if (!ObjectUtils.isEmpty(course.courseClassDomain) && !TextUtils.isEmpty(course.courseClassDomain.teacherName)) {
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
        
        
        helper.itemView.setOnClickListener { CourseDetailActivity.start(context, item.courseId) }
        
        if (ObjectUtils.isEmpty(item.course.courseEnclosureDomains) && !item.isTransferCourse && !item.isChangeCourse) {
            helper.getView<View>(R.id.ll_order_extra).visibility = View.GONE
        } else {
            helper.getView<View>(R.id.ll_order_extra).visibility = View.VISIBLE
            if (!ObjectUtils.isEmpty(item.course.courseEnclosureDomains)) {
                val stringBuilder = StringBuilder()
                for (date in item.course.courseEnclosureDomains) {
                    stringBuilder.append(date.name).append("x").append(date.count).append("\n")
                }
                val s = stringBuilder.toString().substring(0, stringBuilder.toString().length - 1)
                helper.getTextView(R.id.tv_material).text = s
            } else {
                helper.getTextView(R.id.tv_material).visibility = View.GONE
                helper.getView<View>(R.id.tv_tittle).visibility = View.GONE
            }
            if (orderType == AllOrderFragment.have_pay || orderType == AllOrderFragment.back_order_some
                    || orderType == AllOrderFragment.cancel_back || orderType == AllOrderFragment.reject_back) {
                //调课
                if (item.isTransferCourse) {
                    helper.getView<View>(R.id.tv_change_order).visibility = View.VISIBLE
                    helper.getView<View>(R.id.tv_change_order).setOnClickListener {
                        item.orderNo = orderNo ?: ""
                        ChangeCourseActivity.start(context, item)
                    }
                } else {
                    helper.getView<View>(R.id.tv_change_order).visibility = View.GONE
                }
                //转班
                if (item.isChangeCourse) {
                    helper.getView<View>(R.id.tv_change_course).visibility = View.VISIBLE
                    helper.getView<View>(R.id.tv_change_course).setOnClickListener {
                        item.orderNo = orderNo ?: ""
                        //                        ChangeClassActivity.start(context, item)
                        //                        ChangeClassActivity.start(context, item)
                        ChoiceChangeClassActivity.start(context, item)
                        
                    }
                    if (item.isTransferCourse) {
                        (helper.getView<View>(R.id.tv_change_course).layoutParams as RelativeLayout.LayoutParams).rightMargin = DensityUtil.dip2px(context, 60f)
                    } else {
                        (helper.getView<View>(R.id.tv_change_course).layoutParams as RelativeLayout.LayoutParams).rightMargin = DensityUtil.dip2px(context, 0f)
                    }
                } else {
                    helper.getView<View>(R.id.tv_change_course).visibility = View.GONE
                }
                
            } else {
                helper.getView<View>(R.id.tv_change_order).visibility = View.GONE
                helper.getView<View>(R.id.tv_change_course).visibility = View.GONE
            }
            
        }
        
        
        if (getItemViewType(position) != 1) {
            setMoreDate(helper, item, position)
        }
    }
    
    private fun setMoreDate(helper: BaseAdapterHelper, item: OrderItemDomain, position: Int) {
        GlideUtils.load(context, item.course.teachers[1].headUrl, helper.getImageView(R.id.iv_teacher_two_icon))
        helper.getTextView(R.id.tv_order_teacher_name_two).text = item.course.teachers[1].realname
        if (getItemViewType(position) == 2) {
            helper.getView<View>(R.id.iv_more).visibility = View.GONE
        } else {
            helper.getView<View>(R.id.iv_more).visibility = View.VISIBLE
        }
    }
    
    fun setOrderType(orderType: String?) {
        this.orderType = orderType
    }
    
    private var orderNo: String? = null
    
    fun setOrderNo(orderNo: String?) {
        this.orderNo = orderNo
    }
}
