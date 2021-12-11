/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.common.area;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.util.List;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/29 16:04
 * @since 2.1.0
 */
public class LiveAreaListResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    private static class ResultData{

        /**
        * list
        */
        private  List<AreaDaomain> list;

        //属性get||set方法



        public List<AreaDaomain> getList() {
            return this.list;
        }

        public void setList(List<AreaDaomain> list) {
            this.list = list;
        }

    }
}