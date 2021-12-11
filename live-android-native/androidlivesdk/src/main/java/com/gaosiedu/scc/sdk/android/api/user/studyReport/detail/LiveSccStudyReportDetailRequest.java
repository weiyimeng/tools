/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.studyReport.detail;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/12/24 19:42
* @since 2.1.0
*/
public class LiveSccStudyReportDetailRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/studyReport/detail";

    public LiveSccStudyReportDetailRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 课次id
     */
    private Integer courseKnowledgeId;

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

    public Integer getCourseKnowledgeId() {
        return this.courseKnowledgeId;
    }

    public void setCourseKnowledgeId(Integer courseKnowledgeId) {
        this.courseKnowledgeId = courseKnowledgeId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}