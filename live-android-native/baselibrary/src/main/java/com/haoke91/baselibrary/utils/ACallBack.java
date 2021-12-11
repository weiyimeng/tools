package com.haoke91.baselibrary.utils;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/11/5 5:38 PM
 * 修改人：weiyimeng
 * 修改时间：2018/11/5 5:38 PM
 * 修改备注：
 */
public interface ACallBack<T> {
    int AGORA_STREAM = 0x01;//声网直播第一帧
    int ALIYUNLIVE_STREAM=0x02;//阿里直播第一帧
    int ALIYUNVIDEO_STREAM=0x03;//阿里云回放第一帧
    
    void call(T t);
}
