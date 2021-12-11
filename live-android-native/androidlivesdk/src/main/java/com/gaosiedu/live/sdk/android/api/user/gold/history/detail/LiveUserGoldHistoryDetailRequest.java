/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.gold.history.detail;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/17 14:09
* @since 2.1.0
*/
public class LiveUserGoldHistoryDetailRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "/user/gold/exchange/history/detail";

    public LiveUserGoldHistoryDetailRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 用户兑换记录的id
     */
    private Integer recordId;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public Integer getRecordId() {
        return this.recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}