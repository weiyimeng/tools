/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.comment;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/29 19:15
* @since 2.1.0
*/
public class LiveCourseCommentListRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "course/comment";

    public LiveCourseCommentListRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 
     */
    private Integer courseId;

    /**
     * sortParams
     */
    private String sortParams;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public Integer getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getSortParams() {
        return this.sortParams;
    }

    public void setSortParams(String sortParams) {
        this.sortParams = sortParams;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}