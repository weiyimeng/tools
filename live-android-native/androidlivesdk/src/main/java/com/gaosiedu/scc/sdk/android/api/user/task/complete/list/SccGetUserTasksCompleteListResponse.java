/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.task.complete.list;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.scc.sdk.android.domain.*;
import java.util.List;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/11 15:44
 * @since 2.1.0
 */
public class SccGetUserTasksCompleteListResponse extends ResponseResult  {

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
        * behave
        */
        private  String behave;
        /**
        * createTime
        */
        private  String createTime;
        /**
        * getGold
        */
        private  Integer getGold;
        /**
        * getProgress
        */
        private  Integer getProgress;
        /**
        * id
        */
        private  Integer id;
        /**
        * rewardInfo
        */
        private  String rewardInfo;
        /**
        * rootType
        */
        private  String rootType;
        /**
        * serialVersionUID
        */
        private  Long serialVersionUID;
        /**
        * status
        */
        private  Integer status;
        /**
        * taskCondition
        */
        private  String taskCondition;
        /**
        * taskName
        */
        private  String taskName;
        /**
        * type
        */
        private  String type;
        /**
        * userId
        */
        private  String userId;
        /**
        * uuid
        */
        private  String uuid;

        //属性get||set方法


        public String getBehave() {
        return this.behave;
        }

        public void setBehave(String behave) {
        this.behave = behave;
        }



        public String getCreateTime() {
        return this.createTime;
        }

        public void setCreateTime(String createTime) {
        this.createTime = createTime;
        }



        public Integer getGetGold() {
        return this.getGold;
        }

        public void setGetGold(Integer getGold) {
        this.getGold = getGold;
        }



        public Integer getGetProgress() {
        return this.getProgress;
        }

        public void setGetProgress(Integer getProgress) {
        this.getProgress = getProgress;
        }



        public Integer getId() {
        return this.id;
        }

        public void setId(Integer id) {
        this.id = id;
        }



        public String getRewardInfo() {
        return this.rewardInfo;
        }

        public void setRewardInfo(String rewardInfo) {
        this.rewardInfo = rewardInfo;
        }



        public String getRootType() {
        return this.rootType;
        }

        public void setRootType(String rootType) {
        this.rootType = rootType;
        }



        public Long getSerialVersionUID() {
        return this.serialVersionUID;
        }

        public void setSerialVersionUID(Long serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
        }



        public Integer getStatus() {
        return this.status;
        }

        public void setStatus(Integer status) {
        this.status = status;
        }



        public String getTaskCondition() {
        return this.taskCondition;
        }

        public void setTaskCondition(String taskCondition) {
        this.taskCondition = taskCondition;
        }



        public String getTaskName() {
        return this.taskName;
        }

        public void setTaskName(String taskName) {
        this.taskName = taskName;
        }



        public String getType() {
        return this.type;
        }

        public void setType(String type) {
        this.type = type;
        }



        public String getUserId() {
        return this.userId;
        }

        public void setUserId(String userId) {
        this.userId = userId;
        }



        public String getUuid() {
        return this.uuid;
        }

        public void setUuid(String uuid) {
        this.uuid = uuid;
        }

    }
}