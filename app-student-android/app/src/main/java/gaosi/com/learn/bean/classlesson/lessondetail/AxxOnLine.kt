package gaosi.com.learn.bean.classlesson.lessondetail

import java.io.Serializable

/**
 * 爱学习在线
 */
class AxxOnLine : Serializable {

    var speakerName: String = "" //主讲姓名
    var speakerAvatar: String? = "" //主讲头像
    var assistant1Name: String? = "" //助教1姓名
    var assistant1Avatar: String? = "" //助教1头像
    var assistant2Name: String? = "" //助教2姓名
    var assistant2Avatar: String? = "" //助教2头像


    var lessonBeginTime: Long? = 0 //课程开始时间
    var lessonEndTime: Long? = 0  //课程结束时间
    var replayUrl: String? = "" //直播回放地址
    var lessonImg: String? = "" //课程图片
    var elapseTime: Long? = 0 //即将开课状态时，剩余开课时间（毫秒）
    var lessonStatus: Int? = 1 //Number  必有字段  备注：直播课状态0-未开放，1-未开课，2-马上开课，3-正在直播，4-直播完成回放生成中，5-回放已生成
    var teacherInClass: Boolean? = true //老师是否来上课 true来了 false没来
    var liveBaseUrl: String? = "" //爱学习在线直播sdk地址
}
