/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.returned.list;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.live.sdk.android.domain.*;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/04 16:13
 * @since 2.1.0
 */
public class LiveUserOrderReturnedListResponse extends ResponseResult {
    
    private ResultData data;
    
    public ResultData getData() {
        return data;
    }
    
    public void setData(ResultData data) {
        this.data = data;
    }
    
    
    public static class ResultData extends LiveSdkBasePageResponse {
        
        private List<ListData> list;
        
        public List<ListData> getList() {
            return list;
        }
        
        public void setList(List<ListData> list) {
            this.list = list;
        }
        
    }
    
    public static class ListData {
        /**
         * activityDiffPrice
         */
        private BigDecimal activityDiffPrice;
        /**
         * activityList
         */
        private List<CourseActivityDomain> activityList;
        /**
         * bankAddress
         */
        private String bankAddress;
        /**
         * cardNum
         */
        private String cardNum;
        /**
         * consumeAmount
         */
        private BigDecimal consumeAmount;
        /**
         * count
         */
        private int count;
        /**
         * couponList
         */
        private List<CourseCouponDomain> couponList;
        /**
         * createTime
         */
        private String createTime;
        /**
         * expressPrice
         */
        private BigDecimal expressPrice;
        /**
         * id
         */
        private int id;
        /**
         * notesPrice
         */
        private BigDecimal notesPrice;
        /**
         * orderId
         */
        private int orderId;
        /**
         * orderItemIds
         */
        private String orderItemIds;
        /**
         * orderNo
         */
        private String orderNo;
        /**
         * orderRefundDomain
         */
        private OrderRefundDomain orderRefundDomain;
        /**
         * orderReturnNo
         */
        private String orderReturnNo;
        /**
         * orderTradeNo
         */
        private String orderTradeNo;
        /**
         * price
         */
        private BigDecimal price;
        /**
         * reason
         */
        private String reason;
        /**
         * refundItems
         */
        private List<OrderReturnItemDomain> refundItems;
        /**
         * returnPrice
         */
        private BigDecimal returnPrice;
        /**
         * status
         */
        private int status;
        /**
         * userId
         */
        private int userId;
        
        //属性get||set方法
        
        
        public BigDecimal getActivityDiffPrice() {
            return this.activityDiffPrice;
        }
        
        public void setActivityDiffPrice(BigDecimal activityDiffPrice) {
            this.activityDiffPrice = activityDiffPrice;
        }
        
        
        public List<CourseActivityDomain> getActivityList() {
            return this.activityList;
        }
        
        public void setActivityList(List<CourseActivityDomain> activityList) {
            this.activityList = activityList;
        }
        
        
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
        
        
        public BigDecimal getConsumeAmount() {
            return this.consumeAmount;
        }
        
        public void setConsumeAmount(BigDecimal consumeAmount) {
            this.consumeAmount = consumeAmount;
        }
        
        
        public int getCount() {
            return this.count;
        }
        
        public void setCount(int count) {
            this.count = count;
        }
        
        
        public List<CourseCouponDomain> getCouponList() {
            return this.couponList;
        }
        
        public void setCouponList(List<CourseCouponDomain> couponList) {
            this.couponList = couponList;
        }
        
        
        public String getCreateTime() {
            return this.createTime;
        }
        
        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        
        
        public BigDecimal getExpressPrice() {
            return this.expressPrice;
        }
        
        public void setExpressPrice(BigDecimal expressPrice) {
            this.expressPrice = expressPrice;
        }
        
        
        public int getId() {
            return this.id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        
        public BigDecimal getNotesPrice() {
            return this.notesPrice;
        }
        
        public void setNotesPrice(BigDecimal notesPrice) {
            this.notesPrice = notesPrice;
        }
        
        
        public int getOrderId() {
            return this.orderId;
        }
        
        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }
        
        
        public String getOrderItemIds() {
            return this.orderItemIds;
        }
        
        public void setOrderItemIds(String orderItemIds) {
            this.orderItemIds = orderItemIds;
        }
        
        
        public String getOrderNo() {
            return this.orderNo;
        }
        
        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }
        
        
        public OrderRefundDomain getOrderRefundDomain() {
            return this.orderRefundDomain;
        }
        
        public void setOrderRefundDomain(OrderRefundDomain orderRefundDomain) {
            this.orderRefundDomain = orderRefundDomain;
        }
        
        
        public String getOrderReturnNo() {
            return this.orderReturnNo;
        }
        
        public void setOrderReturnNo(String orderReturnNo) {
            this.orderReturnNo = orderReturnNo;
        }
        
        
        public String getOrderTradeNo() {
            return this.orderTradeNo;
        }
        
        public void setOrderTradeNo(String orderTradeNo) {
            this.orderTradeNo = orderTradeNo;
        }
        
        
        public BigDecimal getPrice() {
            return this.price;
        }
        
        public void setPrice(BigDecimal price) {
            this.price = price;
        }
        
        
        public String getReason() {
            return this.reason;
        }
        
        public void setReason(String reason) {
            this.reason = reason;
        }
        
        
        public List<OrderReturnItemDomain> getRefundItems() {
            return this.refundItems;
        }
        
        public void setRefundItems(List<OrderReturnItemDomain> refundItems) {
            this.refundItems = refundItems;
        }
        
        
        public BigDecimal getReturnPrice() {
            return this.returnPrice;
        }
        
        public void setReturnPrice(BigDecimal returnPrice) {
            this.returnPrice = returnPrice;
        }
        
        
        public int getStatus() {
            return this.status;
        }
        
        public void setStatus(int status) {
            this.status = status;
        }
        
        
        public int getUserId() {
            return this.userId;
        }
        
        public void setUserId(int userId) {
            this.userId = userId;
        }
        
    }
}
