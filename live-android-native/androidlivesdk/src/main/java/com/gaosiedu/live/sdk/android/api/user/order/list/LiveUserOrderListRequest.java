/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/31 10:42
* @since 2.1.0
*/
public class LiveUserOrderListRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "user/order/list";

    public LiveUserOrderListRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 课程名称
     */
    private String searchCourseName;

    /**
     * 订单编号
     */
    private String searchOrderNo;

    /**
     * 订单状态：不传值表示全部订单, cancel：表示已取消，unPay：未支付的订单，payed：已支付的,refund : 退款 
     */
    private String searchStatus;

    /**
     * sortParams
     */
    private String sortParams;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public String getSearchCourseName() {
        return this.searchCourseName;
    }

    public void setSearchCourseName(String searchCourseName) {
        this.searchCourseName = searchCourseName;
    }

    public String getSearchOrderNo() {
        return this.searchOrderNo;
    }

    public void setSearchOrderNo(String searchOrderNo) {
        this.searchOrderNo = searchOrderNo;
    }

    public String getSearchStatus() {
        return this.searchStatus;
    }

    public void setSearchStatus(String searchStatus) {
        this.searchStatus = searchStatus;
    }

    public String getSortParams() {
        return this.sortParams;
    }

    public void setSortParams(String sortParams) {
        this.sortParams = sortParams;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}