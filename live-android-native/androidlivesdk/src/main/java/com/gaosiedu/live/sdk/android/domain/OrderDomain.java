/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import com.gaosiedu.live.sdk.android.domain.CourseActivityDomain;
import com.gaosiedu.live.sdk.android.domain.CourseCouponDomain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 订单
 *
 * @author lixun
 * @describe
 * @date 2017/7/25 17:51
 * @since 2.1.0
 */
public class OrderDomain implements Serializable {

    /**
     * 是都需要收货地址
     */

    private boolean isNeedAddress;


    /**
     * 是否还有未支付订单
     */

    private boolean hasNoPayOrder = false;


    /**
     * 使用订单优惠券优惠的金额
     */
    private BigDecimal orderCouponPrice;

    /**
     * 订单收货地址
     */
    private OrderAddressDomain orderAddressDomain;
    /**
     * 订单是否可退
     */
    private boolean refundable;
    /**
     * 订单应退金额
     */
        private BigDecimal returnPrice;
    /**
     * 订单使用的优惠券
     */
    private List<CourseCouponDomain> couponList;

    /**
     * 订单使用的活动
     */
    private List<CourseActivityDomain> activityList;

    /**
     * 关联订单项
     */
    private List<OrderItemDomain> orderItemList;
    /**
     * 创建订单使用
     */
    private String dataInfo;
    /**
     * 创建订单使用
     */
    private String cartIds;
    /**
     * 创建订单使用
     */
    private String addressId;

    /**
     * 搜索关键字
     */
    private String keyWord;

    /**
     * 订单id
     */
    private int id;

    /**
     * 订单编号(ajz+时间+9位序列)
     */
    private String orderNo;

    /**
     * 当日订单序列号
     */
    private int orderSeq;

    /**
     * 用id
     */
    private int userId;

    /**
     * 合伙人id(支付宝)
     */
    private String partnerId;

    /**
     * 支付方式(1:支付宝,2:微信 )
     */
    private int payWayId;

    /**
     * 支付流水号
     */
    private String tradeNo;

    /**
     * 0  未付款 1 已付款  -1  已取消, 2 支付中,3 退款中,4 全部退款，5部分退款,6:换货中，8：换货完成，9：换货驳回
     */
    private int status;

    /**
     * 应付金额(订单总金额+各项优惠 得出的结果)
     */
    private BigDecimal dueAmount = new BigDecimal(0.00);

    /**
     * 订单金额
     */
    private BigDecimal orderAmount = new BigDecimal(0.00);

    /**
     * 调价前应付金额(订单总金额+各项优惠 得出的结果)
     */
    private BigDecimal oldDueAmount = new BigDecimal(0.00);

    /**
     * 使用优惠券减免总金额
     */
    private BigDecimal couponAmount = new BigDecimal(0.00);

    /**
     * 使用活动减免总金额
     */
    private BigDecimal activityAmount;

    /**
     * 支付金额
     */
    private BigDecimal amount = new BigDecimal(0.00);

    /**
     * 已退款的金额
     */
    private BigDecimal retiredAmount = BigDecimal.ZERO;

    /**
     * 使用余额抵扣
     */
    private BigDecimal balance = new BigDecimal(0.00);

    /**
     * 调整后的价格
     */
    private BigDecimal adjustPrice = new BigDecimal(0.00);

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 支付完成时间
     */
    private Date payTime;

    /**
     * 支付开始时间
     */
    private Date payStartTime;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 支付渠道类型(1支付宝，2微信)
     */
    private String outChannelType;

    /**
     * 银行流水号
     */
    private String bankSeqNo;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    /**
     * 可以申请退款的最后时间
     */
    private Date latestRefundTime;

    /**
     * 发货状态（0，未发货，1已全发货，2，部分发货 3，已全部退货，4部分退货，）
     */
    private int shippingStatus;

    /**
     * 订单创建时关联学校id
     */
    private int schoolId;

    /**
     * 支付终端（1.pc 2mobile）
     */
    private int payType;

    /**
     * 物料成本
     */
    private BigDecimal materialCost;

    /**
     * 退款时候用于记录每一个订单项的计算应付金额
     */
    private Map<String, Object> orderItemIdToPayable;


    /**
     * 订单已使用的优惠
     */
    private Set<String> orderHasUseActivityList = new HashSet<>();

    /**
     * 活动与订单项绑定关系map
     */
    private Map<String, List<OrderItemDomain>> activityBindOrderItem;


    /**
     * 活动减免金额
     */
    private Map<String, CourseActivityDomain> activityReturnAmountMap;


    /**
     * 物料成本
     */
    private BigDecimal noReturnShouldPayAmount = BigDecimal.ZERO;


    /**
     * 预退订单项的是实付金额
     */
    private BigDecimal returnItemTotalPayAmount = BigDecimal.ZERO;


    /**
     * 当前课耗
     */
    private  BigDecimal currentConsumePrice = BigDecimal.ZERO;

    /**
     * 课耗已使用的
     */
    private BigDecimal consumeAmount;
    /**
     * 换该单原因
     */
    private String changeReason;
    /**
     * 换单后或退款时应退至余额的钱
     */
    private BigDecimal returnBalance = new BigDecimal(0.00);


    /**
     * 不含资料服务费的退款金额
     */
    private  BigDecimal returnPriceWithOutMaterial = new BigDecimal(0.00);


    /**
     * 扣除的资料费用
     */
    private BigDecimal totalMaterialAmount = BigDecimal.ZERO;


    /**
     * orders
     */
    private static final long serialVersionUID = 1L;
    /**
     * 判断是否超过订单退款期 支付宝3个月 微信6个月
     * 1 不过期，0过期  默认不过期 退款使用
     */
    private int expireStatus = 1;
    /**
     * 订单下学科优惠券
     */
    private List<CourseCouponDomain> subjectCouponList;

    private BigDecimal subjectCouponPrice;

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

    public int getExpireStatus() {
        return expireStatus;
    }

    public void setExpireStatus(int expireStatus) {
        this.expireStatus = expireStatus;
    }

    public BigDecimal getReturnBalance() {
        return returnBalance;
    }

    public void setReturnBalance(BigDecimal returnBalance) {
        this.returnBalance = returnBalance;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public OrderAddressDomain getOrderAddressDomain() {
        return orderAddressDomain;
    }

    public void setOrderAddressDomain(OrderAddressDomain orderAddressDomain) {
        this.orderAddressDomain = orderAddressDomain;
    }

    public String getDataInfo() {
        return dataInfo;
    }

    public void setDataInfo(String dataInfo) {
        this.dataInfo = dataInfo;
    }

    public String getCartIds() {
        return cartIds;
    }

    public void setCartIds(String cartIds) {
        this.cartIds = cartIds;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 订单编号(ajz+时间+9位序列)
     *
     * @return order_no 订单编号(ajz+时间+9位序列)
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 订单编号(ajz+时间+9位序列)
     *
     * @param orderNo 订单编号(ajz+时间+9位序列)
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    /**
     * 当日订单序列号
     *
     * @return order_seq 当日订单序列号
     */
    public int getOrderSeq() {
        return orderSeq;
    }

    /**
     * 当日订单序列号
     *
     * @param orderSeq 当日订单序列号
     */
    public void setOrderSeq(int orderSeq) {
        this.orderSeq = orderSeq;
    }

    /**
     * 用id
     *
     * @return user_id 用id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * 用id
     *
     * @param userId 用id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * 合伙人id(支付宝)
     *
     * @return partner_id 合伙人id(支付宝)
     */
    public String getPartnerId() {
        return partnerId;
    }

    /**
     * 合伙人id(支付宝)
     *
     * @param partnerId 合伙人id(支付宝)
     */
    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId == null ? null : partnerId.trim();
    }

    /**
     * 支付方式(1:支付宝,2:微信 )
     *
     * @return pay_way_id 支付方式(1:支付宝,2:微信 )
     */
    public int getPayWayId() {
        return payWayId;
    }

    /**
     * 支付方式(1:支付宝,2:微信 )
     *
     * @param payWayId 支付方式(1:支付宝,2:微信 )
     */
    public void setPayWayId(int payWayId) {
        this.payWayId = payWayId;
    }

    /**
     * 支付流水号
     *
     * @return trade_no 支付流水号
     */
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * 支付流水号
     *
     * @param tradeNo 支付流水号
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    /**
     * 0  未付款 1 已付款  -1  已取消, 2 支付中,3 退款中,4 全部退款，5部分退款,6:换货中，8：换货完成，9：换货驳回
     *
     * @return status 0  未付款 1 已付款  -1  已取消, 2 支付中,3 退款中,4 全部退款，5部分退款,6:换货中，8：换货完成，9：换货驳回
     */
    public int getStatus() {
        return status;
    }

    /**
     * 0  未付款 1 已付款  -1  已取消, 2 支付中,3 退款中,4 全部退款，5部分退款,6:换货中，8：换货完成，9：换货驳回
     *
     * @param status 0  未付款 1 已付款  -1  已取消, 2 支付中,3 退款中,4 全部退款，5部分退款,6:换货中，8：换货完成，9：换货驳回
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 应付金额(订单总金额+各项优惠 得出的结果)
     *
     * @return due_amount 应付金额(订单总金额+各项优惠 得出的结果)
     */
    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    /**
     * 应付金额(订单总金额+各项优惠 得出的结果)
     *
     * @param dueAmount 应付金额(订单总金额+各项优惠 得出的结果)
     */
    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    /**
     * 订单金额
     *
     * @return order_amount 订单金额
     */
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    /**
     * 订单金额
     *
     * @param orderAmount 订单金额
     */
    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    /**
     * 调价前应付金额(订单总金额+各项优惠 得出的结果)
     *
     * @return old_due_amount 调价前应付金额(订单总金额+各项优惠 得出的结果)
     */
    public BigDecimal getOldDueAmount() {
        return formatPriceWithBigDecimal(oldDueAmount);
    }

    /**
     * 调价前应付金额(订单总金额+各项优惠 得出的结果)
     *
     * @param oldDueAmount 调价前应付金额(订单总金额+各项优惠 得出的结果)
     */
    public void setOldDueAmount(BigDecimal oldDueAmount) {
        this.oldDueAmount = oldDueAmount;
    }

    /**
     * 使用优惠券减免总金额
     *
     * @return coupon_amount 使用优惠券减免总金额
     */
    public BigDecimal getCouponAmount() {
        return formatPriceWithBigDecimal(couponAmount);
    }

    /**
     * 使用优惠券减免总金额
     *
     * @param couponAmount 使用优惠券减免总金额
     */
    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = formatPriceWithBigDecimal(couponAmount);
    }

    public BigDecimal getActivityAmount() {
        return activityAmount;
    }

    public void setActivityAmount(BigDecimal activityAmount) {
        this.activityAmount = activityAmount;
    }

    /**
     * 支付金额
     *
     * @return amount 支付金额
     */
    public BigDecimal getAmount() {
        return formatPriceWithBigDecimal(amount);
    }

    /**
     * 支付金额
     *
     * @param amount 支付金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 使用余额抵扣
     *
     * @return balance 使用余额抵扣
     */
    public BigDecimal getBalance() {
        return formatPriceWithBigDecimal(balance);
    }

    /**
     * 使用余额抵扣
     *
     * @param balance 使用余额抵扣
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * 调整后的价格
     *
     * @return adjust_price 调整后的价格
     */
    public BigDecimal getAdjustPrice() {
        return formatPriceWithBigDecimal(adjustPrice);
    }

    /**
     * 调整后的价格
     *
     * @param adjustPrice 调整后的价格
     */
    public void setAdjustPrice(BigDecimal adjustPrice) {
        this.adjustPrice = adjustPrice;
    }

    /**
     * 创建时间
     *
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 支付完成时间
     *
     * @return pay_time 支付完成时间
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * 支付完成时间
     *
     * @param payTime 支付完成时间
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * 支付开始时间
     *
     * @return pay_start_time 支付开始时间
     */
    public Date getPayStartTime() {
        return payStartTime;
    }

    /**
     * 支付开始时间
     *
     * @param payStartTime 支付开始时间
     */
    public void setPayStartTime(Date payStartTime) {
        this.payStartTime = payStartTime;
    }

    /**
     * 取消时间
     *
     * @return cancel_time 取消时间
     */
    public Date getCancelTime() {
        return cancelTime;
    }

    /**
     * 取消时间
     *
     * @param cancelTime 取消时间
     */
    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    /**
     * 支付渠道类型(1支付宝，2微信)
     *
     * @return out_channel_type 支付渠道类型(1支付宝，2微信)
     */
    public String getOutChannelType() {
        return outChannelType;
    }

    /**
     * 支付渠道类型(1支付宝，2微信)
     *
     * @param outChannelType 支付渠道类型(1支付宝，2微信)
     */
    public void setOutChannelType(String outChannelType) {
        this.outChannelType = outChannelType == null ? null : outChannelType.trim();
    }

    /**
     * 银行流水号
     *
     * @return bank_seq_no 银行流水号
     */
    public String getBankSeqNo() {
        return bankSeqNo;
    }

    /**
     * 银行流水号
     *
     * @param bankSeqNo 银行流水号
     */
    public void setBankSeqNo(String bankSeqNo) {
        this.bankSeqNo = bankSeqNo == null ? null : bankSeqNo.trim();
    }

    /**
     * 最后更新时间
     *
     * @return last_update_time 最后更新时间
     */
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * 最后更新时间
     *
     * @param lastUpdateTime 最后更新时间
     */
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * 可以申请退款的最后时间
     *
     * @return latest_refund_time 可以申请退款的最后时间
     */
    public Date getLatestRefundTime() {
        return latestRefundTime;
    }

    /**
     * 可以申请退款的最后时间
     *
     * @param latestRefundTime 可以申请退款的最后时间
     */
    public void setLatestRefundTime(Date latestRefundTime) {
        this.latestRefundTime = latestRefundTime;
    }

    /**
     * 发货状态（0，未发货，1已全发货，2，部分发货 3，已全部退货，4部分退货，）
     *
     * @return shipping_status 发货状态（0，未发货，1已全发货，2，部分发货 3，已全部退货，4部分退货，）
     */
    public int getShippingStatus() {
        return shippingStatus;
    }

    /**
     * 发货状态（0，未发货，1已全发货，2，部分发货 3，已全部退货，4部分退货，）
     *
     * @param shippingStatus 发货状态（0，未发货，1已全发货，2，部分发货 3，已全部退货，4部分退货，）
     */
    public void setShippingStatus(int shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    /**
     * 订单创建时关联学校id
     *
     * @return school_id 订单创建时关联学校id
     */
    public int getSchoolId() {
        return schoolId;
    }

    /**
     * 订单创建时关联学校id
     *
     * @param schoolId 订单创建时关联学校id
     */
    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    /**
     * 支付终端（1.pc 2mobile）
     *
     * @return pay_type 支付终端（1.pc 2mobile）
     */
    public int getPayType() {
        return payType;
    }

    /**
     * 支付终端（1.pc 2mobile）
     *
     * @param payType 支付终端（1.pc 2mobile）
     */
    public void setPayType(int payType) {
        this.payType = payType;
    }

    public List<OrderItemDomain> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemDomain> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<CourseCouponDomain> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CourseCouponDomain> couponDomains) {
        this.couponList = couponDomains;
    }

    public List<CourseActivityDomain> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<CourseActivityDomain> activityList) {
        this.activityList = activityList;
    }

    public boolean isRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    public BigDecimal getRetiredAmount() {
        return retiredAmount;
    }

    public void setRetiredAmount(BigDecimal retiredAmount) {
        this.retiredAmount = retiredAmount;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(BigDecimal returnPrice) {
        this.returnPrice = returnPrice;
    }

    public BigDecimal getOrderCouponPrice() {
        return orderCouponPrice;
    }

    public void setOrderCouponPrice(BigDecimal orderCouponPrice) {
        this.orderCouponPrice = orderCouponPrice;
    }

    public boolean isHasNoPayOrder() {
        return hasNoPayOrder;
    }

    public void setHasNoPayOrder(boolean hasNoPayOrder) {
        this.hasNoPayOrder = hasNoPayOrder;
    }

    public boolean isNeedAddress() {
        return isNeedAddress;
    }

    public void setNeedAddress(boolean needAddress) {
        isNeedAddress = needAddress;
    }

    public Set<String> getOrderHasUseActivityList() {
        return orderHasUseActivityList;
    }

    public void setOrderHasUseActivityList(Set<String> orderHasUseActivityList) {
        this.orderHasUseActivityList = orderHasUseActivityList;
    }

    public Map<String, List<OrderItemDomain>> getActivityBindOrderItem() {
        return activityBindOrderItem;
    }

    public void setActivityBindOrderItem(Map<String, List<OrderItemDomain>> activityBindOrderItem) {
        this.activityBindOrderItem = activityBindOrderItem;
    }

    /**
     * 保留2位小数
     *
     * @param price
     * @return
     */
    public static BigDecimal formatPriceWithBigDecimal(BigDecimal price) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return new BigDecimal(df.format(price == null ? BigDecimal.ZERO : price));
    }

    public Map<String, Object> getOrderItemIdToPayable() {
        return orderItemIdToPayable;
    }

    public void setOrderItemIdToPayable(Map<String, Object> orderItemIdToPayable) {
        this.orderItemIdToPayable = orderItemIdToPayable;
    }

    public Map<String, CourseActivityDomain> getActivityReturnAmountMap() {
        return activityReturnAmountMap;
    }

    public void setActivityReturnAmountMap(Map<String, CourseActivityDomain> activityReturnAmountMap) {
        this.activityReturnAmountMap = activityReturnAmountMap;
    }

    public BigDecimal getNoReturnShouldPayAmount() {
        return noReturnShouldPayAmount;
    }

    public void setNoReturnShouldPayAmount(BigDecimal noReturnShouldPayAmount) {
        this.noReturnShouldPayAmount = noReturnShouldPayAmount;
    }

    public BigDecimal getReturnItemTotalPayAmount() {
        return returnItemTotalPayAmount;
    }

    public void setReturnItemTotalPayAmount(BigDecimal returnItemTotalPayAmount) {
        this.returnItemTotalPayAmount = returnItemTotalPayAmount;
    }

    public BigDecimal getCurrentConsumePrice() {
        return currentConsumePrice;
    }

    public void setCurrentConsumePrice(BigDecimal currentConsumePrice) {
        this.currentConsumePrice = currentConsumePrice;
    }

    public BigDecimal getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(BigDecimal consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public BigDecimal getTotalMaterialAmount() {
        return totalMaterialAmount;
    }

    public void setTotalMaterialAmount(BigDecimal totalMaterialAmount) {
        this.totalMaterialAmount = totalMaterialAmount;
    }

    public BigDecimal getReturnPriceWithOutMaterial() {
        return returnPriceWithOutMaterial;
    }

    public void setReturnPriceWithOutMaterial(BigDecimal returnPriceWithOutMaterial) {
        this.returnPriceWithOutMaterial = returnPriceWithOutMaterial;
    }
}
