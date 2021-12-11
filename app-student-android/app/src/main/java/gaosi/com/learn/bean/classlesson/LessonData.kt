package gaosi.com.learn.bean.classlesson

import java.io.Serializable

/**
 * 讲次信息
 */
class LessonData: Serializable {

    var curLessonId: String? = null //当前lessonId
    var haveTreasureBox: Int? = 0 //是否有宝箱 0 没有 1有
    var wheatherJitter: Boolean? = false //是否抖动 true 抖动 false 不抖动
    var haveHomeWorkSize: Int? = 0 //讲次数
    var haveDoubleReport: Int? = 0 //是否存在双师报告
    var lessonList: List<LessonBean>? = null
    var className: String? = null // 班级名称
    var beginTime: Long? = null //班级开课时间
    var endTime: Long? = null //班级结课时间
    var subjectId: Int? = null // 学科ID
    var subjectName: String? = null //学科名称
    var courseTypeId: Int? = null //课程类型ID 2-在线课，3-双师课，4-AI好课，6-线下课
    var courseTypeName: String? = null //课程类型名称
    var showClassTime: Int? = null // 0 显示暂未开课 1 显示开课时间

}