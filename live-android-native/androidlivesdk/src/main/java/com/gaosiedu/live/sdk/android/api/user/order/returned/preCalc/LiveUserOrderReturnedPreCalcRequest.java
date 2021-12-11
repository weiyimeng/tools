/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.returned.preCalc;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/** 退款单的计算 请求接口
* @author sdk-generator-android request
* @describe
* @date 2018/11/09 15:38
* @since 2.1.0
*/
public class LiveUserOrderReturnedPreCalcRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/order/return/preCalc";

    public LiveUserOrderReturnedPreCalcRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 订单
     */
    private int orderId;

    /**
     * 订单项id，用逗号分割
     */
    private String orderItemIdsStr;

    /**
     * 用户id
     */
    private int userId;


    //属性get||set方法
    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderItemIdsStr() {
        return this.orderItemIdsStr;
    }

    public void setOrderItemIdsStr(String orderItemIdsStr) {
        this.orderItemIdsStr = orderItemIdsStr;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
