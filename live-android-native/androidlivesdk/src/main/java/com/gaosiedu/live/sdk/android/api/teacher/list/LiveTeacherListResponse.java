/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.teacher.list;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.live.sdk.android.domain.*;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/29 19:15
 * @since 2.1.0
 */
public class LiveTeacherListResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData extends LiveSdkBasePageResponse {

        private List<ListData> list;

        public List<ListData> getList() {
            return list;
        }

        public void setList(List<ListData> list) {
            this.list = list;
        }

    }
    public static class ListData{
        /**
        * bgImg
        */
        private  String bgImg;
        /**
        * coach
        */
        private  int coach;
        /**
        * courseList
        */
        private  List<CourseDomain> courseList;
        /**
        * credentialsCode
        */
        private  String credentialsCode;
        /**
        * credentialsType
        */
        private  int credentialsType;
        /**
        * detailsId
        */
        private  int detailsId;
        /**
        * gradeIds
        */
        private  String gradeIds;
        /**
        * gradeNames
        */
        private  String gradeNames;
        /**
        * headUrl
        */
        private  String headUrl;
        /**
        * honour
        */
        private  String honour;
        /**
        * id
        */
        private  int id;
        /**
        * isLike
        */
        private  boolean isLike;
        /**
        * loginName
        */
        private  String loginName;
        /**
        * mobile
        */
        private  String mobile;
        /**
        * namePinyin
        */
        private  String namePinyin;
        /**
        * realname
        */
        private  String realname;
        /**
        * smallImg
        */
        private  String smallImg;
        /**
        * speciality
        */
        private  String speciality;
        /**
        * status
        */
        private  int status;
        /**
        * subjectIds
        */
        private  String subjectIds;
        /**
        * subjectNames
        */
        private  String subjectNames;
        /**
        * tagIds
        */
        private  String tagIds;
        /**
        * tagNames
        */
        private  String tagNames;
        /**
        * teacherDesc
        */
        private  String teacherDesc;
        /**
        * teacherIcon
        */
        private  String teacherIcon;
        /**
        * teacherImg
        */
        private  String teacherImg;
        /**
        * teacherLevel
        */
        private  int teacherLevel;
        /**
        * teacherWxId
        */
        private  String teacherWxId;
        /**
        * type
        */
        private  int type;
        /**
        * userId
        */
        private  int userId;
        /**
        * wxUrl
        */
        private  String wxUrl;

        //属性get||set方法


        public String getBgImg() {
        return this.bgImg;
        }

        public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
        }



        public int getCoach() {
        return this.coach;
        }

        public void setCoach(int coach) {
        this.coach = coach;
        }




        public List<CourseDomain> getCourseList() {
            return this.courseList;
        }

        public void setCourseList(List<CourseDomain> courseList) {
            this.courseList = courseList;
        }



        public String getCredentialsCode() {
        return this.credentialsCode;
        }

        public void setCredentialsCode(String credentialsCode) {
        this.credentialsCode = credentialsCode;
        }



        public int getCredentialsType() {
        return this.credentialsType;
        }

        public void setCredentialsType(int credentialsType) {
        this.credentialsType = credentialsType;
        }



        public int getDetailsId() {
        return this.detailsId;
        }

        public void setDetailsId(int detailsId) {
        this.detailsId = detailsId;
        }



        public String getGradeIds() {
        return this.gradeIds;
        }

        public void setGradeIds(String gradeIds) {
        this.gradeIds = gradeIds;
        }



        public String getGradeNames() {
        return this.gradeNames;
        }

        public void setGradeNames(String gradeNames) {
        this.gradeNames = gradeNames;
        }



        public String getHeadUrl() {
        return this.headUrl;
        }

        public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
        }



        public String getHonour() {
        return this.honour;
        }

        public void setHonour(String honour) {
        this.honour = honour;
        }



        public int getId() {
        return this.id;
        }

        public void setId(int id) {
        this.id = id;
        }



        public boolean getIsLike() {
        return this.isLike;
        }

        public void setIsLike(boolean isLike) {
        this.isLike = isLike;
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



        public String getNamePinyin() {
        return this.namePinyin;
        }

        public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
        }



        public String getRealname() {
        return this.realname;
        }

        public void setRealname(String realname) {
        this.realname = realname;
        }



        public String getSmallImg() {
        return this.smallImg;
        }

        public void setSmallImg(String smallImg) {
        this.smallImg = smallImg;
        }



        public String getSpeciality() {
        return this.speciality;
        }

        public void setSpeciality(String speciality) {
        this.speciality = speciality;
        }



        public int getStatus() {
        return this.status;
        }

        public void setStatus(int status) {
        this.status = status;
        }



        public String getSubjectIds() {
        return this.subjectIds;
        }

        public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
        }



        public String getSubjectNames() {
        return this.subjectNames;
        }

        public void setSubjectNames(String subjectNames) {
        this.subjectNames = subjectNames;
        }



        public String getTagIds() {
        return this.tagIds;
        }

        public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
        }



        public String getTagNames() {
        return this.tagNames;
        }

        public void setTagNames(String tagNames) {
        this.tagNames = tagNames;
        }



        public String getTeacherDesc() {
        return this.teacherDesc;
        }

        public void setTeacherDesc(String teacherDesc) {
        this.teacherDesc = teacherDesc;
        }



        public String getTeacherIcon() {
        return this.teacherIcon;
        }

        public void setTeacherIcon(String teacherIcon) {
        this.teacherIcon = teacherIcon;
        }



        public String getTeacherImg() {
        return this.teacherImg;
        }

        public void setTeacherImg(String teacherImg) {
        this.teacherImg = teacherImg;
        }



        public int getTeacherLevel() {
        return this.teacherLevel;
        }

        public void setTeacherLevel(int teacherLevel) {
        this.teacherLevel = teacherLevel;
        }



        public String getTeacherWxId() {
        return this.teacherWxId;
        }

        public void setTeacherWxId(String teacherWxId) {
        this.teacherWxId = teacherWxId;
        }



        public int getType() {
        return this.type;
        }

        public void setType(int type) {
        this.type = type;
        }



        public int getUserId() {
        return this.userId;
        }

        public void setUserId(int userId) {
        this.userId = userId;
        }



        public String getWxUrl() {
        return this.wxUrl;
        }

        public void setWxUrl(String wxUrl) {
        this.wxUrl = wxUrl;
        }

    }
}
