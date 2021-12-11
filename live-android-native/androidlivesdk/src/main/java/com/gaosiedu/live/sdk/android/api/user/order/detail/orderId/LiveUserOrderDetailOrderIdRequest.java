/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.detail.orderId;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/31 10:19
* @since 2.1.0
*/
public class LiveUserOrderDetailOrderIdRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/order/detail/orderId";

    public LiveUserOrderDetailOrderIdRequest() {
        super();
        setPath(PATH);
    }


    /**
     * orderId
     */
    private long orderId;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
