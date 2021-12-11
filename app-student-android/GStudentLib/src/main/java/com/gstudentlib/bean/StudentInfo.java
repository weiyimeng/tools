package com.gstudentlib.bean;

import java.io.Serializable;

/**
 * Created by 张旭 on 2017/11/20.
 * 邮箱：zhangxu0@gaosiedu.com
 */

public class StudentInfo implements Serializable{
    
    private String studentName; //学生名称（在获取选取用户的时候会用到）
    private String institutionName;//机构名称
    private String sex; //性别 1 男 0女
    private String id; //学生ID
    private String userId; //用户ID
    private String parentTel1; //账号手机号
    private String truthName; //学生姓名
    private int isBeiXiao; //是否是北校
    private String path;//用户头像

    //附加字段（开发自行添加作为标识位）
    private boolean selectStatus;

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isSelectStatus() {
        return selectStatus;
    }

    public void setSelectStatus(boolean selectStatus) {
        this.selectStatus = selectStatus;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentTel1() {
        return parentTel1;
    }

    public void setParentTel1(String parentTel1) {
        this.parentTel1 = parentTel1;
    }

    public String getTruthName() {
        return truthName;
    }

    public void setTruthName(String truthName) {
        this.truthName = truthName;
    }

    public int getIsBeiXiao() {
        return isBeiXiao;
    }

    public void setIsBeiXiao(int isBeixiao) {
        this.isBeiXiao = isBeixiao;
    }

    public String getPhone() {
        return parentTel1;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
