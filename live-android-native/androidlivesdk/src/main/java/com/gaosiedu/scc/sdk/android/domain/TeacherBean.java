/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 教师
 *
 * @author lixun
 * @describe
 * @date 2017/7/25 17:43
 * @since 2.1.0
 */

public class TeacherBean implements Serializable {

    /**
     *
     */
    private int id;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 密码
     */
  //  private String password;

    /**
     * 注册手机号
     */
    private String mobile;

    /**
     * 老师真实姓名
     */
    private String realname;

    /**
     * 类型（1老师，2助教）
     */
    private int type;

    /**
     * 头像地址
     */
    private String headUrl;

    /**
     * 微信二维码地址
     */
    private String wxUrl;

    /**
     * 授课年级Id
     */
    private String gradeIds;

    /**
     * 授课年级名称（用“,”分割）
     */
    private String gradeNames;

    /**
     * 授课科目Id
     */
    private String subjectIds;

    /**
     * 授课科目名称（用“,”分割）
     */
    private String subjectNames;

    /**
     * 教师状态(1:正常,其他:不在前端展示)
     */
    private int status;

    /**
     * 教师Id
     */
    private int userId;

    /**
     * 1  身份证 2  护照 3  军人证
     */
    private int credentialsType;

    /**
     * 证件号码
     */
    private String credentialsCode;

    /**
     * 1  金牌教师 2  专家教师
     */
    private int teacherLevel;

    /**
     * 教师荣誉
     */
    private String honour;

    /**
     * 教师图片
     */
    private String teacherImg;

    /**
     * 老师特长
     */
    private String speciality;

    /**
     * 教师详细信息id
     */
    private int detailsId;

    /**
     * 教师辅导次数
     */
    private int coach;

    /**
     * 教师名称拼音
     */
    private String namePinyin;

    /**
     * 教师简单描述
     */
    private String teacherDesc;

    /**
     * 老师标签id
     */
    private String tagIds;

    /**
     * 老师标签name
     */
    private String tagNames;

    /**
     * 教师微信号
     */
    private String teacherWxId;

    /**
     * 教师主页背景图片
     */
    private String bgImg;

    /**
     * 课程详情页教师头像
     */
    private String smallImg;

    /**
     * 收藏中的教师头像
     */
    private String teacherIcon;
    /**
     * 是否已关注
     */
    private  boolean isLike;
    /**
     * 教师课程列表
     */
    private List<CourseBean> courseList;

    public List<CourseBean> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<CourseBean> courseList) {
        this.courseList = courseList;
    }

    public boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean like) {
        isLike = like;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getWxUrl() {
        return wxUrl;
    }

    public void setWxUrl(String wxUrl) {
        this.wxUrl = wxUrl;
    }

    public String getGradeIds() {
        return gradeIds;
    }

    public void setGradeIds(String gradeIds) {
        this.gradeIds = gradeIds;
    }

    public String getGradeNames() {
        return gradeNames;
    }

    public void setGradeNames(String gradeNames) {
        this.gradeNames = gradeNames;
    }

    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

    public String getSubjectNames() {
        return subjectNames;
    }

    public void setSubjectNames(String subjectNames) {
        this.subjectNames = subjectNames;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCredentialsType() {
        return credentialsType;
    }

    public void setCredentialsType(int credentialsType) {
        this.credentialsType = credentialsType;
    }

    public String getCredentialsCode() {
        return credentialsCode;
    }

    public void setCredentialsCode(String credentialsCode) {
        this.credentialsCode = credentialsCode;
    }

    public int getTeacherLevel() {
        return teacherLevel;
    }

    public void setTeacherLevel(int teacherLevel) {
        this.teacherLevel = teacherLevel;
    }

    public String getHonour() {
        return honour;
    }

    public void setHonour(String honour) {
        this.honour = honour;
    }

    public String getTeacherImg() {
        return teacherImg;
    }

    public void setTeacherImg(String teacherImg) {
        this.teacherImg = teacherImg;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public int getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(int detailsId) {
        this.detailsId = detailsId;
    }

    public int getCoach() {
        return coach;
    }

    public void setCoach(int coach) {
        this.coach = coach;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public String getTeacherDesc() {
        return teacherDesc;
    }

    public void setTeacherDesc(String teacherDesc) {
        this.teacherDesc = teacherDesc;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getTagNames() {
        return tagNames;
    }

    public void setTagNames(String tagNames) {
        this.tagNames = tagNames;
    }

    public String getTeacherWxId() {
        return teacherWxId;
    }

    public void setTeacherWxId(String teacherWxId) {
        this.teacherWxId = teacherWxId;
    }

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }

    public String getSmallImg() {
        return smallImg;
    }

    public void setSmallImg(String smallImg) {
        this.smallImg = smallImg;
    }

    public String getTeacherIcon() {
        return teacherIcon;
    }

    public void setTeacherIcon(String teacherIcon) {
        this.teacherIcon = teacherIcon;
    }
}
