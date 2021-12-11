/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.user.balance.info;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/30 19:28
 * @since 2.1.0
 */
public class LiveUserBalanceInfoResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{

        /**
        * availableBalance
        */
        private  BigDecimal availableBalance;
        /**
        * balance
        */
        private  BigDecimal balance;
        /**
        * freezeBalance
        */
        private  BigDecimal freezeBalance;
        /**
        * status
        */
        private  int status;
        /**
        * userId
        */
        private  int userId;

        //属性get||set方法


        public BigDecimal getAvailableBalance() {
        return this.availableBalance;
        }

        public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
        }



        public BigDecimal getBalance() {
        return this.balance;
        }

        public void setBalance(BigDecimal balance) {
        this.balance = balance;
        }



        public BigDecimal getFreezeBalance() {
        return this.freezeBalance;
        }

        public void setFreezeBalance(BigDecimal freezeBalance) {
        this.freezeBalance = freezeBalance;
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
