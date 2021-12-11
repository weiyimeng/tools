/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.auth.login;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/** 退出 请求接口
* @author sdk-generator-android request
* @describe
* @date 2019/01/03 18:37
* @since 2.1.0
*/
public class LiveLogoutRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "auth/logout";

    public LiveLogoutRequest() {
        super();
        setPath(PATH);
    }


    /**
     * token
     */
    private String token;

    /**
     * userId
     */
    private String userId;


    //属性get||set方法
    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
