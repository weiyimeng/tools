package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjia on 2018/8/17.
 */
public class CourseBean implements Serializable {
    /**
     * 用户课程状态
     */
    private int userCourseStatus;

    /**
     * 课程截止时间str
     */
    private String expireTimeStr;

    /**
     * 最近课次(课程详情用到)
     */
    private CourseKnowledgeBean courseKnowledgeBean;
    /**
     * 课程助教
     */
    private List<TeacherBean> assistants;
    /**
     * 课程教师
     */
    private List<TeacherBean> teachers;
    /**
     * 已开课次数
     */
    private int completedCount;
    /**
     * 用户课程id
     */
    private int userCourseId;
    /**
     * 用户本课订单id
     */
    private int orderId;

    /**
     * 课程
     */
    private int id;

    /**
     * 课程名称
     */
    private String name;
    /**
     * 课程介绍
     */
    private String info;

    /**
     * 课程招生对象
     */
    private String courseDesc;

    /**
     * 课程体系名称
     */
    private String courseTypeNames;

    /**
     * 课程类型id
     */
    private String courseTypeIds;

    /**
     * 课程班级id
     */
    private String courseGradeIds;

    /**
     * 课程科目id
     */
    private String courseSubjectIds;

    /**
     * 试卷Id
     */
    private int testPaperId;

    /**
     * 试卷分数线
     */
    private int scores;

    /**
     * -1已下线，0 未上线。1 已上线
     */
    private int status;

    /**
     * 1:长线课，2：短线课 ，3：公开课
     */
    private String type;

    /**
     * 科目(yw:语文,sx:数学,hx:化学 ......)
     */
    private String subject;

    /**
     * 学期
     */
    private int term;

    /**
     * 课程价格
     */
    private BigDecimal price;

    /**
     * 应付价格
     */
    private BigDecimal payable;

    /**
     * 0 免费 1  收费
     */
    private boolean isFree = false;

    /**
     * 限制人数
     */
    private int restrictCount;

    /**
     * 1  支持 2  不支持
     */
    private int isReplay;

    /**
     * 课程图片地址
     */
    private String iconUrl;

    /**
     * 课程试看地址
     */
    private String shikanUrl;

    /**
     * 创建人
     */
    private int createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 审核时间
     */
    private Date applyDateTime;

    /**
     * 第一次课的开始时间
     */
    private Date publishTime;

    /**
     * 课程失效时间
     */
    private Date expireTime;

    /**
     * 1  通过 2  未通过
     */
    private int liveStatus;

    /**
     * 1:长线课;2:短训课;3:专属课
     */
    private int courseType;

    /**
     * 是否为组合套餐(1:套餐,其他非套餐)
     */
    private int combo;

    /**
     * 上课时间备注信息（课程安排）
     */
    private String timeremark;

    /**
     * 报名截止日期,下架时间
     */
    private Date deadline;

    /**
     * 报名截止日字符串
     */
    private String deadlineStr;

    /**
     * 报名开始时间
     */
    private Date enabletime;
    /**
     * 是否开始报名
     */
    private boolean isBegin = true;

    /**
     * 列表中是否显示 0不展示，1展示
     */
    private int listshow;

    /**
     * 是否为自动分班：1自动，0不自动
     */
    private int autoClass;

    /**
     * 报名状态1开始报名0暂停报名
     */
    private int courseEnroll;

    /**
     * 暂时作为课程学期
     */
    private int courseUp;

    /**
     * 课程难度
     */
    private int courseDifficulty;

    /**
     * 课程备注
     */
    private String courseRemark;

    /**
     * 课程班级名称
     */
    private String courseGradeNames;

    /**
     * 教授学科名称
     */
    private String courseSubjectNames;

    /**
     * 课程服务ids
     */
    private String courseServiceIds;

    /**
     * 课程服务名称
     */
    private String courseServiceNames;

    /**
     * 授课状态1未开始2授课中3已结课
     */
    private int courseStatus;

    /**
     * 删除0，显示1
     */
    private int deleteFlag;

    /**
     * 原售价
     */
    private BigDecimal oldPrice;

    /**
     * 首页展示已报名人数
     */
    private int enrollCount;

    /**
     * 适合对象
     */
    private String plan;

    /**
     * 课程最新一次播放的开始时间(需要任务每天跑一次）
     */
    private Date lastLiveStartTime;

    /**
     * 课程最新一次播放的结束时间(需要任务每天跑一次）
     */
    private Date lastLiveEndTime;

    /**
     * 该课程已报名学生数量也就是学生的人气（需要任务去跑）
     */
    private int userBuyCount;

    /**
     * 该课程已收藏学生数量（需要任务去跑）
     */
    private int userCollectCount;

    /**
     * 课程老师ids
     */
    private String teacherIds;

    /**
     * 老师名称
     */
    private String teacherNames;

    /**
     * 课程时长
     */
    private Double courseDuration;
    /**
     * 课程知识点列表
     */
    private List<CourseKnowledgeBean> knowledges;
    /**
     * 是否收藏1收藏，0
     */
    private boolean isLike;
    /**
     * 是否购买
     */
    private boolean isBuy;


    /**
     * 课程过期时间
     */
    private Date validityTime;

    /**
     * 课程年份
     */
    private int courseYear;

    /**
     * 1有升班，0或null没有升班
     */
    private int passCourse;

    /**
     * 时间，代表周几 1-7代表周一到周日
     */
    private String time;

    /**
     * 是否可退费：0表示可以，1表示不可以
     */
    private int isRefund;

    /**
     * 时间段 1表示早上，2表示下午，3表示下午
     */
    private String periodTime;
    /**
     * 标识该课是否免费转班 0免费，1不免费
     */
    private int exchangeFree;

    public int getUserCourseStatus() {
        return userCourseStatus;
    }

    public void setUserCourseStatus(int userCourseStatus) {
        this.userCourseStatus = userCourseStatus;
    }

    public String getExpireTimeStr() {
        return expireTimeStr;
    }

    public void setExpireTimeStr(String expireTimeStr) {
        this.expireTimeStr = expireTimeStr;
    }

    public CourseKnowledgeBean getCourseKnowledgeBean() {
        return courseKnowledgeBean;
    }

    public void setCourseKnowledgeBean(CourseKnowledgeBean courseKnowledgeBean) {
        this.courseKnowledgeBean = courseKnowledgeBean;
    }

    public List<TeacherBean> getAssistants() {
        return assistants;
    }

    public void setAssistants(List<TeacherBean> assistants) {
        this.assistants = assistants;
    }

    public List<TeacherBean> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<TeacherBean> teachers) {
        this.teachers = teachers;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public int getUserCourseId() {
        return userCourseId;
    }

    public void setUserCourseId(int userCourseId) {
        this.userCourseId = userCourseId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public String getCourseTypeNames() {
        return courseTypeNames;
    }

    public void setCourseTypeNames(String courseTypeNames) {
        this.courseTypeNames = courseTypeNames;
    }

    public String getCourseTypeIds() {
        return courseTypeIds;
    }

    public void setCourseTypeIds(String courseTypeIds) {
        this.courseTypeIds = courseTypeIds;
    }

    public String getCourseGradeIds() {
        return courseGradeIds;
    }

    public void setCourseGradeIds(String courseGradeIds) {
        this.courseGradeIds = courseGradeIds;
    }

    public String getCourseSubjectIds() {
        return courseSubjectIds;
    }

    public void setCourseSubjectIds(String courseSubjectIds) {
        this.courseSubjectIds = courseSubjectIds;
    }

    public int getTestPaperId() {
        return testPaperId;
    }

    public void setTestPaperId(int testPaperId) {
        this.testPaperId = testPaperId;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPayable() {
        return payable;
    }

    public void setPayable(BigDecimal payable) {
        this.payable = payable;
    }

    public boolean getFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public int getRestrictCount() {
        return restrictCount;
    }

    public void setRestrictCount(int restrictCount) {
        this.restrictCount = restrictCount;
    }

    public int getIsReplay() {
        return isReplay;
    }

    public void setIsReplay(int isReplay) {
        this.isReplay = isReplay;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getShikanUrl() {
        return shikanUrl;
    }

    public void setShikanUrl(String shikanUrl) {
        this.shikanUrl = shikanUrl;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getApplyDateTime() {
        return applyDateTime;
    }

    public void setApplyDateTime(Date applyDateTime) {
        this.applyDateTime = applyDateTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public String getTimeremark() {
        return timeremark;
    }

    public void setTimeremark(String timeremark) {
        this.timeremark = timeremark;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getDeadlineStr() {
        return deadlineStr;
    }

    public void setDeadlineStr(String deadlineStr) {
        this.deadlineStr = deadlineStr;
    }

    public Date getEnabletime() {
        return enabletime;
    }

    public void setEnabletime(Date enabletime) {
        this.enabletime = enabletime;
    }

    public boolean isBegin() {
        return isBegin;
    }

    public void setBegin(boolean begin) {
        isBegin = begin;
    }

    public int getListshow() {
        return listshow;
    }

    public void setListshow(int listshow) {
        this.listshow = listshow;
    }

    public int getAutoClass() {
        return autoClass;
    }

    public void setAutoClass(int autoClass) {
        this.autoClass = autoClass;
    }

    public int getCourseEnroll() {
        return courseEnroll;
    }

    public void setCourseEnroll(int courseEnroll) {
        this.courseEnroll = courseEnroll;
    }

    public int getCourseUp() {
        return courseUp;
    }

    public void setCourseUp(int courseUp) {
        this.courseUp = courseUp;
    }

    public int getCourseDifficulty() {
        return courseDifficulty;
    }

    public void setCourseDifficulty(int courseDifficulty) {
        this.courseDifficulty = courseDifficulty;
    }

    public String getCourseRemark() {
        return courseRemark;
    }

    public void setCourseRemark(String courseRemark) {
        this.courseRemark = courseRemark;
    }

    public String getCourseGradeNames() {
        return courseGradeNames;
    }

    public void setCourseGradeNames(String courseGradeNames) {
        this.courseGradeNames = courseGradeNames;
    }

    public String getCourseSubjectNames() {
        return courseSubjectNames;
    }

    public void setCourseSubjectNames(String courseSubjectNames) {
        this.courseSubjectNames = courseSubjectNames;
    }

    public String getCourseServiceIds() {
        return courseServiceIds;
    }

    public void setCourseServiceIds(String courseServiceIds) {
        this.courseServiceIds = courseServiceIds;
    }

    public String getCourseServiceNames() {
        return courseServiceNames;
    }

    public void setCourseServiceNames(String courseServiceNames) {
        this.courseServiceNames = courseServiceNames;
    }

    public int getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(int courseStatus) {
        this.courseStatus = courseStatus;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getEnrollCount() {
        return enrollCount;
    }

    public void setEnrollCount(int enrollCount) {
        this.enrollCount = enrollCount;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Date getLastLiveStartTime() {
        return lastLiveStartTime;
    }

    public void setLastLiveStartTime(Date lastLiveStartTime) {
        this.lastLiveStartTime = lastLiveStartTime;
    }

    public Date getLastLiveEndTime() {
        return lastLiveEndTime;
    }

    public void setLastLiveEndTime(Date lastLiveEndTime) {
        this.lastLiveEndTime = lastLiveEndTime;
    }

    public int getUserBuyCount() {
        return userBuyCount;
    }

    public void setUserBuyCount(int userBuyCount) {
        this.userBuyCount = userBuyCount;
    }

    public int getUserCollectCount() {
        return userCollectCount;
    }

    public void setUserCollectCount(int userCollectCount) {
        this.userCollectCount = userCollectCount;
    }

    public String getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(String teacherIds) {
        this.teacherIds = teacherIds;
    }

    public String getTeacherNames() {
        return teacherNames;
    }

    public void setTeacherNames(String teacherNames) {
        this.teacherNames = teacherNames;
    }

    public Double getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(Double courseDuration) {
        this.courseDuration = courseDuration;
    }

    public List<CourseKnowledgeBean> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(List<CourseKnowledgeBean> knowledges) {
        this.knowledges = knowledges;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public Date getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(Date validityTime) {
        this.validityTime = validityTime;
    }

    public int getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(int courseYear) {
        this.courseYear = courseYear;
    }

    public int getPassCourse() {
        return passCourse;
    }

    public void setPassCourse(int passCourse) {
        this.passCourse = passCourse;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(int isRefund) {
        this.isRefund = isRefund;
    }

    public String getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(String periodTime) {
        this.periodTime = periodTime;
    }

    public int getExchangeFree() {
        return exchangeFree;
    }

    public void setExchangeFree(int exchangeFree) {
        this.exchangeFree = exchangeFree;
    }
}
