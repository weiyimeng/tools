/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.wrongquestions.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/30 15:09
* @since 2.1.0
*/
public class LiveSccUserWrongQuestionListRequest  extends LiveSdkBasePageRequest {

    private transient final String PATH = "user/wrongquestions/list";

    public LiveSccUserWrongQuestionListRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 年级id
     */
    private Integer gradeId;

    /**
     * 科目id
     */
    private Integer subjectId;

    /**
     * userId
     */
    private String userId;

    /**
     * 错题类型。 空|0 : “全部错题” ；1 : "收藏错题"；2 ： “已订正”；3 ： “待订正”；
     */
    private Integer wrongType;


    //属性get||set方法

    public Integer getGradeId() {
        return this.gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }


    public Integer getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Integer getWrongType() {
        return this.wrongType;
    }

    public void setWrongType(Integer wrongType) {
        this.wrongType = wrongType;
    }

}