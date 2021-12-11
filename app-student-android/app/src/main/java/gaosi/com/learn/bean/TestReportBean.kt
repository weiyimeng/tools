package gaosi.com.learn.bean

import java.io.Serializable

/**
 * description:
 * created by huangshan on 2020-04-10 12:07
 */
data class TestReportGradeBean(
     var gradeId: Int? = 0, //年级id
     var gradeName: String? = "", //年级名称
     var type: Int? = 0, //1-小学，2-初中，3-高中
     var itemType: Int = 1,
     var isClicked: Boolean = false //是否被点击
) : Serializable

data class TestReportClassBean(
     var gradeId: Int? = 0, //年级ID
     var classId: Int? = 0, //班级ID
     var className: String? = "", //班级名称
     var subjectId: Int? = 0, //学科id
     var subjectName: String? = "", //学科名称
     var periodId: Int? = 0, //学期id
     var periodName: String? = "", //学期名称
     var teacherId: Int? = 0, //教师id
     var teacherName: String? = "", //教师姓名
     var teacherAvatarUrl: String? = "", //教师头像链接
     var reports: ArrayList<TestReportDetailBean>? = ArrayList()
): Serializable

data class TestReportDetailBean(
     var reportId: Int? = 0, //报告id
     var reportUrl: String? = "", //报告url
     var reportType: Int? = 1, //类型（1-期中评测报告，2-期末评测报告）
     var viewed: Int? = 0 //1-查看过，0-未查看过
): Serializable