package gaosi.com.learn.bean.classlesson.lessondetail

import java.io.Serializable

/**
 * 讲次详情 英语口语信息
 */
class LessonEnglishSpeechBean : Serializable {
    
    var englishSpeechStar: Int? = 0 //英语语音星
    var englishQuestionsCount: Int? = 0 //英语题目总数
    var flag: Int? = 0 // 1 有更新 0 无更新
    var operationalStatus: Int = -1  //类型：Number  必有字段  备注：可操作状态（未开放-0，去预习-10，已完成-100）
    var processStatus: Int = -1
    var goldCoinsNum: Int? = 0 //金币前置金币数量
    
}
