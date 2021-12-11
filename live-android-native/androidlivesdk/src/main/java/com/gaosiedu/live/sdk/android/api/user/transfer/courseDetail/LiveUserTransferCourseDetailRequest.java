/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.transfer.courseDetail;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/** 调课的课程详情 请求接口
* @author sdk-generator-android request
* @describe
* @date 2018/11/09 15:38
* @since 2.1.0
*/
public class LiveUserTransferCourseDetailRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/transfer/courseDetail";

    public LiveUserTransferCourseDetailRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 要调课的课程id
     */
    private int courseId;

    /**
     * 用户id
     */
    private int userId;


    //属性get||set方法
    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
