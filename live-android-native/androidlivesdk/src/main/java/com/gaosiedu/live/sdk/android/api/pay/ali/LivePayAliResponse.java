/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.pay.ali;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 支付宝支付 返回值
 *
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/12/03 13:31
 * @since 2.1.0
 */
public class LivePayAliResponse extends ResponseResult {
    
    private ResultData data;
    
    public ResultData getData() {
        return data;
    }
    
    public void setData(ResultData data) {
        this.data = data;
    }
    
    
    public static class ResultData {
        
        /**
         * data
         */
        private String data;
        /**
         * error
         */
        private String error;
        /**
         * orderNo
         */
        private String orderNo;
        /**
         * init,completed：支付完成,error：错误,cancle:表示订单已取消
         */
        private String status;
        
        //属性get||set方法
        
        
        public String getData() {
            return this.data;
        }
        
        public void setData(String data) {
            this.data = data;
        }
        
        
        public String getError() {
            return this.error;
        }
        
        public void setError(String error) {
            this.error = error;
        }
        
        
        public String getOrderNo() {
            return this.orderNo;
        }
        
        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }
        
        
        public String getStatus() {
            return this.status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
    }
    
    public static class AlipayInfo {
        private String platformParam;
        private String platformCode;
        
        public String getPlatformParam() {
            return platformParam;
        }
        
        public void setPlatformParam(String platformParam) {
            this.platformParam = platformParam;
        }
        
        public String getPlatformCode() {
            return platformCode;
        }
        
        public void setPlatformCode(String platformCode) {
            this.platformCode = platformCode;
        }
    }
}
