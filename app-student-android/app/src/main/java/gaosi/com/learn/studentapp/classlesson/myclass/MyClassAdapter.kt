package gaosi.com.learn.studentapp.classlesson.myclass

import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.style.ImageSpan
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gsbaselib.utils.DateUtil
import com.gsbaselib.utils.TypeValue
import com.gsbaselib.utils.glide.ImageLoader
import gaosi.com.learn.R
import gaosi.com.learn.bean.classlesson.myclass.MyClassInfoBean
import gaosi.com.learn.studentapp.classlesson.status.CourseTypeFlag
import gaosi.com.learn.studentapp.classlesson.status.CourseTypeId
import gaosi.com.learn.studentapp.classlesson.status.SubjectId
import gaosi.com.learn.view.CenterAlignImageSpan

/**
 * description:
 * created by huangshan on 2020/6/16 下午7:55
 */
class MyClassAdapter : BaseQuickAdapter<MyClassInfoBean, BaseViewHolder>(R.layout.item_my_class) {

    override fun convert(helper: BaseViewHolder?, data: MyClassInfoBean?) {
        helper?.run {
            val ivTeacherHead1 = getView<ImageView>(R.id.ivTeacherHead1)
            val ivTeacherHead2 = getView<ImageView>(R.id.ivTeacherHead2)

            data?.run {
                if (subjectId == 0) {
                    //多学科不显示
                    val text = if (courseTypeFlag ?: 0 == CourseTypeFlag.GOOD_COURSE || courseTypeFlag == CourseTypeFlag.SPECIAL_SUBJECT_COURSE) "        $className" else "    $className"
                    val spannableString = SpannableString(text)
                    val courseTypeDrawable = ContextCompat.getDrawable(mContext, CourseTypeId.getCourseTypeLabel(courseType))
                    courseTypeDrawable?.setBounds(0, 0, TypeValue.dp2px(44F), TypeValue.dp2px(20F))
                    val imageSpan = CenterAlignImageSpan(courseTypeDrawable)
                    spannableString.setSpan(imageSpan, 0, 2, ImageSpan.ALIGN_BASELINE)
                    if (courseTypeFlag ?: 0 == CourseTypeFlag.GOOD_COURSE || courseTypeFlag == CourseTypeFlag.SPECIAL_SUBJECT_COURSE) {
                        val courseTypeFlagDrawable = ContextCompat.getDrawable(mContext, CourseTypeFlag.getCourseTypeFlag(courseTypeFlag))
                        courseTypeFlagDrawable?.setBounds(0, 0, TypeValue.dp2px(32F), TypeValue.dp2px(20F))
                        val imageSpan2 = CenterAlignImageSpan(courseTypeFlagDrawable)
                        spannableString.setSpan(imageSpan2, 4, 6, ImageSpan.ALIGN_BASELINE)
                    }
                    setText(R.id.tvClassName, spannableString)
                } else {
                    val text = if (courseTypeFlag ?: 0 == CourseTypeFlag.GOOD_COURSE || courseTypeFlag == CourseTypeFlag.SPECIAL_SUBJECT_COURSE) "            $className" else "        $className"
                    val spannableString = SpannableString(text)
                    val courseTypeDrawable = ContextCompat.getDrawable(mContext, CourseTypeId.getCourseTypeLabel(courseType))
                    courseTypeDrawable?.setBounds(0, 0, TypeValue.dp2px(44F), TypeValue.dp2px(20F))
                    val imageSpan = CenterAlignImageSpan(courseTypeDrawable)
                    spannableString.setSpan(imageSpan, 0, 2, ImageSpan.ALIGN_BASELINE)

                    if (courseTypeFlag ?: 0 == CourseTypeFlag.GOOD_COURSE || courseTypeFlag == CourseTypeFlag.SPECIAL_SUBJECT_COURSE) {
                        val courseTypeFlagDrawable = ContextCompat.getDrawable(mContext, CourseTypeFlag.getCourseTypeFlag(courseTypeFlag))
                        courseTypeFlagDrawable?.setBounds(0, 0, TypeValue.dp2px(32F), TypeValue.dp2px(20F))
                        val imageSpan2 = CenterAlignImageSpan(courseTypeFlagDrawable)
                        spannableString.setSpan(imageSpan2, 4, 6, ImageSpan.ALIGN_BASELINE)

                        val subjectDrawable = ContextCompat.getDrawable(mContext, SubjectId.getSubjectLabel(subjectId))
                        subjectDrawable?.setBounds(0, 0, TypeValue.dp2px(20F), TypeValue.dp2px(20F))
                        val imageSpan1 = CenterAlignImageSpan(subjectDrawable)
                        spannableString.setSpan(imageSpan1, 8, 10, ImageSpan.ALIGN_BASELINE)
                    } else {
                        val subjectDrawable = ContextCompat.getDrawable(mContext, SubjectId.getSubjectLabel(subjectId))
                        subjectDrawable?.setBounds(0, 0, TypeValue.dp2px(20F), TypeValue.dp2px(20F))
                        val imageSpan1 = CenterAlignImageSpan(subjectDrawable)
                        spannableString.setSpan(imageSpan1, 4, 6, ImageSpan.ALIGN_BASELINE)
                    }
                    setText(R.id.tvClassName, spannableString)
                }
                if (showClassTime == 0) {
                    setText(R.id.tvDate, "暂未开课")
                } else {
                    val beginDataTimeString = DateUtil.longToString(beginTime ?: 0, "MM月dd日")
                    val endDataTimeString = DateUtil.longToString(endTime ?: 0, "MM月dd日")
                    setText(R.id.tvDate, "$beginDataTimeString-$endDataTimeString")
                }

                setImageResource(R.id.ivClassStatus, getClassStatusLabel(status))

                if (classTeachers.isNullOrEmpty()) {
                    setVisible(R.id.layout_teacher1, false)
                    setVisible(R.id.layout_teacher2, false)
                } else {
                    setVisible(R.id.layout_teacher1, true)
                    setText(R.id.tvTeacherName1, classTeachers?.get(0)?.userName)
                    ImageLoader.setCircleImageViewResource(ivTeacherHead1, classTeachers?.get(0)?.path
                            ?: "", R.drawable.icon_default_teacher_header)
                    setText(R.id.tvTeacherType1, classTeachers?.get(0)?.typeName)
                    if (classTeachers?.size ?: 0 > 1) {
                        setVisible(R.id.layout_teacher2, true)
                        setText(R.id.tvTeacherName2, classTeachers?.get(1)?.userName)
                        ImageLoader.setCircleImageViewResource(ivTeacherHead2, classTeachers?.get(1)?.path
                                ?: "", R.drawable.icon_default_teacher_header)
                        setText(R.id.tvTeacherType2, classTeachers?.get(1)?.typeName)
                    } else {
                        setVisible(R.id.layout_teacher2, false)
                    }
                }
            }
        }
    }

    private fun getClassStatusLabel(status: Int?): Int {
        return when (status) {
            1 -> R.drawable.icon_unstart_class_label
            2 -> R.drawable.icon_start_class_label
            3 -> R.drawable.icon_closed_class_label
            4 -> R.drawable.icon_leave_class_label
            5 -> R.drawable.icon_refunding_label
            else -> R.drawable.icon_unstart_class_label
        }
    }
}