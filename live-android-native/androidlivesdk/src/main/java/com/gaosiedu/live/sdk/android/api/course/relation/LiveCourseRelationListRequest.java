/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.relation;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/29 16:04
* @since 2.1.0
*/
public class LiveCourseRelationListRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "course/relation";

    public LiveCourseRelationListRequest() {
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
    private Integer userId;


    //属性get||set方法
    public Integer getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
