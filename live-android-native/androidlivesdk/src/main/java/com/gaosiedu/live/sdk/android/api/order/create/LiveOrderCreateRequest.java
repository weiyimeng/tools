/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.order.create;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/08/29 19:15
 * @since 2.1.0
 */
public class LiveOrderCreateRequest extends LiveSdkBaseRequest {
    
    private transient final String PATH = "order/create";
    
    public LiveOrderCreateRequest() {
        super();
        setPath(PATH);
    }
    
    
    /**
     *
     */
    private String addressId;
    /**
     * 1 ali  2wx
     */
    private String payWayId;
    /**
     *
     */
    private String courseAndCouponIdsStr;
    private String source = "android";
    
    /**
     *
     */
    private String courseIdsStr;
    
    /**
     *
     */
    private String orderCouponIdsStr;
    /**
     * 学科优惠券
     */
    private String subjectCouponIdsStr;
    
    public String getSubjectCouponIdsStr() {
        return subjectCouponIdsStr;
    }
    
    public void setSubjectCouponIdsStr(String subjectCouponIdsStr) {
        this.subjectCouponIdsStr = subjectCouponIdsStr;
    }
    
    /**
     * 1  使用   else 0
     */
    private Integer useBalance;
    
    /**
     *
     */
    private Integer userId;
    
    
    //属性get||set方法
    public String getAddressId() {
        return this.addressId;
    }
    
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
    
    public String getCourseAndCouponIdsStr() {
        return this.courseAndCouponIdsStr;
    }
    
    public void setCourseAndCouponIdsStr(String courseAndCouponIdsStr) {
        this.courseAndCouponIdsStr = courseAndCouponIdsStr;
    }
    
    public String getCourseIdsStr() {
        return this.courseIdsStr;
    }
    
    public void setCourseIdsStr(String courseIdsStr) {
        this.courseIdsStr = courseIdsStr;
    }
    
    public String getOrderCouponIdsStr() {
        return this.orderCouponIdsStr;
    }
    
    public void setOrderCouponIdsStr(String orderCouponIdsStr) {
        this.orderCouponIdsStr = orderCouponIdsStr;
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
    
    public String getPayWayId() {
        return payWayId;
    }
    
    public void setPayWayId(String payWayId) {
        this.payWayId = payWayId;
    }
}


