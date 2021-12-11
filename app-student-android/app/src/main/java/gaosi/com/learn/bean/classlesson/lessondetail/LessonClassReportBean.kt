package gaosi.com.learn.bean.classlesson.lessondetail

import java.io.Serializable
import kotlin.math.roundToInt

/**
 * 双师报告信息
 */
class LessonClassReportBean: Serializable {

    var videoUrl: String? = "" //视频地址
    var videoName: String? = "" //视频名称
    var videoType: String? = "" //视频类型
    var videoSize: Long? = 0 //视频类型
    var open: Boolean? = false //是否上架
    var status: Int? = 0 //状态
    var statusName: String? = "" //状态名称
    var isExpire: Boolean? = false //是否过期
    var ratio: Double? = 0.0 //14.6% 学生某讲正确率
    var haveReport: Boolean? = false //是否生成双师报告
    var reportName: String? = "" //报告名称
    var currentStatus: String? = "" //状态
    var updateTime: String? = "" //更新时间
    var haveStudyReport: Boolean? = false //是否存在学情报告
    var haveBuyLesson: Int? = 0 //未购买0 购买1 已退课 2
    
    fun getRatio(): Int {
        return ratio?.roundToInt()!!
    }

}