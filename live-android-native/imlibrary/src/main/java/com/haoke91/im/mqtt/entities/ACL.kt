package com.haoke91.im.mqtt.entities

/**
 * 项目名称：IMSDK_android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/11/12 14:47
 */
class ACL {
    
    /**
     * commonMessageAcl : {"text":{"interval":5,"sercurity":false,"status":true}}
     * customMessageAcl : {"like":{"interval":-1,"sercurity":false,"status":true},"gift":{"interval":-1,"sercurity":false,"status":true},"answer":{"interval":-1,"status":true},"comment":{"noBroadcast":true,"status":true},"flower":{"interval":60,"sercurity":false,"status":true}}
     *
     *
     * interval//间隔可发送时间，-1 代表无限制：
     * sercurity :检验文本
     * status: 状态，是否正常
     */
    
    var commonMessageAcl: CommonMessageAclBean? = null
    var customMessageAcl: CustomMessageAclBean? = null
    
    class CommonMessageAclBean {
        /**
         * text : {"interval":5,"sercurity":false,"status":true}
         */
        
        var text: AclBean? = null
    }
    
    class CustomMessageAclBean {
        var like: AclBean? = null
        var gift: AclBean? = null
        var answer: AclBean? = null
        var comment: AclBean? = null
        var flower: AclBean? = null
    }
    
    inner class AclBean {
        var interval: Long = -1
        var isSercurity: Boolean = false
        var isStatus: Boolean = false
    }
}
