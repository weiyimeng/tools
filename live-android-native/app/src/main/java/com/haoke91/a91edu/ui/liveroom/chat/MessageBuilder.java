package com.haoke91.a91edu.ui.liveroom.chat;

import com.google.gson.JsonObject;
import com.haoke91.im.mqtt.IMManager;
import com.haoke91.im.mqtt.entities.Constant;
import com.haoke91.im.mqtt.entities.Message;
import com.haoke91.im.mqtt.entities.User;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/8 5:14 PM
 * 修改人：weiyimeng
 * 修改时间：2018/11/8 5:14 PM
 * 修改备注：
 */
public class MessageBuilder {
    /**
     * 文本消息
     *
     * @return
     */
    public static Message createTextMessage(String message) {
        //        Message message1 = Message.create();
        //        User user = new User();
        //        user.prop = new Prop();
        //        message1.content(message);
        //        message1.user = user;
        //        Message msg = new Message(Constant.MessageType.COMMON, Constant.MessageFlag.TEXT, Constant.Role.STUDENT, "", "", new User());
        //        JsonObject obj = new JsonObject();
        //        obj.addProperty("text", message);
        //        msg.setContent(obj);
        Message msg = Message.Companion.createTextMessage(message);
        return msg;
    }
    
    /**
     * 点赞消息
     *
     * @return
     */
    public static Message createPriseMessage() {
        Message msg = new Message(Constant.MessageType.CUSTOM, Constant.MessageFlag.LIKE, Constant.Role.STUDENT, IMManager.Companion.getInstance().getSessionUser().getUserId(), "ALL", IMManager.Companion.getInstance().getSessionUser());
        JsonObject obj=new JsonObject();
        obj.addProperty("img_url","");
        msg.setContent(obj);
        return msg;
    }
    
    /**
     * 送花消息
     *
     * @return
     */
    public static Message createFlowerMessage() {
        Message msg = new Message(Constant.MessageType.CUSTOM, Constant.MessageFlag.FLOWER, Constant.Role.STUDENT, IMManager.Companion.getInstance().getSessionUser().getUserId(), "ALL", IMManager.Companion.getInstance().getSessionUser());
        JsonObject obj=new JsonObject();
        obj.addProperty("img_url","");
        msg.setContent(obj);
        return msg;
    }
    
    /**
     * 自定义消息
     * @param flag
     * @return
     */
    public static Message createCustomMessage(Constant.MessageFlag flag) {
        Message msg = new Message(Constant.MessageType.CUSTOM, flag, Constant.Role.STUDENT, IMManager.Companion.getInstance().getSessionUser().getUserId(), "ALL", IMManager.Companion.getInstance().getSessionUser());
        JsonObject obj=new JsonObject();
        obj.addProperty("img_url","");
        msg.setContent(obj);
        return msg;
    }
    
    /**
     * 系统消息
     *
     * @return
     */
    public static Message createSystemMessage() {
        Message msg = new Message(Constant.MessageType.COMMON, Constant.MessageFlag.TEXT, Constant.Role.STUDENT, "", "", new User());
        return msg;
    }
}
