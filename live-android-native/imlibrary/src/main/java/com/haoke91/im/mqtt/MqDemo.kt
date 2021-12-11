package com.haoke91.im.mqtt

import android.os.Build
import java.util.*

/**
 * 项目名称：IMSDK_android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/10/29 15:33
 */
class MqDemo {
    
    public fun test() {
        var mImManager = IMManager.instance
        var config = IMManager.Config()
        config.broker = "tcp://post-cn-4590twjcw07.mqtt.aliyuncs.com:1883"
        config.accessKey = "LTAIqzljvPL7tP39"
        config.secretKey = "iNo9ACtu7Sul33GU6ijJslxqq9AST2"
        config.topic = "gsl_mq_test_2018-10-17"
        config.groupId = "GID_gsl_mq_tester_001"
//        config.clientId = "${config.groupId}@@@${getOnlyId()}"
//        mImManager.initialize(config,callback = object :Callback)
    }
    
    /**
     * 获取设备唯一id号
     */
    private fun getOnlyId(): String {
        var serial: String? = null
        
        var m_szDevIDShort = "35" +
                Build.BOARD.length % 10 + Build.BRAND.length % 10 +
                
                Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +
                
                Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
                
                Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +
                
                Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +
                
                Build.TAGS.length % 10 + Build.TYPE.length % 10 +
                
                Build.USER.length % 10 //13 位
        
        try {
            serial = android.os.Build::class.java.getField("SERIAL").get(null).toString()
            //API>=9 使用serial号
            return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (e: Exception) {
            //SERIAL需要一个初始化
            serial = "serial" // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return UUID(m_szDevIDShort.hashCode().toLong(), serial!!.hashCode().toLong()).toString()
    }
}
