/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.collection.add;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/30 19:32
* @since 2.1.0
*/
public class LiveUserCollectionAddRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/collection/add";

    public LiveUserCollectionAddRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 收藏商品的id
     */
    private Integer associateId;

    /**
     * 收藏状态 1：正常，0：取消或未收藏
     */
    private Integer status;

    /**
     * 收藏类型，type为1时关联课程，type为2时关联人
     */
    private Integer type;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public Integer getAssociateId() {
        return this.associateId;
    }

    public void setAssociateId(Integer associateId) {
        this.associateId = associateId;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}