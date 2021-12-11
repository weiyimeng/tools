/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.live.init;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.scc.sdk.android.domain.*;
import java.util.List;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/19 11:33
 * @since 2.1.0
 */
public class LiveSccLiveInitResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }




        public static class ResultData{


        /**
        * auditionSecond
        */
        private  int auditionSecond;
        /**
        * authenticationKey
        */
        private  String authenticationKey;
        /**
        * ckStartTime
        */
        private  long ckStartTime;
        /**
        * courseClassCode
        */
        private  String courseClassCode;
        /**
        * courseClassName
        */
        private  String courseClassName;
        /**
        * courseId
        */
        private  int courseId;
        /**
        * headerUrl
        */
        private  String headerUrl;
        /**
        * knowlageId
        */
        private  int knowlageId;
        /**
        * live
        */
        private  String live;
        /**
        * mobile
        */
        private  String mobile;
        /**
        * name
        */
        private  String name;
        /**
        * playback
        */
        private  boolean playback;
        /**
        * serverType
        */
        private  String serverType;
        /**
        * soonerClassRoomId
        */
        private  Object soonerClassRoomId;
        /**
        * startTime
        */
        private  long startTime;
        /**
        * url
        */
        private  String url;
        /**
        * userCourseId
        */
        private  String userCourseId;
        /**
        * userCourseItemId
        */
        private  String userCourseItemId;
        /**
        * userId
        */
        private  int userId;

        //属性get||set方法


        public int getAuditionSecond() {
        return this.auditionSecond;
        }

        public void setAuditionSecond(int auditionSecond) {
        this.auditionSecond = auditionSecond;
        }



        public String getAuthenticationKey() {
        return this.authenticationKey;
        }

        public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
        }



        public long getCkStartTime() {
        return this.ckStartTime;
        }

        public void setCkStartTime(long ckStartTime) {
        this.ckStartTime = ckStartTime;
        }



        public String getCourseClassCode() {
        return this.courseClassCode;
        }

        public void setCourseClassCode(String courseClassCode) {
        this.courseClassCode = courseClassCode;
        }



        public String getCourseClassName() {
        return this.courseClassName;
        }

        public void setCourseClassName(String courseClassName) {
        this.courseClassName = courseClassName;
        }



        public int getCourseId() {
        return this.courseId;
        }

        public void setCourseId(int courseId) {
        this.courseId = courseId;
        }



        public String getHeaderUrl() {
        return this.headerUrl;
        }

        public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
        }



        public int getKnowlageId() {
        return this.knowlageId;
        }

        public void setKnowlageId(int knowlageId) {
        this.knowlageId = knowlageId;
        }



        public String getLive() {
        return this.live;
        }

        public void setLive(String live) {
        this.live = live;
        }



        public String getMobile() {
        return this.mobile;
        }

        public void setMobile(String mobile) {
        this.mobile = mobile;
        }



        public String getName() {
        return this.name;
        }

        public void setName(String name) {
        this.name = name;
        }



        public boolean getPlayback() {
        return this.playback;
        }

        public void setPlayback(boolean playback) {
        this.playback = playback;
        }



        public String getServerType() {
        return this.serverType;
        }

        public void setServerType(String serverType) {
        this.serverType = serverType;
        }



        public Object getSoonerClassRoomId() {
        return this.soonerClassRoomId;
        }

        public void setSoonerClassRoomId(Object soonerClassRoomId) {
        this.soonerClassRoomId = soonerClassRoomId;
        }



        public long getStartTime() {
        return this.startTime;
        }

        public void setStartTime(long startTime) {
        this.startTime = startTime;
        }



        public String getUrl() {
        return this.url;
        }

        public void setUrl(String url) {
        this.url = url;
        }



        public String getUserCourseId() {
        return this.userCourseId;
        }

        public void setUserCourseId(String userCourseId) {
        this.userCourseId = userCourseId;
        }



        public String getUserCourseItemId() {
        return this.userCourseItemId;
        }

        public void setUserCourseItemId(String userCourseItemId) {
        this.userCourseItemId = userCourseItemId;
        }



        public int getUserId() {
        return this.userId;
        }

        public void setUserId(int userId) {
        this.userId = userId;
        }

    }
}
