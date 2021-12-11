/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.wrongquestions.mine;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
 * 我的错题本请求接口
* @author sdk-generator-android request
* @describe
* @date 2018/08/28 15:11
* @since 2.1.0
*/
public class LiveSccUserWrongQuestionBookDetailRequest  extends LiveSdkBaseRequest {

    private final String PATH = "user/wrongquestions/mine";

    public LiveSccUserWrongQuestionBookDetailRequest() {
        super();
        setPath(PATH);
    }

    /**
     * 年级id
     */
    private int gradeId;

    /**
     * userId
     */
    private String userId;


    public int getGradeId() {
        return this.gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }


    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}