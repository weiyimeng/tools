/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.base.ossUploadToken;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/21 11:00
 * @since 2.1.0
 */
public class LiveUserBaseOssUploadTokenResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * 临时的AccessKeyId
        */
        private  String accessKeyId;
        /**
        * 临时的AccessKeySecret
        */
        private  String accessKeySecret;
        /**
        * 过期时间
        */
        private  String expirationTime;
        /**
        * 临时的RequestId
        */
        private  String requestId;
        /**
        * 临时的securityToken
        */
        private  String securityToken;

        //属性get||set方法


        public String getAccessKeyId() {
        return this.accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
        }



        public String getAccessKeySecret() {
        return this.accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
        }



        public String getExpirationTime() {
        return this.expirationTime;
        }

        public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
        }



        public String getRequestId() {
        return this.requestId;
        }

        public void setRequestId(String requestId) {
        this.requestId = requestId;
        }



        public String getSecurityToken() {
        return this.securityToken;
        }

        public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
        }

    }
}