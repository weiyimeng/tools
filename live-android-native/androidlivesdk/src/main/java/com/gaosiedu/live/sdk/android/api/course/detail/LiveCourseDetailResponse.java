/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.detail;


import android.text.TextUtils;
import android.text.format.Time;

import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/10/09 09:59
 * @since 2.1.0
 */
public class LiveCourseDetailResponse extends ResponseResult {
    
    private ResultData data;
    
    public ResultData getData() {
        return data;
    }
    
    public void setData(ResultData data) {
        this.data = data;
    }
    
    
    public static class ResultData {
        
        /**
         * activityList
         */
        private List<CourseActivityDomain> activityList;
        /**
         * addToShoppingCartable
         */
        private boolean addToShoppingCartable;
        /**
         * applyDateTime
         */
        private String applyDateTime;
        /**
         * assistants
         */
        private List<TeacherDomain> assistants;
        /**
         * autoClass
         */
        private int autoClass;
        /**
         * classDomainList
         */
        private List<CourseClassDomain> classDomainList;
        /**
         * combo
         */
        private int combo;
        /**
         * completedCount
         */
        private int completedCount;
        /**
         * couponList
         */
        private List<CourseCouponDomain> couponList;
        /**
         * courseClassDomain
         */
        private CourseClassDomain courseClassDomain;
        /**
         * courseDesc
         */
        private String courseDesc;
        /**
         * courseDifficulty
         */
        private int courseDifficulty;
        /**
         * courseDuration
         */
        private Double courseDuration;
        /**
         * courseEnclosureDomains
         */
        private List<CourseEnclosureDomain> courseEnclosureDomains;
        /**
         * courseEnroll
         */
        private int courseEnroll;
        /**
         * courseGradeIds
         */
        private String courseGradeIds;
        /**
         * courseGradeNames
         */
        private String courseGradeNames;
        /**
         * courseKnowledgeDomain
         */
        private CourseKnowledgeDomain courseKnowledgeDomain;
        /**
         * coursePurchaseEnd
         */
        private boolean coursePurchaseEnd;
        /**
         * coursePurchaseStart
         */
        private boolean coursePurchaseStart;
        /**
         * courseRemark
         */
        private String courseRemark;
        /**
         * courseServiceIds
         */
        private String courseServiceIds;
        /**
         * courseServiceNames
         */
        private String courseServiceNames;
        /**
         * courseStatus
         */
        private int courseStatus;
        /**
         * courseSubjectIds
         */
        private String courseSubjectIds;
        /**
         * courseSubjectNames
         */
        private String courseSubjectNames;
        /**
         * courseType
         */
        private int courseType;
        /**
         * courseTypeIds
         */
        private String courseTypeIds;
        /**
         * courseTypeNames
         */
        private String courseTypeNames;
        /**
         * courseUp
         */
        private int courseUp;
        /**
         * courseYear
         */
        private int courseYear;
        private int knowlageCount;
        
        /**
         * createTime
         */
        private String createTime;
        /**
         * createUserId
         */
        private int createUserId;
        /**
         * deadline
         */
        private String deadline;
        /**
         * deadlineStr
         */
        private String deadlineStr;
        /**
         * deleteFlag
         */
        private int deleteFlag;
        /**
         * enabletime
         */
        private String enabletime;
        /**
         * enrollCount
         */
        private int enrollCount;
        /**
         * exchangeFree
         */
        private int exchangeFree;
        /**
         * expireTime
         */
        private String expireTime;
        /**
         * expireTimeStr
         */
        private String expireTimeStr;
        /**
         * iconUrl
         */
        private String iconUrl;
        /**
         * id
         */
        private int id;
        /**
         * info
         */
        private String info;
        /**
         * isBegin
         */
        private boolean isBegin;
        /**
         * isBuy
         */
        private boolean isBuy;
        /**
         * isFree
         */
        private boolean isFree;
        /**
         * isLike
         */
        private boolean isLike;
        /**
         * isRefund
         */
        private int isRefund;
        /**
         * isReplay
         */
        private int isReplay;
        /**
         * knowledges
         */
        private List<CourseKnowledgeDomain> knowledges;
        /**
         * lastLiveEndTime
         */
        private String lastLiveEndTime;
        /**
         * lastLiveStartTime
         */
        private String lastLiveStartTime;
        /**
         * lastLiveTimeString
         */
        private String lastLiveTimeString;
        /**
         * listshow
         */
        private int listshow;
        /**
         * liveStatus
         */
        private int liveStatus;
        /**
         * materialPrice
         */
        private BigDecimal materialPrice;
        /**
         * materielCost
         */
        private BigDecimal materielCost;
        /**
         * name
         */
        private String name;
        /**
         * oldPrice
         */
        private BigDecimal oldPrice;
        /**
         * orderId
         */
        private int orderId;
        /**
         * 是否在购物车中添加:true标识过了报名截止时间，false标识没有过报名截止时间
         */
        private boolean overDeadTimeStatus;
        /**
         * passCourse
         */
        private int passCourse;
        /**
         * payable
         */
        private BigDecimal payable;
        /**
         * periodTime
         */
        private String periodTime;
        /**
         * plan
         */
        private String plan;
        /**
         * price
         */
        private BigDecimal price;
        /**
         * publishTime
         */
        private String publishTime;
        /**
         * purchasable
         */
        private boolean purchasable;
        /**
         * restrictCount
         */
        private int restrictCount;
        /**
         * returnFlag
         */
        private boolean returnFlag;
        /**
         * scores
         */
        private int scores;
        /**
         * shikanUrl
         */
        private String shikanUrl;
        /**
         * status
         */
        private Integer status;
        /**
         * storeHouseList
         */
        private List<StoreHouseDomain> storeHouseList;
        /**
         * subject
         */
        private String subject;
        /**
         * teacherIds
         */
        private String teacherIds;
        /**
         * teacherNames
         */
        private String teacherNames;
        /**
         * teachers
         */
        private List<TeacherDomain> teachers;
        /**
         * templateDomains
         */
        private List<CourseTemplateDomain> templateDomains;
        /**
         * term
         */
        private Integer term;
        /**
         * testPaperId
         */
        private Integer testPaperId;
        /**
         * time
         */
        private String time;
        /**
         * timeremark
         */
        private String timeremark;
        /**
         * type
         */
        private String type;
        /**
         * updateTime
         */
        private String updateTime;
        /**
         * userBuyCount
         */
        private Integer userBuyCount;
        /**
         * userCollectCount
         */
        private Integer userCollectCount;
        /**
         * userCourseId
         */
        private Integer userCourseId;
        /**
         * userCourseStatus
         */
        private Integer userCourseStatus;
        /**
         * userOrderItemStatus
         */
        private Integer userOrderItemStatus;
        /**
         * validityTime
         */
        private String validityTime;
        
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
        
        
        public Integer getAutoClass() {
            return this.autoClass;
        }
        
        public void setAutoClass(Integer autoClass) {
            this.autoClass = autoClass;
        }
        
        
        public List<CourseClassDomain> getClassDomainList() {
            return this.classDomainList;
        }
        
        public void setClassDomainList(List<CourseClassDomain> classDomainList) {
            this.classDomainList = classDomainList;
        }
        
        
        public Integer getCombo() {
            return this.combo;
        }
        
        public void setCombo(Integer combo) {
            this.combo = combo;
        }
        
        
        public Integer getCompletedCount() {
            return this.completedCount;
        }
        
        public void setCompletedCount(Integer completedCount) {
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
        
        
        public Integer getCourseDifficulty() {
            return this.courseDifficulty;
        }
        
        public void setCourseDifficulty(Integer courseDifficulty) {
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
        
        
        public Integer getCourseEnroll() {
            return this.courseEnroll;
        }
        
        public void setCourseEnroll(Integer courseEnroll) {
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
        
        
        public Integer getCourseStatus() {
            return this.courseStatus;
        }
        
        public void setCourseStatus(Integer courseStatus) {
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
        
        
        public Integer getCourseType() {
            return this.courseType;
        }
        
        public void setCourseType(Integer courseType) {
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
        
        
        public Integer getCourseUp() {
            return this.courseUp;
        }
        
        public void setCourseUp(Integer courseUp) {
            this.courseUp = courseUp;
        }
        
        
        public Integer getCourseYear() {
            return this.courseYear;
        }
        
        public void setCourseYear(Integer courseYear) {
            this.courseYear = courseYear;
        }
        
        
        public String getCreateTime() {
            return this.createTime;
        }
        
        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        
        
        public Integer getCreateUserId() {
            return this.createUserId;
        }
        
        public void setCreateUserId(Integer createUserId) {
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
        
        
        public Integer getDeleteFlag() {
            return this.deleteFlag;
        }
        
        public void setDeleteFlag(Integer deleteFlag) {
            this.deleteFlag = deleteFlag;
        }
        
        
        public String getEnabletime() {
            return this.enabletime;
        }
        
        public void setEnabletime(String enabletime) {
            this.enabletime = enabletime;
        }
        
        
        public Integer getEnrollCount() {
            return this.enrollCount;
        }
        
        public void setEnrollCount(Integer enrollCount) {
            this.enrollCount = enrollCount;
        }
        
        
        public Integer getExchangeFree() {
            return this.exchangeFree;
        }
        
        public void setExchangeFree(Integer exchangeFree) {
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
        
        
        public Integer getId() {
            return this.id;
        }
        
        public void setId(Integer id) {
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
        
        
        public Integer getIsRefund() {
            return this.isRefund;
        }
        
        public void setIsRefund(Integer isRefund) {
            this.isRefund = isRefund;
        }
        
        
        public Integer getIsReplay() {
            return this.isReplay;
        }
        
        public void setIsReplay(Integer isReplay) {
            this.isReplay = isReplay;
        }
        
        
        public int getKnowlageCount() {
            return knowlageCount;
        }
        
        public void setKnowlageCount(int knowlageCount) {
            this.knowlageCount = knowlageCount;
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
        
        
        public Integer getListshow() {
            return this.listshow;
        }
        
        public void setListshow(Integer listshow) {
            this.listshow = listshow;
        }
        
        
        public Integer getLiveStatus() {
            return this.liveStatus;
        }
        
        public void setLiveStatus(Integer liveStatus) {
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
        
        
        public Integer getOrderId() {
            return this.orderId;
        }
        
        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }
        
        
        public boolean getOverDeadTimeStatus() {
            return this.overDeadTimeStatus;
        }
        
        public void setOverDeadTimeStatus(boolean overDeadTimeStatus) {
            this.overDeadTimeStatus = overDeadTimeStatus;
        }
        
        
        public Integer getPassCourse() {
            return this.passCourse;
        }
        
        public void setPassCourse(Integer passCourse) {
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
        
        
        public Integer getRestrictCount() {
            return this.restrictCount;
        }
        
        public void setRestrictCount(Integer restrictCount) {
            this.restrictCount = restrictCount;
        }
        
        
        public boolean getReturnFlag() {
            return this.returnFlag;
        }
        
        public void setReturnFlag(boolean returnFlag) {
            this.returnFlag = returnFlag;
        }
        
        
        public Integer getScores() {
            return this.scores;
        }
        
        public void setScores(Integer scores) {
            this.scores = scores;
        }
        
        
        public String getShikanUrl() {
            return this.shikanUrl;
        }
        
        public void setShikanUrl(String shikanUrl) {
            this.shikanUrl = shikanUrl;
        }
        
        
        public Integer getStatus() {
            return this.status;
        }
        
        public void setStatus(Integer status) {
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
        
        
        public List<CourseTemplateDomain> getTemplateDomains() {
            return this.templateDomains;
        }
        
        public void setTemplateDomains(List<CourseTemplateDomain> templateDomains) {
            this.templateDomains = templateDomains;
        }
        
        
        public Integer getTerm() {
            return this.term;
        }
        
        public void setTerm(Integer term) {
            this.term = term;
        }
        
        
        public Integer getTestPaperId() {
            return this.testPaperId;
        }
        
        public void setTestPaperId(Integer testPaperId) {
            this.testPaperId = testPaperId;
        }
        
        
        public String getTime() {
            return this.time;
        }
        
        public void setTime(String time) {
            this.time = time;
        }
        
        
        public String getTimeremark() {
            if(TextUtils.isEmpty(timeremark)){
                timeremark=" ";
            }
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
        
        
        public Integer getUserBuyCount() {
            return this.userBuyCount;
        }
        
        public void setUserBuyCount(Integer userBuyCount) {
            this.userBuyCount = userBuyCount;
        }
        
        
        public Integer getUserCollectCount() {
            return this.userCollectCount;
        }
        
        public void setUserCollectCount(Integer userCollectCount) {
            this.userCollectCount = userCollectCount;
        }
        
        
        public Integer getUserCourseId() {
            return this.userCourseId;
        }
        
        public void setUserCourseId(Integer userCourseId) {
            this.userCourseId = userCourseId;
        }
        
        
        public Integer getUserCourseStatus() {
            return this.userCourseStatus;
        }
        
        public void setUserCourseStatus(Integer userCourseStatus) {
            this.userCourseStatus = userCourseStatus;
        }
        
        
        public Integer getUserOrderItemStatus() {
            return this.userOrderItemStatus;
        }
        
        public void setUserOrderItemStatus(Integer userOrderItemStatus) {
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
