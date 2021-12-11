/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.gold.info;


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
public class LiveUserGoldInfoResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * entityGold
        */
        private  long entityGold;
        /**
        * gold
        */
        private  long gold;
        /**
        * inventedGold
        */
        private  long inventedGold;
        /**
        * status
        */
        private  int status;
        /**
        * userId
        */
        private  int userId;

        //属性get||set方法


        public long getEntityGold() {
        return this.entityGold;
        }

        public void setEntityGold(long entityGold) {
        this.entityGold = entityGold;
        }



        public long getGold() {
        return this.gold;
        }

        public void setGold(long gold) {
        this.gold = gold;
        }



        public long getInventedGold() {
        return this.inventedGold;
        }

        public void setInventedGold(long inventedGold) {
        this.inventedGold = inventedGold;
        }



        public int getStatus() {
        return this.status;
        }

        public void setStatus(int status) {
        this.status = status;
        }



        public int getUserId() {
        return this.userId;
        }

        public void setUserId(int userId) {
        this.userId = userId;
        }

    }
}
