/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.coupon.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/25 16:28
* @since 2.1.0
*/
public class LiveUserCouponListRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "user/coupon/list";

    public LiveUserCouponListRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 优惠券使用状态;1表示可以使用，2表示过期未使用，3已使用
     */
    private Integer status;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}