package com.haoke91.a91edu.adapter

import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.util.ArrayMap
import android.view.View
import android.widget.CheckBox
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SpanUtils
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain
import com.gaosiedu.live.sdk.android.domain.ShoppingCarDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.ui.course.CourseDetailActivity
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.ViewUtils
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.baselibrary.recycleview.SwipeMenuLayout
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.MultiItemTypeSupport
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter
import com.haoke91.baselibrary.utils.ACallBack
import com.orhanobut.logger.Logger

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/15 下午3:55
 * 修改人：weiyimeng
 * 修改时间：2018/8/15 下午3:55
 * 修改备注：
 */
class ApplyBackOrderAdapter(context: Context, dates: List<OrderItemDomain>?) : QuickWithPositionAdapter<OrderItemDomain>(context, object : MultiItemTypeSupport<OrderItemDomain> {
    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.item_cart_order
            else -> R.layout.item_cart_order_two
        }
    }
    
    override fun getItemViewType(position: Int, item: OrderItemDomain): Int {
        return if (ObjectUtils.isEmpty(item.course.teachers)) {
            1
        } else item.course.teachers.size
    }
}, dates) {
    
    /**
     * key  课程id  value 课程实体
     */
    private val choiceMap: ArrayMap<Int, OrderItemDomain> = ArrayMap()
    val choiceOrder: ArrayMap<*, *>
        get() = choiceMap
    
    override fun convert(helper: BaseAdapterHelper, item: OrderItemDomain, position: Int) {
        val course = item.course
        (helper.itemView as SwipeMenuLayout).isSwipeEnable = false
        val cb_choice_order = helper.getView<CheckBox>(R.id.cb_choice_order)
        ViewUtils.addDefaultScreenArea(cb_choice_order, 20, 20, 20, 20)
        if (item.isRefundable) {
            cb_choice_order.visibility = View.VISIBLE
            cb_choice_order.isEnabled = true
        } else {
            cb_choice_order.visibility = View.INVISIBLE
            cb_choice_order.isEnabled = false
        }
        cb_choice_order.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && item.isRefundable) {
                choiceMap.put(item.id, item)
            } else {
                choiceMap.remove(item.id)
            }
            onOrderChangeListener?.call(null)
        }
        if (!ObjectUtils.isEmpty(visibility)) {
            cb_choice_order.visibility = visibility!!
        }
        helper.getTextView(R.id.tv_order_course_name).text = Html.fromHtml(course.name ?: "")
        helper.getTextView(R.id.tv_order_course_time).text = course.timeremark
        val spannableStringBuilder = SpanUtils().append("¥").setFontSize(12, true).setForegroundColor(context.resources.getColor(R.color.FF4F00))
                .appendSpace(4).append(item.price.toString()).setFontSize(20, true).setForegroundColor(context.resources.getColor(R.color.FF4F00))
                .create()
        helper.getTextView(R.id.tv_course_price).text = spannableStringBuilder!!
        if (!ObjectUtils.isEmpty(course.teachers)) {
            helper.getTextView(R.id.tv_order_teacher_name).text = course.teachers[0].realname
            GlideUtils.loadHead(context, course.teachers[0].headUrl, helper.getImageView(R.id.iv_order_teacher_icon))
        }
        
        if (ObjectUtils.isEmpty(course.courseClassDomain)) {
            helper.getTextView(R.id.tv_assistant_name).visibility = View.INVISIBLE
            helper.getImageView(R.id.iv_order_assistant_icon).visibility = View.INVISIBLE
            helper.getView<View>(R.id.tv_assistant).visibility = View.INVISIBLE
        } else {
            helper.getTextView(R.id.tv_assistant_name).visibility = View.VISIBLE
            helper.getImageView(R.id.iv_order_assistant_icon).visibility = View.VISIBLE
            helper.getView<View>(R.id.tv_assistant).visibility = View.VISIBLE
            helper.getTextView(R.id.tv_assistant_name).text = course.courseClassDomain.teacherName
            GlideUtils.loadHead(context, course.courseClassDomain.headUrl, helper.getImageView(R.id.iv_order_assistant_icon))
        }
        
        
        if (TextUtils.isEmpty(Utils.getHolidayByNumber(course.term, null))) {
            helper.getTextView(R.id.tv_order_holiday).visibility = View.GONE
        } else {
            var tvHoliday = helper.getTextView(R.id.tv_order_holiday)
            helper.getTextView(R.id.tv_order_holiday).visibility = View.VISIBLE
            tvHoliday.text = Utils.getHolidayByNumber(course.term, tvHoliday)
        }
        helper.getTextView(R.id.tv_order_tag).text = course.courseSubjectNames?.substring(0, 1)
        if (getItemViewType(position) != 1) {
            setMoreDate(helper, item, position)
        }
        
        helper.getView<View>(R.id.tv_course_endTime).visibility = View.GONE
        helper.getView<View>(R.id.cl_course).setOnClickListener {
            if (position < all.size) {
                cb_choice_order.performClick()
                onOrderChangeListener?.call(item)
            }
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
    
    private var onOrderChangeListener: ACallBack<OrderItemDomain>? = null
    
    
    fun setOnOrderChangeListener(onOrderChangeListener: ACallBack<OrderItemDomain>) {
        this.onOrderChangeListener = onOrderChangeListener
    }
    
    private var visibility: Int? = null
    fun setBoxVisibility(visibility: Int) {
        this.visibility = visibility
    }
}
