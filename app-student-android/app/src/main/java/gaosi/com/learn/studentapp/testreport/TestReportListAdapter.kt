package gaosi.com.learn.studentapp.testreport

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gsbaselib.utils.glide.ImageLoader
import gaosi.com.learn.R
import gaosi.com.learn.bean.TestReportClassBean
import gaosi.com.learn.studentapp.classlesson.status.SubjectId
import gaosi.com.learn.view.CenterAlignImageSpan
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.text.SpannableString
import android.text.style.ImageSpan
import com.gsbaselib.utils.TypeValue

/**
 * description:
 * created by huangshan on 2020-04-13 15:13
 */
class TestReportListAdapter : BaseQuickAdapter<TestReportClassBean, BaseViewHolder>(R.layout.item_test_report_list) {

    override fun convert(helper: BaseViewHolder?, data: TestReportClassBean?) {
        helper?.let {
            val cardView = it.getView<CardView>(R.id.cardView)
            val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
            val tvClassName = it.getView<TextView>(R.id.tvClassName)
            val ivTeacherHeader = it.getView<ImageView>(R.id.ivTeacherHeader)
            val tvTeacherName = it.getView<TextView>(R.id.tvTeacherName)
            val rlMidterm = it.getView<RelativeLayout>(R.id.rl_midterm)
            val vTestReportNotify1 = it.getView<View>(R.id.vTestReportNotify1)
            val rlFinalterm = it.getView<RelativeLayout>(R.id.rl_finalterm)
            val vTestReportNotify2 = it.getView<View>(R.id.vTestReportNotify2)
            it.addOnClickListener(R.id.rl_midterm)
            it.addOnClickListener(R.id.rl_finalterm)
            rlMidterm.visibility = View.GONE
            rlFinalterm.visibility = View.GONE
            if (it.layoutPosition == getData().size - 1) {
                params.setMargins(TypeValue.dp2px(16f), 0, TypeValue.dp2px(16f), TypeValue.dp2px(15f))
            } else {
                params.setMargins(TypeValue.dp2px(16f), 0, TypeValue.dp2px(16f), TypeValue.dp2px(32f))
            }
            cardView.layoutParams = params
            data?.run {
                val spannableString = SpannableString("      $className")

                val subjectDrawable = ContextCompat.getDrawable(mContext, SubjectId.getSubjectLabel(subjectId))
                subjectDrawable?.setBounds(0, 0, TypeValue.dp2px(18F), TypeValue.dp2px(18F))
                val imageSpan = CenterAlignImageSpan(subjectDrawable)
                spannableString.setSpan(imageSpan, 0, 2, ImageSpan.ALIGN_BASELINE)

                val periodDrawable = ContextCompat.getDrawable(mContext, getPeriodLabel(periodId))
                periodDrawable?.setBounds(0, 0, TypeValue.dp2px(18F), TypeValue.dp2px(18F))
                val imageSpan1 = CenterAlignImageSpan(periodDrawable)
                spannableString.setSpan(imageSpan1, 3, 5, ImageSpan.ALIGN_BASELINE)
                tvClassName.text = spannableString

                ImageLoader.setCircleImageViewResource(ivTeacherHeader, teacherAvatarUrl
                        ?: "", R.drawable.icon_default_teacher_header)
                tvTeacherName.text = teacherName
                reports?.forEach { testReportDetailBean ->
                    if (testReportDetailBean.reportType == 1) {
                        rlMidterm.visibility = View.VISIBLE
                        if (testReportDetailBean.viewed == 1) {
                            vTestReportNotify1.visibility = View.INVISIBLE
                        } else {
                            vTestReportNotify1.visibility = View.VISIBLE
                        }
                    } else if (testReportDetailBean.reportType == 2) {
                        rlFinalterm.visibility = View.VISIBLE
                        if (testReportDetailBean.viewed == 1) {
                            vTestReportNotify2.visibility = View.INVISIBLE
                        } else {
                            vTestReportNotify2.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取学期标签
     */
    private fun getPeriodLabel(periodId: Int?): Int {
        return when (periodId) {
            1 -> R.drawable.icon_spring_label
            2 -> R.drawable.icon_summer_label
            3 -> R.drawable.icon_autumn_label
            4 -> R.drawable.icon_winter_label
            else -> R.drawable.icon_spring_label
        }
    }
}