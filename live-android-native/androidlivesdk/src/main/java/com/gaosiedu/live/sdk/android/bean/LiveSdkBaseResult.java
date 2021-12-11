package com.gaosiedu.live.sdk.android.bean;


import java.io.Serializable;

public class LiveSdkBaseResult<T> implements Serializable {

T data;
    private String code;

    private String msg;


    public LiveSdkResult toLiveSdkResult(){
        LiveSdkResult liveSdkResult = new LiveSdkResult();
        liveSdkResult.setCode(code);
        liveSdkResult.setMsg(msg);
        return liveSdkResult;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
