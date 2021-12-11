/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.returned.create;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**  创建退款单 返回值
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/11/09 15:38
 * @since 2.1.0
 */
public class LiveUserOrderReturnedCreateResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * 活动差值
        */
        private  BigDecimal activityDiffPrice;
        /**
        * 订单使用的活动
        */
        private  List<CourseActivityDomain> activityList;
        /**
        * 开户行地址
        */
        private  String bankAddress;
        /**
        * cardNum
        */
        private  String cardNum;
        /**
        * 课耗
        */
        private  BigDecimal consumeAmount;
        /**
        * 退款中数量
        */
        private  Integer count;
        /**
        * 订单使用的优惠券
        */
        private  List<CourseCouponDomain> couponList;
        /**
        * 申请退货时间
        */
        private  String createTime;
        /**
        * 快递总费用
        */
        private  BigDecimal expressPrice;
        /**
        * 退货单id
        */
        private  Integer id;
        /**
        * 讲义总费用
        */
        private  BigDecimal notesPrice;
        /**
        * 关联的订单id
        */
        private  Integer orderId;
        /**
        * 增加订单项的ids
        */
        private  String orderItemIds;
        /**
        * 订单orderNo
        */
        private  String orderNo;
        /**
        * 退货单
        */
        private  OrderRefundDomain orderRefundDomain;
        /**
        * 退货单编号
        */
        private  String orderReturnNo;
        /**
        * 订单交易流水号
        */
        private  String orderTradeNo;
        /**
        * 退款总金额（不计算快递费和讲义费的退款金额）
        */
        private  BigDecimal price;
        /**
        * 退款原因
        */
        private  String reason;
        /**
        * 退款单明细
        */
        private  List<OrderReturnItemDomain> refundItems;
        /**
        * 最终退款金额
        */
        private  BigDecimal returnPrice;
        /**
        * 应退金额中余额部分
        */
        private  BigDecimal returnPriceBalance;
        /**
        * 应退金额中现金部分
        */
        private  BigDecimal returnPriceCash;
        /**
        * 1：原路返回 2：退至余额 3：退至银行卡
        */
        private  Integer returnWays;
        /**
        *  退货状态（0：审核中，1：同意（退货中），2：已退货 ,-1：驳回，3：用户取消退款)
        */
        private  Integer status;

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



        public Integer getCount() {
        return this.count;
        }

        public void setCount(Integer count) {
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



        public Integer getId() {
        return this.id;
        }

        public void setId(Integer id) {
        this.id = id;
        }



        public BigDecimal getNotesPrice() {
        return this.notesPrice;
        }

        public void setNotesPrice(BigDecimal notesPrice) {
        this.notesPrice = notesPrice;
        }



        public Integer getOrderId() {
        return this.orderId;
        }

        public void setOrderId(Integer orderId) {
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



        public BigDecimal getReturnPriceBalance() {
        return this.returnPriceBalance;
        }

        public void setReturnPriceBalance(BigDecimal returnPriceBalance) {
        this.returnPriceBalance = returnPriceBalance;
        }



        public BigDecimal getReturnPriceCash() {
        return this.returnPriceCash;
        }

        public void setReturnPriceCash(BigDecimal returnPriceCash) {
        this.returnPriceCash = returnPriceCash;
        }



        public Integer getReturnWays() {
        return this.returnWays;
        }

        public void setReturnWays(Integer returnWays) {
        this.returnWays = returnWays;
        }



        public Integer getStatus() {
        return this.status;
        }

        public void setStatus(Integer status) {
        this.status = status;
        }

    }
}