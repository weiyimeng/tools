/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.live.initAnchorCk;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.scc.sdk.android.domain.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/11/21 15:51
 * @since 2.1.0
 */
public class LiveSccLiveInitAnchorCkResponse extends ResponseResult  {

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
        * liveType
        */
        private  String liveType;
        /**
        * lmcLiveRoomBean
        */
        private  LmcLiveRoomBean lmcLiveRoomBean;
        /**
        * sccLiveRoomExtDataListBean
        */
        private  SccLiveRoomExtDataListBean sccLiveRoomExtDataListBean;
        /**
        * teacherBean
        */
        private  TeacherBean teacherBean;

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



        public String getLiveType() {
        return this.liveType;
        }

        public void setLiveType(String liveType) {
        this.liveType = liveType;
        }



        public LmcLiveRoomBean getLmcLiveRoomBean() {
        return this.lmcLiveRoomBean;
        }

        public void setLmcLiveRoomBean(LmcLiveRoomBean lmcLiveRoomBean) {
        this.lmcLiveRoomBean = lmcLiveRoomBean;
        }



        public SccLiveRoomExtDataListBean getSccLiveRoomExtDataListBean() {
        return this.sccLiveRoomExtDataListBean;
        }

        public void setSccLiveRoomExtDataListBean(SccLiveRoomExtDataListBean sccLiveRoomExtDataListBean) {
        this.sccLiveRoomExtDataListBean = sccLiveRoomExtDataListBean;
        }



        public TeacherBean getTeacherBean() {
        return this.teacherBean;
        }

        public void setTeacherBean(TeacherBean teacherBean) {
        this.teacherBean = teacherBean;
        }

    }
}
