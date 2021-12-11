/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.wrongquestions.reason;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/28 19:07
* @since 2.1.0
*/
public class LiveSccUserWrongQuestionSaveReasonRequest  extends LiveSdkBaseRequest {

    private final String PATH = "user/wrongquestions/addReason";

    public LiveSccUserWrongQuestionSaveReasonRequest() {
        super();
        setPath(PATH);
    }



    /**
     * 错题原因
     */
    private String reason;

    /**
     * userId
     */
    private String userId;

    /**
     * 用户错题id
     */
    private int userWrongQuestionId;



    //属性get||set方法

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

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