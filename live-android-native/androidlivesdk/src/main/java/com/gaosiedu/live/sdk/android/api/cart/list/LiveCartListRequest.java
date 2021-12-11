/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.cart.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/08/29 14:11
 * @since 2.1.0
 */
public class LiveCartListRequest extends LiveSdkBaseRequest {
    
    private final String PATH = "cart/list";
    
    public LiveCartListRequest(Integer userId) {
        super();
        setPath(PATH);
        this.userId = userId;
    }
    
    
    /**
     * 用户id
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
