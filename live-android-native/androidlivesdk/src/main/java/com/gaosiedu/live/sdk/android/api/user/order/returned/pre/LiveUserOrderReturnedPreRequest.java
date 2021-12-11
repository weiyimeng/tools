/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.returned.pre;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/** 退款单全部预申请的初始化 请求接口
* @author sdk-generator-android request
* @describe
* @date 2018/11/09 15:38
* @since 2.1.0
*/
public class LiveUserOrderReturnedPreRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/order/return/pre";

    public LiveUserOrderReturnedPreRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 订单id
     */
    private int orderId;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
