/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.pay.wx;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
 * 微信支付支付 请求接口
 *
 * @author sdk-generator-android request
 * @describe
 * @date 2018/11/27 15:37
 * @since 2.1.0
 */
public class LivePayWxRequest extends LiveSdkBaseRequest {
    
    private transient final String PATH = "pay/wx";
    
    public LivePayWxRequest() {
        super();
        setPath(PATH);
    }
    
    
    /**
     * 订单编号
     */
    private String orderNo;
    
    /**
     * JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付、MWEB--微信h5
     */
    private String platformSubType = "MWEB";
    private int payType = 4;
    
    /**
     * 用户id
     */
    private int userId;
    //    public String spbillCreateIp;
    
    //属性get||set方法
    public String getOrderNo() {
        return this.orderNo;
    }
    
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    public String getPlatformSubType() {
        return this.platformSubType;
    }
    
    public void setPlatformSubType(String platformSubType) {
        this.platformSubType = platformSubType;
    }
    
    public int getUserId() {
        return this.userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
}
