/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.common.subject;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/08/29 16:04
* @since 2.1.0
*/
public class LiveGradeSubjectListRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "common/grade/subject";

    public LiveGradeSubjectListRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 
     */
    private Integer grade;


    //属性get||set方法
    public Integer getGrade() {
        return this.grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

}