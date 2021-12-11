/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.order.pre;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 返回值
 *
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/12/21 15:50
 * @since 2.1.0
 */
public class LiveOrderCheckResponse extends ResponseResult {
    
    private ResultData data;
    
    public ResultData getData() {
        return data;
    }
    
    public void setData(ResultData data) {
        this.data = data;
    }
    
    
    public static class ResultData {
        
        /**
         * flag
         */
        private boolean flag;
        
        //属性get||set方法
        
        
        public boolean getFlag() {
            return this.flag;
        }
        
        public void setFlag(boolean flag) {
            this.flag = flag;
        }
        
    }
}
