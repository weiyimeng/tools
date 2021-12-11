/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.cart.add.again;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/25 11:58
* @since 2.1.0
*/
public class LiveCartAddAgainRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "cart/add/again";

    public LiveCartAddAgainRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 
     */
    private String courseIds;

    /**
     * 
     */
    private Integer userId;


    //属性get||set方法
    public String getCourseIds() {
        return this.courseIds;
    }

    public void setCourseIds(String courseIds) {
        this.courseIds = courseIds;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}