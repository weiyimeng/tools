package com.haoke91.im.mqtt

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.haoke91.im.mqtt.entities.*
import com.haoke91.im.mymqttdemo.MacSignature
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import com.haoke91.im.mqtt.entities.Constant.*
import com.haoke91.im.mqtt.entities.Constant.LoginStatus.*
import com.orhanobut.logger.Logger
import org.json.JSONObject

/**
 * 项目名称：IMSDK_android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/10/22 10:47
 *
 *
 
 *发送消息topic :parentTopic/room/subgroup/
 
 *发送消息topic : parentTopic +"/p2p/" + clientId
 
 *
 */
class IMManager {
    private val TAG = "im==="
    var mConfig: Config = Config()
    private var mqttClient: MqttClient? = null
    private var mCallback: IMCallBack? = null
    private var mGson: Gson? = null
    private var isOnline = false
    private var myUser: User? = null
    private var myAcl: ACL? = null
    // 时间控制
    private var commonMessageLastTime = 0L
    private var customMessageLastTime_like = 0L
    private var customMessageLastTime_flower = 0L
    private var systemTime = 0L
    //计数器
    private var timerUserCountData: TimerUserCountData = TimerUserCountData()
    //用户群组容器
    private lateinit var userContainer: HashMap<String, User>
    var isInBackground = false
    
    private constructor() {
        mGson = Gson()
        userContainer = hashMapOf()
    }
    
    public companion object {
        var isForbidden = false
        val instance = IMManager()
    }
    
    /**
     * 设置是否是后台操作
     */
    public fun isInBackground(isInBackground: Boolean) {
        this.isInBackground = isInBackground
    }
    
    /**
     * initialize
     */
    fun initialize(config: Config, callback: IMCallBack) {
        //        config.clientId = "${config.groupId}@@@${config.userId}"
        checkConfig(config)
        mCallback = callback
        var memoryPersistence = MemoryPersistence()
        try {
            mqttClient = MqttClient(config.broker, config.clientId, memoryPersistence)
            var connOpt = MqttConnectOptions()
            /**
             * 计算签名，将签名作为MQTT的password。
             * 签名的计算方法，参考工具类MacSignature，第一个参数是ClientID的前半部分，即GroupID
             * 第二个参数阿里云的SecretKey
             */
            var sign = MacSignature.macSignature(config.groupId!!, config.secretKey!!)
            connOpt.serverURIs = arrayOf(config.broker)
            connOpt.userName = config.accessKey
            connOpt.password = sign.toCharArray()
            connOpt.isCleanSession = true
            connOpt.keepAliveInterval = config.keepAliveInterval
            connOpt.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1
            connOpt.isAutomaticReconnect = config.isAutomaticReconnect
            connOpt.connectionTimeout = 10
            //            var service: ExecutorService = ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, LinkedBlockingDeque())
            mqttClient?.setCallback(object : MqttCallbackExtended {
                override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                    LogU.log("connectComplete 链接成功")
                    userContainer.clear() //清空用户容器
                    mCallback?.onConnected(0)
                    //                    service.execute {
                    var t1 = "${mConfig.topic}/${mConfig.roomId}/${mConfig.subGroup}/#"
                    //                        var t2 = "$topic/p2p/${mConfig?.clientId}/#"
                    //                        mqttClient.subscribe(arrayOf(t1, t2), intArrayOf(2, 2))
                    //                    }
                    var t2 = "${mConfig.topic}/${mConfig.roomId}/ALL/#"
                    mqttClient?.subscribe(t1, 2)
                    mqttClient?.subscribe(t2, 2)
                    isOnline = true
                }
                
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    var result = message?.payload?.toString(charset("UTF-8"))
                    LogU.log("接收到消息===\n$result")
                    var msg = Gson().fromJson(result, Message::class.java)
                    handleMessages(msg)
                }
                
                override fun connectionLost(cause: Throwable?) {
                    LogU.log("connectionLost" + cause.toString())
                    if (isOnline) {
                        isOnline = false
                        callback.onWarning(IMCallBack.WARNING_UNCONNECTED)
                    }
                }
                
                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    LogU.log("消息发送成功 deliveryComplete")
                }
                
                
            })
            mqttClient?.connect(connOpt)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
    
    class Config {
        /**
         * 设置当前用户私有的MQTT的接入点。例如此处示意使用XXX，实际使用请替换用户自己的接入点。接入点的获取方法是，在控制台申请MQTT实例，每个实例都会分配一个接入点域名。
         */
        public var broker: String? = null
        /**
         * 设置阿里云的AccessKey，用于鉴权
         */
        public var accessKey: String? = null
        /**
         * 设置阿里云的SecretKey，用于鉴权
         */
        public var secretKey: String? = null
        /**
         * 发消息使用的一级Topic，需要先在MQ控制台里申请
         */
        public var topic: String? = null
        /**
         * groupId
         */
        public var groupId: String? = null
        /**
         * MQTT的ClientID，一般由两部分组成，GroupID@@@DeviceID
         * 其中GroupID在MQ控制台里申请
         * DeviceID由应用方设置，可能是设备编号等，需要唯一，否则服务端拒绝重复的ClientID连接
         */
        public var clientId: String? = null
        /**
         * 自动链接 default=true
         */
        public var isAutomaticReconnect = false
        /**
         * 监控连接时效 每隔x s发送心跳包
         */
        public var keepAliveInterval = 90
        /**
         * room 房间
         */
        public var roomId: String? = null
        /**
         * 分组
         */
        public var subGroup = "ALL"
        public var userId: String? = null
        var knowledgeId: String? = null
    }
    
    private fun checkConfig(config: Config): Boolean {
        if (config.broker == null) {
            LogU.log("the broker of config is null")
            return false
        }
        if (config.accessKey == null) {
            Log.e(TAG, "the accessKey of config is null")
            return false
        }
        if (config.clientId == null) {
            Log.e(TAG, "the clientId of config is null")
            return false
        }
        if (config.secretKey == null) {
            Log.e(TAG, "the secretKey of config is null")
            return false
        }
        if (config.groupId == null) {
            Log.e(TAG, "the groupId of config is null")
            return false
        }
        if (config.topic == null) {
            Log.e(TAG, "the topic of config is null")
            return false
        }
        if (config.roomId == null) {
            LogU.log("the roomId of config is null or illegal")
            return false
        }
        if (config.userId == null) {
            Log.e(TAG, "the userId of config is null or Illegal")
            return false
        }
        mConfig = config
        return true
    }
    
    /**
     * 获取当前用户
     */
    public fun getSessionUser() = myUser ?: User()
    
    /**
     * login
     * @param user 当前用户信息
     */
    fun login(user: User, acl: ACL) {
        myUser = user
        myAcl = acl
        if (mqttClient?.isConnected == true) {
            var msg = Message(MessageType.CUSTOM, MessageFlag.ONUSERLOGIN, Role.STUDENT, user.userId, "ALL", user)
            var obj = JsonObject()
            obj.addProperty("userId", user.userId)
            obj.addProperty("loginStatus", LOGIN.value)
            obj.addProperty("time", getCurrentTime())
            msg.content = obj
            //            isForbidden
            //            if (myAcl?.commonMessageAcl?.text?.interval == 0L) {
            //                user.prop?.speaking = "off"
            //            } else {
            user.prop?.speaking = if (isForbidden) "off" else "on"
            //            }
            var t1 = "${mConfig.topic}/${mConfig.roomId}/${mConfig.subGroup}/"
            MessageTrack.getInstance().putMessageId(msg.msgId) //标记消息id，幂等处理
            mqttClient?.publish(t1, Gson().toJson(msg).toByteArray(), 2, true)
            isOnline = true
        }
    }
    
    /**
     * 退出
     */
    public fun logout() {
        if (mqttClient?.isConnected == true) {
            var msg = Message(MessageType.CUSTOM, MessageFlag.ONUSERLOGINOUT, Role.STUDENT, getSessionUser().userId, "all", getSessionUser())
            var obj = JsonObject()
            obj.addProperty("userId", msg.fromUser?.userId)
            obj.addProperty("loginStatus", LOGOUT.value)
            obj.addProperty("time", getCurrentTime())
            msg.content = obj
            var topic = "${mConfig.topic}/${mConfig.roomId}/${mConfig.subGroup}/"
            MessageTrack.getInstance().putMessageId(msg.msgId)
            mqttClient?.publish(topic, Gson().toJson(msg).toByteArray(), 2, false)
        }
        isOnline = false
        mqttClient?.disconnect() //应该在返回消息后关闭连接
    }
    
    /**
     * 重连
     */
    fun reconnect() {
        if (mqttClient?.isConnected == false)
            mqttClient?.reconnect()
    }
    
    fun isConnecting() = mqttClient != null && (mqttClient?.isConnected == true)
    
    fun getAcl() = myAcl ?: ACL()
    
    fun getCurrentTime(): Long {
        return systemTime + System.currentTimeMillis()
    }
    
    /**
     * 设置系统时间
     */
    public fun setCurrentTime(time: Long) {
        systemTime = time - System.currentTimeMillis()
    }
    
    /**
     * 用户计数
     */
    public fun getUserCountData() = {
        timerUserCountData = TimerUserCountData()
        if (userContainer != null) {
            timerUserCountData.tatalUserCount = userContainer.size
            var self = getSessionUser()
            for ((userid, user) in userContainer) {
                if (user.loginStatus == LOGIN.value) {
                    timerUserCountData.loginUserCount++
                }
                //                if (user.subgroupIds.contains(self.subgroupIds)) {
                //                    timerUserCountData.groupUserCount++
                //                    if (user.loginStatus == LOGIN.value)
                //                        timerUserCountData.groupLoginUserCount++
                //                }
            }
        } else {
            timerUserCountData
        }
    }
    
    /**
     * 拉取用户列表
     */
    public fun loadUserContainer() {
        if (mqttClient?.isConnected == true) {
            var msg = Message(MessageType.CUSTOM, MessageFlag.COMMENT, Role.STUDENT, "user id", mConfig?.subGroup, User())
            mqttClient?.publish(mConfig?.topic, mGson!!.toJson(msg).toByteArray(), 2, false)
        }
    }
    
    /**
     * 拉取历史消息
     */
    public fun loadHistoryCommonMessage() {
        if (mqttClient?.isConnected == true) {
            var msg = Message(MessageType.CUSTOM, MessageFlag.COMMENT, Role.STUDENT, "user id", "all", User())
            mqttClient?.publish(mConfig.topic, mGson!!.toJson(msg).toByteArray(), 2, false)
        }
    }
    
    /**
     * 1对1发送
     */
    private fun sendMsgOne2One(msg: Message) {
        var acl = getAcl()
        when (msg.type) {
            Constant.MessageType.COMMON.value -> {
                if (myAcl?.commonMessageAcl?.text?.interval ?: 0 > 0) {
                    //                    if (0L == acl.commonMessageAcl?.text?.interval) {
                    //                        mCallback?.onWarning(IMCallBack.WARNING_FORBIDDEN)
                    //                        return
                    //                    }
                    if (getCurrentTime() - commonMessageLastTime - (acl.commonMessageAcl?.text?.interval ?: 0) * 1000 < 0) { //发送频繁
                        mCallback?.onWarning(IMCallBack.WARNING_OFEN)
                        return
                    }
                }
            }
            Constant.MessageType.CUSTOM.value -> {
                when (msg.flag) {
                    Constant.MessageFlag.FLOWER.value -> {
                        if (myAcl?.customMessageAcl?.flower?.interval ?: 0 > 0) {
                            //                            if (0L == acl.customMessageAcl?.flower?.interval) {
                            //                                mCallback?.onWarning(IMCallBack.WARNING_FORBIDDEN)
                            //                                return
                            //                            }
                            if (getCurrentTime() - customMessageLastTime_flower - (acl.customMessageAcl?.flower?.interval ?: 0) * 1000 < 0) { //发送频繁
                                mCallback?.onWarning(IMCallBack.WARNING_OFEN)
                                return
                            }
                            customMessageLastTime_flower = getCurrentTime()
                        }
                    }
                    Constant.MessageFlag.LIKE.value -> {
                        if (myAcl?.customMessageAcl?.like?.interval ?: 0 > 0) {
                            if (0L == acl.customMessageAcl?.like?.interval) {
                                mCallback?.onWarning(IMCallBack.WARNING_FORBIDDEN)
                                return
                            }
                            if (getCurrentTime() - customMessageLastTime_like - (acl.customMessageAcl?.like?.interval ?: 0) * 1000 < 0) { //发送频繁
                                mCallback?.onWarning(IMCallBack.WARNING_OFEN)
                                return
                            }
                            customMessageLastTime_like = getCurrentTime()
                        }
                    }
                }
                customMessageLastTime_like
            }
        }
        when (msg.type) {
            Constant.MessageType.COMMON.value -> commonMessageLastTime = getCurrentTime()
        }
        var t = "${mConfig?.topic}/p2p/${mConfig?.clientId}"
        if (mqttClient != null && mqttClient!!.isConnected)
            mqttClient!!.publish(t, mGson!!.toJson(msg).toByteArray(), 2, false)
    }
    
    /**
     * 发送消息
     */
    private fun sendMsg(msg: Message): Boolean {
        var acl = getAcl()
        //isForbidden: true 被禁言
        //interval 0:被禁言  -1：不限制  >0:多久可发一次
        when (msg.type) {
            Constant.MessageType.COMMON.value -> {
                if (isForbidden) {
                    mCallback?.onWarning(IMCallBack.WARNING_FORBIDDEN)
                    return false
                }
                if (myAcl?.commonMessageAcl?.text?.interval ?: 0 > 0) {
                    //                    if (0L == acl.commonMessageAcl?.text?.interval) {
                    //                        getSessionUser().prop?.speaking = "off"
                    //                        mCallback?.onWarning(IMCallBack.WARNING_FORBIDDEN)
                    //                        return false
                    //                    } else {
                    //                        getSessionUser().prop?.speaking = "on"
                    //                    }
                    if (getCurrentTime() - commonMessageLastTime - (acl.commonMessageAcl?.text?.interval ?: 0) * 1000 < 0) { //发送频繁
                        mCallback?.onWarning(IMCallBack.WARNING_OFEN)
                        return false
                    }
                }
            }
            Constant.MessageType.CUSTOM.value -> {
                when (msg.flag) {
                    Constant.MessageFlag.FLOWER.value -> {
                        if (myAcl?.customMessageAcl?.flower?.interval ?: 0 > 0) {
                            //                            if (0L == acl.customMessageAcl?.flower?.interval) {
                            //                                mCallback?.onWarning(IMCallBack.WARNING_FORBIDDEN)
                            //                                return false
                            //                            }
                            if (getCurrentTime() - customMessageLastTime_flower - (acl.customMessageAcl?.flower?.interval ?: 0) * 1000 < 0) { //发送频繁
                                mCallback?.onWarning(IMCallBack.WARNING_OFEN)
                                return false
                            }
                            customMessageLastTime_flower = getCurrentTime()
                        }
                    }
                    Constant.MessageFlag.LIKE.value -> {
                        if (myAcl?.customMessageAcl?.like?.interval ?: 0 > 0) {
                            //                            if (0L == acl.customMessageAcl?.like?.interval) {
                            //                                mCallback?.onWarning(IMCallBack.WARNING_FORBIDDEN)
                            //                                return false
                            //                            }
                            if (getCurrentTime() - customMessageLastTime_like - (acl.customMessageAcl?.like?.interval ?: 0) * 1000 < 0) { //发送频繁
                                mCallback?.onWarning(IMCallBack.WARNING_OFEN)
                                return false
                            }
                            customMessageLastTime_like = getCurrentTime()
                        }
                        
                    }
                    
                }
            }
        }
        when (msg.type) {
            Constant.MessageType.COMMON.value -> commonMessageLastTime = getCurrentTime()
        }
        var t = "${mConfig?.topic}/${mConfig?.roomId}/${mConfig?.subGroup}"
        if (mqttClient != null && mqttClient!!.isConnected) {
            MessageTrack.getInstance().putMessageId(msg.msgId)
            mqttClient!!.publish(t, mGson!!.toJson(msg).toByteArray(), 2, false)
        } else {
            return false
        }
        return true
    }
    
    /**
     * 文本消息到人(待实现)
     */
    private fun sendTextMessageToUser(content: String, userId: String, role: Role = Role.STUDENT) {
        var msg = Message(MessageType.COMMON, MessageFlag.TEXT, role, getSessionUser().userId, userId, getSessionUser())
        var obj = JsonObject()
        obj.addProperty("text", content)
        msg.content = obj
        sendMsgOne2One(msg)
    }
    
    /**
     * 文本消息到组
     */
    fun sendTextMessageToGroup(content: Message): Boolean {
        return sendMsg(content)
    }
    
    /**
     * 自定义消息到人(待实现)
     */
    private fun sendCustomMessageToUser(content: Any, messageFlag: MessageFlag, userId: String, role: Role = Role.STUDENT) {
        var msg = Message(MessageType.CUSTOM, messageFlag, role, getSessionUser().userId, userId, getSessionUser())
        msg.content = content
        sendMsg(msg)
    }
    
    /**
     * 自定义消息到组
     */
    fun sendCustomMessageToGroup(msg: Message): Boolean {
        //        var msg = Message(MessageType.CUSTOM, messageFlag, Role.STUDENT, getSessionUser().userId, "All", getSessionUser())
        //        msg.content = content
        return sendMsg(msg)
    }
    
    /**
     * 退出通信
     */
    public fun exit() {
        commonMessageLastTime = 0L
        customMessageLastTime_like = 0L
        customMessageLastTime_flower = 0L
        MessageTrack.getInstance().clear()
        if (mqttClient == null)
            return
        if (mqttClient!!.isConnected) {
            mqttClient!!.disconnect()
            mqttClient!!.close()
        }
    }
    
    /**
     * 消息处理
     */
    private fun handleMessages(msg: Message) {
        if (MessageTrack.getInstance().contains(msg.msgId) || msg.msgId == null || msg.msgId!!.isEmpty()) {
            if (!msg.msgId.isNullOrEmpty()) {
                MessageTrack.getInstance().removeMessageId(msg.msgId ?: "")
            }
            return
        }
        when (msg?.type) {
            Constant.MessageType.COMMON.value -> {
                if (getSessionUser().userId == msg.from)
                    return
                mCallback?.onTextMessage(msg)
            }
            MessageType.COMMAND.value -> {
                if (getSessionUser().userId == msg.from)
                    return
                when (msg.flag) {
                    "changeUserProp" -> ""
                }
                mCallback?.onCommandMessage(msg)
            }
            Constant.MessageType.CUSTOM.value -> {
                mCallback?.onCustomMessage(msg)
                when (msg.flag) {
                    Constant.MessageFlag.ONUSERLOGIN.value -> {
                        var user = getSessionUser()
                        if (user.userId == msg.fromUser?.userId && user._id != msg.fromUser?._id) {
                            mCallback?.onKickOut()
                        } else {
                            mCallback?.onUserLogin(msg.fromUser ?: User())
                        }
                        if (msg.fromUser != null)
                            userContainer.put(msg.fromUser!!.userId, msg.fromUser!!)
                    }
                    Constant.MessageFlag.ONUSERLOGINOUT.value -> {
                        var user = getSessionUser()
                        if (user.userId != msg.fromUser?.userId) {
                            mCallback?.onUserLogout(msg.fromUser ?: User())
                        }
                        if (msg.fromUser != null) {
                            userContainer.put(msg.fromUser!!.userId, msg.fromUser!!)
                        }
                    }
                    MessageFlag.ONKICKOUT.value -> {
                        var user = getSessionUser()
                        var content = JSONObject(Gson().toJson(msg.content))
                        var receiveUserId = content.optString("receiveUserId")
                        if (user.userId == receiveUserId) {
                            mCallback?.onKickOut()
                        }
                        if (msg.fromUser != null)
                            userContainer.put(msg.fromUser!!.userId, msg.fromUser!!)
                    }
                }
                
            }
        }
    }
}
