/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.course.last;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.scc.sdk.android.domain.*;
import java.util.List;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/31 10:00
 * @since 2.1.0
 */
public class LiveSccUserCourseLastResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }




        public static class ResultData{


        /**
        * knowledges
        */
        private  List<CourseKnowledgeBean> knowledges;

        //属性get||set方法



        public List<CourseKnowledgeBean> getKnowledges() {
            return this.knowledges;
        }

        public void setKnowledges(List<CourseKnowledgeBean> knowledges) {
            this.knowledges = knowledges;
        }

    }
}