/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.storage.token;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**  集团存储 第一步获取的token 返回值
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/12/05 15:11
 * @since 2.1.0
 */
public class LiveStorageTokenResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * authorization
        */
        private  String authorization;
        /**
        * businessKey
        */
        private  String businessKey;
        /**
        * env
        */
        private  String env;
        /**
        * url
        */
        private  String url;

        //属性get||set方法


        public String getAuthorization() {
        return this.authorization;
        }

        public void setAuthorization(String authorization) {
        this.authorization = authorization;
        }



        public String getBusinessKey() {
        return this.businessKey;
        }

        public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
        }



        public String getEnv() {
        return this.env;
        }

        public void setEnv(String env) {
        this.env = env;
        }



        public String getUrl() {
        return this.url;
        }

        public void setUrl(String url) {
        this.url = url;
        }

    }
}