/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.wrongquestions.collect;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/28 19:02
* @since 2.1.0
*/
public class LiveSccUserWrongQuestionCollectRequest  extends LiveSdkBaseRequest {

    private final String PATH = "user/wrongquestions/collect";

    public LiveSccUserWrongQuestionCollectRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 是否收藏。1 收藏；0 取消收藏
     */
    private int collect;

    /**
     * 用户错题订正id
     */
    private String userId;

    /**
     * 用户错题id
     */
    private int userWrongQuestionId;



    //属性get||set方法

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
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