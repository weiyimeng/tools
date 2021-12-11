/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.cart.list;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.util.List;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/29 14:11
 * @since 2.1.0
 */
public class LiveCartListResponse extends ResponseResult {
    
    private ResultData data;
    
    public ResultData getData() {
        return data;
    }
    
    public void setData(ResultData data) {
        this.data = data;
    }
    
    
    public static class ResultData {
        /**
         *
         */
        private List<ShoppingCarDomain> list;
        
        //属性get||set方法
        
        
        public List<ShoppingCarDomain> getList() {
            return this.list;
        }
        
        public void setList(List<ShoppingCarDomain> list) {
            this.list = list;
        }
        
    }
}
