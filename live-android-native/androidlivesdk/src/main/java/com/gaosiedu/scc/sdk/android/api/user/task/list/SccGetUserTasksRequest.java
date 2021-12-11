/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.task.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/11 15:44
* @since 2.1.0
*/
public class SccGetUserTasksRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/task/list";

    public SccGetUserTasksRequest() {
        super();
        setPath(PATH);
    }

    /**
     * userId
     */
    private String userId;


    //属性get||set方法

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}