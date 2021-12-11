/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.common.area.all;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.ProvinceDomain;

import java.util.List;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/13 17:13
 * @since 2.1.0
 */
public class LiveAreaAllResponse extends ResponseResult {
    
    private ResultData data;
    
    public ResultData getData() {
        return data;
    }
    
    public void setData(ResultData data) {
        this.data = data;
    }
    
    
    public static class ResultData {
        
        /**
         * areas
         */
        private List<ProvinceDomain> areas;
        
        //属性get||set方法
        
        
        public List<ProvinceDomain> getAreas() {
            return this.areas;
        }
        
        public void setAreas(List<ProvinceDomain> areas) {
            this.areas = areas;
        }
        
    }
}
