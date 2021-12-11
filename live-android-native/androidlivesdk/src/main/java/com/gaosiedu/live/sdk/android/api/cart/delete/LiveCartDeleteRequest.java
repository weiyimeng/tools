/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.cart.delete;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/08/24 18:00
 * @since 2.1.0
 */
public class LiveCartDeleteRequest extends LiveSdkBaseRequest {
    
    private transient final String PATH = "cart/delete";
    
    public LiveCartDeleteRequest() {
        super();
        setPath(PATH);
    }
    
    
    /**
     * 购物车id，多个用逗号分隔
     */
    private String ids;
    
    /**
     * 用户id
     */
    private Integer userId;
    
    
    //属性get||set方法
    public String getIds() {
        return this.ids;
    }
    
    public void setIds(String ids) {
        this.ids = ids;
    }
    
    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
}
