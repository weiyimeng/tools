/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.wrongquestions.correct.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/30 15:18
* @since 2.1.0
*/
public class LiveSccUserWrongQuestionCorrectListRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/wrongquestions/correct/list";

    public LiveSccUserWrongQuestionCorrectListRequest() {
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



    //属性get||set方法

    public Integer getUserWrongQuestionId() {
        return this.userWrongQuestionId;
    }

    public void setUserWrongQuestionId(Integer userWrongQuestionId) {
        this.userWrongQuestionId = userWrongQuestionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}