package gaosi.com.learn.bean.classlesson.lessondetail

import java.io.Serializable

/**
 * 双师在线
 * Created by huangshan on 2020/02/18.
 */
class DoubleTeacherLive : Serializable {

    var speakerName: String = "" //主讲姓名
    var speakerAvatar: String? = "" //主讲头像
    var assistant1Name: String? = "" //助教1姓名
    var assistant1Avatar: String? = "" //助教1头像
    var assistant2Name: String? = "" //助教2姓名
    var assistant2Avatar: String? = "" //助教2头像


    var lessonBeginTime: Long? = null //课程开始时间
    var lessonEndTime: Long? = null  //课程结束时间
    var replayUrl: String? = "" //直播回放地址
    var lessonImg: String? = "" //课程图片
    var elapseTime: Long? = 0 //即将开课状态时，剩余开课时间（毫秒）
    var lessonStatus: Int? = 1 //0-未开放，1-未开课，2-马上开课，3-正在直播，4-直播完成回放生成中，5-回放已生成
    var aiLesson: Int? = 0 //0:不是ai课 1：是ai课
}
