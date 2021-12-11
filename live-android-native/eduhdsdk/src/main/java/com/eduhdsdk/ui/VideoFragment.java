package com.eduhdsdk.ui;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.classroomsdk.Config;
import com.classroomsdk.NotificationCenter;
import com.classroomsdk.Tools;
import com.classroomsdk.WBSession;
import com.classroomsdk.WhiteBoradConfig;
import com.classroomsdk.bean.ShareDoc;
import com.eduhdsdk.R;
import com.eduhdsdk.message.JSVideoWhitePadInterface;
import com.eduhdsdk.message.RoomControler;
import com.eduhdsdk.message.RoomSession;
import com.eduhdsdk.message.VideoWBCallback;
import com.eduhdsdk.tools.ScreenScale;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;


public class VideoFragment extends Fragment implements NotificationCenter.NotificationCenterDelegate, VideoWBCallback {

    private static VideoFragment mInstance = null;
    private View fragmentView;
    private RelativeLayout lin_video_play;
    private SurfaceViewRenderer suf_mp4;
    private ImageView img_close_mp4;
    private LinearLayout lin_video_control, ll_close_mp3;
    private ImageView img_play_mp4;
    private TextView txt_mp4_name;
    private TextView txt_mp4_time;
    private SeekBar sek_mp4;
    private ImageView img_voice_mp4;
    private XWalkView xWalkView;
    private SeekBar sek_voice_mp4;
    private String shareMediaPeerId;
    private Map<String, Object> shareMediaAttrs;
    private double vol = 0.5;
    private boolean isMute = false;
    private double ratio = (double) 16 / (double) 9;

    private EglRenderer.FrameListener frameListener;
    private RelativeLayout re_laoding;
    private ImageView loadingImageView;
    private SharedPreferences spkv = null;
    private SharedPreferences.Editor editor = null;
    //白板全屏右下角视频界面
    private RelativeLayout rel_fullscreen_mp4videoitem;
    private SurfaceViewRenderer fullscreen_sf_video;
    private int videoWidth;
    private int videoHeight;
    private int videoMarginRight;
    private int videoMarginBottom;
    private ImageView fullscreen_bg_video_back, fullscreen_img_video_back;
    private boolean isZoom;
    private boolean isOneToOne;

   /* //判断是否使用右下角视频
    private boolean isUseFullscreen = true;*/

    public void setStream(String shareMediaPeerId, Map<String, Object> shareMediaAttrs) {
        this.shareMediaPeerId = shareMediaPeerId;
        this.shareMediaAttrs = shareMediaAttrs;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    static public VideoFragment getInstance() {
        synchronized (VideoFragment.class) {
            if (mInstance == null) {
                mInstance = new VideoFragment();
            }
            return mInstance;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (fragmentView == null) {

            fragmentView = inflater.inflate(R.layout.fragment_video, null);

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

            lin_video_play = (RelativeLayout) fragmentView.findViewById(R.id.lin_video_play);
            suf_mp4 = (SurfaceViewRenderer) fragmentView.findViewById(R.id.suf_mp4);
            suf_mp4.init(EglBase.create().getEglBaseContext(), null);

            re_laoding = (RelativeLayout) fragmentView.findViewById(R.id.re_laoding);
            loadingImageView = (ImageView) fragmentView.findViewById(R.id.loadingImageView);

            img_close_mp4 = (ImageView) fragmentView.findViewById(R.id.img_close_mp4);
            lin_video_control = (LinearLayout) fragmentView.findViewById(R.id.lin_video_control);

            img_play_mp4 = (ImageView) lin_video_control.findViewById(R.id.img_play);
            txt_mp4_name = (TextView) lin_video_control.findViewById(R.id.txt_media_name);
            txt_mp4_time = (TextView) lin_video_control.findViewById(R.id.txt_media_time);

            sek_mp4 = (SeekBar) lin_video_control.findViewById(R.id.sek_media);
            sek_mp4.setPadding((int) (10 * ScreenScale.getWidthScale()), 0, (int) (10 * ScreenScale.getWidthScale()), 0);

            img_voice_mp4 = (ImageView) lin_video_control.findViewById(R.id.img_media_voice);
            sek_voice_mp4 = (SeekBar) lin_video_control.findViewById(R.id.sek_media_voice);
            sek_voice_mp4.setPadding((int) (10 * ScreenScale.getWidthScale()), 0, (int) (10 * ScreenScale.getWidthScale()), 0);
            ll_close_mp3 = (LinearLayout) lin_video_control.findViewById(R.id.ll_close_mp3);
            ll_close_mp3.setVisibility(View.GONE);

            //白板全屏右下角视频界面
            rel_fullscreen_mp4videoitem = (RelativeLayout) fragmentView.findViewById(R.id.rel_fullscreen_mp4videoitem);
            fullscreen_sf_video = (SurfaceViewRenderer) rel_fullscreen_mp4videoitem.findViewById(R.id.fullscreen_sf_video);
            fullscreen_sf_video.init(EglBase.create().getEglBaseContext(), null);
            fullscreen_bg_video_back = (ImageView) rel_fullscreen_mp4videoitem.findViewById(R.id.fullscreen_bg_video_back);
            fullscreen_img_video_back = (ImageView) rel_fullscreen_mp4videoitem.findViewById(R.id.fullscreen_img_video_back);

            xWalkView = (XWalkView) fragmentView.findViewById(R.id.video_white_board);

            XWalkPreferences.setValue("enable-javascript", true);
            XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
            XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
            XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
            XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);
            //xWalkView.setZOrderOnTop(true);
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
            if (Config.isWhiteVideoBoardTest) {
//                xWalkView.loadUrl("http://192.168.1.192:8444/publish/index.html#/mobileApp?loadComponentName=" + "videoDrawWhiteboardComponent");//支祥
//                xWalkView.loadUrl("http://192.168.1.64:8585/publish/index.html#/mobileApp?loadComponentName=" + "videoDrawWhiteboardComponent");
                xWalkView.loadUrl("http://192.168.0.200:9251/publish/index.html#/mobileApp?loadComponentName=" + "videoDrawWhiteboardComponent");//建行
//                xWalkView.loadUrl("http://192.168.1.220:9251/publish/index.html#/mobileApp?loadComponentName=" + "videoDrawWhiteboardComponent");
//                 xWalkView.loadUrl("http://192.168.1.108:8444/publish/index.html#/mobileApp?loadComponentName=" + "videoDrawWhiteboardComponent");//李珂
//                xWalkView.loadUrl("http://192.168.1.228:8444/publish/index.html#/mobileApp?loadComponentName=" + "videoDrawWhiteboardComponent");//李凯
//              xWalkView.loadUrl("http://192.168.1.182:1314/publish/index.html#/mobileApp_videoWhiteboard?videoDrawingBoardType=" + "mediaVideo");//广生
//                xWalkView.loadUrl("http://192.168.1.64:8585/publish/index.html#/mobileApp?loadComponentName=" + "videoDrawWhiteboardComponent");
            } else {
                xWalkView.loadUrl("file:///android_asset/react_mobile_new_publishdir/index.html#/mobileApp?loadComponentName=" + "videoDrawWhiteboardComponent");
            }
        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }

        ScreenScale.scaleView(fragmentView, "VideoFragment");
        //白板全屏右下角界面大小
        RelativeLayout.LayoutParams fullscreen_video_param = new RelativeLayout.LayoutParams(0, 0);
        fullscreen_video_param.width = videoWidth;
        fullscreen_video_param.height = videoHeight;
        fullscreen_video_param.rightMargin = videoMarginRight;
        fullscreen_video_param.bottomMargin = videoMarginBottom;
        fullscreen_sf_video.setLayoutParams(fullscreen_video_param);
        fullscreen_bg_video_back.setLayoutParams(fullscreen_video_param);
        fullscreen_img_video_back.setLayoutParams(fullscreen_video_param);

       /* if (!isUseFullscreen) {
            RelativeLayout.LayoutParams video_control_param = new RelativeLayout.LayoutParams(lin_video_control.getLayoutParams());
            video_control_param.rightMargin = videoWidth + videoMarginRight;
            lin_video_control.setLayoutParams(video_control_param);
        }*/

        return fragmentView;
    }

    public void setFullscreenHide() {
        rel_fullscreen_mp4videoitem.setVisibility(View.GONE);
        fullscreen_sf_video.setVisibility(View.INVISIBLE);
        fullscreen_img_video_back.setVisibility(View.GONE);
        fullscreen_bg_video_back.setVisibility(View.GONE);
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
        NotificationCenter.getInstance().removeObserver(this);
        super.onDetach();
    }

    public void setVoice() {
        if (img_voice_mp4 != null && sek_voice_mp4 != null) {
            if (isMute) {
                TKRoomManager.getInstance().setRemoteAudioVolume(0, shareMediaPeerId, 2);
                img_voice_mp4.setImageResource(R.drawable.icon_no_voice);
                sek_voice_mp4.setProgress(0);
            } else {
                TKRoomManager.getInstance().setRemoteAudioVolume(vol, shareMediaPeerId, 2);
                img_voice_mp4.setImageResource(R.drawable.icon_voice);
                sek_voice_mp4.setProgress((int) (vol * 100));
            }
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

        if (shareMediaPeerId != null) {
            /*suf_mp4.setZOrderOnTop(true);*/
            suf_mp4.setZOrderMediaOverlay(true);
            suf_mp4.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
            TKRoomManager.getInstance().playMedia(shareMediaPeerId, suf_mp4);
            suf_mp4.requestLayout();
            if (txt_mp4_name != null) {
                txt_mp4_name.setText((String) shareMediaAttrs.get("filename"));
            }
        }

        lin_video_play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showControlView();
                return true;
            }
        });

        if (TKRoomManager.getInstance().getMySelf().role == 0) {
            img_close_mp4.setVisibility(View.VISIBLE);
            lin_video_control.setVisibility(View.VISIBLE);
        } else {
            img_close_mp4.setVisibility(View.INVISIBLE);
            lin_video_control.setVisibility(View.INVISIBLE);
        }
        img_close_mp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*TKRoomManager.getInstance().unPlayMedia(shareMediaPeerId);*/
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                TKRoomManager.getInstance().stopShareMedia();
                TKRoomManager.getInstance().delMsg("VideoWhiteboard", "VideoWhiteboard", "__all", null);
                img_close_mp4.setClickable(false);
            }
        });

        img_play_mp4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (RoomSession.isPublish) {
                            boolean ispause = (Boolean) shareMediaAttrs.get("pause") == null ? false : (Boolean) shareMediaAttrs.get("pause");

                            if (ispause && RoomControler.isNotCloseVideoPlayer() && totleTime == 100) {
                                TKRoomManager.getInstance().seekMedia(0);
                            }

                            TKRoomManager.getInstance().playMedia(ispause);
                            if (ispause) {
                                if (TKRoomManager.getInstance().getMySelf().role == 0 && RoomSession.isClassBegin && RoomControler.isShowVideoWhiteBoard()) {
                                    TKRoomManager.getInstance().delMsg("VideoWhiteboard", "VideoWhiteboard", "__all", null);
                                }
                            } else {
                                if (TKRoomManager.getInstance().getMySelf().role == 0 && RoomSession.isClassBegin && RoomControler.isShowVideoWhiteBoard()) {
                                    JSONObject js = new JSONObject();
                                    try {
                                        js.put("videoRatio", ratio);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    TKRoomManager.getInstance().pubMsg("VideoWhiteboard", "VideoWhiteboard", "__all", js.toString(), true, "ClassBegin", null);
                                }
                            }
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
                                TKRoomManager.getInstance().startShareMedia(url, true, "__all", attrMap);
                            } else {
                                TKRoomManager.getInstance().startShareMedia(url, true, TKRoomManager.getInstance().getMySelf().peerId, attrMap);
                            }
                        }
                    }
                });

        sek_mp4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                if (isfromUser && shareMediaAttrs != null) {
                    currenttime = ((double) pro / (double) seekBar.getMax()) * (int) shareMediaAttrs.get("duration");
                    TKRoomManager.getInstance().seekMedia((long) currenttime);
                }
            }
        });

        TKRoomManager.getInstance().setRemoteAudioVolume(vol, shareMediaPeerId, 2);
        img_voice_mp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMute) {
                    TKRoomManager.getInstance().setRemoteAudioVolume(vol, shareMediaPeerId, 2);
                    img_voice_mp4.setImageResource(R.drawable.icon_voice);
                    sek_voice_mp4.setProgress((int) (vol * 100));
                } else {
                    TKRoomManager.getInstance().setRemoteAudioVolume(0, shareMediaPeerId, 2);
                    img_voice_mp4.setImageResource(R.drawable.icon_no_voice);
                    sek_voice_mp4.setProgress(0);
                }
                isMute = !isMute;
            }
        });
        sek_voice_mp4.setProgress((int) (vol * 100));
        sek_voice_mp4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float vol = (float) progress / (float) seekBar.getMax();
                if (vol > 0) {
                    img_voice_mp4.setImageResource(R.drawable.icon_voice);
                } else {
                    img_voice_mp4.setImageResource(R.drawable.icon_no_voice);
                }
                TKRoomManager.getInstance().setRemoteAudioVolume(vol, shareMediaPeerId, 2);
                if (fromUser) {
                    VideoFragment.this.vol = vol;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        frameListener = new EglRenderer.FrameListener() {
            @Override
            public void onFrame(final Bitmap bitmap) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        re_laoding.setVisibility(View.GONE);
                        showControlView();
                        setSync();
                    }
                });
            }
        };

        suf_mp4.addFrameListener(frameListener, 0);
    }

    public void hideLaoding(String peerIdVideo) {
        if (re_laoding != null) {
            re_laoding.setVisibility(View.GONE);
        }
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
                        if (user.publishState > 1 && user.publishState < 4 && !user.disablevideo) {
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

    int totleTime;

    public void controlMedia(Map<String, Object> onUpdateAttributeAttrs, long pos, boolean isPlay) {

        if (sek_mp4 != null) {
            int curtime = (int) ((double) pos / (int) onUpdateAttributeAttrs.get("duration") * 100);
            totleTime = curtime;
            sek_mp4.setProgress(curtime);
        }
        if (img_play_mp4 != null) {
            if (!isPlay) {
                img_play_mp4.setImageResource(R.drawable.btn_pause_normal);
            } else {
                img_play_mp4.setImageResource(R.drawable.btn_play_normal);
            }
        }

        if (txt_mp4_time != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss ");
            Date curDate = new Date(pos);
            Date daDate = new Date((int) onUpdateAttributeAttrs.get("duration"));
            String strcur = formatter.format(curDate);
            String strda = formatter.format(daDate);
            txt_mp4_time.setText(strcur + "/" + strda);
        }

        if (txt_mp4_name != null) {
            txt_mp4_name.setText((String) onUpdateAttributeAttrs.get("filename"));
        }

        if (onUpdateAttributeAttrs.containsKey("width") && onUpdateAttributeAttrs.containsKey("height") &&
                ((int) onUpdateAttributeAttrs.get("width")) != 0 && ((int) onUpdateAttributeAttrs.get("height")) != 0) {
            int wid = (int) onUpdateAttributeAttrs.get("width");
            int hid = (int) onUpdateAttributeAttrs.get("height");
            ratio = (double) wid / (double) hid;
        }
    }

    public void setSuf_mp4() {
        TKRoomManager.getInstance().unPlayMedia(shareMediaPeerId);

        if (suf_mp4 != null) {
            suf_mp4.removeFrameListener(frameListener);
            suf_mp4.pauseVideo();
            suf_mp4.setVisibility(View.GONE);
            frameListener = null;
            suf_mp4.release();
            suf_mp4 = null;

            if (fullscreen_sf_video != null) {
//            fullscreen_sf_video.surfaceDestroyed(fullscreen_sf_video.getHolder());
                fullscreen_sf_video.pauseVideo();
                fullscreen_sf_video.release();
                fullscreen_sf_video.setVisibility(View.GONE);
                fullscreen_sf_video = null;
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
        if (fullscreen_sf_video != null) {
            frameListener = null;
        }
        super.onStop();
    }

    Timer timer = new Timer();

    @Override
    public void onDestroyView() {
        RoomSession.jsVideoWBTempMsg = new JSONArray();
        if (suf_mp4 != null) {
            suf_mp4.release();
            suf_mp4 = null;
        }
        if (/*isUseFullscreen && */fullscreen_sf_video != null) {
            fullscreen_sf_video.release();
            fullscreen_sf_video = null;
        }
        isMute = false;
        if (xWalkView != null) {
            xWalkView.removeAllViews();
            xWalkView.onDestroy();
            xWalkView = null;
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        mInstance = null;
        JSVideoWhitePadInterface.getInstance().setVideoWBCallBack(null);
        super.onDestroyView();
    }

    private boolean onRemoteMsg(boolean add, String id, String name, long ts, Object data, boolean inList, String fromID, String associatedMsgID, String associatedUserID, JSONObject jsonObjectRemoteMsg) {
        if (add) {
            if (name.equals("VideoWhiteboard")) {
                if (xWalkView != null) {
                    if (suf_mp4 != null) {
                        suf_mp4.setZOrderMediaOverlay(false);
                        suf_mp4.setZOrderOnTop(false);
                    }
                    xWalkView.setVisibility(View.VISIBLE);
                    xWalkView.setZOrderOnTop(true);
                    /*img_close_mp4.setVisibility(View.GONE);*/
//                    if (Config.isWhiteVideoBoardTest) {
//                        xWalkView.loadUrl("http://192.168.1.182:1314/publish/index.html#/mobileApp_videoWhiteboard");//广生
//                    } else {
//                        xWalkView.loadUrl("file:///android_asset/react_mobile_video_whiteboard_publishdir/index.html#/mobileApp_videoWhiteboard");
//                    }
                }
            }
        } else {
            if (name.equals("VideoWhiteboard")) {
                RoomSession.jsVideoWBTempMsg = new JSONArray();
                if (xWalkView != null) {
                    if (suf_mp4 != null) {
                        suf_mp4.setZOrderMediaOverlay(true);
                    }
                    xWalkView.setVisibility(View.GONE);
                   /* xWalkView.setZOrderOnTop(false);*/
                   /* if (TKRoomManager.getInstance().getMySelf().role == 0) {
                        img_close_mp4.setVisibility(View.VISIBLE);
                    }*/
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
            j.put("_tplId", RoomSession._tplId);
            j.put("_skinId", RoomSession._skinId);
            j.put("_skinResource", RoomSession._skinResource);
            if (Tools.isTablet(getActivity())) {
                j.put("deviceType", "pad");
            } else {
                j.put("deviceType", "phone");
            }
            j.put("clientType", "android");
            j.put("languageType", getlanguageType());
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
            /*final JSONObject js = new JSONObject();
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
    }

    private String getlanguageType() {
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
        return lan;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (xWalkView != null) {
            xWalkView.onShow();
        }
    }

    /***
     * @param state    退出视频标注
     */
    @Override
    public void exitAnnotation(String state) {
        if (!TextUtils.isEmpty(state) && state.equals("media")) {
            TKRoomManager.getInstance().playMedia(true);
            if (TKRoomManager.getInstance().getMySelf().role == 0 && RoomSession.isClassBegin && RoomControler.isShowVideoWhiteBoard()) {
                TKRoomManager.getInstance().delMsg("VideoWhiteboard", "VideoWhiteboard", "__all", null);
            }
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

    private void moveUpView(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 120 * ScreenScale.getWidthScale(), 0);
        animator.setDuration(1000);
        animator.start();
    }

    private void backView(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, 120 * ScreenScale.getWidthScale());
        animator.setDuration(300);
        animator.start();
    }

    /**
     * 过滤掉用户多次点击的操作，防止动画错乱
     */
    boolean is_show_control_view = true;

    /**
     * 显示媒体控制栏，三 秒后隐藏
     */
    private void showControlView() {
        if (is_show_control_view) {
            is_show_control_view = false;
            moveUpView(lin_video_control);
            if (timer != null) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                backView(lin_video_control);
                                is_show_control_view = true;
                            }
                        });
                    }
                }, 3000);
            }
        }
    }

    @Override
    public void didReceivedNotification(final int id, final Object... args) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (id) {
                    case WBSession.onRemoteMsg:
                        boolean addRemoteMsg = (boolean) args[0];
                        String idRemoteMsg = (String) args[1];
                        String nameRemoteMsg = (String) args[2];
                        long tsRemoteMsg = (long) args[3];
                        Object dataRemoteMsg = (Object) args[4];
                        boolean inList1 = (boolean) args[5];
                        String fromIDRemoteMsg = (String) args[6];
                        String associatedMsgIDRemoteMsg = (String) args[7];
                        String associatedUserIDRemoteMsg = (String) args[8];
                        JSONObject jsonObjectRemoteMsg = (JSONObject) args[9];
                        onRemoteMsg(addRemoteMsg, idRemoteMsg, nameRemoteMsg, tsRemoteMsg, dataRemoteMsg, inList1, fromIDRemoteMsg,
                                associatedMsgIDRemoteMsg, associatedUserIDRemoteMsg, jsonObjectRemoteMsg);
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

   /* //设置是否使用右下角的小窗口
    public void setUseFullscreen(boolean useFullscreen) {
        isUseFullscreen = useFullscreen;
    }*/
}
