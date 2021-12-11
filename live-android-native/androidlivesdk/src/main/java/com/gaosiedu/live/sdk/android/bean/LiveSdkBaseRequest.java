package com.gaosiedu.live.sdk.android.bean;

import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.lang.reflect.Type;


/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/16 下午7:30
 * 修改人：weiyimeng
 * 修改时间：2018/8/16 下午7:30
 * 修改备注：
 */
public abstract class LiveSdkBaseRequest implements Serializable {
    
    
    /**
     * 请求路径
     */
    public transient String path;
    
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
}
