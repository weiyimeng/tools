/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.order.returned.cancel;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/31 12:05
 * @since 2.1.0
 */
public class LiveUserOrderReturnedCancelResponse extends ResponseResult  {

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
