/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.cart.add;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/24 17:43
 * @since 2.1.0
 */
public class LiveCartCreateResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }



    public static class ResultData {
        /**
        * id
        */
        private  int id;
        /**
        * 0false 1true
        */
        private  boolean flag;

        //属性get||set方法


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }
}
