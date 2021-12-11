package gaosi.com.learn.bean.classlesson.lessondetail

import java.io.Serializable

class ForeignCourse : Serializable {

    var displayName: String? = ""//显示名称
    var resourceUrl: String? = ""//资源
    var resourceType: Int? = 0//资源类型（0-html，1-mp4）
    var processStatus: Int? = 0//作业状态（未预约-30，去预习-40，去上课-50， 已完成-100）
    var operationalStatus: Int? = 0//可操作状态（未开放-0，已退班-20，超过有效期-30，有权限-100）
    var cardType: Int? = 0//卡片类型（专题课-5，英语预习-6, 外教课-7）
    var classTime: String? = ""//外教课上课时间
    var lessonStartTime: String? = ""//上课时间字段
    var fbClassId: Int? = 0//飞博班级Id
    var goldCoinsNum: Int? = 0 //金币数量
    var previewStatus: Int? = 0 //0-未预习 1-已预习

}