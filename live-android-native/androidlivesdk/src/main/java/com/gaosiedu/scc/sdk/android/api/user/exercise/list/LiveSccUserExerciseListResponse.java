/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.exercise.list;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.scc.sdk.android.domain.*;
import java.util.List;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/30 17:53
 * @since 2.1.0
 */
public class LiveSccUserExerciseListResponse extends ResponseResult  {

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
        * comment
        */
        private  String comment;
        /**
        * correctUserName
        */
        private  String correctUserName;
        /**
        * exercise
        */
        private  ExerciseBean exercise;
        /**
        * exerciseResourceDomain
        */
        private  List<ExerciseResourceBean> exerciseResourceDomain;
        /**
        * exerciseResultId
        */
        private  Integer exerciseResultId;
        /**
        * exerciseResultStatus
        */
        private  Integer exerciseResultStatus;
        /**
        * knowledgeOrder
        */
        private  String knowledgeOrder;
        /**
        * remark
        */
        private  String remark;
        /**
        * score
        */
        private  Integer score;
        /**
        * submitTime
        */
        private  String submitTime;

        //属性get||set方法


        public String getComment() {
        return this.comment;
        }

        public void setComment(String comment) {
        this.comment = comment;
        }



        public String getCorrectUserName() {
        return this.correctUserName;
        }

        public void setCorrectUserName(String correctUserName) {
        this.correctUserName = correctUserName;
        }



        public ExerciseBean getExercise() {
        return this.exercise;
        }

        public void setExercise(ExerciseBean exercise) {
        this.exercise = exercise;
        }




        public List<ExerciseResourceBean> getExerciseResourceDomain() {
            return this.exerciseResourceDomain;
        }

        public void setExerciseResourceDomain(List<ExerciseResourceBean> exerciseResourceDomain) {
            this.exerciseResourceDomain = exerciseResourceDomain;
        }



        public Integer getExerciseResultId() {
        return this.exerciseResultId;
        }

        public void setExerciseResultId(Integer exerciseResultId) {
        this.exerciseResultId = exerciseResultId;
        }



        public Integer getExerciseResultStatus() {
        return this.exerciseResultStatus;
        }

        public void setExerciseResultStatus(Integer exerciseResultStatus) {
        this.exerciseResultStatus = exerciseResultStatus;
        }



        public String getKnowledgeOrder() {
        return this.knowledgeOrder;
        }

        public void setKnowledgeOrder(String knowledgeOrder) {
        this.knowledgeOrder = knowledgeOrder;
        }



        public String getRemark() {
        return this.remark;
        }

        public void setRemark(String remark) {
        this.remark = remark;
        }



        public Integer getScore() {
        return this.score;
        }

        public void setScore(Integer score) {
        this.score = score;
        }



        public String getSubmitTime() {
        return this.submitTime;
        }

        public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
        }

    }
}