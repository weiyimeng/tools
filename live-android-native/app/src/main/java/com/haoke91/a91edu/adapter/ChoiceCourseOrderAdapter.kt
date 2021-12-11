package com.haoke91.a91edu.adapter

import android.content.Context
import android.view.View
import android.widget.CheckBox
import com.blankj.utilcode.util.ObjectUtils
import com.gaosiedu.live.sdk.android.domain.CourseKnowledgeDomain
import com.haoke91.a91edu.R
import com.haoke91.a91edu.R.id.cb_choice_order
import com.haoke91.a91edu.R.id.cb_coupons
import com.haoke91.a91edu.utils.Utils
import com.haoke91.a91edu.utils.ViewUtils
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/26 8:40 PM
 * 修改人：weiyimeng
 * 修改时间：2018/11/26 8:40 PM
 * 修改备注：
 * @version
 */
class ChoiceCourseOrderAdapter(context: Context, dates: List<CourseKnowledgeDomain>?) : QuickWithPositionAdapter<CourseKnowledgeDomain>(context, R.layout.item_course_orderm, dates) {
    private var checkPosition: Int? = null
    override fun convert(helper: BaseAdapterHelper, item: CourseKnowledgeDomain, position: Int) {
        if ("wating" != item.flag) {
            helper.itemView.visibility = View.GONE
            return
        }
        val cb_coupons = helper.getView<View>(R.id.cb_coupons) as CheckBox
        cb_coupons.isChecked = checkPosition == position
        helper.getTextView(R.id.tv_course_name).text = String.format("第%s讲  %s", Utils.convertLowerCaseToUpperCase(item.displayOrder), item.name) //名称
        helper.getTextView(R.id.tv_teacher_name).text = item.teacherName
        helper.getTextView(R.id.tv_course_time).text = String.format("%s-%s", Utils.dateToString(item.startTime, "yyyy-MM-dd HH:mm"), Utils.dateToString(item.endTime, "HH:mm"))
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
        //                cb_coupons.isEnabled = false
        cb_coupons.setOnClickListener {
            if (cb_coupons.isChecked) {
                cb_coupons.isChecked = false
                checkPosition = -1
            } else {
                checkPosition = position
                cb_coupons.isChecked = true
            }
            notifyDataSetChanged()
        }
    }
    
    fun getCourseId(): Int {
        return all[checkPosition!!].id
    }
    
    fun getCheckCourse(): Int? {
        return if (ObjectUtils.isEmpty(checkPosition) || checkPosition == -1) {
            -1
        } else {
            all[checkPosition!!].displayOrder
        }
    }
}
