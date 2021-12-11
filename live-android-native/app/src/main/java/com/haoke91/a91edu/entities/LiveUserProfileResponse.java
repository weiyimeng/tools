package com.haoke91.a91edu.entities;


import com.gaosiedu.live.sdk.android.base.ResponseResult;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseResponse;

public class LiveUserProfileResponse extends ResponseResult {
    private UserInfo data;
    
    public UserInfo getData() {
        return data;
    }
    
    public void setData(UserInfo data) {
        this.data = data;
    }
}
