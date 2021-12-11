/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.haoke91.a91edu.entities;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseResponse;


/**
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/08/21 16:34
 * @since 2.1.0
 */
public class LiveLoginResponse extends ResponseResult {
    
    /**
     * 访问token
     */
    //  private String token;
    
    private Token data;
    
    
    public Token getData() {
        return data;
    }
    
    public void setData(Token data) {
        this.data = data;
    }
    
    
    public static class Token {
        private String token;
        private UserInfo userProfileDomain;
        
        //属性get||set方法
        public String getToken() {
            return this.token;
        }
        
        public void setToken(String token) {
            this.token = token;
        }
        
        public UserInfo getUserProfileDomain() {
            return userProfileDomain;
        }
        
        public void setUserProfileDomain(UserInfo userProfileDomain) {
            this.userProfileDomain = userProfileDomain;
        }
    }
    
}
