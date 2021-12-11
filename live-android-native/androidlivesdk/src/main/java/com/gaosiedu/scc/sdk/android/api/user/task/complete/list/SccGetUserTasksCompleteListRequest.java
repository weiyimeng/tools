/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.task.complete.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/11 15:44
* @since 2.1.0
*/
public class SccGetUserTasksCompleteListRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "user/task/complete/list";

    public SccGetUserTasksCompleteListRequest() {
        super();
        setPath(PATH);
    }


    /**
     * sortParams
     */
    private String sortParams;

    /**
     * userId
     */
    private String userId;

    public String getSortParams() {
        return this.sortParams;
    }

    public void setSortParams(String sortParams) {
        this.sortParams = sortParams;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
