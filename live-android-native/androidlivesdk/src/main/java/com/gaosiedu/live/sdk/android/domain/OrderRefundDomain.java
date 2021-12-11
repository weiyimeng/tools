/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangjunle
 * @describe
 * @date 2017/7/31 11:52
 * @since 2.1.0
 */
public class OrderRefundDomain implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private int id;

    /**
     * 关联的订单id
     */
    private int orderId;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 退款单编号
     */
    private String orderRefundNo;

    /**
     * 订单支付的金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单使用的网站余额
     */
    private BigDecimal orderBalance;

    /**
     * 申请退还的金额
     */
    private BigDecimal applyAmount;

    /**
     * 申请退款余额
     */
    private BigDecimal applyBalance;

    /**
     * 实际退还的金额
     */
    private BigDecimal acceptAmount;

    /**
     * 实际退还的余额
     */
    private BigDecimal acceptBalance;

    /**
     * 调用第三方退款时的跟踪单号
     */
    private String traceNo;

    /**
     * 申请的时间
     */
    private Date applyTime;

    /**
     * 同意时间
     */
    private Date acceptTime;

    /**
     * 订单支付时间
     */
    private Date orderPayTime;

    /**
     * 最后处理时间
     */
    private Date lastUpdateTime;

    /**
     * 状态（0：过期待退款，1：未过期待退款，2：退款完成，3:转账中-1：驳回)
     */
    private int status;

    /**
     * 备注消息
     */
    private String remark;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 关联到的课次id
     */
    private String knowledgeIds;

    /**
     * 订单对应的支付跟踪编码
     */
    private String orderTraceNo;

    /**
     * 退货单id
     */
    private int orderReturnId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderRefundNo() {
        return orderRefundNo;
    }

    public void setOrderRefundNo(String orderRefundNo) {
        this.orderRefundNo = orderRefundNo;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderBalance() {
        return orderBalance;
    }

    public void setOrderBalance(BigDecimal orderBalance) {
        this.orderBalance = orderBalance;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getApplyBalance() {
        return applyBalance;
    }

    public void setApplyBalance(BigDecimal applyBalance) {
        this.applyBalance = applyBalance;
    }

    public BigDecimal getAcceptAmount() {
        return acceptAmount;
    }

    public void setAcceptAmount(BigDecimal acceptAmount) {
        this.acceptAmount = acceptAmount;
    }

    public BigDecimal getAcceptBalance() {
        return acceptBalance;
    }

    public void setAcceptBalance(BigDecimal acceptBalance) {
        this.acceptBalance = acceptBalance;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Date getOrderPayTime() {
        return orderPayTime;
    }

    public void setOrderPayTime(Date orderPayTime) {
        this.orderPayTime = orderPayTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getKnowledgeIds() {
        return knowledgeIds;
    }

    public void setKnowledgeIds(String knowledgeIds) {
        this.knowledgeIds = knowledgeIds;
    }

    public String getOrderTraceNo() {
        return orderTraceNo;
    }

    public void setOrderTraceNo(String orderTraceNo) {
        this.orderTraceNo = orderTraceNo;
    }

    public int getOrderReturnId() {
        return orderReturnId;
    }

    public void setOrderReturnId(int orderReturnId) {
        this.orderReturnId = orderReturnId;
    }
}
