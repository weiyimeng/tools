/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.common.area;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/29 16:04
* @since 2.1.0
*/
public class LiveAreaListRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "common/area/list";

    public LiveAreaListRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 
     */
    private int parent;


    //属性get||set方法
    public int getParent() {
        return this.parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

}