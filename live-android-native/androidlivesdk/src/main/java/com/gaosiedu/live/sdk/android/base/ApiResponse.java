/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.base;


/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/31 10:19
 * @since 2.1.0
 */
public class ApiResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * result
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