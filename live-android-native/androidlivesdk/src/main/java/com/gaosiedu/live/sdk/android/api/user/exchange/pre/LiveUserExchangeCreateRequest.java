/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.exchange.pre;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/04 16:13
* @since 2.1.0
*/
public class LiveUserExchangeCreateRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/order/exchange/pre";

    public LiveUserExchangeCreateRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 换课原因
     */
    private String changeReason;

    /**
     * 要換的課程id
     */
    private Integer courseId;

    /**
     * 标识是否为免费转班 1不免费，0免费
     */
    private Integer exchangeFree;

    /**
     * 订单项id
     */
    private Integer orderItemId;

    /**
     * 订单编号
     */
    private String orderNO;

    /**
     * useBalance
     */
    private Integer useBalance;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public String getChangeReason() {
        return this.changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public Integer getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getExchangeFree() {
        return this.exchangeFree;
    }

    public void setExchangeFree(Integer exchangeFree) {
        this.exchangeFree = exchangeFree;
    }

    public Integer getOrderItemId() {
        return this.orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getOrderNO() {
        return this.orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public Integer getUseBalance() {
        return this.useBalance;
    }

    public void setUseBalance(Integer useBalance) {
        this.useBalance = useBalance;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}