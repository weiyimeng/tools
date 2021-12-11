package com.eduhdsdk.message;

import android.app.UiModeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import com.classroomsdk.NotificationCenter;
import com.classroomsdk.WhiteBoradConfig;
import com.classroomsdk.WhiteBoradManager;
import com.eduhdsdk.R;
import com.eduhdsdk.entity.ChatData;
import com.eduhdsdk.entity.Trophy;
import com.eduhdsdk.entity.VideoItem;
import com.eduhdsdk.tools.SoundPlayUtils;
import com.eduhdsdk.tools.Tools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.talkcloud.room.RoomUser;
import com.talkcloud.room.TKAudioFrame;
import com.talkcloud.room.TKMediaFrameObserver;
import com.talkcloud.room.TKPlayBackManager;
import com.talkcloud.room.TKPlayBackManagerObserver;
import com.talkcloud.room.TKRoomManager;
import com.talkcloud.room.TKRoomManagerObserver;
import com.talkcloud.room.TKVideoFrame;
import com.talkcloud.room.TKVideoMirrorMode;
import com.talkcloud.room.TkAudioStatsReport;
import com.talkcloud.room.TkVideoStatsReport;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tkwebrtc.MediaCodecVideoEncoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import static org.chromium.base.ThreadUtils.runOnUiThread;

/**
 * Created by Administrator on 2017/12/12.
 */

public class RoomSession implements TKRoomManagerObserver, TKMediaFrameObserver, TKPlayBackManagerObserver {
    
    public static final int onRoomJoin = 1;
    public static final int onRoomLeave = 2;
    public static final int onError = 3;
    public static final int onWarning = 4;
    public static final int onUserJoin = 5;
    public static final int onUserLeft = 6;
    public static final int onUserPropertyChanged = 7;
    public static final int onUserVideoStatus = 8;
    public static final int onKickedout = 9;
    public static final int onMessageReceived = 10;
    public static final int onRemotePubMsg = 11;
    public static final int onRemoteDelMsg = 12;
    public static final int onUpdateAttributeStream = 13;
    public static final int onPlayBackClearAll = 14;
    public static final int onPlayBackUpdateTime = 15;
    public static final int onPlayBackDuration = 16;
    public static final int onPlayBackEnd = 17;
    public static final int onShareMediaState = 18;
    public static final int onShareScreenState = 19;
    public static final int onShareFileState = 20;
    public static final int onAudioVolume = 21;
    public static final int onRoomUser = 22;
    public static final int onRoomUserNumber = 23;
    public static final int onConnectionLost = 24;
    public static final int onFirstVideoFrame = 25;
    public static final int onUserAudioStatus = 26;
    
    public static boolean isClassBegin = false;
    public static HashSet<String> publishSet = new HashSet<String>();
    public static HashSet<String> pandingSet = new HashSet<String>();
    public static JSONArray jsVideoWBTempMsg = new JSONArray();
    public static boolean isShowVideoWB = false;
    public static List<ChatData> chatDataCache = new ArrayList<ChatData>();
    private AsyncHttpClient client = new AsyncHttpClient();
    private Context context;
    
    public static HashMap<String, Object> params = new HashMap<String, Object>();
    public static HashMap<String, Boolean> playingMap = new HashMap<String, Boolean>();
    public static List<RoomUser> playingList = Collections.synchronizedList(new ArrayList<RoomUser>());
    public static ArrayList<RoomUser> memberList = new ArrayList<RoomUser>();
    public static ArrayList<ChatData> chatList = new ArrayList<ChatData>();
    
    //room params
    public static String host = "";
    public static int port = 80;
    public static String nickname = "";
    public static String userid = "";
    public static String password = "";
    public static int userrole = -1;
    public static String path = "";
    public static String servername;
    public static String param = "";
    public static String domain = "";
    public static String mobilename = "";
    public static boolean mobilenameNotOnList = true;
    public static long classStartTime, serviceTime, localTime, syetemTime;
    public static Timer timerAddTime, timerbefClassbegin, timerAfterLeaved, tSendGift, numberTimer;
    
    private boolean isToast = false;
    private boolean isSending = false;
    static private RoomSession mInstance = null;
    public static boolean isPublish = false;
    public static boolean isPlay = false;
    public static boolean isInRoom = false;
    public static boolean _bigroom = false;
    public static boolean isShareFile = false;
    public static boolean isShareScreen = false;
    public static boolean _possibleSpeak = true;
    //    private boolean isPlayBackPlay = true;
    private boolean isEnd = false;
    private boolean isActivityStart = false;
    public static boolean fullScreen = false;
    public static boolean chatShow = false;
    public static int start = 0;
    public static int max = 19;
    
    //视频的宽高比例值
    public static int wid_ratio = 4;
    public static int hid_ratio = 3;
    //房间最大视频数
    public static int maxVideo = -1;
    // 房间类型  0为一对一   非0为一对多
    public static int roomType = -1;
    //房间号
    public static String serial;
    // 自定义奖杯数据集合
    public static List<Trophy> trophyList = new ArrayList<>();
    //自定义单个奖杯铃声的 地址
    public static String _MP3Url;
    //房间名称
    public static String roomName;
    //房间模板，皮肤信息，皮肤地址
    public static String _tplId, _skinId, _skinResource;
    
    private ConcurrentHashMap<Integer, ArrayList<Object[]>> messageBuffer = new ConcurrentHashMap<>();
    
    static public RoomSession getInstance() {
        synchronized (RoomSession.class) {
            if (mInstance == null) {
                mInstance = new RoomSession();
            }
            return mInstance;
        }
    }
    
    public void init(Context context) {
        this.context = context;
    }
    
    public void addTempVideoWBRemoteMsg(boolean add, String id, String name, long ts, Object data, String fromID, String associatedMsgID, String associatedUserID) {
        if (add) {
            if (name.equals("VideoWhiteboard")) {
                isShowVideoWB = true;
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
                
                if (associatedMsgID.equals("VideoWhiteboard") || id.equals("VideoWhiteboard")) {
                    jsVideoWBTempMsg.put(jsobj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            isShowVideoWB = false;
        }
    }
    
    public void getBigRoomUnmberAndUsers() {
        if (RoomSession._bigroom) {
            if (RoomSession.numberTimer == null) {
                RoomSession.numberTimer = new Timer();
                final int[] role = {1, 2};
                TKRoomManager.getInstance().getRoomUserNum(role, null);
                
                final HashMap hashMap = new HashMap<String, Object>();
                hashMap.put("ts", "asc");
                hashMap.put("role", "asc");
                TKRoomManager.getInstance().getRoomUsers(role, start, max, null, hashMap);
                
                RoomSession.numberTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TKRoomManager.getInstance().getRoomUserNum(role, null);
                                TKRoomManager.getInstance().getRoomUsers(role, start, max, null, hashMap);
                            }
                        });
                    }
                }, 2000, 1000);
            }
        }
    }
    
    public void getGiftNum(String roomNum, final String peerId) {
        
        String url = "http://" + host + ":" + port + "/ClientAPI/getgiftinfo";
        RequestParams params = new RequestParams();
        params.put("serial", roomNum);
        params.put("receiveid", peerId);
        
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                try {
                    int nRet = response.getInt("result");
                    if (nRet == 0) {
                        JSONArray infos = response.optJSONArray("giftinfo");
                        JSONObject info = infos.getJSONObject(0);
                        final long gifnum = info.optInt("giftnumber", 0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId, "__all", "giftnumber", gifnum);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("emm", "error=" + throwable.toString());
            }
        });
    }
    
    public void getSystemTime() {
        String timeUrl = "http://" + host + ":" + port + "/ClientAPI/systemtime";
        client.post(timeUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                try {
                    syetemTime = response.getLong("time");
                    
                    if (TKRoomManager.getInstance().getRoomProperties() != null) {
                        if (TKRoomManager.getInstance().getRoomProperties().getLong("endtime") <= syetemTime) {
                            Toast.makeText(context, context.getString(R.string.checkmeeting_error_5001), Toast.LENGTH_SHORT).show();
                            if (TKRoomManager.getInstance().getMySelf().role == 0) {
                                RoomClient.getInstance().setExit(true);
                                TKRoomManager.getInstance().leaveRoom();
                            }
                            return;
                        } else if (TKRoomManager.getInstance().getRoomProperties().getLong("endtime") > syetemTime &&
                            TKRoomManager.getInstance().getRoomProperties().getLong("endtime") - 5 * 60 <= syetemTime
                            ) {
                            long time = TKRoomManager.getInstance().getRoomProperties().getLong("endtime") - syetemTime;
                            if (time >= 60) {
                                Toast.makeText(context, time / 60 + context.getString(R.string.end_class_tip_minute), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, time + context.getString(R.string.end_class_tip_second), Toast.LENGTH_SHORT).show();
                            }
                            startClass();
                        } else {
                            startClass();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                RoomClient.getInstance().joinRoomcallBack(-1);
            }
        });
    }
    
    public void getSystemNowTime() {
        if (timerAfterLeaved != null) {
            timerAfterLeaved.cancel();
            timerAfterLeaved = null;
        }
        timerAfterLeaved = new Timer();
        
        String timeUrl = "http://" + host + ":" + port + "/ClientAPI/systemtime";
        
        client.post(timeUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                try {
                    if (TKRoomManager.getInstance().getRoomProperties() != null) {
                        syetemTime = response.optLong("time");
                        final long endClassTime = TKRoomManager.getInstance().getRoomProperties().optLong("endtime");
                        if (endClassTime > syetemTime) {
                            
                            long time = TKRoomManager.getInstance().getRoomProperties().optLong("endtime") - syetemTime;
                            if (time <= 5 * 60) {
                                if (time - 5 * 60 == syetemTime) {
                                    isToast = true;
                                }
                                if (time >= 60) {
                                    Toast.makeText(context, time / 60 + context.getString(R.string.end_class_tip_minute), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, time + context.getString(R.string.end_class_tip_second), Toast.LENGTH_SHORT).show();
                                }
                            }
                            
                            timerAfterLeaved.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    syetemTime += 1;
                                    
                                    if (endClassTime - syetemTime == 5 * 60 && !isToast) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(context, context.getString(R.string.end_class_time), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                    
                                    if (endClassTime == syetemTime) {
                                        if (timerAfterLeaved != null) {
                                            timerAfterLeaved.cancel();
                                            timerAfterLeaved = null;
                                        }
                                        RoomClient.getInstance().setExit(true);
                                        TKRoomManager.getInstance().leaveRoom();
                                    }
                                }
                            }, 1000, 1000);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                RoomClient.getInstance().joinRoomcallBack(-1);
            }
        });
    }
    
    /**
     * 发送奖杯
     *
     * @param userMap
     * @param map
     */
    public void sendGift(final HashMap<String, RoomUser> userMap, final Map<String, Object> map) {
        synchronized (this) {
            if (isSending) {
                return;
            }
            isSending = true;
            tSendGift = new Timer();
            tSendGift.schedule(new TimerTask() {
                int count = 0;
                
                @Override
                public void run() {
                    if (count == 2) {
                        isSending = false;
                        tSendGift.cancel();
                    } else {
                        count++;
                    }
                }
            }, 0, 1000);
            
            String url = "http://" + host + ":" + port + "/ClientAPI/sendgift";
            RequestParams params = new RequestParams();
            params.put("serial", TKRoomManager.getInstance().getRoomProperties().optString("serial"));
            params.put("sendid", TKRoomManager.getInstance().getMySelf().peerId);
            params.put("sendname", TKRoomManager.getInstance().getMySelf().nickName);
            HashMap<String, String> js = new HashMap<String, String>();
            for (RoomUser u : userMap.values()) {
                js.put(u.peerId, u.nickName);
            }
            params.put("receivearr", js);
            client.post(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                    try {
                        int nRet = response.getInt("result");
                        if (nRet == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (RoomUser u : userMap.values()) {
                                        long giftnumber = 0;
                                        if (u.properties.containsKey("giftnumber")) {
                                            giftnumber = u.properties.get("giftnumber") instanceof Integer ? (int) u.properties.get("giftnumber") : (long) u.properties.get("giftnumber");
                                        }
                                        giftnumber++;
                                        
                                        HashMap<String, Object> gift_send_data = new HashMap<String, Object>();
                                        gift_send_data.put("giftnumber", giftnumber);
                                        if (map != null) {
                                            gift_send_data.put("giftinfo", map);
                                        }
                                        TKRoomManager.getInstance().changeUserProperty(u.peerId, "__all", gift_send_data);
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                
                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("emm", "error=" + throwable.toString());
                }
            });
        }
    }
    
    private void sendClassBeginToPhp() {
        if (!(TKRoomManager.getInstance().getMySelf().role == 0)) {
            return;
        }
        String webFun_controlroom = "http://" + host + ":" + port + "/ClientAPI" + "/roomstart";
        RequestParams params = new RequestParams();
        try {
            params.put("serial", TKRoomManager.getInstance().getRoomProperties().get("serial"));
            params.put("companyid", TKRoomManager.getInstance().getRoomProperties().get("companyid"));
            client.post(webFun_controlroom, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                    try {
                        int nRet = response.getInt("result");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                
                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("emm", "error=" + throwable.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public void sendClassDissToPhp() {
        String webFun_controlroom = "http://" + host + ":" + port + "/ClientAPI" + "/roomover";
        RequestParams params = new RequestParams();
        try {
            params.put("act", 3);
            params.put("serial", TKRoomManager.getInstance().getRoomProperties().get("serial"));
            params.put("companyid", TKRoomManager.getInstance().getRoomProperties().get("companyid"));
            client.post(webFun_controlroom, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                    try {
                        int nRet = response.getInt("result");
                        if (nRet != 0) {
                            Log.e("demo", "下课接口调用失败，失败数据：");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                
                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("emm", "error=" + throwable.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public void startClass() {
        TKRoomManager.getInstance().stopShareMedia();
        //        lin_audio_control.setVisibility(View.INVISIBLE);
        try {
            long expires = TKRoomManager.getInstance().getRoomProperties().optLong("endtime") + 5 * 60;
            if (RoomControler.isNotLeaveAfterClass()) {
                TKRoomManager.getInstance().delMsg("__AllAll", "__AllAll", "__none", new HashMap<String, Object>());
            }
            TKRoomManager.getInstance().pubMsg("ClassBegin", "ClassBegin", "__all", new JSONObject().put("recordchat", true).toString(), true, expires);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendClassBeginToPhp();
    }
    
    public void releaseTimer() {
        if (timerAddTime != null) {
            timerAddTime.cancel();
            timerAddTime = null;
        }
        if (timerbefClassbegin != null) {
            timerbefClassbegin.cancel();
            timerbefClassbegin = null;
        }
        
        if (timerAfterLeaved != null) {
            timerAfterLeaved.cancel();
            timerAfterLeaved = null;
        }
    }
    
    @Override
    public void onRoomJoined() {
        
        //获取房间信息
        getRoomInfo();
        
        //MediaCodecVideoEncoder.isVp8HwSupported()  表示是Vp8  ture  表示硬解     false  表示软解
        if (TKRoomManager.getInstance().getRoomProperties().has("vcodec")) {
            int vcodec = TKRoomManager.getInstance().getRoomProperties().optInt("vcodec");
            switch (vcodec) {
                case 0:
                    if (!MediaCodecVideoEncoder.isVp8HwSupported() && wid_ratio > 640) {
                        TKRoomManager.getInstance().setVideoProfile(640, 480);
                    }
                    break;
                case 1:
                    if (!MediaCodecVideoEncoder.isVp9HwSupported() && wid_ratio > 640) {
                        TKRoomManager.getInstance().setVideoProfile(640, 480);
                    }
                    break;
                case 2:
                    if (!MediaCodecVideoEncoder.isH264HwSupported() && wid_ratio > 640) {
                        TKRoomManager.getInstance().setVideoProfile(640, 480);
                    }
                    break;
            }
        }
        
        TKRoomManager.getInstance().setLocalVideoMirrorMode(TKVideoMirrorMode.TKVideoMirrorModeDisabled);
        
        closeSpeaker();
        
        if (RoomControler.haveTimeQuitClassroomAfterClass()) {
            if (timerAfterLeaved != null) {
                timerAfterLeaved.cancel();
                timerAfterLeaved = null;
            } else {
                timerAfterLeaved = new Timer();
            }
            getSystemNowTime();
        }
        WhiteBoradManager.getInstance().setUserrole(TKRoomManager.getInstance().getMySelf().role);
        getGiftNum(serial, TKRoomManager.getInstance().getMySelf().peerId);
        
        if (TKRoomManager.getInstance().getMySelf().role == 0) {
            TKRoomManager.getInstance().pubMsg("UpdateTime", "UpdateTime", "__all", null, false, null, null);
        }
        
        isInRoom = true;
        
        TKRoomManager.getInstance().getMySelf().nickName = StringEscapeUtils.unescapeHtml4(TKRoomManager.getInstance().getMySelf().nickName);
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onRoomJoin);
        } else {
            addMessageToBuffer(onRoomJoin);
        }
    }
    
    private void getRoomInfo() {
        JSONObject jsonRoomInfo = TKRoomManager.getInstance().getRoomProperties();
        if (jsonRoomInfo != null) {
            
            if (jsonRoomInfo.has("videoheight") && jsonRoomInfo.has("videowidth")) {
                wid_ratio = jsonRoomInfo.optInt("videowidth");
                hid_ratio = jsonRoomInfo.optInt("videoheight");
            }
            
            maxVideo = jsonRoomInfo.optInt("maxvideo");
            if (maxVideo < 13) {
                maxVideo = 7;
            } else {
                maxVideo = 7;
            }
            
            roomType = jsonRoomInfo.optInt("roomtype");
            serial = jsonRoomInfo.optString("serial");
            
            if (jsonRoomInfo.has("voicefile")) {
                _MP3Url = jsonRoomInfo.optString("voicefile", "");
            }
            
            if (jsonRoomInfo.has("trophy")) {
                JSONArray trophyArray = jsonRoomInfo.optJSONArray("trophy");
                if (trophyArray != null && com.classroomsdk.RoomControler.isCustomTrophy()) {
                    setDataTrophy(trophyArray);
                }
            }
            
            if (jsonRoomInfo.has("roomname")) {
                roomName = jsonRoomInfo.optString("roomname");
                roomName = StringEscapeUtils.unescapeHtml4(roomName);
            }
            
            if (jsonRoomInfo.has("tplId") && jsonRoomInfo.has("skinId") &&
                jsonRoomInfo.has("skinResource")) {
                _tplId = jsonRoomInfo.optString("tplId");
                _skinId = jsonRoomInfo.optString("skinId");
                _skinResource = jsonRoomInfo.optString("skinResource");
            }
            
            String chairmancontrol = jsonRoomInfo.optString("chairmancontrol");
            if (chairmancontrol != null && !chairmancontrol.isEmpty()) {
                RoomControler.chairmanControl = chairmancontrol;
            }
        }
    }
    
    public void closeSpeaker() {
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                if (audioManager.isWiredHeadsetOn()) {
                    TKRoomManager.getInstance().useLoudSpeaker(false);
                } else {
                    TKRoomManager.getInstance().useLoudSpeaker(true);
                    openSpeaker();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setDataTrophy(JSONArray trophyArray) {
        trophyList.clear();
        for (int x = 0; x < trophyArray.length(); x++) {
            Trophy trophy = new Trophy();
            try {
                trophy.setCompanyid(trophyArray.getJSONObject(x).optString("companyid"));
                trophy.setTrophyname(trophyArray.getJSONObject(x).optString("trophyname"));
                trophy.setTrophyimg(trophyArray.getJSONObject(x).optString("trophyimg"));
                trophy.setTrophyvoice(trophyArray.getJSONObject(x).optString("trophyvoice"));
                trophy.setTrophyIcon(trophyArray.getJSONObject(x).optString("trophyIcon"));
                trophy.setTrophyeffect(trophyArray.getJSONObject(x).optString("trophyeffect"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            trophyList.add(trophy);
        }
    }
    
    /**
     * 打开扬声器
     */
    private void openSpeaker() {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
            try {
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.ROUTE_SPEAKER);
                if (!audioManager.isSpeakerphoneOn()) {
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    audioManager.setSpeakerphoneOn(true);
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                        AudioManager.STREAM_VOICE_CALL);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /***
     *    离开房间的回调
     */
    @Override
    public void onRoomLeaved() {
        if (trophyList != null && trophyList.size() > 0) {
            SoundPlayUtils.releaseTrophy();
        } else {
            SoundPlayUtils.release();
        }
        WhiteBoradConfig.getsInstance().clear();
        publishSet.clear();
        isClassBegin = false;
        isPublish = false;
        isPlay = false;
        isInRoom = false;
        _bigroom = false;
        playingMap.clear();
        playingList.clear();
        
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onRoomLeave);
        } else {
            addMessageToBuffer(onRoomLeave);
        }
    }
    
    /***
     *  房间链接丢失
     */
    @Override
    public void onConnectionLost() {
        if (trophyList != null && trophyList.size() > 0) {
            SoundPlayUtils.releaseTrophy();
        } else {
            SoundPlayUtils.release();
        }
        //        WhiteBoradConfig.getsInstance().clear();
        publishSet.clear();
        isClassBegin = false;
        isPublish = false;
        isPlay = false;
        isInRoom = false;
        _bigroom = false;
        playingMap.clear();
        playingList.clear();
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onConnectionLost);
        } else {
            addMessageToBuffer(onConnectionLost);
        }
    }
    
    /***
     *   错误回调
     * @param errorCode   错误码
     *                    1501 摄像头丢失  1502 socket 5 次链接失败，应退出教室   1503 udp 链接异常(链接成功过)
     *                   1504 udp 链接异常（没有链接成功过，防火墙拦截）  1505 房间视频路数已达上限
     *                   进入房间的错误：3001 服务器过期  3002 公司被冻结  3003 教室被删除或过期
     *                  4007 教室不存在  4008 教室密码错误  4110 该教室需要密码，请输入密码
     * @param errMsg       错误信息
     */
    @Override
    public void onError(int errorCode, String errMsg) {
        if (errorCode == 3001 || errorCode == 3002 || errorCode == 3003 || errorCode == 4007 || errorCode == 4008
            || errorCode == 4110 || errorCode == 0 || errorCode == 4012 || errorCode == 3) {
            RoomClient.getInstance().joinRoomcallBack(errorCode);
        } else if (errorCode == 10002) {
            RoomClient.getInstance().setExit(true);
            WhiteBoradConfig.getsInstance().clear();
            playingMap.clear();
            playingList.clear();
            publishSet.clear();
            isClassBegin = false;
            isInRoom = false;
            isPublish = false;
            isPlay = false;
            
            maxVideo = -1;
            roomType = -1;
            serial = null;
            trophyList.clear();
            _MP3Url = null;
            roomName = null;
            _tplId = null;
            _skinId = null;
            _skinResource = null;
            
            RoomClient.getInstance().joinRoomcallBack(-1);
        }
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onError, errorCode, errMsg);
        } else {
            addMessageToBuffer(onError, errorCode, errMsg);
        }
    }
    
    /***
     *     警告回调
     * @param warning   1751 摄像头打开   1752 摄像头关闭
     */
    @Override
    public void onWarning(int warning) {
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onWarning, warning);
        } else {
            addMessageToBuffer(onWarning, warning);
        }
    }
    
    /***
     *    其他用户进入房间的回调
     * @param roomUser          进入的用户
     * @param inList            是否在我之前进入房间，true—之前，false—之后
     */
    @Override
    public void onUserJoined(RoomUser roomUser, boolean inList) {
        
        getRoomInfo();
        
        roomUser.nickName = StringEscapeUtils.unescapeHtml4(roomUser.nickName);
        ChatData ch = new ChatData();
        ch.setState(1);
        ch.setInOut(true);
        ch.setStystemMsg(true);
        ch.setUser(roomUser);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        ch.setTime(str);
        //        if (roomUser.role != 4){
        //            chatList.add(ch);
        //        }
        
        if (inList && roomUser.role != 4) {
            if (roomUser.role == 0 && TKRoomManager.getInstance().getMySelf().role == 0 ||
                (roomType == 0 && roomUser.role == TKRoomManager.getInstance().getMySelf().role)) {
                TKRoomManager.getInstance().evictUser(roomUser.peerId);
            }
            if (roomUser.properties.containsKey("isInBackGround") && TKRoomManager.getInstance().getMySelf().role != 2) {
                if (roomUser == null) {
                    return;
                }
                boolean isinback = Tools.isTure(roomUser.properties.get("isInBackGround"));
                ChatData ch2 = new ChatData();
                ch2.setState(2);
                ch2.setHold(isinback);
                ch2.setStystemMsg(true);
                ch2.setUser(roomUser);
                SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
                Date curDate2 = new Date(System.currentTimeMillis());//获取当前时间
                String str2 = formatter2.format(curDate2);
                ch2.setTime(str2);
                //                if (roomUser.role != 4){
                //                    chatList.add(ch2);
                //                }
            }
        }
        getMemberList();
        
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onUserJoin, roomUser, inList);
        } else {
            addMessageToBuffer(onUserJoin, roomUser, inList);
        }
    }
    
    private void getMemberList() {
        memberList.clear();
        for (RoomUser u : TKRoomManager.getInstance().getUsers().values()) {
            if (!u.peerId.equals(TKRoomManager.getInstance().getMySelf().peerId) && (u.role == 2 || u.role == 1)) {
                if (u.role == 1) {
                    memberList.add(0, u);
                } else {
                    memberList.add(u);
                }
            }
        }
    }
    
    /***
     *     其他用户离开房间
     * @param roomUser      离开的用户
     */
    @Override
    public void onUserLeft(RoomUser roomUser) {
        ChatData ch = new ChatData();
        ch.setState(1);
        ch.setInOut(false);
        ch.setStystemMsg(true);
        ch.setUser(roomUser);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        ch.setTime(str);
        if (roomUser != null && roomUser.role != 4) {
            //            chatList.add(ch);
        }
        RoomSession.publishSet.remove(roomUser.peerId);
        if (playingMap.containsKey(roomUser.peerId)) {
            playingMap.remove(roomUser.peerId);
        }
        if (playingList.contains(roomUser.peerId)) {
            playingList.remove(roomUser.peerId);
        }
        getMemberList();
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onUserLeft, roomUser);
        } else {
            addMessageToBuffer(onUserLeft, roomUser);
        }
    }
    
    /***
     *   用户属性改变
     * @param roomUser    改变属性的用户
     * @param map          改变的属性集合
     * @param fromId      改变该用户属性的用户的用户 ID
     */
    @Override
    public void onUserPropertyChanged(RoomUser roomUser, Map<String, Object> map, String fromId) {
        pandingSet.remove(roomUser.peerId);
        if (map.containsKey("isInBackGround")) {
            boolean isinback = Tools.isTure(map.get("isInBackGround"));
            if (TKRoomManager.getInstance().getMySelf().role != 2) {
                ChatData ch = new ChatData();
                ch.setState(2);
                ch.setHold(isinback);
                ch.setStystemMsg(true);
                ch.setUser(roomUser);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                ch.setTime(str);
                //                if (roomUser != null && roomUser.role != 4){
                //                    chatList.add(ch);
                //                }
            }
        }
        
        if (roomUser != null && roomUser.role != -1 && roomUser.getPublishState() == 0) {
            if (!playingMap.containsKey(roomUser.peerId) && (roomUser.canDraw && roomUser.role == 2)) {
                playingMap.put(roomUser.peerId, true);
            }
        }
        
        if (playingMap.containsKey(roomUser.peerId)) {
            if (roomUser.getPublishState() == 0 && !roomUser.canDraw) {
                playingMap.remove(roomUser.peerId);
            }
        }
        
        if (roomUser.peerId.equals(TKRoomManager.getInstance().getMySelf().peerId) && map.containsKey("servername") && !fromId.equals(TKRoomManager.getInstance().getMySelf().peerId)) {
            String servername = (String) map.get("servername");
            SharedPreferences sp = context.getSharedPreferences("classroom", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("servername", servername);
            editor.commit();
            TKRoomManager.getInstance().switchService(servername);
        }
        
        getMemberList();
        
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onUserPropertyChanged, roomUser, map, fromId);
        } else {
            addMessageToBuffer(onUserPropertyChanged, roomUser, map, fromId);
        }
    }
    
    /***
     *    用户视频状态改变  (多流时回调)
     * @param userId           用户 id
     * @param state           视频状态 0 取消发布 1 发布
     * @param deviceId       设备 ID（多流时使用）
     */
    @Override
    public void onUserVideoStatus(String userId, int state, String deviceId) {
    
    }
    
    /***
     *   用户音频状态改变
     * @param userId     用户 id
     * @param state      音频状态 0 取消发布 1 发布
     */
    @Override
    public void onUserAudioStatus(String userId, int state) {
        
        RoomUser roomUser = TKRoomManager.getInstance().getUser(userId);
        if (roomUser != null) {
            roomUser.properties.remove("passivityPublish");
        }
        if (publishSet.contains(userId)) {
            publishSet.remove(userId);
        }
        
        if (state > 0) {
            
            if (TKRoomManager.getInstance().getMySelf().role == -1) {
                for (String key : playingMap.keySet()) {
                    RoomUser user = TKRoomManager.getInstance().getUser(key);
                    if (roomUser.role == 0 && user.role == roomUser.role) {
                        playingMap.remove(key);
                        break;
                    }
                }
            }
            playingMap.put(userId, state >= 1 && state < 4);
            
            if (state >= 1) {
                publishSet.add(userId);
            }
        } else {
            if (roomUser.getPublishState() == 0) {
                playingMap.remove(userId);
            }
        }
        
        getPlayingList();
        
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onUserAudioStatus, userId, state);
        } else {
            addMessageToBuffer(onUserAudioStatus, userId, state);
        }
    }
    
    /***
     *  用户视频状态改变
     * @param userId   用户 id
     * @param state    视频状态 0 取消发布 1 发布
     */
    @Override
    public void onUserVideoStatus(String userId, int state) {
        RoomUser roomUser = TKRoomManager.getInstance().getUser(userId);
        if (roomUser != null) {
            roomUser.properties.remove("passivityPublish");
        }
        if (publishSet.contains(userId)) {
            publishSet.remove(userId);
        }
        
        if (state > 0) {
            
            if (TKRoomManager.getInstance().getMySelf().role == -1) {
                for (String key : playingMap.keySet()) {
                    RoomUser user = TKRoomManager.getInstance().getUser(key);
                    if (roomUser.role == 0 && user.role == roomUser.role) {
                        playingMap.remove(key);
                        break;
                    }
                }
            }
            
            if (playingMap.size() <= maxVideo) {
                playingMap.put(userId, state > 1 && state < 4);
            }
            
            
            if (state >= 1) {
                publishSet.add(userId);
            }
            
        } else {
            if (roomUser.getPublishState() == 0) {
                playingMap.remove(userId);
            }
        }
        
        getPlayingList();
        
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onUserVideoStatus, userId, state);
        } else {
            addMessageToBuffer(onUserVideoStatus, userId, state);
        }
    }
    
    public void getPlayingList() {
        playingList.clear();
        for (int x = 0; x < memberList.size(); x++) {
            RoomUser roomUser = memberList.get(x);
            if (roomUser.getPublishState() > 0 || (roomUser.canDraw && roomUser.role == 2)) {
                if (playingList.size() <= RoomSession.maxVideo && roomUser != null) {
                    if (roomUser.role == 0) {
                        playingList.add(0, roomUser);
                    } else {
                        playingList.add(roomUser);
                    }
                }
            }
        }
        
        for (String p : playingMap.keySet()) {
            RoomUser u = TKRoomManager.getInstance().getUser(p);
            if (playingList.size() <= RoomSession.maxVideo && u != null && !playingList.contains(u)) {
                if (u.role == 0) {
                    playingList.add(0, u);
                } else {
                    playingList.add(u);
                }
            }
        }
    }
    
    /***
     *   自己被请出房间的回调
     * @param reason   原因
     */
    @Override
    public void onKickedout(int reason) {
        RoomClient.getInstance().setExit(true);
        TKRoomManager.getInstance().leaveRoom();
        RoomClient.getInstance().kickout(reason == 1 ? RoomClient.Kickout_ChairmanKickout : RoomClient.Kickout_Repeat);
        
        if (reason == 1) {
            SharedPreferences preferences = context.getSharedPreferences("KickOutPersonInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor numberEditor = preferences.edit();
            numberEditor.putString("RoomNumber", RoomSession.serial);
            numberEditor.putLong("Time", System.currentTimeMillis());
            numberEditor.commit();
        }
        
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onKickedout, reason);
        } else {
            addMessageToBuffer(onKickedout, reason);
        }
    }
    
    /***
     *    收到文本消息
     * @param roomUser     发送文本消息的用户
     * @param jsonObject   文本消息，json 格式
     * @param ts            消息发送时间戳
     */
    @Override
    public void onMessageReceived(RoomUser roomUser, JSONObject jsonObject, long ts) {
        ChatData ch = new ChatData();
        ch.setUser(roomUser);
        ch.setStystemMsg(false);
        ch.setMsgTime(System.currentTimeMillis());
        int type = jsonObject.optInt("type");
        if (type == 0) {
            chatDataCache.add(ch);
            ch.setMessage(jsonObject.optString("msg"));
            ch.setTrans(false);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = null;
            if (StringUtils.isEmpty(RoomSession.path)) {
                curDate = new Date(System.currentTimeMillis());//获取当前时间
            } else {
                curDate = new Date(ts);
            }
            String str = formatter.format(curDate);
            ch.setTime(str);
            chatList.add(ch);
        }
        
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onMessageReceived, roomUser, jsonObject, ts);
        } else {
            addMessageToBuffer(onMessageReceived, roomUser, jsonObject, ts);
        }
    }
    
    /***
     *  收到信令消息回调
     * @param id          消息 id
     * @param name        消息名字
     * @param ts          消息发送时间戳
     * @param data        消息携带数据
     * @param inList      消息是否在消息列表中
     * @param fromID       发送者 id
     * @param associatedMsgID  消息关联消息的 id （该消息删除时会跟随删除)
     * @param associatedUserID  消息关联用户的 id （该用户退出时会跟随删除）
     */
    @Override
    public void onRemotePubMsg(String id, String name, long ts, Object data, boolean inList, String fromID,
                               String associatedMsgID, String associatedUserID) {
        
        RoomSession.getInstance().addTempVideoWBRemoteMsg(true, id, name, ts, data, fromID, associatedMsgID, associatedUserID);
        
        if (name.equals("ClassBegin")) {
            if (isClassBegin) {
                return;
            }
            isClassBegin = true;
            
            if (RoomControler.haveTimeQuitClassroomAfterClass()) {
                if (timerAfterLeaved != null) {
                    timerAfterLeaved.cancel();
                    timerAfterLeaved = null;
                }
            }
            
            classStartTime = ts;
            TKRoomManager.getInstance().pubMsg("UpdateTime", "UpdateTime", TKRoomManager.getInstance().getMySelf().peerId, null, false, null, null);
            if (timerbefClassbegin != null) {
                timerbefClassbegin.cancel();
                timerbefClassbegin = null;
            }
            
            if (TKRoomManager.getInstance().getMySelf().role == 0 && !inList) {
                TKRoomManager.getInstance().unPlayMedia(TKRoomManager.getInstance().getMySelf().peerId);
            }
            
            if (userrole == 2 && RoomControler.isAutoHasDraw() && roomType == 0) {
                TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId, "__all", "candraw", true);
            }
            
            RoomClient.getInstance().onClassBegin();
            
        } else if (name.equals("BigRoom")) {
            _bigroom = true;
        } else if (name.equals("UpdateTime")) {
            if (RoomSession.isClassBegin) {
                if (timerbefClassbegin != null) {
                    timerbefClassbegin.cancel();
                    timerbefClassbegin = null;
                }
                serviceTime = ts;
                localTime = serviceTime - classStartTime;
            } else {
                if (!RoomSession.isClassBegin) {
                    
                    
                    if (TKRoomManager.getInstance().getMySelf().role == 0 && getfinalClassBeginMode()) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        try {
                            long expires = TKRoomManager.getInstance().getRoomProperties().getLong("endtime") + 5 * 60;
                            if (RoomControler.isNotLeaveAfterClass()) {
                                TKRoomManager.getInstance().delMsg("__AllAll", "__AllAll", "__none", new HashMap<String, Object>());
                            }
                            TKRoomManager.getInstance().pubMsg("ClassBegin", "ClassBegin", "__all", new HashMap<String, Object>(), true, expires);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if (name.equals("EveryoneBanChat")) {
            _possibleSpeak = false;
        } else if (name.equals("FullScreen")) {
            fullScreen = true;
        } else if (name.equals("ChatShow")) {
            chatShow = true;
        }
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onRemotePubMsg, id, name, ts, data, inList, fromID, associatedMsgID, associatedUserID);
        } else {
            addMessageToBuffer(onRemotePubMsg, id, name, ts, data, inList, fromID, associatedMsgID, associatedUserID);
        }
    }
    
    private boolean getfinalClassBeginMode() {
        boolean isauto = true;
        try {
            isauto = TKRoomManager.getInstance().getRoomProperties().getString("companyid").equals("10035") ? false : RoomControler.isAutoClassBegin();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isauto;
    }
    
    /***
     *   收到信令消息删除回调
     * @param id               消息 id
     * @param name            消息名字
     * @param ts              消息发送时间戳
     * @param data            消息携带数据
     * @param inList          消息是否在消息列表中
     * @param fromID           发送者 id
     * @param associatedMsgID  消息关联消息的 id （该消息删除时会跟随删除)
     * @param associatedUserID   消息关联用户的 id （该用户退出时会跟随删除）
     */
    @Override
    public void onRemoteDelMsg(String id, String name, long ts, Object data, boolean inList,
                               String fromID, String associatedMsgID, String associatedUserID) {
        RoomSession.getInstance().addTempVideoWBRemoteMsg(false, id, name, ts, data, fromID, associatedMsgID, associatedUserID);
        if (name.equals("ClassBegin")) {
            isClassBegin = false;
            
            if (!RoomControler.isNotLeaveAfterClass()) {
                TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId, "__all",
                    "publishState", 0);
                TKRoomManager.getInstance().delMsg("__AllAll", "__AllAll", "__none", new HashMap<String, Object>());
            }
            
            localTime = 0;
            if (timerAddTime != null) {
                if (!RoomControler.haveTimeQuitClassroomAfterClass()) {
                    timerAddTime.cancel();
                    timerAddTime = null;
                }
            }
            
            RoomClient.getInstance().onClassDismiss();
            TKRoomManager.getInstance().unPlayMedia(fromID);
        } else if (name.equals("EveryoneBanChat")) {
            _possibleSpeak = true;
        } else if (name.equals("FullScreen")) {
            fullScreen = false;
        } else if (name.equals("ChatShow")) {
            chatShow = false;
        }
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onRemoteDelMsg, id, name, ts, data, inList, fromID, associatedMsgID, associatedUserID);
        } else {
            addMessageToBuffer(onRemoteDelMsg, id, name, ts, data, inList, fromID, associatedMsgID, associatedUserID);
        }
    }
    
    /***
     *    网络媒体播放进度，状态回调
     * @param peerid    用户 Id
     * @param pos       播放进度
     * @param isPlay    是否在播放
     * @param hashMap   流自定义扩展属性
     */
    @Override
    public void onUpdateAttributeStream(String peerid, long pos, boolean isPlay, HashMap<String, Object> hashMap) {
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onUpdateAttributeStream, peerid, pos, isPlay, hashMap);
        } else {
            addMessageToBuffer(onUpdateAttributeStream, peerid, pos, isPlay, hashMap);
        }
    }
    
    /***
     *    回放清除所有的数据的回调
     */
    @Override
    public void onPlayBackClearAll() {
        if (playingMap != null) {
            playingMap.clear();
        }
        
        if (memberList != null) {
            memberList.clear();
        }
        
        if (playingList != null) {
            playingList.clear();
        }
        
        
        isEnd = false;
        if (chatList != null) {
            chatList.clear();
        }
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onPlayBackClearAll);
        } else {
            addMessageToBuffer(onPlayBackClearAll);
        }
    }
    
    /***
     *     回放播放进度回调
     * @param recordStartTime   当前时间
     */
    @Override
    public void onPlayBackUpdateTime(long recordStartTime) {
        if (isEnd) {
            return;
        }
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onPlayBackUpdateTime, recordStartTime);
        } else {
            addMessageToBuffer(onPlayBackUpdateTime, recordStartTime);
        }
    }
    
    /***
     *    回放起止时间回调
     * @param startTime     开始时间
     * @param endTime       结束时间
     */
    @Override
    public void onPlayBackDuration(long startTime, long endTime) {
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onPlayBackDuration, startTime, endTime);
        } else {
            addMessageToBuffer(onPlayBackDuration, startTime, endTime);
        }
    }
    
    /***
     *   回放结束回调
     */
    @Override
    public void onPlayBackEnd() {
        //        isPlayBackPlay = false;
        isEnd = true;
        try {
            TKPlayBackManager.getInstance().pausePlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onPlayBackEnd);
        } else {
            addMessageToBuffer(onPlayBackEnd);
        }
    }
    
    /***
     *   有网络媒体文件共享的回调    mp4/mp3
     * @param peerId   共享者用户 id
     * @param state    媒体共享状态 0 停止 1 开始
     * @param attrs    自定义数据
     */
    @Override
    public void onShareMediaState(String peerId, int state, Map<String, Object> attrs) {
        if (state == 0) {
            isPublish = false;
            TKRoomManager.getInstance().delMsg("VideoWhiteboard", "VideoWhiteboard", "__all", null);
        } else if (state == 1) {
            isPublish = true;
            isPlay = false;
        }
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onShareMediaState, peerId, state, attrs);
        } else {
            addMessageToBuffer(onShareMediaState, peerId, state, attrs);
        }
    }
    
    /***
     *   有屏幕共享的回调
     * @param peerId     共享者用户 id
     * @param state      媒体共享状态 0 停止 1 开始
     */
    @Override
    public void onShareScreenState(String peerId, int state) {
        if (state == 0) {
            isPublish = false;
            isShareScreen = false;
        } else if (state == 1) {
            isPublish = true;
            isShareScreen = true;
        }
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onShareScreenState, peerId, state);
        } else {
            addMessageToBuffer(onShareScreenState, peerId, state);
        }
    }
    
    /***
     *  有文件媒体共享的回调
     * @param peerId      共享者用户 id
     * @param state       媒体共享状态 0 停止 1 开始
     */
    @Override
    public void onShareFileState(String peerId, int state) {
        if (state == 0) {
            isPublish = false;
            isShareFile = false;
        } else if (state == 1) {
            isShareFile = true;
            isPublish = true;
        }
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onShareFileState, peerId, state);
        } else {
            addMessageToBuffer(onShareFileState, peerId, state);
        }
    }
    
    /***
     *  音量回调
     * @param peerId   用户 id
     * @param volume   音量
     */
    @Override
    public void onAudioVolume(String peerId, int volume) {
        NotificationCenter.getInstance().postNotificationName(onAudioVolume, peerId, volume);
    }
    
    /***
     *    获取回放房间信息回掉
     * @param code         响应码   0  成功   非0 不成功
     * @param response     响应数据    错误日志
     */
    @Override
    public void onPlayBackRoomJson(int code, String response) {
        RoomClient.getInstance().onPlayBackRoomJson(code, response);
    }
    
    /***
     *    视频监控数据
     * @param peerId                 用户 id
     * @param tkVideoStatsReport    网络状态信息
     */
    @Override
    public void onVideoStatsReport(String peerId, TkVideoStatsReport tkVideoStatsReport) {
    
    }
    
    /***
     *   音频监控数据
     * @param peerId               用户 id
     * @param tkAudioStatsReport  网络状态信息
     */
    @Override
    public void onAudioStatsReport(String peerId, TkAudioStatsReport tkAudioStatsReport) {
    
    }
    
    /***
     *  获取大并发用户列表回调
     * @param code         获取是否成功 0-成功
     * @param arrayList    用户列表（成功）
     */
    @Override
    public void onGetRoomUsersBack(int code, ArrayList<RoomUser> arrayList) {
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onRoomUser, code, arrayList);
        } else {
            addMessageToBuffer(onRoomUser, code, arrayList);
        }
    }
    
    /***
     *   获取大并发教室中人数
     * @param code    获取是否成功 0-成功
     * @param number   人数
     */
    @Override
    public void onGetRoomUserNumBack(int code, int number) {
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onRoomUserNumber, code, number);
        } else {
            addMessageToBuffer(onRoomUserNumber, code, number);
        }
    }
    
    /***
     *    切换纯音频教室回调
     * @param fromId     执行该命令的用户 ID
     * @param isSwitch   true-切换， false-不切换
     */
    @Override
    public void onAudioRoomSwitch(String fromId, boolean isSwitch) {
    
    }
    
    /***
     *    视频数据首帧回调
     * @param peerId      peerID
     * @param width       视频类型
     * @param height      视频宽
     * @param mediaType   视频高
     */
    @Override
    public void onFirstVideoFrame(String peerId, int width, int height, int mediaType) {
        if (isActivityStart) {
            onStart();
            NotificationCenter.getInstance().postNotificationName(onFirstVideoFrame, peerId, width, height, mediaType);
        } else {
            addMessageToBuffer(onFirstVideoFrame, peerId, width, height, mediaType);
        }
    }
    
    /**
     * 视频数据首帧回掉（多流）
     *
     * @param peerID    用户id
     * @param mediaType 视频类型
     * @param width     宽
     * @param height    高
     * @param cameraId  设备id
     */
    @Override
    public void onFirstVideoFrame(String peerID, int mediaType, int width, int height, String cameraId) {
    
    }
    
    /***
     *   切换纯音频教室回调
     * @param peerID        用户 Id
     * @param mediaType     视频类型
     *     TK_MEDIA_CAMERA = 0; TK_MEDIA_MIC = 11; TK_MEDIA_SPEAKER = 12;
     *     TK_MEDIA_FILE = 101;  TK_MEDIA_SCREEN = 102; TK_MEDIA_MEDIA = 103;
     */
    @Override
    public void onFirstAudioFrame(String peerID, int mediaType) {
    
    }
    
    /***
     *   房间外网络检测回掉
     * @param networkQuality   网络等级
     * @param delay             延迟时间
     */
    @Override
    public void onNetworkQuality(int networkQuality, long delay) {
    
    }
    
    /***
     *   房间信息回掉
     * @param infoCode  信息代号
     * @param message   描述信息
     */
    @Override
    public void onInfo(int infoCode, String message) {
    
    }
    
    private void addMessageToBuffer(int key, Object... args) {
        if (messageBuffer.containsKey(key) && messageBuffer.get(key) != null) {
            ArrayList<Object[]> bufValue = messageBuffer.get(key);
            bufValue.add(args);
        } else {
            ArrayList<Object[]> bufValue = new ArrayList<>();
            bufValue.add(args);
            messageBuffer.put(key, bufValue);
        }
    }
    
    public void onStart() {
        isActivityStart = true;
        for (int k : messageBuffer.keySet()) {
            ArrayList<Object[]> bufValue = messageBuffer.get(k);
            if (bufValue != null) {
                for (int i = 0; i < bufValue.size(); i++) {
                    NotificationCenter.getInstance().postNotificationName(k, bufValue.get(i));
                }
            }
        }
        messageBuffer.clear();
    }
    
    public void onStop() {
        isActivityStart = false;
        messageBuffer.clear();
        releaseTimer();
        _possibleSpeak = true;
    }
    
    public void clear() {
        
        host = "";
        port = 80;
        nickname = "";
        userid = "";
        password = "";
        userrole = -1;
        path = "";
        servername = "";
        param = "";
        domain = "";
        mobilename = "";
        mobilenameNotOnList = true;
        
        maxVideo = -1;
        roomType = -1;
        serial = null;
        trophyList.clear();
        _MP3Url = null;
        roomName = null;
        _tplId = null;
        _skinId = null;
        _skinResource = null;
        RoomControler.chairmanControl = null;
    }
    
    public int[] getRes() {
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.loading);
        int len = typedArray.length();
        int[] resId = new int[len];
        for (int i = 0; i < len; i++) {
            resId[i] = typedArray.getResourceId(i, -1);
        }
        typedArray.recycle();
        return resId;
    }
    
    /***
     *
     * @param tkAudioFrame  音频远端原始数据
     * @param uid            用户 id
     * @param type           类型
     * @return
     */
    @Override
    public boolean onRenderAudioFrame(TKAudioFrame tkAudioFrame, String uid, int type) {
        return false;
    }
    
    /***
     *
     * @param tkVideoFrame   视频本地原始数据
     * @param id              用户 id
     * @return
     */
    @Override
    public boolean onCaptureVideoFrame(TKVideoFrame tkVideoFrame, String id) {
        return false;
    }
    
    /***
     *
     * @param tkVideoFrame  视频远端原始数据
     * @param uid            用户 id
     * @param type           类型
     * @return 设备 ID
     */
    @Override
    public boolean onRenderVideoFrame(TKVideoFrame tkVideoFrame, String uid, int type) {
        return false;
    }
    
    /***
     *
     * @param tkVideoFrame   视频远端原始数据
     * @param uid             用户 id
     * @param type            类型
     * @param deviceId        设备 ID
     * @return
     */
    @Override
    public boolean onRenderVideoFrame(TKVideoFrame tkVideoFrame, String uid, int type, String deviceId) {
        return false;
    }
    
    /***
     *
     * @param tkAudioFrame  音频本地原始数据
     * @param uid            用户 id
     * @param type           类型
     * @return
     */
    @Override
    public boolean onCaptureAudioFrame(TKAudioFrame tkAudioFrame, String uid, int type) {
        return false;
    }
    
}
