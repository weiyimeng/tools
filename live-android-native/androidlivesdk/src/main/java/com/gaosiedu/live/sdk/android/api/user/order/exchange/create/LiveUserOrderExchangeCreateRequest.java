/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.exchange.create;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/** 转班生成订单 请求接口
* @author sdk-generator-android request
* @describe
* @date 2018/11/09 15:38
* @since 2.1.0
*/
public class LiveUserOrderExchangeCreateRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/order/exchange/create";

    public LiveUserOrderExchangeCreateRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 订单地址id
     */
    private Integer addressId;

    /**
     * changeReason
     */
    private String changeReason;

    /**
     * 课程ids
     */
    private Integer courseId;

    /**
     * exchangeFree
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