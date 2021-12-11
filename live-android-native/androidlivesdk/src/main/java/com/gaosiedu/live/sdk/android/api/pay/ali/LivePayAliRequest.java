/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.pay.ali;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
 * 支付宝支付 请求接口
 *
 * @author sdk-generator-android request
 * @describe
 * @date 2018/11/27 15:37
 * @since 2.1.0
 */
public class LivePayAliRequest extends LiveSdkBaseRequest {
    
    private transient final String PATH = "pay/ali";
    
    public LivePayAliRequest() {
        super();
        setPath(PATH);
    }
    
    
    /**
     * 订单编号
     */
    private String orderNo;
    
    /**
     * WEBPC--电脑网站支付、WAP--手机网站支付
     */
    private String platformSubType = "APP";
    private Integer payType = 4;
    /**
     * 用户id
     */
    private Integer userId;
    
    
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
    
    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
}
