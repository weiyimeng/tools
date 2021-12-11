/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.sms.code;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/** 发送短信验证码 请求接口
* @author sdk-generator-android request
* @describe
* @date 2018/11/23 11:34
* @since 2.1.0
*/
public class LiveMobileCodeRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "sms/code/send";

    public LiveMobileCodeRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 
     */
    private String mobile;

    /**
     * 
     */
    private Integer type;


    //属性get||set方法
    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}