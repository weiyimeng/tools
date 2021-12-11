/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.task.list;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.scc.sdk.android.domain.*;
import java.util.List;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/11 15:44
 * @since 2.1.0
 */
public class SccGetUserTasksResponse extends ResponseResult  {

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
        private  List<SccUserTaskList> list;

        //属性get||set方法



        public List<SccUserTaskList> getList() {
            return this.list;
        }

        public void setList(List<SccUserTaskList> list) {
            this.list = list;
        }

    }
}