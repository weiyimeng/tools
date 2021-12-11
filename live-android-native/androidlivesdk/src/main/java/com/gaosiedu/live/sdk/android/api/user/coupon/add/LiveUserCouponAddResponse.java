/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.coupon.add;


import com.gaosiedu.live.sdk.android.api.user.coupon.list.LiveUserCouponListResponse;
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
public class LiveUserCouponAddResponse extends ResponseResult  {

    private LiveUserCouponListResponse.ListData data;

    public LiveUserCouponListResponse.ListData getData() {
        return data;
    }

    public void setData(LiveUserCouponListResponse.ListData data) {
        this.data = data;
    }


//    public static class ResultData{
//
//        /**
//        * 作用于订单 true表示作用于订单，false表示作用于课程
//        */
//        private  Boolean actOnOrder;
//        /**
//        * choosable
//        */
//        private  Boolean choosable;
//        /**
//        * courseId
//        */
//        private  Integer courseId;
//        /**
//        * 截止时间
//        */
//        private  String endTime;
//        /**
//        * executing
//        */
//        private  Boolean executing;
//        /**
//        * flag
//        */
//        private  String flag;
//        /**
//        * 优惠券id
//        */
//        private  Integer id;
//        /**
//        * 优惠券名称
//        */
//        private  String name;
//        /**
//        * plan
//        */
//        private  String plan;
//        /**
//        * priority
//        */
//        private  Integer priority;
//        /**
//        * 作用范围
//        */
//        private  String rangeName;
//        /**
//        * 优惠的价格
//        */
//        private  String resume;
//        /**
//        * rule
//        */
//        private  String rule;
//        /**
//        * startTime
//        */
//        private  String startTime;
//        /**
//        * 状态(-1表示删除,1表示有效,0:表示冻结/暂停,2:结束)
//        */
//        private  Integer status;
//        /**
//        * usable
//        */
//        private  Boolean usable;
//        /**
//        * usableShow
//        */
//        private  Boolean usableShow;
//        /**
//        * useRange
//        */
//        private  String useRange;
//
//        //属性get||set方法
//
//
//        public Boolean getActOnOrder() {
//        return this.actOnOrder;
//        }
//
//        public void setActOnOrder(Boolean actOnOrder) {
//        this.actOnOrder = actOnOrder;
//        }
//
//
//
//        public Boolean getChoosable() {
//        return this.choosable;
//        }
//
//        public void setChoosable(Boolean choosable) {
//        this.choosable = choosable;
//        }
//
//
//
//        public Integer getCourseId() {
//        return this.courseId;
//        }
//
//        public void setCourseId(Integer courseId) {
//        this.courseId = courseId;
//        }
//
//
//
//        public String getEndTime() {
//        return this.endTime;
//        }
//
//        public void setEndTime(String endTime) {
//        this.endTime = endTime;
//        }
//
//
//
//        public Boolean getExecuting() {
//        return this.executing;
//        }
//
//        public void setExecuting(Boolean executing) {
//        this.executing = executing;
//        }
//
//
//
//        public String getFlag() {
//        return this.flag;
//        }
//
//        public void setFlag(String flag) {
//        this.flag = flag;
//        }
//
//
//
//        public Integer getId() {
//        return this.id;
//        }
//
//        public void setId(Integer id) {
//        this.id = id;
//        }
//
//
//
//        public String getName() {
//        return this.name;
//        }
//
//        public void setName(String name) {
//        this.name = name;
//        }
//
//
//
//        public String getPlan() {
//        return this.plan;
//        }
//
//        public void setPlan(String plan) {
//        this.plan = plan;
//        }
//
//
//
//        public Integer getPriority() {
//        return this.priority;
//        }
//
//        public void setPriority(Integer priority) {
//        this.priority = priority;
//        }
//
//
//
//        public String getRangeName() {
//        return this.rangeName;
//        }
//
//        public void setRangeName(String rangeName) {
//        this.rangeName = rangeName;
//        }
//
//
//
//        public String getResume() {
//        return this.resume;
//        }
//
//        public void setResume(String resume) {
//        this.resume = resume;
//        }
//
//
//
//        public String getRule() {
//        return this.rule;
//        }
//
//        public void setRule(String rule) {
//        this.rule = rule;
//        }
//
//
//
//        public String getStartTime() {
//        return this.startTime;
//        }
//
//        public void setStartTime(String startTime) {
//        this.startTime = startTime;
//        }
//
//
//
//        public Integer getStatus() {
//        return this.status;
//        }
//
//        public void setStatus(Integer status) {
//        this.status = status;
//        }
//
//
//
//        public Boolean getUsable() {
//        return this.usable;
//        }
//
//        public void setUsable(Boolean usable) {
//        this.usable = usable;
//        }
//
//
//
//        public Boolean getUsableShow() {
//        return this.usableShow;
//        }
//
//        public void setUsableShow(Boolean usableShow) {
//        this.usableShow = usableShow;
//        }
//
//
//
//        public String getUseRange() {
//        return this.useRange;
//        }
//
//        public void setUseRange(String useRange) {
//        this.useRange = useRange;
//        }
//
//    }
}
