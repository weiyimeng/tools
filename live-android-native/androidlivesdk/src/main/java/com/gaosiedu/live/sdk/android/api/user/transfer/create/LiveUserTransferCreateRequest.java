/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.transfer.create;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**  请求接口
* @author sdk-generator-android request
* @describe
* @date 2018/11/28 14:27
* @since 2.1.0
*/
public class LiveUserTransferCreateRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/transfer/create";

    public LiveUserTransferCreateRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 调课原因
     */
    private String changeReason;

    /**
     * 新课程id
     */
    private Integer newCourseId;

    /**
     * 新课次id
     */
    private Integer newKnowledgeId;

    /**
     * 原始课程id
     */
    private Integer oldCourseId;

    /**
     * 原始课次id
     */
    private Integer oldKnowledgeId;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public String getChangeReason() {
        return this.changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
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

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}