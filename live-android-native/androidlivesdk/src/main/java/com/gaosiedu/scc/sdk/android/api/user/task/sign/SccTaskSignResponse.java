/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.scc.sdk.android.api.user.task.sign;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.scc.sdk.android.domain.*;

/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/09/11 15:44
 * @since 2.1.0
 */
public class SccTaskSignResponse extends ResponseResult {
    
    private ResultData data;
    
    public ResultData getData(){
        return data;
    }
    
    public void setData(ResultData data){
        this.data = data;
    }
    
    
    public static class ResultData {
        
        /**
         * userId
         */
        private String userId;
        
        //属性get||set方法
        
        public String getUserId(){
            return this.userId;
        }
        
        public void setUserId(String userId){
            this.userId = userId;
        }
        
    }
}
