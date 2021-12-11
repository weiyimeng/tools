/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.live.initCk;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/11/07 18:25
* @since 2.1.0
*/
public class LiveSccLiveInitCkRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/live/initCk";

    public LiveSccLiveInitCkRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 课次id
     */
    private int courseKnowledgeId;

    /**
     * 用户id
     */
    private int userId;


    //属性get||set方法
    public int getCourseKnowledgeId() {
        return this.courseKnowledgeId;
    }

    public void setCourseKnowledgeId(int courseKnowledgeId) {
        this.courseKnowledgeId = courseKnowledgeId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
