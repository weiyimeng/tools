/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.returned.cancel;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/31 12:05
* @since 2.1.0
*/
public class LiveUserOrderReturnedCancelRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/order/return/cancel";

    public LiveUserOrderReturnedCancelRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 退货单id
     */
    private String id;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
