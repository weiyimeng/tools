/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.wrongquestions.detail;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/19 11:30
* @since 2.1.0
*/
public class LiveSccUserWrongQuestionDetailRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/wrongquestions/detail";

    public LiveSccUserWrongQuestionDetailRequest() {
        super();
        setPath(PATH);
    }



    /**
     * userId
     */
    private String userId;

    /**
     * 用户错题id
     */
    private Integer userWrongQuestionId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUserWrongQuestionId() {
        return userWrongQuestionId;
    }

    public void setUserWrongQuestionId(Integer userWrongQuestionId) {
        this.userWrongQuestionId = userWrongQuestionId;
    }
}