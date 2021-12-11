package gaosi.com.learn.bean.classlesson.lessondetail

import java.io.Serializable

/**
 * 项目名称：app-student-android
 * 类描述： 课前预习
 * 创建人：weiyimeng
 * 创建时间：2019/5/7 2:45 PM
 * 修改人：weiyimeng
 * 修改时间：2019/5/7 2:45 PM
 * 修改备注：
 * @version
 */
class EnglishPreview : Serializable {
    var displayName: String = ""
    var resourceUrl: String = "" //类型：String  必有字段  备注：资源地址
    var resourceType: Int = 1 //类型：Number  必有字段  备注：资源类型（0-html，1-mp4）
    var operationalStatus: Int = 0  //类型：Number  必有字段  备注：可操作状态（未开放-0，去预习-20，允许-100）
    var processStatus: Int = 10      //
    var cardType: Int = 1 //类型：Number  必有字段  备注：卡片类型（专题课-5，英语预习-6）
    var goldCoinsNum: Int? = null // 金币数
    var preview: Int? = null // 类型：Number 1：旧 2：单词 3：都有，需要弹框

}
