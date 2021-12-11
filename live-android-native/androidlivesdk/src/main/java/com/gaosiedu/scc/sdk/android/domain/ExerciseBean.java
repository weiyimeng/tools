/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 作业
 *
 * @author lixun
 * @describe
 * @date 2017/7/25 17:47
 * @since 2.1.0
 */
public class ExerciseBean implements Serializable {
    /**
     *
     */
    private int id;

    /**
     * 练习名称
     */
    private String name;

    /**
     * 内容
     */
    private String content;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程id
     */
    private int courseId;

    /**
     * 课次名称
     */
    private String knowledgeName;

    /**
     * 课次排序
     */
    private int knowledgeOrder;

    /**
     * 作业提交最晚时间
     * @return
     */
    private Date latest;

    /**
     * 作业提交最晚时间
     * @return
     */
    private Date earliest;

    /**
     * 对于某个用户改作业的成绩
     * @return
     */
    private int score;
    /**
     * 作业分数-带小数的
     */
    private String scoreStr;

    public String getScoreStr() {
        return scoreStr;
    }

    public void setScoreStr(String scoreStr) {
        this.scoreStr = scoreStr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getKnowledgeName() {
        return knowledgeName;
    }

    public void setKnowledgeName(String knowledgeName) {
        this.knowledgeName = knowledgeName;
    }

    public int getKnowledgeOrder() {
        return knowledgeOrder;
    }

    public void setKnowledgeOrder(int knowledgeOrder) {
        this.knowledgeOrder = knowledgeOrder;
    }

    public Date getLatest() {
        return latest;
    }

    public void setLatest(Date latest) {
        this.latest = latest;
    }

    public Date getEarliest() {
        return earliest;
    }

    public void setEarliest(Date earliest) {
        this.earliest = earliest;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
