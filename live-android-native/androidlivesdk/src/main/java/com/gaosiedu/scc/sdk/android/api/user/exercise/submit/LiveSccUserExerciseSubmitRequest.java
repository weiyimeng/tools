/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.exercise.submit;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/21 18:05
* @since 2.1.0
*/
public class LiveSccUserExerciseSubmitRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/exercise/submit";

    public LiveSccUserExerciseSubmitRequest() {
        super();
        setPath(PATH);
    }


    /**
     * exerciseId
     */
    private Integer exerciseId;

    /**
     * resourceList
     */
    private List<String> resourceList;

    /**
     * userId
     */
    private Integer userId;


    //属性get||set方法
    public Integer getExerciseId() {
        return this.exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public List<String> getResourceList() {
        return this.resourceList;
    }

    public void setResourceList(List<String> resourceList) {
        this.resourceList = resourceList;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}