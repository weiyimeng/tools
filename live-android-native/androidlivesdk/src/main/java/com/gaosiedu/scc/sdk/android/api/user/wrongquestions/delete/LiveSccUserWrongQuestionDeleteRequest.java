/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.wrongquestions.delete;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/28 19:04
* @since 2.1.0
*/
public class LiveSccUserWrongQuestionDeleteRequest  extends LiveSdkBaseRequest {

    private final String PATH = "user/wrongquestions/delete";

    public LiveSccUserWrongQuestionDeleteRequest() {
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
    private int userWrongQuestionId;



    //属性get||set方法


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUserWrongQuestionId() {
        return userWrongQuestionId;
    }

    public void setUserWrongQuestionId(int userWrongQuestionId) {
        this.userWrongQuestionId = userWrongQuestionId;
    }
}