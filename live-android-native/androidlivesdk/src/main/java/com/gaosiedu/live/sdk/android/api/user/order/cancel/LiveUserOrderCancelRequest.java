/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.cancel;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/08/31 10:19
 * @since 2.1.0
 */
public class LiveUserOrderCancelRequest extends LiveSdkBaseRequest {
    
    private transient final String PATH = "user/order/cancel";
    
    public LiveUserOrderCancelRequest() {
        super();
        setPath(PATH);
    }
    
    
    /**
     * 订单id
     */
    private int id;
    
    /**
     * 用户id
     */
    private int userId;
    
    
    //属性get||set方法
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return this.userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
}
