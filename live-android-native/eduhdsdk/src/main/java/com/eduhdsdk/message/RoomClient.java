package com.eduhdsdk.message;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.classroomsdk.WBSession;
import com.eduhdsdk.R;
import com.eduhdsdk.interfaces.CheckForUpdateCallBack;
import com.eduhdsdk.interfaces.MeetingNotify;
import com.eduhdsdk.tools.KeyBoardUtil;
import com.eduhdsdk.tools.Tools;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.talkcloud.room.TKPlayBackManager;
import com.talkcloud.room.TKRoomManager;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/***
 * xiaoyang for customer
 */
public class RoomClient {
    
    private MeetingNotify notify;
    private com.eduhdsdk.interfaces.JoinmeetingCallBack callBack;
    static private RoomClient mInstance = null;
    public static int Kickout_ChairmanKickout = 0;
    public static int Kickout_Repeat = 1;
    private static AsyncHttpClient client = new AsyncHttpClient();
    
    private int port = 80;
    private String host;
    private String serial;
    private String nickname;
    private String userid;
    private String password;
    private String param;
    private String domain;
    private String servername;
    private String path;
    private String finalnickname;
    
    private Activity activity;
    private int type = 3;
    
    public static String webServer = "global.talk-cloud.net";
    // public static String webServer = "global.talk-cloud.neiwang";
    //    public static String webServer = "demo.talk-cloud.net";
    
    public boolean isExit() {
        return isExit;
    }
    
    public void setExit(boolean exit) {
        isExit = exit;
    }
    
    private boolean isExit = false;
    
    static public RoomClient getInstance() {
        synchronized (RoomClient.class) {
            if (mInstance == null) {
                mInstance = new RoomClient();
            }
            return mInstance;
        }
    }
    
    public void joinRoom(Activity activity, Map<String, Object> map) {
        
        HashMap<String, Object> params = new HashMap<>();
        params.put("autoSubscribeAV", true);
        /*params.put("AudioSource", AudioManager.STREAM_MUSIC); //机灵宝宝*/
        TKRoomManager.getInstance().init(activity.getApplicationContext(), "talkcloud", params);
        TKRoomManager.getInstance().registerRoomObserver(RoomSession.getInstance());
        TKRoomManager.getInstance().registerMediaFrameObserver(RoomSession.getInstance());
        WBSession.getInstance().addobservers();
        
        this.activity = activity;
        checkRoom(activity, map);
    }
    
    public void joinRoom(Activity activity, String temp) {
        temp = Uri.decode(temp);
        
        String[] temps = temp.split("&");
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < temps.length; i++) {
            String[] t = temps[i].split("=");
            if (t.length > 1) {
                map.put(t[0], t[1]);
            }
        }
        if (map.containsKey("path")) {
            String tempPath = "http://" + map.get("path");
            map.put("path", tempPath);
        }
        joinRoom(activity, map);
    }
    
    public void joinPlayBackRoom(final Activity activity, String temp) {
        
        HashMap<String, Object> params = new HashMap<>();
        params.put("autoSubscribeAV", true);
        /*params.put("AudioSource", AudioManager.STREAM_MUSIC); //机灵宝宝*/
        TKRoomManager.getInstance().init(activity.getApplicationContext(), "talkplus", params);
        TKRoomManager.getInstance().registerRoomObserver(RoomSession.getInstance());
        TKRoomManager.getInstance().registerMediaFrameObserver(RoomSession.getInstance());
        WBSession.getInstance().addobservers();
        
        this.activity = activity;
        String[] temps = temp.split("&");
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < temps.length; i++) {
            String[] t = temps[i].split("=");
            if (t.length > 1) {
                map.put(t[0], t[1]);
            }
        }
        
        if (map.containsKey("path")) {
            String tempPath = "http://" + map.get("path");
            map.put("path", tempPath);
        }
        
        host = map.get("host") instanceof String ? (String) map.get("host") : "";
        serial = map.get("serial") instanceof String ? (String) map.get("serial") : "";
        nickname = map.get("nickname") instanceof String ? (String) map.get("nickname") : "";
        userid = map.get("userid") instanceof String ? (String) map.get("userid") : "";
        password = map.get("password") instanceof String ? (String) map.get("password") : "";
        param = map.get("param") instanceof String ? (String) map.get("param") : "";
        domain = map.get("domain") instanceof String ? (String) map.get("domain") : "";
        finalnickname = Uri.encode(nickname);
        path = map.get("path") instanceof String ? (String) map.get("path") : "";
        
        if (map.get("port") instanceof Integer) {
            port = (Integer) map.get("port");
        } else if (map.get("port") instanceof String) {
            boolean isNum = ((String) map.get("port")).matches("[0-9]+");
            if (isNum) {
                port = (Integer) map.get("port");
            }
        }
        
        if (map.containsKey("type")) {
            if (map.get("type") instanceof Integer) {
                type = (Integer) map.get("type");
            } else if (map.get("type") instanceof String) {
                boolean isNum = ((String) map.get("type")).matches("[0-9]+");
                if (isNum) {
                    type = Integer.parseInt(map.get("type") + "");
                }
            }
        }
        getmobilename(host, port);
        String url = path + "room.json";
        
        TKPlayBackManager.getInstance().getPlayBackRoomJson(url);
    }
    
    public void checkRoom(final Activity activity, Map<String, Object> map) {
        
        host = map.get("host") instanceof String ? (String) map.get("host") : "";
        serial = map.get("serial") instanceof String ? (String) map.get("serial") : "";
        nickname = map.get("nickname") instanceof String ? (String) map.get("nickname") : "";
        userid = map.get("userid") instanceof String ? (String) map.get("userid") : "";
        
        password = map.get("password") instanceof String ? (String) map.get("password") : "";
        param = map.get("param") instanceof String ? (String) map.get("param") : "";
        domain = map.get("domain") instanceof String ? (String) map.get("domain") : "";
        servername = map.get("servername") instanceof String ? (String) map.get("servername") : "";
        path = map.get("path") instanceof String ? (String) map.get("path") : "";
        
        String clientType = map.get("clientType") instanceof String ? (String) map.get("clientType") : "2";
        finalnickname = Uri.encode(nickname);
        
        int userrole = 2;
        if (map.get("userrole") instanceof Integer) {
            userrole = (Integer) map.get("userrole");
        } else if (map.get("userrole") instanceof String) {
            boolean isNum = ((String) map.get("userrole")).matches("[0-9]+");
            if (isNum) {
                userrole = (Integer) map.get("userrole");
            }
        }
        
        if (map.get("port") instanceof Integer) {
            port = (Integer) map.get("port");
        } else if (map.get("port") instanceof String) {
            boolean isNum = ((String) map.get("port")).matches("[0-9]+");
            if (isNum) {
                port = (Integer) map.get("port");
            }
        }
        
        getmobilename(host, port);
        
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!param.isEmpty()) {
            params.put("param", param);
        }
        
        params.put("userid", userid);
        params.put("password", password);
        params.put("serial", serial);
        params.put("userrole", userrole);
        params.put("nickname", nickname);
        params.put("volume", 100);
        
        //新加皮肤字段   1 PC   2 Android   3 IOS
        params.put("clientType", clientType);
        
        params.put("mobilenameOnList", RoomSession.mobilenameNotOnList);
        if (domain != null && !domain.isEmpty()) {
            params.put("domain", domain);
        }
        if (servername != null && !servername.isEmpty()) {
            params.put("servername", servername);
        }
        
        if (userrole == 2 && checkKickOut(serial)) {
            Toast.makeText(activity, activity.getString(R.string.kick_out), Toast.LENGTH_SHORT).show();
            callBack.callBack(100);
            return;
        }
        
        if (TextUtils.isEmpty(password) && userrole != 2) {
            callBack.callBack(4110);
        } else {
            
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            UiModeManager uiModeManager = (UiModeManager) activity.getSystemService(Context.UI_MODE_SERVICE);
            if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
                hashMap.put("devicetype", "AndroidTV");
            } else {
                if (Tools.isPad(activity)) {
                    hashMap.put("devicetype", "AndroidPad");
                } else {
                    hashMap.put("devicetype", "AndroidPhone");
                }
            }
            TKRoomManager.getInstance().joinRoom(host, port, finalnickname, params, hashMap);
        }
    }
    
    private boolean checkKickOut(String meetingid) {
        SharedPreferences preferences = activity.getSharedPreferences("KickOutPersonInfo", Context.MODE_PRIVATE);
        String roomNumber = preferences.getString("RoomNumber", null);
        Long time = preferences.getLong("Time", 0);
        if (!TextUtils.isEmpty(roomNumber) && roomNumber.equals(meetingid)) {
            if (System.currentTimeMillis() - time <= 60 * 3 * 1000) {
                return true;
            }
        }
        return false;
    }
    
    String apkDownLoadUrl = "";
    
    public void checkForUpdate(final Activity activity, String url, final CheckForUpdateCallBack ack) {
        url = "http://" + url + ":" + 80 + "/ClientAPI/getupdateinfo";
        
        RequestParams params = new RequestParams();
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            params.put("type", 9);
            params.put("version", "2018111400");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                try {
                    final int nRet = response.getInt("result");
                    if (nRet == 0) {
                        apkDownLoadUrl = response.optString("updateaddr");
                        final int code = response.optInt("updateflag");
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ack.callBack(code);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("emm", "error");
            }
        });
    }
    
    String target;
    
    public void downLoadApp(final Activity activity) {
        target = Environment.getExternalStorageDirectory().getAbsolutePath() + "/talkcloudHD.apk";
        if (!apkDownLoadUrl.contains("http://")) {
            apkDownLoadUrl = "http://" + apkDownLoadUrl;
        }
        if (apkDownLoadUrl != null && !apkDownLoadUrl.isEmpty()) {
            HttpUtils http = new HttpUtils();
            http.download(apkDownLoadUrl, target, new RequestCallBack<File>() {
                
                @Override
                public void onFailure(HttpException exception, String msg) {
                
                }
                
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    dialog.dismiss();
                    // 安装apk
                    installApk(activity);
                }
                
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    initProgressDialog(activity, total, current);
                }
            });
        }
    }
    
    protected void initProgressDialog(Activity activity, long total, long current) {
        
        if (activity.isFinishing()) {
            return;
        }
        
        if (dialog == null) {
            dialog = new ProgressDialog(activity);
        }
        dialog.setTitle(activity.getString(R.string.updateing));//设置标题
        dialog.setMessage("");//设置dialog内容
        //        dialog.setIcon(R.drawable.icon_word);//设置图标，与为Title左侧
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 水平线进度条
        // dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//圆形进度条
        dialog.setMax((int) total);//最大值
        dialog.setProgress((int) current);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    
    private ProgressDialog dialog;
    
    protected void installApk(Activity activity) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            File file = new File(target);
            Uri apkUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", file);//在AndroidManifest中的android:authorities值
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            activity.startActivity(intent);
        } else {
            Uri data = Uri.parse("file://" + target);
            intent.setDataAndType(data, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }
    
    public void regiestInterface(MeetingNotify notify, com.eduhdsdk.interfaces.JoinmeetingCallBack callBack) {
        this.notify = notify;
        this.callBack = callBack;
    }
    
    public void checkForCrashes(final Activity activity, String HOCKEY_APP_HASH) {
        
        CrashManagerListener listener = new CrashManagerListener() {
            @Override
            public boolean shouldAutoUploadCrashes() {
                return true;
            }
            
            @Override
            public void onNewCrashesFound() {
                super.onNewCrashesFound();
                
                final Dialog dialog = new Dialog(activity);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(R.color.nothing);
                LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = li.inflate(R.layout.layout_crash_dialog, null);
                
                v.findViewById(R.id.bt_cancel_crash_message).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
                
                v.findViewById(R.id.bt_send_crash_message).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
                
                dialog.setContentView(v);
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.CENTER);
                
                lp.width = KeyBoardUtil.dp2px(activity, 300f); // 宽度
                lp.height = KeyBoardUtil.dp2px(activity, 160f); // 高度
                
                dialogWindow.setAttributes(lp);
                dialog.show();
            }
        };
        CrashManager.register(activity, "http://global.talk-cloud.com/update/public/", HOCKEY_APP_HASH, listener);
    }
    
    public void setNotify(MeetingNotify notify) {
        this.notify = notify;
    }
    
    public void setCallBack(com.eduhdsdk.interfaces.JoinmeetingCallBack callBack) {
        this.callBack = callBack;
    }
    
    public void joinRoomcallBack(int code) {
        if (this.callBack != null) {
            if (code == 0) {
                Intent intent = null;
                int role = TKRoomManager.getInstance().getMySelf().role;
                setRoomSession(role);
                JSONObject jsonRoomInfo = TKRoomManager.getInstance().getRoomProperties();
                if (jsonRoomInfo != null) {
                    int roomType = jsonRoomInfo.optInt("roomtype");
                    
                    String chairmancontrol = jsonRoomInfo.optString("chairmancontrol");
                    if (chairmancontrol != null && !chairmancontrol.isEmpty()) {
                        RoomControler.chairmanControl = chairmancontrol;
                    }
                    
                    //                    if (roomType == 0 && !RoomControler.isShowAssistantAV()) {
                    //                        intent = new Intent(activity, OneToOneThreeActivity.class);
                    //                    } else {
                    //                        intent = new Intent(activity, DemoActivity.class);
                    //                    }
                    //                    if (intent != null) {
                    //                        activity.startActivity(intent);
                    //                    }
                }
            }
            callBack.callBack(code);
        }
    }
    
    public void onPlayBackRoomJson(int code, String response) {
        if (this.callBack != null) {
            Intent intent = null;
            int roomType = 0;
            if (code == 0) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        if (jsonObject.has("room")) {
                            JSONObject room = jsonObject.optJSONObject("room");
                            int role = room.optInt("roomrole", -1);
                            userid = room.optString("userid", userid);
                            serial = room.optString("serial", serial);
                            roomType = room.optInt("roomtype");
                            String chairmancontrol = room.optString("chairmancontrol");
                            if (chairmancontrol != null && !chairmancontrol.isEmpty()) {
                                RoomControler.chairmanControl = chairmancontrol;
                            }
                            setRoomSession(role);
                            joinPlayBackRoom();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //                if (roomType == 0 && !RoomControler.isShowAssistantAV()) {
                //                    intent = new Intent(activity, OneToOneThreeActivity.class);
                //                } else {
                //                    intent = new Intent(activity, DemoActivity.class);
                //                }
                //                if (intent != null) {
                //                    activity.startActivity(intent);
                //                }
            }
            callBack.callBack(code);
        }
    }
    
    private void joinPlayBackRoom() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (param != null && !param.isEmpty()) {
            params.put("param", param);
        }
        if (domain != null && !domain.isEmpty()) {
            params.put("domain", domain);
        }
        if (finalnickname != null && !finalnickname.isEmpty()) {
            params.put("servername", finalnickname);
        }
        if (path != null && !path.isEmpty()) {
            params.put("playback", true);
        }
        if (!TextUtils.isEmpty(path)) {
            params.put("path", path);
        }
        if (type != -1) {
            params.put("type", type);
        }
        params.put("password", RoomSession.password);
        params.put("nickname", RoomSession.nickname);
        params.put("volume", 100);
        params.put("mobilenameOnList", RoomSession.mobilenameNotOnList);
        params.put("serial", RoomSession.serial);
        params.put("userrole", RoomSession.userrole);
        RoomSession.params = params;
        
        TKPlayBackManager.getInstance().joinPlayBackRoom(RoomSession.host, RoomSession.port,
            RoomSession.nickname, RoomSession.params, new HashMap<String, Object>());
    }
    
    private void setRoomSession(int role) {
        RoomSession.host = host;
        RoomSession.port = port;
        RoomSession.nickname = finalnickname;
        if (param != null && !param.isEmpty()) {
            RoomSession.param = param;
        }
        RoomSession.serial = serial;
        RoomSession.password = password;
        RoomSession.userid = userid;
        RoomSession.userrole = role;
        if (domain != null && !domain.isEmpty()) {
            RoomSession.domain = domain;
        }
        if (path != null && !path.isEmpty()) {
            RoomSession.path = path;
        }
        if (servername != null && !servername.isEmpty()) {
            RoomSession.servername = servername;
        }
    }
    
    private void getmobilename(String host, int port) {
        String url = "http://" + host + ":" + port + "/ClientAPI/getmobilename";
        RequestParams params = new RequestParams();
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                try {
                    int nRet = response.getInt("result");
                    if (nRet == 0) {
                        String mobilename = response.optJSONArray("mobilename").toString();
                        RoomSession.mobilename = mobilename;
                        try {
                            String brand = Build.MODEL;
                            if (mobilename != null && !mobilename.isEmpty()) {
                                JSONArray mNames = new JSONArray(mobilename);
                                for (int i = 0; i < mNames.length(); i++) {
                                    if (brand.toLowerCase().equals(mNames.optString(i).toLowerCase())) {
                                        RoomSession.mobilenameNotOnList = false;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("emm", "error");
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
    
    public void kickout(int res) {
        if (notify != null) {
            notify.onKickOut(res);
        }
    }
    
    /***
     * 警告权限
     * @param code 1没有视频权限2没有音频权限
     */
    public void warning(int code) {
        if (notify != null) {
            notify.onWarning(code);
        }
    }
    
    public void onClassBegin() {
        if (notify != null) {
            notify.onClassBegin();
        }
    }
    
    public void onClassDismiss() {
        if (notify != null) {
            notify.onClassDismiss();
        }
    }
}
