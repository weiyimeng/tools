/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.gold.exchange;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/05 10:12
* @since 2.1.0
*/
public class LiveUserGoldExchangeRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/gold/exchange";

    public LiveUserGoldExchangeRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 收货地址ID
     */
    private Integer addressId;

    /**
     * 兑换物品的数目
     */
    private Integer count;

    /**
     * 兑换物品id
     */
    private Integer productId;

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

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}