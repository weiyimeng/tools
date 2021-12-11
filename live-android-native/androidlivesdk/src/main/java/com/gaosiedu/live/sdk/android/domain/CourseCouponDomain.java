/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.live.sdk.android.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程数据
 *
 * @author lixun
 * @describe
 * @date 2017/10/5 17:40
 * @since 2.1.0
 */
public class CourseCouponDomain implements Serializable {
    private int type;
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    private int couponType;
    
    public int getCouponType() {
        return couponType;
    }
    
    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }
    
    /**
     * 优惠券id
     */
    private int id;
    
    /**
     * 优惠券名称
     */
    private String name;
    /**
     * 优惠券说明
     */
    private String plan;
    
    /**
     * 优先级
     */
    private int priority;
    
    //    /**
    //     * 优惠券参与次数
    //     */
    //    private int numberOfTimes;
    
    //    /**
    //     * 课程id
    //     */
    //    private int courseId;
    
    //    /**
    //     * 参与条件(json)
    //     */
    //    private String condition;
    
    /**
     * 优惠券规则
     */
    private String rule;
    
    /**
     * 作用于订单
     */
    private boolean actOnOrder;
    
    //    /**
    //     * 是否忽略开始报名时间(如果检测课程存在忽略开始报名时间的优惠券则,可以根据优惠券时间进行结算)
    //     */
    //    private boolean ignoreEnableTime;
    //
    //    /**
    //     * 是否忽略报名截止时间(如果检测课程存在忽略报名截止时间的优惠券则,可以根据优惠券时间进行结算)
    //     */
    //    private boolean ignoreDeadline;
    
    /**
     * 在不可用时任然展示
     */
    private boolean usableShow;
    
    /**
     * 开启时间
     */
    private Date startTime;
    
    /**
     * 截止时间
     */
    private Date endTime;
    
    /**
     * 优惠券正在执行中
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
     * 参与优惠券使用的课程范围
     */
    private String useRange;
    /**
     * 是否可选
     */
    private boolean choosable;
    /**
     * 关联课程ID
     */
    private int courseId;
    
    private boolean isCheck;
    
    
    public boolean isCheck() {
        return isCheck;
    }
    
    public void setCheck(boolean check) {
        isCheck = check;
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
    
    public String getRule() {
        return rule;
    }
    
    public void setRule(String rule) {
        this.rule = rule;
    }
    
    public boolean isActOnOrder() {
        return actOnOrder;
    }
    
    public void setActOnOrder(boolean actOnOrder) {
        this.actOnOrder = actOnOrder;
    }
    
    public boolean isUsableShow() {
        return usableShow;
    }
    
    public void setUsableShow(boolean usableShow) {
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
    
    public boolean isChoosable() {
        return choosable;
    }
    
    public void setChoosable(boolean choosable) {
        this.choosable = choosable;
    }
    
    public String getUseRange() {
        return useRange;
    }
    
    public void setUseRange(String useRange) {
        this.useRange = useRange;
    }
    
    public int getCourseId() {
        return courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
