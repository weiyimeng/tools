/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.storage.token;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
 * 集团存储 第一步获取的token 请求接口
 *
 * @author sdk-generator-android request
 * @describe
 * @date 2018/12/05 11:53
 * @since 2.1.0
 */
public class LiveStorageTokenRequest extends LiveSdkBaseRequest {
    
    private transient final String PATH = "storage/token";
    
    public LiveStorageTokenRequest() {
        super();
        setPath(PATH);
    }
    
    private int userId;
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    //属性get||set方法
}
