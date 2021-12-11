/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.base.profile;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/25 16:28
* @since 2.1.0
*/
public class LiveUserBaseProfileRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/profile";

    public LiveUserBaseProfileRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}