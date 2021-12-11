/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.cart.empty;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/24 17:41
 * @since 2.1.0
 */
public class LiveCartEmptyResponse extends ResponseResult {
    
    private ResultData data;
    
    public ResultData getData() {
        return data;
    }
    
    public void setData(ResultData data) {
        this.data = data;
    }
    
    
    private static class ResultData {
        /**
         * 更新条数
         */
        private int updateCount;

        //属性get||set方法

        public int getUpdateCount() {
            return updateCount;
        }

        public void setUpdateCount(int updateCount) {
            this.updateCount = updateCount;
        }
    }
}
