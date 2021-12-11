package gaosi.com.learn.studentapp.classlesson.status

import gaosi.com.learn.R

/**
 * 课程ID 2-在线课，3-双师课，4-AI好课，6-线下课
 */
object CourseTypeId {

    const val TOL_ONLINE = 1 //TOL在线大班
    const val SMALL_CLASS_ONLINE = 2 //在线课
    const val DT_LIVE = 3 //双师课
    const val AI_DT_LIVE = 4 //AI好课
    const val FOREIGN_CLASS = 5 //在线外教
    const val OFFLINE_CLASS = 6 //线下课

    /**
     * 获取课程类型标签
     */
    public fun getCourseTypeLabel(courseTypeId: Int?): Int {
        return when (courseTypeId) {
            TOL_ONLINE ->  R.drawable.icon_online_class_label
            SMALL_CLASS_ONLINE -> R.drawable.icon_online_class_label
            DT_LIVE -> R.drawable.icon_dt_class_label
            AI_DT_LIVE -> R.drawable.icon_ai_class_label
            OFFLINE_CLASS -> R.drawable.icon_offline_class_label
            else -> R.drawable.icon_offline_class_label
        }
    }
}