/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.live.init;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/29 19:15
* @since 2.1.0
*/
public class LiveInitRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "live/init";

    public LiveInitRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 
     */
    private Integer ckId;

    /**
     * 
     */
    private Integer userId;


    //属性get||set方法
    public Integer getCkId() {
        return this.ckId;
    }

    public void setCkId(Integer ckId) {
        this.ckId = ckId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}