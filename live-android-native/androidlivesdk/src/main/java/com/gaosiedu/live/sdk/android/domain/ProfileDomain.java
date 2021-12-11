/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 用户基本信息
 *
 * @author lixun
 * @describe
 * @date 2017/7/25 17:44
 * @since 2.1.0
 */
public class ProfileDomain implements Serializable {

    private int id;

    private String loginName;

    private String password;

    private String name;

    private int score;

    private int status;

    private int organizationId;

    private int role;

    private String sex;

    private String mobile;

    private Date birthday;

    private String birthdayStr;

    private String grade;

    private String school;

    private String address;

    private String province;

    private String city;
    /**
     * 所在的区  add lht 2018-8-1
     */
    private String area;

    private String qq;

    private String email;

    private String smallHeadimg;

    private String headimg;

    private Date createTime;

    private Date graduationYear;

    private String realSchool;

    private int realSchoolId;

    private String realProvince;

    private int officialSchoolNo;

    private String contactPhone;

    private String recipientName;

    private String studentNumber;

    /**
     * 伙伴
     */
    private PartnerUserDoamin partnerUser;

    /**
     * 用户标识(标签)
     */
    private Map<String,String> flags;
    /**
     * 用户余额 lht 2018-9-6 add
     */
    private BigDecimal balance;
    /**
     *等级名称
     */
    private String levelName;

    /**
     * 段位
     */
    private int level;

    /**
     * 等级
     */
    private int rank;
    /**
     * 总成长值
     */
    private int totalGrowth;
    /**
     * 用户金币
     */
    private int gold;

    /**
     * 信息完善的百分比
     */
    private String completeRate;


    public String getBirthdayStr() {
        return birthdayStr;
    }

    public void setBirthdayStr(String birthdayStr) {
        this.birthdayStr = birthdayStr;
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
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade == null ? null : grade.trim();
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school == null ? null : school.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getSmallHeadimg() {
        return smallHeadimg;
    }

    public void setSmallHeadimg(String smallHeadimg) {
        this.smallHeadimg = smallHeadimg == null ? null : smallHeadimg.trim();
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Date graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getRealSchool() {
        return realSchool;
    }

    public void setRealSchool(String realSchool) {
        this.realSchool = realSchool == null ? null : realSchool.trim();
    }

    public int getRealSchoolId() {
        return realSchoolId;
    }

    public void setRealSchoolId(int realSchoolId) {
        this.realSchoolId = realSchoolId;
    }

    public String getRealProvince() {
        return realProvince;
    }

    public void setRealProvince(String realProvince) {
        this.realProvince = realProvince == null ? null : realProvince.trim();
    }

    public int getOfficialSchoolNo() {
        return officialSchoolNo;
    }

    public void setOfficialSchoolNo(int officialSchoolNo) {
        this.officialSchoolNo = officialSchoolNo;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName == null ? null : recipientName.trim();
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber == null ? null : studentNumber.trim();
    }

    public PartnerUserDoamin getPartnerUser() {
        return partnerUser;
    }

    public void setPartnerUser(PartnerUserDoamin partnerUser) {
        this.partnerUser = partnerUser;
    }

    public Map<String, String> getFlags() {
        return flags;
    }

    public void setFlags(Map<String, String> flags) {
        this.flags = flags;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getTotalGrowth() {
        return totalGrowth;
    }

    public void setTotalGrowth(int totalGrowth) {
        this.totalGrowth = totalGrowth;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String getCompleteRate() {
        return completeRate;
    }

    public void setCompleteRate(String completeRate) {
        this.completeRate = completeRate;
    }
}
