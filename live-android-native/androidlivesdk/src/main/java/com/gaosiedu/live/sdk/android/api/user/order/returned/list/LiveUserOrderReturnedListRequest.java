/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.returned.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/04 16:13
* @since 2.1.0
*/
public class LiveUserOrderReturnedListRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "user/order/return/list";

    public LiveUserOrderReturnedListRequest() {
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