package com.haoke91.im.mqtt

import java.util.*

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/12/17 14:32
 */
class MessageTrack {
    private constructor()
    
    private var stringTrack = StringBuffer()
    
    companion object {
        private var mTrack: MessageTrack? = null
        fun getInstance(): MessageTrack {
            if (mTrack == null) {
                synchronized(MessageTrack::class.java) {
                    mTrack = MessageTrack()
                }
            }
            return mTrack!!
        }
    }
    
    /**
     * 加入
     */
    fun putMessageId(messageId: String?) {
        if (messageId.isNullOrEmpty()) {
            return
        }
        var id = ",$messageId"
        stringTrack.append(id)
    }
    
    /**
     * 移除消息记录
     */
    fun removeMessageId(messageId: String) {
        if (messageId.isNullOrEmpty()) {
            return
        }
        var id = ",$messageId"
        var startIndex = stringTrack.indexOf(id)
        if (startIndex > -1) {
            stringTrack.delete(startIndex, startIndex + id.length)
        }
    }
    
    fun clear() {
        stringTrack.setLength(0)
    }
    
    /**
     * 是否包含
     */
    fun contains(id: String?): Boolean {
        if (id.isNullOrEmpty()) {
            return false
        }
        var msgId = ",$id"
        return if (stringTrack.contains(msgId!!)) {
            stringTrack.removePrefix(msgId!!)
            true
        } else {
            false
        }
    }
}
