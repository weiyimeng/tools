/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.detail.orderNo;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/10 12:06
* @since 2.1.0
*/
public class LiveUserOrderDetailOrderNORequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/order/orderNo/detail";

    public LiveUserOrderDetailOrderNORequest() {
        super();
        setPath(PATH);
    }


    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户id
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