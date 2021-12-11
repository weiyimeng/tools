/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import com.gaosiedu.live.sdk.android.domain.TeacherDomain;
import com.gaosiedu.live.sdk.android.domain.ExerciseResultDomain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 课程知识点
 *
 * @author lixun
 * @describe
 * @date 2017/7/25 17:41
 * @since 2.1.0
 */
public class CourseKnowledgeDomain implements Serializable {

    /**用户课次是否参与直播或者参与回放(true 表示已参与)*/
    private boolean knowledgeJoin;
    /**
     * 用户作业是否提交(true 表示已提交)
     */
    private boolean submitAble;
    /**
     * 该课次是否有有效的作业(true 表示有)
     */
    private boolean hasHomeWork;/**
     * 作业
     */
    private ExerciseResultDomain homeworkExercise;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 开课状态
     */
    private String flag;
    /**
     *
     */
    private int id;

    /**
     * 课程Id
     */
    private int courseId;

    /**
     * 课程知识点名称
     */
    private String name;
    /**
     * 课程科目id
     */
    private String courseSubjectIds;
    /**
     * 课程学科名称
     */
    private String courseSubjectNames;

    /**
     * 学期
     */
    private int term;

    /**
     * 教师id
     */
    private int teacherId;

    /**
     * 授课教师()
     */
    private String teacherName;

    /**
     * 授课教师
     */
    private TeacherDomain tearcher;

    /**
     * 助教(多个id之间用,分割)
     */
    private String assistants;

    /**
     * -1  删除 1 有效
     */
    private int status;

    /**
     * 1  免费 0/null 不免费
     */
    private int isFree;

    /**
     * 开课日期
     */
    private Date classDate;

    /**
     * 1  每天 2  每周 3  制定日期
     */
    private String classFrequency;

    /**
     * 学生上课的具体时间
     */
    private Date startTime;

    /**
     * 上课结束时间
     */
    private Date endTime;

    /**
     * 创建人
     */
    private int createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 课程排序
     */
    private int displayOrder;

    /**
     * 授课方式(1：推桌面，2：在线ppt+音频)
     */
    private int liveType;

    /**
     * 是否支持手机观看(1:支持,2:不支持)
     */
    private int mobileSupported;

    /**
     * 试听时长
     */
    private int auditionTime;

    /**
     * 支持平台
     */
    private String supportPlatform;

    /**
     * 课次作业列表
     */
    private List<ExerciseDomain> exercises;

    private String showTime;

    /**
     * 课次资源列表
     */
    private List<CourseResourceDomain> resources;

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public TeacherDomain getTearcher() {
        return tearcher;
    }

    public void setTearcher(TeacherDomain tearcher) {
        this.tearcher = tearcher;
    }

    public String getAssistants() {
        return assistants;
    }

    public void setAssistants(String assistants) {
        this.assistants = assistants;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public Date getClassDate() {
        return classDate;
    }

    public void setClassDate(Date classDate) {
        this.classDate = classDate;
    }

    public String getClassFrequency() {
        return classFrequency;
    }

    public void setClassFrequency(String classFrequency) {
        this.classFrequency = classFrequency;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getLiveType() {
        return liveType;
    }

    public void setLiveType(int liveType) {
        this.liveType = liveType;
    }

    public int getMobileSupported() {
        return mobileSupported;
    }

    public void setMobileSupported(int mobileSupported) {
        this.mobileSupported = mobileSupported;
    }

    public int getAuditionTime() {
        return auditionTime;
    }

    public void setAuditionTime(int auditionTime) {
        this.auditionTime = auditionTime;
    }

    public String getSupportPlatform() {
        return supportPlatform;
    }

    public void setSupportPlatform(String supportPlatform) {
        this.supportPlatform = supportPlatform;
    }

    public List<ExerciseDomain> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseDomain> exercises) {
        this.exercises = exercises;

    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


    public List<CourseResourceDomain> getResources() {
        return resources;
    }

    public void setResources(List<CourseResourceDomain> resources) {
        this.resources = resources;
    }

    public boolean isSubmitAble() {
        return submitAble;
    }

    public void setSubmitAble(boolean submitAble) {
        this.submitAble = submitAble;
    }

    public boolean isHasHomeWork() {
        return hasHomeWork;
    }

    public void setHasHomeWork(boolean hasHomeWork) {

        this.hasHomeWork = hasHomeWork;
    }

    public ExerciseResultDomain getHomeworkExercise() {
        return homeworkExercise;
    }

    public boolean isKnowledgeJoin() {
        return knowledgeJoin;
    }

    public void setKnowledgeJoin(boolean knowledgeJoin) {
        this.knowledgeJoin = knowledgeJoin;
    }

    public void setHomeworkExercise(ExerciseResultDomain homeworkExercise) {

        this.homeworkExercise = homeworkExercise;
    }

    public String getCourseSubjectIds() {
        return courseSubjectIds;
    }

    public void setCourseSubjectIds(String courseSubjectIds) {
        this.courseSubjectIds = courseSubjectIds;
    }

    public String getCourseSubjectNames() {
        return courseSubjectNames;
    }

    public void setCourseSubjectNames(String courseSubjectNames) {
        this.courseSubjectNames = courseSubjectNames;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }
}
