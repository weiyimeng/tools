/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.exchange.create;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/31 10:19
* @since 2.1.0
*/
public class LiveUserExchangeCreateRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/order/exchange/create";

    public LiveUserExchangeCreateRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 订单地址id
     */
    private Integer addressId;
    private String source = "android";

    /**
     * changeReason
     */
    private String changeReason;

    /**
     * 课程ids
     */
    private int courseId;

    /**
     * exchangeFree
     */
    private int exchangeFree;

    /**
     * 订单项id
     */
    private int orderItemId;

    /**
     * 订单编号
     */
    private String orderNO;

    /**
     * useBalance
     */
    private int useBalance;

    /**
     * 用户id
     */
    private int userId;


    //属性get||set方法
    public Integer getAddressId() {
        return this.addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getChangeReason() {
        return this.changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getExchangeFree() {
        return this.exchangeFree;
    }

    public void setExchangeFree(int exchangeFree) {
        this.exchangeFree = exchangeFree;
    }

    public int getOrderItemId() {
        return this.orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getOrderNO() {
        return this.orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public int getUseBalance() {
        return this.useBalance;
    }

    public void setUseBalance(int useBalance) {
        this.useBalance = useBalance;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
