/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.detail.orderNo;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/10 15:54
 * @since 2.1.0
 */
public class LiveUserOrderDetailOrderNOResponse extends ResponseResult {
    
    private ResultData data;
    
    public ResultData getData() {
        return data;
    }
    
    public void setData(ResultData data) {
        this.data = data;
    }
    
    
    public static class ResultData implements Serializable {
        
        /**
         * activityAmount
         */
        private BigDecimal activityAmount;
        /**
         * activityList
         */
        private List<CourseActivityDomain> activityList;
        /**
         * addressId
         */
        private String addressId;
        /**
         * adjustPrice
         */
        private BigDecimal adjustPrice;
        /**
         * amount
         */
        private BigDecimal amount;
        /**
         * balance
         */
        private BigDecimal balance;
        /**
         * bankSeqNo
         */
        private String bankSeqNo;
        /**
         * cancelTime
         */
        private String cancelTime;
        /**
         * cartIds
         */
        private String cartIds;
        /**
         * changeReason
         */
        private String changeReason;
        /**
         * consumeAmount
         */
        private BigDecimal consumeAmount;
        /**
         * couponAmount
         */
        private BigDecimal couponAmount;
        /**
         * couponList
         */
        private List<CourseCouponDomain> couponList;
        /**
         * createTime
         */
        private String createTime;
        /**
         * currentConsumePrice
         */
        private BigDecimal currentConsumePrice;
        /**
         * dataInfo
         */
        private String dataInfo;
        /**
         * dueAmount
         */
        private BigDecimal dueAmount;
        /**
         * expireStatus
         */
        private int expireStatus;
        /**
         * hasNoPayOrder
         */
        private boolean hasNoPayOrder;
        /**
         * id
         */
        private int id;
        /**
         * keyWord
         */
        private String keyWord;
        /**
         * lastUpdateTime
         */
        private String lastUpdateTime;
        /**
         * latestRefundTime
         */
        private String latestRefundTime;
        /**
         * materialCost
         */
        private BigDecimal materialCost;
        /**
         * needAddress
         */
        //        private  boolean needAddress;
        /**
         * noReturnShouldPayAmount
         */
        private BigDecimal noReturnShouldPayAmount;
        /**
         * oldDueAmount
         */
        private BigDecimal oldDueAmount;
        /**
         * orderAddressDomain
         */
        private OrderAddressDomain orderAddressDomain;
        /**
         * orderAmount
         */
        private BigDecimal orderAmount;
        /**
         * orderCouponPrice
         */
        private BigDecimal orderCouponPrice;
        /**
         * orderHasUseActivityList
         */
        //        private Set<String> orderHasUseActivityList;
        /**
         * orderItemList
         */
        private List<OrderItemDomain> orderItemList;
        /**
         * orderNo
         */
        private String orderNo;
        /**
         * orderSeq
         */
        private int orderSeq;
        /**
         * outChannelType
         */
        private String outChannelType;
        /**
         * partnerId
         */
        private String partnerId;
        /**
         * payStartTime
         */
        private String payStartTime;
        /**
         * payTime
         */
        private String payTime;
        /**
         * payType
         */
        private int payType;
        /**
         * payWayId  1支付宝 2微信
         */
        private int payWayId;
        /**
         * refundable
         */
        private boolean refundable;
        /**
         * retiredAmount
         */
        private BigDecimal retiredAmount;
        /**
         * returnBalance
         */
        private BigDecimal returnBalance;
        /**
         * returnItemTotalPayAmount
         */
        private BigDecimal returnItemTotalPayAmount;
        /**
         * returnPrice
         */
        private BigDecimal returnPrice;
        /**
         * returnPriceWithOutMaterial
         */
        private BigDecimal returnPriceWithOutMaterial;
        /**
         * schoolId
         */
        private int schoolId;
        /**
         * serialVersionUID
         */
        private Long serialVersionUID;
        /**
         * shippingStatus
         */
        private int shippingStatus;
        /**
         * status
         */
        private int status;
        /**
         * totalMaterialAmount
         */
        private BigDecimal totalMaterialAmount;
        /**
         * tradeNo
         */
        private String tradeNo;
        /**
         * userId
         */
        private int userId;
        
        //属性get||set方法
        
        
        public BigDecimal getActivityAmount() {
            return this.activityAmount;
        }
        
        public void setActivityAmount(BigDecimal activityAmount) {
            this.activityAmount = activityAmount;
        }
        
        
        public List<CourseActivityDomain> getActivityList() {
            return this.activityList;
        }
        
        public void setActivityList(List<CourseActivityDomain> activityList) {
            this.activityList = activityList;
        }
        
        
        public String getAddressId() {
            return this.addressId;
        }
        
        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }
        
        
        public BigDecimal getAdjustPrice() {
            return this.adjustPrice;
        }
        
        public void setAdjustPrice(BigDecimal adjustPrice) {
            this.adjustPrice = adjustPrice;
        }
        
        
        public BigDecimal getAmount() {
            return this.amount;
        }
        
        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
        
        
        public BigDecimal getBalance() {
            return this.balance;
        }
        
        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }
        
        
        public String getBankSeqNo() {
            return this.bankSeqNo;
        }
        
        public void setBankSeqNo(String bankSeqNo) {
            this.bankSeqNo = bankSeqNo;
        }
        
        
        public String getCancelTime() {
            return this.cancelTime;
        }
        
        public void setCancelTime(String cancelTime) {
            this.cancelTime = cancelTime;
        }
        
        
        public String getCartIds() {
            return this.cartIds;
        }
        
        public void setCartIds(String cartIds) {
            this.cartIds = cartIds;
        }
        
        
        public String getChangeReason() {
            return this.changeReason;
        }
        
        public void setChangeReason(String changeReason) {
            this.changeReason = changeReason;
        }
        
        
        public BigDecimal getConsumeAmount() {
            return this.consumeAmount;
        }
        
        public void setConsumeAmount(BigDecimal consumeAmount) {
            this.consumeAmount = consumeAmount;
        }
        
        
        public BigDecimal getCouponAmount() {
            return this.couponAmount;
        }
        
        public void setCouponAmount(BigDecimal couponAmount) {
            this.couponAmount = couponAmount;
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
        
        
        public BigDecimal getCurrentConsumePrice() {
            return this.currentConsumePrice;
        }
        
        public void setCurrentConsumePrice(BigDecimal currentConsumePrice) {
            this.currentConsumePrice = currentConsumePrice;
        }
        
        
        public String getDataInfo() {
            return this.dataInfo;
        }
        
        public void setDataInfo(String dataInfo) {
            this.dataInfo = dataInfo;
        }
        
        
        public BigDecimal getDueAmount() {
            return this.dueAmount;
        }
        
        public void setDueAmount(BigDecimal dueAmount) {
            this.dueAmount = dueAmount;
        }
        
        
        public int getExpireStatus() {
            return this.expireStatus;
        }
        
        public void setExpireStatus(int expireStatus) {
            this.expireStatus = expireStatus;
        }
        
        
        public boolean getHasNoPayOrder() {
            return this.hasNoPayOrder;
        }
        
        public void setHasNoPayOrder(boolean hasNoPayOrder) {
            this.hasNoPayOrder = hasNoPayOrder;
        }
        
        
        public int getId() {
            return this.id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        
        public String getKeyWord() {
            return this.keyWord;
        }
        
        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }
        
        
        public String getLastUpdateTime() {
            return this.lastUpdateTime;
        }
        
        public void setLastUpdateTime(String lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }
        
        
        public String getLatestRefundTime() {
            return this.latestRefundTime;
        }
        
        public void setLatestRefundTime(String latestRefundTime) {
            this.latestRefundTime = latestRefundTime;
        }
        
        
        public BigDecimal getMaterialCost() {
            return this.materialCost;
        }
        
        public void setMaterialCost(BigDecimal materialCost) {
            this.materialCost = materialCost;
        }
        
        
        //        public boolean getNeedAddress() {
        //        return this.needAddress;
        //        }
        //
        //        public void setNeedAddress(boolean needAddress) {
        //        this.needAddress = needAddress;
        //        }
        
        
        public BigDecimal getNoReturnShouldPayAmount() {
            return this.noReturnShouldPayAmount;
        }
        
        public void setNoReturnShouldPayAmount(BigDecimal noReturnShouldPayAmount) {
            this.noReturnShouldPayAmount = noReturnShouldPayAmount;
        }
        
        
        public BigDecimal getOldDueAmount() {
            return this.oldDueAmount;
        }
        
        public void setOldDueAmount(BigDecimal oldDueAmount) {
            this.oldDueAmount = oldDueAmount;
        }
        
        
        public OrderAddressDomain getOrderAddressDomain() {
            return this.orderAddressDomain;
        }
        
        public void setOrderAddressDomain(OrderAddressDomain orderAddressDomain) {
            this.orderAddressDomain = orderAddressDomain;
        }
        
        
        public BigDecimal getOrderAmount() {
            return this.orderAmount;
        }
        
        public void setOrderAmount(BigDecimal orderAmount) {
            this.orderAmount = orderAmount;
        }
        
        
        public BigDecimal getOrderCouponPrice() {
            return this.orderCouponPrice;
        }
        
        public void setOrderCouponPrice(BigDecimal orderCouponPrice) {
            this.orderCouponPrice = orderCouponPrice;
        }
        
        
        //        public Set<String> getOrderHasUseActivityList() {
        //            return this.orderHasUseActivityList;
        //        }
        //
        //        public void setOrderHasUseActivityList(Set<String> orderHasUseActivityList) {
        //            this.orderHasUseActivityList = orderHasUseActivityList;
        //        }
        
        
        public List<OrderItemDomain> getOrderItemList() {
            return this.orderItemList;
        }
        
        public void setOrderItemList(List<OrderItemDomain> orderItemList) {
            this.orderItemList = orderItemList;
        }
        
        
        public String getOrderNo() {
            return this.orderNo;
        }
        
        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }
        
        
        public int getOrderSeq() {
            return this.orderSeq;
        }
        
        public void setOrderSeq(int orderSeq) {
            this.orderSeq = orderSeq;
        }
        
        
        public String getOutChannelType() {
            return this.outChannelType;
        }
        
        public void setOutChannelType(String outChannelType) {
            this.outChannelType = outChannelType;
        }
        
        
        public String getPartnerId() {
            return this.partnerId;
        }
        
        public void setPartnerId(String partnerId) {
            this.partnerId = partnerId;
        }
        
        
        public String getPayStartTime() {
            return this.payStartTime;
        }
        
        public void setPayStartTime(String payStartTime) {
            this.payStartTime = payStartTime;
        }
        
        
        public String getPayTime() {
            return this.payTime;
        }
        
        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }
        
        
        public int getPayType() {
            return this.payType;
        }
        
        public void setPayType(int payType) {
            this.payType = payType;
        }
        
        
        public int getPayWayId() {
            return this.payWayId;
        }
        
        public void setPayWayId(int payWayId) {
            this.payWayId = payWayId;
        }
        
        
        public boolean getRefundable() {
            return this.refundable;
        }
        
        public void setRefundable(boolean refundable) {
            this.refundable = refundable;
        }
        
        
        public BigDecimal getRetiredAmount() {
            return this.retiredAmount;
        }
        
        public void setRetiredAmount(BigDecimal retiredAmount) {
            this.retiredAmount = retiredAmount;
        }
        
        
        public BigDecimal getReturnBalance() {
            return this.returnBalance;
        }
        
        public void setReturnBalance(BigDecimal returnBalance) {
            this.returnBalance = returnBalance;
        }
        
        
        public BigDecimal getReturnItemTotalPayAmount() {
            return this.returnItemTotalPayAmount;
        }
        
        public void setReturnItemTotalPayAmount(BigDecimal returnItemTotalPayAmount) {
            this.returnItemTotalPayAmount = returnItemTotalPayAmount;
        }
        
        
        public BigDecimal getReturnPrice() {
            return this.returnPrice;
        }
        
        public void setReturnPrice(BigDecimal returnPrice) {
            this.returnPrice = returnPrice;
        }
        
        
        public BigDecimal getReturnPriceWithOutMaterial() {
            return this.returnPriceWithOutMaterial;
        }
        
        public void setReturnPriceWithOutMaterial(BigDecimal returnPriceWithOutMaterial) {
            this.returnPriceWithOutMaterial = returnPriceWithOutMaterial;
        }
        
        
        public int getSchoolId() {
            return this.schoolId;
        }
        
        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }
        
        
        public Long getSerialVersionUID() {
            return this.serialVersionUID;
        }
        
        public void setSerialVersionUID(Long serialVersionUID) {
            this.serialVersionUID = serialVersionUID;
        }
        
        
        public int getShippingStatus() {
            return this.shippingStatus;
        }
        
        public void setShippingStatus(int shippingStatus) {
            this.shippingStatus = shippingStatus;
        }
        
        
        public int getStatus() {
            return this.status;
        }
        
        public void setStatus(int status) {
            this.status = status;
        }
        
        
        public BigDecimal getTotalMaterialAmount() {
            return this.totalMaterialAmount;
        }
        
        public void setTotalMaterialAmount(BigDecimal totalMaterialAmount) {
            this.totalMaterialAmount = totalMaterialAmount;
        }
        
        
        public String getTradeNo() {
            return this.tradeNo;
        }
        
        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }
        
        
        public int getUserId() {
            return this.userId;
        }
        
        public void setUserId(int userId) {
            this.userId = userId;
        }
        
    }
}
