/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券
 *
 * @author lixun
 * @describe
 * @date 2017/7/25 17:45
 * @since 2.1.0
 */
//新版将采用courseCoupon
//@Deprecated
public class CouponDomain implements Serializable {
    /**
     *
     */
    private int id;
    /**
     * 折扣名称
     */
    private String name;
    /**
     * 优惠券类型1.全场通用,2.课程体系通用,3.课程适用,4.产品适用,5:学科通用,6:年级通用
     */
    private int type;
    /**
     * 优惠券过期时间
     */
    private Date expiryTime;
    /**
     * 减免的具体金额(负数)
     */
    private BigDecimal remission;
    /**
     * 优惠券关联id
     */
    private  int associateId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public BigDecimal getRemission() {
        return remission;
    }

    public void setRemission(BigDecimal remission) {
        this.remission = remission;
    }

    public int getAssociateId() {
        return associateId;
    }

    public void setAssociateId(int associateId) {
        this.associateId = associateId;
    }
}
