package com.gaosiedu.live.sdk.android.bean;


import com.gaosiedu.live.sdk.android.base.ResponseResult;

public class LiveSdkBasePageRequest extends LiveSdkBaseRequest {
    
    private Integer pageMaxSize = 16;
    
    private Integer pageNum = 1;
    
    private Integer pageSize = pageMaxSize;
    
    
    public Integer getPageNum() {
        return pageNum;
    }
    
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum <= 0 ? 1 : pageNum;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize <= 0 || pageSize > pageMaxSize ? pageMaxSize : pageSize;
    }
    
}
