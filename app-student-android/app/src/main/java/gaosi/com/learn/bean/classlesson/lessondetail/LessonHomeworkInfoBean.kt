package gaosi.com.learn.bean.classlesson.lessondetail

import java.io.Serializable

/**
 * 讲次详情课后作业信息
 */
class LessonHomeworkInfoBean: Serializable {
    var stars: Int? = 0 //星星数量
    var correctionSubjectiveStar: Int? = 0 //订正主观题星
    var correctionObjectiveStar: Int? = 0 //订正客观题星
    var submitStar: Int? = 0 //提交题星
    var questionsCount: Int? = 0 //题目总数
    var haveAnswerCount: Int? = 0 //已答题目
    var myselfCorrect: Int? = 0 //是否已经自批改 0:未自批改1：已经自批改
    var flag: Int? = 0 //0不需要删除。 1需要删除重交 2已重判 3已重交
    var topicType: String? = "" //题目类型
    var popupWindow: Int? = 0 //pop
    var operationalStatus: Int = -1  //类型：Number  必有字段  备注：可操作状态（未开放-0，去预习-10，已完成-100）
    var processStatus: Int = -1
    var aiHomeworkCorrection: Int? = null // 0 不是智能批改白名单 1 是智能批改白名单
    var submitSource: Int? = null //0 正常提交 1 智能批改提交
    var goldCoinsNum: Int? = null // 金币数
}
