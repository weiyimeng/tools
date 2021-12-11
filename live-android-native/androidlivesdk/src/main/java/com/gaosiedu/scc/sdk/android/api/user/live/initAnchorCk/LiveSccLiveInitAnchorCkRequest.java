/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.live.initAnchorCk;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/11/22 10:39
* @since 2.1.0
*/
public class LiveSccLiveInitAnchorCkRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/live/initAnchorCk";

    public LiveSccLiveInitAnchorCkRequest() {
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
     * 直播类型
     */
    private String liveType;

    /**
     * 角色
     */
    private String role;

    /**
     * 教师id
     */
    private Integer teacherId;


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

    public String getLiveType() {
        return this.liveType;
    }

    public void setLiveType(String liveType) {
        this.liveType = liveType;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getTeacherId() {
        return this.teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

}