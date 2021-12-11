package gaosi.com.learn.studentapp.classlesson.status

import gaosi.com.learn.R

object CourseTypeFlag {
    const val BIG_COURSE = 1 //大班课
    const val GOOD_COURSE = 2 //精品课
    const val SPECIAL_SUBJECT_COURSE = 3 //专题课

    fun getCourseTypeFlag(courseTypeId: Int?): Int {
        return when (courseTypeId) {
//            CourseTypeFlag.BIG_COURSE -> R.drawable.icon_course_good
            GOOD_COURSE -> R.drawable.icon_course_good
            SPECIAL_SUBJECT_COURSE -> R.drawable.icon_course_special
            else -> R.drawable.icon_course_good
        }
    }

}