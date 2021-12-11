/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wangjunle
 * @describe
 * @date 2018/8/9 9:55
 * @since 2.1.0
 */
public class CourseEnclosureDomain implements Serializable {
    /**
     * 随材名字
     */
    private String name;
    /**
     * 随材类型
     */
    private int type;

    /**
     * 附件数量
     */
    private int count;

    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 课程id
     */
    private int courseId;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
