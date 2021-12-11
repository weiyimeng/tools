/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.detail;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/06 09:41
* @since 2.1.0
*/
public class LiveCourseDetailRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "course/detail";

    public LiveCourseDetailRequest() {
        super();
        setPath(PATH);
    }


    /**
     *
     */
    private Integer courseId;

    /**
     *
     */
    private String userId;


    //属性get||set方法
    public Integer getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
