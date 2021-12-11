package com.haoke91.a91edu.net;

import com.gaosiedu.live.sdk.android.base.ResponseResult;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/26 下午2:39
 * 修改人：weiyimeng
 * 修改时间：2018/7/26 下午2:39
 * 修改备注：
 */
public abstract class ResponseCallback<T extends ResponseResult> {
    /**
     * @param date        date
     * @param isFromCache 是否缓存中读取
     */
    public abstract void onResponse(T date, boolean isFromCache);
    
    public void onEmpty(T date, boolean isFromCache) {
    }
    
    
    /**
     * @param fraction  下载的进度
     * @param totalSize 总字节长度
     */
    public void downloadProgress(float fraction, long totalSize) {
    }
    
    public void onError() {
    }
    
    public void onFail(T date, boolean isFromCache) {
    }
}
