/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.order.address;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/29 19:15
* @since 2.1.0
*/
public class LiveOrderAddressRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "order/address";

    public LiveOrderAddressRequest() {
        super();
        setPath(PATH);
    }


    /**
     *
     */
    private int orderId;

    /**
     *
     */
    private int userId;


    //属性get||set方法
    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
