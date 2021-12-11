/*
 *
 * Copyright 2015 TedXiong xiong-wei@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haoke91.videolibrary.videoplayer;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.haoke91.baselibrary.model.VideoUrl;
import com.haoke91.baselibrary.utils.AudioManagerUtils;
import com.haoke91.baselibrary.views.popwindow.EasyPopup;
import com.haoke91.videolibrary.MessageClickCallback;
import com.haoke91.videolibrary.R;
import com.haoke91.videolibrary.VideoApp;
import com.haoke91.videolibrary.VideoPlayCallbackImpl;
import com.orhanobut.logger.Logger;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * SuperVideoPlayer
 */
public abstract class BaseVideoPlayer<T extends MediaControlImpl> extends RelativeLayout {
    private final String TAG = "BaseVideoPlayer";
    public static final int LIVING_STATUS_UNSTART = 0;//课程未开始
    public static final int LIVING_STATUS_START = 1;//课程开始，但未推流
    public static final int LIVING_STATUS_LIVING = 2;//隐藏背景图
    public static final int LIVING_STATUS_PAUSE = 3;//暂停
    public static final int LIVING_STATUS_END = 4;//上课结束
    public static final int TIME_SHOW_CONTROLLER = 15000;
    public static final int MSG_HIDE_CONTROLLER = 10;
    //    private static final int MSG_UPDATE_PLAY_TIME = 11;
    private static final int MSG_PLAY_ON_TV_RESULT = 12;
    private static final int MSG_EXIT_FORM_TV_RESULT = 13;
    private static final int MESSAGE_HIDE_CENTER_BOX = 14;
    private VideoPlayerController.PageType mCurrPageType = VideoPlayerController.PageType.SHRINK;//当前是横屏还是竖屏
    public static boolean isOpenDanMu = true;
    
    protected Context mContext;
    protected T mMediaController;
    public MediaToolbar mMediaToolbar;
    protected VideoPlayCallbackImpl mVideoPlayCallback;
    //private ResolutionPanel mResolutionView;
    //private MoreSettingPanel mMoreView;
    private View mProgressBarView;
    //    protected AudinceListView rv_audience_list;
    
    //是否自动隐藏控制栏
    private boolean mAutoHideController = true;
    protected LinearLayout ll_volume, ll_brightness;//声音亮度
    private TextView tv_volume_num, tv_brightness;
    protected TextView tv_change_time;
    protected TXCloudVideoView mCloudVideoView;
    private MyHandler mHandler;
    private GestureDetector mGestureDetector;
    private AudioManager mAudioManager;
    private int mMaxVolume;
    private float brightness;
    protected TextView btn_load_fail;
    //    private LiveDanmuView mDanmuView;
    protected FrameLayout fl_controller;
    //    protected MessageListView view_chat_list;
    //    private PlayerPresenter mPlayerPresenter;
    //直播状态布局
    private View mRl_img;
    private ImageView mIv_play_bg;
    private TextView mTv_play_tip;
    public boolean isEnablePlaying = true;//播放器是否可播放
    public boolean isLiving = true;//是否直播 （回放）
    
    public BaseVideoPlayer(Context context) {
        super(context);
        initView(context);
    }
    
    public BaseVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    
    public BaseVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    
    private void initView(Context context) {
        mContext = context;
        View.inflate(context, R.layout.super_video_player_layout, this);
        mHandler = new MyHandler(this, mContext);
        //        view_chat_list = findViewById(R.id.view_chat_list);
        mRl_img = findViewById(R.id.rl_img);
        mIv_play_bg = findViewById(R.id.iv_play_bg);
        mTv_play_tip = findViewById(R.id.tv_play_tip);
        btn_load_fail = findViewById(R.id.btn_load_fail);
        mCloudVideoView = findViewById(R.id.cloud_video_view);
        //        mDanmuView = findViewById(R.id.view_danmaku);
        //        rv_audience_list = findViewById(R.id.view_audience_list);
        initPlayerConfig();
        mMediaToolbar = findViewById(R.id.video_toolbar);
        mProgressBarView = findViewById(R.id.progressbar);
        ll_volume = findViewById(R.id.ll_volume);
        ll_brightness = findViewById(R.id.ll_video_brightness_box);
        tv_volume_num = findViewById(R.id.tv_volume_num);
        tv_brightness = findViewById(R.id.tv_video_brightness);
        tv_change_time = findViewById(R.id.tv_change_time);
        fl_controller = findViewById(R.id.fl_controller);
        View bottom = getController();
        if (bottom != null) {
            fl_controller.addView(bottom);
            mMediaController = (T) fl_controller.getChildAt(0);
            mMediaController.setMediaControl(mMediaControl);
            mMediaToolbar.setMediaControl(mMediaControl);
            
        }
        //        view_chat_list.setMediaControl(mMediaControl);
        //        rv_audience_list.setMediaControl(mMediaControl);
        mCloudVideoView.setOnTouchListener(mOnTouchVideoListener);
        mGestureDetector = new GestureDetector(mContext, new MyGestureListener());
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mRl_img.setOnClickListener(onClickListener);
        btn_load_fail.setOnClickListener(onClickListener);
        
        if (mCurrPageType == VideoPlayerController.PageType.SHRINK) {
            mMediaToolbar.setMoreViewVis(GONE);
        }
        //        mCloudVideoView.requestDisallowInterceptTouchEvent(false);
        //        configDanmu();
        AudioManagerUtils.getInstance().requestFocus();
        
        
        BarUtils.addMarginTopEqualStatusBarHeight(mMediaToolbar);
    }
    
    //    private DanmakuContext mDanmuContext;
    
    
    protected abstract void initPlayerConfig();
    
    protected abstract View getController();
    
    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mRl_img) {
                showOrHideController(mContext);
            } else if (v == btn_load_fail) {
                if (!ObjectUtils.isEmpty(source)) {
                    VideoUrl videoUrl = source.get(source.size() - 1);
                    stopPlay();
                    setPlayUrl(videoUrl.getFormatUrl());
                    showProgressView(true);
                    btn_load_fail.setVisibility(GONE);
                }
            }
        }
    };
    
    
    private class MyHandler extends Handler {
        private final WeakReference<BaseVideoPlayer> mPlayer;
        private final WeakReference<Context> mContextRef;
        
        public MyHandler(BaseVideoPlayer player, Context context) {
            mPlayer = new WeakReference<>(player);
            mContextRef = new WeakReference<>(context);
        }
        
        @Override
        public void handleMessage(android.os.Message msg) {
            BaseVideoPlayer player = mPlayer.get();
            if (player != null) {
                switch (msg.what) {
                    //                    case BaseVideoPlayer.MSG_UPDATE_PLAY_TIME:
                    //                        //      Logger.e("MSG_UPDATE_PLAY_TIME");
                    //                        player.updatePlayTime();
                    //                        player.updatePlayProgress();
                    //                        break;
                    case BaseVideoPlayer.MSG_HIDE_CONTROLLER:
                        if (mContextRef == null) {
                            return;
                        }
                        Context context = mContextRef.get();
                        if (context != null && player.isControlbarVis()) {
                            // if (context != null) {
                            player.showOrHideController(context);
                        }
                        break;
                    case BaseVideoPlayer.MESSAGE_HIDE_CENTER_BOX:
                        if (player.ll_volume != null && player.ll_volume.getVisibility() == VISIBLE) {
                            player.ll_volume.setVisibility(View.GONE);
                        }
                        if (player.ll_brightness != null && player.ll_brightness.getVisibility() == VISIBLE) {
                            player.ll_brightness.setVisibility(View.GONE);
                        }
                        
                        break;
                    default:
                }
            }
        }
    }
    
    private long lastClickTime;
    private float rawDownY, rawDownX;
    private OnTouchListener mOnTouchVideoListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (mCurrPageType == VideoPlayerController.PageType.EXPAND) {
                mGestureDetector.onTouchEvent(motionEvent);
            }
            int action = motionEvent.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                // showOrHideController(mContext);
                rawDownY = motionEvent.getRawY();
                rawDownX = motionEvent.getRawX();
                //   hideMoreView();
                mHandler.removeMessages(MESSAGE_HIDE_CENTER_BOX);
            } else if (action == MotionEvent.ACTION_MOVE) {
            } else if (action == MotionEvent.ACTION_UP) {
                if (Math.abs((int) (rawDownY - motionEvent.getRawY())) < 40 && Math.abs((int) (rawDownX - motionEvent.getRawX())) < 40) {
                    long currentTime = Calendar.getInstance().getTimeInMillis();
                    if (currentTime - lastClickTime > 300) {
                        lastClickTime = currentTime;
                        showOrHideController(mContext);
                        
                    }
                }
                brightness = -1;
                mVolume = -1;
                mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_CENTER_BOX, 3000);
                if (mScrollMode == 3) {
                    tv_change_time.setVisibility(View.GONE);
                    setProgressChange();
                }
            }
            return true;
            // return mCurrPageType == MediaController.PageType.EXPAND;
        }
    };
    
    //    private void hideMoreView() {
    //
    //        if (mMoreView != null && mMoreView.getVisibility() == View.VISIBLE) {
    //            mMoreView.setVisibility(View.GONE);
    //        }
    //    }
    
    
    /**
     * 课间休息
     *
     * @param status livestatus
     */
    public void onRestMoment(int status, String imageUrl) {
        isEnablePlaying = false;
        mProgressBarView.setVisibility(GONE);
        if (status == LIVING_STATUS_UNSTART) {
            mRl_img.setVisibility(VISIBLE);
            mTv_play_tip.setText("还没开始上课，请稍等一下");
            mIv_play_bg.setBackgroundResource(R.mipmap.img_livestatus_unstart);
        } else if (status == LIVING_STATUS_START) {
            mRl_img.setVisibility(VISIBLE);
            mTv_play_tip.setText("已开始上课请注意听讲哦");
            if (!ObjectUtils.isEmpty(imageUrl)) {
                Glide.with(mContext).load(imageUrl).into(mIv_play_bg);
            } else {
                mIv_play_bg.setBackgroundResource(R.mipmap.img_livestatus_start);
            }
        } else if (status == LIVING_STATUS_LIVING) {
            //            mProgressBarView.setVisibility(VISIBLE);
            mRl_img.setVisibility(GONE);
        } else if (status == LIVING_STATUS_PAUSE) {
            mRl_img.setVisibility(VISIBLE);
            mTv_play_tip.setText("课间休息一下");
            mIv_play_bg.setBackgroundResource(R.mipmap.img_livestatus_wait);
        } else if (status == LIVING_STATUS_END) {
            mRl_img.setVisibility(VISIBLE);
            mTv_play_tip.setText("已经下课了，如果还没听懂过会还可以看课程回放哦");
            mIv_play_bg.setBackgroundResource(R.mipmap.img_livestatus_end);
        }
        pausePlay();
        if (mMediaController != null) {
            mMediaController.setPlayState(VideoPlayerController.PlayState.PAUSE);
        }
    }
    
    //    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        pausePlay();
        if (mMediaController != null) {
            mMediaController.setPlayState(VideoPlayerController.PlayState.PAUSE);
        }
    }
    
    //    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        resumePlay();
        if (mMediaController != null) {
            mMediaController.setPlayState(VideoPlayerController.PlayState.PLAY);
        }
    }
    
    /**
     * 关闭视频
     */
    //    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (mMediaController != null) {
            mMediaController.setPlayState(VideoPlayerController.PlayState.PAUSE);
        }
        stopHideTimer(true);
        stopPlay();
        mCloudVideoView.onDestroy();
    }
    
    public void setPlayUrl(String vodUrl) {
        showProgressView(true);
    }
    
    
    public void updateUI(String title) {
        mMediaToolbar.updateTitle(title);
        if (mMediaController != null) {
            mMediaController.updateUI();
        }
    }
    
    public void updateNums(String gold, String grow, String peoples) {
        mMediaToolbar.updateNums(gold, grow, peoples);
    }
    
    private EasyPopup speedPop;
    
    public class MediaControl implements VideoPlayerController.MediaControlImpl, MediaToolbar.MediaToolbarImpl, MessageClickCallback {
        
        @Override
        public void alwaysShowController() {
            BaseVideoPlayer.this.alwaysShowController();
        }
        
        @Override
        public void onPlayTurn() {
            if (isPlaying()) {
                pausePlay(true);
            } else {
                goOnPlay();
            }
        }
        
        
        @Override
        public void onProgressTurn(VideoPlayerController.ProgressState state, int progress) {
            if (state.equals(VideoPlayerController.ProgressState.START)) {
                mHandler.removeMessages(MSG_HIDE_CONTROLLER);
            } else if (state.equals(VideoPlayerController.ProgressState.STOP)) {
                resetHideTimer();
            } else {
                BaseVideoPlayer.this.onProgressTurn(progress);
                updatePlayTime();
            }
        }
        
        
        @Override
        public void onDanmaku() {
            if (mMessageCallBack != null) {
                mMessageCallBack.onDanmuToggle();
            }
            if (mVideoPlayCallback != null) {
                mVideoPlayCallback.onSwitchPageType();
            }
            
        }
        
        
        @Override
        public void onMoreSetting() {
        
        }
        
        @Override
        public void onBack() {
            if (mVideoPlayCallback != null) {
                mVideoPlayCallback.onBack();
            }
        }
        
        @Override
        public void onclick(final View view) {
            if (view.getId() == R.id.tv_resolution || view.getId() == R.id.tv_speed) {
                if (mVideoPlayCallback != null) {
                    mVideoPlayCallback.onClick(view);
                }
            }
            
        }
        
        @Override
        public void onResolutionChange(String videoUrl) {
            lastPlayTime = getCurrentPlaybackTime();
            BaseVideoPlayer.this.onResolutionChange(videoUrl);
            if (mMediaController != null) {
                mMediaController.setPlayState(VideoPlayerController.PlayState.PLAY);
            }
            
        }
        
        @Override
        public void removeHandlerMessage() {
            mHandler.removeMessages(MSG_HIDE_CONTROLLER);
            
        }
        
        @Override
        public void sendHandlerMessage(long time) {
            mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER, time);
        }
        
        @Override
        public void sendMessage(String message) {
            if (mMessageCallBack != null) {
                mMessageCallBack.onSendMessage(message);
            }
        }
        
        @Override
        public void changeTextOrEmoji() {
        
        }
        
        @Override
        public void onMessViewClick() {
            showOrHideController(mContext);
            
        }
        
        @Override
        public void onLoadHistory() {
            //            LogU.log("onLoadHistory");
            //            long time;
            //            if (ObjectUtils.isEmpty(view_chat_list.getData())) {
            //                time = System.currentTimeMillis();
            //            } else {
            //                time = view_chat_list.getData().get(0).getTime();
            //            }
            //            mPlayerPresenter.getHistory(time);
        }
        
        @Override
        public void onlySeeTeacher(boolean only) {
        }
        
        
        @Override
        public void onRateChange(float rate) {
            changeRate(rate);
        }
        
        @Override
        public void onPraise() {
            mMessageCallBack.onPraise();
        }
        
        @Override
        public void onFlower() {
            mMessageCallBack.onFlower();
        }
    }
    
    
    protected float lastPlayTime = 0;
    
    
    private MediaControl mMediaControl = new MediaControl();
    
    public MediaControl getMediaControl() {
        return mMediaControl;
    }
    
    protected void onCompletion() {
        mVideoPlayCallback.onPlayFinish();
        mPhoneListener.stopListen();
    }
    
    
    public void setVideoPlayCallback(VideoPlayCallbackImpl videoPlayCallback) {
        mVideoPlayCallback = videoPlayCallback;
    }
    
    public void setPageType(VideoPlayerController.PageType pageType) {
        if (mMediaController != null) {
            mMediaController.setPageType(pageType);
        }
        mCurrPageType = pageType;
        if (mCurrPageType == VideoPlayerController.PageType.SHRINK) {
            mMediaToolbar.setMoreViewVis(GONE);
            //   mResolutionView.setVisibility(GONE);
        } else {
            mMediaToolbar.setMoreViewVis(VISIBLE);
        }
    }
    
    
    /**
     * 获取已经播放的时长
     *
     * @return
     */
    protected float getCurrentPlaybackTime() {
        return 0;
    }
    
    
    /**
     * 改变seekbar
     *
     * @param progress
     */
    protected void onProgressTurn(int progress) {
    }
    
    protected void onResolutionChange(String url) {
    
    }
    
    protected abstract void pausePlay();
    
    protected void resumePlay() {
    
    }
    
    protected abstract void stopPlay();
    
    protected abstract boolean isPlaying();
    
    public void changeRate(float rate) {
    }
    
    
    /**
     * 暂停播放
     *
     * @param isShowController 是否显示控制条
     */
    public void pausePlay(boolean isShowController) {
        pausePlay();
        //  mTxplayer.pause();
        if (mMediaController != null) {
            mMediaController.setPlayState(VideoPlayerController.PlayState.PAUSE);
        }
        stopHideTimer(isShowController);
    }
    
    /***
     * 继续播放
     */
    public void goOnPlay() {
        resumePlay();
        if (mMediaController != null) {
            mMediaController.setPlayState(VideoPlayerController.PlayState.PLAY);
        }
        resetHideTimer();
        
    }
    
    
    public boolean isAutoHideController() {
        return mAutoHideController;
    }
    
    public void setAutoHideController(boolean autoHideController) {
        mAutoHideController = autoHideController;
    }
    
    
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        
        /**
         * 双击
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
            //                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
            //            else
            //                mLayout++;
            //            if (mVideoView != null)
            //                mVideoView.setVideoLayout(mLayout, 0);
            return true;
        }
        
        /**
         * 滑动
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (ObjectUtils.isEmpty(e1) || ObjectUtils.isEmpty(e2)) {
                return false;
            }
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getY();
            int windowWidth = ScreenUtils.getScreenWidth();
            int windowHeight = ScreenUtils.getScreenHeight();
            switch (mScrollMode) {
                case 0:
                    if (Math.abs(e2.getX() - e1.getX()) > 20) {
                        mScrollMode = 3;
                    } else {
                        if (mOldX > windowWidth / 2) {// 右边滑动
                            mScrollMode = 1;
                            
                        } else if (mOldX < windowWidth / 2) {// 左边滑动
                            mScrollMode = 2;
                        }
                    }
                    break;
                case 1:
                    float voicePercent = (mOldY - y) / windowHeight;
                    onVolumeSlide(voicePercent);
                    break;
                case 2:
                    float brightPercent = (mOldY - y) / windowHeight;
                    onBrightnessSlide(brightPercent);
                    break;
                case 3:
                    float dis = e2.getX() - e1.getX();
                    float percent = dis / windowWidth;
                    onProgressChange(percent);
                    break;
                
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
        
        @Override
        public boolean onDown(MotionEvent e) {
            
            mScrollMode = 0;
            return true;
        }
        
        
        //        @Override
        //        public boolean onSingleTapUp(MotionEvent e) {
        //            Log.e("TAG", "e====" + e.getAction());
        //            if (mScrollMode == 3 && e.getAction() == MotionEvent.ACTION_UP) {
        //                setProgressChange();
        //            }
        //
        //            return super.onSingleTapUp(e);
        //        }
        
    }
    
    //滑动 控制类型 1 声音 2 亮度  3 进度
    private int mScrollMode = 0;
    
    
    /**
     * 侧滑改变进度
     *
     * @param percent
     */
    protected void onProgressChange(float percent) {
    }
    
    protected void setProgressChange() {
    }
    
    private void onBrightnessSlide(float percent) {
        if (brightness < 0) {
            brightness = ((Activity) mContext).getWindow().getAttributes().screenBrightness;
            if (brightness <= 0.00f) {
                brightness = 0.50f;
            } else if (brightness < 0.01f) {
                brightness = 0.01f;
            }
        }
        WindowManager.LayoutParams lpa = ((Activity) mContext).getWindow().getAttributes();
        lpa.screenBrightness = brightness + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        ll_brightness.setVisibility(View.VISIBLE);
        ll_volume.setVisibility(View.GONE);
        tv_change_time.setVisibility(View.GONE);
        tv_brightness.setText(((int) (lpa.screenBrightness * 100)) + "%");
        ((Activity) mContext).getWindow().setAttributes(lpa);
    }
    
    private int mVolume;
    
    private void onVolumeSlide(float percent) {
        // if (mVolume == -1) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, mVolume, 0);//通話
            if (mVolume < 0) {
                mVolume = 0;
            }
        }
        // mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (mVolume < 0) {
            mVolume = 0;
        }
        ll_brightness.setVisibility(View.GONE);
        ll_volume.setVisibility(View.VISIBLE);
        tv_change_time.setVisibility(View.GONE);
        // 显示
        // mOperationBg.setImageResource(R.drawable.video_volumn_bg);
        //  mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        // }
        
        int index = (int) (percent * 2 * mMaxVolume) + mVolume;
        if (index > mMaxVolume) {
            index = mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }
        // 变更声音
        int i = (int) (index * 1.0 / mMaxVolume * 100);
        tv_volume_num.setText(String.valueOf(i));
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);//音樂
//        mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, index, 0);//通話
        setVolume(index);
    }
    
    
    public String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }
    
    
    public void loadVideo() {
        resetHideTimer();
        if (mMediaController != null) {
            mMediaController.setPlayState(VideoPlayerController.PlayState.PLAY);
        }
    }
    
    /**
     * 更新播放的进度时间
     */
    protected void updatePlayTime() {
    }
    
    /**
     * 音量控制
     *
     * @param volume
     */
    public void setVolume(int volume) {
    
    }
    
    
    /**
     * 更新播放进度条
     */
    protected void updatePlayProgress() {
    }
    
    
    /**
     * 显示loading圈
     *
     * @param isTransparentBg isTransparentBg
     */
    public void showProgressView(Boolean isTransparentBg) {
        //        mProgressBarView.setVisibility(VISIBLE);
        if (!isTransparentBg) {
            mProgressBarView.setBackgroundResource(android.R.color.black);
        } else {
            mProgressBarView.setBackgroundResource(android.R.color.transparent);
        }
        mProgressBarView.setVisibility(VISIBLE);
    }
    
    protected void hideProgressbar() {
        mProgressBarView.setVisibility(View.GONE);
    }
    
    private boolean isControlbarVis() {
        return mMediaToolbar != null && mMediaToolbar.getVisibility() == VISIBLE;
    }
    
    
    private boolean isShowAnimation;
    
    /***
     *
     * @param context
     */
    public void showOrHideController(Context context) {
        if (isShowAnimation) {
            return;
        }
        isShowAnimation = true;
        //        if (mMediaController != null && mMediaController.getVisibility() == View.VISIBLE) {
        if (mMediaToolbar.getVisibility() == View.VISIBLE) {
            if (!isLiving && mMediaController != null) {
                Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.anim_exit_from_bottom);
                
                animation.setAnimationListener(new AnimationImp() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isShowAnimation = false;
                        mMediaController.setVisibility(View.GONE);
                    }
                });
                mMediaController.startAnimation(animation);
                
            }
            
            Animation toolAnimation = AnimationUtils.loadAnimation(context,
                R.anim.anim_exit_from_top);
            toolAnimation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    isShowAnimation = false;
                    mMediaToolbar.setVisibility(View.GONE);
                }
            });
            mMediaToolbar.startAnimation(toolAnimation);
            
            BarUtils.setStatusBarVisibility((Activity) context, false);
            BarUtils.setStatusBarColor((Activity) context, Color.parseColor("#000000"));
            BarUtils.setStatusBarAlpha((Activity) context, 0x00);
        } else {
            if (!isLiving && mMediaController != null) {
                if (mMediaController != null) {
                    mMediaController.setVisibility(View.VISIBLE);
                    mMediaController.clearAnimation();
                    Animation animation = AnimationUtils.loadAnimation(context,
                        R.anim.anim_enter_from_bottom);
                    animation.setAnimationListener(new AnimationImp() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isShowAnimation = false;
                            
                        }
                    });
                    mMediaController.startAnimation(animation);
                    
                }
                
            }
            resetHideTimer();
            
            
            mMediaToolbar.setVisibility(View.VISIBLE);
            mMediaToolbar.clearAnimation();
            Animation toolAnimation = AnimationUtils.loadAnimation(context,
                R.anim.anim_enter_from_top);
            toolAnimation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    isShowAnimation = false;
                }
            });
            
            mMediaToolbar.startAnimation(toolAnimation);
            
            
            //            View decorView = ((Activity) context).getWindow().getDecorView();
            //            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//全部显示出来。
            BarUtils.setStatusBarVisibility((Activity) context, true);
            BarUtils.setStatusBarColor((Activity) context, Color.parseColor("#000000"));
            BarUtils.setStatusBarAlpha((Activity) context, 0x44);
        }
    }
    
    private void alwaysShowController() {
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        mMediaToolbar.setVisibility(View.VISIBLE);
        if (mMediaController != null) {
            mMediaController.setVisibility(View.VISIBLE);
        }
    }
    
    private void resetHideTimer() {
        if (!isAutoHideController()) {
            return;
        }
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER, TIME_SHOW_CONTROLLER);
    }
    
    private void stopHideTimer(boolean isShowController) {
        //   mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        if (mMediaController != null) {
            mMediaController.clearAnimation();
            mMediaController.setVisibility(isShowController ? View.VISIBLE : View.GONE);
            
        }
    }
    
    
    private class AnimationImp implements Animation.AnimationListener {
        
        @Override
        public void onAnimationEnd(Animation animation) {
        
        }
        
        @Override
        public void onAnimationRepeat(Animation animation) {
        }
        
        @Override
        public void onAnimationStart(Animation animation) {
        }
    }
    
    
    static class TXPhoneStateListener extends PhoneStateListener implements Application.ActivityLifecycleCallbacks {
        private WeakReference<BaseVideoPlayer> mPlayer;
        int activityCount;
        
        public TXPhoneStateListener(BaseVideoPlayer superVideoPlayer) {
            mPlayer = new WeakReference(superVideoPlayer);
        }
        
        public void startListen() {
            BaseVideoPlayer player = mPlayer.get();
            if (player != null) {
                player.startListen();
            }
        }
        
        public void stopListen() {
            BaseVideoPlayer player = mPlayer.get();
            if (player != null) {
                player.stopListen();
            }
        }
        
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            
            BaseVideoPlayer player = mPlayer.get();
            if (player != null) {
                player.onCallStateChange(state, activityCount);
            }
        }
        
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        
        }
        
        @Override
        public void onActivityPaused(Activity activity) {
        
        }
        
        @Override
        public void onActivityDestroyed(Activity activity) {
        
        }
        
        @Override
        public void onActivityResumed(Activity activity) {
            activityCount++;
            Logger.e("SuperVideoPlayer", "onActivityResumed" + activityCount);
        }
        
        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        
        }
        
        @Override
        public void onActivityStarted(Activity activity) {
        
        }
        
        @Override
        public void onActivityStopped(Activity activity) {
            activityCount--;
        }
        
        boolean isInBackground() {
            return (activityCount < 0);
        }
    }
    
    private void stopListen() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
        
        VideoApp.getApp().unregisterActivityLifecycleCallbacks(mPhoneListener);
    }
    
    private void startListen() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        
        VideoApp.getApp().registerActivityLifecycleCallbacks(mPhoneListener);
    }
    
    private void onCallStateChange(int state, int activityCount) {
        // TXVodPlayer player = mTxplayer;
        switch (state) {
            //电话等待接听
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d(TAG, "CALL_STATE_RINGING");
                //                if (player != null) {
                //                    player.pause();
                //                }
                pausePlay();
                break;
            //电话接听
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.d(TAG, "CALL_STATE_OFFHOOK");
                //                if (player != null) {
                //                    player.pause();
                //                }
                pausePlay();
                
                break;
            //电话挂机
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d(TAG, "CALL_STATE_IDLE");
                //                if (player != null && activityCount >= 0) {
                //                    player.resume();
                //                }
                resumePlay();
                
                break;
        }
    }
    
    protected TXPhoneStateListener mPhoneListener = new TXPhoneStateListener(this);
    
    
    private ArrayList<VideoUrl> source;
    
    public void setDataSource(ArrayList<VideoUrl> source) {
        if (!ObjectUtils.isEmpty(source)) {
            this.source = source;
            //   mResolutionView.setDataSource(source);
            if (mMediaController != null) {
                mMediaController.setDataSource(source);
                mMediaController.updateResolutionTxt(source.get(0).getFormatName());
                
            }
        }
        
    }
    
    public ArrayList<VideoUrl> getDataSource() {
        return source;
    }
    
    public void setBgImageVis(int vis) {
        if (!ObjectUtils.isEmpty(mRl_img)) {
            mRl_img.setVisibility(vis);
        }
    }
    
    private LiveMessageCallBack mMessageCallBack;
    
    public void setMediaCallBak(LiveMessageCallBack callBack) {
        this.mMessageCallBack = callBack;
    }
    
    public void setControlEnable(boolean enable) {
        mCloudVideoView.setEnabled(enable);
    }
    
    /**
     * 设置播放器是否可播放
     */
    public void enablePlay(boolean enablePlay) {
        isEnablePlaying = enablePlay;
    }
    
}






