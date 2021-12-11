package com.haoke91.videolibrary.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.haoke91.baselibrary.utils.ACallBack;
import com.haoke91.videolibrary.R;
import com.haoke91.videolibrary.model.AudienceBean;
import com.orhanobut.logger.Logger;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/6/19 上午9:54
 * 修改人：weiyimeng
 * 修改时间：2018/6/19 上午9:54
 * 修改备注：
 */
public class AgoraPlayer extends BaseVideoPlayer<LivePlayerController> {
    private final String TAG = "agora===";
    private AudienceListView rv_audience_list;
    private RtcEngine mRtcEngine;// Tutorial Step 1
    private ArrayList<AudienceBean> data;
    //    private AudienceListAdapter adapter;
    private final int uid_teacher = 1;
    private int uid_my = 0;
    private String token = null, appId = "", channel = "";
    private SurfaceView mTeacherSurfaceView;
    private int lowCount = 0;
    private ACallBack<Integer> mACallBack;
    
    public AgoraPlayer(Context context) {
        super(context);
    }
    
    public AgoraPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public AgoraPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    
    @Override
    protected void initPlayerConfig() {
        isLiving = true;
        data = new ArrayList();
        //        initializeAgoraEngine();
        
    }
    
    public void setACallBack(ACallBack<Integer> callBack) {
        this.mACallBack = callBack;
    }
    
    public void create(String token, String appId, String channel, int uId) {
        this.token = token;
        this.appId = appId;
        this.channel = channel;
        this.uid_my = uId;
        initializeAgoraEngine();
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);//通话 还是直播
        mRtcEngine.setAudioProfile(Constants.AudioProfile.getValue(Constants.AudioProfile.MUSIC_HIGH_QUALITY), Constants.AudioScenario.getValue(Constants.AudioScenario.SHOWROOM));//设置媒体通道
        mRtcEngine.joinChannel(this.token, this.channel, "Extra Optional Data", uid_my); // if you do not specify the uid, we will generate the uid for you
        //声网直播去掉清晰度
        mMediaToolbar.findViewById(R.id.tv_resolution).setVisibility(GONE);
    }
    
    public void setListView(AudienceListView rv_audience_list) {
        this.rv_audience_list = rv_audience_list;
        this.rv_audience_list.setVisibility(GONE);
        this.rv_audience_list.setData(data);
        
    }
    
    private void initializeAgoraEngine() {
        try {
            //appid:473409521ed747c4ae622168ea194c5a
            mRtcEngine = RtcEngine.create(mContext, appId, new IRtcEngineEventHandler() { // Tutorial Step 1
                @Override
                public void onFirstRemoteVideoDecoded(final int uid, final int width, final int height, int elapsed) { // Tutorial Step 5
                    Log.e(TAG, "====uid" + uid + "===width==" + width + "==height===" + height + "==elapsed==" + elapsed);
                    final TXCloudVideoView render = findViewById(R.id.cloud_video_view);
                    if (mACallBack != null){
                        mACallBack.call(ACallBack.AGORA_STREAM);
                    }
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mTeacherSurfaceView == null){
                                mTeacherSurfaceView = RtcEngine.CreateRendererView(mContext);
                                mTeacherSurfaceView.setZOrderMediaOverlay(true);
                                AudienceBean bean = new AudienceBean(uid, mTeacherSurfaceView);
                                data.add(0, bean);
                                render.addView(mTeacherSurfaceView, 0);
                                mRtcEngine.setRemoteVideoStreamType(uid, Constants.VIDEO_STREAM_HIGH);
                                mRtcEngine.setupRemoteVideo(new VideoCanvas(mTeacherSurfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));
                            }
                            if (!isEnablePlaying){
                                mTeacherSurfaceView.setVisibility(GONE);
                                mRtcEngine.pauseAudio();
                            } else{
                                mTeacherSurfaceView.setVisibility(VISIBLE);
                                mRtcEngine.enableAudio();
                            }
                        }
                    });
                    
                }
                
                @Override
                public void onRemoteVideoStateChanged(int uid, int state) {
                    super.onRemoteVideoStateChanged(uid, state);
                    Log.e(TAG, "onRemoteVideoState" + state);
                }
                
                @Override
                public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
                    super.onJoinChannelSuccess(channel, uid, elapsed);
                    Log.e(TAG, "onJoinSuccess");
                    //                    ToastUtils.showShort("进入房间");
                }
                
                @Override
                public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
                    super.onRejoinChannelSuccess(channel, uid, elapsed);
                    Log.e(TAG, "=== onRejoinChannelSuccess");
                }
                
                @Override
                public void onUserJoined(final int uid, int elapsed) {
                    if (data != null && data.size() >= 0 && data.get(0).uuid == uid){
                        onResume();
                        if (isEnablePlaying && mTeacherSurfaceView != null){
                            //                            onRestMoment(LIVING_STATUS_LIVING, "");
                            mTeacherSurfaceView.setVisibility(VISIBLE);
                            mTeacherSurfaceView.setZOrderMediaOverlay(true);
                            //                        btn_load_fail.setVisibility(GONE);
                        }
                    }
                    Log.e(TAG, "onUserJoined========" + uid);
                }
                
                @Override
                public void onUserOffline(final int uid, int reason) { // Tutorial Step 7
                    Log.e(TAG, "onUserOffline===老师退出房间,断流");
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //                            if (mTeacherSurfaceView != null){
                            //                                mTeacherSurfaceView.setZOrderMediaOverlay(false);
                            //                                mTeacherSurfaceView.setVisibility(GONE);
                            //                            }
                            //                            btn_load_fail.setVisibility(VISIBLE);
                            //                            onRestMoment(BaseVideoPlayer.LIVING_STATUS_END, "");
                        }
                    });
                }
                
                //其他用户已停发/已重发视频流回调
                @Override
                public void onUserMuteVideo(final int uid, final boolean muted) { // Tutorial Step 10
                    Log.e(TAG, "onUserMuteVideo========" + muted);
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onRemoteUserVideoMuted(uid, muted);
                        }
                    });
                }
                
                @Override
                public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
                    super.onNetworkQuality(uid, txQuality, rxQuality);
                    //                    Log.e(TAG, "tx==" + txQuality + "  rx==" + rxQuality);
                }
                
                @Override
                public void onRemoteVideoStats(RemoteVideoStats stats) {
                    super.onRemoteVideoStats(stats);
                    if (stats.receivedFrameRate < 10){
                        lowCount++;
                    } else{
                        lowCount = 0;
                    }
                    if (lowCount > 10 && lowCount % 10 == 1){
                        //                        ToastUtils.showShort("检测到视频卡顿");
                    }
                }
                
                @Override
                public void onLeaveChannel(RtcStats stats) {
                    super.onLeaveChannel(stats);
                }
                
                @Override
                public void onWarning(int warn) {
                    super.onWarning(warn);
                    Log.e(TAG, "onWarning" + warn);
                    if (warn == Constants.WARN_ADM_PLAYOUT_AUDIO_LOWLEVEL){
                        //未获取到流信息
                    }
                }
                
                @Override
                public void onError(int err) {
                    Log.e("tag", "错误code==" + err);
                }
                
                @Override
                public void onStreamMessageError(int uid, int streamId, int error, int missed, int cached) {
                    super.onStreamMessageError(uid, streamId, error, missed, cached);
                    Log.e("agora===", "onError===" + error);
                }
                
                @Override
                public void onConnectionInterrupted() {
                    super.onConnectionInterrupted();
                    Log.e("agora===", "===onConnectionInterrupted");
                    ToastUtils.showShort("连接中断");
//                    pausePlay();
                }
                
                @Override
                public void onConnectionLost() {
                    super.onConnectionLost();
                    Log.e(TAG, "===onConnectionLost");
                }
                
                @Override
                public void onConnectionBanned() {
                    super.onConnectionBanned();
                    Logger.e("===onConnectionBanned 服务器禁止回调");
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "e===" + e);
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
        //        mRtcEngine.disableAudio();
        mRtcEngine.enableVideo();
        mRtcEngine.enableLocalVideo(false);
        mRtcEngine.muteLocalVideoStream(true);
        mRtcEngine.muteLocalAudioStream(true);
        // false 不交换宽高
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);
        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
        mRtcEngine.setVideoQualityParameters(true);
        mRtcEngine.setParameters("{\"che.audio.live_for_comm\":true}");
        mRtcEngine.setParameters("{\"che.video.moreFecSchemeEnable\":true}");
        //        mRtcEngine.setParameters("{\"che.video.lowBitRateStreamParameter\":{\"width\":240,\"height\":180,\"frameRate\":15,\"bitRate\":140}}");//小流
        mRtcEngine.setParameters("{\"che.video.lowBitRateStreamParameter\":{\"width\":640,\"height\":360,\"frameRate\":15,\"bitRate\":140}}");//大流
        
        // 双流模式，区分大小流
        mRtcEngine.enableDualStreamMode(true);
        
    }
    
    private void onRemoteUserVideoMuted(int uid, boolean muted) {
        //        if (isEnablePlaying){
        //            btn_load_fail.setVisibility(muted ? VISIBLE : GONE);
        //        }
        //        SurfaceView surfaceView = (SurfaceView) rv_list.getChildAt(0);
        //
        //        Object tag = surfaceView.getTag();
        //        if (tag != null && (Integer) tag == uid) {
        //            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
        //        }
    }
    
    @Override
    protected View getController() {
        LivePlayerController mediaController = new LivePlayerController(mContext);
        mediaController.setVisibility(GONE);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mediaController.setLayoutParams(layoutParams);
        return mediaController;
    }
    
    @Override
    protected void pausePlay() {
        if (mRtcEngine != null){
            mRtcEngine.pauseAudio();
        }
    }
    
    @Override
    protected void resumePlay() {
        if (isEnablePlaying && mRtcEngine != null){
            mRtcEngine.resumeAudio();
            if (mTeacherSurfaceView != null){
                mTeacherSurfaceView.setVisibility(VISIBLE);
                mTeacherSurfaceView.setZOrderMediaOverlay(true);
            }
        }
    }
    
    @Override
    protected void stopPlay() {
        leaveChannel();
        if (mRtcEngine != null){
            mRtcEngine.destroy();
        }
        mRtcEngine = null;
    }
    
    private void leaveChannel() {
        if (mRtcEngine != null){
            mRtcEngine.leaveChannel();
        }
    }
    
    @Override
    protected boolean isPlaying() {
        return false;
    }
    
}
