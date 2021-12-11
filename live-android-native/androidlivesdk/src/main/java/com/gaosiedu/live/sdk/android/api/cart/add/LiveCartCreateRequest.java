/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.cart.add;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/24 17:43
* @since 2.1.0
*/
public class LiveCartCreateRequest  extends LiveSdkBaseRequest {

    private final String PATH = "cart/add";

    public LiveCartCreateRequest() {
        super();
        setPath(PATH);
    }


    /**
     *
     */
    private String associateId;

    /**
     *
     */
    private Integer shoppingCount;

    /**
     *
     */
    private Integer type;

    /**
     *
     */
    private Integer userId;


    //属性get||set方法
    public String getAssociateId() {
        return this.associateId;
    }

    public void setAssociateId(String associateId) {
        this.associateId = associateId;
    }

    public Integer getShoppingCount() {
        return this.shoppingCount;
    }

    public void setShoppingCount(Integer shoppingCount) {
        this.shoppingCount = shoppingCount;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
