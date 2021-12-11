/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.order.pre;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/08/28 17:05
 * @since 2.1.0
 */
public class LivePreCreateOrderRequest extends LiveSdkBaseRequest {
    
    private transient final String PATH = "order/pre";
    
    public LivePreCreateOrderRequest() {
        super();
        setPath(PATH);
    }
    
    
    /**
     * 订单号拼接 ，链接
     * 111,111
     */
    private String courseIdsStr;
    
    
    /**
     *
     */
    private Integer useBalance = 1;
    
    /**
     *
     */
    private Integer userId;
    
    
    //属性get||set方法
    public String getCourseIdsStr() {
        return this.courseIdsStr;
    }
    
    public void setCourseIdsStr(String courseIdsStr) {
        this.courseIdsStr = courseIdsStr;
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
