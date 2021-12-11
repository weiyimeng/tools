/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.template;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/29 19:15
* @since 2.1.0
*/
public class LiveCourseTemplateRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "course/template";

    public LiveCourseTemplateRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 
     */
    private Integer courseId;


    //属性get||set方法
    public Integer getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

}