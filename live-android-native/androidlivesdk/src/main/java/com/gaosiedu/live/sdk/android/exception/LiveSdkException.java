package com.gaosiedu.live.sdk.android.exception;

import com.google.gson.Gson;
import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseResult;

/**
 *
 */
public class LiveSdkException extends IllegalStateException {

    private LiveSdkBaseResult sdkBaseResult;

    public LiveSdkException(String s) {
        super(s);
        this.sdkBaseResult = new Gson().fromJson(s,LiveSdkBaseResult.class);
    }

    public LiveSdkBaseResult getSdkBaseResult() {
        return sdkBaseResult;
    }

    public void setSdkBaseResult(LiveSdkBaseResult sdkBaseResult) {
        this.sdkBaseResult = sdkBaseResult;
    }
}
