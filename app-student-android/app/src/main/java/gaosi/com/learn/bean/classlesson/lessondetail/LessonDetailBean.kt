package gaosi.com.learn.bean.classlesson.lessondetail

import java.io.Serializable

/**
 * 讲次详情
 */
class LessonDetailBean : Serializable {
    
    var classId: Long? = 0
    var lessonId: Long? = 0
    var classTypeId: Int? = 0 //班型ID
    var num: Int? = 0
    var lessonName: String? = ""
    var haveEnglishHomework: Int? = 0
    var havaClassReport: Int? = 0
    var status: Int? = 0 // 0.未开课，1.开课, 2.结课未超过7天，3.已退班未超过7天，4.结课超过7天，5.退班超过7天
    var subjectId: Int? = 0 // 2 数学 3 语文 4 英语 5 物理 6化学
    var lessonHomeworkInfo: LessonHomeworkInfoBean? = null
    var englishSpeech: LessonEnglishSpeechBean? = null
    var englishRecite: LessonEnglishReciteBean? = null
    var dtClassReport: LessonClassReportBean? = null
    var englishPreview: EnglishPreview? = null
    var specialCourse: SpecialCourse? = null
    var foreignCourse: ForeignCourse? = null
    var axxOnline: AxxOnLine? = null
    var dtLive: DoubleTeacherLive? = null
    var axxOffline: AxxOffLine? = null
    var evaluation: TestReport? = null
    var courseType: Int? = 0 //类型：Number  必有字段  备注：课程类型（五件套-0，三阶课-1）
    var isActivation: Int? = 0 // 是否激活（三阶课新增字段）0 否 1是 3已过期
    var havaForeignCourse: Int? = 0//是否有外教课 0没有 1有
    var haveOnlineTeachingMaterial: Int? = null //是否有电子教材 0没有 1有
    var isJrClass: Int? = 0 //是否武汉巨人班级：0，不是；1，是
    var mainClassId: Int? = 0 //大班ID
    var beginTime: Long? = null //课程开始时间 讲次级别
    var courseTypeId: Int? = null //课程类型ID 2-在线课，3-双师课，4-AI好课，6-线下课
    var courseTypeName: String? = null //课程类型名称
    var showClassTime: Int? = null // 0 显示暂未开课 1 不显示

}
