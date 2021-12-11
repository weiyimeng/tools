package com.haoke91.im.mqtt.entities

/**
 * 项目名称：IMSDK_android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/11/20 18:24
 */
class Constant {
    enum class MessageType(var value: String = "common") {
        //common 文本消息    custom：自定义消息
        SYSTEM("system"),
        COMMON("common"), CUSTOM("custom"), COMMAND("command")
    }
    
    enum class MessageFlag(var value: String = "text", var exp: String = "") {
        TEXT("text", "文本消息"), IMG("img", "图片消息"), VIDEO("video", "视频消息"),
        ONUSERLOGIN("onUserLogin", "登录"), ONUSERLOGINOUT("onUserLoginOut", "退出"), ONKICKOUT("onKickOut", "被提出"),
        
        FLOWER("flower", "送花"), SENDGOLD("sendGold", "发送金币"), COMMENT("comment", "评价"), LIKE("like", "点赞"), ANSWER("answer", "回答问题"),
        LIVELOGINSUCCESS("liveLoginSuccess", "直播登入"), LIVELOGINOUT("liveLoginOut", "直播登入"), LIVEOVERCLASS("liveOverClass", "直播下课"), LIVEBEGINCLASS("liveBeginClass", "直播上课"), LIVETIMECOUNT("liveTimeCount", "直播一段时间统计"),
        REPLAYLOGINSUCCESS("replayLoginSuccess", "回放登入"), REPLAYLOGINOUT("replayLoginOut", "回放登出"), REPLAYTIMECOUNT("replayTimeCount", "回放一段时间统计"),
        
        CHANGELIVESTATUS("changeLiveStatus", "直播间状态更改"), SENDNOTICE("sendNotice", "直播间公告"), QUESTION("question", "教师发送问题"),
        
        ONLINETIME("onLineTime", "在线时长"),
        BGMUSIC("bgMusic","播放音乐"),
        RANKLIST("rankList", "榜单信息"),
        BARRAGE("barrage", "弹幕"),
        CHANGEUSERPROP("changeUserProp", "改变用户属性"),
        CHANGEGROUPPROP("changeGroupProp", "改变组属性")
    }
    
    enum class Role(var value: String = "student") {
        STUDENT("student"), TEACHER("teacher")
    }
    
    enum class LoginStatus(var value: String = "login") {
        LOGIN("login"), LOGOUT("loginOut"), ONKICKOUT("onKickOut")
    }
}
