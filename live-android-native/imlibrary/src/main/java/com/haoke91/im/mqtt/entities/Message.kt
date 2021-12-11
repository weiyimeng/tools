package com.haoke91.im.mqtt.entities

import android.webkit.MimeTypeMap
import com.google.gson.JsonObject
import com.haoke91.im.mqtt.IMManager
import com.haoke91.im.mqtt.entities.Constant.*
import org.json.JSONObject
import java.util.*

/**
 * 项目名称：IMSDK_android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/10/29 16:11
 */
class Message {
    private constructor()
    constructor(type: Constant.MessageType, flag: Constant.MessageFlag, role: Constant.Role, fromId: String, to: String, fromUser: User) : this() {
        this.type = type.value
        this.flag = flag.value
        this.role = role.value
        this.from = fromId
        this.to = to
        this.fromUser = fromUser
        this.roomId = IMManager.instance.mConfig.roomId?:""
        this.knowledgeId = IMManager.instance.mConfig.knowledgeId?:""
        this.time = IMManager.instance.getCurrentTime()
        this.msgId = UUID.randomUUID().toString()
    }
    
    var msgId: String? = null
    var sourceId = "91haoke_android"
    var roomId = ""
    var knowledgeId = ""
    var username: String? = null
    var type: String? = null
    var flag: String? = null
    var content: Any? = null
    var role: String? = null
    var from: String? = null
    var to: String? = null
    var time: Long = 0
    var sysTime: Long = 0
    var status: String? = null
    var fromUser: User? = null
    
    companion object {
        fun createTextMessage(content: String): Message {
            var msg = Message()
            msg.type = MessageType.COMMON.value
            msg.flag = MessageFlag.TEXT.value
            msg.from = IMManager.instance.getSessionUser().userId
            msg.fromUser = IMManager.instance.getSessionUser()
            msg.to = "ALL"
            msg.time = IMManager.instance.getCurrentTime()
            msg.role = Role.STUDENT.value
            msg.msgId = UUID.randomUUID().toString()
            msg.roomId = IMManager.instance.mConfig.roomId?:""
            msg.knowledgeId = IMManager.instance.mConfig.knowledgeId?:""
            var obj = JsonObject()
            obj.addProperty("text", content)
            msg.content = obj
            return msg
        }
    }
}
