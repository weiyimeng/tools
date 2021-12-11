/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 课程数据
 *
 * @author lixun
 * @describe
 * @date 2017/10/5 17:40
 * @since 2.1.0
 */
public class CourseActivityDomain implements Serializable {
    
    /**
     * 活动id
     */
    private int id;
    
    /**
     * 活动名称h
     */
    private String name;
    /**
     * 活动说明
     */
    private String plan;
    
    /**
     * 优先级
     */
    private int priority;
    
    /**
     * 活动参与次数
     */
    private int numberOfTimes;
    
    /**
     * 课程id
     */
    private int courseId;
    
    /**
     * 参与条件(json)
     */
    private String condition;
    
    /**
     * 活动规则
     */
    private String rule;
    /**
     * 作用于订单
     */
    private int actOnOrder;
    
    /**
     * 是否忽略启用时间(如果检测课程存在忽略启动时间的活动则,可以根据活动时间进行结算)
     */
    private int ignoreEnableTime;
    
    /**
     * 忽略截止时间
     */
    private int ignoreDeadline;
    
    /**
     * 在不可用时任然展示
     */
    private int usableShow;
    
    
    /**
     * 开启时间
     */
    private Date startTime;
    
    /**
     * 截止时间
     */
    private Date endTime;
    
    /**
     * 活动正在执行中
     */
    private boolean executing = false;
    
    /**
     * 状态(-1表示删除,1表示有效,0:表示冻结/暂停,2:结束)
     */
    private int status;
    
    /**
     * 标签
     */
    private String flag;
    
    /**
     * 简述
     */
    private String resume;
    
    /**
     * 用户是否可用
     */
    private boolean usable;
    
    /**
     * 生效金额
     */
    private BigDecimal actionAmount = BigDecimal.ZERO;
    
    /**
     * 优惠的作用域（"order","orderItem"）
     */
    private String discountsTarget;
    
    //是否已经计算
    private boolean hasCalculate = false;
    
    /**
     * 活动失效后的取消优惠的金额
     */
    private BigDecimal activityReturnAmount = BigDecimal.ZERO;
    /**
     * 是否显示倒计时 0 无，1有
     */
    private int countDown;

    public int getCountDown() {
        return countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }

    public BigDecimal getActivityReturnAmount() {
        return activityReturnAmount;
    }
    
    public void setActivityReturnAmount(BigDecimal activityReturnAmount) {
        this.activityReturnAmount = activityReturnAmount;
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
    
    public String getPlan() {
        return plan;
    }
    
    public void setPlan(String plan) {
        this.plan = plan;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public int getNumberOfTimes() {
        return numberOfTimes;
    }
    
    public void setNumberOfTimes(int numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }
    
    public int getCourseId() {
        return courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public String getRule() {
        return rule;
    }
    
    public void setRule(String rule) {
        this.rule = rule;
    }
    
    public int getActOnOrder() {
        return actOnOrder;
    }
    
    public void setActOnOrder(int actOnOrder) {
        this.actOnOrder = actOnOrder;
    }
    
    public int getIgnoreEnableTime() {
        return ignoreEnableTime;
    }
    
    public void setIgnoreEnableTime(int ignoreEnableTime) {
        this.ignoreEnableTime = ignoreEnableTime;
    }
    
    public int getIgnoreDeadline() {
        return ignoreDeadline;
    }
    
    public void setIgnoreDeadline(int ignoreDeadline) {
        this.ignoreDeadline = ignoreDeadline;
    }
    
    public int getUsableShow() {
        return usableShow;
    }
    
    public void setUsableShow(int usableShow) {
        this.usableShow = usableShow;
    }
    
    public Date getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    public Date getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    public boolean isExecuting() {
        return executing;
    }
    
    public void setExecuting(boolean executing) {
        this.executing = executing;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getFlag() {
        return flag;
    }
    
    public void setFlag(String flag) {
        this.flag = flag;
    }
    
    public String getResume() {
        return resume;
    }
    
    public void setResume(String resume) {
        this.resume = resume;
    }
    
    public boolean isUsable() {
        return usable;
    }
    
    public void setUsable(boolean usable) {
        this.usable = usable;
    }
    
    public BigDecimal getActionAmount() {
        return actionAmount;
    }
    
    public void setActionAmount(BigDecimal actionAmount) {
        this.actionAmount = actionAmount;
    }
    
    public String getDiscountsTarget() {
        return discountsTarget;
    }
    
    public void setDiscountsTarget(String discountsTarget) {
        this.discountsTarget = discountsTarget;
    }
    
    
    public boolean isHasCalculate() {
        return hasCalculate;
    }
    
    public void setHasCalculate(boolean hasCalculate) {
        this.hasCalculate = hasCalculate;
    }
    
}
