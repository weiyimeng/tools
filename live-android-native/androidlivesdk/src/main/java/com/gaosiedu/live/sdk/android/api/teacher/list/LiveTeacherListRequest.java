/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.teacher.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/29 19:15
* @since 2.1.0
*/
public class LiveTeacherListRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "teacher/list";

    public LiveTeacherListRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 
     */
    private Integer grade;

    /**
     * 
     */
    private String keywords;

    /**
     * sortParams
     */
    private String sortParams;

    /**
     * 
     */
    private Integer subject;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public Integer getGrade() {
        return this.grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSortParams() {
        return this.sortParams;
    }

    public void setSortParams(String sortParams) {
        this.sortParams = sortParams;
    }

    public Integer getSubject() {
        return this.subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}