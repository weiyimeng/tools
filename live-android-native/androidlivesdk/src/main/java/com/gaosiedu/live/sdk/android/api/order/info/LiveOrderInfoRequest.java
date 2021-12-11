/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.order.info;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/29 19:15
* @since 2.1.0
*/
public class LiveOrderInfoRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "order/info";

    public LiveOrderInfoRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 
     */
    private String orderNo;

    /**
     * 
     */
    private Integer userId;


    //属性get||set方法
    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}