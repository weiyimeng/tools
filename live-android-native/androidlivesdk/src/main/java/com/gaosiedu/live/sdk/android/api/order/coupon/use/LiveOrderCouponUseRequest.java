/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.order.coupon.use;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/29 19:15
* @since 2.1.0
*/
public class LiveOrderCouponUseRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "order/coupon/use";

    public LiveOrderCouponUseRequest() {
        super();
        setPath(PATH);
    }

    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 课程ids，多个用逗号隔开
     */
    private String courseIdsStr;
    /**
     * 课程优惠券
     */
    private String courseAndCouponIdsStr;
    /**
     * 订单优惠券
     */
    private String orderCouponIdsStr;
    /**
     * 是否使用优惠券，1使用，0不使用
     */
    private Integer  useBalance;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCourseIdsStr() {
        return courseIdsStr;
    }

    public void setCourseIdsStr(String courseIdsStr) {
        this.courseIdsStr = courseIdsStr;
    }

    public String getCourseAndCouponIdsStr() {
        return courseAndCouponIdsStr;
    }

    public void setCourseAndCouponIdsStr(String courseAndCouponIdsStr) {
        this.courseAndCouponIdsStr = courseAndCouponIdsStr;
    }

    public String getOrderCouponIdsStr() {
        return orderCouponIdsStr;
    }

    public void setOrderCouponIdsStr(String orderCouponIdsStr) {
        this.orderCouponIdsStr = orderCouponIdsStr;
    }

    public Integer getUseBalance() {
        return useBalance;
    }

    public void setUseBalance(Integer useBalance) {
        this.useBalance = useBalance;
    }
}