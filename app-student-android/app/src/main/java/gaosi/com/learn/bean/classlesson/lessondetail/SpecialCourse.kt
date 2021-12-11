package gaosi.com.learn.bean.classlesson.lessondetail

import java.io.Serializable

/**
 * 项目名称：app-student-android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2019/5/7 2:52 PM
 * 修改人：weiyimeng
 * 修改时间：2019/5/7 2:52 PM
 * 修改备注：
 * @version
 */
class SpecialCourse : Serializable {
    var displayName: String = ""
    var resourceUrl: String = "" //类型：String  必有字段  备注：资源地址
    var resourceType: Int = 1 //类型：Number  必有字段  备注：资源类型（0-html，1-mp4）
    var operationalStatus: Int = 0  //可操作状态（未开放-0，已退班-20，超过有效期-30，有权限-100）
    var processStatus:Int=10 //作业状态（未开放-10， 未完成-20，已完成-100）
    var hasNewVersion: Int = 0//资源是否有更新 0 没有更新 1 有更新
    var cardType: Int = 1 //类型：Number  必有字段  备注：卡片类型（专题课-5，英语预习-6）
    var goldCoinsNum: Int? = null // 金币数
}
