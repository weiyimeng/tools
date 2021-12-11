package com.haoke91.im.mqtt

import com.haoke91.im.mqtt.entities.Message
import com.haoke91.im.mqtt.entities.User

/**
 * 项目名称：IMSDK_android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/10/22 10:55
 */
interface IMCallBack {
    /**
     * 连接 0成功 1失败
     */
    fun onConnected(code: Int)
    
    /**
     * 普通消息
     */
    fun onTextMessage(msg: Message)
    
    fun onImageMessage(msg: Message)
    
    fun onAudioMessage(msg: Message)
    
    fun onVideoMessage(msg: Message)
    
    /**
     * 登录状态
     */
    fun onLoginSuccess(time:Long)
    
    fun onLogout()
    
    fun onKickOut()
    
    fun onLoginError(msg: Message)
    
    fun onUserLogin(user: User)
    
    fun onUserLogout(user: User)
    
    /**
     * 自定义消息
     */
    fun onCustomMessage(msg: Message)
    
    /**
     * 命令消息
     */
    fun onCommandMessage(msg: Message)
    
    /**
     * 系统消息
     */
    fun onSysMessage(msg: Message)
    
    /**
     * push命令（属性or状态改变）
     */
    fun onPropChange(msg: Message)
    
    /**
     * 当前用户状态改变（登录，退出，链接，踢出等）
     *
     * @param status
     */
    fun onStatusChange(status: String)
    
    fun onUserPropChange(msg: Message)
    
    fun onUserStatusChange(msg: Message)
    
    fun onGroupPropChange(msg: Message)
    
    fun onGroupStatusChange(msg: Message)
    
    /**
     * pull结果（load方法调用时，在此获取结果
     */
    fun onCommonMessagePush(msg: Message)
    
    fun onCustomMessagePush(msg: Message)
    
    fun onUserInfoPush(msg: Message)
    
    /**
     * 房间人数变化监听
     */
    fun onUserCountChange(timerUserCountData: Int)
    
    /**
     * 计时器
     */
    fun onTimerUpdate(time: Long)
    
    /**
     * 系统错误消息
     */
    fun onError(err: String)
    
    /**
     * 错误提示
     *
     */
    fun onWarning(code: Int)
    
    /**
     * 错误提示 code说明
     *
     */
    companion object {
        var WARNING_OFEN = 0x101 //不可频繁发送
        var WARNING_UNCONNECTED = 0x102 //断开连接
        var WARNING_FORBIDDEN = 0x103 //被禁言
    }
    
    
}
