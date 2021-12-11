/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.sms.code;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 发送短信验证码 返回值
 *
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/11/23 11:34
 * @since 2.1.0
 */
public class LiveMobileCodeResponse extends ResponseResult {
    
    private ResultData data;
    
    public ResultData getData() {
        return data;
    }
    
    public String failMsg;
    public String failCode;
    
    public void setData(ResultData data) {
        this.data = data;
    }
    
    
    public static class ResultData {
        
        
        //属性get||set方法
    }
}
