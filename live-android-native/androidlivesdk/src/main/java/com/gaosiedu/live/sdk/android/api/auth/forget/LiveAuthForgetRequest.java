/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.auth.forget;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/** 忘记密码 请求接口
* @author sdk-generator-android request
* @describe
* @date 2018/11/23 15:33
* @since 2.1.0
*/
public class LiveAuthForgetRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "auth/forget";

    public LiveAuthForgetRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 验证码
     */
    private String mobileCode;

    /**
     * 新密码
     */
    private String password;

    /**
     * 用户名也就是手机号
     */
    private String username;


    //属性get||set方法
    public String getMobileCode() {
        return this.mobileCode;
    }

    public void setMobileCode(String mobileCode) {
        this.mobileCode = mobileCode;
    }

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