package com.classroomsdk;

import android.app.Fragment;

import com.classroomsdk.bean.ShareDoc;
import com.classroomsdk.interfaces.IWBStateCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class WhiteBoradConfig {

    private static volatile WhiteBoradConfig cInstance;
    private WhiteBoradManager mManager = WhiteBoradManager.getInstance();
    private WBFragment fragment ;
    public int totalpagenum;
    public int currentNumber;

    public static WhiteBoradConfig getsInstance() {
        if (cInstance == null) {
            synchronized (WhiteBoradConfig.class) {
                if (cInstance == null) {
                    cInstance = new WhiteBoradConfig();
                }
            }
        }
        return cInstance;
    }

    /**
     * 创建白板并注册
     */

    public WBFragment CreateWhiteBorad(IWBStateCallBack callBack){
        fragment = new WBFragment();
        registerDelegate(callBack,fragment);
        return fragment;
    }

    /**
     * 白板大小
     * @param width  宽
     * @param height 高
     */
    public void SetTransmitWindowSize(int width, int height) {
        if (fragment != null && WBSession.isPageFinish) {
            fragment.transmitWindowSize(width,height);
        }
    }

    /**
     * 退出清除缓存及白板
     */
    public void clear() {
        fragment = null;
        mManager.clear();
    }

    /**
     * 白板是否是回放
     * @param playBack  true|false   是|不是
     */
    public void setPlayBack(boolean playBack) {
        if (fragment != null) {
            fragment.setPlayBack(playBack);
        }
    }

    /**
     * 添加文档
     * @param serial 教室号
     * @param path  文件路径
     * @param isClassBegin 是否上课
     */
    public void uploadRoomFile(String serial,String path,boolean isClassBegin){
        mManager.uploadRoomFile(serial, path, isClassBegin);
    }

    /**
     * 删除文档
     * @param roomNum  教室号
     * @param fileid   文件id
     * @param ismedia  是否音频文件
     * @param isClassBegin 是否已上课
     */
    public void delRoomFile(String roomNum,long fileid,boolean ismedia,boolean isClassBegin){
        mManager.delRoomFile(roomNum, fileid, ismedia,isClassBegin);
    }

    /**
     * 切换文档
     * @param doc 文档对象
     */
    public void localChangeDoc(ShareDoc doc){
        if (fragment != null && WBSession.isPageFinish) {
            mManager.localChangeDoc(doc);
        }
    }
    /**
     * 切换文档
     */
    public void localChangeDoc(){
        if (fragment != null && WBSession.isPageFinish) {
            fragment.localChangeDoc();
        }
    }

    /**
     * 获取 host
     * @return
     */
    public String getFileServierUrl() {
        return mManager.getFileServierUrl();
    }

    /**
     * 获取port
     * @return
     */
    public int getFileServierPort() {
        return mManager.getFileServierPort();
    }

    /**
     * 获取当前课件库文件
     * @return
     */
    public ShareDoc getCurrentFileDoc() {
        return mManager.getCurrentFileDoc();
    }

    /**
     * 获取当前媒体库文件
     * @return
     */
    public ShareDoc getCurrentMediaDoc() {
        return mManager.getCurrentMediaDoc();
    }

    /**
     * 设置当前媒体文件
     * @param doc
     */
    public void setCurrentMediaDoc(ShareDoc doc) {
        mManager.setCurrentMediaDoc(doc);
    }

    /**
     * 设置当前课件库文件
     * @param doc
     */
    public void setCurrentFileDoc(ShareDoc doc) {
        mManager.setCurrentFileDoc(doc);
    }

    /**
     * 获取课件库 教室文件
     * @return
     */
    public ArrayList<ShareDoc> getClassDocList() {
        return mManager.getClassDocList();
    }

    /**
     * 获取课件库 公用文件
     * @return
     */
    public ArrayList<ShareDoc> getAdminDocList() {
        return mManager.getAdminDocList();
    }

    /**
     * 获取媒体库 教室文件
     * @return
     */
    public ArrayList<ShareDoc> getClassMediaList() {
        return mManager.getClassMediaList();
    }

    /**
     * 获取媒体库 公用文件
     * @return
     */
    public ArrayList<ShareDoc> getAdminmMediaList() {
        return mManager.getAdminmMediaList();
    }

    /**
     * 获取所有课件库文件
     * @return
     */
    public ArrayList<ShareDoc> getDocList() {
        return mManager.getDocList();
    }

    /**
     * 获取所有媒体库文件
     * @return
     */
    public ArrayList<ShareDoc> getMediaList() {
        return mManager.getMediaList();
    }

    /**
     * 回放播放控制
     * @param isplay
     */
    public void playbackPlayAndPauseController(boolean isplay) {
        mManager.playbackPlayAndPauseController(isplay);
    }

    /**
     * 是否显示工具箱
     * @param isShow 是否显示 true 显示
     */
    public void showToolbox(boolean isShow){
        if (fragment != null && WBSession.isPageFinish) {
            try {
                JSONObject js = new JSONObject();
                js.put("isShow", isShow);
                fragment.interactiveJS("'toolbox'", js.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否显示画笔工具
     * @param isShow 是否显示
     */
    public void choosePen(boolean isShow){
        if (fragment != null && WBSession.isPageFinish) {
            try {
                JSONObject js = new JSONObject();
                js.put("isShow", isShow);
                fragment.interactiveJS("'chooseShow'", js.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否显示自定义奖杯
     * @param isShow  true 显示
     */
    public void showCustomTrophy(boolean isShow){
        if (fragment != null && WBSession.isPageFinish) {
            try {
                JSONObject js = new JSONObject();
                js.put("isShow", isShow);
                fragment.interactiveJS("'custom_trophy'", js.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     下一页
     @return  是否切到上一页
     */
    public void nextPage(){
        if (fragment != null && WBSession.isPageFinish) {
            ShareDoc shareDoc = mManager.getCurrentFileDoc();
            fragment.interactiveJSPaging(shareDoc, true, totalpagenum > currentNumber ? true : false);
        }
    }

    /**
     上一页
     */
    public void prePage(){
        if (fragment != null && WBSession.isPageFinish) {
            ShareDoc shareDoc = mManager.getCurrentFileDoc();
            fragment.interactiveJSPaging(shareDoc, false, false);
        }
    }

    /**
     跳转到指定页
     @param pageNum 跳转的页码
     */
    public void skipToPageNum(int pageNum) {
        if (fragment != null && WBSession.isPageFinish) {
            fragment.interactiveJSSelectPage(pageNum);
        }

    }

    /**
     * 放大或缩小白板
     * @param EnlargeOrNarrow  true 放大  false 缩小
     */
    public void EnlargeOrNarrowWhiteboard(boolean EnlargeOrNarrow){
        if (fragment != null && WBSession.isPageFinish) {
            if (EnlargeOrNarrow) {
                fragment.interactiveJS("'whiteboardSDK_enlargeWhiteboard'", null);
            } else {
                fragment.interactiveJS("'whiteboardSDK_narrowWhiteboard'", null);
            }
        }
    }

    /**
     * 全屏  退出全屏
     * @param isFull true 全屏 false 退出全屏
     */
    public void changeWebPageFullScreen(boolean isFull){
        if (fragment != null && WBSession.isPageFinish) {
            fragment.changeWebPageFullScreen(isFull);
        }

    }

    /**
     * 通知全屏放大
     * @param isFull
     */
    public void sendJSPageFullScreen(boolean isFull) {
        if (fragment != null && WBSession.isPageFinish) {
            fragment.sendJSPageFullScreen(isFull);
        }
    }

    /**
     * 关闭PPT
     */
    public void closeNewPptVideo(){
        if (fragment != null && WBSession.isPageFinish) {
            fragment.closeNewPptVideo();
        }
    }

    /**
     * 是否打开备注
     * @param isopen
     */
    public void openDocumentRemark(boolean isopen){
        if (fragment != null && WBSession.isPageFinish) {
            String action;
            if (isopen) {
                action = "'whiteboardSDK_openDocumentRemark'";
            } else {
                action = "'whiteboardSDK_closeDocumentRemark'";
            }
            fragment.interactiveJS(action, null);
        }

    }

    /**
     * 注册白板
     */
    private void registerDelegate(IWBStateCallBack callBack,Fragment fragment){
        //注册
        mManager.setWBCallBack(callBack);
        mManager.setLocalControl((WBFragment)fragment);
    }


    /**
     * 白板页数
     * @param totalPage  总页
     * @param current    当前页
     */
    public void SetPage(int totalPage,int current){
        this.totalpagenum = totalPage;
        this.currentNumber = current;
    }

}
