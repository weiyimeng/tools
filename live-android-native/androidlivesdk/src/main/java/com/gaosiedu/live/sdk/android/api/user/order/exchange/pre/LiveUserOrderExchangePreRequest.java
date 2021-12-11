/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.exchange.pre;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
 * 转班计算 请求接口
 *
 * @author sdk-generator-android request
 * @describe
 * @date 2018/11/09 15:38
 * @since 2.1.0
 */
public class LiveUserOrderExchangePreRequest extends LiveSdkBaseRequest {
    
    private transient final String PATH = "user/order/exchange/pre";
    
    public LiveUserOrderExchangePreRequest() {
        super();
        setPath(PATH);
    }
    
    
    /**
     * 要換的課程id
     */
    private int courseId;
    
    /**
     * 标识是否为免费转班 1不免费，0免费
     */
    private int exchangeFree;
    
    /**
     * 订单项id
     */
    private int orderItemId;
    
    /**
     * 订单编号
     */
    private String orderNO;
    
    /**
     * useBalance
     */
    private int useBalance;
    
    /**
     * 用户id
     */
    private int userId;
    
    
    public int getCourseId() {
        return this.courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public int getExchangeFree() {
        return this.exchangeFree;
    }
    
    public void setExchangeFree(int exchangeFree) {
        this.exchangeFree = exchangeFree;
    }
    
    public int getOrderItemId() {
        return this.orderItemId;
    }
    
    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }
    
    public String getOrderNO() {
        return this.orderNO;
    }
    
    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }
    
    public int getUseBalance() {
        return this.useBalance;
    }
    
    public void setUseBalance(int useBalance) {
        this.useBalance = useBalance;
    }
    
    public int getUserId() {
        return this.userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
}
