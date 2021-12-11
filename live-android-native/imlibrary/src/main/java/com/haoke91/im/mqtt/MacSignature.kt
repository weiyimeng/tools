package com.haoke91.im.mymqttdemo

import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * 项目名称：workspace_mygitee
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/10/19 17:10
 * @version
 */
class MacSignature {
    companion object {
        /**
         * @param text      要签名的文本
         * @param secretKey 阿里云 MQ SecretKey
         * @return 加密后的字符串
         * @throws InvalidKeyException
         * @throws NoSuchAlgorithmException
         */
        @Throws(InvalidKeyException::class, NoSuchAlgorithmException::class)
        public fun macSignature(text: String, secretKey: String): String {
            val charset = Charset.forName("UTF-8")
            val algorithm = "HmacSHA1"
            val mac = Mac.getInstance(algorithm)
            mac.init(SecretKeySpec(secretKey.toByteArray(charset), algorithm))
            val bytes = mac.doFinal(text.toByteArray(charset))
            android.util.Base64.encode(bytes, android.util.Base64.DEFAULT).toString()
            org.apache.commons.codec.binary.Base64.encodeBase64(bytes)
            return String(org.apache.commons.codec.binary.Base64.encodeBase64(bytes), charset)
            //            return ""
        }
        
        /**
         * 发送方签名方法
         *
         * @param clientId  MQTT ClientID
         * @param secretKey 阿里云 MQ SecretKey
         * @return 加密后的字符串
         * @throws NoSuchAlgorithmException
         * @throws InvalidKeyException
         */
        @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
        fun publishSignature(clientId: String, secretKey: String): String {
            return macSignature(clientId, secretKey)
        }
        
        /**
         * 订阅方签名方法
         *
         * @param topics    要订阅的 Topic 集合
         * @param clientId  MQTT ClientID
         * @param secretKey 阿里云 MQ SecretKey
         * @return 加密后的字符串
         * @throws NoSuchAlgorithmException
         * @throws InvalidKeyException
         */
        @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
        fun subSignature(topics: List<String>, clientId: String, secretKey: String): String {
            Collections.sort(topics) //以字典顺序排序
            var topicText = ""
            for (topic in topics) {
                topicText += topic + "\n"
            }
            val text = topicText + clientId
            return macSignature(text, secretKey)
        }
        
        /**
         * 订阅方签名方法
         *
         * @param topic     要订阅的 Topic
         * @param clientId  MQTT ClientID
         * @param secretKey 阿里云 MQ SecretKey
         * @return 加密后的字符串
         * @throws NoSuchAlgorithmException
         * @throws InvalidKeyException
         */
        @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
        fun subSignature(topic: String, clientId: String, secretKey: String): String {
            val topics = ArrayList<String>()
            topics.add(topic)
            return subSignature(topics, clientId, secretKey)
        }
        
    }
}
