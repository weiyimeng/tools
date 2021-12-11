/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.auth.forget;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.domain.*;

import java.math.BigDecimal;
import java.util.*;

/**  忘记密码 返回值
 * @author sdk-generator-adnroid response
 * @describe
 * @date 2018/11/23 15:33
 * @since 2.1.0
 */
public class LiveAuthForgetResponse extends ResponseResult  {

    private ResultData data;

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public static class ResultData{


        //属性get||set方法
    }
}