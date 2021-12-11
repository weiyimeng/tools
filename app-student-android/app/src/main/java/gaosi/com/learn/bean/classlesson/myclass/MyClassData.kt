package gaosi.com.learn.bean.classlesson.myclass

import java.io.Serializable

/**
 * description:
 * created by huangshan on 2020/6/16 下午7:41
 */
class MyClassData : Serializable {
    var classList: ArrayList<MyClassInfoBean>? = null
    var nextPage: Int? = 0 //是否有下一页 0没有 1有
}

class MyClassInfoBean : Serializable {
    var id: Int? = 0 //班级Id
    var gsId: Long? = 0 // gsId 在线班级存在
    var subjectId: Int? = 0 //学科ID
    var status: Int? = 0 //课程状态 1 未开课 2 开课中 3 已结课 4 已退班 5 退费中
    var className: String? = "" //班级名称
    var period: Int? = 0 //学期
    var classTypeId: Int? = 0 //班型Id
    var classTypeName: String? = "" //班型名称
    var beginTime: Long? = 0 //开课时间
    var endTime: Long? = 0 //结课时间
    var courseName: String? = ""
    var courseType: Int? = 0 //课程类型 1 在线课TOL 2在线小班 3 双师课 4 AI好课 5在线外教课 6线下课
    var classTeachers: ArrayList<ClassTeacherBean>? = null
    var showClassTime: Int? = 0 // 0 显示暂未开课 1 显示开课时间
    val lecturePackageId: Long? = 0//场次包id
    val lectureId: Long? = 0//场次id
    val lectureType: Int? = 0
    val courseTypeFlag: Int? = 0//1 大班课；2 精品课；3 专题课
}

class ClassTeacherBean : Serializable {
    var path: String? = ""
    var userName: String? = ""
    var userId: Int? = 0
    var type: Int? = 0 // 老师类型 1 主讲 2 辅导
    var typeName: String = "" //老师类型名称
}