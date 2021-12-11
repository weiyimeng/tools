/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.scc.sdk.android.domain;

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
public class ExerciseResultBean implements Serializable {
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
    private ExerciseBean exercise;
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
    private List<ExerciseResourceBean> exerciseResourceDomain;

    /**
     * 作业等级：2优，3良，4中，5差
     */
    private Integer exerciseLevel;
    /**
     * 批改获得金币
     */
    private Integer evaluationGold;
    /**
     * 批改获得进步值
     */
    private Integer evaluationProgress;
    /**
     * 提交获得金币
     */
    private Integer submitGold;
    /**
     * 提交获得进步值
     */
    private Integer submitProgress;

    /**
     * 是否允许重新上传。1 允许；2 不允许。（根据截止日期是否大于今天日期判断）
     */
    private Integer allowReSubmit;

    public String getKnowledgeOrder() {
        return knowledgeOrder;
    }

    public void setKnowledgeOrder(String knowledgeOrder) {
        this.knowledgeOrder = knowledgeOrder;
    }

    public List<ExerciseResourceBean> getExerciseResourceDomain() {
        return exerciseResourceDomain;
    }

    public void setExerciseResourceDomain(List<ExerciseResourceBean> exerciseResourceDomain) {
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

    public ExerciseBean getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseBean exercise) {
        this.exercise = exercise;
    }

    public Integer getExerciseLevel() {
        return exerciseLevel;
    }

    public void setExerciseLevel(Integer exerciseLevel) {
        this.exerciseLevel = exerciseLevel;
    }

    public Integer getEvaluationGold() {
        return evaluationGold;
    }

    public void setEvaluationGold(Integer evaluationGold) {
        this.evaluationGold = evaluationGold;
    }

    public Integer getEvaluationProgress() {
        return evaluationProgress;
    }

    public void setEvaluationProgress(Integer evaluationProgress) {
        this.evaluationProgress = evaluationProgress;
    }

    public Integer getSubmitGold() {
        return submitGold;
    }

    public void setSubmitGold(Integer submitGold) {
        this.submitGold = submitGold;
    }

    public Integer getSubmitProgress() {
        return submitProgress;
    }

    public void setSubmitProgress(Integer submitProgress) {
        this.submitProgress = submitProgress;
    }

    public Integer getAllowReSubmit() {
        return allowReSubmit;
    }

    public void setAllowReSubmit(Integer allowReSubmit) {
        this.allowReSubmit = allowReSubmit;
    }
}
