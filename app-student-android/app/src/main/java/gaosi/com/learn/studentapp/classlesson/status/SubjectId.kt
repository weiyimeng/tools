package gaosi.com.learn.studentapp.classlesson.status

import gaosi.com.learn.R

/**
 * 学科ID
 */
object SubjectId {

    const val MATH = 2 //数学
    const val CHINESE = 3 //语文
    const val ENGLISH = 4 //英语
    const val PHYSICS = 5 //物理
    const val CHEMISTRY = 6 //化学
    const val BIOLOGY = 10 //生物
    const val HISTORY = 24 //历史
    const val GEOGRAPHY = 25 //地理
    const val POLITICS = 26 //政治

    /**
     * 获取学科标签
     */
    public fun getSubjectLabel(subjectId: Int?): Int {
        return when (subjectId) {
            MATH -> R.drawable.icon_math_label
            CHINESE -> R.drawable.icon_chinese_label
            ENGLISH -> R.drawable.icon_english_label
            PHYSICS -> R.drawable.icon_physics_label
            CHEMISTRY -> R.drawable.icon_chemistry_label
            BIOLOGY -> R.drawable.icon_biology_label
            HISTORY -> R.drawable.icon_history_label
            GEOGRAPHY -> R.drawable.icon_geography_label
            POLITICS -> R.drawable.icon_politics_label
            else -> R.drawable.icon_math_label
        }
    }
}