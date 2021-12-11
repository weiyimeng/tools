/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程班级
 *
 * @author wjl
 * @describe
 * @date 2018/05/22 17:47
 * @since 2.1.0
 */
public class CourseClassDomain implements Serializable {
    /**
     *
     */
    private int id;
    
    /**
     * 班级名称
     */
    private String name;
    
    /**
     * 班级编码
     */
    private String code;
    
    /**
     * 班级人数
     */
    private int count;
    
    /**
     * 排序
     */
    private int sort;
    
    /**
     * 课程id
     */
    private int courseId;
    
    /**
     * 助教id
     */
    private int assistant;
    
    /**
     * 班级描述
     */
    private String info;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 助教老师名称
     */
    private String teacherName;
    /**
     * 该班实际人数
     */
    private int factCount;
    /**
     * 助教老师头像
     */
    private String headUrl;
    
    public String getHeadUrl() {
        return headUrl;
    }
    
    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
    
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
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public int getSort() {
        return sort;
    }
    
    public void setSort(int sort) {
        this.sort = sort;
    }
    
    public int getCourseId() {
        return courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public int getAssistant() {
        return assistant;
    }
    
    public void setAssistant(int assistant) {
        this.assistant = assistant;
    }
    
    public String getInfo() {
        return info;
    }
    
    public void setInfo(String info) {
        this.info = info;
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
    
    public String getTeacherName() {
        if (!TextUtils.isEmpty(teacherName)) {
            String replace = teacherName.replace("辅导老师", "").replace("()", "").replace("（）", "");
            return replace;
        }
        return teacherName;
    }
    
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
    
    public int getFactCount() {
        return factCount;
    }
    
    public void setFactCount(int factCount) {
        this.factCount = factCount;
    }
}
