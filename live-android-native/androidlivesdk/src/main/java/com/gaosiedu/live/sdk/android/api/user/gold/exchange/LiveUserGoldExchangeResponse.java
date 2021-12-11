/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.gold.exchange;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/05 10:12
 * @since 2.1.0
 */
public class LiveUserGoldExchangeResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * flag
        */
        private  boolean flag;
        /**
        * msg
        */
        private  String msg;

        //属性get||set方法


        public boolean getFlag() {
        return this.flag;
        }

        public void setFlag(boolean flag) {
        this.flag = flag;
        }



        public String getMsg() {
        return this.msg;
        }

        public void setMsg(String msg) {
        this.msg = msg;
        }

    }
}
