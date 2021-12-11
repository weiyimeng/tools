package com.gstudentlib.event;

import java.io.Serializable;

public class UpdateHyResourceEvent implements Serializable {
    public boolean shouldUpdate;

    //如果不需要更新，默认完成更新h5图片资源
    public boolean finishUpdate = true;

    //资源更新状态
    public int updateStatus = 0;

    //下载地址
    public String zipUrl;
}