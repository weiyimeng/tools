package com.haoke91.a91edu.view;

import com.haoke91.im.mqtt.entities.Message;
import com.haoke91.im.mqtt.entities.User;

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
     *
     * @param code
     */
    void onConnected(int code);
    
    /**
     * 接收到文本消息
     *
     * @param msg
     */
    void onTextMessage(Message msg);
    
    /**
     * 接收到自定义消息
     *
     * @param msg
     */
    void onCustomMessage(Message msg);
    
    /**
     * 有用户进入
     */
    void onUserLogin(User user);
    
    /**
     *
     */
    void onKickOut();
    
    /**
     * 有用户退出
     */
    void onUserLogout(User user);
    
    /**
     * 修改用户金币、成长值
     */
    void onChangeUserProp(int gold, int progress);
    
    /**
     * 获取历史消息 null默认没有更多
     *
     * @param messages
     */
    void onGetHistory(List<Message> messages);
    
    /**
     * 直播状态：start end suspend
     *
     * @param status
     */
    void onLiveStatus(String status);
    
    /**
     * 弹幕是否开启 on off
     *
     * @param status
     */
    void onBarrageStatus(String status);
    
    /**
     * 禁言
     *
     * @param isForbidden
     */
    void onForbidden(boolean isForbidden);
    
    /**
     * 消息撤回
     *
     * @param id
     */
    void onWithdrewText(String id);
    
    /**
     * 是否可播放音乐
     * @return
     */
    boolean onEnablePlayAudio();
}
