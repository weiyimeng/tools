/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.address.count;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/30 19:10
 * @since 2.1.0
 */
public class LiveUserAddressCountResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * addressCount
        */
        private  int addressCount;
        /**
        * userId
        */
        private  int userId;

        //属性get||set方法


        public int getAddressCount() {
        return this.addressCount;
        }

        public void setAddressCount(int addressCount) {
        this.addressCount = addressCount;
        }



        public int getUserId() {
        return this.userId;
        }

        public void setUserId(int userId) {
        this.userId = userId;
        }

    }
}
