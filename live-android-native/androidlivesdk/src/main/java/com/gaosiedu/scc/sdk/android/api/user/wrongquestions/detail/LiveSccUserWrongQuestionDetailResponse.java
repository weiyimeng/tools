/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.wrongquestions.detail;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.scc.sdk.android.domain.*;
import java.util.List;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/19 11:30
 * @since 2.1.0
 */
public class LiveSccUserWrongQuestionDetailResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }




        public static class ResultData{


        /**
        * collect
        */
        private  Short collect;
        /**
        * correct
        */
        private  Short correct;
        /**
        * correctList
        */
        private  List<SccUserWrongQuestionCorrectBean> correctList;
        /**
        * correctNum
        */
        private  Integer correctNum;
        /**
        * delStatus
        */
        private  Short delStatus;
        /**
        * gradeId
        */
        private  Integer gradeId;
        /**
        * gradeName
        */
        private  String gradeName;
        /**
        * id
        */
        private  Integer id;
        /**
        * questionAnalysis
        */
        private  String questionAnalysis;
        /**
        * questionAnswer
        */
        private  String questionAnswer;
        /**
        * questionContent
        */
        private  String questionContent;
        /**
        * questionCreatetime
        */
        private  String questionCreatetime;
        /**
        * questionId
        */
        private  Integer questionId;
        /**
        * questionSource
        */
        private  String questionSource;
        /**
        * questionStem
        */
        private  String questionStem;
        /**
        * questionType
        */
        private  Integer questionType;
        /**
        * questionWrongReason
        */
        private  String questionWrongReason;
        /**
        * quetionSource
        */
        private  String quetionSource;
        /**
        * subjectId
        */
        private  Integer subjectId;
        /**
        * subjectName
        */
        private  String subjectName;
        /**
        * teacherId
        */
        private  String teacherId;
        /**
        * userAnswer
        */
        private  String userAnswer;
        /**
         * userAnswerImg
         */
        private  String userAnswerImg;
        /**
        * userAnswerTime
        */
        private  String userAnswerTime;
        /**
        * userId
        */
        private  String userId;

        //属性get||set方法


        public Short getCollect() {
        return this.collect;
        }

        public void setCollect(Short collect) {
        this.collect = collect;
        }



        public Short getCorrect() {
        return this.correct;
        }

        public void setCorrect(Short correct) {
        this.correct = correct;
        }




        public List<SccUserWrongQuestionCorrectBean> getCorrectList() {
            return this.correctList;
        }

        public void setCorrectList(List<SccUserWrongQuestionCorrectBean> correctList) {
            this.correctList = correctList;
        }



        public Integer getCorrectNum() {
        return this.correctNum;
        }

        public void setCorrectNum(Integer correctNum) {
        this.correctNum = correctNum;
        }



        public Short getDelStatus() {
        return this.delStatus;
        }

        public void setDelStatus(Short delStatus) {
        this.delStatus = delStatus;
        }



        public Integer getGradeId() {
        return this.gradeId;
        }

        public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
        }



        public String getGradeName() {
        return this.gradeName;
        }

        public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
        }



        public Integer getId() {
        return this.id;
        }

        public void setId(Integer id) {
        this.id = id;
        }



        public String getQuestionAnalysis() {
        return this.questionAnalysis;
        }

        public void setQuestionAnalysis(String questionAnalysis) {
        this.questionAnalysis = questionAnalysis;
        }



        public String getQuestionAnswer() {
        return this.questionAnswer;
        }

        public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
        }



        public String getQuestionContent() {
        return this.questionContent;
        }

        public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
        }



        public String getQuestionCreatetime() {
        return this.questionCreatetime;
        }

        public void setQuestionCreatetime(String questionCreatetime) {
        this.questionCreatetime = questionCreatetime;
        }



        public Integer getQuestionId() {
        return this.questionId;
        }

        public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
        }



        public String getQuestionSource() {
        return this.questionSource;
        }

        public void setQuestionSource(String questionSource) {
        this.questionSource = questionSource;
        }



        public String getQuestionStem() {
        return this.questionStem;
        }

        public void setQuestionStem(String questionStem) {
        this.questionStem = questionStem;
        }



        public Integer getQuestionType() {
        return this.questionType;
        }

        public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
        }



        public String getQuestionWrongReason() {
        return this.questionWrongReason;
        }

        public void setQuestionWrongReason(String questionWrongReason) {
        this.questionWrongReason = questionWrongReason;
        }



        public String getQuetionSource() {
        return this.quetionSource;
        }

        public void setQuetionSource(String quetionSource) {
        this.quetionSource = quetionSource;
        }



        public Integer getSubjectId() {
        return this.subjectId;
        }

        public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
        }



        public String getSubjectName() {
        return this.subjectName;
        }

        public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
        }



        public String getTeacherId() {
        return this.teacherId;
        }

        public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
        }



        public String getUserAnswer() {
        return this.userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
        }

        public String getUserAnswerImg() {
            return userAnswerImg;
        }

        public void setUserAnswerImg(String userAnswerImg) {
            this.userAnswerImg = userAnswerImg;
        }

        public String getUserAnswerTime() {
        return this.userAnswerTime;
        }

        public void setUserAnswerTime(String userAnswerTime) {
        this.userAnswerTime = userAnswerTime;
        }



        public String getUserId() {
        return this.userId;
        }

        public void setUserId(String userId) {
        this.userId = userId;
        }

    }

}