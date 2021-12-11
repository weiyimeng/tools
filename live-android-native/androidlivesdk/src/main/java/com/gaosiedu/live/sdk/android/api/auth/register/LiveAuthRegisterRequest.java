/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.auth.register;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/** 注册 请求接口
* @author sdk-generator-android request
* @describe
* @date 2018/11/23 11:13
* @since 2.1.0
*/
public class LiveAuthRegisterRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "auth/register";

    public LiveAuthRegisterRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 手机验证码
     */
    private String mobileCode;

    /**
     * 密码
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