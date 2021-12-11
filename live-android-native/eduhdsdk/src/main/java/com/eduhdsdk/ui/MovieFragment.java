package com.eduhdsdk.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.classroomsdk.Config;
import com.classroomsdk.NotificationCenter;
import com.classroomsdk.Tools;
import com.classroomsdk.WBSession;
import com.eduhdsdk.R;
import com.eduhdsdk.message.JSVideoWhitePadInterface;
import com.eduhdsdk.message.RoomControler;
import com.eduhdsdk.message.RoomSession;
import com.eduhdsdk.message.VideoWBCallback;
import com.talkcloud.room.RoomUser;
import com.talkcloud.room.TKRoomManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tkwebrtc.EglBase;
import org.tkwebrtc.EglRenderer;
import org.tkwebrtc.RendererCommon;
import org.tkwebrtc.SurfaceViewRenderer;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2018/2/27/027.
 */

public class MovieFragment extends Fragment implements VideoWBCallback, NotificationCenter.NotificationCenterDelegate {
    
    static private MovieFragment mInstance = null;
    private View fragmentView;
    LinearLayout re_video_play;
    SurfaceViewRenderer suf_mp4;
    private String peerIdShareFile;
    private EglRenderer.FrameListener frameListener;
    private RelativeLayout re_laoding;
    private ImageView loadingImageView;
    private XWalkView xWalkView;
    private SharedPreferences spkv = null;
    private SharedPreferences.Editor editor = null;
    //白板全屏右下角视频界面
    private RelativeLayout rel_fullscreen_mp4videoitem;
    private SurfaceViewRenderer fullscreen_sf_video;
    private ImageView fullscreen_bg_video_back, fullscreen_img_video_back;
    private int videoWidth;
    private int videoHeight;
    private int videoMarginRight;
    private int videoMarginBottom;
    private boolean isZoom;
    private boolean isOneToOne;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    public void setShareFilePeerId(String peerIdShareFile) {
        this.peerIdShareFile = peerIdShareFile;
        TKRoomManager.getInstance().playFile(peerIdShareFile, suf_mp4);
    }
    
    static public MovieFragment getInstance() {
        synchronized (MovieFragment.class) {
            if (mInstance == null) {
                mInstance = new MovieFragment();
            }
            return mInstance;
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_movie, null);
            
            if (getActivity() instanceof OneToManyThreeActivity) {
                isOneToOne = ((OneToManyThreeActivity) getActivity()).getIsOneToOne();
                isZoom = ((OneToManyThreeActivity) getActivity()).getZoom();
                videoWidth = ((OneToManyThreeActivity) getActivity()).getFullscreen_video_param().width;
                videoHeight = ((OneToManyThreeActivity) getActivity()).getFullscreen_video_param().height;
                videoMarginRight = ((OneToManyThreeActivity) getActivity()).getFullscreen_video_param().rightMargin;
                videoMarginBottom = ((OneToManyThreeActivity) getActivity()).getFullscreen_video_param().bottomMargin;
            } else if (getActivity() instanceof OneToOneThreeActivity) {
                isOneToOne = ((OneToOneThreeActivity) getActivity()).getIsOneToOne();
                isZoom = ((OneToOneThreeActivity) getActivity()).getZoom();
                videoWidth = ((OneToOneThreeActivity) getActivity()).getFullscreen_video_param().width;
                videoHeight = ((OneToOneThreeActivity) getActivity()).getFullscreen_video_param().height;
                videoMarginRight = ((OneToOneThreeActivity) getActivity()).getFullscreen_video_param().rightMargin;
                videoMarginBottom = ((OneToOneThreeActivity) getActivity()).getFullscreen_video_param().bottomMargin;
            }
            
            fragmentView.bringToFront();
            re_video_play = (LinearLayout) fragmentView.findViewById(R.id.re_video_play);
            suf_mp4 = (SurfaceViewRenderer) fragmentView.findViewById(R.id.suf_mp4);
            suf_mp4.init(EglBase.create().getEglBaseContext(), null);
            suf_mp4.setZOrderMediaOverlay(true);
            
            re_laoding = (RelativeLayout) fragmentView.findViewById(R.id.re_laoding);
            loadingImageView = (ImageView) fragmentView.findViewById(R.id.loadingImageView);
            
            //白板全屏右下角视频界面
            rel_fullscreen_mp4videoitem = (RelativeLayout) fragmentView.findViewById(R.id.rel_fullscreen_mp4videoitem);
            fullscreen_sf_video = (SurfaceViewRenderer) rel_fullscreen_mp4videoitem.findViewById(R.id.fullscreen_sf_video);
            fullscreen_sf_video.init(EglBase.create().getEglBaseContext(), null);
            fullscreen_bg_video_back = (ImageView) rel_fullscreen_mp4videoitem.findViewById(R.id.fullscreen_bg_video_back);
            fullscreen_img_video_back = (ImageView) rel_fullscreen_mp4videoitem.findViewById(R.id.fullscreen_img_video_back);
            
            xWalkView = (XWalkView) fragmentView.findViewById(R.id.movie_white_board);
            XWalkPreferences.setValue("enable-javascript", true);
            XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
            XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
            XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
            XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);
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
            JSVideoWhitePadInterface.getInstance().setVideoWBCallBack(this);
            xWalkView.addJavascriptInterface(JSVideoWhitePadInterface.getInstance(), "JSVideoWhitePadInterface");
            xWalkView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            xWalkView.setBackgroundColor(0);
            
            xWalkView.setResourceClient(new XWalkResourceClient(xWalkView) {
                @Override
                public void onReceivedSslError(XWalkView view, ValueCallback<Boolean> callback, SslError error) {
                    callback.onReceiveValue(true);
                }
            });
            
            if (RoomSession.isShowVideoWB) {
                xWalkView.setVisibility(View.VISIBLE);
                xWalkView.setZOrderOnTop(true);
            } else {
                xWalkView.setVisibility(View.INVISIBLE);
            }
            
            if (Config.isWhiteMovieBoardTest) {
                //                xWalkView.loadUrl("http://192.168.1.108:8444/publish/index.html#/mobileApp?loadComponentName=" + "localFileVideoDrawWhiteboardComponent");//李
                //                xWalkView.loadUrl("http://192.168.1.251:9251/publish/index.html#/mobileApp?loadComponentName=" + "localFileVideoDrawWhiteboardComponent");//建行
                //                xWalkView.loadUrl("http://192.168.1.182:1314/publish/index.html#/mobileApp_videoWhiteboard?videoDrawingBoardType=" + "fileVideo");//广生
                xWalkView.loadUrl("http://192.168.1.64:8585/publish/index.html#/mobileApp?loadComponentName=" + "localFileVideoDrawWhiteboardComponent");
            } else {
                xWalkView.loadUrl("file:///android_asset/react_mobile_new_publishdir/index.html#/mobileApp?loadComponentName=" + "localFileVideoDrawWhiteboardComponent");
            }
        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }
        //白板全屏右下角界面大小
        RelativeLayout.LayoutParams fullscreen_video_param = new RelativeLayout.LayoutParams(0, 0);
        fullscreen_video_param.width = videoWidth;
        fullscreen_video_param.height = videoHeight;
        fullscreen_video_param.rightMargin = videoMarginRight;
        fullscreen_video_param.bottomMargin = videoMarginBottom;
        fullscreen_sf_video.setLayoutParams(fullscreen_video_param);
        fullscreen_bg_video_back.setLayoutParams(fullscreen_video_param);
        fullscreen_img_video_back.setLayoutParams(fullscreen_video_param);
        return fragmentView;
    }
    
    public void setFullscreenHide() {
        if (rel_fullscreen_mp4videoitem != null) {
            rel_fullscreen_mp4videoitem.setVisibility(View.GONE);
            fullscreen_sf_video.setVisibility(View.INVISIBLE);
            fullscreen_img_video_back.setVisibility(View.GONE);
            fullscreen_bg_video_back.setVisibility(View.GONE);
        }
    }
    
    public void setFullscreenShow(String peerId) {
        if (fullscreen_sf_video != null) {
            rel_fullscreen_mp4videoitem.setVisibility(View.VISIBLE);
            fullscreen_sf_video.setVisibility(View.VISIBLE);
            fullscreen_sf_video.setZOrderOnTop(true);
            /*fullscreen_sf_video.setZOrderMediaOverlay(true);*/
            fullscreen_img_video_back.setVisibility(View.INVISIBLE);
            fullscreen_bg_video_back.setVisibility(View.INVISIBLE);
            TKRoomManager.getInstance().playVideo(peerId, fullscreen_sf_video, RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        re_laoding.setVisibility(View.VISIBLE);
        Glide.with(getActivity())
            .asGif()
            .load(R.drawable.loading)
            .into(loadingImageView);
        
        if (peerIdShareFile != null) {
            suf_mp4.setZOrderOnTop(true);
            suf_mp4.setZOrderMediaOverlay(true);
            suf_mp4.setEnableHardwareScaler(true);
            suf_mp4.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
            TKRoomManager.getInstance().playFile(peerIdShareFile, suf_mp4);
            suf_mp4.requestLayout();
        }
        
        frameListener = new EglRenderer.FrameListener() {
            @Override
            public void onFrame(final Bitmap bitmap) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        re_laoding.setVisibility(View.GONE);
                        setSync();
                    }
                });
            }
        };
        suf_mp4.addFrameListener(frameListener, 0);
    }
    
    public void setSync() {
        if (isZoom && RoomControler.isFullScreenVideo()) {
            //显示全屏同步画中画
            rel_fullscreen_mp4videoitem.setVisibility(View.VISIBLE);
            fullscreen_sf_video.setZOrderMediaOverlay(true);
            fullscreen_sf_video.setZOrderOnTop(true);
            fullscreen_sf_video.setVisibility(View.VISIBLE);
            if (isOneToOne) {
                RoomUser user = null;
                if (TKRoomManager.getInstance().getMySelf().role == 0) {
                    for (int i = 0; i < RoomSession.playingList.size(); i++) {
                        if (2 == RoomSession.playingList.get(i).role) {
                            user = RoomSession.playingList.get(i);
                        }
                    }
                    if (user != null) {
                        if (user.getPublishState() > 1 && user.getPublishState() < 4 && !user.disablevideo) {
                            if (user.hasVideo) {
                                fullscreen_sf_video.setVisibility(View.VISIBLE);
                                fullscreen_bg_video_back.setVisibility(View.GONE);
                                fullscreen_img_video_back.setVisibility(View.GONE);
                                TKRoomManager.getInstance().playVideo(user.peerId, fullscreen_sf_video,
                                    RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                            } else {
                                fullscreen_sf_video.setVisibility(View.INVISIBLE);
                                fullscreen_img_video_back.setImageResource(R.drawable.icon_student_one_to_one);
                                fullscreen_img_video_back.setVisibility(View.VISIBLE);
                                fullscreen_bg_video_back.setVisibility(View.VISIBLE);
                            }
                        } else {
                            fullscreen_sf_video.setVisibility(View.INVISIBLE);
                            fullscreen_img_video_back.setImageResource(R.drawable.icon_student_one_to_one);
                            fullscreen_img_video_back.setVisibility(View.VISIBLE);
                            fullscreen_bg_video_back.setVisibility(View.VISIBLE);
                        }
                    } else {
                        fullscreen_sf_video.setVisibility(View.INVISIBLE);
                        fullscreen_img_video_back.setImageResource(R.drawable.icon_student_one_to_one);
                        fullscreen_img_video_back.setVisibility(View.VISIBLE);
                        fullscreen_bg_video_back.setVisibility(View.VISIBLE);
                    }
                } else {
                    for (int i = 0; i < RoomSession.playingList.size(); i++) {
                        if (RoomSession.playingList.get(i).role == 0) {
                            TKRoomManager.getInstance().playVideo(RoomSession.playingList.get(i).peerId, fullscreen_sf_video,
                                RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                        }
                    }
                }
            } else {
                for (int i = 0; i < RoomSession.playingList.size(); i++) {
                    if (RoomSession.playingList.get(i).role == 0) {
                        TKRoomManager.getInstance().playVideo(RoomSession.playingList.get(i).peerId, fullscreen_sf_video,
                            RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public void onStop() {
        if (xWalkView != null) {
            xWalkView.onHide();
        }
        if (suf_mp4 != null && frameListener != null) {
            suf_mp4.removeFrameListener(frameListener);
            frameListener = null;
        }
        
        re_laoding.setVisibility(View.GONE);
        super.onStop();
    }
    
    @Override
    public void onDestroyView() {
        RoomSession.jsVideoWBTempMsg = new JSONArray();
        suf_mp4.release();
        suf_mp4 = null;
        if (xWalkView != null) {
            xWalkView.removeAllViews();
            xWalkView.onDestroy();
            xWalkView = null;
        }
        mInstance = null;
        super.onDestroyView();
    }
    
    private boolean onRemoteMsg(boolean add, String id, String name, long ts, Object data, String fromID, String associatedMsgID, String associatedUserID, JSONObject jsonObjectRemoteMsg) {
        if (add) {
            if (name.equals("VideoWhiteboard")) {
                if (xWalkView != null) {
                    if (suf_mp4 != null) {
                        suf_mp4.setZOrderMediaOverlay(false);
                        suf_mp4.setZOrderOnTop(false);
                    }
                    xWalkView.setVisibility(View.VISIBLE);
                    xWalkView.setZOrderOnTop(true);
                }
            }
        } else {
            if (name.equals("VideoWhiteboard")) {
                RoomSession.jsVideoWBTempMsg = new JSONArray();
                if (xWalkView != null) {
                    if (suf_mp4 != null) {
                        suf_mp4.setZOrderMediaOverlay(true);
                    }
                   /* xWalkView.setVisibility(View.INVISIBLE);*/
                    /*xWalkView.setZOrderOnTop(false);*/
                }
            }
        }
        
        JSONObject jsobj = new JSONObject();
        try {
            jsobj.put("id", id);
            jsobj.put("ts", ts);
            jsobj.put("data", data == null ? null : data.toString());
            jsobj.put("name", name);
            jsobj.put("fromID", fromID);
            if (!associatedMsgID.equals("")) {
                jsobj.put("associatedMsgID", associatedMsgID);
            }
            if (!associatedUserID.equals("")) {
                jsobj.put("associatedUserID", associatedUserID);
            }
            if (add) {
                if (associatedMsgID.equals("VideoWhiteboard") || id.equals("VideoWhiteboard")) {
                    xWalkView.loadUrl("javascript:JsSocket.pubMsg(" + jsobj.toString() + ")");
                }
            } else {
                if (associatedMsgID.equals("VideoWhiteboard") || id.equals("VideoWhiteboard")) {
                    xWalkView.loadUrl("javascript:JsSocket.delMsg(" + jsobj.toString() + ")");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        
        spkv = context.getSharedPreferences("dataphone", MODE_PRIVATE);
        editor = spkv.edit();
        
        NotificationCenter.getInstance().addObserver(this, WBSession.onRemoteMsg);
        NotificationCenter.getInstance().addObserver(this, WBSession.onRoomLeaved);
        NotificationCenter.getInstance().addObserver(this, WBSession.onPlayBackClearAll);
        NotificationCenter.getInstance().addObserver(this, WBSession.onRoomConnectFaild);
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        NotificationCenter.getInstance().removeObserver(this);
    }
    
    private void roomPlaybackClearAll() {
        JSONObject js = new JSONObject();
        xWalkView.loadUrl("javascript:JsSocket.playback_clearAll(" + js.toString() + ")");
    }
    
    private void roomDisConnect() {
        if (xWalkView != null) {
            JSONObject js = new JSONObject();
            xWalkView.loadUrl("javascript:JsSocket.disconnect(" + js.toString() + ")");
        }
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
            TKRoomManager.getInstance().pubMsg(msgName, msgId, toId, data, save, associatedMsgID, associatedUserID);
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
            //            Map<String,Object> datamap = new HashMap<String,Object>();
            //            if(data!=null){
            //                datamap =  toMap(data);
            //            }
            TKRoomManager.getInstance().delMsg(msgName, msgId, toId, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onPageFinished() {
        final JSONObject j = new JSONObject();
        try {
            j.put("isSendLogMessage", true);
            j.put("debugLog", true);
            j.put("myself", TKRoomManager.getInstance().getMySelf().toJson());
            /*j.put("_tplId", TKRoomManager.getInstance().get_tplId());
            j.put("_skinId", TKRoomManager.getInstance().get_skinId());
            j.put("_skinResource", TKRoomManager.getInstance().get_skinResource());*/
            if (Tools.isTablet(getActivity())) {
                j.put("deviceType", "pad");
            } else {
                j.put("deviceType", "phone");
            }
            j.put("clientType", "android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    xWalkView.loadUrl("javascript:JsSocket.updateFakeJsSdkInitInfo('" + j.toString() + "')");
                }
            });
        }
        if (RoomSession.jsVideoWBTempMsg.length() > 0) {
           /* final JSONObject js = new JSONObject();
            try {
                js.put("type", "room-msglist");
                js.put("message", RoomSession.jsVideoWBTempMsg);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        xWalkView.loadUrl("javascript:JsSocket.msgList(" + RoomSession.jsVideoWBTempMsg.toString() + ")");
                        //                        RoomSession.jsVideoWBTempMsg = new JSONArray();
                    }
                });
            }
        }
        
        if (xWalkView != null) {
            xWalkView.onShow();
            DisplayMetrics dm = new DisplayMetrics();
            if (getActivity() != null) {
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                final int wid = dm.widthPixels;
                final int hid = dm.heightPixels;
                transmitWindowSize(wid, hid);
            }
        }
        WBSession.getInstance().onPageFinished();
    }
    
    /***
     * @param state  退出标注
     */
    @Override
    public void exitAnnotation(String state) {
        if (TextUtils.isEmpty(state) && state.equals("file")) {
        
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
    
    public void transmitWindowSize(int wid, int hid) {
        try {
            final JSONObject js = new JSONObject();
            js.put("windowWidth", wid);
            js.put("windowHeight", hid);
            
            if (xWalkView != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        xWalkView.loadUrl("javascript:JsSocket.receiveActionCommand(" + "'whiteboardSDK_updateWhiteboardSize'" + "," + js.toString() + ")");
                        //                        RoomSession.jsVideoWBTempMsg = new JSONArray();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void didReceivedNotification(final int id, final Object... args) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (id) {
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
                            onRemoteMsg(addRemoteMsg, idRemoteMsg, nameRemoteMsg, tsRemoteMsg, dataRemoteMsg, fromIDRemoteMsg,
                                associatedMsgIDRemoteMsg, associatedUserIDRemoteMsg, jsonObjectRemoteMsg);
                        }
                        break;
                    case WBSession.onRoomLeaved:
                        roomDisConnect();
                        break;
                    case WBSession.onPlayBackClearAll:
                        roomPlaybackClearAll();
                        break;
                    case WBSession.onRoomConnectFaild:
                        roomDisConnect();
                        break;
                }
            }
        });
    }
}
