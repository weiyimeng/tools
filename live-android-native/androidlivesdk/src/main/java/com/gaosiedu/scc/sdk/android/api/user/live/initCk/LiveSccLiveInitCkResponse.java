/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.live.initCk;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.scc.sdk.android.domain.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/11/07 18:25
 * @since 2.1.0
 */
public class LiveSccLiveInitCkResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }




        public static class ResultData{


        /**
        * imServerBean
        */
        private  ImServerBean imServerBean;
        /**
        * knowlageBaseBean
        */
        private LiveKnowledgeBaseBean knowlageBaseBean;
        /**
        * lmcLiveRoomBean
        */
        private  LmcLiveRoomBean lmcLiveRoomBean;
        /**
        * sccUserGoldBean
        */
        private  SccUserGoldBean sccUserGoldBean;
        /**
        * sccUserLevelBean
        */
        private  UserInfoBean sccUserLevelBean;
        /**
        * userBaseBean
        */
        private  LiveUserBaseBean userBaseBean;

        //属性get||set方法


        public ImServerBean getImServerBean() {
        return this.imServerBean;
        }

        public void setImServerBean(ImServerBean imServerBean) {
        this.imServerBean = imServerBean;
        }



        public LiveKnowledgeBaseBean getKnowlageBaseBean() {
        return this.knowlageBaseBean;
        }

        public void setKnowlageBaseBean(LiveKnowledgeBaseBean knowlageBaseBean) {
        this.knowlageBaseBean = knowlageBaseBean;
        }



        public LmcLiveRoomBean getLmcLiveRoomBean() {
        return this.lmcLiveRoomBean;
        }

        public void setLmcLiveRoomBean(LmcLiveRoomBean lmcLiveRoomBean) {
        this.lmcLiveRoomBean = lmcLiveRoomBean;
        }



        public SccUserGoldBean getSccUserGoldBean() {
        return this.sccUserGoldBean;
        }

        public void setSccUserGoldBean(SccUserGoldBean sccUserGoldBean) {
        this.sccUserGoldBean = sccUserGoldBean;
        }



        public UserInfoBean getSccUserLevelBean() {
        return this.sccUserLevelBean;
        }

        public void setSccUserLevelBean(UserInfoBean sccUserLevelBean) {
        this.sccUserLevelBean = sccUserLevelBean;
        }



        public LiveUserBaseBean getUserBaseBean() {
        return this.userBaseBean;
        }

        public void setUserBaseBean(LiveUserBaseBean userBaseBean) {
        this.userBaseBean = userBaseBean;
        }

    }
}
