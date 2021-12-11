/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.gold.history;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/05 10:12
* @since 2.1.0
*/
public class LiveUserGoldHistoryRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "/user/gold/exchange/history";

    public LiveUserGoldHistoryRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 兑换物品的类型，1表示实物，2表示虚拟
     */
    private Integer type;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}