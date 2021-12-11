/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.course.detail;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/31 10:00
* @since 2.1.0
*/
public class LiveSccUserCourseDetailRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/course/detail";

    public LiveSccUserCourseDetailRequest() {
        super();
        setPath(PATH);
    }

    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * id
     */
    private Integer id;


    /**
     * 用户id
     */
    private String userId;


    //属性get||set方法
    public Integer getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
