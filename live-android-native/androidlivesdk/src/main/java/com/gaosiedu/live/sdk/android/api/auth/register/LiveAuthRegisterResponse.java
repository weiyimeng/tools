/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.auth.register;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**  注册 返回值
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/11/23 11:13
 * @since 2.1.0
 */
public class LiveAuthRegisterResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * registerFlag
        */
        private  Boolean registerFlag;
        /**
        * token
        */
        private  String token;

        //属性get||set方法


        public Boolean getRegisterFlag() {
        return this.registerFlag;
        }

        public void setRegisterFlag(Boolean registerFlag) {
        this.registerFlag = registerFlag;
        }



        public String getToken() {
        return this.token;
        }

        public void setToken(String token) {
        this.token = token;
        }

    }
}