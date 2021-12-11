/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.transfer.list;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.live.sdk.android.domain.*;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/04 16:13
 * @since 2.1.0
 */
public class LiveUserTransferListResponse extends ResponseResult  {

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
        * changeReason
        */
        private  String changeReason;
        /**
        * createTime
        */
        private  String createTime;
        /**
        * id
        */
        private  int id;
        /**
        * newCourseId
        */
        private  int newCourseId;
        /**
        * newKnowledgeId
        */
        private  int newKnowledgeId;
        /**
        * oldCourseId
        */
        private  int oldCourseId;
        /**
        * oldKnowledgeId
        */
        private  int oldKnowledgeId;
        /**
        * status
        */
        private  int status;
        /**
        * userId
        */
        private  int userId;

        //属性get||set方法


        public String getChangeReason() {
        return this.changeReason;
        }

        public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
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



        public int getNewCourseId() {
        return this.newCourseId;
        }

        public void setNewCourseId(int newCourseId) {
        this.newCourseId = newCourseId;
        }



        public int getNewKnowledgeId() {
        return this.newKnowledgeId;
        }

        public void setNewKnowledgeId(int newKnowledgeId) {
        this.newKnowledgeId = newKnowledgeId;
        }



        public int getOldCourseId() {
        return this.oldCourseId;
        }

        public void setOldCourseId(int oldCourseId) {
        this.oldCourseId = oldCourseId;
        }



        public int getOldKnowledgeId() {
        return this.oldKnowledgeId;
        }

        public void setOldKnowledgeId(int oldKnowledgeId) {
        this.oldKnowledgeId = oldKnowledgeId;
        }



        public int getStatus() {
        return this.status;
        }

        public void setStatus(int status) {
        this.status = status;
        }



        public int getUserId() {
        return this.userId;
        }

        public void setUserId(int userId) {
        this.userId = userId;
        }

    }
}
