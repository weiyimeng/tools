/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.returned.create;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/** 创建退款单 请求接口
* @author sdk-generator-android request
* @describe
* @date 2018/11/09 15:38
* @since 2.1.0
*/
public class LiveUserOrderReturnedCreateRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/order/return/create";

    public LiveUserOrderReturnedCreateRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 开户行地址
     */
    private String bankAddress;

    /**
     * 银行卡号
     */
    private String cardNum;

    /**
     * 用户订单地址id
     */
    private int orderId;

    /**
     * 订单明细id列表
     */
    private List orderItemIds;

    /**
     * 订单明细id字符串表示,用,分隔
     */
    private String orderItemIdsStr;

    /**
     * 退货原因
     */
    private String reason;

    /**
     * 身份证正面
     */
    private String resourceFront;

    /**
     * 身份证反面
     */
    private String resourceReverse;

    /**
     * 用户选择退款方式，1：原路返回，2余额 3退至银行卡
     */
    private int returnWays;

    /**
     * 用户id
     */
    private int userId;


    //属性get||set方法
    public String getBankAddress() {
        return this.bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getCardNum() {
        return this.cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public List getOrderItemIds() {
        return this.orderItemIds;
    }

    public void setOrderItemIds(List orderItemIds) {
        this.orderItemIds = orderItemIds;
    }

    public String getOrderItemIdsStr() {
        return this.orderItemIdsStr;
    }

    public void setOrderItemIdsStr(String orderItemIdsStr) {
        this.orderItemIdsStr = orderItemIdsStr;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResourceFront() {
        return this.resourceFront;
    }

    public void setResourceFront(String resourceFront) {
        this.resourceFront = resourceFront;
    }

    public String getResourceReverse() {
        return this.resourceReverse;
    }

    public void setResourceReverse(String resourceReverse) {
        this.resourceReverse = resourceReverse;
    }

    public int getReturnWays() {
        return this.returnWays;
    }

    public void setReturnWays(int returnWays) {
        this.returnWays = returnWays;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
