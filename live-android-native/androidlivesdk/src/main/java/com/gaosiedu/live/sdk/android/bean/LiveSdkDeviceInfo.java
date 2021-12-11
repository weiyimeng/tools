package com.gaosiedu.live.sdk.android.bean;

import java.io.Serializable;

/**
 * MyApplication
 *
 * @author lianyutao
 * @date 2018/8/17
 * @description ${DESCRIPTION}
 */
public class LiveSdkDeviceInfo implements Serializable{

    /**
     * 设备名称或品牌
     */
    private String deviceName;

    /**
     * 设备名称或品牌
     */
    private String deviceSystemName;


    /**
     * 设备系统版本
     */
    private String deviceSystemVersion;

    /**
     * 应用系统版本
     */
    private String appVersion;

    /**
     * sdk版本
     */
    private String sdkVersion;

    /**
     * 其他信息
     */
    private String otherInfo;
    

    
    //TODO 其他信息可以与后台预定


    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceSystemName() {
        return deviceSystemName;
    }

    public void setDeviceSystemName(String deviceSystemName) {
        this.deviceSystemName = deviceSystemName;
    }

    public String getDeviceSystemVersion() {
        return deviceSystemVersion;
    }

    public void setDeviceSystemVersion(String deviceSystemVersion) {
        this.deviceSystemVersion = deviceSystemVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }
    
    
}
