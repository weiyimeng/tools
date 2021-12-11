package com.haoke91.a91edu.entities;

import com.blankj.utilcode.util.ObjectUtils;
import com.gaosiedu.live.sdk.android.base.LiveSdkHelper;

import java.io.Serializable;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/6 下午7:43
 * 修改人：weiyimeng
 * 修改时间：2018/8/6 下午7:43
 * 修改备注：
 */
public class UserInfo implements Serializable {
    
    
    /**
     * id : 7919
     * loginName : 连同学
     * name : 涛涛44
     * score : 8945
     * sex : m
     * mobile : 13716151311
     * birthday : 1276876800000
     * grade : 4
     * school : 清华附中
     * address :
     * province : null
     * city : null
     * headimg : head/1491103856989_7919.png
     * realSchool :
     * realSchoolId : null
     * studentNumber : 161270619
     * flags : null
     * registerFlag : false
     */
    
    private int id;
    private String loginName;
    private String name;
    private int score;
    private String sex;
    private String mobile;
    private String birthday;
    private String birthdayShow;
    private String grade;
    private String school;
    private String address;
    private String province;
    private String city;
    private String area;
    private String headimg;
    private String realSchool;
    private String realSchoolId;
    private String studentNumber;
    private String flags;
    private boolean registerFlag;
    private String smallHeadimg;
    private String areaName = "";
    private String cityName = "";
    private String provinceName = "";
    private String levelName;
    private int totalGrowth;
    private int level;
    private int gold;
    
    public String getArea() {
        return area;
    }
    
    public void setArea(String area) {
        this.area = area;
    }
    
    public String getBirthdayShow() {
        return birthdayShow;
    }
    
    public void setBirthdayShow(String birthdayShow) {
        this.birthdayShow = birthdayShow;
    }
    
    public String getLevelName() {
        return levelName;
    }
    
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
    
    public int getTotalGrowth() {
        return totalGrowth;
    }
    
    public void setTotalGrowth(int totalGrowth) {
        this.totalGrowth = totalGrowth;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public int getGold() {
        return gold;
    }
    
    public void setGold(int gold) {
        this.gold = gold;
    }
    
    public String getAreaName() {
        return areaName;
    }
    
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    public String getProvinceName() {
        return provinceName;
    }
    
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    
    public String getSmallHeadimg() {
        return smallHeadimg;
    }
    
    public void setSmallHeadimg(String smallHeadimg) {
        this.smallHeadimg = smallHeadimg;
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public String getSex() {
        return sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }
    
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public String getBirthday() {
        return birthday;
    }
    
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    public String getGrade() {
        if (ObjectUtils.isEmpty(grade)) {
            return String.valueOf(LiveSdkHelper.DEFAULT);
        }
        return grade;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public String getSchool() {
        return school;
    }
    
    public void setSchool(String school) {
        this.school = school;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getProvince() {
        return province;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getHeadimg() {
        return headimg;
    }
    
    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }
    
    public String getRealSchool() {
        return realSchool;
    }
    
    public void setRealSchool(String realSchool) {
        this.realSchool = realSchool;
    }
    
    public Object getRealSchoolId() {
        return realSchoolId;
    }
    
    public void setRealSchoolId(String realSchoolId) {
        this.realSchoolId = realSchoolId;
    }
    
    public String getStudentNumber() {
        return studentNumber;
    }
    
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    
    public Object getFlags() {
        return flags;
    }
    
    public void setFlags(String flags) {
        this.flags = flags;
    }
    
    public boolean isRegisterFlag() {
        return registerFlag;
    }
    
    public void setRegisterFlag(boolean registerFlag) {
        this.registerFlag = registerFlag;
        
    }
}
