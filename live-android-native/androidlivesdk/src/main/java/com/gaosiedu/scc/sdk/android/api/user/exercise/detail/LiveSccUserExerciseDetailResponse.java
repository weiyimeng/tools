/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.exercise.detail;


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
public class LiveSccUserExerciseDetailResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }




        public static class ResultData{


        /**
        * content
        */
        private  String content;
        /**
        * courseId
        */
        private  Integer courseId;
        /**
        * courseName
        */
        private  String courseName;
        /**
        * earliest
        */
        private  String earliest;
        /**
        * id
        */
        private  Integer id;
        /**
        * knowledgeName
        */
        private  String knowledgeName;
        /**
        * knowledgeOrder
        */
        private  Integer knowledgeOrder;
        /**
        * latest
        */
        private  String latest;
        /**
        * name
        */
        private  String name;
        /**
        * score
        */
        private  Integer score;
        /**
        * scoreStr
        */
        private  String scoreStr;

        //属性get||set方法


        public String getContent() {
        return this.content;
        }

        public void setContent(String content) {
        this.content = content;
        }



        public Integer getCourseId() {
        return this.courseId;
        }

        public void setCourseId(Integer courseId) {
        this.courseId = courseId;
        }



        public String getCourseName() {
        return this.courseName;
        }

        public void setCourseName(String courseName) {
        this.courseName = courseName;
        }



        public String getEarliest() {
        return this.earliest;
        }

        public void setEarliest(String earliest) {
        this.earliest = earliest;
        }



        public Integer getId() {
        return this.id;
        }

        public void setId(Integer id) {
        this.id = id;
        }



        public String getKnowledgeName() {
        return this.knowledgeName;
        }

        public void setKnowledgeName(String knowledgeName) {
        this.knowledgeName = knowledgeName;
        }



        public Integer getKnowledgeOrder() {
        return this.knowledgeOrder;
        }

        public void setKnowledgeOrder(Integer knowledgeOrder) {
        this.knowledgeOrder = knowledgeOrder;
        }



        public String getLatest() {
        return this.latest;
        }

        public void setLatest(String latest) {
        this.latest = latest;
        }



        public String getName() {
        return this.name;
        }

        public void setName(String name) {
        this.name = name;
        }



        public Integer getScore() {
        return this.score;
        }

        public void setScore(Integer score) {
        this.score = score;
        }



        public String getScoreStr() {
        return this.scoreStr;
        }

        public void setScoreStr(String scoreStr) {
        this.scoreStr = scoreStr;
        }

    }
}