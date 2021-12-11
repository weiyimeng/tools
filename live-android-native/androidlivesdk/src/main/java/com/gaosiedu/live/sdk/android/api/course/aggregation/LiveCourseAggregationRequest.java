/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.aggregation;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/29 19:15
* @since 2.1.0
*/
public class LiveCourseAggregationRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "course/aggregation";

    public LiveCourseAggregationRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 年级
     */
    private String grade;

    /**
     * sortParams
     */
    private String sortParams;

    /**
     * 学科id
     */
    private String subject;

    /**
     * 学期
     */
    private String term;

    /**
     * 用户id
     */
    private String userId;


    //属性get||set方法
    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSortParams() {
        return this.sortParams;
    }

    public void setSortParams(String sortParams) {
        this.sortParams = sortParams;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTerm() {
        return this.term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
