/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.auth.pubKey;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**  登陆加密公钥 返回值
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/11/27 15:37
 * @since 2.1.0
 */
public class LivePubKeyResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * pubKey
        */
        private  String pubKey;

        //属性get||set方法


        public String getPubKey() {
        return this.pubKey;
        }

        public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
        }

    }
}