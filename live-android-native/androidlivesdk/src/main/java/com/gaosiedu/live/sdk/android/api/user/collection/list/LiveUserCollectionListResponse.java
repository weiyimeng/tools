/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.collection.list;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.live.sdk.android.domain.*;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageRequest;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/30 19:32
 * @since 2.1.0
 */
public class LiveUserCollectionListResponse extends ResponseResult  {

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
        * courseDomain
        */
        private  CourseDomain courseDomain;
        /**
        * status
        */
        private  int status;
        /**
        * teacherDomain
        */
        private  TeacherDomain teacherDomain;

        //属性get||set方法


        public CourseDomain getCourseDomain() {
        return this.courseDomain;
        }

        public void setCourseDomain(CourseDomain courseDomain) {
        this.courseDomain = courseDomain;
        }



        public int getStatus() {
        return this.status;
        }

        public void setStatus(int status) {
        this.status = status;
        }



        public TeacherDomain getTeacherDomain() {
        return this.teacherDomain;
        }

        public void setTeacherDomain(TeacherDomain teacherDomain) {
        this.teacherDomain = teacherDomain;
        }

    }
}
