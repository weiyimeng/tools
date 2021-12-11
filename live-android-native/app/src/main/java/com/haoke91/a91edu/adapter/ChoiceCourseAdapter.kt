package com.haoke91.a91edu.adapter

import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import com.blankj.utilcode.util.ObjectUtils
import com.gaosiedu.live.sdk.android.api.course.transfer.list.LiveCourseTransferListResponse
import com.haoke91.a91edu.R
import com.haoke91.a91edu.utils.Utils
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/27 6:31 PM
 * 修改人：weiyimeng
 * 修改时间：2018/11/27 6:31 PM
 * 修改备注：
 * @version
 */
class ChoiceCourseAdapter(context: Context, date: List<LiveCourseTransferListResponse.ListData>?) : QuickWithPositionAdapter<LiveCourseTransferListResponse.ListData>(context, R.layout.item_choice_course, date) {
    private var checkPosition: Int? = null
    
    override fun convert(helper: BaseAdapterHelper, item: LiveCourseTransferListResponse.ListData, position: Int) {
        helper.getTextView(R.id.tv_order_course_name).text = Html.fromHtml(item?.name)
        helper.getTextView(R.id.tv_order_tag).text = item?.courseSubjectNames?.substring(0, 1)
        val tvHoliday = helper.getTextView(R.id.tv_order_holiday)
        if (TextUtils.isEmpty(Utils.getHolidayByNumber(item?.term, tvHoliday))) {
            helper.getTextView(R.id.tv_order_holiday).visibility = View.GONE
        } else {
            helper.getTextView(R.id.tv_order_holiday).visibility = View.VISIBLE
            helper.getTextView(R.id.tv_order_holiday).text = Utils.getHolidayByNumber(item?.term, tvHoliday)
        }
        
        val cb_coupons = helper.getView<View>(R.id.cb_cart_choice_all) as CheckBox
        cb_coupons.isChecked = checkPosition == position
        if (!ObjectUtils.isEmpty(item.knowledges)) {
            helper.getTextView(R.id.tv_course_name).text = String.format("第%s讲  %s", Utils.convertLowerCaseToUpperCase(item.knowledges[0]?.displayOrder ?: 0), Html.fromHtml(item.knowledges[0]?.name)) //名称
            helper.getTextView(R.id.tv_teacher_name).text = item.knowledges[0]?.teacherName
            helper.getTextView(R.id.tv_course_time).text = String.format("%s-%s", Utils.dateToString(item.knowledges[0]?.startTime, "yyyy-MM-dd HH:mm"), Utils.dateToString(item.knowledges[0]?.endTime, "HH:mm"))
        }
        helper.itemView.setOnClickListener {
            if (cb_coupons.isChecked) {
                cb_coupons.isChecked = false
                checkPosition = -1
            } else {
                checkPosition = position
                cb_coupons.isChecked = true
            }
            notifyDataSetChanged()
        }
        cb_coupons.isEnabled = false
        //        cb_coupons.setOnClickListener {
        //            if (cb_coupons.isChecked) {
        //                cb_coupons.isChecked = false
        //                checkPosition = -1
        //            } else {
        //                checkPosition = position
        //                cb_coupons.isChecked = true
        //            }
        //            notifyDataSetChanged()
        //        }
    }
    
    fun getCheckCourse(): Int? {
        return checkPosition
    }
}
