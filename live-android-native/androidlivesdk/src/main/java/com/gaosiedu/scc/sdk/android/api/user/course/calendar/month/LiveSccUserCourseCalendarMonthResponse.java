/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.course.calendar.month;


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
public class LiveSccUserCourseCalendarMonthResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }




        public static class ResultData{


        /**
        * list
        */
        private  List<UserCourseMonthBean> list;

        //属性get||set方法



        public List<UserCourseMonthBean> getList() {
            return this.list;
        }

        public void setList(List<UserCourseMonthBean> list) {
            this.list = list;
        }

    }
}