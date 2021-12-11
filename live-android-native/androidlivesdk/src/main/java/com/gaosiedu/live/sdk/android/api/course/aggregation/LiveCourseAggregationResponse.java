/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.course.aggregation;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.live.sdk.android.domain.*;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/29 19:15
 * @since 2.1.0
 */
public class LiveCourseAggregationResponse extends ResponseResult  {

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
    public static class ListData implements Serializable{
        /**
        * courseCount
        */
        private  int courseCount;
        /**
        * grade
        */
        private  int grade;
        /**
        * gradeName
        */
        private  String gradeName;
        /**
        * icoImg
        */
        private  String icoImg;
        /**
        * maxPrice
        */
        private  double maxPrice;
        /**
        * minPrice
        */
        private  double minPrice;
        /**
        * name
        */
        private  String name;
        /**
        * signUpCount
        */
        private  int signUpCount;
        /**
        * subjectId
        */
        private  int subjectId;
        /**
        * subjectName
        */
        private  String subjectName;
        /**
        * term
        */
        private  int term;
        /**
        * termName
        */
        private  String termName;

        //属性get||set方法


        public int getCourseCount() {
        return this.courseCount;
        }

        public void setCourseCount(int courseCount) {
        this.courseCount = courseCount;
        }



        public int getGrade() {
        return this.grade;
        }

        public void setGrade(int grade) {
        this.grade = grade;
        }



        public String getGradeName() {
        return this.gradeName;
        }

        public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
        }



        public String getIcoImg() {
        return this.icoImg;
        }

        public void setIcoImg(String icoImg) {
        this.icoImg = icoImg;
        }



        public double getMaxPrice() {
        return this.maxPrice;
        }

        public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
        }



        public double getMinPrice() {
        return this.minPrice;
        }

        public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
        }



        public String getName() {
        return this.name;
        }

        public void setName(String name) {
        this.name = name;
        }



        public int getSignUpCount() {
        return this.signUpCount;
        }

        public void setSignUpCount(int signUpCount) {
        this.signUpCount = signUpCount;
        }



        public int getSubjectId() {
        return this.subjectId;
        }

        public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
        }



        public String getSubjectName() {
        return this.subjectName;
        }

        public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
        }



        public int getTerm() {
        return this.term;
        }

        public void setTerm(int term) {
        this.term = term;
        }



        public String getTermName() {
        return this.termName;
        }

        public void setTermName(String termName) {
        this.termName = termName;
        }

    }
}
