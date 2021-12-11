/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.cart.delete;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/24 18:00
 * @since 2.1.0
 */
public class LiveCartDeleteResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }



    public static class ResultData{
        /**
        * 大于0 标识删除成功
        */
        private  int result;

        //属性get||set方法


        public int getResult() {
        return this.result;
        }

        public void setResult(int result) {
        this.result = result;
        }

    }
}
