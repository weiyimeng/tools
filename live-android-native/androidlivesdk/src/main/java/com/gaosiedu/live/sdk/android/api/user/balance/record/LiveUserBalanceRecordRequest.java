/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.balance.record;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/30 19:28
* @since 2.1.0
*/
public class LiveUserBalanceRecordRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "user/balance/record";

    public LiveUserBalanceRecordRequest() {
        super();
        setPath(PATH);
    }


    /**
     * sortParams
     */
    private String sortParams;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public String getSortParams() {
        return this.sortParams;
    }

    public void setSortParams(String sortParams) {
        this.sortParams = sortParams;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
