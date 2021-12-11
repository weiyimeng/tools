package com.haoke91.a91edu.adapter

import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.util.ArrayMap
import android.view.View
import android.widget.CheckBox

import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SpanUtils
import com.gaosiedu.live.sdk.android.api.cart.delete.LiveCartDeleteRequest
import com.gaosiedu.live.sdk.android.api.cart.delete.LiveCartDeleteResponse
import com.gaosiedu.live.sdk.android.domain.ShoppingCarDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.net.Api
import com.haoke91.a91edu.net.ResponseCallback
import com.haoke91.a91edu.ui.course.CourseDetailActivity
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.ViewUtils
import com.haoke91.a91edu.utils.imageloader.GlideUtils
import com.haoke91.a91edu.utils.manager.UserManager
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.MultiItemTypeSupport
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter
import com.orhanobut.logger.Logger
import me.drakeet.multitype.MultiTypeAdapter

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/11 下午2:11
 * 修改人：weiyimeng
 * 修改时间：2018/7/11 下午2:11
 * 修改备注：
 */
class CartOrderAdapter(context: Context, data: List<ShoppingCarDomain>?) : QuickWithPositionAdapter<ShoppingCarDomain>(context, object : MultiItemTypeSupport<ShoppingCarDomain> {
    override fun getLayoutId(viewType: Int): Int {
        return when (viewType) {
            1 -> R.layout.item_cart_order
            else -> R.layout.item_cart_order_two
        }
        
    }
    
    override fun getItemViewType(position: Int, item: ShoppingCarDomain): Int {
        return if (ObjectUtils.isEmpty(item.courseDomain.teachers)) {
            1
        } else item.courseDomain.teachers.size
    }
}, data) {
    
    /**
     * key  课程id  value 课程实体
     */
    private val choiceMap: ArrayMap<Int, ShoppingCarDomain> = ArrayMap()
    
    //true  有失效课程
    var isHasInvalid: Boolean = false
        private set
    
    val choiceOrder: ArrayMap<*, *>
        get() = choiceMap
    
    var invalidCourseCount: Int = 0
    
    private var onOrderChangeListener: OnOrderChangeListener? = null
    
    override fun convert(helper: BaseAdapterHelper, item: ShoppingCarDomain, position: Int) {
        val cb_choice_order = helper.getView<CheckBox>(R.id.cb_choice_order)
        ViewUtils.addDefaultScreenArea(cb_choice_order, 20, 20, 20, 20)
        cb_choice_order.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                choiceMap.put(item.id, item)
            } else {
                choiceMap.remove(item.id)
            }
            onOrderChangeListener?.onOrderChange()
        }
        
        if (date?.contains(item.associateId.toString()) && item.expiredStatus != 0) {
            choiceMap.put(item.id, item)
            cb_choice_order.isChecked = true
            onOrderChangeListener?.onOrderChange()
        }
        
        if (item.expiredStatus == 0 || !item.courseDomain.isCoursePurchaseStart) { //失效 未开始
            if (item.expiredStatus == 0) {
                isHasInvalid = true
                invalidCourseCount++
                helper.getTextView(R.id.tv_invalid).visibility = View.VISIBLE
                helper.getTextView(R.id.tv_not_begin).visibility = View.GONE
                cb_choice_order.visibility = View.GONE
            } else {
                helper.getTextView(R.id.tv_invalid).visibility = View.GONE
                helper.getTextView(R.id.tv_not_begin).visibility = View.VISIBLE
                cb_choice_order.visibility = View.VISIBLE
                cb_choice_order.isChecked = choiceMap.containsKey(item.id)
            }
        } else {
            helper.getTextView(R.id.tv_invalid).visibility = View.GONE
            helper.getTextView(R.id.tv_not_begin).visibility = View.GONE
            cb_choice_order.visibility = View.VISIBLE
            cb_choice_order.isChecked = choiceMap.containsKey(item.id)
        }
        
        helper.getTextView(R.id.tv_order_tag).text = item.courseDomain.courseSubjectNames?.substring(0, 1)
        if (TextUtils.isEmpty(Utils.getHolidayByNumber(item.courseDomain.term, null))) {
            helper.getTextView(R.id.tv_order_holiday).visibility = View.GONE
        } else {
            helper.getTextView(R.id.tv_order_holiday).visibility = View.VISIBLE
            helper.getTextView(R.id.tv_order_holiday).text = Utils.getHolidayByNumber(item.courseDomain.term, helper.getTextView(R.id.tv_order_holiday))
        }
        val spannableStringBuilder = SpanUtils().append("¥").setFontSize(12, true).setForegroundColor(context.resources.getColor(R.color.FF4F00))
                .appendSpace(4).append((item.courseDomain.price ?: "").toString()).setFontSize(20, true).setForegroundColor(context.resources.getColor(R.color.FF4F00))
                .create()
        helper.getTextView(R.id.tv_course_price).text = spannableStringBuilder
        helper.getTextView(R.id.tv_order_course_time).text = item.courseDomain.timeremark
        helper.getTextView(R.id.tv_order_course_name).text = Html.fromHtml(item.courseDomain.name ?: " ")
        GlideUtils.loadHead(context, item.courseDomain.teachers[0]?.headUrl, helper.getImageView(R.id.iv_order_teacher_icon))
        helper.getTextView(R.id.tv_order_teacher_name).text = item.courseDomain.teachers[0]?.realname
        if (!ObjectUtils.isEmpty(item.courseClassDomain) && !TextUtils.isEmpty(item.courseClassDomain.teacherName)) {
            helper.getView<View>(R.id.tv_assistant).visibility = View.VISIBLE
            helper.getImageView(R.id.iv_order_assistant_icon).visibility = View.VISIBLE
            helper.getTextView(R.id.tv_assistant_name).visibility = View.VISIBLE
            GlideUtils.loadHead(context, item.courseClassDomain.headUrl, helper.getImageView(R.id.iv_order_assistant_icon))
            helper.getTextView(R.id.tv_assistant_name).text = item.courseClassDomain.teacherName
        } else {
            helper.getImageView(R.id.iv_order_assistant_icon).visibility = View.INVISIBLE
            helper.getTextView(R.id.tv_assistant_name).visibility = View.INVISIBLE
            helper.getView<View>(R.id.tv_assistant).visibility = View.INVISIBLE
            
            
        }
        helper.getView<View>(R.id.tv_order_delete).setOnClickListener { deleteCourseFromCart(position, item) }
        //   helper.getTextView(R.id.txt_course_name).setText(item);
        //        if (!ObjectUtils.isEmpty(item.courseDomain.knowledges)) {
        helper.getTextView(R.id.tv_course_endTime).text = context.getString(R.string.class_count, item.courseDomain.knowlageCount)
        //        } else {
        //            helper.getTextView(R.id.tv_course_endTime).text = context.getString(R.string.class_count, 0)
        //        }
        
        helper.getView<View>(R.id.cl_course).setOnClickListener {
            if (position < all.size) {
                CourseDetailActivity.start(context, all[position].courseDomain.id)
            }
        }
        
        if (getItemViewType(position) != 1) {
            setMoreDate(helper, item, position)
        }
    }
    
    private fun setMoreDate(helper: BaseAdapterHelper, item: ShoppingCarDomain, position: Int) {
        GlideUtils.load(context, item.courseDomain.teachers[1].headUrl, helper.getImageView(R.id.iv_teacher_two_icon))
        helper.getTextView(R.id.tv_order_teacher_name_two).text = item.courseDomain.teachers[1].realname
        if (getItemViewType(position) == 2) {
            helper.getView<View>(R.id.iv_more).visibility = View.GONE
        } else {
            helper.getView<View>(R.id.iv_more).visibility = View.VISIBLE
        }
    }
    
    private fun deleteCourseFromCart(position: Int, item: ShoppingCarDomain) {
        Utils.loading(context)
        val liveCartDeleteRequest = LiveCartDeleteRequest()
        liveCartDeleteRequest.ids = item.id?.toString()
        liveCartDeleteRequest.userId = UserManager.getInstance().userId
        Api.getInstance().post(liveCartDeleteRequest, LiveCartDeleteResponse::class.java, object : ResponseCallback<LiveCartDeleteResponse>() {
            override fun onResponse(date: LiveCartDeleteResponse, isFromCache: Boolean) {
                all.removeAt(position)
                choiceMap.remove(item.id)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, all.size)
                onOrderChangeListener?.onOrderChange()
                
            }
        }, "")
    }
    
    /**
     * @param isEdit true  编辑状态  false  完成状态
     */
    fun changeEditStatus(isEdit: Boolean) {
        //        this.isEdit = isEdit;
        if (isEdit) {
        
        } else {
        
        }
        //   notifyDataSetChanged();
    }
    
    /**
     * @param isAll true 全部选中
     */
    fun choiceAll(isAll: Boolean) {
        if (isAll) {
            if (ObjectUtils.isEmpty(all)) {
                return
            }
            all
                    .filter { !choiceMap.containsKey(it.id) && it.expiredStatus != 0 }
                    .forEach { choiceMap.put(it.id, it) }
        } else {
            choiceMap?.clear()
        }
        notifyDataSetChanged()
    }
    
    fun isAllCheck(): Boolean {
        return all.size - invalidCourseCount == choiceOrder?.size
    }
    
    
    fun setOnOrderChangeListener(onOrderChangeListener: OnOrderChangeListener) {
        this.onOrderChangeListener = onOrderChangeListener
    }
    
    interface OnOrderChangeListener {
        fun onOrderChange()
    }
    
    lateinit var date: String
    fun setChoice(date: String?) {
        this.date = date.toString()
    }
    
    override fun setData(elem: List<ShoppingCarDomain>?) {
        invalidCourseCount = 0
        super.setData(elem)
    }
}
