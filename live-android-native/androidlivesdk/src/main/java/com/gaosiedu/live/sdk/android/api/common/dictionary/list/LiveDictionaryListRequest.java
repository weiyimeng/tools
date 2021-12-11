/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.common.dictionary.list;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;


/**
 * @author sdk-generator-android request
 * @describe
 * @date 2018/08/29 16:04
 * @since 2.1.0
 */
public class LiveDictionaryListRequest extends LiveSdkBaseRequest {
    
    private transient final String PATH = "common/dictionary/list";
    
    public LiveDictionaryListRequest() {
        super();
        setPath(PATH);
    }
    
    
    /**
     * subject  课程
     * grade    年级
     * term     假期
     */
    private String dicType;
    
    
    //属性get||set方法
    public String getDicType() {
        return this.dicType;
    }
    
    public void setDicType(String dicType) {
        this.dicType = dicType;
    }
    
}
