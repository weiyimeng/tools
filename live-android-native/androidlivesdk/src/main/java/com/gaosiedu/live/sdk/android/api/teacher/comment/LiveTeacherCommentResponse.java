/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.teacher.comment;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.live.sdk.android.domain.*;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/29 19:15
 * @since 2.1.0
 */
public class LiveTeacherCommentResponse extends ResponseResult  {

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
        * associateId
        */
        private  int associateId;
        /**
        * content
        */
        private  String content;
        /**
        * createTime
        */
        private  String createTime;
        /**
        * createTimeStr
        */
        private  String createTimeStr;
        /**
        * id
        */
        private  int id;
        /**
        * ip
        */
        private  String ip;
        /**
        * level
        */
        private  int level;
        /**
        * profileDomain
        */
        private  ProfileDomain profileDomain;
        /**
        * status
        */
        private  int status;
        /**
        * type
        */
        private  int type;
        /**
        * typeName
        */
        private  String typeName;
        /**
        * updateTime
        */
        private  String updateTime;
        /**
        * userId
        */
        private  int userId;

        //属性get||set方法


        public int getAssociateId() {
        return this.associateId;
        }

        public void setAssociateId(int associateId) {
        this.associateId = associateId;
        }



        public String getContent() {
        return this.content;
        }

        public void setContent(String content) {
        this.content = content;
        }



        public String getCreateTime() {
        return this.createTime;
        }

        public void setCreateTime(String createTime) {
        this.createTime = createTime;
        }



        public String getCreateTimeStr() {
        return this.createTimeStr;
        }

        public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
        }



        public int getId() {
        return this.id;
        }

        public void setId(int id) {
        this.id = id;
        }



        public String getIp() {
        return this.ip;
        }

        public void setIp(String ip) {
        this.ip = ip;
        }



        public int getLevel() {
        return this.level;
        }

        public void setLevel(int level) {
        this.level = level;
        }



        public ProfileDomain getProfileDomain() {
        return this.profileDomain;
        }

        public void setProfileDomain(ProfileDomain profileDomain) {
        this.profileDomain = profileDomain;
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
