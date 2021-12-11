/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.returned.detail;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/04 16:13
* @since 2.1.0
*/
public class LiveUserOrderReturnedDetailRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/order/return/detail";

    public LiveUserOrderReturnedDetailRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 课程的退款单编号
     */
    private String orderReturnId;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public String getOrderReturnId() {
        return this.orderReturnId;
    }

    public void setOrderReturnId(String orderReturnId) {
        this.orderReturnId = orderReturnId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
