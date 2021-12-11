/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.exercise.result.detail;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/30 17:53
* @since 2.1.0
*/
public class LiveSccUserExerciseResultDetailRequest extends LiveSdkBaseRequest {

    private transient final String PATH = "user/exercise/result/detail";

    public LiveSccUserExerciseResultDetailRequest() {
        super();
        setPath(PATH);
    }

    /**
     * 作业结果id
     */
    private Integer exerciseResultId;

    /**
     * 用户id
     */
    private String userId;


    //属性get||set方法


    public Integer getExerciseResultId() {
        return exerciseResultId;
    }

    public void setExerciseResultId(Integer exerciseResultId) {
        this.exerciseResultId = exerciseResultId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}