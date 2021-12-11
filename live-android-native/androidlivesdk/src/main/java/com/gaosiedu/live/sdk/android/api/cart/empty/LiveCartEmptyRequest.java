/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.cart.empty;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/24 17:41
* @since 2.1.0
*/
public class LiveCartEmptyRequest  extends LiveSdkBaseRequest {

    private final String PATH = "cart/empty";

    public LiveCartEmptyRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 
     */
    private Integer userId;


    //属性get||set方法
    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}