/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.relation;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/29 16:04
 * @since 2.1.0
 */
public class LiveCourseRelationListResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    private static class ResultData extends LiveSdkBasePageResponse {

        private List<ListData> list;

        public List<ListData> getList() {
            return list;
        }

        public void setList(List<ListData> list) {
            this.list = list;
        }

    }
    private static class ListData{
        /**
        * activityList
        */
        private  List<CourseActivityDomain> activityList;
        /**
        * addToShoppingCartable
        */
        private  boolean addToShoppingCartable;
        /**
        * applyDateTime
        */
        private  String applyDateTime;
        /**
        * assistants
        */
        private  List<TeacherDomain> assistants;
        /**
        * autoClass
        */
        private  int autoClass;
        /**
        * classDomainList
        */
        private  List<CourseClassDomain> classDomainList;
        /**
        * combo
        */
        private  int combo;
        /**
        * completedCount
        */
        private  int completedCount;
        /**
        * couponList
        */
        private  List<CourseCouponDomain> couponList;
        /**
        * courseClassDomain
        */
        private  CourseClassDomain courseClassDomain;
        /**
        * courseDesc
        */
        private  String courseDesc;
        /**
        * courseDifficulty
        */
        private  int courseDifficulty;
        /**
        * courseDuration
        */
        private  Double courseDuration;
        /**
        * courseEnclosureDomains
        */
        private  List<CourseEnclosureDomain> courseEnclosureDomains;
        /**
        * courseEnroll
        */
        private  int courseEnroll;
        /**
        * courseGradeIds
        */
        private  String courseGradeIds;
        /**
        * courseGradeNames
        */
        private  String courseGradeNames;
        /**
        * courseKnowledgeDomain
        */
        private  CourseKnowledgeDomain courseKnowledgeDomain;
        /**
        * coursePurchaseEnd
        */
        private  boolean coursePurchaseEnd;
        /**
        * coursePurchaseStart
        */
        private  boolean coursePurchaseStart;
        /**
        * courseRemark
        */
        private  String courseRemark;
        /**
        * courseServiceIds
        */
        private  String courseServiceIds;
        /**
        * courseServiceNames
        */
        private  String courseServiceNames;
        /**
        * courseStatus
        */
        private  int courseStatus;
        /**
        * courseSubjectIds
        */
        private  String courseSubjectIds;
        /**
        * courseSubjectNames
        */
        private  String courseSubjectNames;
        /**
        * courseType
        */
        private  int courseType;
        /**
        * courseTypeIds
        */
        private  String courseTypeIds;
        /**
        * courseTypeNames
        */
        private  String courseTypeNames;
        /**
        * courseUp
        */
        private  int courseUp;
        /**
        * courseYear
        */
        private  int courseYear;
        /**
        * createTime
        */
        private  String createTime;
        /**
        * createUserId
        */
        private  int createUserId;
        /**
        * deadline
        */
        private  String deadline;
        /**
        * deadlineStr
        */
        private  String deadlineStr;
        /**
        * deleteFlag
        */
        private  int deleteFlag;
        /**
        * enabletime
        */
        private  String enabletime;
        /**
        * enrollCount
        */
        private  int enrollCount;
        /**
        * exchangeFree
        */
        private  int exchangeFree;
        /**
        * expireTime
        */
        private  String expireTime;
        /**
        * expireTimeStr
        */
        private  String expireTimeStr;
        /**
        * iconUrl
        */
        private  String iconUrl;
        /**
        * id
        */
        private  int id;
        /**
        * info
        */
        private  String info;
        /**
        * isBegin
        */
        private  boolean isBegin;
        /**
        * isBuy
        */
        private  boolean isBuy;
        /**
        * isFree
        */
        private  boolean isFree;
        /**
        * isLike
        */
        private  boolean isLike;
        /**
        * isRefund
        */
        private  int isRefund;
        /**
        * isReplay
        */
        private  int isReplay;
        /**
        * knowledges
        */
        private  List<CourseKnowledgeDomain> knowledges;
        /**
        * lastLiveEndTime
        */
        private  String lastLiveEndTime;
        /**
        * lastLiveStartTime
        */
        private  String lastLiveStartTime;
        /**
        * lastLiveTimeString
        */
        private  String lastLiveTimeString;
        /**
        * listshow
        */
        private  int listshow;
        /**
        * liveStatus
        */
        private  int liveStatus;
        /**
        * materialPrice
        */
        private  BigDecimal materialPrice;
        /**
        * materielCost
        */
        private BigDecimal materielCost;
        /**
        * name
        */
        private  String name;
        /**
        * oldPrice
        */
        private  BigDecimal oldPrice;
        /**
        * orderId
        */
        private  int orderId;
        /**
        * passCourse
        */
        private  int passCourse;
        /**
        * payable
        */
        private  BigDecimal payable;
        /**
        * periodTime
        */
        private  String periodTime;
        /**
        * plan
        */
        private  String plan;
        /**
        * price
        */
        private  BigDecimal price;
        /**
        * publishTime
        */
        private  String publishTime;
        /**
        * purchasable
        */
        private  boolean purchasable;
        /**
        * restrictCount
        */
        private  int restrictCount;
        /**
        * returnFlag
        */
        private  boolean returnFlag;
        /**
        * scores
        */
        private  int scores;
        /**
        * shikanUrl
        */
        private  String shikanUrl;
        /**
        * status
        */
        private  int status;
        /**
        * storeHouseList
        */
        private  List<StoreHouseDomain> storeHouseList;
        /**
        * subject
        */
        private  String subject;
        /**
        * teacherIds
        */
        private  String teacherIds;
        /**
        * teacherNames
        */
        private  String teacherNames;
        /**
        * teachers
        */
        private  List<TeacherDomain> teachers;
        /**
        * term
        */
        private  int term;
        /**
        * testPaperId
        */
        private  int testPaperId;
        /**
        * time
        */
        private  String time;
        /**
        * timeremark
        */
        private  String timeremark;
        /**
        * type
        */
        private  String type;
        /**
        * updateTime
        */
        private  String updateTime;
        /**
        * userBuyCount
        */
        private  int userBuyCount;
        /**
        * userCollectCount
        */
        private  int userCollectCount;
        /**
        * userCourseId
        */
        private  int userCourseId;
        /**
        * userCourseStatus
        */
        private  int userCourseStatus;
        /**
        * userOrderItemStatus
        */
        private  int userOrderItemStatus;
        /**
        * validityTime
        */
        private  String validityTime;

        //属性get||set方法



        public List<CourseActivityDomain> getActivityList() {
            return this.activityList;
        }

        public void setActivityList(List<CourseActivityDomain> activityList) {
            this.activityList = activityList;
        }



        public boolean getAddToShoppingCartable() {
        return this.addToShoppingCartable;
        }

        public void setAddToShoppingCartable(boolean addToShoppingCartable) {
        this.addToShoppingCartable = addToShoppingCartable;
        }



        public String getApplyDateTime() {
        return this.applyDateTime;
        }

        public void setApplyDateTime(String applyDateTime) {
        this.applyDateTime = applyDateTime;
        }




        public List<TeacherDomain> getAssistants() {
            return this.assistants;
        }

        public void setAssistants(List<TeacherDomain> assistants) {
            this.assistants = assistants;
        }



        public int getAutoClass() {
        return this.autoClass;
        }

        public void setAutoClass(int autoClass) {
        this.autoClass = autoClass;
        }




        public List<CourseClassDomain> getClassDomainList() {
            return this.classDomainList;
        }

        public void setClassDomainList(List<CourseClassDomain> classDomainList) {
            this.classDomainList = classDomainList;
        }



        public int getCombo() {
        return this.combo;
        }

        public void setCombo(int combo) {
        this.combo = combo;
        }



        public int getCompletedCount() {
        return this.completedCount;
        }

        public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
        }




        public List<CourseCouponDomain> getCouponList() {
            return this.couponList;
        }

        public void setCouponList(List<CourseCouponDomain> couponList) {
            this.couponList = couponList;
        }



        public CourseClassDomain getCourseClassDomain() {
        return this.courseClassDomain;
        }

        public void setCourseClassDomain(CourseClassDomain courseClassDomain) {
        this.courseClassDomain = courseClassDomain;
        }



        public String getCourseDesc() {
        return this.courseDesc;
        }

        public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
        }



        public int getCourseDifficulty() {
        return this.courseDifficulty;
        }

        public void setCourseDifficulty(int courseDifficulty) {
        this.courseDifficulty = courseDifficulty;
        }



        public Double getCourseDuration() {
        return this.courseDuration;
        }

        public void setCourseDuration(Double courseDuration) {
        this.courseDuration = courseDuration;
        }




        public List<CourseEnclosureDomain> getCourseEnclosureDomains() {
            return this.courseEnclosureDomains;
        }

        public void setCourseEnclosureDomains(List<CourseEnclosureDomain> courseEnclosureDomains) {
            this.courseEnclosureDomains = courseEnclosureDomains;
        }



        public int getCourseEnroll() {
        return this.courseEnroll;
        }

        public void setCourseEnroll(int courseEnroll) {
        this.courseEnroll = courseEnroll;
        }



        public String getCourseGradeIds() {
        return this.courseGradeIds;
        }

        public void setCourseGradeIds(String courseGradeIds) {
        this.courseGradeIds = courseGradeIds;
        }



        public String getCourseGradeNames() {
        return this.courseGradeNames;
        }

        public void setCourseGradeNames(String courseGradeNames) {
        this.courseGradeNames = courseGradeNames;
        }



        public CourseKnowledgeDomain getCourseKnowledgeDomain() {
        return this.courseKnowledgeDomain;
        }

        public void setCourseKnowledgeDomain(CourseKnowledgeDomain courseKnowledgeDomain) {
        this.courseKnowledgeDomain = courseKnowledgeDomain;
        }



        public boolean getCoursePurchaseEnd() {
        return this.coursePurchaseEnd;
        }

        public void setCoursePurchaseEnd(boolean coursePurchaseEnd) {
        this.coursePurchaseEnd = coursePurchaseEnd;
        }



        public boolean getCoursePurchaseStart() {
        return this.coursePurchaseStart;
        }

        public void setCoursePurchaseStart(boolean coursePurchaseStart) {
        this.coursePurchaseStart = coursePurchaseStart;
        }



        public String getCourseRemark() {
        return this.courseRemark;
        }

        public void setCourseRemark(String courseRemark) {
        this.courseRemark = courseRemark;
        }



        public String getCourseServiceIds() {
        return this.courseServiceIds;
        }

        public void setCourseServiceIds(String courseServiceIds) {
        this.courseServiceIds = courseServiceIds;
        }



        public String getCourseServiceNames() {
        return this.courseServiceNames;
        }

        public void setCourseServiceNames(String courseServiceNames) {
        this.courseServiceNames = courseServiceNames;
        }



        public int getCourseStatus() {
        return this.courseStatus;
        }

        public void setCourseStatus(int courseStatus) {
        this.courseStatus = courseStatus;
        }



        public String getCourseSubjectIds() {
        return this.courseSubjectIds;
        }

        public void setCourseSubjectIds(String courseSubjectIds) {
        this.courseSubjectIds = courseSubjectIds;
        }



        public String getCourseSubjectNames() {
        return this.courseSubjectNames;
        }

        public void setCourseSubjectNames(String courseSubjectNames) {
        this.courseSubjectNames = courseSubjectNames;
        }



        public int getCourseType() {
        return this.courseType;
        }

        public void setCourseType(int courseType) {
        this.courseType = courseType;
        }



        public String getCourseTypeIds() {
        return this.courseTypeIds;
        }

        public void setCourseTypeIds(String courseTypeIds) {
        this.courseTypeIds = courseTypeIds;
        }



        public String getCourseTypeNames() {
        return this.courseTypeNames;
        }

        public void setCourseTypeNames(String courseTypeNames) {
        this.courseTypeNames = courseTypeNames;
        }



        public int getCourseUp() {
        return this.courseUp;
        }

        public void setCourseUp(int courseUp) {
        this.courseUp = courseUp;
        }



        public int getCourseYear() {
        return this.courseYear;
        }

        public void setCourseYear(int courseYear) {
        this.courseYear = courseYear;
        }



        public String getCreateTime() {
        return this.createTime;
        }

        public void setCreateTime(String createTime) {
        this.createTime = createTime;
        }



        public int getCreateUserId() {
        return this.createUserId;
        }

        public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
        }



        public String getDeadline() {
        return this.deadline;
        }

        public void setDeadline(String deadline) {
        this.deadline = deadline;
        }



        public String getDeadlineStr() {
        return this.deadlineStr;
        }

        public void setDeadlineStr(String deadlineStr) {
        this.deadlineStr = deadlineStr;
        }



        public int getDeleteFlag() {
        return this.deleteFlag;
        }

        public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
        }



        public String getEnabletime() {
        return this.enabletime;
        }

        public void setEnabletime(String enabletime) {
        this.enabletime = enabletime;
        }



        public int getEnrollCount() {
        return this.enrollCount;
        }

        public void setEnrollCount(int enrollCount) {
        this.enrollCount = enrollCount;
        }



        public int getExchangeFree() {
        return this.exchangeFree;
        }

        public void setExchangeFree(int exchangeFree) {
        this.exchangeFree = exchangeFree;
        }



        public String getExpireTime() {
        return this.expireTime;
        }

        public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
        }



        public String getExpireTimeStr() {
        return this.expireTimeStr;
        }

        public void setExpireTimeStr(String expireTimeStr) {
        this.expireTimeStr = expireTimeStr;
        }



        public String getIconUrl() {
        return this.iconUrl;
        }

        public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
        }



        public int getId() {
        return this.id;
        }

        public void setId(int id) {
        this.id = id;
        }



        public String getInfo() {
        return this.info;
        }

        public void setInfo(String info) {
        this.info = info;
        }



        public boolean getIsBegin() {
        return this.isBegin;
        }

        public void setIsBegin(boolean isBegin) {
        this.isBegin = isBegin;
        }



        public boolean getIsBuy() {
        return this.isBuy;
        }

        public void setIsBuy(boolean isBuy) {
        this.isBuy = isBuy;
        }



        public boolean getIsFree() {
        return this.isFree;
        }

        public void setIsFree(boolean isFree) {
        this.isFree = isFree;
        }



        public boolean getIsLike() {
        return this.isLike;
        }

        public void setIsLike(boolean isLike) {
        this.isLike = isLike;
        }



        public int getIsRefund() {
        return this.isRefund;
        }

        public void setIsRefund(int isRefund) {
        this.isRefund = isRefund;
        }



        public int getIsReplay() {
        return this.isReplay;
        }

        public void setIsReplay(int isReplay) {
        this.isReplay = isReplay;
        }




        public List<CourseKnowledgeDomain> getKnowledges() {
            return this.knowledges;
        }

        public void setKnowledges(List<CourseKnowledgeDomain> knowledges) {
            this.knowledges = knowledges;
        }



        public String getLastLiveEndTime() {
        return this.lastLiveEndTime;
        }

        public void setLastLiveEndTime(String lastLiveEndTime) {
        this.lastLiveEndTime = lastLiveEndTime;
        }



        public String getLastLiveStartTime() {
        return this.lastLiveStartTime;
        }

        public void setLastLiveStartTime(String lastLiveStartTime) {
        this.lastLiveStartTime = lastLiveStartTime;
        }



        public String getLastLiveTimeString() {
        return this.lastLiveTimeString;
        }

        public void setLastLiveTimeString(String lastLiveTimeString) {
        this.lastLiveTimeString = lastLiveTimeString;
        }



        public int getListshow() {
        return this.listshow;
        }

        public void setListshow(int listshow) {
        this.listshow = listshow;
        }



        public int getLiveStatus() {
        return this.liveStatus;
        }

        public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
        }



        public BigDecimal getMaterialPrice() {
        return this.materialPrice;
        }

        public void setMaterialPrice(BigDecimal materialPrice) {
        this.materialPrice = materialPrice;
        }



        public BigDecimal getMaterielCost() {
        return this.materielCost;
        }

        public void setMaterielCost(BigDecimal materielCost) {
        this.materielCost = materielCost;
        }



        public String getName() {
        return this.name;
        }

        public void setName(String name) {
        this.name = name;
        }



        public BigDecimal getOldPrice() {
        return this.oldPrice;
        }

        public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
        }



        public int getOrderId() {
        return this.orderId;
        }

        public void setOrderId(int orderId) {
        this.orderId = orderId;
        }



        public int getPassCourse() {
        return this.passCourse;
        }

        public void setPassCourse(int passCourse) {
        this.passCourse = passCourse;
        }



        public BigDecimal getPayable() {
        return this.payable;
        }

        public void setPayable(BigDecimal payable) {
        this.payable = payable;
        }



        public String getPeriodTime() {
        return this.periodTime;
        }

        public void setPeriodTime(String periodTime) {
        this.periodTime = periodTime;
        }



        public String getPlan() {
        return this.plan;
        }

        public void setPlan(String plan) {
        this.plan = plan;
        }



        public BigDecimal getPrice() {
        return this.price;
        }

        public void setPrice(BigDecimal price) {
        this.price = price;
        }



        public String getPublishTime() {
        return this.publishTime;
        }

        public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
        }



        public boolean getPurchasable() {
        return this.purchasable;
        }

        public void setPurchasable(boolean purchasable) {
        this.purchasable = purchasable;
        }



        public int getRestrictCount() {
        return this.restrictCount;
        }

        public void setRestrictCount(int restrictCount) {
        this.restrictCount = restrictCount;
        }



        public boolean getReturnFlag() {
        return this.returnFlag;
        }

        public void setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
        }



        public int getScores() {
        return this.scores;
        }

        public void setScores(int scores) {
        this.scores = scores;
        }



        public String getShikanUrl() {
        return this.shikanUrl;
        }

        public void setShikanUrl(String shikanUrl) {
        this.shikanUrl = shikanUrl;
        }



        public int getStatus() {
        return this.status;
        }

        public void setStatus(int status) {
        this.status = status;
        }




        public List<StoreHouseDomain> getStoreHouseList() {
            return this.storeHouseList;
        }

        public void setStoreHouseList(List<StoreHouseDomain> storeHouseList) {
            this.storeHouseList = storeHouseList;
        }



        public String getSubject() {
        return this.subject;
        }

        public void setSubject(String subject) {
        this.subject = subject;
        }



        public String getTeacherIds() {
        return this.teacherIds;
        }

        public void setTeacherIds(String teacherIds) {
        this.teacherIds = teacherIds;
        }



        public String getTeacherNames() {
        return this.teacherNames;
        }

        public void setTeacherNames(String teacherNames) {
        this.teacherNames = teacherNames;
        }




        public List<TeacherDomain> getTeachers() {
            return this.teachers;
        }

        public void setTeachers(List<TeacherDomain> teachers) {
            this.teachers = teachers;
        }



        public int getTerm() {
        return this.term;
        }

        public void setTerm(int term) {
        this.term = term;
        }



        public int getTestPaperId() {
        return this.testPaperId;
        }

        public void setTestPaperId(int testPaperId) {
        this.testPaperId = testPaperId;
        }



        public String getTime() {
        return this.time;
        }

        public void setTime(String time) {
        this.time = time;
        }



        public String getTimeremark() {
        return this.timeremark;
        }

        public void setTimeremark(String timeremark) {
        this.timeremark = timeremark;
        }



        public String getType() {
        return this.type;
        }

        public void setType(String type) {
        this.type = type;
        }



        public String getUpdateTime() {
        return this.updateTime;
        }

        public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        }



        public int getUserBuyCount() {
        return this.userBuyCount;
        }

        public void setUserBuyCount(int userBuyCount) {
        this.userBuyCount = userBuyCount;
        }



        public int getUserCollectCount() {
        return this.userCollectCount;
        }

        public void setUserCollectCount(int userCollectCount) {
        this.userCollectCount = userCollectCount;
        }



        public int getUserCourseId() {
        return this.userCourseId;
        }

        public void setUserCourseId(int userCourseId) {
        this.userCourseId = userCourseId;
        }



        public int getUserCourseStatus() {
        return this.userCourseStatus;
        }

        public void setUserCourseStatus(int userCourseStatus) {
        this.userCourseStatus = userCourseStatus;
        }



        public int getUserOrderItemStatus() {
        return this.userOrderItemStatus;
        }

        public void setUserOrderItemStatus(int userOrderItemStatus) {
        this.userOrderItemStatus = userOrderItemStatus;
        }



        public String getValidityTime() {
        return this.validityTime;
        }

        public void setValidityTime(String validityTime) {
        this.validityTime = validityTime;
        }

    }
}
