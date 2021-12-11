/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.order.coupon.use;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/29 19:15
 * @since 2.1.0
 */
public class LiveOrderCouponUseResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * activityAmount
        */
        private  BigDecimal activityAmount;
        /**
        * activityList
        */
//        private  List<CourseActivityDomain> activityList;
        /**
        * addressId
        */
        private  String addressId;
        /**
        * adjustPrice
        */
        private  BigDecimal adjustPrice;
        /**
        * amount
        */
        private  BigDecimal amount;
        /**
        * balance
        */
        private  BigDecimal balance;
        /**
        * bankSeqNo
        */
        private  String bankSeqNo;
        /**
        * cancelTime
        */
        private  String cancelTime;
        /**
        * cartIds
        */
        private  String cartIds;
        /**
        * changeReason
        */
        private  String changeReason;
        /**
        * consumeAmount
        */
        private  BigDecimal consumeAmount;
        /**
        * couponAmount
        */
        private  BigDecimal couponAmount;
        /**
        * couponList
        */
//        private  List<CourseCouponDomain> couponList;
        /**
        * createTime
        */
        private  String createTime;
        /**
        * currentConsumePrice
        */
        private  BigDecimal currentConsumePrice;
        /**
        * dataInfo
        */
        private  String dataInfo;
        /**
        * dueAmount
        */
        private  BigDecimal dueAmount;
        /**
        * expireStatus
        */
        private  Integer expireStatus;
        /**
        * hasNoPayOrder
        */
        private  Boolean hasNoPayOrder;
        /**
        * id
        */
        private  Integer id;
        /**
        * isNeedAddress
        */
        private  Boolean isNeedAddress;
        /**
        * keyWord
        */
        private  String keyWord;
        /**
        * lastUpdateTime
        */
        private  String lastUpdateTime;
        /**
        * latestRefundTime
        */
        private  String latestRefundTime;
        /**
        * materialCost
        */
        private  BigDecimal materialCost;
        /**
        * noReturnShouldPayAmount
        */
        private  BigDecimal noReturnShouldPayAmount;
        /**
        * oldDueAmount
        */
        private  BigDecimal oldDueAmount;
        /**
        * orderAddressDomain
        */
//        private  OrderAddressDomain orderAddressDomain;
        /**
        * orderAmount
        */
        private  BigDecimal orderAmount;
        /**
        * orderCouponPrice
        */
        private  BigDecimal orderCouponPrice;
        /**
        * orderHasUseActivityList
        */
//        private  Set<String> orderHasUseActivityList;
        /**
        * orderItemList
        */
//        private  List<OrderItemDomain> orderItemList;
        /**
        * orderNo
        */
        private  String orderNo;
        /**
        * orderSeq
        */
        private  Integer orderSeq;
        /**
        * outChannelType
        */
        private  String outChannelType;
        /**
        * partnerId
        */
        private  String partnerId;
        /**
        * payStartTime
        */
        private  String payStartTime;
        /**
        * payTime
        */
        private  String payTime;
        /**
        * payType
        */
        private  Integer payType;
        /**
        * payWayId
        */
        private  Integer payWayId;
        /**
        * refundable
        */
        private  Boolean refundable;
        /**
        * retiredAmount
        */
        private  BigDecimal retiredAmount;
        /**
        * returnBalance
        */
        private  BigDecimal returnBalance;
        /**
        * returnItemTotalPayAmount
        */
        private  BigDecimal returnItemTotalPayAmount;
        /**
        * returnPrice
        */
        private  BigDecimal returnPrice;
        /**
        * returnPriceWithOutMaterial
        */
        private  BigDecimal returnPriceWithOutMaterial;
        /**
        * schoolId
        */
        private  Integer schoolId;
        /**
        * serialVersionUID
        */
        private  Long serialVersionUID;
        /**
        * shippingStatus
        */
        private  Integer shippingStatus;
        /**
        * status
        */
        private  Integer status;
        /**
        * totalMaterialAmount
        */
        private  BigDecimal totalMaterialAmount;
        /**
        * tradeNo
        */
        private  String tradeNo;
        /**
        * userId
        */
        private  Integer userId;

        //属性get||set方法


        public BigDecimal getActivityAmount() {
        return this.activityAmount;
        }

        public void setActivityAmount(BigDecimal activityAmount) {
        this.activityAmount = activityAmount;
        }








//        public List<CourseActivityDomain> getActivityList() {
//            return this.activityList;
//        }
//
//        public void setActivityList(List<CourseActivityDomain> activityList) {
//            this.activityList = activityList;
//        }







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




//        public List<CourseCouponDomain> getCouponList() {
//            return this.couponList;
//        }
//
//        public void setCouponList(List<CourseCouponDomain> couponList) {
//            this.couponList = couponList;
//        }



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



        public Integer getExpireStatus() {
        return this.expireStatus;
        }

        public void setExpireStatus(Integer expireStatus) {
        this.expireStatus = expireStatus;
        }



        public Boolean getHasNoPayOrder() {
        return this.hasNoPayOrder;
        }

        public void setHasNoPayOrder(Boolean hasNoPayOrder) {
        this.hasNoPayOrder = hasNoPayOrder;
        }



        public Integer getId() {
        return this.id;
        }

        public void setId(Integer id) {
        this.id = id;
        }



        public Boolean getIsNeedAddress() {
        return this.isNeedAddress;
        }

        public void setIsNeedAddress(Boolean isNeedAddress) {
        this.isNeedAddress = isNeedAddress;
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



//        public OrderAddressDomain getOrderAddressDomain() {
//        return this.orderAddressDomain;
//        }
//
//        public void setOrderAddressDomain(OrderAddressDomain orderAddressDomain) {
//        this.orderAddressDomain = orderAddressDomain;
//        }



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








//        public List<OrderItemDomain> getOrderItemList() {
//            return this.orderItemList;
//        }
//
//        public void setOrderItemList(List<OrderItemDomain> orderItemList) {
//            this.orderItemList = orderItemList;
//        }



        public String getOrderNo() {
        return this.orderNo;
        }

        public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        }



        public Integer getOrderSeq() {
        return this.orderSeq;
        }

        public void setOrderSeq(Integer orderSeq) {
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



        public Integer getPayType() {
        return this.payType;
        }

        public void setPayType(Integer payType) {
        this.payType = payType;
        }



        public Integer getPayWayId() {
        return this.payWayId;
        }

        public void setPayWayId(Integer payWayId) {
        this.payWayId = payWayId;
        }



        public Boolean getRefundable() {
        return this.refundable;
        }

        public void setRefundable(Boolean refundable) {
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



        public Integer getSchoolId() {
        return this.schoolId;
        }

        public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
        }



        public Long getSerialVersionUID() {
        return this.serialVersionUID;
        }

        public void setSerialVersionUID(Long serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
        }



        public Integer getShippingStatus() {
        return this.shippingStatus;
        }

        public void setShippingStatus(Integer shippingStatus) {
        this.shippingStatus = shippingStatus;
        }



        public Integer getStatus() {
        return this.status;
        }

        public void setStatus(Integer status) {
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



        public Integer getUserId() {
        return this.userId;
        }

        public void setUserId(Integer userId) {
        this.userId = userId;
        }

    }
}
