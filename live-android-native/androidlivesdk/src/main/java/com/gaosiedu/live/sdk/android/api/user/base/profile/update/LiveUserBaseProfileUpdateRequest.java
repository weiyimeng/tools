/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.base.profile.update;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
* @author sdk-generator-android request
* @describe
* @date 2018/09/25 16:28
* @since 2.1.0
*/
public class LiveUserBaseProfileUpdateRequest  extends LiveSdkBaseRequest {

    private transient final String PATH = "user/update";

    public LiveUserBaseProfileUpdateRequest() {
        super();
        setPath(PATH);
    }


    /**
     * 区编号
     */
    private String area;

    /**
     * 生日：按格式yyyyMMdd传递
     */
    private String birthdayStr;

    /**
     * 省市编号
     */
    private String city;

    /**
     * 年级
     */
    private String grade;

    /**
     * 用户昵称
     */
    private String loginName;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 省id
     */
    private String province;

    /**
     * 学校
     */
    private String school;

    /**
     * 性别：m为男，s为女
     */
    private String sex;

    /**
     * 用户头像
     */
    private String smallHeadimg;

    /**
     * 用户id
     */
    private Integer userId;


    //属性get||set方法
    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBirthdayStr() {
        return this.birthdayStr;
    }

    public void setBirthdayStr(String birthdayStr) {
        this.birthdayStr = birthdayStr;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSchool() {
        return this.school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSmallHeadimg() {
        return this.smallHeadimg;
    }

    public void setSmallHeadimg(String smallHeadimg) {
        this.smallHeadimg = smallHeadimg;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}