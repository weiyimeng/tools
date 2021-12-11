package gaosi.com.learn.bean.main

import java.io.Serializable

/**
 * description:
 * created by huangshan on 2020/6/5 下午2:28
 */
class StudyTaskData : Serializable {
    var studentId: Int? = 0 //学生Id
    var theStartTime: Long? = 0 //开始时间
    var theEndOfTime: Long? = 0 //结束时间
    var taskEveryDay: ArrayList<TaskEveryDayBean>? = null //每日任务列表
}

//每日任务列表
class TaskEveryDayBean: Serializable {
    var taskYear: Int? = 0 //哪年
    var taskMonth: Int? = 0 //哪月
    var taskDay: Int? = 0 //哪天
    var taskDateStr: String? = "" //当前日期字符串
    var taskDate: Long? = 0 //当前日期
    var itIsToday: Int? = 0 //是否是今天 0 不是 1是
    var todayTaskStatus: Int? = 0 //是否完成 0 未完成 1已完成
    var studyTaskInfoList: ArrayList<StudyTaskInfoBean>? = null //任务列表
}

class StudyTaskInfoBean: Serializable {
    var classId: Int? = 0 //班级id
    var lessonId: Int? = 0 //讲次id
    var className: String? = "" //班级名称
    var lessonName: String? = "" //讲次名称
    var type: Int? = 0 //任务类型 1 在线课TOL 2在线小班 3 双师课 4 AI好课 5在线外教课 6线下课
    var lessonNum: Int? = 0 //讲次序号
    var subjectId: Int? = 0 //学科Id
    var subjectName: String? = "" //学科名称
    var classTypeId: Int? = 0 //班型Id
    var beginTime: Long? = 0 //开课时间
    var endTime: Long? = 0 //下课时间
    var isJrClass: Int? = 0 //是否是武汉巨人 0 不是 1 是
    var mainClassId: Int? = 0 //大班id
    var courseType: Int? = 0 //课程类型 0 五件套 1三阶课
    var axxOnlineTol: ClassInfo? = null //在线TOL
    var axxOnline: ClassInfo? = null //在线小班
    var axxOffline: ClassInfo? = null //线下课
    var foreignCourse: ClassInfo? = null //外教课
    var dtLive: ClassInfo? = null //双师课
}

class ClassInfo: Serializable {
    var lessonImg: String? = "" //课程图片
    var speakerName: String? = "" //主讲姓名
    var speakerAvatar: String? = "" //主讲头像
    var lessonBeginTime: Long? = 0 //课程开始时间
    var lessonEndTime: Long?  = 0 //课程结束时间
    var elapseTime: Long? = 0 //即将开课状态时，剩余开课时间（毫秒）
    var replayUrl: String? = "" //直播回放地址
    var lessonStatus: Int? = 0 //直播课状态 0-未开放，1-未开课，2-马上开课，3-正在直播，4-直播完成回放生成中，5-回放已生成
    var teacherInClass: Boolean? = false //老师是否来上课
    var liveBaseUrl: String? = "" //爱学习在线直播sdk地址
    var aiLesson: Int? = 0 //0直播 1：ai

    var courseId: Int? = 0 //课程ID TOL
    var gaoSiId: Long? = 0 //gsId TOL
    var diyLessonId: Int? = 0 //diyLessonId TOL

    var fbClassId: Int? = 0 //飞博班级Id
    var classTime: String? = "" //上课时间
    var lessonStartTime: String? = "" //课程开始时间
    var privacyShow: Boolean? = true //是否显示隐私协议弹窗
    var liveRoomType: Int? = 0
    var lecturePackageId: Long? = 0 //场次包ID
    var lectureId: Long? = 0 //场次ID
    var lectureType: Int? = 0 //场次类型
    var courseTypeFlag: Int? = 0 //1大班课 2精品课 3专题课
}