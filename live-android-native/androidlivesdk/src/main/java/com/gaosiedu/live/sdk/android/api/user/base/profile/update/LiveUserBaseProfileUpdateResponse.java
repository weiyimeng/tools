/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.base.profile.update;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/25 16:28
 * @since 2.1.0
 */
public class LiveUserBaseProfileUpdateResponse extends ResponseResult {
    
    private ResultData data;
    
    public ResultData getData() {
        return data;
    }
    
    public void setData(ResultData data) {
        this.data = data;
    }
    
    
    public static class ResultData {
        
        /**
         * 区编号
         */
        private String area;
        /**
         * 区名
         */
        private String areaName;
        /**
         * 生日
         */
        private String birthdayShow;
        /**
         * 市编号
         */
        private String city;
        /**
         * 城市名
         */
        private String cityName;
        /**
         * 金币值
         */
        private int gold;
        /**
         * 年级
         */
        private String grade;
        /**
         * 用户id
         */
        private int id;
        /**
         * 用户等级
         */
        private int level;
        /**
         * 用户等级名称
         */
        private String levelName;
        /**
         * 登录昵称
         */
        private String loginName;
        /**
         * 手机号
         */
        private String mobile;
        /**
         * 姓名
         */
        private String name;
        /**
         * 省编号
         */
        private String province;
        /**
         * 省名
         */
        private String provinceName;
        /**
         * 学校
         */
        private String school;
        /**
         * 性别
         */
        private String sex;
        /**
         * 头像
         */
        private String smallHeadimg;
        /**
         * 成长值
         */
        private int totalGrowth;
        
        //属性get||set方法
        
        
        public String getArea() {
            return this.area;
        }
        
        public void setArea(String area) {
            this.area = area;
        }
        
        
        public String getAreaName() {
            return this.areaName;
        }
        
        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }
        
        
        public String getBirthdayShow() {
            return this.birthdayShow;
        }
        
        public void setBirthdayShow(String birthday) {
            this.birthdayShow = birthday;
        }
        
        
        public String getCity() {
            return this.city;
        }
        
        public void setCity(String city) {
            this.city = city;
        }
        
        
        public String getCityName() {
            return this.cityName;
        }
        
        public void setCityName(String cityName) {
            this.cityName = cityName;
        }
        
        
        public int getGold() {
            return this.gold;
        }
        
        public void setGold(int gold) {
            this.gold = gold;
        }
        
        
        public String getGrade() {
            return this.grade;
        }
        
        public void setGrade(String grade) {
            this.grade = grade;
        }
        
        
        public int getId() {
            return this.id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        
        public int getLevel() {
            return this.level;
        }
        
        public void setLevel(int level) {
            this.level = level;
        }
        
        
        public String getLevelName() {
            return this.levelName;
        }
        
        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }
        
        
        public String getLoginName() {
            return this.loginName;
        }
        
        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }
        
        
        public String getMobile() {
            return this.mobile;
        }
        
        public void setMobile(String mobile) {
            this.mobile = mobile;
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
        
        
        public String getProvinceName() {
            return this.provinceName;
        }
        
        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
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
        
        
        public int getTotalGrowth() {
            return this.totalGrowth;
        }
        
        public void setTotalGrowth(int totalGrowth) {
            this.totalGrowth = totalGrowth;
        }
        
    }
}
