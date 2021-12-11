/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/08/29 18:20
 * @since 2.1.0
 */
public class LiveCourseListRequest extends LiveSdkBasePageRequest {
    
    private transient final String PATH = "course/list";
    
    public LiveCourseListRequest() {
        super();
        setPath(PATH);
    }
    
    
    /**
     *
     */
    private String activityIdStr;
    
    /**
     *
     */
    private Integer courseYear;
    
    /**
     *
     */
    private Integer grade;
    
    /**
     *
     */
    private Integer isFree;
    
    /**
     *
     */
    private String keywords;
    
    /**
     * passCourse=1 系列课,passCourse=0专题课
     */
    private Integer passCourse;
    
    /**
     *
     */
    private String periodTime;
    
    /**
     *
     */
    private Integer sort;
    
    /**
     * sortParams
     */
    private String sortParams;
    
    /**
     *
     */
    private Integer subjectId;
    
    /**
     *
     */
    private Integer teacherId;
    
    /**
     *
     */
    private Integer term;
    
    /**
     *
     */
    private String time;
    
    /**
     * 公开课type=3
     */
    private String type;
    
    /**
     * 用户id
     */
    private Integer userId;
    
    
    //属性get||set方法
    public String getActivityIdStr() {
        return this.activityIdStr;
    }
    
    public void setActivityIdStr(String activityIdStr) {
        this.activityIdStr = activityIdStr;
    }
    
    public Integer getCourseYear() {
        return this.courseYear;
    }
    
    public void setCourseYear(Integer courseYear) {
        this.courseYear = courseYear;
    }
    
    public Integer getGrade() {
        return this.grade;
    }
    
    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    
    public Integer getIsFree() {
        return this.isFree;
    }
    
    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }
    
    public String getKeywords() {
        return this.keywords;
    }
    
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    public Integer getPassCourse() {
        return this.passCourse;
    }
    
    public void setPassCourse(Integer passCourse) {
        this.passCourse = passCourse;
    }
    
    public String getPeriodTime() {
        return this.periodTime;
    }
    
    public void setPeriodTime(String periodTime) {
        this.periodTime = periodTime;
    }
    
    public Integer getSort() {
        return this.sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    
    public String getSortParams() {
        return this.sortParams;
    }
    
    public void setSortParams(String sortParams) {
        this.sortParams = sortParams;
    }
    
    public int getSubjectId() {
        return this.subjectId;
    }
    
    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
    
    public Integer getTeacherId() {
        return this.teacherId;
    }
    
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
    
    public Integer getTerm() {
        return this.term;
    }
    
    public void setTerm(Integer term) {
        this.term = term;
    }
    
    public String getTime() {
        return this.time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
}
