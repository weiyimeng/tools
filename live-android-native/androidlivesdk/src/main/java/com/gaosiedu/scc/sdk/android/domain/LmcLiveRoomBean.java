package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;

/**
 * @author chengsa
 * @date 2018/11/6
 * @comment lmc鉴权需要的参数信息
 */
public class LmcLiveRoomBean implements Serializable {

    /**
     * 直播间id
     */
    private int roomId;

    /**
     * appId
     */
    private String appId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 角色
     */
    private String role;

    /**
     * 分组
     */
    private String subGroupId;

    /**
     * 随机
     */
    private String random;

    /**
     * 过期时长单位秒
     */
    private String expire;

    /**
     * app签名
     */
    private String appSign;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * lmc服务地址
     */
    private String lmcService;

    /**
     * 直播地址
     */
    private String liveUrl;

    /**
     * 直播地址
     */
    private String httpsLiveUrl;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSubGroupId() {
        return subGroupId;
    }

    public void setSubGroupId(String subGroupId) {
        this.subGroupId = subGroupId;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getAppSign() {
        return appSign;
    }

    public void setAppSign(String appSign) {
        this.appSign = appSign;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLmcService() {
        return lmcService;
    }

    public void setLmcService(String lmcService) {
        this.lmcService = lmcService;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getHttpsLiveUrl() {
        return httpsLiveUrl;
    }

    public void setHttpsLiveUrl(String httpsLiveUrl) {
        this.httpsLiveUrl = httpsLiveUrl;
    }
}
