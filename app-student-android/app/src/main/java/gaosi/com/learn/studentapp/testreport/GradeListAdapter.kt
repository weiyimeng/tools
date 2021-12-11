package gaosi.com.learn.studentapp.testreport

import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import gaosi.com.learn.R
import gaosi.com.learn.bean.TestReportGradeBean

private const val CATEGORY = 1//分类
private const val CONTENT = 2//内容

/**
 * description:
 * created by huangshan on 2020-04-10 17:47
 */
class GradeListAdapter : BaseQuickAdapter<TestReportGradeBean, BaseViewHolder>(null) {

    init {
        multiTypeDelegate = object : MultiTypeDelegate<TestReportGradeBean>() {
            override fun getItemType(data: TestReportGradeBean): Int {
                return if (data.itemType == 3) {
                    2
                } else {
                    data.itemType
                }
            }
        }
        multiTypeDelegate
                .registerItemType(CATEGORY, R.layout.item_test_report_grade_category)
                .registerItemType(CONTENT, R.layout.item_test_report_grade_content)
    }

    override fun convert(helper: BaseViewHolder?, data: TestReportGradeBean?) {
        when (helper?.itemViewType) {
            CATEGORY -> {
                val tvCategory = helper.getView<TextView>(R.id.tvCategory)
                tvCategory.text = data?.gradeName
            }
            CONTENT -> {
                val tvGrade = helper.getView<TextView>(R.id.tvGrade)
                tvGrade.text = data?.gradeName
                if (data?.isClicked == true) {
                    tvGrade.setTextColor(Color.parseColor("#E6FFFFFF"))
                    tvGrade.setBackgroundResource(R.drawable.bg_dress_save)
                } else {
                    tvGrade.setTextColor(Color.parseColor("#E6051535"))
                    tvGrade.setBackgroundResource(R.drawable.bg_gray_shape_44)
                }
            }
        }
    }
}