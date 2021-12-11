/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.wrongquestions.correct.list;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBasePageResponse;
import com.gaosiedu.scc.sdk.android.domain.*;
import java.util.List;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/30 15:18
 * @since 2.1.0
 */
public class LiveSccUserWrongQuestionCorrectListResponse extends ResponseResult  {

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
        private  List<SccUserWrongQuestionCorrectBean> list;

        //属性get||set方法



        public List<SccUserWrongQuestionCorrectBean> getList() {
            return this.list;
        }

        public void setList(List<SccUserWrongQuestionCorrectBean> list) {
            this.list = list;
        }

    }
}