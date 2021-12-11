package gaosi.com.learn.studentapp.login

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gsbaselib.utils.glide.ImageLoader
import com.gstudentlib.base.STBaseConstants
import com.gstudentlib.bean.StudentInfo
import gaosi.com.learn.R

/**
 *  选择学员adapter
 * Created by huangshan on 2018/8/13.
 */
class UserListAdapter : BaseQuickAdapter<StudentInfo, BaseViewHolder>(R.layout.item_user_list) {

    override fun convert(holder: BaseViewHolder, data: StudentInfo) {

        val rlUser = holder.getView<RelativeLayout>(R.id.rlUser)
        val ivHeaderBg = holder.getView<ImageView>(R.id.ivHeaderBg)
        val ivRight = holder.getView<ImageView>(R.id.ivRight)
        val tvName = holder.getView<TextView>(R.id.tvName)
        val tvInstitutionName = holder.getView<TextView>(R.id.tvInstitutionName)
        val ivHeader = holder.getView<ImageView>(R.id.ivHeader)
        tvName?.text = data.truthName
        if (data.institutionName != null) {
            tvInstitutionName.text = data.institutionName
            tvInstitutionName.visibility = View.VISIBLE
        } else {
            tvInstitutionName.visibility = View.GONE
        }
        if (data.path != null) {
            ImageLoader.setCircleImageViewResource(ivHeader, data.path, R.drawable.icon_default_header)
        } else {
            ivHeader?.setImageResource(R.drawable.icon_default_header)
        }
        var bgId = R.drawable.app_bg_change_student_1
        var bgSelectId = R.drawable.app_bg_change_student_1_select
        var rescourseBgId = R.drawable.app_icon_change_student_1
        when(holder.position % 4) {
            0 -> {
                bgId = R.drawable.app_bg_change_student_1
                bgSelectId = R.drawable.app_bg_change_student_1_select
                rescourseBgId = R.drawable.app_icon_change_student_1
            }
            1 -> {
                bgId = R.drawable.app_bg_change_student_2
                bgSelectId = R.drawable.app_bg_change_student_2_select
                rescourseBgId = R.drawable.app_icon_change_student_2
            }
            2 -> {
                bgId = R.drawable.app_bg_change_student_3
                bgSelectId = R.drawable.app_bg_change_student_3_select
                rescourseBgId = R.drawable.app_icon_change_student_3
            }
            3 -> {
                bgId = R.drawable.app_bg_change_student_4
                bgSelectId = R.drawable.app_bg_change_student_4_select
                rescourseBgId = R.drawable.app_icon_change_student_4
            }
        }
        ivHeaderBg.setImageResource(rescourseBgId)
        if (STBaseConstants.userInfo != null) {
            if (STBaseConstants.userInfo.id == data.id) {
                rlUser?.background =  ContextCompat.getDrawable(mContext, bgSelectId)
                ivHeader?.background = ContextCompat.getDrawable(mContext, R.drawable.bg_header_white)
                ivRight?.visibility = View.VISIBLE
            } else {
                rlUser?.background =  ContextCompat.getDrawable(mContext, bgId)
                ivHeader?.background = ContextCompat.getDrawable(mContext, R.drawable.bg_header_green)
                ivRight?.visibility = View.INVISIBLE
            }
        } else {
            rlUser?.background =  ContextCompat.getDrawable(mContext, bgId)
            ivHeader?.background = ContextCompat.getDrawable(mContext, R.drawable.bg_header_green)
            ivRight?.visibility = View.INVISIBLE
        }
    }
}
