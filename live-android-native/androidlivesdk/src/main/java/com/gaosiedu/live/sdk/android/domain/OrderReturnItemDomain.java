/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import com.gaosiedu.live.sdk.android.domain.CourseDomain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退款单明细
 *
 * @author lixun
 * @describe
 * @date 2017/7/25 17:54
 * @since 2.1.0
 */
public class OrderReturnItemDomain implements Serializable {
    /**
     * 退款单关联的订单（暂时没用）
     */
    private OrderDomain orderDomain;
    /**
     * 关联课程（缓存中取）
     */
    private CourseDomain courseDomain;

    /**
     *
     */
    private int id;

    /**
     * 退货单id
     */
    private int orderReturnId;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 关联的订单id
     */
    private int orderId;

    /**
     * 订单行id
     */
    private int orderItemId;

    /**
     * 退货金额
     */
    private BigDecimal price;

    /**
     * 快递费用
     */
    private BigDecimal expressPrice;

    /**
     * 讲义费用
     */
    private BigDecimal notesPrice;

    /**
     *
     */
    private BigDecimal returnPrice;

    /**
     * 退货数量
     */
    private int count;

    /**
     * 状态(暂时没有用)
     */
    private int status;

    /**
     * 类型1.课程2商品
     */
    private int type;

    /**
     * 商品id（课程或商品id）
     */
    private int associateId;

    /**
     * 0未发货1已发货
     */
    private int shippingStatus;

    /**
     * 可退课次ids
     */
    private String returnKnowledgeIds;

    /**
     * order_return_item
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 退货单id
     * @return order_return_id 退货单id
     */
    public int getOrderReturnId() {
        return orderReturnId;
    }

    /**
     * 退货单id
     * @param orderReturnId 退货单id
     */
    public void setOrderReturnId(int orderReturnId) {
        this.orderReturnId = orderReturnId;
    }

    /**
     * 用户id
     * @return user_id 用户id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * 用户id
     * @param userId 用户id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * 关联的订单id
     * @return order_id 关联的订单id
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * 关联的订单id
     * @param orderId 关联的订单id
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * 订单行id
     * @return order_item_id 订单行id
     */
    public int getOrderItemId() {
        return orderItemId;
    }

    /**
     * 订单行id
     * @param orderItemId 订单行id
     */
    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    /**
     * 退货金额
     * @return price 退货金额
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 退货金额
     * @param price 退货金额
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 快递费用
     * @return express_price 快递费用
     */
    public BigDecimal getExpressPrice() {
        return expressPrice;
    }

    /**
     * 快递费用
     * @param expressPrice 快递费用
     */
    public void setExpressPrice(BigDecimal expressPrice) {
        this.expressPrice = expressPrice;
    }

    /**
     * 讲义费用
     * @return notes_price 讲义费用
     */
    public BigDecimal getNotesPrice() {
        return notesPrice;
    }

    /**
     * 讲义费用
     * @param notesPrice 讲义费用
     */
    public void setNotesPrice(BigDecimal notesPrice) {
        this.notesPrice = notesPrice;
    }

    /**
     *
     * @return return_price
     */
    public BigDecimal getReturnPrice() {
        return returnPrice;
    }

    /**
     *
     * @param returnPrice
     */
    public void setReturnPrice(BigDecimal returnPrice) {
        this.returnPrice = returnPrice;
    }

    /**
     * 退货数量
     * @return count 退货数量
     */
    public int getCount() {
        return count;
    }

    /**
     * 退货数量
     * @param count 退货数量
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 状态(暂时没有用)
     * @return status 状态(暂时没有用)
     */
    public int getStatus() {
        return status;
    }

    /**
     * 状态(暂时没有用)
     * @param status 状态(暂时没有用)
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 类型1.课程2商品
     * @return type 类型1.课程2商品
     */
    public int getType() {
        return type;
    }

    /**
     * 类型1.课程2商品
     * @param type 类型1.课程2商品
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 商品id（课程或商品id）
     * @return associate_id 商品id（课程或商品id）
     */
    public int getAssociateId() {
        return associateId;
    }

    /**
     * 商品id（课程或商品id）
     * @param associateId 商品id（课程或商品id）
     */
    public void setAssociateId(int associateId) {
        this.associateId = associateId;
    }

    /**
     * 0未发货1已发货
     * @return shipping_status 0未发货1已发货
     */
    public int getShippingStatus() {
        return shippingStatus;
    }

    /**
     * 0未发货1已发货
     * @param shippingStatus 0未发货1已发货
     */
    public void setShippingStatus(int shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    /**
     * 可退课次ids
     * @return return_knowledge_ids 可退课次ids
     */
    public String getReturnKnowledgeIds() {
        return returnKnowledgeIds;
    }

    /**
     * 可退课次ids
     * @param returnKnowledgeIds 可退课次ids
     */
    public void setReturnKnowledgeIds(String returnKnowledgeIds) {
        this.returnKnowledgeIds = returnKnowledgeIds == null ? null : returnKnowledgeIds.trim();
    }

   
    public OrderDomain getOrderDomain() {
        return orderDomain;
    }

    public void setOrderDomain(OrderDomain orderDomain) {
        this.orderDomain = orderDomain;
    }

    public CourseDomain getCourseDomain() {
        return courseDomain;
    }

    public void setCourseDomain(CourseDomain courseDomain) {
        this.courseDomain = courseDomain;
    }
}
