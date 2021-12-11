/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 作业结果
 *
 * @author lixun
 * @describe
 * @date 2017/7/25 17:48
 * @since 2.1.0
 */
public class ExerciseResultDomain implements Serializable {
    /**
     * 作业结果状态，3是退回作业，0是未提交作业
     */
    private int exerciseResultStatus;
    /**
     * 作业结果id
     */
    private int exerciseResultId;
    /**
     *备注消息
     */
    private String remark;
    /**
     * 教师评语
     */
    private String comment;
    /**
     * 关联的作业
     */
    private ExerciseDomain exercise;
    /**
     * 得分
     */
    private int score;
    /**
     * 提交时间
     */
    private Date submitTime;
    /**
     * 批改教师名称
     */
    private String correctUserName;
    /**
     * 课次排序
     */
    private String knowledgeOrder;
    /**
     * 作业资源
     */
    private List<ExerciseResourceDomain> exerciseResourceDomain;

    public String getKnowledgeOrder() {
        return knowledgeOrder;
    }

    public void setKnowledgeOrder(String knowledgeOrder) {
        this.knowledgeOrder = knowledgeOrder;
    }

    public List<ExerciseResourceDomain> getExerciseResourceDomain() {
        return exerciseResourceDomain;
    }

    public void setExerciseResourceDomain(List<ExerciseResourceDomain> exerciseResourceDomain) {
        this.exerciseResourceDomain = exerciseResourceDomain;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getCorrectUserName() {
        return correctUserName;
    }

    public void setCorrectUserName(String correctUserName) {
        this.correctUserName = correctUserName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getExerciseResultStatus() {
        return exerciseResultStatus;
    }

    public void setExerciseResultStatus(int exerciseResultStatus) {
        this.exerciseResultStatus = exerciseResultStatus;
    }

    public int getExerciseResultId() {
        return exerciseResultId;
    }

    public void setExerciseResultId(int exerciseResultId) {
        this.exerciseResultId = exerciseResultId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ExerciseDomain getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseDomain exercise) {
        this.exercise = exercise;
    }
}
