/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.transfer.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/04 16:13
* @since 2.1.0
*/
public class LiveUserTransferListRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "user/transfer/list";

    public LiveUserTransferListRequest() {
        super();
        setPath(PATH);
    }


    /**
     * changeReason
     */
    private String changeReason;

    /**
     * createTime
     */
    private String createTime;

    /**
     * newCourseId
     */
    private Integer newCourseId;

    /**
     * newKnowledgeId
     */
    private Integer newKnowledgeId;

    /**
     * oldCourseId
     */
    private Integer oldCourseId;

    /**
     * oldKnowledgeId
     */
    private Integer oldKnowledgeId;

    /**
     * status
     */
    private Integer status;

    /**
     * userId
     */
    private Integer userId;


    //属性get||set方法
    public String getChangeReason() {
        return this.changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getNewCourseId() {
        return this.newCourseId;
    }

    public void setNewCourseId(Integer newCourseId) {
        this.newCourseId = newCourseId;
    }

    public Integer getNewKnowledgeId() {
        return this.newKnowledgeId;
    }

    public void setNewKnowledgeId(Integer newKnowledgeId) {
        this.newKnowledgeId = newKnowledgeId;
    }

    public Integer getOldCourseId() {
        return this.oldCourseId;
    }

    public void setOldCourseId(Integer oldCourseId) {
        this.oldCourseId = oldCourseId;
    }

    public Integer getOldKnowledgeId() {
        return this.oldKnowledgeId;
    }

    public void setOldKnowledgeId(Integer oldKnowledgeId) {
        this.oldKnowledgeId = oldKnowledgeId;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}