/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.order.pre;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/28 17:05
 * @since 2.1.0
 */
public class LivePreCreateOrderResponse extends ResponseResult {
    
    private ResultData data;
    
    public ResultData getData() {
        return data;
    }
    
    public void setData(ResultData data) {
        this.data = data;
    }
    
    
    public static class ResultData implements Serializable {
        
        private OrderAddressDomain orderAddressDomain;
        
        public OrderAddressDomain getOrderAddressDomain() {
            return orderAddressDomain;
        }
        
        public void setOrderAddressDomain(OrderAddressDomain orderAddressDomain) {
            this.orderAddressDomain = orderAddressDomain;
        }
        
        /**
         * 使用活动减免总金额
         */
        private BigDecimal activityAmount;
        /**
         * 订单使用的活动
         */
        private List<CourseActivityDomain> activityList;
        /**
         * 使用余额抵扣
         */
        private BigDecimal balance;
        /**
         * 订单使用的优惠券
         */
        private List<CourseCouponDomain> couponList;
        /**
         * 应付金额(订单总金额+各项优惠 得出的结果)
         */
        private BigDecimal dueAmount;
        
        
        /**
         * 学科优惠券列表
         */
        private List<CourseCouponDomain> subjectCouponList;
        /**
         * 学科优惠券优惠金额
         */
        private BigDecimal subjectCouponPrice;
        
        /**
         * 活动优惠字符串显示
         */
        private String activityStr;
        
        public List<CourseCouponDomain> getSubjectCouponList() {
            return subjectCouponList;
        }
        
        public void setSubjectCouponList(List<CourseCouponDomain> subjectCouponList) {
            this.subjectCouponList = subjectCouponList;
        }
        
        public BigDecimal getSubjectCouponPrice() {
            return subjectCouponPrice;
        }
        
        public void setSubjectCouponPrice(BigDecimal subjectCouponPrice) {
            this.subjectCouponPrice = subjectCouponPrice;
        }
        
        public String getActivityStr() {
            return activityStr;
        }
        
        public void setActivityStr(String activityStr) {
            this.activityStr = activityStr;
        }
        
        /**
         * id
         */
        private int id;
        /**
         * 是否需要收货地址
         */
        private boolean needAddress;
        /**
         * 调价前应付金额(订单总金额+各项优惠 得出的结果)
         */
        private BigDecimal oldDueAmount;
        /**
         * 订单金额
         */
        private BigDecimal orderAmount;
        /**
         * 关联订单项
         */
        private List<OrderItemDomain> orderItemList;
        /**
         * 用户id
         */
        private int userId;
        
        //属性get||set方法
        
        
        public BigDecimal getActivityAmount() {
            if (activityAmount == null) {
                return BigDecimal.ZERO;
            }
            return activityAmount;
        }
        
        public void setActivityAmount(BigDecimal activityAmount) {
            this.activityAmount = activityAmount;
        }
        
        public List<CourseActivityDomain> getActivityList() {
            return activityList;
        }
        
        public void setActivityList(List<CourseActivityDomain> activityList) {
            this.activityList = activityList;
        }
        
        public BigDecimal getBalance() {
            return balance;
        }
        
        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }
        
        public List<CourseCouponDomain> getCouponList() {
            return couponList;
        }
        
        public void setCouponList(List<CourseCouponDomain> couponList) {
            this.couponList = couponList;
        }
        
        public BigDecimal getDueAmount() {
            return dueAmount;
        }
        
        public void setDueAmount(BigDecimal dueAmount) {
            this.dueAmount = dueAmount;
        }
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public boolean isNeedAddress() {
            return needAddress;
        }
        
        public void setNeedAddress(boolean needAddress) {
            needAddress = needAddress;
        }
        
        public BigDecimal getOldDueAmount() {
            return oldDueAmount;
        }
        
        public void setOldDueAmount(BigDecimal oldDueAmount) {
            this.oldDueAmount = oldDueAmount;
        }
        
        public BigDecimal getOrderAmount() {
            if(orderAmount==null){
                orderAmount= BigDecimal.ZERO;
            }
            return orderAmount;
        }
        
        public void setOrderAmount(BigDecimal orderAmount) {
            this.orderAmount = orderAmount;
        }
        
        public List<OrderItemDomain> getOrderItemList() {
            return orderItemList;
        }
        
        public void setOrderItemList(List<OrderItemDomain> orderItemList) {
            this.orderItemList = orderItemList;
        }
        
        public int getUserId() {
            return userId;
        }
        
        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
