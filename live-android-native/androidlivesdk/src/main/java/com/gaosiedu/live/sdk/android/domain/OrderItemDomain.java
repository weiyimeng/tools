/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;


import com.gaosiedu.live.sdk.android.api.user.address.list.LiveUserAddressListResponse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 订单明细
 *
 * @author lixun
 * @describe
 * @date 2017/7/25 17:52
 * @since 2.1.0
 */
public class OrderItemDomain extends CommonStatus implements Serializable {
    /**
     * 物流快递信息
     */
    public List<OrderDeliverDomain> orderDeliverDomains;
    /**
     * 此订单项是否可退(退款时用)
     */
    private boolean refundable;
    /**
     * 此订单项能退的课程数（退款时用）。
     */
    private int returnCount;
    /**
     * 此订单项应退的金额（退款时用）
     */
    private BigDecimal returnPrice;
    /**
     * 此订单项的课次单价（退款时用）
     */
    private BigDecimal knowledgePrice;
    /**
     * 此订单项可退的课次（以“_”连接）
     */
    private String knowledgeIds;
    /**
     * 此订单项的总课次（退款用）
     */
    private int totalCount;
    /**
     * 是否可以换课
     */
    private boolean changeCourse;
    /**
     * 是否可以调课
     */
    private boolean transferCourse;
    /**
     * 调课的次数（用来限定用户调课的次数）
     */
    private int transferCount;
    
    /**
     * 来源：1:购买 2换课 3赠送
     */
    private int source;
    
    private int id;
    
    private int ordersId;
    
    private int courseId;
    
    /**
     * 关联课程
     */
    private CourseDomain course;
    
    private int userId;
    
    private BigDecimal price;
    
    private BigDecimal oldPrice;
    
    private int count;
    
    private int type;
    
    private int shippingStatus;
    
    //    private int status;
    
    private String note;
    
    private int activity;
    
    private BigDecimal activityPrice;
    
    private BigDecimal couponPrice;
    
    private int couponId;
    
    private BigDecimal couponAllPrice;
    
    private BigDecimal peopleChangePrice;
    
    /**
     * 物料成本
     */
    private BigDecimal materialCost=BigDecimal.ZERO;
    
    /**
     * 已退款的金额
     */
    private BigDecimal retiredAmount;
    
    /**
     * 参与全局(订单)活动减免金额
     */
    private BigDecimal activityAllPrice;
    
    /**
     * 实付金额
     */
    private BigDecimal realPrice;
    
    /**
     * 订单里是使用的优惠金额
     */
    private BigDecimal useActivityPrice;
    
    /**
     * 课程消耗金额
     */
    private BigDecimal courseConsumePrice;
    
    /**
     * 已经使用的课耗
     */
    private BigDecimal consumePrice;
    
    
    /**
     * 已参与的活动
     */
    private List<CourseActivityDomain> usedActivityList;
    /**
     * 订单 地址
     */
    private LiveUserAddressListResponse.ListData addressInfo;
    
    public LiveUserAddressListResponse.ListData getAddressInfo() {
        return addressInfo;
    }
    
    public void setAddressInfo(LiveUserAddressListResponse.ListData addressInfo) {
        this.addressInfo = addressInfo;
    }
    
    /**
     * 标识是否已退
     */
    private boolean hasReturn = false;
    /**
     * 课程id 用于下单自动分班wjl
     */
    private int courseClassId;
    
    public boolean isTransferCourse() {
        return transferCourse;
    }
    
    public void setTransferCourse(boolean transferCourse) {
        this.transferCourse = transferCourse;
    }
    
    public int getTransferCount() {
        return transferCount;
    }
    
    public void setTransferCount(int transferCount) {
        this.transferCount = transferCount;
    }
    
    public int getCourseClassId() {
        return courseClassId;
    }
    
    public void setCourseClassId(int courseClassId) {
        this.courseClassId = courseClassId;
    }
    
    public int getSource() {
        return source;
    }
    
    public void setSource(int source) {
        this.source = source;
    }
    
    public boolean isChangeCourse() {
        return changeCourse;
    }
    
    public void setChangeCourse(boolean changeCourse) {
        this.changeCourse = changeCourse;
    }
    
    public boolean isHasReturn() {
        return hasReturn;
    }
    
    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }
    
    public int getActivity() {
        return activity;
    }
    
    public void setActivity(int activity) {
        this.activity = activity;
    }
    
    public int getReturnCount() {
        return returnCount;
    }
    
    public void setReturnCount(int returnCount) {
        this.returnCount = returnCount;
    }
    
    public BigDecimal getReturnPrice() {
        return formatPriceWithBigDecimal(returnPrice);
    }
    
    public void setReturnPrice(BigDecimal returnPrice) {
        this.returnPrice = returnPrice;
    }
    
    public BigDecimal getKnowledgePrice() {
        return formatPriceWithBigDecimal(knowledgePrice);
    }
    
    public void setKnowledgePrice(BigDecimal knowledgePrice) {
        this.knowledgePrice = knowledgePrice;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getOrdersId() {
        return ordersId;
    }
    
    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }
    
    public CourseDomain getCourse() {
        return course;
    }
    
    public void setCourse(CourseDomain course) {
        this.course = course;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public BigDecimal getOldPrice() {
        return oldPrice;
    }
    
    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }
    
    public int getShippingStatus() {
        return shippingStatus;
    }
    
    public void setShippingStatus(int shippingStatus) {
        this.shippingStatus = shippingStatus;
    }
    
    //    public int getStatus() {
    //        return status;
    //    }
    //
    //    public void setStatus(int status) {
    //        this.status = status;
    //    }
    
    public String getNote() {
        return note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    
    public BigDecimal getActivityPrice() {
        return activityPrice;
    }
    
    public void setActivityPrice(BigDecimal activityPrice) {
        this.activityPrice = activityPrice;
    }
    
    public BigDecimal getCouponPrice() {
        return couponPrice;
    }
    
    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }
    
    public BigDecimal getPeopleChangePrice() {
        return peopleChangePrice;
    }
    
    public void setPeopleChangePrice(BigDecimal peopleChangePrice) {
        this.peopleChangePrice = peopleChangePrice;
    }
    
    public String getKnowledgeIds() {
        return knowledgeIds;
    }
    
    public void setKnowledgeIds(String knowledgeIds) {
        this.knowledgeIds = knowledgeIds;
    }
    
    public int getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public int getCourseId() {
        return courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public boolean isRefundable() {
        return refundable;
    }
    
    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }
    
    public BigDecimal getCouponAllPrice() {
        return couponAllPrice;
    }
    
    public void setCouponAllPrice(BigDecimal couponAllPrice) {
        this.couponAllPrice = couponAllPrice;
    }
    
    public BigDecimal getActivityAllPrice() {
        return activityAllPrice;
    }
    
    public void setActivityAllPrice(BigDecimal activityAllPrice) {
        this.activityAllPrice = activityAllPrice;
    }
    
    public BigDecimal getMaterialCost() {
        return materialCost;
    }
    
    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }
    
    public BigDecimal getRetiredAmount() {
        return retiredAmount;
    }
    
    public void setRetiredAmount(BigDecimal retiredAmount) {
        this.retiredAmount = retiredAmount;
    }
    
    public int getCouponId() {
        return couponId;
    }
    
    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }
    
    
    public BigDecimal getRealPrice() {
        return realPrice;
    }
    
    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
    }
    
    
    public BigDecimal getUseActivityPrice() {
        return useActivityPrice;
    }
    
    public void setUseActivityPrice(BigDecimal useActivityPrice) {
        this.useActivityPrice = useActivityPrice;
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
    
    public BigDecimal getCourseConsumePrice() {
        return courseConsumePrice;
    }
    
    public void setCourseConsumePrice(BigDecimal courseConsumePrice) {
        this.courseConsumePrice = courseConsumePrice;
    }
    
    public BigDecimal getConsumePrice() {
        return consumePrice;
    }
    
    public void setConsumePrice(BigDecimal consumePrice) {
        this.consumePrice = consumePrice;
    }
    
    public List<CourseActivityDomain> getUsedActivityList() {
        return usedActivityList;
    }
    
    public void setUsedActivityList(List<CourseActivityDomain> usedActivityList) {
        this.usedActivityList = usedActivityList;
    }
    
    
}
