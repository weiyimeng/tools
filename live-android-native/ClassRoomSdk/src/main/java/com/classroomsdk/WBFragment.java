package com.classroomsdk;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.classroomsdk.bean.ShareDoc;
import com.classroomsdk.interfaces.ILocalControl;
import com.classroomsdk.interfaces.IWBCallback;
import com.classroomsdk.interfaces.JSWhitePadInterface;
import com.talkcloud.room.RoomUser;
import com.talkcloud.room.TKRoomManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class WBFragment extends Fragment implements NotificationCenter.NotificationCenterDelegate, IWBCallback, ILocalControl {
    
    private View fragmentView;
    XWalkView xWalkView;
    private boolean candraw = false;
    private boolean isTouchable = false;
    private boolean isClassBegin = false;
    private ShareDoc currentFile;
    private boolean isPlayBack = false;
    private SharedPreferences spkv = null;
    private SharedPreferences.Editor editor = null;
    
    public void setPlayBack(boolean playBack) {
        isPlayBack = playBack;
    }
    
    @SuppressLint("JavascriptInterface")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_white_pad, null);
            XWalkPreferences.setValue("enable-javascript", true);
            XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
            XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
            
            XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
            XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);
            
            xWalkView = (XWalkView) fragmentView.findViewById(R.id.xwalkWebView);
            
            xWalkView.setZOrderOnTop(false);
            XWalkSettings webs = xWalkView.getSettings();
            webs.setJavaScriptEnabled(true);
            webs.setCacheMode(WebSettings.LOAD_DEFAULT);
            webs.setDomStorageEnabled(true);
            webs.setDatabaseEnabled(true);
            webs.setAllowFileAccess(true);
            webs.setSupportZoom(false);
            webs.setBuiltInZoomControls(false);
            
            webs.setLoadWithOverviewMode(false);
            webs.setJavaScriptCanOpenWindowsAutomatically(true);
            webs.setLoadWithOverviewMode(true);
            webs.setDomStorageEnabled(true);
            webs.setUseWideViewPort(true);
            webs.setMediaPlaybackRequiresUserGesture(false);
            webs.setSupportSpatialNavigation(true);
            webs.setAllowFileAccessFromFileURLs(true);
            
            webs.setLayoutAlgorithm(XWalkSettings.LayoutAlgorithm.NORMAL);
            webs.setUseWideViewPort(true);
            
            xWalkView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            xWalkView.setHorizontalScrollBarEnabled(false);
            JSWhitePadInterface.getInstance().setWBCallBack(this);
            xWalkView.addJavascriptInterface(JSWhitePadInterface.getInstance(), "JSWhitePadInterface");
            
            xWalkView.setLayerType(View.LAYER_TYPE_HARDWARE, null);// 硬件加速
            /*xWalkView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 软件加速*/
            xWalkView.requestFocus();

            /*xWalkView.setOnClickListener(pageClickListener);*/
            
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = LocaleList.getDefault().get(0);
            } else {
                locale = Locale.getDefault();
            }
            
            String lan = null;
            if (locale.equals(Locale.TAIWAN)) {
                lan = "tw";
            } else if (locale.equals(Locale.SIMPLIFIED_CHINESE)) {
                lan = "ch";
            } else if (locale.equals(Locale.ENGLISH) || locale.toString().equals("en_US".toString())) {
                lan = "en";
            }
            if (TextUtils.isEmpty(lan)) {
                if (locale.toString().endsWith("#Hant")) {
                    lan = "tw";
                }
                if (locale.toString().endsWith("#Hans")) {
                    lan = "ch";
                }
            }
            //            }
            if (Config.isWhiteBoardTest) {
                //                xWalkView.loadUrl("http://192.168.1.220:9251/publish/index.html#/mobileApp?languageType=" + lan);
                //                xWalkView.loadUrl("http://192.168.1.182:9403/publish/index.html#/mobileApp?ts=" + System.currentTimeMillis());
                xWalkView.loadUrl("http://192.168.0.200:9251/publish/index.html#/mobileApp?languageType=" + lan);//建行
                //                xWalkView.loadUrl("http://192.168.1.108:8444/publish/index.html#/mobileApp?languageType=" + lan);//李珂
                //                 xWalkView.loadUrl("http://192.168.1.220:9251/publish/index.html#/mobileApp?languageType=" + lan);
            } else {
                /*xWalkView.loadUrl("http://baidu.com");*/
                xWalkView.loadUrl("file:///android_asset/react_mobile_new_publishdir/index.html#/mobileApp?languageType=" + lan);
            }
            
            xWalkView.setResourceClient(new XWalkResourceClient(xWalkView) {
                @Override
                public void onReceivedSslError(XWalkView view, ValueCallback<Boolean> callback, SslError error) {
                    callback.onReceiveValue(true);
                }
            });
            
            xWalkView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    
                    switch (event.getAction()) {
                        
                        case MotionEvent.ACTION_UP:
                            if (!candraw || !isTouchable) {
                                xWalkView.callOnClick();
                            } else {
                                return v.onTouchEvent(event);
                            }
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }
        return fragmentView;
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        NotificationCenter.getInstance().removeObserver(this);
        WBSession.getInstance().onRelease();
    }
    
    public void setDrawable(boolean candraw) {
        this.candraw = candraw;
    }
    
    public void setWBTouchable(boolean isTouchable) {
        this.isTouchable = isTouchable;
    }
    
    
    private void setCurrentFile() {
        if (RoomControler.isDocumentClassification()) {
            if (WhiteBoradManager.getInstance().getClassDocList().size() > 0) {
                currentFile = WhiteBoradManager.getInstance().getClassDocList().get(0);
                WhiteBoradManager.getInstance().setCurrentFileDoc(currentFile);
            } else {
                if (WhiteBoradManager.getInstance().getAdminDocList().size() > 0) {
                    currentFile = WhiteBoradManager.getInstance().getAdminDocList().get(0);
                    WhiteBoradManager.getInstance().setCurrentFileDoc(currentFile);
                } else {
                    currentFile = WhiteBoradManager.getInstance().getmBlankShareDoc();
                    WhiteBoradManager.getInstance().setCurrentFileDoc(currentFile);
                }
            }
        } else {
            if (WhiteBoradManager.getInstance().getDocList().size() > 1) {
                currentFile = WhiteBoradManager.getInstance().getDocList().get(1);
                WhiteBoradManager.getInstance().setCurrentFileDoc(currentFile);
            } else if (WhiteBoradManager.getInstance().getDocList().size() > 0) {
                currentFile = WhiteBoradManager.getInstance().getDocList().get(0);
                WhiteBoradManager.getInstance().setCurrentFileDoc(currentFile);
            }
        }
    }
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            xWalkView.requestFocus();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
    
    @Override
    public void localChangeDoc() {
        JSONObject jsobj = new JSONObject();
        currentFile = WhiteBoradManager.getInstance().getCurrentFileDoc();
        if (currentFile != null) {
            JSONObject data = Packager.pageSendData(currentFile);
            try {
                jsobj.put("data", data.toString());
                jsobj.put("name", "ShowPage");
                jsobj.put("id", "DocumentFilePage_ShowPage");
                xWalkView.loadUrl("javascript:JsSocket.pubMsg(" + jsobj.toString() + ")");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    
    private JSONObject mapToJson(Map<String, Object> map) {
        JSONObject jsb = new JSONObject(map);
        return jsb;
    }
    
    @Override
    public void pubMsg(String js) {
        try {
            JSONObject jsobj = new JSONObject(js);
            String msgName = jsobj.optString("name");
            String msgId = jsobj.optString("id");
            String toId = jsobj.optString("toID");
            String data = jsobj.optString("data");
            String associatedMsgID = jsobj.optString("associatedMsgID");
            String associatedUserID = jsobj.optString("associatedUserID");
            boolean save = jsobj.optBoolean("do_not_save", false);
            if (jsobj.has("do_not_save")) {
                save = false;
            } else {
                save = true;
            }
            JSONObject jsdata = new JSONObject(data);
            if (msgId.equals("DocumentFilePage_ShowPage")) {
                currentFile = Packager.pageDoc(jsdata);
                //                WhiteBoradManager.getInstance().addDocList(currentFile);
                WhiteBoradManager.getInstance().setCurrentFileDoc(currentFile);
                WhiteBoradManager.getInstance().getDocList();
                //                WhiteBoradManager.getInstance().getAdminDocList();
            }
            //            if (!isClassBegin||(RoomManager.getInstance().getMySelf().role == 2&&msgId.equals("DocumentFilePage_ShowPage"))) {
            //                return;
            //            }
            
            TKRoomManager.getInstance().pubMsg(msgName, msgId, toId, data, save, associatedMsgID, associatedUserID, Tools.toHashMap(jsobj.optString("expandParams")));

            /*if (msgId.equals("AnswerCommit")) {
                TKRoomManager.getInstance().pubMsg(msgName, msgId, toId, data, save, associatedMsgID, associatedUserID,
                        Tools.toHashMap(jsobj.optString("expandParams")));
            } else {
                TKRoomManager.getInstance().pubMsg(msgName, msgId, toId, data, save, associatedMsgID, associatedUserID);
            }*/

            /*int myrole = TKRoomManager.getInstance().getMySelf().role;
            if (isClassBegin && msgId.equals("DocumentFilePage_ShowPage") && (myrole == 0 || (myrole == 2 && candraw))) {
                TKRoomManager.getInstance().pubMsg(msgName, msgId, toId, data, save, associatedMsgID, associatedUserID);
            } else if (isClassBegin && msgId.equals("AnswerCommit")) {
                TKRoomManager.getInstance().pubMsg(msgName, msgId, toId, data, save, associatedMsgID, associatedUserID,
                        Tools.toHashMap(jsobj.optString("expandParams")));
            } else if (isClassBegin && !msgId.equals("DocumentFilePage_ShowPage")) {
                TKRoomManager.getInstance().pubMsg(msgName, msgId, toId, data, save, associatedMsgID, associatedUserID);
            }*/
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void delMsg(String js) {
        try {
            JSONObject jsobj = new JSONObject(js);
            String msgName = jsobj.optString("name");
            String msgId = jsobj.optString("id");
            String toId = jsobj.optString("toID");
            String data = jsobj.optString("data");
            TKRoomManager.getInstance().delMsg(msgName, msgId, toId, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onPageFinished() {
        final JSONObject j = new JSONObject();
        try {
            if (Tools.isTablet(getActivity())) {
                j.put("deviceType", "pad");
            } else {
                j.put("deviceType", "phone");
            }
            j.put("clientType", "android");
            j.put("isSendLogMessage", true);
            j.put("playback", isPlayBack);
            j.put("debugLog", true);//白板debug，h5程序可以做debug用
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    xWalkView.loadUrl("javascript:JsSocket.updateFakeJsSdkInitInfo(" + j.toString() + ")");
                    changeWBUrlAndPort();
                }
            });
        }
        WhiteBoradManager.getInstance().onPageFinished();
        WBSession.getInstance().onCheckedRoom();
        WBSession.getInstance().OnFileListed();
    }
    
    @Override
    public void changePageFullScreen(String isFull) {
        if (!TextUtils.isEmpty(isFull)) {
            try {
                JSONObject object = new JSONObject(isFull);
                boolean full = object.optBoolean("fullScreen");
                WhiteBoradManager.getInstance().fullScreenToLc(full);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void sendJSPageFullScreen(boolean isFull) {
        try {
            JSONObject js = new JSONObject();
            js.put("isFullScreen", isFull);
            if (xWalkView != null) {
                xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'fullScreenChangeCallback'" +
                    "," + js.toString() + ")");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void saveValueByKey(String key, String value) {
        if (editor != null) {
            editor.putString(key, value);
            editor.commit();
        }
    }
    
    @Override
    public void getValueByKey(String key, final int callbackId) {
        if (spkv != null) {
            final String value = spkv.getString(key, "");
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (xWalkView != null) {
                            xWalkView.loadUrl("javascript:JsSocket.JsSocketCallback(" + callbackId + ",'" + value + "')");
                        }
                    }
                });
            }
        }
    }
    
    //白板PPT预加载成功
    @Override
    public void documentPreloadingEnd() {
        WBSession.isdocumentFinish = true;
        WBSession.getInstance().onRoomConnected();
        WBSession.getInstance().onPageFinished();
    }
    
    private void roomDisConnect() {
        if (xWalkView != null) {
            JSONObject js = new JSONObject();
            xWalkView.loadUrl("javascript:JsSocket.disconnect(" + js.toString() + ")");
        }
    }
    
    public void changeWebPageFullScreen(final boolean isFull) {
        WhiteBoradManager.getInstance().fullScreenToLc(isFull);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFull) {
                    xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand (" + "'whiteboardSDK_fullScreen'" + ")");
                } else {
                    xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand (" + "'whiteboardSDK_exitFullScreen'" + ")");
                }
            }
        });
    }
    
    @Override
    public void onJsPlay(String url, boolean isvideo, long fileid) {
        WhiteBoradManager.getInstance().onWBMediaPublish(url, isvideo, fileid);
    }
    
    /***
     * JS调用我们的方法View状态
     */
    @Override
    public void receiveJSActionCommand(String stateJson) {
        WhiteBoradManager.getInstance().onWhiteBoradReceiveActionCommand(stateJson);
    }
    
    /***
     * JS调用我们的方法改变用户属性
     */
    @Override
    public void setProperty(String jsonProperty) {
        WhiteBoradManager.getInstance().onWhiteBoradSetProperty(jsonProperty);
    }
    
    public void changeWBUrlAndPort() {
        JSONObject js = new JSONObject();
        try {
            if (WBSession.host == null) {
                return;
            }
            if (WBSession.host.endsWith("neiwang")) {
                String addres = WBSession.DocServerAddrBackup;
                if (!TextUtils.isEmpty(addres) && addres.length() > 1) {
                    if (addres.substring(0, 1).equals("[") && addres.substring(addres.length() - 1, addres.length()).equals("]")) {
                        js.put("backup_doc_host", addres.substring(1, addres.length() - 1));
                    } else {
                        js.put("backup_doc_host", WBSession.DocServerAddrBackup);
                    }
                }
                js.put("backup_doc_protocol", "http");
                js.put("backup_doc_port", 80);
                
                js.put("doc_protocol", "http");
                js.put("doc_host", WBSession.DocServerAddr);
                js.put("doc_port", 80);
                
                js.put("web_protocol", "http");
                js.put("web_host", WBSession.host);
                js.put("web_port", 80);
                
                JSONArray jsobjs = new JSONArray(WBSession.DocServerAddrBackupList);
                js.put("backup_doc_host_list", jsobjs);
                
            } else {
                if (Config.isWhiteBoardTest || Config.isWhiteVideoBoardTest) {
                    String addres = WBSession.DocServerAddrBackup;
                    if (!TextUtils.isEmpty(addres) && addres.length() > 1) {
                        if (addres.substring(0, 1).equals("[") && addres.substring(addres.length() - 1, addres.length()).equals("]")) {
                            js.put("backup_doc_host", addres.substring(1, addres.length() - 1));
                        } else {
                            js.put("backup_doc_host", WBSession.DocServerAddrBackup);
                        }
                    }
                    js.put("backup_doc_protocol", "http");
                    js.put("backup_doc_port", 80);
                    
                    js.put("doc_protocol", "http");
                    js.put("doc_host", WBSession.DocServerAddr);
                    js.put("doc_port", 80);
                    
                    js.put("web_protocol", "http");
                    js.put("web_host", WBSession.host);
                    js.put("web_port", 80);
                    
                    JSONArray jsobjs = new JSONArray(WBSession.DocServerAddrBackupList);
                    js.put("backup_doc_host_list", jsobjs);
                    
                } else {
                    String addres = WBSession.DocServerAddrBackup;
                    if (!TextUtils.isEmpty(addres) && addres.length() > 1) {
                        if (addres.substring(0, 1).equals("[") && addres.substring(addres.length() - 1, addres.length()).equals("]")) {
                            js.put("backup_doc_host", addres.substring(1, addres.length() - 1));
                        } else {
                            js.put("backup_doc_host", WBSession.DocServerAddrBackup);
                        }
                    }
                    js.put("backup_doc_protocol", "https");
                    js.put("backup_doc_port", 443);
                    
                    js.put("doc_protocol", "https");
                    js.put("doc_host", WBSession.DocServerAddr);
                    js.put("doc_port", 443);
                    
                    js.put("web_protocol", "https");
                    js.put("web_host", WBSession.host);
                    js.put("web_port", 443);
                    
                    
                    JSONArray jsobjs = new JSONArray(WBSession.DocServerAddrBackupList);
                    js.put("backup_doc_host_list", jsobjs);
                }
            }
            xWalkView.loadUrl("javascript:JsSocket.updateWebAddressInfo(" + js.toString() + ")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onStop() {
        super.onStop();
        if (getActivity().isFinishing()) {
            xWalkView.removeAllViews();
            xWalkView.onDestroy();
            xWalkView = null;
        }
    }
    
    protected void closeNewPptVideo() {
        if (xWalkView != null) {
            xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'closeDynamicPptWebPlay'" + ")");
        }
    }
    
    private void roomPlaybackClearAll() {
        JSONObject js = new JSONObject();
        xWalkView.loadUrl("javascript:JsSocket.playback_clearAll(" + js.toString() + ")");
    }
    
    public void userleft(String userid) {
        JSONObject js = new JSONObject();
        try {
            js.put("userid", userid);
            xWalkView.loadUrl("javascript:JsSocket.participantLeft(" + js.toString() + ")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public void userChange(RoomUser user, Map<String, Object> changedProperties, String fromId) {
        JSONObject js = new JSONObject();
        try {
            JSONObject jsmsg = new JSONObject();
            jsmsg.put("userid", user.peerId);
            JSONObject jsmap = mapToJson(changedProperties);
            jsmsg.put("properties", jsmap);
            jsmsg.put("fromID", fromId);
            js.put("message", jsmsg);
            xWalkView.loadUrl("javascript:JsSocket.setProperty(" + jsmsg.toString() + ")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void playbackPlayAndPauseController(boolean isplay) {
        if (xWalkView != null) {
            try {
                JSONObject jsmsg = new JSONObject();
                jsmsg.put("play", isplay);
                xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'playbackPlayAndPauseController'" + "," + jsmsg.toString() + ")");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void transmitWindowSize(int wid, int hid) {
        JSONObject js = new JSONObject();
        try {
            JSONObject jsmsg = new JSONObject();
            jsmsg.put("windowWidth", wid);
            jsmsg.put("windowHeight", hid);
            if (xWalkView != null) {
                xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'whiteboardSDK_updateWhiteboardSize'" + "," + js.toString() + ")");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    /***
     *  跟JS交互
     */
    public void interactiveJS(String name, String json) {
        if (xWalkView != null) {
            if (TextUtils.isEmpty(json)) {
                xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + name + ")");
            } else {
                xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + name + "," + json.toString() + ")");
            }
        }
    }
    
    /***
     *  跟JS交互 翻页  b  ture 代表右翻页   false 左翻页
     */
    public void interactiveJSPaging(ShareDoc shareDoc, boolean b, boolean nextOrAdd) {
        
        if (shareDoc.getFileid() == 0) {  // 白板加页
            if (b) {
                if (xWalkView != null) {
                    if (nextOrAdd) {
                        xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'whiteboardSDK_nextPage'" + ")");
                    } else {
                        xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'whiteboardSDK_addPage'" + ")");
                    }
                }
            } else {
                xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'whiteboardSDK_prevPage'" + ")");
            }
        } else {
            if (shareDoc.isDynamicPPT()) {  //  PPT文档
                if (b) {
                    if (xWalkView != null) {
                        xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'whiteboardSDK_nextStep'" + ")");
                    }
                } else {
                    if (xWalkView != null) {
                        xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'whiteboardSDK_prevStep'" + ")");
                    }
                }
            } else {    // H5和普通文档
                if (b) {
                    if (xWalkView != null) {
                        xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'whiteboardSDK_nextPage'" + ")");
                    }
                } else {
                    if (xWalkView != null) {
                        xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'whiteboardSDK_prevPage'" + ")");
                    }
                }
            }
        }
    }
    
    /***
     *
     * @param number   文档指定放到哪一页
     */
    protected void interactiveJSSelectPage(int number) {
        JSONObject js = new JSONObject();
        try {
            js.put("number", number);
            if (xWalkView != null) {
                xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'whiteboardSDK_skipPage'"
                    + "," + js.toString() + ")");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        spkv = context.getSharedPreferences("dataphone", MODE_PRIVATE);
        editor = spkv.edit();
        NotificationCenter.getInstance().addObserver(this, WBSession.onCheckRoom);
        NotificationCenter.getInstance().addObserver(this, WBSession.onFileList);
        NotificationCenter.getInstance().addObserver(this, WBSession.onRemoteMsg);
        NotificationCenter.getInstance().addObserver(this, WBSession.onRoomConnected);
        NotificationCenter.getInstance().addObserver(this, WBSession.onUserJoined);
        NotificationCenter.getInstance().addObserver(this, WBSession.onUserLeft);
        NotificationCenter.getInstance().addObserver(this, WBSession.onUserChanged);
        NotificationCenter.getInstance().addObserver(this, WBSession.onRoomLeaved);
        NotificationCenter.getInstance().addObserver(this, WBSession.onPlayBackClearAll);
        NotificationCenter.getInstance().addObserver(this, WBSession.onRoomConnectFaild);
        NotificationCenter.getInstance().addObserver(this, WBSession.participantEvicted);
        NotificationCenter.getInstance().addObserver(this, WBSession.duration);
        NotificationCenter.getInstance().addObserver(this, WBSession.playbackEnd);
        NotificationCenter.getInstance().addObserver(this, WBSession.playback_updatetime);
        NotificationCenter.getInstance().addObserver(this, WBSession.participantPublished);
        NotificationCenter.getInstance().addObserver(this, WBSession.onPlayBackRoomJson);
    }
    
    @Override
    public void didReceivedNotification(final int id, final Object... args) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (id) {
                    case WBSession.onCheckRoom:
                        JSONObject jsonObjectCheckRoom = (JSONObject) args[0];
                        onCheckRoom(jsonObjectCheckRoom);
                        break;
                    
                    case WBSession.onPlayBackRoomJson:
                        int code = (int) args[0];
                        String response = (String) args[1];
                        onPlayBackRoomJson(code, response);
                        break;
                    
                    case WBSession.onFileList:
                        onFileList();
                        break;
                    
                    case WBSession.onRemoteMsg:
                        if (args != null) {
                            boolean addRemoteMsg = (boolean) args[0];
                            String idRemoteMsg = (String) args[1];
                            String nameRemoteMsg = (String) args[2];
                            long tsRemoteMsg = (long) args[3];
                            Object dataRemoteMsg = (Object) args[4];
                            boolean inList = (boolean) args[5];
                            String fromIDRemoteMsg = (String) args[6];
                            String associatedMsgIDRemoteMsg = (String) args[7];
                            String associatedUserIDRemoteMsg = (String) args[8];
                            JSONObject jsonObjectRemoteMsg = (JSONObject) args[9];
                            onRemoteMsg(addRemoteMsg, idRemoteMsg, nameRemoteMsg, tsRemoteMsg, dataRemoteMsg, inList, fromIDRemoteMsg,
                                associatedMsgIDRemoteMsg, associatedUserIDRemoteMsg, jsonObjectRemoteMsg);
                        }
                        break;
                    
                    case WBSession.onRoomConnected:
                        JSONArray jsonArrayRoomConnected = (JSONArray) args[0];
                        List listRoomConnected = (List) args[1];
                        JSONObject jsonObjectRoomConnected = (JSONObject) args[2];
                        onRoomConnected(jsonArrayRoomConnected, listRoomConnected, jsonObjectRoomConnected);
                        break;
                    
                    case WBSession.onUserJoined:
                        RoomUser roomUserJoined = (RoomUser) args[0];
                        boolean inListUserJoined = (boolean) args[1];
                        JSONObject jsonObjectUserJoined = (JSONObject) args[2];
                        onUserJoined(roomUserJoined, inListUserJoined, jsonObjectUserJoined);
                        break;
                    
                    case WBSession.onUserLeft:
                        RoomUser roomUserLeft = (RoomUser) args[0];
                        String peeridUserLeft = (String) args[1];
                        onUserLeft(roomUserLeft, peeridUserLeft);
                        break;
                    
                    case WBSession.onUserChanged:
                        RoomUser roomUserChanged = (RoomUser) args[0];
                        Map<String, Object> mapUserChanged = (Map<String, Object>) args[1];
                        String sUserChanged = (String) args[2];
                        JSONObject jsonObjectUserChanged = (JSONObject) args[3];
                        onUserChanged(roomUserChanged, mapUserChanged, sUserChanged, jsonObjectUserChanged);
                        break;
                    
                    case WBSession.onRoomLeaved:
                        onRoomLeaved();
                        break;
                    
                    case WBSession.onPlayBackClearAll:
                        onPlayBackClearAll();
                        break;
                    
                    case WBSession.onRoomConnectFaild:
                        onRoomConnectFaild();
                        break;
                    
                    case WBSession.participantEvicted:
                        JSONObject participant = (JSONObject) args[0];
                        participantEvicted(participant);
                        break;
                    
                    case WBSession.duration:
                        JSONObject durationJSONO = (JSONObject) args[0];
                        duration(durationJSONO);
                        break;
                    
                    case WBSession.playbackEnd:
                        playbackEnd();
                        break;
                    
                    case WBSession.playback_updatetime:
                        JSONObject playback_updatetimeJSONO = (JSONObject) args[0];
                        playback_updatetime(playback_updatetimeJSONO);
                        break;
                    
                    case WBSession.participantPublished:
                        JSONObject participantJSONO = (JSONObject) args[0];
                        participantPublished(participantJSONO);
                        break;
                }
            }
        });
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
    
    private void onFileList() {
        if (getActivity() != null && WBSession.isPageFinish) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsobj = new JSONObject();
                        ShareDoc shareDoc = WhiteBoradManager.getInstance().getDefaultFileDoc();
                        if (shareDoc != null) {
                            JSONObject data = Packager.pageSendData(shareDoc);
                            jsobj.put("cmd", data.toString());
                        } else {
                            jsobj.put("cmd", "");
                        }
                        if (xWalkView != null) {
                            xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'preLoadingFile'" +
                                "," + jsobj.toString() + ")");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    
    private void onPlayBackRoomJson(int code, String response) {
        if (xWalkView != null) {
            if (code == 0) {
                xWalkView.loadUrl("javascript:JsSocket.checkroom(" + response.toString() + ")");
            }
            
        }
    }
    
    private void participantPublished(JSONObject participantJSONO) {
        if (xWalkView != null) {
            xWalkView.loadUrl("javascript:JsSocket.participantPublished(" + participantJSONO.toString() + ")");
        }
    }
    
    private void playback_updatetime(JSONObject playback_updatetimeJSONO) {
        if (xWalkView != null) {
            xWalkView.loadUrl("javascript:JsSocket.playbackEnd(" + playback_updatetimeJSONO.toString() + ")");
        }
    }
    
    private void playbackEnd() {
        if (xWalkView != null) {
            xWalkView.loadUrl("javascript:JsSocket.playbackEnd(" + ")");
        }
    }
    
    private void duration(JSONObject durationJSONO) {
        if (xWalkView != null && durationJSONO != null) {
            xWalkView.loadUrl("javascript:JsSocket.duration(" + durationJSONO.toString() + ")");
        }
    }
    
    private void participantEvicted(JSONObject participant) {
        if (xWalkView != null && participant != null) {
            xWalkView.loadUrl("javascript:JsSocket.participantEvicted(" + participant.toString() + ")");
        }
    }
    
    private void onRoomConnectFaild() {
        roomDisConnect();
    }
    
    private void onPlayBackClearAll() {
        roomPlaybackClearAll();
    }
    
    private void onRoomLeaved() {
        if (xWalkView != null) {
            xWalkView.loadUrl("javascript:JsSocket.leaveroom(" + ")");
        }
    }
    
    private void onUserChanged(RoomUser roomUser, Map<String, Object> map, String s, JSONObject jsonObject) {
        if (xWalkView != null && jsonObject != null) {
            xWalkView.loadUrl("javascript:JsSocket.setProperty(" + jsonObject.toString() + ")");
        }
        if (roomUser.peerId.equals(TKRoomManager.getInstance().getMySelf().peerId) && map.containsKey("candraw")) {
            boolean candraw = Tools.isTure(map.get("candraw"));
            setDrawable(candraw);
        }
    }
    
    private void onUserLeft(RoomUser roomUser, final String peeridUserLeft) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (xWalkView != null) {
                    xWalkView.loadUrl("javascript:JsSocket.participantLeft(" + "'" + peeridUserLeft + "'" + ")");
                }
            }
        });
    }
    
    private void onUserJoined(RoomUser roomUser, boolean inList, final JSONObject jsonObject) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (xWalkView != null) {
                    xWalkView.loadUrl("javascript:JsSocket.participantJoined(" + jsonObject.toString() + ")");
                }
            }
        });
    }
    
    private void onRoomConnected(JSONArray jsonArray, List list, JSONObject jsonObject) {
        
        changeWBUrlAndPort();
        
        JSONObject jsdata = new JSONObject();
        for (int i = 0; i < list.size(); i++) {
            JSONObject js = mapToJson((Map<String, Object>) list.get(i));
            String id = js.optString("id");
            Object data = js.opt("data");
            try {
                if (!js.optString("associatedMsgID", "").equals("VideoWhiteboard")) {
                    jsdata.put(id, js);
                }
                if ("ClassBegin".equals(js.optString("name"))) {
                    isClassBegin = true;
                    JSWhitePadInterface.isClassbegin = true;
                } else if (id.equals("DocumentFilePage_ShowPage")) {
                    currentFile = WhiteBoradManager.getInstance().getCurrentFileDoc();
                    JSONObject jsmdata = null;
                    if (data instanceof JSONObject) {
                        jsmdata = (JSONObject) data;
                    } else if (data instanceof String) {
                        String strdata = (String) data;
                        jsmdata = new JSONObject(strdata);
                    }
                    currentFile = Packager.pageDoc(jsmdata);
                    WhiteBoradManager.getInstance().setCurrentFileDoc(currentFile);
                    WhiteBoradManager.getInstance().getDocList();

                   /* WhiteBoradManager.getInstance().getAdminDocList();*/
                } else if (id.equals("WBPageCount")) {
                    JSONObject jsmdata = null;
                    if (data instanceof JSONObject) {
                        jsmdata = (JSONObject) data;
                    } else if (data instanceof String) {
                        String strdata = (String) data;
                        jsmdata = new JSONObject(strdata);
                    }
                    WhiteBoradManager.getInstance().getmBlankShareDoc().setPagenum(jsmdata.optInt("totalPage"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            JSONObject myselfProperties = Packager.myPropertie(TKRoomManager.getInstance().getMySelf().toJson());
            hashMap.put("properties", myselfProperties);
            hashMap.put("id", TKRoomManager.getInstance().getMySelf().peerId);
            jsonObject.put("myself", mapToJson(hashMap));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        xWalkView.loadUrl("javascript:JsSocket.roomConnected(" + 0 + "," + jsonObject.toString() + ")");
        
        if (!jsdata.has("DocumentFilePage_ShowPage")) {
            if (WhiteBoradManager.getInstance().getDefaultFileDoc() == null) {
                if (WhiteBoradManager.getInstance().getDocList().size() > 1) {
                    currentFile = WhiteBoradManager.getInstance().getDocList().get(1);
                } else {
                    if (WhiteBoradManager.getInstance().getDocList().size() > 0) {
                        currentFile = WhiteBoradManager.getInstance().getDocList().get(0);
                    }
                }
            } else {
                currentFile = WhiteBoradManager.getInstance().getDefaultFileDoc();
            }
            WhiteBoradManager.getInstance().setCurrentFileDoc(currentFile);
            if (currentFile != null) {
                final JSONObject jsobj = new JSONObject();
                JSONObject data = Packager.pageSendData(currentFile);
                try {
                    jsobj.put("data", data.toString());
                    jsobj.put("name", "ShowPage");
                    jsobj.put("id", "DocumentFilePage_ShowPage");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            xWalkView.loadUrl("javascript:JsSocket.pubMsg(" + jsobj.toString() + ")");
                        }
                    });
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
        TKRoomManager.getInstance().pubMsg("UpdateTime", "UpdateTime", TKRoomManager.getInstance().getMySelf().peerId,
            null, false, null, null);
    }
    
    private void onRemoteMsg(final boolean add, String id, String name, long ts, Object data, boolean inList, String fromID, String associatedMsgID, String associatedUserID, final JSONObject jsonObject) {
        if (add) {
            if (name.equals("ClassBegin")) {
                isClassBegin = true;
                JSWhitePadInterface.isClassbegin = true;
                
                if (TKRoomManager.getInstance().getMySelf().role == 0) {
                    if (RoomControler.isDocumentClassification()) {
                        WhiteBoradManager.getInstance().getClassDocList();
                        WhiteBoradManager.getInstance().getAdminDocList();
                        WhiteBoradManager.getInstance().getClassMediaList();
                        WhiteBoradManager.getInstance().getAdminmMediaList();
                    }
                    
                    if (inList) {
                        if (!RoomControler.isAutoClassBegin()) {
                            if (RoomControler.isNotLeaveAfterClass()) {
                                WhiteBoradManager.getInstance().resumeFileList();
                                currentFile = WhiteBoradManager.getInstance().getCurrentFileDoc();
                            }
                            
                            if (currentFile != null) {
                                JSONObject jsdata = Packager.pageSendData(currentFile);
                                TKRoomManager.getInstance().pubMsg("ShowPage", "DocumentFilePage_ShowPage", "__all", jsdata.toString(), true, null, null);
                            }
                        }
                    }
                    
                }
            } else if (id.equals("DocumentFilePage_ShowPage")) {
                currentFile = WhiteBoradManager.getInstance().getCurrentFileDoc();
                String strdata = null;
                if (data instanceof String) {
                    strdata = (String) data;
                } else if (data instanceof Map) {
                    strdata = new JSONObject((Map) data).toString();
                }
                try {
                    JSONObject jsmdata = new JSONObject(strdata);
                    currentFile = Packager.pageDoc(jsmdata);
                    WhiteBoradManager.getInstance().setCurrentFileDoc(currentFile);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (name.equals("DocumentChange")) {

                /*WhiteBoradManager.getInstance().getAdminDocList();
                WhiteBoradManager.getInstance().getAdminmMediaList();
                WhiteBoradManager.getInstance().getClassDocList();
                WhiteBoradManager.getInstance().getClassMediaList();*/
                
                String strdata = null;
                if (data instanceof String) {
                    strdata = (String) data;
                } else if (data instanceof Map) {
                    strdata = new JSONObject((Map) data).toString();
                }
                ShareDoc doc = new ShareDoc();
                try {
                    JSONObject jsmdata = new JSONObject(strdata);
                    boolean isdel = Tools.isTure(jsmdata.get("isDel"));
                    doc = Packager.pageDoc(jsmdata);
                    if (!isClassBegin && doc.getFileid() == WhiteBoradConfig.getsInstance().getCurrentMediaDoc().getFileid()) {
                        TKRoomManager.getInstance().stopShareMedia();
                    }
                    WhiteBoradManager.getInstance().onRoomFileChange(doc, isdel, false, isClassBegin);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (name.equals("ClassBegin")) {
                isClassBegin = false;
                JSWhitePadInterface.isClassbegin = false;
                //                clearLcAllData();
                if (!RoomControler.isNotLeaveAfterClass()) {
                    WhiteBoradManager.getInstance().resumeFileList();
                    if (WhiteBoradManager.getInstance().getDefaultFileDoc() == null) {
                       /* setCurrentFile();*/
                        if (WhiteBoradManager.getInstance().getDocList().size() > 1) {
                            currentFile = WhiteBoradManager.getInstance().getDocList().get(1);
                        } else {
                            if (WhiteBoradManager.getInstance().getDocList().size() > 0) {
                                currentFile = WhiteBoradManager.getInstance().getDocList().get(0);
                            } else {
                                currentFile = WhiteBoradManager.getInstance().getmBlankShareDoc();
                            }
                        }
                    } else {
                        currentFile = WhiteBoradManager.getInstance().getDefaultFileDoc();
                    }
                    WhiteBoradManager.getInstance().setCurrentFileDoc(currentFile);
                    
                    final JSONObject jsobj = new JSONObject();
                    JSONObject resumedasta = Packager.pageSendData(currentFile);
                    try {
                        jsobj.put("data", resumedasta.toString());
                        jsobj.put("name", "ShowPage");
                        jsobj.put("id", "DocumentFilePage_ShowPage");
                        if (getActivity() != null && xWalkView != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    xWalkView.loadUrl("javascript:JsSocket.pubMsg(" + jsobj.toString() + ")");
                                }
                            });
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        if (getActivity() != null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (jsonObject != null) {
                    if (add) {
                        xWalkView.loadUrl("javascript:JsSocket.pubMsg(" + jsonObject.toString() + ")");
                    } else {
                        xWalkView.loadUrl("javascript:JsSocket.delMsg(" + jsonObject.toString() + ")");
                    }
                }
                
            }
        });



      /*  JSONObject jsobj = new JSONObject();
        try {
            jsobj.put("id", id);
            jsobj.put("ts", ts);
            jsobj.put("data", data == null ? null : data.toString());
            jsobj.put("name", name);
            jsobj.put("fromID", fromID);
            jsobj.put("serverData", jsonObject.toString());

            if (!associatedMsgID.equals("")) {
                jsobj.put("associatedMsgID", associatedMsgID);
            }
            if (!associatedUserID.equals("")) {
                jsobj.put("associatedUserID", associatedUserID);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }
    
    private void onCheckRoom(final JSONObject jsonObjectCheckRoom) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (xWalkView != null) {
                    xWalkView.loadUrl("javascript:JsSocket.checkroom(" + jsonObjectCheckRoom.toString() + ")");
                }
            }
        });
    }
}
