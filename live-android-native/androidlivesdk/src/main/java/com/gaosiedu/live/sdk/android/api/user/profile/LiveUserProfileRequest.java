package com.gaosiedu.live.sdk.android.api.user.profile;


import com.gaosiedu.live.sdk.android.bean.LiveSdkBaseRequest;

public class LiveUserProfileRequest extends LiveSdkBaseRequest {

    private final String PATH = "user/profile";

    public LiveUserProfileRequest() {
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


}
