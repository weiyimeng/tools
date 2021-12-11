/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.collection.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/30 19:32
* @since 2.1.0
*/
public class LiveUserCollectionListRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "user/collection/list";

    public LiveUserCollectionListRequest() {
        super();
        setPath(PATH);
    }


    /**
     * sortParams
     */
    private String sortParams;

    /**
     * 收藏类型  1为课程  2为人
     */
    private Integer type;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public String getSortParams() {
        return this.sortParams;
    }

    public void setSortParams(String sortParams) {
        this.sortParams = sortParams;
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
