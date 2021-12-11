package com.haoke91.videolibrary.model;

/**
 * Created by liyuejiao on 2018/1/31.
 */

public class VodRspData {
    public String cover;
    public String url;
    public String title;
    public int duration;
    
    @Override
    public String toString() {
        return "VodRspData{" +
            "cover='" + cover + '\'' +
            ", url='" + url + '\'' +
            ", title='" + title + '\'' +
            ", duration=" + duration +
            '}';
    }
}
