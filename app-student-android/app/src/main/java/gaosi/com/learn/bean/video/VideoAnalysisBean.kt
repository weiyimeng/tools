package gaosi.com.learn.bean.video

import java.io.Serializable

/**
 * 查询视频、题目解析bean
 * Created by huangshan on 2019/4/29.
 */
class VideoAnalysisBean : Serializable {
    val videoName: String? = ""//视频名称
    val videoUrl: String? = ""//视频id
    val content: String? = ""//资源备注
    val topicType: Int? = 0//题目类型 1.选择 2.填空 3.判断 4.简答 5.多小题 6.完形填空 7.阅读理解
    val contentType: Int? = 0//0题目解析 1视频
    var topicParsingContentList: List<TopicParsingContentBean>? = null//题目解析
}

class TopicParsingContentBean : Serializable {
    var topicContentId: Int? = 0//内容ID
    var index: Int? = 0//序号
    var analysisList: List<AnalysisBean>? = null
    var answers: List<AnswerBean>? = null
}

class AnalysisBean : Serializable {
    var analysisText: String? = ""//解析内容text
    var analysisHtml: String? = ""//解析内容html
}

class AnswerBean : Serializable {
    var option: String? = ""//选项
    var contentText: String? = ""//答案内容text
    var contentHtml: String? = ""//答案内容html
}