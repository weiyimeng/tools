package com.haoke91.a91edu;

import com.blankj.utilcode.util.ObjectUtils;
import com.haoke91.a91edu.entities.CommandMessage;
import com.haoke91.im.mqtt.IMManager;
import com.haoke91.im.mqtt.entities.ACL;
import com.haoke91.im.mqtt.entities.User;

import org.greenrobot.essentials.ObjectCache;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/11/29 16:16
 */
public class CacheData {
    private static CacheData instance;
    private String teacherId = "";
    private long currentTime;
    public static boolean isLivingPlay = true;//是否是直播课程
    public static boolean isInitIm = true;//是否初始化、连接IM
    public static long CLICK_TIME = 0L;//点击进入直播间时间
    public static boolean isFirstEnter = false;//是否第一次进入直播间
    /**
     * "changeBarrageStatus":弹幕
     * changeUserCommonMsgStatus: 是否可发送普通文本  changeGroupCommonMsgStatus:组是否可发送
     * changeUserCustomMsgStatus:用户是否可发自定义消息 changeGroupCustomMsgStatus: 组内是否可发自定义
     * changeLiveStatus:直播间状态 bgMusic:播放音乐
     * //可不用
     * changeUserProp:用户属性  changeGroupProp:组属性
     * showTimer：计时器
     * //以上
     */
    private Map<String, CommandMessage> mAuthMap;
    
    private CacheData() {
    }
    
    public static CacheData getInstance() {
        if (instance == null){
            synchronized (CacheData.class) {
                instance = new CacheData();
            }
            
        }
        return instance;
    }
    
    private IMManager.Config mConfig;
    private User mUser;
    private ACL mACL;
    
    public IMManager.Config getConfig() {
        return mConfig;
    }
    
    public void setConfig(IMManager.Config config) {
        mConfig = config;
    }
    
    public User getUser() {
        return mUser;
    }
    
    public void setUser(User user) {
        mUser = user;
    }
    
    public ACL getACL() {
        return mACL;
    }
    
    public void setACL(ACL ACL) {
        mACL = ACL;
    }
    
    public String getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
    
    public long getCurrentTime() {
        return currentTime;
    }
    
    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
    
    /**
     * 处理历史命令
     *
     * @param messages
     * @param userId
     * @param subGroup
     * @return
     */
    public Map<String, CommandMessage> dealCommandMessage(List<CommandMessage> messages, String userId, String subGroup) {
        if (ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(subGroup)){
            return new HashMap<>();
        }
        if (mAuthMap == null){
            mAuthMap = new HashMap<>();
        }
        mAuthMap.clear();
        Collections.reverse(messages);
        for (CommandMessage msg : messages) {
            if ("changeUserCommonMsgStatus".equalsIgnoreCase(msg.getFlag()) || "changeGroupCommonMsgStatus".equalsIgnoreCase(msg.getFlag())){
                if (userId.equalsIgnoreCase(msg.getReceiveUserId()) || subGroup.equalsIgnoreCase(msg.getReceiveSubgroupId()) || "ALL".equalsIgnoreCase(msg.getReceiveSubgroupId())){
                    if ("off".equalsIgnoreCase(msg.getValue())){
                        mAuthMap.put("changeCommonStatus", msg);
                    }
                }
                continue;
            }
            if ("changeUserCustomMsgStatus".equalsIgnoreCase(msg.getFlag()) || "changeGroupCustomMsgStatus".equalsIgnoreCase(msg.getFlag())){
                if (userId.equalsIgnoreCase(msg.getReceiveUserId()) || subGroup.equalsIgnoreCase(msg.getReceiveSubgroupId())){
                    mAuthMap.put("changeCustomStatus", msg);
                }
                continue;
            }
            if (userId.equalsIgnoreCase(msg.getReceiveUserId())){
                mAuthMap.put(msg.getFlag(), msg);
            }
            if (subGroup.equalsIgnoreCase(msg.getReceiveSubgroupId()) || "ALL".equalsIgnoreCase(msg.getReceiveSubgroupId())){
                mAuthMap.put(msg.getFlag(), msg);
            }
        }
        return mAuthMap;
    }
    
    /**
     * AuthMap(IM 命令集合)
     *
     * @return
     */
    public Map<String, CommandMessage> getAuthMap() {
        return mAuthMap;
    }
    
    
}
