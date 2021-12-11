/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author duankaiyang
 * @describe
 * @date 2017/8/2 15:44
 * @since 2.1.0
 */
public class CourseResourceBean implements Serializable{
    private int id;

    private int courseId;

    private int knowledgeId;

    private int size;

    private String type;

    private String category;

    private String resourceUrl;

    private String name;

    private Date createTime;

    private int status;

    private int resourceType;

    private int limitDegree;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(int knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl == null ? null : resourceUrl.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public int getLimitDegree() {
        return limitDegree;
    }

    public void setLimitDegree(int limitDegree) {
        this.limitDegree = limitDegree;
    }
}
