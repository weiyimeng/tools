/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.exchange.create;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**  转班生成订单 返回值
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/11/09 15:38
 * @since 2.1.0
 */
public class LiveUserOrderExchangeCreateResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * 使用活动减免总金额
        */
        private  BigDecimal activityAmount;
        /**
        * 订单使用的活动
        */
        private  List<CourseActivityDomain> activityList;
        /**
        * 创建订单使用
        */
        private  String addressId;
        /**
        * 调整后的价格
        */
        private  BigDecimal adjustPrice;
        /**
        * 支付金额
        */
        private  BigDecimal amount;
        /**
        * 使用余额抵扣
        */
        private  BigDecimal balance;
        /**
        * bankSeqNo
        */
        private  String bankSeqNo;
        /**
        * 取消时间
        */
        private  String cancelTime;
        /**
        * 创建订单使用
        */
        private  String cartIds;
        /**
        * 换该单原因
        */
        private  String changeReason;
        /**
        * 课耗已使用的
        */
        private  BigDecimal consumeAmount;
        /**
        * 使用优惠券减免总金额
        */
        private  BigDecimal couponAmount;
        /**
        * 订单使用的优惠券
        */
        private  List<CourseCouponDomain> couponList;
        /**
        * 创建时间
        */
        private  String createTime;
        /**
        * 当前课耗
        */
        private  BigDecimal currentConsumePrice;
        /**
        * 创建订单使用
        */
        private  String dataInfo;
        /**
        * 应付金额(订单总金额+各项优惠 得出的结果)
        */
        private  BigDecimal dueAmount;
        /**
        * 1 不过期，0过期  默认不过期 退款使用
        */
        private  int expireStatus;
        /**
        * 是否还有未支付订单
        */
        private  boolean hasNoPayOrder;
        /**
        * 订单id
        */
        private  int id;
        /**
        * latestRefundTime
        */
        private  String latestRefundTime;
        /**
        * 物料成本
        */
        private  BigDecimal materialCost;
        /**
        * 是否需要收货地址
        */
        private  boolean needAddress;
        /**
        *
        */
        private  BigDecimal noReturnShouldPayAmount;
        /**
        * nowTime
        */
        private  String nowTime;
        /**
        * 调价前应付金额
        */
        private  BigDecimal oldDueAmount;
        /**
        * 订单收货地址
        */
        private  OrderAddressDomain orderAddressDomain;
        /**
        * 订单金额
        */
        private  BigDecimal orderAmount;
        /**
        * 使用订单优惠券优惠的金额
        */
        private  BigDecimal orderCouponPrice;
        /**
        * 关联订单项
        */
        private  List<OrderItemDomain> orderItemList;
        /**
        * 订单编号
        */
        private  String orderNo;
        /**
        * orderSeq
        */
        private  int orderSeq;
        /**
        * outChannelType
        */
        private  String outChannelType;
        /**
        * partnerId
        */
        private  String partnerId;
        /**
        * 支付完成时间
        */
        private  String payTime;
        /**
        * payType
        */
        private  int payType;
        /**
        * 支付方式(1:支付宝,2:微信 )
        */
        private  int payWayId;
        /**
        * 订单是否可退
        */
        private  boolean refundable;
        /**
        * 已退款的金额
        */
        private  BigDecimal retiredAmount;
        /**
        * 换单后或退款时应退至余额的钱
        */
        private  BigDecimal returnBalance;
        /**
        * 预退订单项的是实付金额
        */
        private  BigDecimal returnItemTotalPayAmount;
        /**
        * 订单应退金额
        */
        private  BigDecimal returnPrice;
        /**
        * 不含资料服务费的退款金额
        */
        private  BigDecimal returnPriceWithOutMaterial;
        /**
        * schoolId
        */
        private  int schoolId;
        /**
        * serialVersionUID
        */
        private  long serialVersionUID;
        /**
        * 发货状态（0，未发货，1已全发货，2，部分发货 3，已全部退货，4部分退货，）
        */
        private  int shippingStatus;
        /**
        * 0  未付款 1 已付款  -1  已取消, 2 支付中,3 退款中,4 全部退款，5部分退款,6:换货中，8：换货完成，9：换货驳回
        */
        private  int status;
        /**
        * 扣除的资料费用
        */
        private  BigDecimal totalMaterialAmount;

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



        public boolean getNeedAddress() {
        return this.needAddress;
        }

        public void setNeedAddress(boolean needAddress) {
        this.needAddress = needAddress;
        }



        public BigDecimal getNoReturnShouldPayAmount() {
        return this.noReturnShouldPayAmount;
        }

        public void setNoReturnShouldPayAmount(BigDecimal noReturnShouldPayAmount) {
        this.noReturnShouldPayAmount = noReturnShouldPayAmount;
        }



        public String getNowTime() {
        return this.nowTime;
        }

        public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
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



        public long getSerialVersionUID() {
        return this.serialVersionUID;
        }

        public void setSerialVersionUID(long serialVersionUID) {
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

    }
}
