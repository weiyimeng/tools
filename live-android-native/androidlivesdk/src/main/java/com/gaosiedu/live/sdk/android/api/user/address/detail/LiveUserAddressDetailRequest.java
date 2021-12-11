/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.address.detail;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/30 19:10
* @since 2.1.0
*/
public class LiveUserAddressDetailRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/address/detail";

    public LiveUserAddressDetailRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 收货地址id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}