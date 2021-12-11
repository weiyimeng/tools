/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.balance.record;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.live.sdk.android.domain.*;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/13 11:59
 * @since 2.1.0
 */
public class LiveUserBalanceRecordResponse extends ResponseResult {
    
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
         * balance
         */
        private BigDecimal balance;
        /**
         * courseId
         */
        private String courseId;
        /**
         * courseName
         */
        private String courseName;
        /**
         * createTime
         */
        private String createTime;
        /**
         * id
         */
        private int id;
        /**
         * orderDomain
         */
        //        private  OrderDomain orderDomain;
        /**
         * orderId
         */
        private int orderId;
        /**
         * orderItemId
         */
        private String orderItemId;
        /**
         * recordName
         */
        private String recordName;
        /**
         * status
         */
        private int status;
        /**
         * type
         */
        private int type;
        /**
         * typeName
         */
        private String typeName;
        /**
         * updateTime
         */
        private String updateTime;
        /**
         * userId
         */
        private int userId;
        
        //属性get||set方法
        
        
        public BigDecimal getBalance() {
            return this.balance;
        }
        
        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }
        
        
        public String getCourseId() {
            return this.courseId;
        }
        
        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }
        
        
        public String getCourseName() {
            return this.courseName;
        }
        
        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }
        
        
        public String getCreateTime() {
            return this.createTime;
        }
        
        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        
        
        public int getId() {
            return this.id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        
        //        public OrderDomain getOrderDomain() {
        //        return this.orderDomain;
        //        }
        
        //        public void setOrderDomain(OrderDomain orderDomain) {
        //        this.orderDomain = orderDomain;
        //        }
        
        
        public int getOrderId() {
            return this.orderId;
        }
        
        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }
        
        
        public String getOrderItemId() {
            return this.orderItemId;
        }
        
        public void setOrderItemId(String orderItemId) {
            this.orderItemId = orderItemId;
        }
        
        
        public String getRecordName() {
            return this.recordName;
        }
        
        public void setRecordName(String recordName) {
            this.recordName = recordName;
        }
        
        
        public int getStatus() {
            return this.status;
        }
        
        public void setStatus(int status) {
            this.status = status;
        }
        
        
        public int getType() {
            return this.type;
        }
        
        public void setType(int type) {
            this.type = type;
        }
        
        
        public String getTypeName() {
            return this.typeName;
        }
        
        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
        
        
        public String getUpdateTime() {
            return this.updateTime;
        }
        
        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
        
        
        public int getUserId() {
            return this.userId;
        }
        
        public void setUserId(int userId) {
            this.userId = userId;
        }
        
    }
}
