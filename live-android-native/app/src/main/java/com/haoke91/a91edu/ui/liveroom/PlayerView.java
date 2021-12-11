package com.haoke91.a91edu.ui.liveroom;



import com.haoke91.im.mqtt.entities.Message;
import com.haoke91.im.mqtt.entities.User;
import com.haoke91.videolibrary.presenter.views.BaseView;

import java.util.List;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/7/19 19:08
 */
public interface PlayerView extends BaseView {
    
    /**
     * 连接状态  0：成功
     * @param code
     */
    void onConnected(int code);
    /**
     * 接收到文本消息
     * @param msg
     */
    void onTextMessage(Message msg);
    
    /**
     * 接收到自定义消息
     * @param msg
     */
    void onCustomMessage(Message msg);
    
    /**
     * 有用户进入
     */
    void onUserLogin(User user);
    
    /**
     * 有用户退出
     */
    void onUserLogout(User user);
    
    /**
     * 历史消息
     * @param list
     */
    void onCommonHistory(List<Message> list);
    
    /**
     * 接收到组内信息更新
     */
    void onUserInfoPush(Message msg);
}
