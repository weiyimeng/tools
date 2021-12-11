/*
* Copyright (c) 2016,gaosiedu.com
*/
package com.gaosiedu.live.sdk.android.api.content.banner;

import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

import java.util.*;


/**
 * 首页banner图 请求接口
 *
 * @author sdk-generator-android request
 * @describe
 * @date 2018/12/07 16:09
 * @since 2.1.0
 */
public class LiveContentBannerRequest extends LiveSdkBaseRequest {
    
    private transient final String PATH = "content/banner";
    
    public LiveContentBannerRequest() {
        super();
        setPath(PATH);
    }
    
    
    /**
     * 客户端平台
     */
    private String platForm = "app";
    
    /**
     * 内容类型
     */
    private Integer type;
    
    
    //属性get||set方法
    public String getPlatForm() {
        return this.platForm;
    }
    
    public void setPlatForm(String platForm) {
        this.platForm = platForm;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    
}
