package com.classroomsdk.interfaces;

/**
 * Created by Administrator on 2017/5/9.
 */

public interface IWBStateCallBack {


    /**
     * 文档状态改变
     * @param isdel
     * @param ismedia
     */
    void onRoomDocChange(boolean isdel, boolean ismedia);

    /**
     * 白板全屏
     * @param isZoom  true放大  false缩小
     */
    void onWhiteBoradZoom(boolean isZoom);

    /**
     * 音频数据
     * @param url
     * @param isvideo
     * @param fileid  文件id
     */
    void onWhiteBoradMediaPublish(String url, boolean isvideo, long fileid);

    /**
     * 白板动作
     * @param stateJson
     */
    void onWhiteBoradAction(String stateJson);


}
