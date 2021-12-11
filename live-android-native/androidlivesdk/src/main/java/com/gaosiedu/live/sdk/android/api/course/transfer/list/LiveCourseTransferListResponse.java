/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.transfer.list;


import com.gaosiedu.live.sdk.android.base.LiveSdkHelper;
import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 课程-调课列表 返回值
 *
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/11/09 15:38
 * @since 2.1.0
 */
public class LiveCourseTransferListResponse extends ResponseResult {
    
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
    
    public static class ListData {
        /**
         * 活动列表
         */
        private List<CourseActivityDomain> activityList;
        /**
         * 是否可以添加到购物车
         */
        private boolean addToShoppingCartable;
        /**
         * 课程助教
         */
        private List<TeacherDomain> assistants;
        /**
         * 是否为组合套餐
         */
        private int combo;
        /**
         * 已开课次数 调课需要使用
         */
        private int completedCount;
        /**
         * 对于已经生成好的订单的优惠券
         */
        private List<CourseCouponDomain> couponList;
        /**
         * 该课程可以分班的班级
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
         * 课程时长
         */
        private double courseDuration;
        /**
         * 课程随材
         */
        private List<CourseEnclosureDomain> courseEnclosureDomains;
        /**
         * 报名状态1开始报名0暂停报名
         */
        private int courseEnroll;
        /**
         * 课程班级名称
         */
        private String courseGradeNames;
        /**
         * courseKnowledgeDomain
         */
        private CourseKnowledgeDomain courseKnowledgeDomain;
        /**
         * 课程报名结束
         */
        private boolean coursePurchaseEnd;
        /**
         * 课程报名开始
         */
        private boolean coursePurchaseStart;
        /**
         * courseRemark
         */
        private String courseRemark;
        /**
         * 课程服务名称
         */
        private String courseServiceNames;
        /**
         * 授课状态1未开始2授课中3已结课
         */
        private int courseStatus;
        /**
         * courseSubjectNames
         */
        private String courseSubjectNames;
        /**
         * courseTypeNames
         */
        private String courseTypeNames;
        /**
         * 课程年份
         */
        private int courseYear;
        /**
         * 报名截止日期,下架时间
         */
        private String deadline;
        /**
         * 报名截止日字符串
         */
        private String deadlineStr;
        /**
         * 报名开始时间
         */
        private String enabletime;
        /**
         * 首页展示已报名人数
         */
        private int enrollCount;
        /**
         * 标识该课是否免费转班 0免费，1不免费
         */
        private int exchangeFree;
        /**
         * 课程截止时间str
         */
        private String expireTimeStr;
        /**
         * 课程图片地址
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
         * 是否开始报名
         */
        private boolean isBegin;
        /**
         * 是否购买
         */
        private boolean isBuy;
        /**
         * 0 免费 1  收费
         */
        private boolean isFree;
        /**
         * 是否收藏1收藏，0
         */
        private boolean isLike;
        /**
         * 是否可退费：0表示可以，1表示不可以
         */
        private int isRefund;
        /**
         * isReplay
         */
        private int isReplay;
        /**
         * 课程知识点列表
         */
        private List<CourseKnowledgeDomain> knowledges;
        /**
         * lastLiveTimeString
         */
        private String lastLiveTimeString;
        /**
         * 课程物料费用
         */
        private BigDecimal materialPrice;
        /**
         * 物料成本
         */
        private BigDecimal materielCost;
        /**
         * 课程名称
         */
        private String name;
        /**
         * 原售价
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
         * 有升班，0或null没有升班
         */
        private int passCourse;
        /**
         * 应付价格
         */
        private BigDecimal payable;
        /**
         * plan
         */
        private String plan;
        /**
         * 课程价格
         */
        private BigDecimal price;
        /**
         * 可购买
         */
        private boolean purchasable;
        /**
         * 限制人数
         */
        private int restrictCount;
        /**
         * 退货标识
         */
        private boolean returnFlag;
        /**
         * 课程试看地址
         */
        private String shikanUrl;
        /**
         * -1已下线，0 未上线。1 已上线
         */
        private int status;
        /**
         * 课程物料
         */
        private List<StoreHouseDomain> storeHouseList;
        /**
         * subject
         */
        private String subject;
        /**
         * 主讲老师名称
         */
        private String teacherNames;
        /**
         * 课程教师
         */
        private List<TeacherDomain> teachers;
        /**
         * templateDomains
         */
        private List<CourseTemplateDomain> templateDomains;
        /**
         * 学期
         */
        private Integer term;
        /**
         * 上课时间备注信息（课程安排）
         */
        private String timeremark;
        /**
         * 1:长线课，2：短线课 ，3：公开课
         */
        private String type;
        /**
         * 该课程已报名学生数量
         */
        private int userBuyCount;
        /**
         * 该课程已收藏学生数量
         */
        private int userCollectCount;
        /**
         * userCourseId
         */
        private int userCourseId;
        /**
         * 用户课程状态
         */
        private int userCourseStatus;
        /**
         * 标识该课程对于用户的状态 0：下单 2：退款中
         */
        private int userOrderItemStatus;
        /**
         * 课程过期时间
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
        
        
        public List<TeacherDomain> getAssistants() {
            return this.assistants;
        }
        
        public void setAssistants(List<TeacherDomain> assistants) {
            this.assistants = assistants;
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
        
        
        public double getCourseDuration() {
            return this.courseDuration;
        }
        
        public void setCourseDuration(double courseDuration) {
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
        
        
        public String getCourseSubjectNames() {
            return this.courseSubjectNames;
        }
        
        public void setCourseSubjectNames(String courseSubjectNames) {
            this.courseSubjectNames = courseSubjectNames;
        }
        
        
        public String getCourseTypeNames() {
            return this.courseTypeNames;
        }
        
        public void setCourseTypeNames(String courseTypeNames) {
            this.courseTypeNames = courseTypeNames;
        }
        
        
        public int getCourseYear() {
            return this.courseYear;
        }
        
        public void setCourseYear(int courseYear) {
            this.courseYear = courseYear;
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
        
        
        public String getLastLiveTimeString() {
            return this.lastLiveTimeString;
        }
        
        public void setLastLiveTimeString(String lastLiveTimeString) {
            this.lastLiveTimeString = lastLiveTimeString;
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
        
        
        public boolean getOverDeadTimeStatus() {
            return this.overDeadTimeStatus;
        }
        
        public void setOverDeadTimeStatus(boolean overDeadTimeStatus) {
            this.overDeadTimeStatus = overDeadTimeStatus;
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
        
        
        public int getTerm() {
            if (this.term == null) {
                return LiveSdkHelper.DEFAULT;
            }
            return this.term;
        }
        
        public void setTerm(Integer term) {
            this.term = term;
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
