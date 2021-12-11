/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;

/**
 * @author lianyutao
 * @describe
 * @date 2017/9/8 11:15
 * @since 2.1.0
 */
public class GradeSubjectDomain implements Serializable {


    /**
     * 年级id
     */
    private int gradeId;

    /**
     * 年级名
     */
    private String gradeName;

    /**
     * 学科id
     */
    private int subjectId;

    /**
     * 学科名
     */
    private String subjectName;

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
