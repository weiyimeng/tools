package com.gaosiedu.scc.sdk.android.domain;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/12 9:44 AM
 * 修改人：weiyimeng
 * 修改时间：2018/11/12 9:44 AM
 * 修改备注：
 */
public class LmcRoomConfigRequest extends LiveSdkBaseRequest {
    String appId;
    int roomId;
    String role;
    String userId;
    String subGroupId;
    String random;
    String expire;
    String appSign;
    String nickname;
    
    public LmcRoomConfigRequest(String appId, int roomId, String role, String userId, String subGroupId, String random, String expire, String appSign, String nickname) {
        this.appId = appId;
        this.roomId = roomId;
        this.role = role;
        this.userId = userId;
        this.subGroupId = subGroupId;
        this.random = random;
        this.expire = expire;
        this.appSign = appSign;
        this.nickname = nickname;
    }
    
    public String getAppId() {
        return appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public int getRoomed() {
        return roomId;
    }
    
    public void setRoomed(int roomed) {
        this.roomId = roomed;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
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
}
