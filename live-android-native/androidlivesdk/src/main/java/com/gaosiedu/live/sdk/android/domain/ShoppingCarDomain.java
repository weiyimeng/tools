/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import com.gaosiedu.live.sdk.android.domain.CourseClassDomain;
import com.gaosiedu.live.sdk.android.domain.CourseDomain;
import com.gaosiedu.live.sdk.android.domain.CouponDomain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 购物车
 *
 * @author lixun
 * @describe
 * @date 2017/7/25 17:56
 * @since 2.1.0
 */
public class ShoppingCarDomain implements Serializable {
    /**
     * 购物车去支付时存放对应课程
     */
    private CourseDomain courseDomain;

    private int id;

    private int associateId;

    private int userId;

    private Date createTime;

    private Date updateTime;

    private int status;

    private int type;

    private int shoppingCount;
    
    
    /**
     * 是否加入购物车
     */
    private boolean  flag;
    //用来判断是否可以报名（库存限制）
    private CourseClassDomain courseClassDomain;
    /**
     * 更新记录数wjl2018-08-07
     */
    private int updateCount;
    /**
     * 失效状态 0失效，其他正常
     */
    private int expiredStatus;

    public int getExpiredStatus() {
        return expiredStatus;
    }

    public void setExpiredStatus(int expiredStatus) {
        this.expiredStatus = expiredStatus;
    }

    public CourseDomain getCourseDomain() {
        return courseDomain;
    }

    public void setCourseDomain(CourseDomain courseDomain) {
        this.courseDomain = courseDomain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssociateId() {
        return associateId;
    }

    public void setAssociateId(int associateId) {
        this.associateId = associateId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getShoppingCount() {
        return shoppingCount;
    }

    public void setShoppingCount(int shoppingCount) {
        this.shoppingCount = shoppingCount;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public CourseClassDomain getCourseClassDomain() {
        return courseClassDomain;
    }

    public void setCourseClassDomain(CourseClassDomain courseClassDomain) {
        this.courseClassDomain = courseClassDomain;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }
}
