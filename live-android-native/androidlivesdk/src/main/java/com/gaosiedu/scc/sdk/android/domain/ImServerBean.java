package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;

/**
 * @author chengsa
 * @date 2018/11/7
 * @comment im-server鉴权数据
 */
public class ImServerBean implements Serializable {
    private String aclConfig;

    /**
     * im生成的用户临时id
     */
    private String id;

    /**
     * 用户登录时间
     */
    private Long loginTime;

    /**
     * 用户角色
     */
    private String role;

    /**
     * 直播间id
     */
    private String roomId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 加密密钥
     */
    private String serverSignKey;
    /**
     * mqtt配置信息
     */
    private UserMqttConfig userMqttConfig;

    public String getAclConfig() {
        return aclConfig;
    }

    public void setAclConfig(String aclConfig) {
        this.aclConfig = aclConfig;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserMqttConfig getUserMqttConfig() {
        return userMqttConfig;
    }

    public void setUserMqttConfig(UserMqttConfig userMqttConfig) {
        this.userMqttConfig = userMqttConfig;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getServerSignKey() {
        return serverSignKey;
    }

    public void setServerSignKey(String serverSignKey) {
        this.serverSignKey = serverSignKey;
    }

}
