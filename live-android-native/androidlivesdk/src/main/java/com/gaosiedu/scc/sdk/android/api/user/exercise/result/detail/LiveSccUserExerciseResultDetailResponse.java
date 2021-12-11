/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.exercise.result.detail;


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
public class LiveSccUserExerciseResultDetailResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }




        public static class ResultData{


        /**
         * allowReSubmit
         */
        private  Integer allowReSubmit;
        /**
         * comment
         */
        private  String comment;
        /**
         * correctUserName
         */
        private  String correctUserName;
        /**
         * evaluationGold
         */
        private  Integer evaluationGold;
        /**
         * evaluationProgress
         */
        private  Integer evaluationProgress;
        /**
         * exercise
         */
        private  ExerciseBean exercise;
        /**
         * exerciseLevel
         */
        private  Integer exerciseLevel;
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
         * submitGold
         */
        private  Integer submitGold;
        /**
         * submitProgress
         */
        private  Integer submitProgress;
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

        public Integer getEvaluationGold() {
            return evaluationGold;
        }

        public void setEvaluationGold(Integer evaluationGold) {
            this.evaluationGold = evaluationGold;
        }

        public Integer getEvaluationProgress() {
            return evaluationProgress;
        }

        public void setEvaluationProgress(Integer evaluationProgress) {
            this.evaluationProgress = evaluationProgress;
        }

        public Integer getSubmitGold() {
            return submitGold;
        }

        public void setSubmitGold(Integer submitGold) {
            this.submitGold = submitGold;
        }

        public Integer getSubmitProgress() {
            return submitProgress;
        }

        public void setSubmitProgress(Integer submitProgress) {
            this.submitProgress = submitProgress;
        }

        public Integer getExerciseLevel() {
            return exerciseLevel;
        }

        public void setExerciseLevel(Integer exerciseLevel) {
            this.exerciseLevel = exerciseLevel;
        }

        public Integer getAllowReSubmit() {
            return allowReSubmit;
        }

        public void setAllowReSubmit(Integer allowReSubmit) {
            this.allowReSubmit = allowReSubmit;
        }
        }


}