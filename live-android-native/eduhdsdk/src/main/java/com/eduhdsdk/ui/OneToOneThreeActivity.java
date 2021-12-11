package com.eduhdsdk.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.classroomsdk.NotificationCenter;
import com.classroomsdk.WBFragment;
import com.classroomsdk.WBSession;
import com.classroomsdk.WhiteBoradConfig;
import com.classroomsdk.bean.ShareDoc;
import com.classroomsdk.interfaces.IWBStateCallBack;
import com.eduhdsdk.R;
import com.eduhdsdk.adapter.ThreeChatListAdapter;
import com.eduhdsdk.adapter.ThreeFileListAdapter;
import com.eduhdsdk.adapter.ThreeMediaListAdapter;
import com.eduhdsdk.adapter.ThreeMemberListAdapter;
import com.eduhdsdk.entity.ChatData;
import com.eduhdsdk.entity.TimeMessage;
import com.eduhdsdk.entity.Trophy;
import com.eduhdsdk.entity.VideoItem;
import com.eduhdsdk.message.RoomClient;
import com.eduhdsdk.message.RoomControler;
import com.eduhdsdk.message.RoomSession;
import com.eduhdsdk.tools.CircleView;
import com.eduhdsdk.tools.FrameAnimation;
import com.eduhdsdk.tools.FullScreenTools;
import com.eduhdsdk.tools.KeyBoardUtil;
import com.eduhdsdk.tools.PermissionTest;
import com.eduhdsdk.tools.PersonInfo_ImageUtils;
import com.eduhdsdk.tools.PhotoUtils;
import com.eduhdsdk.tools.ScreenScale;
import com.eduhdsdk.tools.SoundPlayUtils;
import com.eduhdsdk.tools.Tools;
import com.eduhdsdk.tools.Translate;
import com.eduhdsdk.tools.VolumeView;
import com.eduhdsdk.viewutils.AccessResourcesUtil;
import com.eduhdsdk.viewutils.PlaySpeedPopupWindowUtils;
import com.eduhdsdk.viewutils.ThreeChatWindowPop;
import com.eduhdsdk.viewutils.ThreeCoursePopupWindowUtils;
import com.eduhdsdk.viewutils.ThreeMemberListPopupWindowUtils;
import com.eduhdsdk.viewutils.ThreeSendGiftPopUtils;
import com.talkcloud.room.RoomUser;
import com.talkcloud.room.TKPlayBackManager;
import com.talkcloud.room.TKRoomManager;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.tkwebrtc.EglBase;
import org.tkwebrtc.RendererCommon;
import org.tkwebrtc.SurfaceViewRenderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class OneToOneThreeActivity extends Activity implements NotificationCenter.NotificationCenterDelegate,
    View.OnClickListener, IWBStateCallBack, CompoundButton.OnCheckedChangeListener,
    ThreeCoursePopupWindowUtils.PopupWindowClick, ThreeChatWindowPop.ChatPopupWindowClick,
    ThreeSendGiftPopUtils.SendGift, ThreeMemberListPopupWindowUtils.CloseMemberListWindow {
    
    private ArrayList<TimeMessage> timeMessages = new ArrayList<TimeMessage>();
    private NotificationManager nm = null;
    private NotificationCompat.Builder mBuilder = null;
    
    private VideoFragment videofragment;
    private Map<String, Object> onUpdateAttributeAttrs;
    //老师视频
    private LinearLayout vdi_teacher;
    //学生视频
    private LinearLayout vdi_stu_in_sd;
    
    private MovieFragment movieFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    private ScreenFragment screenFragment;
    
    private VideoItem teacherItem;
    private VideoItem stu_in_sd;
    
    private View three_view_one;
    
    //MP3  mp4
    private LinearLayout lin_audio_control;
    private ImageView img_play_mp3, img_voice_mp3;
    private TextView txt_mp3_name, txt_mp3_time;
    private SeekBar sek_mp3, sek_voice_mp3;
    private LinearLayout ll_close_mp3;
    private GifImageView img_disk;
    private GifDrawable gifDrawable;
    private Map<String, Object> mediaAttrs;
    private String updateAttributespeerId;
    
    private double vol = 0.5;
    
    private String mediaPeerId;
    long starttime;
    long endtime;
    long currenttime;
    private String mp3Duration = "00:00";
    
    private static boolean isMediaMute = false;
    private boolean isZoom = false;
    private boolean isPlayBackPlay = true;
    private boolean isEnd = false;
    private boolean isBackApp = false;
    private boolean isOpenCamera = false;
    private boolean canClassDissMiss = false;
    private boolean isFrontCamera = true;
    private boolean isPauseLocalVideo = false;
    private boolean isOneToOne = true;
    private double postionPlayBack = 0.0;
    
    //顶部导航栏
    private RelativeLayout rel_tool_bar;
    private ImageView img_back, flipCamera;
    private TextView txt_class_begin, txt_hand_up;
    private TextView txt_hour, txt_min, txt_ss;
    private TextView txt_mao_01, txt_mao_02;
    private CheckBox cb_member_list, cb_file_person_media_list, cb_tool_case, cb_control, cb_message;
    private LinearLayout lin_time, ll_top, re_top_right;
    
    //load
    private RelativeLayout re_loading;
    private ImageView loadingImageView;
    private TextView tv_load;
    
    private RelativeLayout rl_web;
    //播放控件
    private FrameLayout video_container;
    
    
    //回放使用的控件
    private LinearLayout rel_play_back, ll_voice;
    private ImageView img_play_back, img_back_play_voice;
    private SeekBar sek_play_back, sek_back_play_voice;
    private TextView txt_play_back_time, tv_play_speed, tv_back_name;
    private RelativeLayout rel_play_back_bar, re_play_back, re_back;
    
    
    //白板全屏右下角视频界面
    private RelativeLayout rel_fullscreen_videoitem;
    private SurfaceViewRenderer fullscreen_sf_video;
    private ImageView fullscreen_bg_video_back, fullscreen_img_video_back;
    
    private RelativeLayout ll_wb_container;
    private LinearLayout ll_video_whiteboard;
    
    private LinearLayout lin_menu;
    private FrameLayout wb_container;
    
    private FrameAnimation frameAnimation;
    private PowerManager pm;
    private PowerManager.WakeLock mWakeLock;
    private PopupWindow studentPopupWindow;
    private AudioManager audioManager;
    private Animation operatingAnim;
    private PopupWindow popupWindowPhoto;
    
    //课件
    private ThreeCoursePopupWindowUtils coursePopupWindowUtils;
    //花名册
    private ThreeMemberListPopupWindowUtils memberListPopupWindowUtils;
    //聊天
    private ThreeChatWindowPop chatUtils;
    
    private PlaySpeedPopupWindowUtils playSpeedPopupWindowUtils;
    //自定义奖杯
    private ThreeSendGiftPopUtils sendGiftPopUtils;
    
    //数据
    private ThreeMemberListAdapter memberListAdapter;
    private ThreeFileListAdapter fileListAdapter;
    private ThreeMediaListAdapter mediaListAdapter;
    private ThreeChatListAdapter chlistAdapter;
    private WBFragment wbFragment;
    private boolean huawei, oppo, voio;
    
    private RelativeLayout rl_message, rl_member_list;
    //消息提示小红点
    private TextView tv_no_read_message_number;
    //花名册小红点
    private ImageView iv_hand;
    private RelativeLayout.LayoutParams fullscreen_video_param;
    
    int webandsufwidth;
    
    private CheckBox cb_choose_photo;
    
    int wid_ratio = 4;
    int hid_ratio = 3;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        View view = LayoutInflater.from(this).inflate(R.layout.three_activity_one_to_one, null, false);
        ScreenScale.scaleView(view, "OneToOneThreeActivity  ----    onCreate");
        setContentView(view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        initTimeMessage();
        resultActivityBackApp();
        initVideoItem();
        initview();
        initData();
    }
    
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_back || id == R.id.re_back) {
            showExitDialog();
        } else if (id == R.id.txt_class_begin) {
            if (RoomSession.isClassBegin) {
                try {
                    if (canClassDissMiss || !TKRoomManager.getInstance().getRoomProperties().getString("companyid").equals("10035")) {
                        txt_class_begin.setSelected(false);
                        showClassDissMissDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (RoomControler.haveTimeQuitClassroomAfterClass()) {
                    RoomSession.getInstance().getSystemTime();
                } else {
                    txt_class_begin.setSelected(true);
                    RoomSession.getInstance().startClass();
                }
            }
        } else if (id == R.id.flip_camera) {
            if (TKRoomManager.getInstance().getMySelf().hasVideo) {
                if (isFrontCamera) {
                    TKRoomManager.getInstance().selectCameraPosition(isFrontCamera);
                    isFrontCamera = false;
                } else {
                    TKRoomManager.getInstance().selectCameraPosition(isFrontCamera);
                    isFrontCamera = true;
                }
            } else {
                Toast.makeText(OneToOneThreeActivity.this, getString(R.string.tips_camera), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.tv_play_speed) {    //回放倍速
            playSpeedPopupWindowUtils.showPlaySpeedPopupWindow(tv_play_speed, 100, 280);
        }
    }
    
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int wid = dm.widthPixels;
        int id = buttonView.getId();
        boolean isScale;
        if (id == R.id.cb_file_person_media_list) {   //文件列表
            if (isChecked) {
                cb_file_person_media_list.setEnabled(false);
                if (cb_tool_case.isChecked()) {
                    cb_tool_case.setChecked(false);
                }
                if (scale == 0) {
                    isScale = true;
                } else {
                    isScale = false;
                }
                if (huawei || voio || oppo) {
                    coursePopupWindowUtils.showCoursePopupWindow(ll_wb_container, cb_file_person_media_list, (wid - teacherItem.parent.getLayoutParams().width) / 10 * 9, ll_wb_container.getHeight() * 9 / 10, true, webandsufwidth, isScale);
                } else {
                    coursePopupWindowUtils.showCoursePopupWindow(ll_wb_container, cb_file_person_media_list, (wid - teacherItem.parent.getLayoutParams().width) / 10 * 9, ll_wb_container.getHeight() * 9 / 10, false, webandsufwidth, isScale);
                }
            } else {
                coursePopupWindowUtils.dismissPopupWindow();
            }
        } else if (id == R.id.cb_member_list) {    //  花名册
            iv_hand.setVisibility(View.INVISIBLE);
            if (isChecked) {
                cb_member_list.setEnabled(false);
                if (huawei || voio || oppo) {
                    memberListPopupWindowUtils.showMemberListPopupWindow(ll_wb_container, rl_member_list, ll_wb_container.getWidth() / 10 * 7, ll_wb_container.getHeight() * 9 / 10, true);
                } else {
                    memberListPopupWindowUtils.showMemberListPopupWindow(ll_wb_container, rl_member_list, ll_wb_container.getWidth() / 10 * 7, ll_wb_container.getHeight() * 9 / 10, false);
                }
                if (cb_tool_case.isChecked()) {
                    cb_tool_case.setChecked(false);
                }
            } else {
                memberListPopupWindowUtils.dismissPopupWindow();
            }
            
        } else if (id == R.id.cb_tool_case) {  //工具箱
            WhiteBoradConfig.getsInstance().showToolbox(isChecked);
        } else if (id == R.id.cb_message) {  //  聊天列表
            if (isChecked) {
                cb_message.setEnabled(false);
                if (cb_tool_case.isChecked()) {
                    cb_tool_case.setChecked(false);
                }
                clearNoReadChatMessage();
                if (huawei || oppo || voio) {
                    chatUtils.showChatPopupWindow(ll_wb_container.getWidth() * 9 / 10, ll_wb_container.getHeight() * 9 / 10, ll_wb_container, rl_message, webandsufwidth, true);
                } else {
                    chatUtils.showChatPopupWindow(ll_wb_container.getWidth() * 9 / 10, ll_wb_container.getHeight() * 9 / 10, ll_wb_container, rl_message, webandsufwidth, false);
                }
                
                if (TKRoomManager.getInstance().getMySelf().role == 0 && RoomSession.isClassBegin) {
                    TKRoomManager.getInstance().pubMsg("ChatShow", "ChatShow", "__allExceptSender",
                        null, true, null, null);
                }
                
            } else {
                chatUtils.dismissPopupWindow();
            }
        } else if (id == R.id.cb_choose_photo) {
            if (isChecked) {
                showPhotoControlPop();
            } else {
                if (popupWindowPhoto != null) {
                    popupWindowPhoto.dismiss();
                }
            }
        }
    }
    
    @Override
    public void didReceivedNotification(final int id, final Object... args) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (id) {
                    case RoomSession.onRoomJoin:
                        onRoomJoin();
                        break;
                    
                    case RoomSession.onRoomLeave:
                        onRoomLeave();
                        break;
                    
                    case RoomSession.onConnectionLost:
                        onConnectionLost();
                        break;
                    
                    case RoomSession.onError:
                        int errorCode = (int) args[0];
                        String errMsg = (String) args[1];
                        onError(errorCode, errMsg);
                        break;
                    
                    case RoomSession.onWarning:
                        int onWarning = (int) args[0];
                        onWarning(onWarning);
                        break;
                    
                    case RoomSession.onUserJoin:
                        RoomUser user = (RoomUser) args[0];
                        boolean inList = (boolean) args[1];
                        onUserJoined(user, inList);
                        break;
                    
                    case RoomSession.onUserLeft:
                        RoomUser roomUser = (RoomUser) args[0];
                        onUserLeft(roomUser);
                        break;
                    
                    case RoomSession.onUserPropertyChanged:
                        RoomUser propertyUser = (RoomUser) args[0];
                        Map<String, Object> map = (Map<String, Object>) args[1];
                        String fromId = (String) args[2];
                        onUserPropertyChanged(propertyUser, map, fromId);
                        break;
                    
                    case RoomSession.onUserVideoStatus:
                        String peerId = (String) args[0];
                        int state = (int) args[1];
                        onUserVideoStatus(peerId, state);
                        break;
                    
                    case RoomSession.onKickedout:
                        break;
                    
                    case RoomSession.onMessageReceived:
                        RoomUser chatUser = (RoomUser) args[0];
                        onMessageReceived(chatUser);
                        break;
                    
                    case RoomSession.onRemotePubMsg:
                        String namePub = (String) args[1];
                        long pubMsgTS = (long) args[2];
                        Object dataPub = (Object) args[3];
                        boolean inListPub = (boolean) args[4];
                        onRemotePubMsg(namePub, pubMsgTS, dataPub, inListPub);
                        break;
                    
                    case RoomSession.onRemoteDelMsg:
                        String nameDel = (String) args[1];
                        long delMsgTS = (long) args[2];
                        onRemoteDelMsg(nameDel, delMsgTS);
                        break;
                    
                    case RoomSession.onUpdateAttributeStream:
                        String attributesPeerId = (String) args[0];
                        long streamPos = (long) args[1];
                        Boolean isPlay = (Boolean) args[2];
                        Map<String, Object> dateAttributeAttrs = (Map<String, Object>) args[3];
                        onUpdateAttributeStream(attributesPeerId, streamPos, isPlay, dateAttributeAttrs);
                        break;
                    
                    case RoomSession.onPlayBackUpdateTime:
                        long backTimePos = (long) args[0];
                        onPlayBackUpdateTime(backTimePos);
                        break;
                    
                    case RoomSession.onPlayBackClearAll:
                        onPlayBackClearAll();
                        break;
                    
                    case RoomSession.onPlayBackDuration:
                        long startTime = (long) args[0];
                        long endTime = (long) args[1];
                        onPlayBackDuration(startTime, endTime);
                        break;
                    
                    case RoomSession.onPlayBackEnd:
                        onPlayBackEnd();
                        break;
                    
                    case RoomSession.onShareMediaState: //MP4 MP3
                        String shareMediaPeerId = (String) args[0];
                        int shareMediaState = (int) args[1];
                        Map<String, Object> shareMediaAttrs = (Map<String, Object>) args[2];
                        onShareMediaState(shareMediaPeerId, shareMediaState, shareMediaAttrs);
                        break;
                    
                    case RoomSession.onShareScreenState:
                        String peerIdScreen = (String) args[0];
                        int stateScreen = (int) args[1];
                        onShareScreenState(peerIdScreen, stateScreen);
                        break;
                    
                    case RoomSession.onShareFileState:
                        String peerIdShareFile = (String) args[0];
                        int stateShareFile = (int) args[1];
                        onShareFileState(peerIdShareFile, stateShareFile);
                        break;
                    
                    case RoomSession.onAudioVolume:
                        String volumePeerId = (String) args[0];
                        int volume = (int) args[1];
                        onAudioVolume(volumePeerId, volume);
                        break;
                    
                    case RoomSession.onFirstVideoFrame:
                        String peerIdVideo = (String) args[0];
                        onFirstVideoFrame(peerIdVideo);
                        break;
                    
                    case RoomSession.onUserAudioStatus:
                        String userIdAudio = (String) args[0];
                        int statusAudio = (int) args[1];
                        onUserAudioStatus(userIdAudio, statusAudio);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    
    private void onUserAudioStatus(String userIdAudio, int statusAudio) {
        if (statusAudio > 0) {
            if (RoomControler.isOnlyShowTeachersAndVideos()) {
                if (TKRoomManager.getInstance().getMySelf().role == 2) {
                    RoomUser roomUser = TKRoomManager.getInstance().getUser(userIdAudio);
                    if (userIdAudio.equals(TKRoomManager.getInstance().getMySelf().peerId) || roomUser.role == 0) {
                        doPlayAudio(userIdAudio);
                    } else {
                        TKRoomManager.getInstance().playAudio(userIdAudio);
                    }
                } else {
                    doPlayAudio(userIdAudio);
                }
            } else {
                doPlayAudio(userIdAudio);
            }
        } else {
            doUnPlayAudio(userIdAudio);
           /* if (!RoomSession.isClassBegin && TKRoomManager.getInstance().getMySelf().role == 0 &&
                    TKRoomManager.getInstance().getUser(userIdAudio).role == 0 &&
                    TKRoomManager.getInstance().getMySelf().peerId.equals(userIdAudio)) {
                playSelfBeforeClassBegin();
            }*/
        }
        /*changeUserState(TKRoomManager.getInstance().getUser(userIdAudio));*/
        memberListAdapter.notifyDataSetChanged();
        if (studentPopupWindow != null) {
            studentPopupWindow.dismiss();
        }
    }
    
    private void doUnPlayAudio(String audioUserId) {
        RoomUser roomUser = TKRoomManager.getInstance().getUser(audioUserId);
        if (roomUser != null) {
            return;
        }
        Integer publishState = (Integer) roomUser.properties.get("publishstate") != null ?
            (Integer) roomUser.properties.get("publishstate") : 0;
        
        if (roomUser.role == 0) {//老师
            if (roomUser.hasAudio) {
                teacherItem.img_mic.setVisibility(View.INVISIBLE);
            } else {
                teacherItem.img_mic.setVisibility(View.VISIBLE);
                teacherItem.img_mic.setImageResource(R.drawable.three_img_mic_ban);
            }
            if (publishState == 0) {
                teacherItem.sf_video.setVisibility(View.INVISIBLE);
                teacherItem.bg_video_back.setVisibility(View.VISIBLE);
                teacherItem.img_pen.setVisibility(View.INVISIBLE);
                teacherItem.cv_draw.setVisibility(View.INVISIBLE);
                teacherItem.lin_name_label.setVisibility(View.INVISIBLE);
            }
        /*fullscreen_sf_video.setVisibility(View.INVISIBLE);
        fullscreen_bg_video_back.setVisibility(View.VISIBLE);
        fullscreen_img_video_back.setVisibility(View.VISIBLE);*/
        
        } else if (roomUser.role == 2) {//学生
            
            if (roomUser.hasAudio) {
                stu_in_sd.img_mic.setVisibility(View.INVISIBLE);
            } else {
                stu_in_sd.img_mic.setVisibility(View.VISIBLE);
                stu_in_sd.img_mic.setImageResource(R.drawable.three_img_mic_ban);
            }
            if (publishState == 0) {
                stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
                stu_in_sd.bg_video_back.setVisibility(View.VISIBLE);
                stu_in_sd.lin_name_label.setVisibility(View.INVISIBLE);
                stu_in_sd.img_pen.setVisibility(View.INVISIBLE);
                stu_in_sd.cv_draw.setVisibility(View.INVISIBLE);
            }
        }
    }
    
    private void doPlayAudio(String audioUserId) {
        
        if (TextUtils.isEmpty(audioUserId)) {
            return;
        }
        
        final RoomUser user = TKRoomManager.getInstance().getUser(audioUserId);
        if (user == null) {
            return;
        }
        
        if (user.role == 0) {     //老师
            teacherItem.txt_name.setText(user.nickName);
            teacherItem.rel_group.setVisibility(View.VISIBLE);
            teacherItem.lin_name_label.setVisibility(View.VISIBLE);
            if (!user.disableaudio && user.hasAudio) {
                teacherItem.img_mic.setVisibility(View.VISIBLE);
                TKRoomManager.getInstance().playAudio(user.peerId);
            } else {
                teacherItem.img_mic.setVisibility(View.INVISIBLE);
            }
        } else if (user.role == 2) {    //学生
            
            if (user.hasAudio) {
                TKRoomManager.getInstance().playAudio(user.peerId);
            }
            
            if (!isZoom) {
                stu_in_sd.rel_group.setVisibility(View.VISIBLE);
                stu_in_sd.lin_gift.setVisibility(View.VISIBLE);
                stu_in_sd.txt_name.setText(user.nickName);
                
                if (user.hasAudio && RoomSession.isClassBegin) {
                    stu_in_sd.img_mic.setVisibility(View.VISIBLE);
                    stu_in_sd.img_mic.setImageResource(R.drawable.three_img_mic_ban);
                } else {
                    stu_in_sd.img_mic.setVisibility(View.GONE);
                }
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri;
        switch (requestCode) {
            case PhotoUtils.PHOTO_REQUEST_CAREMA:
                //                isBackApp = false;
                if (isPauseLocalVideo) {
                    TKRoomManager.getInstance().resumeLocalCamera();
                    isPauseLocalVideo = !isPauseLocalVideo;
                }
                isOpenCamera = false;
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        uri = data.getData();
                    } else {
                        uri = PhotoUtils.imageUri;
                    }
                    if (!TextUtils.isEmpty(uri.toString())) {
                        try {
                            String path = PersonInfo_ImageUtils.scaleAndSaveImage(PersonInfo_ImageUtils.getRealFilePath(this,
                                PersonInfo_ImageUtils.getFileUri(uri, this)), 800, 800, this);
                            WhiteBoradConfig.getsInstance().uploadRoomFile(
                                TKRoomManager.getInstance().getRoomProperties().getString("serial"), path, RoomSession.isClassBegin);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case PhotoUtils.ALBUM_IMAGE:
                //                isBackApp = false;
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    try {
                        if (Build.VERSION.SDK_INT >= 19) {
                            imagePath = PersonInfo_ImageUtils.getImageAfterKitKat(data, this);
                        } else {
                            imagePath = PersonInfo_ImageUtils.getImageBeforeKitKat(data, this);
                        }
                        if (!TextUtils.isEmpty(imagePath)) {
                            String path = PersonInfo_ImageUtils.scaleAndSaveImage(imagePath, 800, 800, this);
                            WhiteBoradConfig.getsInstance().uploadRoomFile(
                                TKRoomManager.getInstance().getRoomProperties().getString("serial"), path, RoomSession.isClassBegin);
                        } else {
                            Toast.makeText(this, "图片选择失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    
    @Override
    public void onRoomDocChange(boolean isdel, boolean ismedia) {
        if (RoomControler.isDocumentClassification()) {
            WhiteBoradConfig.getsInstance().getClassDocList();
            WhiteBoradConfig.getsInstance().getAdminDocList();
            WhiteBoradConfig.getsInstance().getClassMediaList();
            WhiteBoradConfig.getsInstance().getAdminmMediaList();
        } else {
            WhiteBoradConfig.getsInstance().getDocList();
            WhiteBoradConfig.getsInstance().getMediaList();
        }
        fileListAdapter.notifyDataSetChanged();
        mediaListAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void onWhiteBoradZoom(final boolean isZoom) {
        this.isZoom = isZoom;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isZoom) {
                    setWhiteBoradEnlarge(isZoom);
                    
                    if (RoomControler.isFullScreenVideo() && RoomSession.isClassBegin && TKRoomManager.getInstance().getMySelf().role == 0) {
                        JSONObject data = new JSONObject();
                        try {
                            data.put("fullScreenType", "courseware_file");
                            data.put("needPictureInPictureSmall", true);
                            TKRoomManager.getInstance().pubMsg("FullScreen", "FullScreen", "__all", data.toString(), true, "ClassBegin", null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    setWhiteBoradNarrow(isZoom);
                    
                    if (RoomControler.isFullScreenVideo() && RoomSession.isClassBegin && TKRoomManager.getInstance().getMySelf().role == 0) {
                        JSONObject data = new JSONObject();
                        try {
                            data.put("fullScreenType", "courseware_file");
                            data.put("needPictureInPictureSmall", false);
                            TKRoomManager.getInstance().delMsg("FullScreen", "FullScreen", "__all", data.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
    
    private String url;
    private boolean isvideo;
    private long fileid;
    private boolean isWBMediaPlay = false;
    
    @Override
    public void onWhiteBoradMediaPublish(String url, boolean isvideo, long fileid) {
        this.url = url;
        this.isvideo = isvideo;
        this.fileid = fileid;
        isWBMediaPlay = true;
    }
    
    /***
     *  JS调用我们的方法（改变按钮的状态）
     */
    int dolayoutsum4 = 0;
    int dolayoutsum16 = 0;
    int scale = 0;
    int total_page_num = 1;
    int currentNumber = 1;
    
    @Override
    public void onWhiteBoradAction(final String stateJson) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(stateJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jsonObject.has("toolbox")) {
                        Boolean toolbox = jsonObject.optBoolean("toolbox"); //工具箱状态
                        //设置工具箱
                        if (toolbox) {
                            cb_tool_case.setChecked(true);
                        } else {
                            cb_tool_case.setChecked(false);
                        }
                    }
                    
                    if (jsonObject.has("scale")) {
                        scale = jsonObject.optInt("scale");
                    }
                    
                    
                    //如果是第一次进入遇到 4:3  先执行一次  下次遇到不执行，并恢复16:9 下次可进  ，同理相反
                    if (scale == 0) {
                        wid_ratio = 4;
                        hid_ratio = 3;
                        if (dolayoutsum4 == 0) {
                            dolayoutsum4++; //1
                        }
                    } else {
                        wid_ratio = 16;
                        hid_ratio = 9;
                        if (dolayoutsum16 == 0) {
                            dolayoutsum16++;
                        }
                    }
                    
                    if (dolayoutsum4 == 1) {
                        dolayoutsum4++;  //2
                        doLayout();
                        dolayoutsum16 = 0;
                    }
                    if (dolayoutsum16 == 1) {
                        dolayoutsum16++;
                        doLayout();
                        dolayoutsum4 = 0;
                    }
                    
                    JSONObject pagejson = jsonObject.optJSONObject("page");
                    if (pagejson != null) {
                        int current = pagejson.optInt("currentPage");   //当前页数
                        int totalPage = pagejson.optInt("totalPage");  //总页数
                        
                        if (TKRoomManager.getInstance().getMySelf().role == 0 && current != 0) {
                            WhiteBoradConfig.getsInstance().getCurrentFileDoc().setCurrentPage(current);
                        }
                        ShareDoc shareDoc = WhiteBoradConfig.getsInstance().getCurrentFileDoc();
                        if (shareDoc != null) {
                            if (RoomControler.isDocumentClassification()) {
                                ArrayList<ShareDoc> classList = WhiteBoradConfig.getsInstance().getClassDocList();
                                ArrayList<ShareDoc> adminList = WhiteBoradConfig.getsInstance().getAdminDocList();
                                if (classList.contains(shareDoc)) {
                                    for (int x = 0; x < classList.size(); x++) {
                                        if (classList.get(x) != null) {
                                            if (classList.get(x).getFileid() == shareDoc.getFileid()) {
                                                classList.get(x).setPagenum(totalPage);
                                                classList.get(x).setCurrentPage(current);
                                                classList.get(x).setPptslide(current);
                                            }
                                        }
                                    }
                                } else {
                                    for (int x = 0; x < adminList.size(); x++) {
                                        if (adminList.get(x) != null) {
                                            if (adminList.get(x).getFileid() == shareDoc.getFileid()) {
                                                adminList.get(x).setPagenum(totalPage);
                                                adminList.get(x).setCurrentPage(current);
                                                adminList.get(x).setPptslide(current);
                                            }
                                        }
                                    }
                                }
                            } else {
                                ArrayList<ShareDoc> arrayList = WhiteBoradConfig.getsInstance().getDocList();
                                for (int x = 0; x < arrayList.size(); x++) {
                                    if (arrayList.get(x) != null) {
                                        if (arrayList.get(x).getFileid() == shareDoc.getFileid()) {
                                            arrayList.get(x).setPagenum(totalPage);
                                            arrayList.get(x).setCurrentPage(current);
                                            arrayList.get(x).setPptslide(current);
                                        }
                                    }
                                }
                            }
                            total_page_num = totalPage;
                            currentNumber = current;
                        }
                    }
                }
            }
        });
    }
    
    @Override
    public void close_chat_window() {
        cb_message.setChecked(false);
        cb_message.postDelayed(new Runnable() {
            @Override
            public void run() {
                cb_message.setEnabled(true);
            }
        }, 100);
    }
    
    @Override
    public void close_window() {
        cb_file_person_media_list.setChecked(false);
        cb_file_person_media_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                cb_file_person_media_list.setEnabled(true);
            }
        }, 100);
    }
    
    @Override
    public void take_photo() {
        if (!isPauseLocalVideo) {
            TKRoomManager.getInstance().pauseLocalCamera();
            isPauseLocalVideo = !isPauseLocalVideo;
        }
        isOpenCamera = true;
        isBackApp = true;
        PhotoUtils.openCamera(OneToOneThreeActivity.this);
    }
    
    @Override
    public void choose_photo() {
        isBackApp = true;
        PhotoUtils.openAlbum(OneToOneThreeActivity.this);
    }
    
    @Override
    public void choose_class_documents() {
        fileListAdapter.setArrayList(WhiteBoradConfig.getsInstance().getClassDocList());
        fileListAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void choose_admin_documents() {
        fileListAdapter.setArrayList(WhiteBoradConfig.getsInstance().getAdminDocList());
        fileListAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void choose_class_media() {
        mediaListAdapter.setArrayList(WhiteBoradConfig.getsInstance().getClassMediaList());
        mediaListAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void choose_admin_media() {
        mediaListAdapter.setArrayList(WhiteBoradConfig.getsInstance().getAdminmMediaList());
        mediaListAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void close_member_list_window() {
        cb_member_list.setChecked(false);
        cb_member_list.postDelayed(new Runnable() {
            @Override
            public void run() {
                cb_member_list.setEnabled(true);
            }
        }, 100);
    }
    
    @Override
    public void send_gift(Trophy trophy, HashMap<String, RoomUser> receiverMap) {
        if (trophy != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("trophyeffect", trophy.getTrophyeffect());
            map.put("trophyvoice", trophy.getTrophyvoice());
            map.put("trophyname", trophy.getTrophyname());
            map.put("trophyimg", trophy.getTrophyimg());
            RoomSession.getInstance().sendGift(receiverMap, map);
        }
    }
    
    @Override
    protected void onStart() {
        if (TKRoomManager.getInstance().getMySelf() != null && RoomSession.isInRoom) {
            if (nm != null) {
                nm.cancel(2);
            }
            if (TKRoomManager.getInstance().getMySelf().role == 2 || TKRoomManager.getInstance().getMySelf().role == 0) {
                if (isBackApp) {
                    int i = TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId, "__all", "isInBackGround", false);
                    if (i == 0) {
                        TKRoomManager.getInstance().setInBackGround(false);
                    }
                }
                flipCamera.setVisibility(View.VISIBLE);
            } else {
                flipCamera.setVisibility(View.GONE);
            }
        }
        isBackApp = false;
        isOpenCamera = false;
        mWakeLock.acquire();
        super.onStart();
        
        //预加载popupwindow布局
        coursePopupWindowUtils.initCoursePopupWindow();
        
        NotificationCenter.getInstance().addObserver(this, RoomSession.onRoomJoin);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onRoomLeave);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onConnectionLost);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onError);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onWarning);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onUserJoin);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onUserLeft);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onUserPropertyChanged);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onUserVideoStatus);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onKickedout);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onMessageReceived);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onRemotePubMsg);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onRemoteDelMsg);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onUpdateAttributeStream);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onPlayBackClearAll);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onPlayBackUpdateTime);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onPlayBackDuration);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onPlayBackEnd);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onShareMediaState);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onShareScreenState);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onShareFileState);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onAudioVolume);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onFirstVideoFrame);
        NotificationCenter.getInstance().addObserver(this, RoomSession.onUserAudioStatus);
        RoomSession.getInstance().onStart();
        
        if (RoomSession.isInRoom) {
            initViewByRoomTypeAndTeacher();
        }
        
        if (TKRoomManager.getInstance().getMySelf() == null) {
            return;
        }
        if (TKRoomManager.getInstance().getMySelf().publishState == 1 || TKRoomManager.getInstance().getMySelf().publishState == 3) {
            TKRoomManager.getInstance().enableSendMyVoice(true);
        }
        
        TKRoomManager.getInstance().enableOtherAudio(false);
        TKRoomManager.getInstance().setMuteAllStream(false);
        RoomSession.getInstance().closeSpeaker();
        
        if (RoomSession.isClassBegin) {
            TKRoomManager.getInstance().pubMsg("UpdateTime", "UpdateTime", TKRoomManager.getInstance().getMySelf().peerId, null, false, null, null);
        }
        
        if (!RoomSession.isInRoom) {
            re_loading.setVisibility(View.VISIBLE);
            if (frameAnimation != null) {
                frameAnimation.playAnimation();
                tv_load.setText(getString(R.string.joining_classroom_home));
            }
        } else {
            if (frameAnimation != null) {
                frameAnimation.stopAnimation();
                re_loading.setVisibility(View.GONE);
            }
        }
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if (RoomSession.isInRoom) {
            if (frameAnimation != null) {
                frameAnimation.stopAnimation();
                re_loading.setVisibility(View.GONE);
            }
        }
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        doLayout();
        
        if (videofragment != null && onUpdateAttributeAttrs != null && onUpdateAttributeAttrs.containsKey("video") && (boolean) onUpdateAttributeAttrs.get("video")) {
            videofragment.setVoice();
        }
        if (RoomSession.isClassBegin) {
            if (TKRoomManager.getInstance().getMySelf() != null && !TextUtils.isEmpty(TKRoomManager.getInstance().getMySelf().peerId)) {
                doPlayVideo(TKRoomManager.getInstance().getMySelf().peerId);
            }
        }
        if (RoomControler.haveTimeQuitClassroomAfterClass() && !RoomSession.isClassBegin) {
            if (RoomSession.timerAfterLeaved != null) {
                RoomSession.timerAfterLeaved.cancel();
                RoomSession.timerAfterLeaved = null;
            }
            RoomSession.getInstance().getSystemNowTime();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onStop() {
        if (!isFinishing()) {
            if (!isBackApp) {
                nm.notify(2, mBuilder.build());
                isBackApp = true;
            }
            if (TKRoomManager.getInstance().getMySelf() != null) {
                if (TKRoomManager.getInstance().getMySelf().role == 2 || TKRoomManager.getInstance().getMySelf().role == 0) {
                    TKRoomManager.getInstance().setInBackGround(true);
                    TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId, "__all", "isInBackGround", true);
                }
            }
        }
        mWakeLock.release();
        
        if (isFinishing()) {
            timeMessages.clear();
        }
        RoomSession.getInstance().onStop();
        NotificationCenter.getInstance().removeObserver(this);
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    
    private void initData() {
        if (frameAnimation == null) {
            frameAnimation = new FrameAnimation(loadingImageView, AccessResourcesUtil.getRes(this), 100, true, false, this);
        }
        
        coursePopupWindowUtils = new ThreeCoursePopupWindowUtils(this, RoomSession.memberList, RoomSession.serial, RoomSession.serial);
        coursePopupWindowUtils.setPopupWindowClick(this);
        
        memberListPopupWindowUtils = new ThreeMemberListPopupWindowUtils(this, RoomSession.memberList);
        memberListPopupWindowUtils.setPopupWindowClick(this);
        
        chatUtils = new ThreeChatWindowPop(this, RoomSession.chatList, null);
        chatUtils.setChatPopupWindowClick(this);
        playSpeedPopupWindowUtils = new PlaySpeedPopupWindowUtils(this);
        sendGiftPopUtils = new ThreeSendGiftPopUtils(this);
        sendGiftPopUtils.preLoadImage();
        sendGiftPopUtils.setOnSendGiftClick(this);
        
        
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 75, 0);
        
        
        operatingAnim = AnimationUtils.loadAnimation(OneToOneThreeActivity.this, R.anim.disk_aim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        
        fileListAdapter = coursePopupWindowUtils.getFileListAdapter();
        memberListAdapter = memberListPopupWindowUtils.getMemberListAdapter();
        mediaListAdapter = coursePopupWindowUtils.getMediaListAdapter();
        chlistAdapter = chatUtils.getChatListAdapter();
        
        RoomSession.getInstance().init(this);
        wbFragment = WhiteBoradConfig.getsInstance().CreateWhiteBorad(this);
        if (RoomSession.path != null && !RoomSession.path.isEmpty()) {
            WhiteBoradConfig.getsInstance().setPlayBack(true);
        }
        
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (!wbFragment.isAdded()) {
            ft.add(R.id.wb_container, wbFragment);
            ft.commit();
        }
        
        huawei = FullScreenTools.hasNotchInScreen(this);
        oppo = FullScreenTools.hasNotchInOppo(this);
        voio = FullScreenTools.hasNotchInScreenAtVoio(this);
    }
    
    private void initview() {
        //mp3 mp4
        img_disk = (GifImageView) findViewById(R.id.img_disk);
        lin_audio_control = (LinearLayout) findViewById(R.id.lin_audio_control);
        img_play_mp3 = (ImageView) lin_audio_control.findViewById(R.id.img_play);
        txt_mp3_name = (TextView) lin_audio_control.findViewById(R.id.txt_media_name);
        txt_mp3_time = (TextView) lin_audio_control.findViewById(R.id.txt_media_time);
        sek_mp3 = (SeekBar) lin_audio_control.findViewById(R.id.sek_media);
        img_voice_mp3 = (ImageView) lin_audio_control.findViewById(R.id.img_media_voice);
        sek_voice_mp3 = (SeekBar) lin_audio_control.findViewById(R.id.sek_media_voice);
        ll_close_mp3 = (LinearLayout) lin_audio_control.findViewById(R.id.ll_close_mp3);
        
        
        three_view_one = findViewById(R.id.three_view_one);
        
        //顶部工具条
        rel_tool_bar = (RelativeLayout) findViewById(R.id.title_bar);
        cb_member_list = (CheckBox) rel_tool_bar.findViewById(R.id.cb_member_list);
        cb_file_person_media_list = (CheckBox) rel_tool_bar.findViewById(R.id.cb_file_person_media_list);
        cb_tool_case = (CheckBox) rel_tool_bar.findViewById(R.id.cb_tool_case);
        cb_control = (CheckBox) rel_tool_bar.findViewById(R.id.cb_control);
        cb_message = (CheckBox) rel_tool_bar.findViewById(R.id.cb_message);
        cb_choose_photo = (CheckBox) rel_tool_bar.findViewById(R.id.cb_choose_photo);
        flipCamera = (ImageView) rel_tool_bar.findViewById(R.id.flip_camera);
        img_back = (ImageView) rel_tool_bar.findViewById(R.id.img_back);//退出按钮
        txt_class_begin = (TextView) rel_tool_bar.findViewById(R.id.txt_class_begin);//上课
        txt_hand_up = (TextView) rel_tool_bar.findViewById(R.id.txt_hand_up);//举手
        lin_time = (LinearLayout) rel_tool_bar.findViewById(R.id.lin_time);
        txt_mao_01 = (TextView) rel_tool_bar.findViewById(R.id.txt_mao_01);
        txt_mao_02 = (TextView) rel_tool_bar.findViewById(R.id.txt_mao_02);
        txt_hour = (TextView) rel_tool_bar.findViewById(R.id.txt_hour);
        txt_min = (TextView) rel_tool_bar.findViewById(R.id.txt_min);
        txt_ss = (TextView) rel_tool_bar.findViewById(R.id.txt_ss);
        ll_top = (LinearLayout) rel_tool_bar.findViewById(R.id.ll_top);
        re_top_right = (LinearLayout) rel_tool_bar.findViewById(R.id.re_top_right);
        
        rl_member_list = (RelativeLayout) rel_tool_bar.findViewById(R.id.rl_member_list);
        rl_message = (RelativeLayout) rel_tool_bar.findViewById(R.id.rl_message);
        
        tv_no_read_message_number = (TextView) rel_tool_bar.findViewById(R.id.tv_no_read_message_number);
        iv_hand = (ImageView) rel_tool_bar.findViewById(R.id.iv_hand);
        
        rl_web = (RelativeLayout) findViewById(R.id.rl_web);
        
        video_container = (FrameLayout) findViewById(R.id.video_container);
        ll_video_whiteboard = (LinearLayout) findViewById(R.id.ll_video_whiteboard);
        
        ll_wb_container = (RelativeLayout) findViewById(R.id.ll_wb_container);
        re_loading = (RelativeLayout) findViewById(R.id.re_laoding);
        loadingImageView = (ImageView) findViewById(R.id.loadingImageView);
        tv_load = (TextView) re_loading.findViewById(R.id.tv_load);
        
        //回放控件
        rel_play_back = (LinearLayout) findViewById(R.id.rel_play_back);
        img_play_back = (ImageView) rel_play_back.findViewById(R.id.img_play_back);
        sek_play_back = (SeekBar) rel_play_back.findViewById(R.id.sek_play_back);
        txt_play_back_time = (TextView) rel_play_back.findViewById(R.id.txt_play_back_time);
        tv_play_speed = (TextView) rel_play_back.findViewById(R.id.tv_play_speed);
        img_back_play_voice = (ImageView) rel_play_back.findViewById(R.id.img_back_play_voice);
        sek_back_play_voice = (SeekBar) rel_play_back.findViewById(R.id.sek_back_play_voice);
        ll_voice = (LinearLayout) rel_play_back.findViewById(R.id.ll_voice);
        rel_play_back_bar = (RelativeLayout) findViewById(R.id.rel_play_back_bar);
        re_back = (RelativeLayout) rel_play_back_bar.findViewById(R.id.re_back);
        re_play_back = (RelativeLayout) findViewById(R.id.re_play_back);
        tv_back_name = (TextView) rel_play_back_bar.findViewById(R.id.tv_back_name);
        
        
        //白板全屏右下角视频界面
        rel_fullscreen_videoitem = (RelativeLayout) findViewById(R.id.rel_fullscreen_videoitem);
        fullscreen_sf_video = (SurfaceViewRenderer) rel_fullscreen_videoitem.findViewById(R.id.fullscreen_sf_video);
        fullscreen_sf_video.init(EglBase.create().getEglBaseContext(), null);
        fullscreen_sf_video.setZOrderOnTop(true);
        fullscreen_bg_video_back = (ImageView) rel_fullscreen_videoitem.findViewById(R.id.fullscreen_bg_video_back);
        fullscreen_img_video_back = (ImageView) rel_fullscreen_videoitem.findViewById(R.id.fullscreen_img_video_back);
        
        lin_menu = (LinearLayout) findViewById(R.id.lin_menu);
        wb_container = (FrameLayout) findViewById(R.id.wb_container);
        
        cb_file_person_media_list.setOnCheckedChangeListener(this);
        cb_tool_case.setOnCheckedChangeListener(this);
        cb_message.setOnCheckedChangeListener(this);
        cb_member_list.setOnCheckedChangeListener(this);
        cb_choose_photo.setOnCheckedChangeListener(this);
        cb_choose_photo.setOnClickListener(this);
        cb_control.setOnClickListener(this);
        
        tv_play_speed.setOnClickListener(this);
        re_back.setOnClickListener(this);
        flipCamera.setOnClickListener(this);
        txt_class_begin.setOnClickListener(this);
        txt_hand_up.setOnClickListener(this);
        img_back.setOnClickListener(this);
        
        
        sek_mp3.setPadding((int) (10 * ScreenScale.getWidthScale()), 0, (int) (10 * ScreenScale.getWidthScale()), 0);
        sek_voice_mp3.setPadding((int) (10 * ScreenScale.getWidthScale()), 0, (int) (10 * ScreenScale.getWidthScale()), 0);
        try {
            gifDrawable = new GifDrawable(getResources(), R.drawable.play_mp3_gif);
            img_disk.setImageDrawable(gifDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //关闭mp3
        ll_close_mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TKRoomManager.getInstance().stopShareMedia();
                lin_audio_control.setVisibility(View.INVISIBLE);
            }
        });
        //播放暂停mp3
        img_play_mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaAttrs != null) {
                    if (RoomSession.isPublish) {
                        TKRoomManager.getInstance().playMedia((Boolean) mediaAttrs.get("pause") == null ? false : (Boolean) mediaAttrs.get("pause"));
                    } else {
                        ShareDoc media = WhiteBoradConfig.getsInstance().getCurrentMediaDoc();
                        WhiteBoradConfig.getsInstance().setCurrentMediaDoc(media);
                        String strSwfpath = media.getSwfpath();
                        int pos = strSwfpath.lastIndexOf('.');
                        strSwfpath = String.format("%s-%d%s", strSwfpath.substring(0, pos), 1, strSwfpath.substring(pos));
                        String url = "http://" + WhiteBoradConfig.getsInstance().getFileServierUrl() + ":" +
                            WhiteBoradConfig.getsInstance().getFileServierPort() + strSwfpath;
                        HashMap<String, Object> attrMap = new HashMap<String, Object>();
                        attrMap.put("filename", media.getFilename());
                        attrMap.put("fileid", media.getFileid());
                        
                        if (RoomSession.isClassBegin) {
                            TKRoomManager.getInstance().startShareMedia(url, false, "__all", attrMap);
                        } else {
                            TKRoomManager.getInstance().startShareMedia(url, false, TKRoomManager.getInstance().getMySelf().peerId, attrMap);
                        }
                    }
                }
            }
        });
        //进度改变监听
        sek_mp3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pro = 0;
            boolean isfromUser = false;
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    pro = progress;
                    isfromUser = fromUser;
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                double currenttime = 0;
                if (isfromUser && mediaAttrs != null) {
                    currenttime = ((double) pro / (double) seekBar.getMax()) * (int) mediaAttrs.get("duration");
                    TKRoomManager.getInstance().seekMedia((long) currenttime);
                }
            }
        });
        //声音喇叭点击监听
        img_voice_mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMediaMute) {
                    TKRoomManager.getInstance().setRemoteAudioVolume(vol, mediaPeerId, 2);
                    img_voice_mp3.setImageResource(R.drawable.icon_voice);
                    sek_voice_mp3.setProgress((int) (vol * 100));
                } else {
                    TKRoomManager.getInstance().setRemoteAudioVolume(0, mediaPeerId, 2);
                    img_voice_mp3.setImageResource(R.drawable.icon_no_voice);
                    sek_voice_mp3.setProgress(0);
                }
                isMediaMute = !isMediaMute;
            }
        });
        sek_voice_mp3.setProgress((int) (vol * 100));
        sek_voice_mp3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float vol = (float) progress / (float) seekBar.getMax();
                if (vol > 0) {
                    img_voice_mp3.setImageResource(R.drawable.icon_voice);
                } else {
                    img_voice_mp3.setImageResource(R.drawable.icon_no_voice);
                }
                TKRoomManager.getInstance().setRemoteAudioVolume(vol, mediaPeerId, 2);
                if (fromUser) {
                    OneToOneThreeActivity.this.vol = vol;
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            
            }
        });
        
        rel_play_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        
        sek_play_back.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    this.progress = progress;
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                long pos = (long) (((double) progress / 100) * (endtime - starttime) + starttime);
                if (isEnd) {
                    img_play_back.setImageResource(R.drawable.btn_play_normal);
                    TKPlayBackManager.getInstance().seekPlayback(0);
                    isPlayBackPlay = false;
                } else {
                    img_play_back.setImageResource(R.drawable.btn_pause_normal);
                    TKPlayBackManager.getInstance().seekPlayback(pos);
                    TKPlayBackManager.getInstance().resumePlayBack();
                    isPlayBackPlay = true;
                }
                isEnd = false;
            }
        });
        img_play_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayBackPlay) {
                    try {
                        TKPlayBackManager.getInstance().pausePlayback();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    img_play_back.setImageResource(R.drawable.btn_play_normal);
                } else {
                    if (isEnd) {
                        TKPlayBackManager.getInstance().seekPlayback(starttime);
                        currenttime = starttime;
                        TKPlayBackManager.getInstance().resumePlayBack();
                        img_play_back.setImageResource(R.drawable.btn_pause_normal);
                        isEnd = false;
                    } else {
                        TKPlayBackManager.getInstance().resumePlayBack();
                        img_play_back.setImageResource(R.drawable.btn_pause_normal);
                    }
                }
                isPlayBackPlay = !isPlayBackPlay;
                WhiteBoradConfig.getsInstance().playbackPlayAndPauseController(isPlayBackPlay);
                
            }
        });
    }
    
    
    private void doLayout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    if (isZoom) {
                        return;
                    }
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int wid = dm.widthPixels;
                    int hid = dm.heightPixels;
                    //        int a = dm.widthPixels / 7;
                    //        int b = a * 3 / 4;   // b = 3a/4
                    //        int c = a * 16 / 10;  // c = 1.6 * a
                    //        int d = c * 3 / 4; // d = 3c/4
                    
                    //        if (TKRoomManager.getInstance().get_room_video_height() != 0 && TKRoomManager.getInstance().get_room_video_width() != 0) {
                    //            wid_ratio = TKRoomManager.getInstance().get_room_video_width();
                    //            hid_ratio = TKRoomManager.getInstance().get_room_video_height();
                    //        }
                    
                    //顶部工具栏
                    RelativeLayout.LayoutParams tool_bar_param = (RelativeLayout.LayoutParams) rel_tool_bar.getLayoutParams();
                    tool_bar_param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    tool_bar_param.height = dm.widthPixels / 7 * 3 / 4 * 4 / 10;
                    rel_tool_bar.setLayoutParams(tool_bar_param);
                    //回放顶部titlebar
                    rel_play_back_bar.setLayoutParams(tool_bar_param);
                    
                    int heightStatusBar = 0;
                    
                    if (huawei || oppo || voio) {
                        int result = 0;
                        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                        if (resourceId > 0) {
                            result = getResources().getDimensionPixelSize(resourceId);
                        }
                        heightStatusBar = result;
                    }
                    
                    if (huawei || oppo || voio) {
                        RelativeLayout.LayoutParams ll_par = (RelativeLayout.LayoutParams) ll_top.getLayoutParams();
                        ll_par.leftMargin = heightStatusBar;
                        ll_top.setLayoutParams(ll_par);
                        
                        RelativeLayout.LayoutParams re_top_right_par = (RelativeLayout.LayoutParams) re_top_right.getLayoutParams();
                        re_top_right_par.rightMargin = heightStatusBar;
                        re_top_right.setLayoutParams(re_top_right_par);
                    }
                    
                    // 设高为 白板高为b   求  (b * 4 / 3) + ( b / 2 * 4 / 3 ) = 屏幕宽  4:3
                    //   b = 屏幕宽 / 2
                    
                    // 设高为 白板高为b   求  (b * 16 / 9) + ( b / 2 * 4 / 3 ) = 屏幕宽  16:9
                    //  22 * b / 9 = 屏幕宽
                    //  b = 屏幕宽 / 2.44444444
                    
                    int webhight = 0;
                    if (huawei || oppo || voio) {
                        if (wid_ratio == 4 && hid_ratio == 3) {
                            webhight = (int) ((wid - heightStatusBar) / 2);
                        }
                        if (wid_ratio == 16 && hid_ratio == 9) {
                            webhight = (int) ((wid - heightStatusBar) * 1000 / 2444);
                        }
                        
                    } else {
                        if (wid_ratio == 4 && hid_ratio == 3) {
                            webhight = (int) (wid / 2);
                        }
                        if (wid_ratio == 16 && hid_ratio == 9) {
                            webhight = (int) (wid * 1000 / 2444);
                        }
                    }
                    //        wid_ratio = 16;
                    //        hid_ratio = 9;
                    //        int  webhight = (int) ((wid - heightStatusBar) * 1000 / 2444) ;
                    
                    LinearLayout.LayoutParams rel_wb_param = (LinearLayout.LayoutParams) ll_wb_container.getLayoutParams();
                    
                    //如果 屏幕高 - titile  > 白板高（webhight）
                    if ((hid - tool_bar_param.height) > webhight) {
                        //取(hid - tool_bar_param.height)
                        rel_wb_param.height = (int) (webhight);
                        
                    } else {
                        //取(webhight)
                        rel_wb_param.height = (int) (hid - tool_bar_param.height - 20);
                    }
                    //白板大小
                    rel_wb_param.width = (int) (rel_wb_param.height * wid_ratio / hid_ratio);
                    
                    //(int) (wid - heightStatusBar - rel_wb_param.width - c - 10  ) / 2;
                    rel_wb_param.leftMargin = heightStatusBar;
                    //        rel_wb_param.gravity = Gravity.CENTER_VERTICAL;
                    ll_wb_container.setLayoutParams(rel_wb_param);
                    
                    if (wbFragment != null && WBSession.isPageFinish) {
                        WhiteBoradConfig.getsInstance().SetTransmitWindowSize(rel_wb_param.width, rel_wb_param.height);
                    }
                    
                    final float scale = getResources().getDisplayMetrics().density;
                    int rightmg = (int) (8 * scale + 0.5f);
                    
                    //白板全屏右下角界面大小
                    fullscreen_video_param = new RelativeLayout.LayoutParams(0, 0);
                    fullscreen_video_param.width = (wid - 8 * 8) / 7;
                    fullscreen_video_param.height = (dm.widthPixels - 8 * 8) / 7 * hid_ratio / wid_ratio + 16;
                    fullscreen_video_param.rightMargin = rightmg;
                    fullscreen_video_param.bottomMargin = 10;
                    fullscreen_sf_video.setLayoutParams(fullscreen_video_param);
                    fullscreen_bg_video_back.setLayoutParams(fullscreen_video_param);
                    fullscreen_img_video_back.setLayoutParams(fullscreen_video_param);
                    
                    /**
                     * 视频和白板空隙10
                     */
                    
                    //右边老师和学生视频框大小
                    LinearLayout.LayoutParams menu_param = (LinearLayout.LayoutParams) lin_menu.getLayoutParams();
                    menu_param.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    menu_param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    menu_param.gravity = Gravity.CENTER_VERTICAL;
                    //        menu_param.leftMargin = 10;
                    lin_menu.setLayoutParams(menu_param);
                    
                    //mp3
                    RelativeLayout.LayoutParams lin_audio_control_param = (RelativeLayout.LayoutParams) lin_audio_control.getLayoutParams();
                    lin_audio_control_param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    lin_audio_control_param.height = tool_bar_param.height * 3 / 2;
                    lin_audio_control.setLayoutParams(lin_audio_control_param);
                    
                    /**
                     * 空隙 10  各减5 添加view 撑开
                     */
                    
                    //老师视频框   - KeyBoardUtil.dp2px(this, 16)
                    LinearLayout.LayoutParams stu_par_menu = (LinearLayout.LayoutParams) teacherItem.parent.getLayoutParams();
                    //        stu_par_menu.height = rel_wb_param.height / 2 - 5;
                    stu_par_menu.height = rel_wb_param.height / 2;
                    stu_par_menu.width = stu_par_menu.height * 4 / 3;
                    teacherItem.parent.setLayoutParams(stu_par_menu);
                    stu_in_sd.parent.setLayoutParams(stu_par_menu);
                    
                    LinearLayout.LayoutParams view_one = (LinearLayout.LayoutParams) three_view_one.getLayoutParams();
                    view_one.width = stu_par_menu.width;
                    //        view_one.height = 10;
                    view_one.height = 0;
                    three_view_one.setLayoutParams(view_one);
                    
                    
                    LinearLayout.LayoutParams stu_video_label_menu = (LinearLayout.LayoutParams) teacherItem.rel_video_label.getLayoutParams();
                    stu_video_label_menu.width = stu_par_menu.width;
                    stu_video_label_menu.height = stu_par_menu.height;
                    teacherItem.rel_video_label.setLayoutParams(stu_video_label_menu);
                    stu_in_sd.rel_video_label.setLayoutParams(stu_video_label_menu);
                    
                    //视频框底部高度
                    RelativeLayout.LayoutParams stu_name_menu = (RelativeLayout.LayoutParams) teacherItem.lin_name_label.getLayoutParams();
                    stu_name_menu.width = stu_video_label_menu.width;
                    stu_name_menu.height = (int) (stu_name_menu.width * ((double) 30 / (double) 350));
                    //        stu_name_menu.bottomMargin = stu_video_label_menu.height / 12;
                    teacherItem.lin_name_label.setLayoutParams(stu_name_menu);
                    stu_in_sd.lin_name_label.setLayoutParams(stu_name_menu);
                    
                    
                    //举手
                    RelativeLayout.LayoutParams img_hand_par = (RelativeLayout.LayoutParams) stu_in_sd.img_hand.getLayoutParams();
                    img_hand_par.height = (int) (stu_name_menu.height);
                    img_hand_par.width = (int) (stu_name_menu.height);
                    //        img_hand_par.leftMargin = stu_param_menu.width / 12;
                    stu_in_sd.img_hand.setLayoutParams(img_hand_par);
                    //音量
                    LinearLayout.LayoutParams img_mic_par = (LinearLayout.LayoutParams) stu_in_sd.img_mic.getLayoutParams();
                    img_mic_par.height = (int) (stu_name_menu.height);
                    img_mic_par.width = (int) (stu_name_menu.height);
                    //        img_mic_par.rightMargin = stu_param_menu.width / 16;
                    stu_in_sd.img_mic.setLayoutParams(img_mic_par);
                    teacherItem.img_mic.setLayoutParams(img_mic_par);
                    //画笔
                    RelativeLayout.LayoutParams img_pen_par = (RelativeLayout.LayoutParams) stu_in_sd.img_pen.getLayoutParams();
                    img_pen_par.height = (int) (stu_name_menu.height);
                    img_pen_par.width = (int) (stu_name_menu.height);
                    //        img_pen_par.rightMargin = stu_param_menu.width / 16;
                    stu_in_sd.img_pen.setLayoutParams(img_pen_par);
                    teacherItem.img_pen.setLayoutParams(img_pen_par);
                    
                    //画笔颜色
                    LinearLayout.LayoutParams cv_draw_par = (LinearLayout.LayoutParams) stu_in_sd.cv_draw.getLayoutParams();
                    cv_draw_par.height = (int) (stu_name_menu.height) / 2;
                    cv_draw_par.weight = (int) (stu_name_menu.height) / 2;
                    stu_in_sd.cv_draw.setLayoutParams(cv_draw_par);
                    teacherItem.cv_draw.setLayoutParams(cv_draw_par);
                    
                    LinearLayout.LayoutParams volue = (LinearLayout.LayoutParams) stu_in_sd.volumeView.getLayoutParams();
                    volue.height = (int) (stu_name_menu.width * ((double) 13 / (double) 350));
                    volue.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    volue.leftMargin = 13;
                    stu_in_sd.volumeView.setLayoutParams(volue);
                    teacherItem.volumeView.setLayoutParams(volue);
                    
                    webandsufwidth = rel_wb_param.width + stu_par_menu.width + 10;
                    
                    if (webandsufwidth >= wid) {
                        webandsufwidth = 0;
                    } else {
                        if (huawei || oppo || voio) {
                            webandsufwidth = (wid - (rel_wb_param.width + stu_par_menu.width + 10) - heightStatusBar) / 2;
                        } else {
                            webandsufwidth = (wid - (rel_wb_param.width + stu_par_menu.width + 10)) / 2;
                        }
                    }
                }
            }
        });
    }
    
    private void doPlayVideo(String peerId) {
        
        if (TextUtils.isEmpty(peerId)) {
            return;
        }
        
        RoomUser roomUser = TKRoomManager.getInstance().getUser(peerId);
        if (roomUser != null && roomUser.role == 0 && roomUser.getPublishState() == 0 && RoomSession.isClassBegin) {
            teacherItem.rel_group.setVisibility(View.INVISIBLE);
        }
        
        stu_in_sd.lin_gift.setVisibility(View.INVISIBLE);
        
        for (int i = 0; i < RoomSession.playingList.size(); i++) {
            final RoomUser user = RoomSession.playingList.get(i);
            if (user == null) {
                return;
            }
            if (RoomSession.roomType == 0) {  //1对1
                if (user.role == 0) {//老师
                    doTeacherVideoPlay(user);
                } else if (user.role == 2) {//学生
                    
                    if (user.getPublishState() > 1 && user.getPublishState() < 4 && !user.disablevideo) {
                        if (user.hasVideo) {
                            if (RoomSession.isClassBegin && isZoom && RoomControler.isFullScreenVideo()) {
                                if (TKRoomManager.getInstance().getMySelf().role == 0) {
                                    if (videofragment != null) {
                                        videofragment.setFullscreenShow(user.peerId);
                                    } else {
                                        if (movieFragment != null) {
                                            movieFragment.setFullscreenShow(user.peerId);
                                        } else {
                                            rel_fullscreen_videoitem.setVisibility(View.VISIBLE);
                                            fullscreen_sf_video.setVisibility(View.VISIBLE);
                                            fullscreen_img_video_back.setVisibility(View.INVISIBLE);
                                            fullscreen_bg_video_back.setVisibility(View.INVISIBLE);
                                            TKRoomManager.getInstance().playVideo(user.peerId, fullscreen_sf_video, RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    if (!isZoom) {
                        stu_in_sd.rel_group.setVisibility(View.VISIBLE);
                        stu_in_sd.lin_gift.setVisibility(View.VISIBLE);
                        stu_in_sd.txt_name.setText(user.nickName);
                        
                        if (user.disableaudio) {
                            stu_in_sd.img_mic.setImageResource(R.drawable.three_img_mic_ban);
                            stu_in_sd.volumeView.setVisibility(View.GONE);
                        } else {
                            if (user.getPublishState() == 0 || user.getPublishState() == 2 || user.getPublishState() == 4) {
                                if (RoomSession.isClassBegin) {
                                    stu_in_sd.img_mic.setImageResource(R.drawable.three_img_mic_ban);
                                    stu_in_sd.volumeView.setVisibility(View.GONE);
                                }
                            }
                        }
                        if (user.getPublishState() > 1 && user.getPublishState() < 4 && !user.disablevideo) {
                            if (user.hasVideo) {
                                if (!isZoom) {
                                    stu_in_sd.sf_video.setVisibility(View.VISIBLE);
                                    stu_in_sd.bg_video_back.setVisibility(View.GONE);
                                    TKRoomManager.getInstance().playVideo(user.peerId, stu_in_sd.sf_video, RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                                }
                                
                            } else {
                                stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
                                stu_in_sd.img_video_back.setImageResource(R.drawable.three_icon_no_camera);
                                stu_in_sd.bg_video_back.setVisibility(View.VISIBLE);
                            }
                        } else {
                            stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
                            if (user.hasVideo) {
                                stu_in_sd.img_video_back.setImageResource(R.drawable.three_icon_camera_close);
                                stu_in_sd.bg_video_back.setVisibility(View.VISIBLE);
                            } else {
                                stu_in_sd.img_video_back.setImageResource(R.drawable.three_icon_no_camera);
                                stu_in_sd.bg_video_back.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void doTeacherVideoPlay(final RoomUser user) {
        teacherItem.rel_group.setVisibility(View.VISIBLE);
        teacherItem.lin_name_label.setVisibility(View.VISIBLE);
        if (user.getPublishState() > 1 && user.getPublishState() < 4 && !user.disablevideo) {
            if (user.hasVideo) {
                if (!isZoom) {
                    teacherItem.sf_video.setVisibility(View.VISIBLE);
                    teacherItem.bg_video_back.setVisibility(View.GONE);
                    teacherItem.img_video_back.setVisibility(View.GONE);
                    TKRoomManager.getInstance().playVideo(user.peerId, teacherItem.sf_video, RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                }
                
                if (RoomSession.isClassBegin && isZoom && RoomControler.isFullScreenVideo() && !TextUtils.isEmpty(stu_in_sd.peerid)) {
                    if (TKRoomManager.getInstance().getMySelf().role != 0) {
                        if (videofragment != null /*&& !videofragment.isAdded()*/) {
                            videofragment.setFullscreenShow(user.peerId);
                        } else {
                            if (movieFragment != null) {
                                movieFragment.setFullscreenShow(user.peerId);
                            } else {
                                rel_fullscreen_videoitem.setVisibility(View.VISIBLE);
                                fullscreen_sf_video.setVisibility(View.VISIBLE);
                                fullscreen_img_video_back.setVisibility(View.INVISIBLE);
                                fullscreen_bg_video_back.setVisibility(View.INVISIBLE);
                                TKRoomManager.getInstance().playVideo(user.peerId, fullscreen_sf_video, RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                            }
                        }
                    }
                }
            } else {
                teacherItem.sf_video.setVisibility(View.INVISIBLE);
                teacherItem.img_video_back.setImageResource(R.drawable.three_icon_no_camera);
                teacherItem.img_video_back.setVisibility(View.VISIBLE);
                teacherItem.bg_video_back.setVisibility(View.VISIBLE);
            }
        } else {
            teacherItem.sf_video.setVisibility(View.INVISIBLE);
            teacherItem.img_video_back.setImageResource(R.drawable.three_icon_no_camera);
            teacherItem.img_video_back.setVisibility(View.VISIBLE);
            teacherItem.bg_video_back.setVisibility(View.VISIBLE);
        }
        teacherItem.txt_name.setText(user.nickName);
        vdi_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getPublishState() != 0) {
                    showTeacherControlPop(user);
                }
            }
        });
    }
    
    PopupWindow popupWindow;
    boolean is_show_teacher_window = true;
    
    private void showTeacherControlPop(final RoomUser user) {
        
        if (!is_show_teacher_window) {
            is_show_teacher_window = true;
            return;
        }
        
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        
        if (!(TKRoomManager.getInstance().getMySelf().role == 0)) {
            return;
        }
        
        if (!RoomControler.isReleasedBeforeClass()) {
            if (!RoomSession.isClassBegin) {
                return;
            }
        }
        
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_av_control, null);
        LinearLayout lin_video_control = (LinearLayout) contentView.findViewById(R.id.lin_video_control);
        LinearLayout lin_audio_control = (LinearLayout) contentView.findViewById(R.id.lin_audio_control);
        LinearLayout lin_plit_screen = (LinearLayout) contentView.findViewById(R.id.lin_plit_screen);
        final ImageView img_video_control = (ImageView) contentView.findViewById(R.id.img_camera);
        final ImageView img_audio_control = (ImageView) contentView.findViewById(R.id.img_audio);
        final TextView txt_video = (TextView) contentView.findViewById(R.id.txt_camera);
        final TextView txt_audio = (TextView) contentView.findViewById(R.id.txt_audio);
        ImageView right_arr = (ImageView) contentView.findViewById(R.id.right_arr);
        right_arr.setVisibility(View.VISIBLE);
        lin_plit_screen.setVisibility(View.GONE);
        
        popupWindow = new PopupWindow(KeyBoardUtil.dp2px(OneToOneThreeActivity.this, 80), KeyBoardUtil.dp2px(OneToOneThreeActivity.this, 115));
        popupWindow.setContentView(contentView);
        
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                is_show_teacher_window = !Tools.isInView(event, vdi_teacher);
                return false;
            }
        });
        
        if (user.getPublishState() == 0 || user.getPublishState() == 2 || user.getPublishState() == 4) {
            img_audio_control.setImageResource(R.drawable.three_icon_open_audio);
            txt_audio.setText(R.string.open_audio);
        } else {
            img_audio_control.setImageResource(R.drawable.three_icon_close_audio);
            txt_audio.setText(R.string.close_audio);
        }
        if (user.getPublishState() == 0 || user.getPublishState() == 1 || user.getPublishState() == 4) {
            img_video_control.setImageResource(R.drawable.three_icon_open_vidio);
            txt_video.setText(R.string.video_on);
        } else {
            img_video_control.setImageResource(R.drawable.three_icon_close_vidio);
            txt_video.setText(R.string.video_off);
        }
        
        lin_video_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getPublishState() == 0 || user.getPublishState() == 1 || user.getPublishState() == 4) {
                    img_video_control.setImageResource(R.drawable.three_icon_close_vidio);
                    txt_video.setText(R.string.video_off);
                    TKRoomManager.getInstance().changeUserProperty(user.peerId,
                        "__all", "publishstate", user.getPublishState() == 0 || user.getPublishState() == 4 ? 2 : 3);
                } else {
                    img_video_control.setImageResource(R.drawable.three_icon_open_vidio);
                    txt_video.setText(R.string.video_on);
                    TKRoomManager.getInstance().changeUserProperty(user.peerId,
                        "__all", "publishstate", user.getPublishState() == 2 ? 4 : 1);
                }
                popupWindow.dismiss();
            }
        });
        
        lin_audio_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getPublishState() == 0 || user.getPublishState() == 2 || user.getPublishState() == 4) {
                    img_audio_control.setImageResource(R.drawable.three_icon_close_audio);
                    txt_audio.setText(R.string.close_audio);
                    TKRoomManager.getInstance().changeUserProperty(user.peerId,
                        "__all", "publishstate", user.getPublishState() == 0 || user.getPublishState() == 4 ? 1 : 3);
                } else {
                    img_audio_control.setImageResource(R.drawable.three_icon_open_audio);
                    txt_audio.setText(R.string.open_audio);
                    TKRoomManager.getInstance().changeUserProperty(user.peerId,
                        "__all", "publishstate", user.getPublishState() == 3 ? 2 : 4);
                }
                popupWindow.dismiss();
            }
        });
        
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //        popupWindow.showAsDropDown(vdi_teacher, -vdi_teacher.getMeasuredWidth(),
        //                -vdi_teacher.getMeasuredHeight() / 2 - vdi_teacher.getMeasuredHeight() / 3);
        popupWindow.showAsDropDown(vdi_teacher, -(popupWindow.getWidth()), -(vdi_teacher.getMeasuredHeight() + popupWindow.getHeight()) / 2, Gravity.CENTER_VERTICAL);
    }
    
    private void initViewByRoomTypeAndTeacher() {
        
        if (RoomSession.roomType == 0) {       //一对一
            //全体禁言隐藏
            cb_control.setVisibility(View.GONE);
            //花名册隐藏
            rl_member_list.setVisibility(View.GONE);
            //课件库隐藏
            cb_file_person_media_list.setVisibility(View.GONE);
            //工具箱隐藏
            cb_tool_case.setVisibility(View.GONE);
            
            if (TKRoomManager.getInstance().getMySelf().role == 0) {//老师
                //课件库显示
                cb_file_person_media_list.setVisibility(View.VISIBLE);
                rl_member_list.setVisibility(View.VISIBLE);
                //学生授权后选择照片界面
                //                rl_choose_photo.setVisibility(View.GONE);
                if (RoomSession.isClassBegin) {
                    //工具箱
                    cb_tool_case.setVisibility(View.VISIBLE);
                } else {
                    cb_tool_case.setVisibility(View.GONE);
                }
                
                if (!RoomControler.isShowClassBeginButton()) {
                    txt_class_begin.setVisibility(View.VISIBLE);
                }
                
            } else if (TKRoomManager.getInstance().getMySelf().role == 2) {//学生
                
                txt_class_begin.setVisibility(View.INVISIBLE);
                
                
            } else if (TKRoomManager.getInstance().getMySelf().role == 4) {  //巡课
                
                if (RoomControler.isShowClassBeginButton() || RoomControler.patrollerCanClassDismiss() || !RoomSession.isClassBegin) {
                    txt_class_begin.setVisibility(View.INVISIBLE);
                } else {
                    txt_class_begin.setVisibility(View.VISIBLE);
                }
                rl_member_list.setVisibility(View.VISIBLE);
                cb_file_person_media_list.setVisibility(View.VISIBLE);
                //隐藏转换摄像头按钮
                flipCamera.setVisibility(View.INVISIBLE);
                
            } else if (TKRoomManager.getInstance().getMySelf().role == -1) {  //回放
                
                lin_time.setVisibility(View.GONE);
                ll_voice.setVisibility(View.GONE);
                
                lin_time.setVisibility(View.GONE);
                ll_voice.setVisibility(View.GONE);
                
                cb_message.setVisibility(View.GONE);
                flipCamera.setVisibility(View.GONE);
                txt_class_begin.setVisibility(View.GONE);
                txt_hand_up.setVisibility(View.GONE);
                re_play_back.setVisibility(View.VISIBLE);
            }
        }
        doLayout();
    }
    
    private void initTimeMessage() {
        timeMessages.clear();
        String one = "<font color='#FFD700'>1</font>";
        TimeMessage tms1 = new TimeMessage();
        tms1.message = getString(R.string.classroom_tip_01_01) + "<font color='#FFD700'>1</font>" + getString(R.string.classroom_tip_01_02);
        TimeMessage tms2 = new TimeMessage();
        tms2.message = getString(R.string.classroom_tip_02_01) + "<font color='#FFD700'>1</font>" + getString(R.string.classroom_tip_02_02);
        TimeMessage tms3 = new TimeMessage();
        tms3.message = getString(R.string.classroom_tip_03_01) + "<font color='#FFD700'>1</font>" + getString(R.string.classroom_tip_03_02);
        TimeMessage tms4 = new TimeMessage();
        tms4.message = getString(R.string.classroom_tip_04_01) + "<font color='#FFD700'>3</font>" + getString(R.string.classroom_tip_04_02) + "<font color='#FFD700'>2</font>" + getString(R.string.classroom_tip_04_03);
        TimeMessage tms5 = new TimeMessage();
        tms5.message = getString(R.string.classroom_tip_05);
        tms5.hasKonwButton = false;
        timeMessages.add(tms1);
        timeMessages.add(tms2);
        timeMessages.add(tms3);
        timeMessages.add(tms4);
        timeMessages.add(tms5);
    }
    
    //点击通知进入一个Activity，点击返回时进入指定页面。
    public void resultActivityBackApp() {
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setTicker(getString(R.string.app_name));
        //        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setContentText(getString(R.string.back_hint));
        //设置点击一次后消失（如果没有点击事件，则该方法无效。）
        mBuilder.setAutoCancel(true);
        //点击通知之后需要跳转的页面
        Intent resultIntent = new Intent(this, OneToOneThreeActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pIntent);
    }
    
    private void initVideoItem() {
        //老师视频
        vdi_teacher = (LinearLayout) findViewById(R.id.vdi_teacher);
        teacherItem = new VideoItem();
        teacherItem.parent = vdi_teacher;
        teacherItem.sf_video = (SurfaceViewRenderer) vdi_teacher.findViewById(R.id.sf_video);
        teacherItem.sf_video.init(EglBase.create().getEglBaseContext(), null);
        teacherItem.sf_video.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        teacherItem.img_mic = (ImageView) vdi_teacher.findViewById(R.id.img_mic);
        teacherItem.img_pen = (ImageView) vdi_teacher.findViewById(R.id.img_pen);
        teacherItem.cv_draw = (CircleView) vdi_teacher.findViewById(R.id.cv_draw);
        teacherItem.img_hand = (ImageView) vdi_teacher.findViewById(R.id.img_hand_up);
        teacherItem.txt_name = (AutoFitTextView) vdi_teacher.findViewById(R.id.txt_name);
        teacherItem.txt_gift_num = (AutoFitTextView) vdi_teacher.findViewById(R.id.txt_gift_num);
        teacherItem.rel_group = (RelativeLayout) vdi_teacher.findViewById(R.id.rel_group);
        teacherItem.img_video_back = (ImageView) vdi_teacher.findViewById(R.id.img_video_back);
        teacherItem.img_video_back.setVisibility(View.VISIBLE);
        teacherItem.lin_gift = (LinearLayout) vdi_teacher.findViewById(R.id.lin_gift);
        teacherItem.icon_gif = (ImageView) vdi_teacher.findViewById(R.id.icon_gif);
        teacherItem.lin_name_label = (RelativeLayout) vdi_teacher.findViewById(R.id.lin_name_label);
        teacherItem.rel_video_label = (RelativeLayout) vdi_teacher.findViewById(R.id.rel_video_label);
        teacherItem.re_background = (RelativeLayout) vdi_teacher.findViewById(R.id.re_background);
        teacherItem.tv_home = (TextView) vdi_teacher.findViewById(R.id.tv_home);
        teacherItem.lin_gift.setVisibility(View.INVISIBLE);
        teacherItem.img_pen.setVisibility(View.INVISIBLE);
        teacherItem.cv_draw.setVisibility(View.INVISIBLE);
        teacherItem.bg_video_back = (ImageView) vdi_teacher.findViewById(R.id.bg_video_back);
        teacherItem.volumeView = (VolumeView) vdi_teacher.findViewById(R.id.volume);
        
        //学生视频
        vdi_stu_in_sd = (LinearLayout) findViewById(R.id.vdi_stu_in_sd);
        stu_in_sd = new VideoItem();
        stu_in_sd.parent = vdi_stu_in_sd;
        stu_in_sd.sf_video = (SurfaceViewRenderer) vdi_stu_in_sd.findViewById(R.id.sf_video);
        stu_in_sd.sf_video.init(EglBase.create().getEglBaseContext(), null);
        stu_in_sd.sf_video.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        stu_in_sd.img_mic = (ImageView) vdi_stu_in_sd.findViewById(R.id.img_mic);
        stu_in_sd.img_pen = (ImageView) vdi_stu_in_sd.findViewById(R.id.img_pen);
        stu_in_sd.cv_draw = (CircleView) vdi_stu_in_sd.findViewById(R.id.cv_draw);
        stu_in_sd.img_hand = (ImageView) vdi_stu_in_sd.findViewById(R.id.img_hand_up);
        stu_in_sd.txt_name = (AutoFitTextView) vdi_stu_in_sd.findViewById(R.id.txt_name);
        stu_in_sd.txt_gift_num = (AutoFitTextView) vdi_stu_in_sd.findViewById(R.id.txt_gift_num);
        stu_in_sd.rel_group = (RelativeLayout) vdi_stu_in_sd.findViewById(R.id.rel_group);
        stu_in_sd.img_video_back = (ImageView) vdi_stu_in_sd.findViewById(R.id.img_video_back);
        stu_in_sd.img_video_back.setVisibility(View.VISIBLE);
        stu_in_sd.lin_gift = (LinearLayout) vdi_stu_in_sd.findViewById(R.id.lin_gift);
        stu_in_sd.icon_gif = (ImageView) vdi_stu_in_sd.findViewById(R.id.icon_gif);
        stu_in_sd.lin_name_label = (RelativeLayout) vdi_stu_in_sd.findViewById(R.id.lin_name_label);
        stu_in_sd.rel_video_label = (RelativeLayout) vdi_stu_in_sd.findViewById(R.id.rel_video_label);
        stu_in_sd.re_background = (RelativeLayout) vdi_stu_in_sd.findViewById(R.id.re_background);
        stu_in_sd.tv_home = (TextView) vdi_stu_in_sd.findViewById(R.id.tv_home);
        stu_in_sd.bg_video_back = (ImageView) vdi_stu_in_sd.findViewById(R.id.bg_video_back);
        stu_in_sd.volumeView = (VolumeView) vdi_stu_in_sd.findViewById(R.id.volume);
        
    }
    
    public void showExitDialog() {
        Tools.threeshowDialog(OneToOneThreeActivity.this, R.string.remind, R.string.logouts, new Tools.OnDialogClick() {
            @Override
            public void dialog_ok(Dialog dialog) {
                sendGiftPopUtils.deleteImage();
                RoomClient.getInstance().setExit(true);
                TKRoomManager.getInstance().leaveRoom();
                dialog.dismiss();
            }
        }, 0);
    }
    
    //下课
    public void showClassDissMissDialog() {
        Tools.threeshowDialog(OneToOneThreeActivity.this, R.string.remind, R.string.make_sure_class_dissmiss, new Tools.OnDialogClick() {
            @Override
            public void dialog_ok(Dialog dialog) {
                try {
                    TKRoomManager.getInstance().delMsg("ClassBegin", "ClassBegin", "__all", new JSONObject().put("recordchat", true).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                txt_class_begin.setVisibility(View.INVISIBLE);
                RoomSession.getInstance().sendClassDissToPhp();
                dialog.dismiss();
            }
        }, 0);
        
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
        }
        return super.onKeyDown(keyCode, event);
    }
    
    /**
     * 清空未读消息数
     */
    private void clearNoReadChatMessage() {
        RoomSession.chatDataCache.clear();
        if (tv_no_read_message_number != null) {
            tv_no_read_message_number.setVisibility(View.INVISIBLE);
            tv_no_read_message_number.setText("");
        }
    }
    
    private void showPhotoControlPop() {
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.three_pop_photo_control, null);
        LinearLayout ll_popu_camera = (LinearLayout) contentView.findViewById(R.id.ll_popu_camera);
        LinearLayout ll_popu_selectphoto = (LinearLayout) contentView.findViewById(R.id.ll_popu_selectphoto);
        popupWindowPhoto = new PopupWindow(80, ScreenScale.getScreenHeight() / 6);
        popupWindowPhoto.setContentView(contentView);
        ll_popu_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPauseLocalVideo) {
                    TKRoomManager.getInstance().pauseLocalCamera();
                    isPauseLocalVideo = !isPauseLocalVideo;
                }
                isOpenCamera = true;
                isBackApp = true;
                PhotoUtils.openCamera(OneToOneThreeActivity.this);
                popupWindowPhoto.dismiss();
            }
        });
        ll_popu_selectphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBackApp = true;
                PhotoUtils.openAlbum(OneToOneThreeActivity.this);
                popupWindowPhoto.dismiss();
            }
        });
        popupWindowPhoto.setBackgroundDrawable(new BitmapDrawable());
        popupWindowPhoto.setFocusable(true);
        popupWindowPhoto.setOutsideTouchable(true);
        
        int[] width_and_height = new int[2];
        cb_choose_photo.getLocationInWindow(width_and_height);
        
        popupWindowPhoto.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (cb_choose_photo != null) {
                    cb_choose_photo.setChecked(false);
                }
            }
        });
        
        popupWindowPhoto.showAtLocation(cb_choose_photo, Gravity.NO_GRAVITY, width_and_height[0] - (Math.abs((cb_choose_photo.getWidth() - popupWindowPhoto.getWidth()) / 2)), width_and_height[1] + cb_choose_photo.getHeight());
    }
    
    private void setWhiteBoradEnlarge(boolean isZoom) {
        
        teacherItem.sf_video.setVisibility(View.INVISIBLE);
        teacherItem.sf_video.setZOrderMediaOverlay(false);
        
        stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
        stu_in_sd.sf_video.setZOrderMediaOverlay(false);
        
        lin_menu.setVisibility(View.GONE);
        lin_audio_control.setVisibility(View.GONE);
        rel_tool_bar.setVisibility(View.GONE);
        stu_in_sd.rel_group.setVisibility(View.GONE);
        stu_in_sd.sf_video.setVisibility(View.GONE);
        stu_in_sd.bg_video_back.setVisibility(View.VISIBLE);
        teacherItem.rel_group.setVisibility(View.GONE);
        teacherItem.sf_video.setVisibility(View.GONE);
        teacherItem.bg_video_back.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams rel_wb_param = (LinearLayout.LayoutParams) ll_wb_container.getLayoutParams();
        rel_wb_param.width = LinearLayout.LayoutParams.MATCH_PARENT;
        rel_wb_param.height = LinearLayout.LayoutParams.MATCH_PARENT;
        ll_wb_container.setLayoutParams(rel_wb_param);
        rl_message.setVisibility(View.GONE);
        WhiteBoradConfig.getsInstance().sendJSPageFullScreen(isZoom);
    }
    
    private void setWhiteBoradNarrow(boolean isZoom) {

        /*teacherItem.sf_video.setVisibility(View.VISIBLE);
        teacherItem.sf_video.setZOrderMediaOverlay(true);*/
        /*TKRoomManager.getInstance().playVideo(teacherItem.peerid, teacherItem.sf_video, RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);*/
        
        //        stu_in_sd.sf_video.setVisibility(View.VISIBLE);
        //        stu_in_sd.sf_video.setZOrderMediaOverlay(true);
        //        TKRoomManager.getInstance().playVideo(stu_in_sd.peerid,stu_in_sd.sf_video,RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
        
        lin_menu.setVisibility(View.VISIBLE);
        rel_tool_bar.setVisibility(View.VISIBLE);
        
        if (!stu_in_sd.peerid.isEmpty()) {
            stu_in_sd.rel_group.setVisibility(View.VISIBLE);
        }
        
        teacherItem.rel_group.setVisibility(View.VISIBLE);
        
        if (TKRoomManager.getInstance().getMySelf().role == 0 && RoomSession.isPublish &&
            !Tools.isMp4(WhiteBoradConfig.getsInstance().getCurrentMediaDoc().getFilename())) {
            lin_audio_control.setVisibility(View.VISIBLE);
        } else {
            lin_audio_control.setVisibility(View.INVISIBLE);
        }
        
        if (!RoomSession.isClassBegin) {
            playSelfBeforeClassBegin();
        }
        
        if (TKRoomManager.getInstance().getMySelf() != null && !TextUtils.isEmpty(TKRoomManager.getInstance().getMySelf().peerId)) {
            doPlayVideo(TKRoomManager.getInstance().getMySelf().peerId);
        }
        
        if (TKRoomManager.getInstance().getMySelf().role == 0) {
            rl_member_list.setVisibility(View.VISIBLE);
            cb_file_person_media_list.setVisibility(View.VISIBLE);
            if (RoomSession.isClassBegin) {
                cb_tool_case.setVisibility(View.VISIBLE);
            } else {
                cb_tool_case.setVisibility(View.GONE);
            }
            
            cb_control.setVisibility(View.GONE);
            rl_message.setVisibility(View.VISIBLE);
        } else if (TKRoomManager.getInstance().getMySelf().role == 2) {
            rl_member_list.setVisibility(View.GONE);
            cb_file_person_media_list.setVisibility(View.GONE);
            cb_tool_case.setVisibility(View.GONE);
            cb_control.setVisibility(View.GONE);
            rl_message.setVisibility(View.VISIBLE);
        } else if (TKRoomManager.getInstance().getMySelf().role == 4) {
            rl_member_list.setVisibility(View.VISIBLE);
            cb_file_person_media_list.setVisibility(View.VISIBLE);
            cb_tool_case.setVisibility(View.GONE);
            cb_control.setVisibility(View.GONE);
            rl_message.setVisibility(View.VISIBLE);
        }
        WhiteBoradConfig.getsInstance().sendJSPageFullScreen(isZoom);
        doLayout();
    }
    
    private void playSelfBeforeClassBegin() {
        RoomUser me = TKRoomManager.getInstance().getMySelf();
        /* me.publishState = 3;*/
        if (TKRoomManager.getInstance().getMySelf().role == 0) {
            doTeacherVideoPlay(me);
            teacherItem.rel_group.setVisibility(View.VISIBLE);
            teacherItem.sf_video.setVisibility(View.VISIBLE);
            teacherItem.bg_video_back.setVisibility(View.GONE);
            teacherItem.img_video_back.setVisibility(View.GONE);
            if (RoomControler.isReleasedBeforeClass() && me.publishState == 0) {
                TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId,
                    "__all", "publishstate", 3);
            } else {
                teacherItem.txt_name.setText(me.nickName);
                teacherItem.sf_video.setVisibility(View.VISIBLE);
                teacherItem.bg_video_back.setVisibility(View.GONE);
                TKRoomManager.getInstance().playVideo(me.peerId, teacherItem.sf_video, RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
            }
            
            if (stu_in_sd.rel_group != null && !RoomSession.isClassBegin /*&& !RoomControler.isReleasedBeforeClass()*/) {
                stu_in_sd.rel_group.setVisibility(View.VISIBLE);
                stu_in_sd.img_video_back.setImageResource(R.drawable.three_zhanwei_student);
                stu_in_sd.bg_video_back.setVisibility(View.VISIBLE);
                stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
                stu_in_sd.lin_name_label.setVisibility(View.INVISIBLE);
                stu_in_sd.img_mic.setVisibility(View.INVISIBLE);
                stu_in_sd.img_hand.setVisibility(View.INVISIBLE);
                stu_in_sd.img_pen.setVisibility(View.INVISIBLE);
                stu_in_sd.cv_draw.setVisibility(View.INVISIBLE);
                stu_in_sd.lin_gift.setVisibility(View.INVISIBLE);
            }
        } else if (TKRoomManager.getInstance().getMySelf().role == 2) {
            if (RoomSession.roomType == 0) {
                if (stu_in_sd.sf_video != null) {
                    if (!RoomSession.isClassBegin) {
                        teacherItem.rel_group.setVisibility(View.VISIBLE);
                        teacherItem.img_video_back.setImageResource(R.drawable.three_zhanwei_teacher);
                        teacherItem.bg_video_back.setVisibility(View.VISIBLE);
                        teacherItem.sf_video.setVisibility(View.INVISIBLE);
                        teacherItem.lin_name_label.setVisibility(View.INVISIBLE);
                        teacherItem.img_mic.setVisibility(View.INVISIBLE);
                        teacherItem.img_hand.setVisibility(View.INVISIBLE);
                        teacherItem.img_pen.setVisibility(View.INVISIBLE);
                        teacherItem.cv_draw.setVisibility(View.INVISIBLE);
                    }
                    stu_in_sd.txt_name.setText(TKRoomManager.getInstance().getMySelf().nickName);
                    if (RoomControler.isReleasedBeforeClass()) {
                        if (me.publishState > 1 && me.publishState < 4 && !me.disablevideo) {
                            stu_in_sd.sf_video.setVisibility(View.VISIBLE);
                            stu_in_sd.bg_video_back.setVisibility(View.GONE);
                        } else {
                            stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
                            stu_in_sd.bg_video_back.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (me.hasVideo) {
                            stu_in_sd.sf_video.setVisibility(View.VISIBLE);
                            stu_in_sd.bg_video_back.setVisibility(View.GONE);
                        } else {
                            stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
                            stu_in_sd.img_video_back.setImageResource(R.drawable.three_icon_no_camera);
                            stu_in_sd.bg_video_back.setVisibility(View.VISIBLE);
                        }
                    }
                    if (RoomControler.isReleasedBeforeClass() && me.publishState == 0) {
                        TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId,
                            "__all", "publishstate", 3);
                    } else {
                        TKRoomManager.getInstance().playVideo(me.peerId, stu_in_sd.sf_video, RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                    }
                }
            }
        } else if (TKRoomManager.getInstance().getMySelf().role == 4) {
            if (!RoomSession.isClassBegin) {
                teacherItem.rel_group.setVisibility(View.INVISIBLE);
                stu_in_sd.rel_group.setVisibility(View.INVISIBLE);
                stu_in_sd.lin_gift.setVisibility(View.INVISIBLE);
                teacherItem.bg_video_back.setVisibility(View.VISIBLE);
                teacherItem.img_video_back.setImageResource(R.drawable.three_zhanwei_teacher);
                stu_in_sd.bg_video_back.setVisibility(View.VISIBLE);
            }
        }
    }
    
    private void onFirstVideoFrame(String peerIdVideo) {
        if (videofragment != null) {
            videofragment.hideLaoding(peerIdVideo);
        }
    }
    
    private void onAudioVolume(String volumePeerId, int volume) {
        RoomUser roomUser = TKRoomManager.getInstance().getUser(volumePeerId);
        if (roomUser != null && roomUser.role == 0) {
            
            if (roomUser.getPublishState() == 1 || roomUser.getPublishState() == 3) {
                teacherItem.img_mic.setImageResource(R.drawable.three_icon_sound);
                teacherItem.img_mic.setVisibility(View.VISIBLE);
                teacherItem.volumeView.setVisibility(View.VISIBLE);
                if (volume <= 5) {
                    teacherItem.volumeView.setIndex(0);
                } else if (volume > 5 && volume < 5000) {
                    teacherItem.volumeView.setIndex(1);
                } else if (volume > 5000 && volume < 10000) {
                    teacherItem.volumeView.setIndex(2);
                } else if (volume > 10000 && volume < 20000) {
                    teacherItem.volumeView.setIndex(3);
                } else if (volume > 20000 && volume < 30000) {
                    teacherItem.volumeView.setIndex(4);
                }
            } else {
                teacherItem.img_mic.setImageResource(R.drawable.three_img_mic_ban);
                teacherItem.volumeView.setVisibility(View.GONE);
            }
        } else if (roomUser != null && roomUser.role == 2) {
            if (roomUser.getPublishState() == 1 || roomUser.getPublishState() == 3) {
                stu_in_sd.img_mic.setImageResource(R.drawable.three_icon_sound);
                stu_in_sd.volumeView.setVisibility(View.VISIBLE);
                if (volume <= 5) {
                    stu_in_sd.volumeView.setIndex(0);
                } else if (volume > 5 && volume < 5000) {
                    stu_in_sd.volumeView.setIndex(1);
                } else if (volume > 5000 && volume < 10000) {
                    stu_in_sd.volumeView.setIndex(2);
                } else if (volume > 10000 && volume < 20000) {
                    stu_in_sd.volumeView.setIndex(3);
                } else if (volume > 20000 && volume < 30000) {
                    stu_in_sd.volumeView.setIndex(4);
                }
            } else {
                if (roomUser.getPublishState() == 0) {
                    stu_in_sd.img_mic.setImageResource(R.drawable.three_img_mic_ban);
                    stu_in_sd.volumeView.setVisibility(View.GONE);
                }
            }
        }
    }
    
    public void setFullscreen_video_param(RelativeLayout.LayoutParams fullscreen_video_param) {
        this.fullscreen_video_param = fullscreen_video_param;
    }
    
    public void setZoom(boolean zoom) {
        isZoom = zoom;
    }
    
    public boolean getIsOneToOne() {
        return isOneToOne;
    }
    
    public RelativeLayout.LayoutParams getFullscreen_video_param() {
        return fullscreen_video_param;
    }
    
    public boolean getZoom() {
        return isZoom;
    }
    
    public void setOneToOne(boolean oneToOne) {
        isOneToOne = oneToOne;
    }
    
    public void removeVideoFragment() {
        videofragment = VideoFragment.getInstance();
       /* videofragment.setUseFullscreen(true);*/
        fragmentManager = getFragmentManager();
        ft = fragmentManager.beginTransaction();
        
        //        if (videofragment.isAdded()) {
        mediaListAdapter.setLocalfileid(-1);
        ft.remove(videofragment);
        ft.commitAllowingStateLoss();
        //*ft.commit();
        videofragment = null;
        //        }
        
        if (RoomSession.isClassBegin) {
            
            if (teacherItem.peerid != null && !teacherItem.peerid.isEmpty()) {
                RoomUser teacherUser = TKRoomManager.getInstance().getUser(teacherItem.peerid);
                if (teacherUser != null) {
                    if (teacherUser.getPublishState() == 2 || teacherUser.getPublishState() == 3) {
                        teacherItem.sf_video.setVisibility(View.VISIBLE);
                        teacherItem.sf_video.setZOrderMediaOverlay(true);
                    }
                }
            }
            
            if (stu_in_sd.peerid != null && !stu_in_sd.peerid.isEmpty()) {
                RoomUser stuUser = TKRoomManager.getInstance().getUser(stu_in_sd.peerid);
                if (stuUser != null) {
                    if (stuUser.getPublishState() == 2 || stuUser.getPublishState() == 3) {
                        stu_in_sd.sf_video.setVisibility(View.VISIBLE);
                        stu_in_sd.sf_video.setZOrderMediaOverlay(true);
                    }
                }
            }
        }
        
        if (isZoom && RoomControler.isFullScreenVideo()) {
            fullscreen_sf_video.setZOrderMediaOverlay(true);
            fullscreen_sf_video.setVisibility(View.VISIBLE);
            fullscreen_bg_video_back.setVisibility(View.GONE);
            fullscreen_img_video_back.setVisibility(View.GONE);
            if (TKRoomManager.getInstance().getMySelf().role == 0) {
                if (!TextUtils.isEmpty(stu_in_sd.peerid)) {
                    TKRoomManager.getInstance().playVideo(stu_in_sd.peerid, fullscreen_sf_video,
                        RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                }
            } else {
                if (!TextUtils.isEmpty(teacherItem.peerid)) {
                    TKRoomManager.getInstance().playVideo(teacherItem.peerid, fullscreen_sf_video,
                        RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                }
            }
        }
    }
    
    public void readyForPlayVideo(String shareMediaPeerId, Map<String, Object> shareMediaAttrs) {
        setFullscreen_video_param(fullscreen_video_param);
        setZoom(RoomSession.fullScreen);
        setOneToOne(isOneToOne);
        
        if (RoomSession.isClassBegin) {
            teacherItem.sf_video.setVisibility(View.INVISIBLE);
            teacherItem.sf_video.setZOrderMediaOverlay(false);
            
            stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
            stu_in_sd.sf_video.setZOrderMediaOverlay(false);
        }
        
        if (isZoom && RoomSession.isClassBegin && RoomControler.isFullScreenVideo()) {
            fullscreen_sf_video.setZOrderMediaOverlay(false);
            fullscreen_sf_video.setVisibility(View.INVISIBLE);
            fullscreen_img_video_back.setVisibility(View.VISIBLE);
            fullscreen_bg_video_back.setVisibility(View.VISIBLE);
        }
        
        videofragment = VideoFragment.getInstance();
        /*if (isZoom && RoomControler.isFullScreenVideo()) {
            videofragment.setUseFullscreen(false);
        }*/
        videofragment.setStream(shareMediaPeerId, shareMediaAttrs);
        fragmentManager = getFragmentManager();
        ft = fragmentManager.beginTransaction();
        ft.replace(R.id.video_container, videofragment);
        ft.commitAllowingStateLoss();
    }
    
    private void onShareFileState(String peerIdShareFile, int stateShareFile) {
        setFullscreen_video_param(fullscreen_video_param);
        setZoom(RoomSession.fullScreen);
        setOneToOne(isOneToOne);
        movieFragment = MovieFragment.getInstance();
        fragmentManager = getFragmentManager();
        ft = fragmentManager.beginTransaction();
        if (stateShareFile == 0) {
            if (movieFragment.isAdded()) {
                ft.remove(movieFragment);
                ft.commitAllowingStateLoss();
            }
            movieFragment = null;
            if (isZoom && RoomControler.isFullScreenVideo()) {
                fullscreen_sf_video.setZOrderMediaOverlay(true);
                fullscreen_sf_video.setVisibility(View.VISIBLE);
                fullscreen_bg_video_back.setVisibility(View.GONE);
                fullscreen_img_video_back.setVisibility(View.GONE);
            /*if (TKRoomManager.getInstance().getMySelf().role == 0) {
                if (!TextUtils.isEmpty(stu_in_sd.peerid)) {
                    TKRoomManager.getInstance().playVideo(stu_in_sd.peerid, fullscreen_sf_video,
                            RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                }
            } else {
                if (!TextUtils.isEmpty(teacherItem.peerid)) {
                    TKRoomManager.getInstance().playVideo(teacherItem.peerid, fullscreen_sf_video,
                            RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                }
            }*/
            }
        } else if (stateShareFile == 1) {
           /* teacherItem.sf_video.setVisibility(View.INVISIBLE);
            teacherItem.sf_video.setZOrderMediaOverlay(false);
            *//*TKRoomManager.getInstance().unPlayVideo(teacherItem.peerid);*//*

            stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
            stu_in_sd.sf_video.setZOrderMediaOverlay(false);
            *//*TKRoomManager.getInstance().unPlayVideo(stu_in_sd.peerid);*/
            
            if (isZoom && RoomSession.isClassBegin && RoomControler.isFullScreenVideo()) {
                fullscreen_sf_video.setZOrderMediaOverlay(false);
                fullscreen_sf_video.setVisibility(View.INVISIBLE);
                fullscreen_img_video_back.setVisibility(View.VISIBLE);
                fullscreen_bg_video_back.setVisibility(View.VISIBLE);
            }
            
            if (wbFragment != null) {
                WhiteBoradConfig.getsInstance().closeNewPptVideo();
            }
            
            movieFragment.setShareFilePeerId(peerIdShareFile);
            if (!movieFragment.isAdded()) {
                ft.replace(R.id.video_container, movieFragment);
                ft.commitAllowingStateLoss();
            }
        }
    }
    
    private void onShareScreenState(String peerIdScreen, int stateScreen) {
        screenFragment = ScreenFragment.getInstance();
        fragmentManager = getFragmentManager();
        ft = fragmentManager.beginTransaction();
        
        if (stateScreen == 0) {
            if (screenFragment.isAdded()) {
                ft.remove(screenFragment);
                ft.commitAllowingStateLoss();
            }
        } else {
            if (wbFragment != null) {
                WhiteBoradConfig.getsInstance().closeNewPptVideo();
            }
            screenFragment.setPeerId(peerIdScreen);
            if (!screenFragment.isAdded()) {
                ft.replace(R.id.video_container, screenFragment);
                ft.commitAllowingStateLoss();
            }
        }
    }
    
    private void onShareMediaState(String shareMediaPeerId, int shareMediaState, Map<String, Object> shareMediaAttrs) {
        
        this.mediaAttrs = shareMediaAttrs;
        this.mediaPeerId = shareMediaPeerId;
        
        if (shareMediaState == 0) {    //媒体结束播放
            
            mediaListAdapter.setLocalfileid(-1);
            mp3Duration = "00:00";
            
            if (shareMediaAttrs.containsKey("video")) {
                if ((boolean) shareMediaAttrs.get("video")) {
                    removeVideoFragment();
                } else {
                    lin_audio_control.setVisibility(View.INVISIBLE);
                    img_disk.clearAnimation();
                    img_disk.setVisibility(View.INVISIBLE);
                }
            }
            ShareDoc media = WhiteBoradConfig.getsInstance().getCurrentMediaDoc();
            
            if (RoomSession.isPlay) {
                RoomSession.isPlay = false;
                
                if (!RoomControler.isDocumentClassification()) {
                    if (media.getFileid() == mediaListAdapter.getLocalfileid()) {
                        return;
                    }
                    mediaListAdapter.setLocalfileid(media.getFileid());
                }
                
                WhiteBoradConfig.getsInstance().setCurrentMediaDoc(media);
                String strSwfpath = media.getSwfpath();
                int pos = strSwfpath.lastIndexOf('.');
                strSwfpath = String.format("%s-%d%s", strSwfpath.substring(0, pos), 1, strSwfpath.substring(pos));
                String url = "http://" + WhiteBoradConfig.getsInstance().getFileServierUrl() + ":" + WhiteBoradConfig.getsInstance().getFileServierPort() + strSwfpath;
                HashMap<String, Object> attrMap = new HashMap<String, Object>();
                attrMap.put("filename", media.getFilename());
                attrMap.put("fileid", fileid);
                
                if (RoomSession.isClassBegin) {
                    TKRoomManager.getInstance().startShareMedia(url, Tools.isMp4(media.getFiletype()), "__all", attrMap);
                } else {
                    TKRoomManager.getInstance().startShareMedia(url, Tools.isMp4(media.getFiletype()), TKRoomManager.getInstance().getMySelf().peerId, attrMap);
                }
            }
            if (isWBMediaPlay) {
                
                HashMap<String, Object> attrMap = new HashMap<String, Object>();
                attrMap.put("filename", "");
                attrMap.put("fileid", fileid);
                if (RoomSession.isClassBegin) {
                    TKRoomManager.getInstance().startShareMedia(url, Tools.isMp4(media.getFiletype()), "__all", attrMap);
                } else {
                    TKRoomManager.getInstance().startShareMedia(url, Tools.isMp4(media.getFiletype()), TKRoomManager.getInstance().getMySelf().peerId, attrMap);
                }
                isWBMediaPlay = false;
            }
            
        } else if (shareMediaState == 1) {    //媒体开始播放
            
            if (wbFragment != null) {
                WhiteBoradConfig.getsInstance().closeNewPptVideo();
            }
            
            isWBMediaPlay = false;
            isMediaMute = false;
            Object objfileid = shareMediaAttrs.get("fileid");
            long fileid = -1;
            if (objfileid != null) {
                if (objfileid instanceof String) {
                    fileid = Long.valueOf(objfileid.toString());
                } else if (objfileid instanceof Number) {
                    fileid = ((Number) objfileid).longValue();
                }
            }
            mediaListAdapter.setLocalfileid(fileid);
            
            if (shareMediaAttrs.containsKey("video")) {
                if ((boolean) shareMediaAttrs.get("video")) {
                    readyForPlayVideo(shareMediaPeerId, shareMediaAttrs);
                } else {
                    if (TKRoomManager.getInstance().getMySelf().role == 0) {
                        lin_audio_control.setVisibility(View.VISIBLE);
                        img_disk.setVisibility(View.VISIBLE);
                        gifDrawable.start();
                    } else {
                        lin_audio_control.setVisibility(View.INVISIBLE);
                        img_disk.setVisibility(View.VISIBLE);
                        boolean ispause = shareMediaAttrs.get("pause") == null ? false : (boolean) shareMediaAttrs.get("pause");
                        if (ispause) {
                            gifDrawable.stop();
                        } else {
                            gifDrawable.start();
                        }
                    }
                    img_voice_mp3.setImageResource(R.drawable.icon_voice);
                    vol = 0.5;
                    sek_voice_mp3.setProgress((int) (vol * 100));
                    int da = (int) shareMediaAttrs.get("duration");
                    SimpleDateFormat formatter = new SimpleDateFormat("mm:ss ");
                    Date daDate = new Date(da);
                    mp3Duration = formatter.format(daDate);
                    txt_mp3_time.setText("00:00" + "/" + mp3Duration);
                    if (txt_mp3_name != null) {
                        txt_mp3_name.setText((String) shareMediaAttrs.get("filename"));
                    }
                }
            }
        }
    }
    
    private void onPlayBackEnd() {
        postionPlayBack = 0.0;
        img_play_back.setImageResource(R.drawable.btn_play_normal);
        sek_play_back.setProgress(0);
        isPlayBackPlay = false;
        isEnd = true;
    }
    
    private void onPlayBackDuration(long startTime, long endTime) {
        this.starttime = startTime;
        this.endtime = endTime;
    }
    
    private void onPlayBackClearAll() {
        postionPlayBack = 0.0;
        if (chlistAdapter != null) {
            chlistAdapter.notifyDataSetChanged();
        }
    }
    
    private void onPlayBackUpdateTime(long backTimePos) {
        this.currenttime = backTimePos;
        double pos = (double) (currenttime - starttime) / (double) (endtime - starttime);
        if (pos < postionPlayBack) {
            pos = postionPlayBack;
            long postionTime = (long) (((double) postionPlayBack) * (endtime - starttime) + starttime);
            TKPlayBackManager.getInstance().seekPlayback(postionTime);
        } else {
            postionPlayBack = pos;
        }
        sek_play_back.setProgress((int) (pos * 100));
        
        String strcur = Tools.secToTime(currenttime - starttime);
        String strda = Tools.secToTime(endtime - starttime);
        txt_play_back_time.setText(strcur + "/" + strda);
    }
    
    private void onUpdateAttributeStream(String attributesPeerId, long streamPos, Boolean isPlay, Map<String, Object> dateAttributeAttrs) {
        
        this.onUpdateAttributeAttrs = dateAttributeAttrs;
        this.updateAttributespeerId = attributesPeerId;
        
        if (dateAttributeAttrs.containsKey("video") && (boolean) dateAttributeAttrs.get("video")) {
            if (videofragment == null) {
                readyForPlayVideo(attributesPeerId, dateAttributeAttrs);
                if (wbFragment != null) {
                    WhiteBoradConfig.getsInstance().closeNewPptVideo();
                }
                
                RoomSession.isPublish = true;
                RoomSession.isPlay = false;
                isWBMediaPlay = false;
                isMediaMute = false;
                Object objfileid = dateAttributeAttrs.get("fileid");
                long fileid = -1;
                if (objfileid != null) {
                    if (objfileid instanceof String) {
                        fileid = Long.valueOf(objfileid.toString());
                    } else if (objfileid instanceof Number) {
                        fileid = ((Number) objfileid).longValue();
                    }
                }
                mediaListAdapter.setLocalfileid(fileid);
            } else {
                videofragment.controlMedia(dateAttributeAttrs, streamPos, isPlay);
            }
        } else {
            if (sek_mp3 != null) {
                int curtime = (int) ((double) streamPos / (int) dateAttributeAttrs.get("duration") * 100);
                sek_mp3.setProgress(curtime);
            }
            if (img_play_mp3 != null) {
                if (!isPlay) {
                    img_play_mp3.setImageResource(R.drawable.btn_pause_normal);
                    gifDrawable.start();
                } else {
                    img_play_mp3.setImageResource(R.drawable.btn_play_normal);
                    gifDrawable.stop();
                }
            }
            if (txt_mp3_time != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("mm:ss ");
                Date curDate = new Date(streamPos);//获取当前时间
                Date daDate = new Date((int) dateAttributeAttrs.get("duration"));
                String strcur = formatter.format(curDate);
                String strda = formatter.format(daDate);
                txt_mp3_time.setText(strcur + "/" + strda);
            }
            if (txt_mp3_name != null) {
                txt_mp3_name.setText((String) dateAttributeAttrs.get("filename"));
            }
        }
    }
    
    private void unPlaySelfAfterClassBegin() {
        if (RoomSession.roomType == 0) {
            
            RoomUser roomUser = TKRoomManager.getInstance().getUser(TKRoomManager.getInstance().getMySelf().peerId);
            if (roomUser == null) {
                return;
            }
            
            if (roomUser.role == 2 && roomUser.getPublishState() == 0) {
                TKRoomManager.getInstance().unPlayVideo(TKRoomManager.getInstance().getMySelf().peerId);
                stu_in_sd.sf_video.setVisibility(View.GONE);
                stu_in_sd.lin_name_label.setVisibility(View.GONE);
                stu_in_sd.img_video_back.setVisibility(View.VISIBLE);
            }
        }
    }
    
    private void changeUserState(final RoomUser user) {
        if (user.role == 0) {
            teacherItem.img_pen.setVisibility(View.INVISIBLE);
            teacherItem.cv_draw.setVisibility(View.INVISIBLE);
            teacherItem.img_hand.setVisibility(View.INVISIBLE);
            teacherItem.peerid = user.peerId;
            
            if (user.disablevideo) {
                teacherItem.img_video_back.setImageResource(R.drawable.three_icon_no_camera);
                teacherItem.bg_video_back.setVisibility(View.VISIBLE);
            } else {
                if (user.getPublishState() == 1) {
                    teacherItem.img_video_back.setImageResource(R.drawable.three_icon_camera_close);
                    teacherItem.bg_video_back.setVisibility(View.VISIBLE);
                }
                
            }
            
            if (user.disableaudio) {
                teacherItem.img_mic.setImageResource(R.drawable.three_img_mic_ban);
                teacherItem.img_mic.setVisibility(View.VISIBLE);
                stu_in_sd.volumeView.setVisibility(View.GONE);
            } else {
                if (user.getPublishState() == 0 || user.getPublishState() == 2 || user.getPublishState() == 4) {
                    if (RoomSession.isClassBegin) {
                        teacherItem.img_mic.setVisibility(View.VISIBLE);
                        teacherItem.img_mic.setImageResource(R.drawable.three_img_mic_ban);
                        stu_in_sd.volumeView.setVisibility(View.GONE);
                    }
                }
            }
            
            if (user.properties.containsKey("primaryColor")) {
                String primaryColor = (String) user.properties.get("primaryColor");
                if (RoomSession.isClassBegin) {
                    teacherItem.img_pen.setVisibility(View.INVISIBLE);
                    teacherItem.cv_draw.setVisibility(View.VISIBLE);
                } else {
                    teacherItem.img_pen.setVisibility(View.GONE);
                    teacherItem.cv_draw.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(primaryColor)) {
                    teacherItem.cv_draw.setColor(primaryColor);
                }
            } else {
                teacherItem.img_pen.setImageResource(R.drawable.three_icon_shouquan);
            }
            
            
            vdi_teacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user.getPublishState() != 0) {
                        showTeacherControlPop(user);
                    }
                }
            });
        }
        
        if (RoomSession.roomType == 0) {
            if (user.role == 2) {
                stu_in_sd.peerid = user.peerId;
            }
            if (!user.peerId.equals(TKRoomManager.getInstance().getMySelf().peerId) && user.role == TKRoomManager.getInstance().getMySelf().role) {
                return;
            }
            if (user.role == 2) {
                if (user.getPublishState() > 0) {
                    stu_in_sd.rel_group.setVisibility(View.VISIBLE);
                    stu_in_sd.img_mic.setVisibility(View.VISIBLE);
                    stu_in_sd.lin_name_label.setVisibility(View.VISIBLE);
                } else {
                    if (RoomSession.isClassBegin) {
                        stu_in_sd.bg_video_back.setVisibility(View.VISIBLE);
                    }
                    if (isZoom) {
                        if (TKRoomManager.getInstance().getMySelf().role == 0) {
                            fullscreen_sf_video.setVisibility(View.INVISIBLE);
                            fullscreen_bg_video_back.setVisibility(View.VISIBLE);
                            fullscreen_img_video_back.setVisibility(View.VISIBLE);
                        }
                    }
                }
                
                if (!user.hasVideo) {
                    stu_in_sd.img_video_back.setImageResource(R.drawable.three_icon_camera_close);
                    stu_in_sd.bg_video_back.setVisibility(View.VISIBLE);
                }
                
                if (user.hasAudio) {
                    stu_in_sd.img_mic.setImageResource(R.drawable.three_img_mic_ban);
                    stu_in_sd.volumeView.setVisibility(View.GONE);
                } else {
                    if (user.getPublishState() == 0 || user.getPublishState() == 2 || user.getPublishState() == 4) {
                        stu_in_sd.img_mic.setImageResource(R.drawable.three_img_mic_ban);
                        stu_in_sd.volumeView.setVisibility(View.GONE);
                    }
                }
                
                if (user.properties.containsKey("candraw")) {
                    boolean candraw = Tools.isTure(user.properties.get("candraw"));
                    if (candraw) {
                        if (RoomSession.isClassBegin) {
                            stu_in_sd.img_pen.setVisibility(View.VISIBLE);//可以画图
                            stu_in_sd.cv_draw.setVisibility(View.VISIBLE);//可以画图
                        } else {
                            stu_in_sd.img_pen.setVisibility(View.GONE);
                            stu_in_sd.cv_draw.setVisibility(View.GONE);
                        }
                    } else {
                        stu_in_sd.img_pen.setVisibility(View.INVISIBLE);//不可以画图
                        stu_in_sd.cv_draw.setVisibility(View.INVISIBLE);//不可以画图
                    }
                } else {
                    stu_in_sd.img_pen.setVisibility(View.INVISIBLE);//没给过画图权限
                    stu_in_sd.cv_draw.setVisibility(View.INVISIBLE);//没给过画图权限
                }
                
                if (user.properties.containsKey("primaryColor")) {
                    String primaryColor = (String) user.properties.get("primaryColor");
                    if (!TextUtils.isEmpty(primaryColor)) {
                        stu_in_sd.cv_draw.setColor(primaryColor);
                    }
                } else {
                    stu_in_sd.img_pen.setImageResource(R.drawable.three_icon_shouquan);
                }
                
                if (user.properties.containsKey("raisehand")) {
                    boolean israisehand = Tools.isTure(user.properties.get("raisehand"));
                    if (israisehand) {
                        stu_in_sd.img_hand.setVisibility(View.VISIBLE);//正在举手
                    } else {
                        stu_in_sd.img_hand.setVisibility(View.INVISIBLE);//同意了，或者拒绝了
                    }
                } else {
                    stu_in_sd.img_hand.setVisibility(View.INVISIBLE);//还没举手
                }
                
                if (user.properties.containsKey("giftnumber")) {
                    long giftnumber = user.properties.get("giftnumber") instanceof Integer ? (int) user.properties.get("giftnumber") : (long) user.properties.get("giftnumber");
                    stu_in_sd.txt_gift_num.setText(String.valueOf(giftnumber));
                }
                
                stu_in_sd.rel_group.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user.getPublishState() != 0) {
                            showStudentControlPop(user);
                        }
                    }
                });
            }
        }
    }
    
    boolean is_show_student_window = true;
    
    private void showStudentControlPop(final RoomUser user) {
        if (!RoomSession.memberList.contains(user)) {
            return;
        }
        
        if (!RoomSession.memberList.contains(user)) {
            return;
        }
        
        if (!is_show_student_window) {
            is_show_student_window = true;
            return;
        }
        
        if (studentPopupWindow != null && studentPopupWindow.isShowing()) {
            return;
        }
        
        if (!RoomSession.isClassBegin && TKRoomManager.getInstance().getMySelf().role == 0) {
            return;
        }
        if (!(TKRoomManager.getInstance().getMySelf().role == 0) &&
            !user.peerId.endsWith(TKRoomManager.getInstance().getMySelf().peerId)) {
            return;
        }
        if (user.peerId.endsWith(TKRoomManager.getInstance().getMySelf().peerId)
            && !RoomControler.isAllowStudentControlAV()) {
            return;
        }
        if (!RoomSession.isClassBegin) {
            if (!RoomControler.isReleasedBeforeClass()) {
                return;
            }
        }
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_student_control_one_to_one, null);
        //        ScreenScale.scaleView(contentView);
        LinearLayout lin_candraw = (LinearLayout) contentView.findViewById(R.id.lin_candraw);
        LinearLayout lin_audio = (LinearLayout) contentView.findViewById(R.id.lin_audio);
        LinearLayout lin_gift = (LinearLayout) contentView.findViewById(R.id.lin_gift);
        
        final ImageView img_candraw = (ImageView) contentView.findViewById(R.id.img_candraw);
        final ImageView img_audio = (ImageView) contentView.findViewById(R.id.img_audio);
        final TextView txt_candraw = (TextView) contentView.findViewById(R.id.txt_candraw);
        final TextView txt_audio = (TextView) contentView.findViewById(R.id.txt_audio);
        ImageView right_arr = (ImageView) contentView.findViewById(R.id.right_arr);
        right_arr.setVisibility(View.VISIBLE);
        
        
        if (TKRoomManager.getInstance().getMySelf().role == 2) {
            lin_candraw.setVisibility(View.GONE);
        }
        LinearLayout lin_video_control = (LinearLayout) contentView.findViewById(R.id.lin_video_control);
        final ImageView img_video_control = (ImageView) contentView.findViewById(R.id.img_camera);
        final TextView txt_video = (TextView) contentView.findViewById(R.id.txt_camera);
        if (user.role == 1) {
            lin_gift.setVisibility(View.GONE);
        } else {
            if (user.peerId.equals(TKRoomManager.getInstance().getMySelf().peerId)) {
                lin_gift.setVisibility(View.GONE);
            } else {
                lin_gift.setVisibility(View.VISIBLE);
            }
        }
        
        if (user.getPublishState() == 0 || user.getPublishState() == 1 || user.getPublishState() == 4) {
            img_video_control.setImageResource(R.drawable.three_icon_close_vidio);
            txt_video.setText(R.string.video_on);
        } else {
            img_video_control.setImageResource(R.drawable.three_icon_open_vidio);
            txt_video.setText(R.string.video_off);
        }
        
        if (user.disableaudio) {
            lin_audio.setVisibility(View.GONE);
        } else {
            lin_audio.setVisibility(View.VISIBLE);
            if (user.getPublishState() == 0 || user.getPublishState() == 2 || user.getPublishState() == 4) {
                img_audio.setImageResource(R.drawable.three_icon_open_audio);
                txt_audio.setText(R.string.open_audio);
            } else {
                img_audio.setImageResource(R.drawable.three_icon_close_audio);
                txt_audio.setText(R.string.close_audio);
            }
        }
        
        if (user.properties.containsKey("candraw")) {
            boolean candraw = Tools.isTure(user.properties.get("candraw"));
            if (candraw) {
                img_candraw.setImageResource(R.drawable.three_icon_quxiaoshouquan);
                txt_candraw.setText(R.string.no_candraw);
            } else {
                img_candraw.setImageResource(R.drawable.three_icon_shouquan);
                txt_candraw.setText(R.string.candraw);
            }
        } else {
            img_candraw.setImageResource(R.drawable.three_icon_shouquan);
            txt_candraw.setText(R.string.candraw);
        }
        
        //        studentPopupWindow = new PopupWindow(160, ScreenScale.getScreenHeight() / 12 * 8);
        studentPopupWindow = new PopupWindow(KeyBoardUtil.dp2px(OneToOneThreeActivity.this, 80f), KeyBoardUtil.dp2px(OneToOneThreeActivity.this, 260f));
        studentPopupWindow.setContentView(contentView);
        studentPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                is_show_student_window = !Tools.isInView(event, stu_in_sd.rel_group);
                return false;
            }
        });
        
        lin_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getPublishState() == 0 || user.getPublishState() == 2 || user.getPublishState() == 4) {
                    img_audio.setImageResource(R.drawable.three_icon_close_audio);
                    txt_audio.setText(R.string.open_audio);
                    TKRoomManager.getInstance().changeUserProperty(user.peerId, "__all",
                        "publishstate", user.getPublishState() == 0 || user.getPublishState() == 4 ? 1 : 3);
                    TKRoomManager.getInstance().changeUserProperty(user.peerId, "__all", "raisehand", false);
                } else {
                    img_audio.setImageResource(R.drawable.three_icon_open_audio);
                    txt_audio.setText(R.string.close_audio);
                    TKRoomManager.getInstance().changeUserProperty(user.peerId, "__all",
                        "publishstate", user.getPublishState() == 3 ? 2 : 4);
                    
                }
                studentPopupWindow.dismiss();
            }
        });
        
        lin_candraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.properties.containsKey("candraw")) {
                    boolean candraw = Tools.isTure(user.properties.get("candraw"));
                    if (candraw) {   //不可以画图
                        img_candraw.setImageResource(R.drawable.three_icon_shouquan);
                        txt_candraw.setText(R.string.candraw);
                        TKRoomManager.getInstance().changeUserProperty(user.peerId, "__all", "candraw", false);
                    } else {  //可以画图
                        img_candraw.setImageResource(R.drawable.three_icon_quxiaoshouquan);
                        txt_candraw.setText(R.string.no_candraw);
                        TKRoomManager.getInstance().changeUserProperty(user.peerId, "__all", "candraw", true);
                    }
                } else {    //可以画图
                    img_candraw.setImageResource(R.drawable.three_icon_quxiaoshouquan);
                    txt_candraw.setText(R.string.no_candraw);
                    TKRoomManager.getInstance().changeUserProperty(user.peerId, "__all", "candraw", true);
                }
                studentPopupWindow.dismiss();
            }
        });
        
        lin_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, RoomUser> receiverMap = new HashMap<String, RoomUser>();
                receiverMap.put(user.peerId, user);
                if (RoomSession.trophyList.size() > 0) {
                    //自定义奖杯
                    if (huawei || oppo || voio) {
                        sendGiftPopUtils.showSendGiftPop(ll_wb_container.getWidth() / 10 * 6, ll_wb_container.getHeight() / 10 * 6, ll_wb_container, receiverMap, true, webandsufwidth);
                    } else {
                        sendGiftPopUtils.showSendGiftPop(ll_wb_container.getWidth() / 10 * 6, ll_wb_container.getHeight() / 10 * 6, ll_wb_container, receiverMap, false, webandsufwidth);
                    }
                } else {
                    //默认奖杯
                    RoomSession.getInstance().sendGift(receiverMap, null);
                }
                studentPopupWindow.dismiss();
            }
        });
        
        lin_video_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getPublishState() == 0 || user.getPublishState() == 1 || user.getPublishState() == 4) {
                    img_video_control.setImageResource(R.drawable.three_icon_close_vidio);
                    txt_video.setText(R.string.video_off);
                    TKRoomManager.getInstance().changeUserProperty(user.peerId, "__all",
                        "publishstate", user.getPublishState() == 0 || user.getPublishState() == 4 ? 2 : 3);
                } else {
                    img_video_control.setImageResource(R.drawable.three_icon_open_vidio);
                    txt_video.setText(R.string.video_on);
                    TKRoomManager.getInstance().changeUserProperty(user.peerId, "__all",
                        "publishstate", user.getPublishState() == 2 ? 4 : 1);
                    
                }
                studentPopupWindow.dismiss();
            }
        });
        studentPopupWindow.setFocusable(false);
        studentPopupWindow.setOutsideTouchable(true);
        studentPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        
        if (RoomSession.roomType == 0) {
           /* studentPopupWindow.showAsDropDown(vdi_stu_in_sd, -KeyBoardUtil.px2dp(this,700),
                    -vdi_stu_in_sd.getMeasuredHeight() / 2 - vdi_stu_in_sd.getMeasuredHeight() / 3);*/
            //            studentPopupWindow.showAsDropDown(vdi_stu_in_sd, -vdi_stu_in_sd.getMeasuredWidth(),
            //                    -vdi_stu_in_sd.getMeasuredHeight() / 2 - vdi_stu_in_sd.getMeasuredHeight() / 3);
            studentPopupWindow.showAsDropDown(vdi_stu_in_sd, -(studentPopupWindow.getWidth()), -(vdi_stu_in_sd.getMeasuredHeight() + studentPopupWindow.getHeight()) / 2, Gravity.CENTER_VERTICAL);
        }
    }
    
    class AddTime extends TimerTask {
        @Override
        public void run() {
            RoomSession.serviceTime += 1;
            RoomSession.localTime = RoomSession.serviceTime - RoomSession.classStartTime;
            showTime();
        }
    }
    
    private void showTime() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String H = "";
                String M = "";
                String S = "";
                long temps = RoomSession.localTime;
                long tempm = temps / 60;
                long temph = tempm / 60;
                long sec = temps - tempm * 60;
                tempm = tempm - temph * 60;
                H = temph == 0 ? "00" : temph >= 10 ? temph + "" : "0" + temph;
                M = tempm == 0 ? "00" : tempm >= 10 ? tempm + "" : "0" + tempm;
                S = sec == 0 ? "00" : sec >= 10 ? sec + "" : "0" + sec;
                txt_hour.setText(H);
                txt_min.setText(M);
                txt_ss.setText(S);
                
                try {
                    if (TKRoomManager.getInstance().getRoomProperties() != null && TKRoomManager.getInstance().getRoomProperties().getLong("endtime") - RoomSession.serviceTime == 60 * 5) {
                        if (TKRoomManager.getInstance().getMySelf().role == 0 && RoomControler.haveTimeQuitClassroomAfterClass()) {
                            Toast.makeText(OneToOneThreeActivity.this, getString(R.string.end_class_time), Toast.LENGTH_LONG).show();
                        }
                    }
                    
                    if (TKRoomManager.getInstance().getRoomProperties() != null && RoomControler.haveTimeQuitClassroomAfterClass() && RoomSession.serviceTime == TKRoomManager.getInstance().getRoomProperties().getLong("endtime")) {
                        if (RoomSession.timerAddTime != null) {
                            RoomSession.timerAddTime.cancel();
                            RoomSession.timerAddTime = null;
                        }
                        
                        if (RoomSession.isClassBegin && TKRoomManager.getInstance().getMySelf().role == 0) {
                            try {
                                TKRoomManager.getInstance().delMsg("ClassBegin", "ClassBegin", "__all", new JSONObject().put("recordchat", true).toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            RoomSession.getInstance().sendClassDissToPhp();
                        }
                        RoomClient.getInstance().onClassDismiss();
                        RoomClient.getInstance().setExit(true);
                        TKRoomManager.getInstance().leaveRoom();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                try {
                    if (TKRoomManager.getInstance().getRoomProperties() == null) {
                        return;
                    }
                    long nowTime = System.currentTimeMillis() / 1000;
                    long endTime = TKRoomManager.getInstance().getRoomProperties().getLong("endtime");
                    long startTime = TKRoomManager.getInstance().getRoomProperties().getLong("starttime");
                    long proTime = endTime - startTime;
                    boolean isstart = false;
                    if (RoomSession.localTime == 0) {
                        proTime = startTime - nowTime;
                        isstart = false;
                    } else {
                        proTime = endTime - RoomSession.serviceTime;
                        isstart = true;
                    }
                    if (TKRoomManager.getInstance().getRoomProperties().getString("companyid").equals("10035")) {
                        if (proTime <= 60) {
                            txt_class_begin.setSelected(false);
                            txt_class_begin.setClickable(true);
                            canClassDissMiss = true;
                        }
                        if (isstart && proTime < -5 * 60) {//自动下课
                            TKRoomManager.getInstance().delMsg("ClassBegin", "ClassBegin", "__all", new JSONObject().put("recordchat", true).toString());
                            RoomSession.getInstance().sendClassDissToPhp();
                        }
                        
                        if (isstart && proTime <= 60 && !timeMessages.get(2).isShowed) {
                            showTimeTipPop(timeMessages.get(2));
                            
                            txt_hour.setTextAppearance(OneToOneThreeActivity.this, R.style.time_yel);
                            txt_min.setTextAppearance(OneToOneThreeActivity.this, R.style.time_yel);
                            txt_ss.setTextAppearance(OneToOneThreeActivity.this, R.style.time_yel);
                            txt_mao_01.setTextAppearance(OneToOneThreeActivity.this, R.style.time_yel);
                            txt_mao_02.setTextAppearance(OneToOneThreeActivity.this, R.style.time_yel);
                        }
                        if (isstart && proTime <= -3 * 60 && !timeMessages.get(3).isShowed) {
                            showTimeTipPop(timeMessages.get(3));
                            
                            txt_hour.setTextAppearance(OneToOneThreeActivity.this, R.style.time_red);
                            txt_min.setTextAppearance(OneToOneThreeActivity.this, R.style.time_red);
                            txt_ss.setTextAppearance(OneToOneThreeActivity.this, R.style.time_red);
                            txt_mao_01.setTextAppearance(OneToOneThreeActivity.this, R.style.time_red);
                            txt_mao_02.setTextAppearance(OneToOneThreeActivity.this, R.style.time_red);
                            
                        }
                        if (isstart && proTime <= -5 * 60 + 10 && !timeMessages.get(4).isShowed) {
                            showTimeTipPop(timeMessages.get(4));
                            txt_hour.setTextAppearance(OneToOneThreeActivity.this, R.style.time_red);
                            txt_min.setTextAppearance(OneToOneThreeActivity.this, R.style.time_red);
                            txt_ss.setTextAppearance(OneToOneThreeActivity.this, R.style.time_red);
                            txt_mao_01.setTextAppearance(OneToOneThreeActivity.this, R.style.time_red);
                            txt_mao_02.setTextAppearance(OneToOneThreeActivity.this, R.style.time_red);
                        }
                    } else {
                        txt_class_begin.setSelected(false);
                        txt_class_begin.setClickable(true);
                        canClassDissMiss = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
    
    private void showTimeTipPop(final TimeMessage tms) {
        if (!(TKRoomManager.getInstance().getMySelf().role == 0) || getfinalClassBeginMode()) {
            return;
        }
        try {
            if (!TKRoomManager.getInstance().getRoomProperties().getString("companyid").equals("10035")) {
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tms.isShowed = true;
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.time_tip_pop, null);
        final TextView txt_tip = (TextView) contentView.findViewById(R.id.txt_tip);
        final TextView txt_i_know = (TextView) contentView.findViewById(R.id.txt_i_know);
        txt_tip.setText(Html.fromHtml(tms.message));
        final PopupWindow popupWindow = new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(contentView);
        txt_i_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        if (tms.hasKonwButton) {
            txt_i_know.setVisibility(View.VISIBLE);
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (tms.count5 == 0) {
                                t.cancel();
                                if (!isFinishing()) {
                                    popupWindow.dismiss();
                                }
                            }
                            txt_i_know.setText(getResources().getString(R.string.i_konw) + tms.count5 + "'");
                            tms.count5--;
                        }
                    });
                }
            }, 1000, 1000);
        } else {
            tms.count5 = 10;
            txt_i_know.setVisibility(View.GONE);
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (tms.count5 == 0) {
                                t.cancel();
                                popupWindow.dismiss();
                            }
                            txt_tip.setText(tms.message + " " + tms.count5 + "'");
                            tms.count5--;
                        }
                    });
                }
            }, 1000, 1000);
        }
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    popupWindow.showAsDropDown(lin_time, txt_hour.getWidth() * 4, -lin_time.getMeasuredHeight());
                }
            }
        });
    }
    
    
    private void onRemotePubMsg(String namePub, long pubMsgTS, Object dataPub, boolean inListPub) {
        
        if (namePub.equals("ClassBegin")) {
            try {
                if (!TKRoomManager.getInstance().getRoomProperties().getString("companyid").equals("10035")) {
                    txt_class_begin.setText(R.string.classdismiss);
                } else {
                    txt_class_begin.setText(R.string.classdismiss);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            setWhiteBoradNarrow(false);
            
            if (!RoomControler.isReleasedBeforeClass()) {
                unPlaySelfAfterClassBegin();
            }
            
            if (TKRoomManager.getInstance().getMySelf().role == 0) {
                WhiteBoradConfig.getsInstance().localChangeDoc();
            }
            
            initViewByRoomTypeAndTeacher();

           /* if (TKRoomManager.getInstance().getMySelf().role == 0) {
                if (!RoomControler.isShowClassBeginButton()) {
                    txt_class_begin.setVisibility(View.VISIBLE);
                } else {
                    txt_class_begin.setVisibility(View.INVISIBLE);
                }
            } else {
                txt_class_begin.setVisibility(View.INVISIBLE);
            }*/
            
            if (TextUtils.isEmpty(RoomSession.path)) {
                if (!RoomControler.isReleasedBeforeClass()) {
                    if (TKRoomManager.getInstance().getMySelf().role == 0) {
                        TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId,
                            "__all", "publishstate", 3);
                    } else if (RoomControler.isAutomaticUp() && RoomSession.publishSet.size() < RoomSession.maxVideo && TKRoomManager.getInstance().getMySelf().role == 2) {
                        TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId,
                            "__all", "publishstate", 3);
                    }
                } else {
                    if (TKRoomManager.getInstance().getMySelf().role == 0) {
                        TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId,
                            "__all", "publishstate", 3);
                    } else if (!RoomControler.isAutomaticUp() && TKRoomManager.getInstance().getMySelf().role == 2) {
                        TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId,
                            "__all", "publishstate", 0);
                    } else if (RoomControler.isAutomaticUp() && TKRoomManager.getInstance().getMySelf().publishState != 3 && RoomSession.publishSet.size() < RoomSession.maxVideo) {
                        if (TKRoomManager.getInstance().getMySelf().role == 2 || TKRoomManager.getInstance().getMySelf().role == 0) {
                            TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId,
                                "__all", "publishstate", 3);
                        }
                    }
                }
            }
            
            for (RoomUser u : TKRoomManager.getInstance().getUsers().values()) {
                changeUserState(u);
            }
            
        } else if (namePub.equals("UpdateTime")) {
            if (RoomSession.isClassBegin) {
                if (TKRoomManager.getInstance().getMySelf().role != -1) {
                    lin_time.setVisibility(View.VISIBLE);
                }
                if (RoomSession.timerAddTime == null) {
                    RoomSession.timerAddTime = new Timer();
                    RoomSession.timerAddTime.schedule(new AddTime(), 1000, 1000);
                }
            } else {
                if (RoomSession.timerbefClassbegin == null && !RoomSession.isClassBegin && !getfinalClassBeginMode()) {
                    RoomSession.timerbefClassbegin = new Timer();
                    RoomSession.timerbefClassbegin.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    long nowTime = System.currentTimeMillis() / 1000;
                                    long startTime = 0;
                                    if (TKRoomManager.getInstance().getRoomProperties() != null) {
                                        startTime = TKRoomManager.getInstance().getRoomProperties().optLong("starttime");
                                    }
                                    long proTime = startTime - nowTime;
                                    if (proTime == 60 && !timeMessages.get(0).isShowed) {
                                        showTimeTipPop(timeMessages.get(0));
                                    }
                                    if (proTime <= -60 && timeMessages != null && timeMessages.size() > 1 && !timeMessages.get(1).isShowed) {
                                        int overtime = Math.abs((int) (proTime / 60));
                                        timeMessages.get(1).message = getString(R.string.classroom_part_01) + "<font color='#FFD700'>" + overtime + "</font> " + getString(R.string.classroom_part_02);
                                        showTimeTipPop(timeMessages.get(1));
                                    }
                                    if (proTime <= 60) {
                                        txt_class_begin.setText(R.string.classbegin);
                                        txt_class_begin.setClickable(true);
                                    }
                                }
                            });
                        }
                    }, 500, 1000);
                } else if (!RoomSession.isClassBegin) {
                    if (TKRoomManager.getInstance().getMySelf().role == 0 && getfinalClassBeginMode()) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        try {
                            long expires = TKRoomManager.getInstance().getRoomProperties().getLong("endtime") + 5 * 60;
                            if (RoomControler.isNotLeaveAfterClass()) {
                                TKRoomManager.getInstance().delMsg("__AllAll", "__AllAll", "__none", new HashMap<String, Object>());
                            }
                            TKRoomManager.getInstance().pubMsg("ClassBegin", "ClassBegin", "__all", new JSONObject().put("recordchat", true).toString(), true, expires);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        
        if (namePub.equals("ShowPage")) {
            mediaListAdapter.notifyDataSetChanged();
            fileListAdapter.notifyDataSetChanged();
        }
        
        if (namePub.equals("StreamFailure")) {
            Map<String, Object> mapdata = null;
            if (dataPub instanceof String) {
                String str = (String) dataPub;
                try {
                    JSONObject js = new JSONObject(str);
                    mapdata = Tools.toMap(js);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mapdata = (Map<String, Object>) dataPub;
            }
            String stupeerid = (String) mapdata.get("studentId");
            
            RoomSession.pandingSet.remove(stupeerid);
            memberListAdapter.setPubFailUserId(stupeerid);
            memberListAdapter.notifyDataSetChanged();
            
            if (TKRoomManager.getInstance().getMySelf().role == 0) {
                RoomUser u = TKRoomManager.getInstance().getUser(stupeerid);
                if (u != null) {
                    if (u.properties.containsKey("passivityPublish")) {
                        int failuretype = -1;
                        if (u.properties.get("failuretype") != null) {
                            failuretype = (Integer) u.properties.get("failuretype");
                        }
                        switch (failuretype) {
                            case 1:
                                Toast.makeText(this, R.string.udp_faild, Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                Toast.makeText(this, R.string.publish_faild, Toast.LENGTH_LONG).show();
                                break;
                            case 3:
                                Toast.makeText(this, R.string.member_overload, Toast.LENGTH_LONG).show();
                                break;
                            case 4:
                                Toast.makeText(this, u.nickName + getResources().getString(R.string.select_back_hint), Toast.LENGTH_LONG).show();
                                break;
                            case 5:
                                Toast.makeText(this, R.string.udp_break, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                    u.properties.remove("passivityPublish");
                }
            }
        }
        
        if (namePub.equals("EveryoneBanChat")) {
            
            ChatData ch = new ChatData();
            ch.setStystemMsg(true);
            ch.setMsgTime(System.currentTimeMillis());
            ch.setMessage(getString(R.string.chat_prompt_yes));
            ch.setTrans(false);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = null;
            if (StringUtils.isEmpty(RoomSession.path)) {
                curDate = new Date(System.currentTimeMillis());//获取当前时间
            } else {
                curDate = new Date(pubMsgTS);
            }
            String str = formatter.format(curDate);
            ch.setTime(str);
            RoomSession.chatList.add(ch);
            
            if (chatUtils.popupIsShow()) {
                chatUtils.setEdtInputHint();
            }
            chlistAdapter.notifyDataSetChanged();
            
            if (chatUtils.popupIsShow()) {
                chatUtils.setTitelPrompt();
            }
            
            if (TKRoomManager.getInstance().getMySelf().role != 0) {
                if (inListPub) {
                    TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId, "__all", "disablechat", true);
                }
            }
        }
        
        if (namePub.equals("FullScreen")) {
            JSONObject jsonObject = null;
            if (dataPub instanceof String) {
                String str = (String) dataPub;
                try {
                    jsonObject = new JSONObject(str);
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            
            if (jsonObject == null) {
                return;
            }
            
            String fullScreenType = jsonObject.optString("fullScreenType");
            //{"fullScreenType":"courseware_file","needPictureInPictureSmall":true}
            //{"fullScreenType": "stream_video","needPictureInPictureSmall": true,"mainPictureInPictureStreamRoleStreamRole": 0,"fullScreenStreamExtensionId": "b29ace48-2916-6627-3cca-b976ef497a56"}
            
            if (fullScreenType.equals("courseware_file") || fullScreenType.equals("stream_media")) {//白板全屏
                isZoom = true;
                setWhiteBoradEnlarge(true);
                
                if (TKRoomManager.getInstance().getMySelf().role == 0 || TKRoomManager.getInstance().getMySelf().role == 4) {
                    if (!TextUtils.isEmpty(stu_in_sd.peerid)) {
                        if (videofragment != null /*&& !videofragment.isAdded()*/) {
                            videofragment.setFullscreenShow(stu_in_sd.peerid);
                        } else {
                            if (movieFragment != null) {
                                movieFragment.setFullscreenShow(stu_in_sd.peerid);
                            } else {
                                rel_fullscreen_videoitem.setVisibility(View.VISIBLE);
                                fullscreen_sf_video.setZOrderMediaOverlay(true);
                                fullscreen_sf_video.setVisibility(View.VISIBLE);
                                fullscreen_bg_video_back.setVisibility(View.INVISIBLE);
                                fullscreen_img_video_back.setVisibility(View.INVISIBLE);
                                TKRoomManager.getInstance().playVideo(stu_in_sd.peerid, fullscreen_sf_video, RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                            }
                        }
                    } else {
                        rel_fullscreen_videoitem.setVisibility(View.VISIBLE);
                        fullscreen_sf_video.setZOrderMediaOverlay(false);
                        fullscreen_sf_video.setVisibility(View.INVISIBLE);
                        fullscreen_bg_video_back.setVisibility(View.VISIBLE);
                        fullscreen_img_video_back.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (!TextUtils.isEmpty(teacherItem.peerid)) {
                        if (videofragment != null /*&& !videofragment.isAdded()*/) {
                            videofragment.setFullscreenShow(teacherItem.peerid);
                        } else {
                            if (movieFragment != null) {
                                movieFragment.setFullscreenShow(teacherItem.peerid);
                            } else {
                                rel_fullscreen_videoitem.setVisibility(View.VISIBLE);
                                fullscreen_sf_video.setZOrderMediaOverlay(true);
                                fullscreen_sf_video.setVisibility(View.VISIBLE);
                                fullscreen_bg_video_back.setVisibility(View.INVISIBLE);
                                fullscreen_img_video_back.setVisibility(View.INVISIBLE);
                                TKRoomManager.getInstance().playVideo(teacherItem.peerid, fullscreen_sf_video, RendererCommon.ScalingType.SCALE_ASPECT_BALANCED);
                            }
                        }
                    }
                }
            }
            
        }
        
        if (namePub.equals("ChatShow")) {
            if (TKRoomManager.getInstance().getMySelf().role == -1) {
                if (!chatUtils.popupIsShow()) {
                    clearNoReadChatMessage();
                    if (huawei || oppo || voio) {
                        chatUtils.showChatPopupWindow(ll_wb_container.getWidth() * 9 / 10, ll_wb_container.getHeight() * 9 / 10, ll_wb_container, rl_message, webandsufwidth, true);
                    } else {
                        chatUtils.showChatPopupWindow(ll_wb_container.getWidth() * 9 / 10, ll_wb_container.getHeight() * 9 / 10, ll_wb_container, rl_message, webandsufwidth, false);
                    }
                }
            }
        }
    }
    
    private void onRemoteDelMsg(String nameDel, long delMsgTS) {
        if (nameDel.equals("ClassBegin")) {
            txt_hand_up.setVisibility(View.GONE);
            if (TKRoomManager.getInstance().getMySelf().role == 0) {
                txt_class_begin.setVisibility(View.VISIBLE);
            } else {
                txt_class_begin.setVisibility(View.GONE);
            }
            
            //            cb_choose_mouse.setVisibility(View.GONE);
            //            ll_tool_case.setVisibility(View.GONE);
            
            try {
                if (!TKRoomManager.getInstance().getRoomProperties().getString("companyid").equals("10035")) {
                    if (RoomSession.userrole == 0) {
                        cb_tool_case.setVisibility(View.GONE);
                        txt_class_begin.setText(R.string.classbegin);
                        if (!RoomControler.isShowClassBeginButton()) {
                            txt_class_begin.setVisibility(View.VISIBLE);
                        }
                        txt_class_begin.setClickable(true);
                    } else {
                        if (!RoomControler.isNotLeaveAfterClass()) {
                            sendGiftPopUtils.deleteImage();
                            RoomClient.getInstance().setExit(true);
                            TKRoomManager.getInstance().leaveRoom();
                        }
                    }
                    txt_hand_up.setText(R.string.raise);
                    lin_time.setVisibility(View.INVISIBLE);
                    txt_hour.setText("00");
                    txt_min.setText("00");
                    txt_ss.setText("00");
                    memberListAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (!RoomControler.isNotLeaveAfterClass()) {
                        RoomSession.chatList.clear();
                        chlistAdapter.notifyDataSetChanged();
                    }
                }
            }, 250);
            
            for (RoomUser u : TKRoomManager.getInstance().getUsers().values()) {
                changeUserState(u);
            }
        }
        
        if (nameDel.equals("EveryoneBanChat")) {
            
            ChatData ch = new ChatData();
            ch.setStystemMsg(true);
            ch.setMsgTime(System.currentTimeMillis());
            ch.setMessage(getString(R.string.chat_prompt_no));
            ch.setTrans(false);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = null;
            if (StringUtils.isEmpty(RoomSession.path)) {
                curDate = new Date(System.currentTimeMillis());
            } else {
                curDate = new Date(delMsgTS);
            }
            String str = formatter.format(curDate);
            ch.setTime(str);
            
            RoomSession.chatList.add(ch);
            
            if (chatUtils.popupIsShow()) {
                chatUtils.setEdtInputHint();
            }
            chlistAdapter.notifyDataSetChanged();
            
            if (chatUtils.popupIsShow()) {
                chatUtils.setTitelPrompt();
            }
        }
        
        if (nameDel.equals("FullScreen")) {
            isZoom = false;
            setWhiteBoradNarrow(false);
            
            for (int i = 0; i < RoomSession.playingList.size(); i++) {
                if (RoomSession.playingList.get(i).role == 0 || RoomSession.playingList.get(i).role == 4) {
                    if (videofragment != null /*&& !videofragment.isAdded()*/) {
                        videofragment.setFullscreenHide();
                    } else {
                        if (movieFragment != null) {
                            movieFragment.setFullscreenHide();
                        } else {
                            rel_fullscreen_videoitem.setVisibility(View.GONE);
                            fullscreen_sf_video.setZOrderMediaOverlay(false);
                            fullscreen_sf_video.setVisibility(View.INVISIBLE);
                            //                            cb_large_or_small.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
        
        if (nameDel.equals("ChatShow")) {
            if (TKRoomManager.getInstance().getMySelf().role == -1) {
                if (chatUtils.popupIsShow()) {
                    chatUtils.dismissPopupWindow();
                }
            }
        }
    }
    
    private void onMessageReceived(RoomUser chatUser) {
        if (!chatUser.peerId.equals(TKRoomManager.getInstance().getMySelf().peerId) && !chatUtils.popupIsShow()) {
            setNoReadChatMessage(RoomSession.chatDataCache.size());
        } else if (chatUtils.popupIsShow()) {
            RoomSession.chatDataCache.clear();
        }
        chlistAdapter.notifyDataSetChanged();
    }
    
    /**
     * 设置未读消息数
     *
     * @param num
     */
    private void setNoReadChatMessage(int num) {
        if (tv_no_read_message_number != null) {
            tv_no_read_message_number.setVisibility(View.VISIBLE);
            if (num > 99) {
                tv_no_read_message_number.setText("99+");
            } else {
                tv_no_read_message_number.setText(num + "");
            }
        }
    }
    
    private void onUserVideoStatus(String peerId, int state) {
        if (state > 0) {
            if (RoomControler.isOnlyShowTeachersAndVideos()) {
                if (TKRoomManager.getInstance().getMySelf().role == 2) {
                    RoomUser roomUser = TKRoomManager.getInstance().getUser(peerId);
                    if (peerId.equals(TKRoomManager.getInstance().getMySelf().peerId) || roomUser.role == 0) {
                        doPlayVideo(peerId);
                    }
                } else {
                    doPlayVideo(peerId);
                }
            } else {
                doPlayVideo(peerId);
            }
        } else {
            doUnPlayVideo(TKRoomManager.getInstance().getUser(peerId));
            /*if (!RoomSession.isClassBegin && TKRoomManager.getInstance().getMySelf().role == 0 &&
                    TKRoomManager.getInstance().getUser(peerId).role == 0 &&
                    TKRoomManager.getInstance().getMySelf().peerId.equals(peerId)) {
                playSelfBeforeClassBegin();
            }*/
        }
        /* doLayout();*/
       /* changeUserState(TKRoomManager.getInstance().getUser(peerId));*/
        memberListAdapter.notifyDataSetChanged();
        if (studentPopupWindow != null) {
            studentPopupWindow.dismiss();
        }
    }
    
    private void doUnPlayVideo(RoomUser roomUser) {
        if (roomUser == null) {
            return;
        }
        
        Integer publishState = (Integer) roomUser.properties.get("publishstate") != null ?
            (Integer) roomUser.properties.get("publishstate") : 0;
        
        if (RoomSession.roomType == 0) {
            if (roomUser.role == 0) {//老师
                if (roomUser.peerId.equals(teacherItem.peerid)) {
                    teacherItem.re_background.setVisibility(View.INVISIBLE);
                    teacherItem.img_video_back.setVisibility(View.VISIBLE);
                    teacherItem.sf_video.setVisibility(View.INVISIBLE);
                    teacherItem.bg_video_back.setVisibility(View.VISIBLE);
                    if (roomUser.hasVideo) {
                        teacherItem.img_video_back.setImageResource(R.drawable.three_icon_camera_close);
                    } else {
                        teacherItem.img_video_back.setImageResource(R.drawable.three_icon_no_camera);
                    }
                    
                    if (publishState == 0) {
                        teacherItem.img_mic.setVisibility(View.INVISIBLE);
                        teacherItem.img_pen.setVisibility(View.INVISIBLE);
                        teacherItem.cv_draw.setVisibility(View.INVISIBLE);
                        teacherItem.lin_name_label.setVisibility(View.INVISIBLE);
                        teacherItem.bg_video_back.setVisibility(View.INVISIBLE);
                        teacherItem.img_video_back.setImageResource(R.drawable.three_zhanwei_teacher);
                    }
                }
                /*fullscreen_sf_video.setVisibility(View.INVISIBLE);
                fullscreen_bg_video_back.setVisibility(View.VISIBLE);
                fullscreen_img_video_back.setVisibility(View.VISIBLE);*/
                
            } else if (roomUser.role == 2) {//学生
                
                if (roomUser.peerId.equals(stu_in_sd.peerid)) {
                    stu_in_sd.re_background.setVisibility(View.INVISIBLE);
                    stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
                    stu_in_sd.bg_video_back.setVisibility(View.VISIBLE);
                    if (publishState == 0) {
                        stu_in_sd.lin_gift.setVisibility(View.INVISIBLE);
                        stu_in_sd.lin_name_label.setVisibility(View.INVISIBLE);
                        stu_in_sd.img_mic.setVisibility(View.INVISIBLE);
                        stu_in_sd.img_pen.setVisibility(View.INVISIBLE);
                        stu_in_sd.cv_draw.setVisibility(View.INVISIBLE);
                        stu_in_sd.bg_video_back.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }
    
    private void onUserPropertyChanged(RoomUser propertyUser, Map<String, Object> map, String fromId) {
        /*if (RoomSession.playingMap.containsKey(propertyUser.peerId) && propertyUser.publishState > 0) {
            RoomSession.playingMap.put(propertyUser.peerId, propertyUser.publishState >= 1 && propertyUser.publishState <= 4);
            memberListAdapter.notifyDataSetChanged();
            if (propertyUser.publishState > 0) {
                doPlayVideo(propertyUser.peerId);
            } else {
                doUnPlayVideo(propertyUser);
            }
        }*/
        changeUserState(propertyUser);
        /*checkRaiseHands();*/
        if (map.containsKey("isInBackGround")) {
            if (propertyUser == null) {
                return;
            }
            boolean isinback = Tools.isTure(map.get("isInBackGround"));
            setBackgroundOrReception(isinback, propertyUser);
        }
        
        if (map.containsKey("disablechat")) {
            if (propertyUser == null) {
                return;
            }
            if (TKRoomManager.getInstance().getMySelf().peerId.equals(propertyUser.peerId)) {
                chatUtils.setEdtInputHint();
            }
        }
        
        if (RoomSession.isClassBegin) {
            if (TKRoomManager.getInstance().getMySelf().properties.containsKey("raisehand")) {
                boolean israisehand = Tools.isTure(TKRoomManager.getInstance().getMySelf().properties.get("raisehand"));
                RoomUser roomUser = TKRoomManager.getInstance().getMySelf();
                if (israisehand) {
                    iv_hand.setVisibility(View.VISIBLE);
                    txt_hand_up.setText(R.string.raiseing);
                    txt_hand_up.setBackgroundResource(R.drawable.three_commom_btn_handup);
                    txt_hand_up.setTextAppearance(OneToOneThreeActivity.this, R.style.three_color_hands_up);
                } else {
                    iv_hand.setVisibility(View.INVISIBLE);
                    txt_hand_up.setText(R.string.raise); //同意了，或者拒绝了
                    txt_hand_up.setBackgroundResource(R.drawable.commom_btn_xiake1);
                    txt_hand_up.setTextAppearance(OneToOneThreeActivity.this, R.style.three_color_hands_up);
                }
            } else {
                if (map.containsKey("raisehand")) {
                    boolean israisehand = Tools.isTure(map.get("raisehand"));
                    if (israisehand) {
                        iv_hand.setVisibility(View.VISIBLE);
                    } else {
                        iv_hand.setVisibility(View.INVISIBLE);
                    }
                }
                txt_hand_up.setText(R.string.raise); //还没举手
                txt_hand_up.setBackgroundResource(R.drawable.commom_btn_xiake1);
                txt_hand_up.setTextAppearance(OneToOneThreeActivity.this, R.style.three_color_hands_up);
            }
            
            if (map.containsKey("isInBackGround")) {
                if (TKRoomManager.getInstance().getMySelf().role != 2) {
                    chlistAdapter.notifyDataSetChanged();
                    memberListAdapter.notifyDataSetChanged();
                }
            }
            if (map.containsKey("giftnumber") && !propertyUser.peerId.equals(fromId)) {
                if (RoomSession.roomType == 0) {
                    showGiftAim(stu_in_sd, map);
                }
            }
            
            if (propertyUser.peerId.equals(TKRoomManager.getInstance().getMySelf().peerId) && map.containsKey("candraw") && !(TKRoomManager.getInstance().getMySelf().role == 0)) {
                boolean candraw = Tools.isTure(map.get("candraw"));
                if (candraw) {
                    if (TKRoomManager.getInstance().getMySelf().role != 4) {
                        cb_choose_photo.setVisibility(View.VISIBLE);
                    }
                } else {
                    cb_choose_photo.setVisibility(View.GONE);
                }
            }
        }
        
        if (propertyUser.peerId.equals(TKRoomManager.getInstance().getMySelf().peerId) && map.containsKey("volume")) {
            Number n_volume = (Number) map.get("volume");
            int int_volume = n_volume.intValue();
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, int_volume, 0);
        }
        memberListAdapter.notifyDataSetChanged();
    }
    
    boolean is_gif = false;
    com.bumptech.glide.load.resource.gif.GifDrawable gifDrawable_play_gift;
    
    private void showGiftAim(final VideoItem item, Map<String, Object> map) {
        
        final ImageView img_gift = new ImageView(this);
        final GifImageView iv_gif = new GifImageView(this);
        
        if (map.containsKey("giftinfo")) {
            //自定义奖杯
            try {
                final Map<String, Object> gift_data = (HashMap<String, Object>) map.get("giftinfo");
                String url = gift_data.get("trophyimg").toString();
                is_gif = url.endsWith(".gif");
                
                SoundPlayUtils.play(gift_data.get("trophyvoice").toString());
                String img_url = "http://" + RoomSession.host + ":" + RoomSession.port + url;
                
                if (is_gif) {
                    Glide.with(this).asGif().load(img_url).into(new SimpleTarget<com.bumptech.glide.load.resource.gif.GifDrawable>() {
                        @Override
                        public void onResourceReady(com.bumptech.glide.load.resource.gif.GifDrawable resource, Transition<? super com.bumptech.glide.load.resource.gif.GifDrawable> transition) {
                            gifDrawable_play_gift = resource;
                            iv_gif.setImageDrawable(gifDrawable_play_gift);
                            setAnimal(is_gif, img_gift, iv_gif, item.lin_gift);
                        }
                    });
                } else {
                    Glide.with(this).asBitmap().load(img_url).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            img_gift.setImageBitmap(resource);
                            setAnimal(is_gif, img_gift, iv_gif, item.lin_gift);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //默认动画
            SoundPlayUtils.play("");
            img_gift.setImageResource(R.drawable.ico_gift);
            setAnimal(false, img_gift, iv_gif, item.lin_gift);
        }
    }
    
    private void setAnimal(final boolean is_gif, final ImageView img_gift, final GifImageView iv_gif, View lin_gift) {
        
        RelativeLayout.LayoutParams relparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relparam.addRule(RelativeLayout.CENTER_VERTICAL);
        relparam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //        relparam.leftMargin = ll_wb_container.getWidth() / 2 - iv_gif.getMeasuredWidth()/2;
        
        if (is_gif) {
            iv_gif.setLayoutParams(relparam);
            if (movieFragment != null) {
                video_container.addView(iv_gif);
            } else {
                rl_web.addView(iv_gif);
            }
            
        } else {
            img_gift.setLayoutParams(relparam);
            rl_web.addView(img_gift);
        }
        
        int[] loca = new int[2];
        lin_gift.getLocationInWindow(loca);
        float x = loca[0];
        float y = loca[1];
        DisplayMetrics dm = new DisplayMetrics();
        //        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //        int wid = dm.widthPixels;
        //        int hid = dm.heightPixels;
        
        
        float gx = 0, gy = 0;
        if (is_gif) {
            gx = rl_web.getWidth() / 2 - iv_gif.getWidth();
            gy = rl_web.getHeight() / 2 - iv_gif.getHeight();
        } else {
            gx = rl_web.getWidth() / 2 - img_gift.getWidth();
            gy = rl_web.getHeight() / 2 - img_gift.getHeight();
        }
        
        int heightStatusBar = 0;
        float dx = 0;
        if (huawei || oppo || voio) {
            heightStatusBar = FullScreenTools.getStatusBarHeight(this);
            dx = x - gx - heightStatusBar;
        } else {
            dx = x - gx;
        }
        
        float dy = y - gy - rel_tool_bar.getHeight();
        
        ObjectAnimator smlTobigXScale = null;
        ObjectAnimator smlTobigYScale = null;
        ObjectAnimator bigToSmlXScale = null;
        ObjectAnimator bigToSmlYScale = null;
        ObjectAnimator translateX = null;
        ObjectAnimator translateY = null;
        if (is_gif) {
            smlTobigXScale = ObjectAnimator.ofFloat(iv_gif, "scaleX", 1.0f, 2.0f);
            smlTobigYScale = ObjectAnimator.ofFloat(iv_gif, "scaleY", 1.0f, 2.0f);
            bigToSmlXScale = ObjectAnimator.ofFloat(iv_gif, "scaleX", 2.0f, 0.0f);
            bigToSmlYScale = ObjectAnimator.ofFloat(iv_gif, "scaleY", 2.0f, 0.0f);
            translateX = ObjectAnimator.ofFloat(iv_gif, "translationX", 0.0f, dx);
            translateY = ObjectAnimator.ofFloat(iv_gif, "translationY", 0.0f, dy);
        } else {
            smlTobigXScale = ObjectAnimator.ofFloat(img_gift, "scaleX", 1.0f, 2.0f);
            smlTobigYScale = ObjectAnimator.ofFloat(img_gift, "scaleY", 1.0f, 2.0f);
            bigToSmlXScale = ObjectAnimator.ofFloat(img_gift, "scaleX", 2.0f, 0.0f);
            bigToSmlYScale = ObjectAnimator.ofFloat(img_gift, "scaleY", 2.0f, 0.0f);
            translateX = ObjectAnimator.ofFloat(img_gift, "translationX", 0.0f, dx);
            translateY = ObjectAnimator.ofFloat(img_gift, "translationY", 0.0f, dy);
        }
        AnimatorSet scaleSet = new AnimatorSet();
        scaleSet.play(smlTobigXScale).with(smlTobigYScale);
        scaleSet.setDuration(1500);
        
        scaleSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (is_gif && gifDrawable_play_gift != null) {
                    gifDrawable_play_gift.start();
                }
            }
            
            @Override
            public void onAnimationEnd(Animator animation) {
            
            }
            
            @Override
            public void onAnimationCancel(Animator animation) {
            
            }
            
            @Override
            public void onAnimationRepeat(Animator animation) {
            
            }
        });
        
        AnimatorSet scaleAndTranSet = new AnimatorSet();
        scaleAndTranSet.playTogether(bigToSmlXScale, bigToSmlYScale, translateX, translateY);
        scaleAndTranSet.setDuration(2000);
        
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleSet).before(scaleAndTranSet);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            
            }
            
            @Override
            public void onAnimationEnd(Animator animation) {
                if (is_gif && gifDrawable_play_gift != null) {
                    iv_gif.clearAnimation();
                    iv_gif.setVisibility(View.GONE);
                    gifDrawable_play_gift.stop();
                } else {
                    img_gift.clearAnimation();
                    img_gift.setVisibility(View.GONE);
                }
            }
            
            @Override
            public void onAnimationCancel(Animator animation) {
            
            }
            
            @Override
            public void onAnimationRepeat(Animator animation) {
            
            }
        });
    }
    
    private void setBackgroundOrReception(boolean b, RoomUser RoomUser) {
        if (RoomUser != null && RoomUser.role == 0) {
            if (b /*&& isBackApp*/) {
                teacherItem.re_background.setVisibility(View.VISIBLE);
                teacherItem.tv_home.setText(R.string.tea_background);
            } else {
                teacherItem.re_background.setVisibility(View.GONE);
            }
        }
        
        if (stu_in_sd.parent != null) {
            if (stu_in_sd.peerid != null) {
                if (!stu_in_sd.peerid.isEmpty()) {
                    if (stu_in_sd.peerid.equals(RoomUser.peerId)) {
                        if (b && isBackApp) {
                            stu_in_sd.re_background.setVisibility(View.VISIBLE);
                            stu_in_sd.tv_home.setText(R.string.background);
                        } else {
                            stu_in_sd.re_background.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }
    
    private void onUserLeft(RoomUser roomUser) {
        chlistAdapter.notifyDataSetChanged();
        memberListAdapter.notifyDataSetChanged();
        memberListPopupWindowUtils.setTiteNumber(RoomSession.memberList.size());
        doUnPlayVideo(roomUser);
        /*changeUserState(roomUser);*/
        
        if (roomUser.role == 0) {//老师
            if (roomUser.peerId.equals(teacherItem.peerid)) {
                teacherItem.re_background.setVisibility(View.INVISIBLE);
                teacherItem.img_video_back.setVisibility(View.VISIBLE);
                teacherItem.sf_video.setVisibility(View.INVISIBLE);
                teacherItem.bg_video_back.setVisibility(View.VISIBLE);
                teacherItem.img_video_back.setImageResource(R.drawable.three_zhanwei_teacher);
                
                teacherItem.img_mic.setVisibility(View.INVISIBLE);
                teacherItem.img_pen.setVisibility(View.INVISIBLE);
                teacherItem.cv_draw.setVisibility(View.INVISIBLE);
                teacherItem.lin_name_label.setVisibility(View.INVISIBLE);
            }
        } else if (roomUser.role == 2) {//学生
            if (roomUser.peerId.equals(stu_in_sd.peerid)) {
                
                stu_in_sd.re_background.setVisibility(View.INVISIBLE);
                stu_in_sd.sf_video.setVisibility(View.INVISIBLE);
                stu_in_sd.bg_video_back.setVisibility(View.VISIBLE);
                stu_in_sd.img_video_back.setVisibility(View.VISIBLE);
                stu_in_sd.img_video_back.setImageResource(R.drawable.three_zhanwei_student);
                
                stu_in_sd.lin_gift.setVisibility(View.INVISIBLE);
                stu_in_sd.lin_name_label.setVisibility(View.INVISIBLE);
                stu_in_sd.img_mic.setVisibility(View.INVISIBLE);
                stu_in_sd.img_pen.setVisibility(View.INVISIBLE);
                stu_in_sd.cv_draw.setVisibility(View.INVISIBLE);
            }
            
        }
        if (RoomSession.roomType == 0 && roomUser != null && roomUser.role == 2) {
            stu_in_sd.txt_name.setText("");
            stu_in_sd.txt_gift_num.setText(String.valueOf(0));
        }
        
        if (isZoom && RoomControler.isFullScreenVideo()) {
            if (roomUser.role == 0 || roomUser.role == 2) {
                if (videofragment != null /*&& !videofragment.isAdded()*/) {
                    videofragment.setFullscreenHide();
                } else {
                    if (movieFragment != null) {
                        movieFragment.setFullscreenHide();
                    } else {
                        fullscreen_sf_video.setVisibility(View.INVISIBLE);
                        fullscreen_img_video_back.setVisibility(View.VISIBLE);
                        fullscreen_bg_video_back.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
    
    private void onUserJoined(RoomUser user, boolean inList) {
        changeUserState(user);
        chlistAdapter.notifyDataSetChanged();
        memberListAdapter.notifyDataSetChanged();
        memberListPopupWindowUtils.setTiteNumber(RoomSession.memberList.size());
    }
    
    private void onWarning(int onWarning) {
        if (10001 == onWarning) {
            if (isOpenCamera) {
                if (10001 == onWarning) {
                    if (isOpenCamera) {
                        PhotoUtils.openCamera(OneToOneThreeActivity.this);
                    }
                }
            }
        }
    }
    
    private void onRoomJoin() {
        if (TKRoomManager.getInstance().isInBackGround()) {//如果onstart内没有发送成功，就再次发送一遍
            TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId, "__all", "isInBackGround", false);
            TKRoomManager.getInstance().setInBackGround(false);
        }
        
        if (RoomSession.trophyList != null && RoomSession.trophyList.size() > 0) {
            SoundPlayUtils.loadTrophy(RoomSession.host, RoomSession.port, this);
        } else {
            if (TextUtils.isEmpty(RoomSession._MP3Url)) {
                SoundPlayUtils.init(this);
            } else {
                SoundPlayUtils.loadMP3(RoomSession.host, RoomSession.port, this);
            }
        }
        
        if (frameAnimation != null) {
            frameAnimation.stopAnimation();
            re_loading.setVisibility(View.GONE);
        }
        
        for (RoomUser u : TKRoomManager.getInstance().getUsers().values()) {
            changeUserState(u);
        }
        
        //        txt_room_name.setText(StringEscapeUtils.unescapeHtml4(TKRoomManager.getInstance().getRoomName()));
        tv_back_name.setText(RoomSession.roomName);
        initViewByRoomTypeAndTeacher();
        
        txt_hand_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!RoomSession.isClassBegin) {
                    return true;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        RoomUser roomUser = TKRoomManager.getInstance().getMySelf();
                        if (roomUser != null && roomUser.getPublishState() != 0) {
                            TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId, "__all", "raisehand", true);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        RoomUser user = TKRoomManager.getInstance().getMySelf();
                        //判断是否在台上
                        if (user.getPublishState() == 0) {
                            if (TKRoomManager.getInstance().getMySelf().properties.containsKey("raisehand")) {
                                boolean israisehand = Tools.isTure(TKRoomManager.getInstance().getMySelf().properties.get("raisehand"));
                                if (israisehand) {
                                    TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId, "__all", "raisehand", false);
                                } else {
                                    TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId, "__all", "raisehand", true);
                                }
                            } else {
                                TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId, "__all", "raisehand", true);
                            }
                        } else {
                            TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId, "__all", "raisehand", false);
                        }
                        break;
                }
                return true;
            }
        });
        
        if (!RoomSession.isClassBegin) {
            playSelfBeforeClassBegin();
        }
        doLayout();
        
        if (TKRoomManager.getInstance().getMySelf().role == -1) {
            showPlayBackControlView();
            
            re_play_back.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    showPlayBackControlView();
                    return true;
                }
            });
        }
        
        if (!PermissionTest.cameraIsCanUse()) {
            Tools.threeshowDialog(this, R.string.remind, R.string.camera_hint, new Tools.OnDialogClick() {
                @Override
                public void dialog_ok(Dialog dialog) {
                    dialog.dismiss();
                }
            }, 0);
        }
        if (PermissionTest.getRecordState() == -2) {
            Tools.threeshowDialog(this, R.string.remind, R.string.mic_hint, new Tools.OnDialogClick() {
                @Override
                public void dialog_ok(Dialog dialog) {
                    dialog.dismiss();
                }
            }, 0);
        }
        
        setCheckBoxEnabled();
    }
    
    private void setCheckBoxEnabled() {
        //        cb_choose_mouse.setEnabled(true);
        cb_choose_photo.setEnabled(true);
        //        cb_large_or_small.setEnabled(true);
        //        cb_remark.setEnabled(true);
        cb_member_list.setEnabled(true);
        cb_file_person_media_list.setEnabled(true);
        cb_tool_case.setEnabled(true);
        cb_control.setEnabled(true);
        cb_message.setEnabled(true);
    }
    
    Timer timer = new Timer();
    boolean is_show_control_view = true;
    
    private void showPlayBackControlView() {
        if (is_show_control_view) {
            is_show_control_view = false;
            moveUpView(rel_play_back);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            backView(rel_play_back);
                            is_show_control_view = true;
                        }
                    });
                }
            }, 3000);
        }
    }
    
    private void moveUpView(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -120 * ScreenScale.getWidthScale());
        animator.setDuration(300);
        animator.start();
    }
    
    private void backView(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0);
        animator.setDuration(300);
        animator.start();
    }
    
    private void onRoomLeave() {
        removeVideoFragment();
        romoveScreenFragment();
        romoveMovieFragment();
        mediaListAdapter.setLocalfileid(-1);
        isWBMediaPlay = false;
        
        
        if (RoomClient.getInstance().isExit()) {
            clear();
            RoomSession.getInstance().clear();
            finish();
        }
        
        if (img_disk != null) {
            img_disk.clearAnimation();
            img_disk.setVisibility(View.INVISIBLE);
        }
        lin_audio_control.setVisibility(View.INVISIBLE);
    }
    
    private void onConnectionLost() {
        
        //当进入房间成功直接退出连接失败
        if (RoomSession.isInRoom) {
            return;
        }
        
        removeVideoFragment();
        romoveScreenFragment();
        romoveMovieFragment();
        mediaListAdapter.setLocalfileid(-1);
        isWBMediaPlay = false;
        
        re_loading.setVisibility(View.VISIBLE);
        if (frameAnimation != null) {
            frameAnimation.playAnimation();
            tv_load.setText(getString(R.string.connected));
        }
        
        if (popupWindowPhoto != null) {
            popupWindowPhoto.dismiss();
        }
        
        if (img_disk != null) {
            img_disk.clearAnimation();
            img_disk.setVisibility(View.INVISIBLE);
        }
        lin_audio_control.setVisibility(View.INVISIBLE);
    }
    
    private void onError(int errorCode, String errMsg) {
        if (errorCode == 10004) {  //UDP连接不同
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Tools.threeShowAlertDialog(OneToOneThreeActivity.this, getString(R.string.udp_alert), 0);
                }
            });
            
        } else if (10002 == errorCode) {
            if (frameAnimation != null) {
                frameAnimation.stopAnimation();
                re_loading.setVisibility(View.GONE);
            }
            removeVideoFragment();
            romoveScreenFragment();
            romoveMovieFragment();
            
            mediaListAdapter.setLocalfileid(-1);
            isWBMediaPlay = false;
            
            clear();
            finish();
        } else if (errorCode == 10005) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Tools.threeShowAlertDialog(OneToOneThreeActivity.this, getString(R.string.fire_wall_alert), 0);
                    TKRoomManager.getInstance().changeUserProperty(TKRoomManager.getInstance().getMySelf().peerId, "__all", "udpstate", 2);
                }
            });
        }
    }
    
    private void romoveScreenFragment() {
        RoomSession.isPublish = false;
        screenFragment = ScreenFragment.getInstance();
        fragmentManager = getFragmentManager();
        ft = fragmentManager.beginTransaction();
        if (screenFragment.isAdded()) {
            ft.remove(screenFragment);
            ft.commitAllowingStateLoss();
        }
    }
    
    private void romoveMovieFragment() {
        RoomSession.isPublish = false;
        movieFragment = MovieFragment.getInstance();
        fragmentManager = getFragmentManager();
        ft = fragmentManager.beginTransaction();
        if (movieFragment.isAdded()) {
            ft.remove(movieFragment);
            ft.commitAllowingStateLoss();
        }
    }
    
    private void clear() {
        RoomSession.isClassBegin = false;
        RoomClient.getInstance().setExit(false);
        RoomSession.playingMap.clear();
        RoomSession.playingList.clear();
        RoomSession.pandingSet.clear();
        RoomSession.chatList.clear();
        TKRoomManager.getInstance().registerRoomObserver(null);
        teacherItem.sf_video.release();
        stu_in_sd.sf_video.release();
        RoomClient.getInstance().setExit(false);
        WhiteBoradConfig.getsInstance().clear();
        RoomSession.getInstance().closeSpeaker();
    }
}
