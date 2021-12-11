package gaosi.com.learn.studentapp.active.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gsbaselib.utils.glide.ImageLoader
import com.gstudentlib.bean.StudentInfo
import gaosi.com.learn.R

/**
 * 作者：created by 逢二进一 on 2019/5/9 10:43
 * 邮箱：dingyuanzheng@gaosiedu.com
 */

class SelectStudentAdapter : BaseQuickAdapter<StudentInfo, BaseViewHolder>(R.layout.item_select_student) {

    override fun convert(holder: BaseViewHolder, data: StudentInfo) {
        val cardView = holder.getView<CardView>(R.id.cardView)
        val ivRight = holder.getView<ImageView>(R.id.ivRight)
        val tvName = holder.getView<TextView>(R.id.tvName)
        val tvInstitutionName = holder.getView<TextView>(R.id.tvInstitutionName)
        val ivHeader = holder.getView<ImageView>(R.id.ivHeader)

        tvName?.text = data.studentName + ""
        if (data.institutionName != null) {
            if (data.institutionName.length > 25) {
                val mInstitutionName = data.institutionName.substring(0, 25) + "..."
                tvInstitutionName.text = mInstitutionName
            } else {
                tvInstitutionName.text = data.institutionName
            }
            tvInstitutionName.visibility = View.VISIBLE
        } else {
            tvInstitutionName.visibility = View.GONE
        }
        if (data.path != null) {
            ImageLoader.setCircleImageViewResource(ivHeader, data.path, R.drawable.icon_default_header)
        } else {
            ivHeader?.setImageResource(R.drawable.icon_default_header)
        }
        if(data.isSelectStatus) {
            cardView?.setCardBackgroundColor(Color.parseColor("#A9DC35"))
            ivHeader?.background = ContextCompat.getDrawable(mContext, R.drawable.bg_header_white)
            tvName?.setTextColor(Color.parseColor("#FFFFFF"))
            tvInstitutionName.setTextColor(Color.parseColor("#FFFFFF"))
            ivRight?.visibility = View.VISIBLE
        }else {
            cardView?.setCardBackgroundColor(Color.parseColor("#F5F7FB"))
            ivHeader?.background = ContextCompat.getDrawable(mContext, R.drawable.bg_header_green)
            tvName?.setTextColor(Color.parseColor("#636B7B"))
            tvInstitutionName.setTextColor(Color.parseColor("#9BA1AC"))
            ivRight?.visibility = View.INVISIBLE
        }
    }
}
