package com.haoke91.im.mqtt.entities

/**
 * 项目名称：IMSDK_android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/11/20 16:11
 */
class User {
    var id: String = ""
    var _id: String? = null //不知道啥区别
    var role: String = "student"
    var time: String? = null
    var sysTime: Long = 0
    var loginStatus: String? = null
    var userId: String = ""
    var subgroupIds: List<String>? = null
    var prop: Prop? = null
}
