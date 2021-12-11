/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.coupon.list;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.live.sdk.android.domain.*;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/27 13:16
 * @since 2.1.0
 */
public class LiveUserCouponListResponse extends ResponseResult {
    
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
         * 作用于订单 true表示作用于订单，false表示作用于课程
         */
        private boolean actOnOrder;
        /**
         * choosable
         */
        private boolean choosable;
        /**
         * courseId
         */
        private int courseId;
        /**
         * 截止时间
         */
        private String endTime;
        /**
         * executing
         */
        private boolean executing;
        /**
         * flag
         */
        private String flag;
        /**
         * 优惠券id
         */
        private int id;
        /**
         * 是否即将过期，1标识是，0标识不是
         */
        private int imminentOverTime;
        /**
         * 优惠券名称
         */
        private String name;
        
        
        /**
         * 作用的学科
         */
        private String subjectId;
        
        public String getSubjectId() {
            return subjectId;
        }
        
        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }
        
        /**
         * plan
         */
        private String plan;
        /**
         * priority
         */
        private int priority;
        /**
         * 作用范围
         */
        
        /**
         * 优惠券类型1.全场通用,2.课程体系通用,3.课程适用,4.产品适用,5:学科通用,6:年级通用
         */
        private int type;
        private String rangeName;
        /**
         * 优惠的价格
         */
        private BigDecimal resume;
        /**
         * rule
         */
        private String rule;
        /**
         * startTime
         */
        private String startTime;
        /**
         * 状态(-1表示删除,1表示有效,0:表示冻结/暂停,2:结束)
         */
        private int status;
        /**
         * usable
         */
        private boolean usable;
        /**
         * usableShow
         */
        private boolean usableShow;
        /**
         * useRange
         */
        private String useRange;
        
        //属性get||set方法
        
        
        public boolean getActOnOrder() {
            return this.actOnOrder;
        }
        
        public void setActOnOrder(boolean actOnOrder) {
            this.actOnOrder = actOnOrder;
        }
        
        
        public boolean getChoosable() {
            return this.choosable;
        }
        
        public void setChoosable(boolean choosable) {
            this.choosable = choosable;
        }
        
        
        public int getCourseId() {
            return this.courseId;
        }
        
        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }
        
        
        public String getEndTime() {
            return this.endTime;
        }
        
        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
        
        
        public boolean getExecuting() {
            return this.executing;
        }
        
        public void setExecuting(boolean executing) {
            this.executing = executing;
        }
        
        
        public String getFlag() {
            return this.flag;
        }
        
        public void setFlag(String flag) {
            this.flag = flag;
        }
        
        
        public int getId() {
            return this.id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        
        public int getImminentOverTime() {
            return this.imminentOverTime;
        }
        
        public void setImminentOverTime(int imminentOverTime) {
            this.imminentOverTime = imminentOverTime;
        }
        
        
        public String getName() {
            return this.name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        
        public String getPlan() {
            return this.plan;
        }
        
        public void setPlan(String plan) {
            this.plan = plan;
        }
        
        
        public int getPriority() {
            return this.priority;
        }
        
        public void setPriority(int priority) {
            this.priority = priority;
        }
        
        
        public String getRangeName() {
            return this.rangeName;
        }
        
        public void setRangeName(String rangeName) {
            this.rangeName = rangeName;
        }
        
        public int getType() {
            return type;
        }
        
        public void setType(int type) {
            this.type = type;
        }
        
        public BigDecimal getResume() {
            return this.resume;
        }
        
        public void setResume(BigDecimal resume) {
            this.resume = resume;
        }
        
        
        public String getRule() {
            return this.rule;
        }
        
        public void setRule(String rule) {
            this.rule = rule;
        }
        
        
        public String getStartTime() {
            return this.startTime;
        }
        
        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }
        
        
        public int getStatus() {
            return this.status;
        }
        
        public void setStatus(int status) {
            this.status = status;
        }
        
        
        public boolean getUsable() {
            return this.usable;
        }
        
        public void setUsable(boolean usable) {
            this.usable = usable;
        }
        
        
        public boolean getUsableShow() {
            return this.usableShow;
        }
        
        public void setUsableShow(boolean usableShow) {
            this.usableShow = usableShow;
        }
        
        
        public String getUseRange() {
            return this.useRange;
        }
        
        public void setUseRange(String useRange) {
            this.useRange = useRange;
        }
        
    }
}
