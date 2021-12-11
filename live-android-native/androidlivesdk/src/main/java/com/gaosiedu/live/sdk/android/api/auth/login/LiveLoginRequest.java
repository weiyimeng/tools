/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.auth.login;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/08/21 16:34
 * @since 2.1.0
 */
public class LiveLoginRequest extends LiveSdkBaseRequest {
    
    // private final String PATH = "auth/login";
    
    public LiveLoginRequest() {
        super();
        setPath("auth/login");
    }
    
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 用户登录名
     */
    private String username;
    
    
    //属性get||set方法
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
}
