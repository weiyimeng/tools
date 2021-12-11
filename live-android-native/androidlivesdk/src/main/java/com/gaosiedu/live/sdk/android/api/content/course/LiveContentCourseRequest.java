/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.content.course;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/29 14:36
* @since 2.1.0
*/
public class LiveContentCourseRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "content/course";

    public LiveContentCourseRequest() {
        super();
        setPath(PATH);
    }

}