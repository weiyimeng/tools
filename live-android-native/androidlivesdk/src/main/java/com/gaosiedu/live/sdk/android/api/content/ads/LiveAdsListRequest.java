/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.content.ads;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/04 09:57
* @since 2.1.0
*/
public class LiveAdsListRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "content/ads";

    public LiveAdsListRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 位置：对于首页顶部的广告为：3
     */
    private Integer location;

    /**
     * 
     */
    private String platform;


    //属性get||set方法
    public Integer getLocation() {
        return this.location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

}