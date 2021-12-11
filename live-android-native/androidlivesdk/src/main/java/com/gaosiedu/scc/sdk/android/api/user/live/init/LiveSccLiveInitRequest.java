/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.live.init;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/09/19 11:33
 * @since 2.1.0
 */
public class LiveSccLiveInitRequest extends LiveSdkBaseRequest {
    
    private transient final String PATH = "user/live/init";
    
    public LiveSccLiveInitRequest() {
        super();
        setPath(PATH);
    }
    
    
    /**
     * 课次id
     */
    private String courseKnowledgeId;
    
    /**
     * 用户id
     */
    private int userId;
    
    
    //属性get||set方法
    public String getCourseKnowledgeId() {
        return this.courseKnowledgeId;
    }
    
    public void setCourseKnowledgeId(String courseKnowledgeId) {
        this.courseKnowledgeId = courseKnowledgeId;
    }
    
    public int getUserId() {
        return this.userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
}
