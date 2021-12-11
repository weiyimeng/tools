/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lianyutao
 * @version 3.0.0
 * @description
 * @date 2018/6/7 15:03
 */
public class StoreHouseDomain implements Serializable{
    /**
     * 主键
     */
    private int id;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品状态1 可用，0或其他不可用
     */
    private int status;

    /**
     * 产品类型1：讲义，2：试卷，3作业本等等（以后可以再加）
     */
    private int type;

    /**
     * 关联课程id
     */
    private int courseId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 产品备注
     */
    private String productRemark;

    /**
     * 删除标记1未删除0表示删除
     */
    private int deleteFlag;

    /**
    /**
     * 金额
     */
    private BigDecimal price;

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

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getProductRemark() {
        return productRemark;
    }

    public void setProductRemark(String productRemark) {
        this.productRemark = productRemark;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
