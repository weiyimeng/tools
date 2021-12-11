/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.cart.pre;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**  请求接口
* @author sdk-generator-android request
* @describe
* @date 2018/12/06 13:56
* @since 2.1.0
*/
public class LiveCartPreRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "cart/pre";

    public LiveCartPreRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 
     */
    private String courseIdsStr;

    /**
     * 
     */
    private Integer userId;


    //属性get||set方法
    public String getCourseIdsStr() {
        return this.courseIdsStr;
    }

    public void setCourseIdsStr(String courseIdsStr) {
        this.courseIdsStr = courseIdsStr;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}