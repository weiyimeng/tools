/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.teacher.detail;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/29 19:15
* @since 2.1.0
*/
public class LiveTeacherDetailRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "teacher/detail";

    public LiveTeacherDetailRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 
     */
    private Integer teacherId;

    /**
     * 
     */
    private Integer userId;


    //属性get||set方法
    public Integer getTeacherId() {
        return this.teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}