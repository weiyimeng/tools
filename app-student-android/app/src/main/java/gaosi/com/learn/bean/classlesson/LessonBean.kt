package gaosi.com.learn.bean.classlesson

import java.io.Serializable

/**
 * 班级讲次信息
 */
class LessonBean : Serializable {
    var classId: String? = ""//班级Id
    var lessonId: String? = "" //讲次Id
    var num: Int? = 0 //序号
    var lessonname: String? = "" //讲次名称
    var lessonName: String? = "" //讲次名称
    var hasHomework: Int? = 0 // 是否有课后作业 0：没有 1：有
    var haveHomework: Int? = 0
    var hasEnglishSpeaking: Int? = 0 // 是否有英语口语作业 0：没有 1：有
    var haveEnglishReciteHomework: Int? = 0 //是否有AI英语口语背诵作业 0：没有 1：有
    var haveEnglishHomework: Int? = 0
    var hasPlayBack: Int? = 0 //是否有课堂回放 0：没有 1：有
    var hasReport: Int? = 0 //是否有学情报告或双师报告 0：没有 1：有
    var hasPrepareEnglish : Int? = 0   //首页是否有课前预习字段，为毛后端字段乱定义！！！
    var haveSpecialLesson : Int? = 0
    var havePrepareEnglish : Int? = 0
    var hasSpecialLesson : Int? = 0
    var havaClassPlay : Int? = 0
    var havaClassReport	: Int? = 0
    var hasForeignCourse: Int? = 0//是否存在1v1外教 0 没有 1： 有
    var havaForeignCourse: Int? = 0//是否存在1v1外教 0 没有 1： 有
    var haveAxxOnline: Int? = 0 //是否存在在线直播 0 没有 1： 有
    var hasAxxOnline: Int? = 0 //是否存在在线直播 0 没有 1： 有
    var haveDtLive: Int? = 0 //是否为双师在线课 0 没有 1 有
    var haveExam: Int? = 0 //是否有期中期末测评报告 0 没有 1 有
    var onlineTeacherUrl: String? = "" // 老师头像
    var onlineTeacherName: String? = "" // 老师名称
    var onlineStatus: String? = "" // 课程状态 0 即将开课 1 已开课
}