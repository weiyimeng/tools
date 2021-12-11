package com.haoke91.videolibrary.videoplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.haoke91.baselibrary.model.VideoUrl;
import com.haoke91.baselibrary.views.popwindow.EasyPopup;
import com.haoke91.baselibrary.views.popwindow.ListPopup;
import com.haoke91.baselibrary.views.popwindow.XGravity;
import com.haoke91.baselibrary.views.popwindow.YGravity;
import com.haoke91.videolibrary.R;
import com.haoke91.videolibrary.utils.TCUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ted on 2015/8/4.
 * MediaController
 */
public class VideoPlayerController extends MediaControlImpl implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private ImageView mPlayImg;//播放按钮
    private SeekBar mProgressSeekBar;//播放进度条
    private TextView mTimeTxt1, mTimeTxt2;//播放时间
    private ImageView mExpandImg;//最大化播放按钮
    private TextView mResolutionTxt;//清晰度
    private MediaControlImpl mMediaControl;
    //    private String mScanVodUrl;
    private TextView tv_video_speed;//播放速度
    private EasyPopup speedPop;
    private ListPopup resolutionPop;
    
    public VideoPlayerController(Context context) {
        super(context);
        initView(context);
    }
    
    public VideoPlayerController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    
    public VideoPlayerController(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    
    
    private void initView(Context context) {
        View.inflate(context, R.layout.biz_video_media_controller, this);
        mPlayImg = findViewById(R.id.pause);
        mProgressSeekBar = findViewById(R.id.media_controller_progress);
        mTimeTxt1 = findViewById(R.id.time_pos);
        mTimeTxt2 = findViewById(R.id.time_duration);
        mExpandImg = findViewById(R.id.expand);
        mResolutionTxt = findViewById(R.id.tv_video_resolution);
        tv_video_speed = findViewById(R.id.tv_video_speed);
        initData();
    }
    
    private void initData() {
        mProgressSeekBar.setOnSeekBarChangeListener(this);
        mPlayImg.setOnClickListener(this);
        mResolutionTxt.setOnClickListener(this);
        mExpandImg.setOnClickListener(this);
        tv_video_speed.setOnClickListener(this);
        setPageType(PageType.SHRINK);
        setPlayState(PlayState.PAUSE);
        //  mResolutionTxt.setText(mTitleStrList.get(0));
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
        if (isFromUser) {
            mMediaControl.onProgressTurn(ProgressState.DOING, progress);
        }
    }
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mMediaControl.onProgressTurn(ProgressState.START, 0);
    }
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMediaControl.onProgressTurn(ProgressState.STOP, 0);
    }
    
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pause) {
            mMediaControl.onPlayTurn();
        } else if (view.getId() == R.id.expand) {
            mMediaControl.onDanmaku();
        } else if (view.getId() == R.id.tv_video_resolution) {
            //mMediaControl.onResolutionTurn();
            if (!ObjectUtils.isEmpty(mMediaControl)) {
                mMediaControl.removeHandlerMessage();
            }
            if (ObjectUtils.isEmpty(resolutionPop)) {
                resolutionPop = ListPopup.create()
                    .setContext(getContext())
                    .setPlayInfo(getDataSource())
                    .setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            if (!ObjectUtils.isEmpty(mMediaControl)) {
                                mMediaControl.sendHandlerMessage(BaseVideoPlayer.TIME_SHOW_CONTROLLER);
                            }
                            
                        }
                    })
                    .setOnViewClickListener(new ListPopup.OnViewClickListener() {
                        @Override
                        public void onVideoClick(VideoUrl videoUrl) {
                            if (!ObjectUtils.isEmpty(mMediaControl)) {
                                mMediaControl.onResolutionChange(videoUrl.getFormatUrl());
                                mResolutionTxt.setText(videoUrl.getFormatName());
                            }
                        }
                    })
                    .apply();
            }
            resolutionPop.showAtAnchorView(view, YGravity.ABOVE, XGravity.CENTER);
            
            
        } else if (view.getId() == R.id.tv_video_speed) {
            if (!ObjectUtils.isEmpty(mMediaControl)) {
                mMediaControl.removeHandlerMessage();
            }
            if (ObjectUtils.isEmpty(speedPop)) {
                speedPop = EasyPopup.create()
                    .setContentView(getContext(), R.layout.layout_any)
                    .setFocusAndOutsideEnable(true)
                    .setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            if (!ObjectUtils.isEmpty(mMediaControl)) {
                                mMediaControl.sendHandlerMessage(BaseVideoPlayer.TIME_SHOW_CONTROLLER);
                            }
                        }
                    })
                    .setOnViewListener(new EasyPopup.OnViewListener() {
                        @Override
                        public void initViews(View view) {
                            setSpeedClickListener(view);
                            //     speedPop.dismiss();
                        }
                    })
                    .apply();
            }
            speedPop.showAtAnchorView(view, YGravity.ABOVE, XGravity.CENTER);
            
        }
    }
    
    private TextView lastSpeedView;
    
    public void setSpeedClickListener(View view) {
        if (lastSpeedView == view) {
            return;
        }
        lastSpeedView = view.findViewById(R.id.tv_speed_2);
        lastSpeedView.setTextColor(Color.parseColor("#00ff00"));
        view.findViewById(R.id.tv_speed_1).setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_speed_2).setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_speed_3).setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_speed_4).setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_speed_5).setOnClickListener(onClickListener);
        //        view.findViewById(R.id.tv_speed_6).setOnClickListener(onClickListener);
    }
    
    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            speedPop.dismiss();
            
            if (lastSpeedView == view) {
                return;
            }
            //            if (lastSpeedView == null) {
            //                lastSpeedView = (TextView) view;
            //            }
            lastSpeedView.setTextColor(Color.parseColor("#000000"));
            lastSpeedView = (TextView) view;
            lastSpeedView.setTextColor(Color.parseColor("#00ff00"));
            if (view.getId() == R.id.tv_speed_1) {
                mMediaControl.onRateChange(0.8f);
                tv_video_speed.setText("0.8");
            } else if (view.getId() == R.id.tv_speed_2) {
                mMediaControl.onRateChange(1.0f);
                tv_video_speed.setText("1.0");
                
            } else if (view.getId() == R.id.tv_speed_3) {
                mMediaControl.onRateChange(1.25f);
                tv_video_speed.setText("1.25");
                
            } else if (view.getId() == R.id.tv_speed_4) {
                mMediaControl.onRateChange(1.5f);
                tv_video_speed.setText("1.5");
            } else if (view.getId() == R.id.tv_speed_5) {
                mMediaControl.onRateChange(2.0f);
                tv_video_speed.setText("2.0");
                
            }
            //            else if (view.getId() == R.id.tv_speed_6){
            //                mMediaControl.onRateChange(3.0f);
            //                tv_video_speed.setText("3.0X");
            //
            //            }
        }
    };
    
    
    //    /***
    //     * 强制横屏模式
    //     */
    //    public void forceLandscapeMode() {
    //        mExpandImg.setVisibility(INVISIBLE);
    //        //   mResolutionTxt.setVisibility(INVISIBLE);
    //    }
    
    
    public void setProgressBar(int progress, int secondProgress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 100) {
            progress = 100;
        }
        if (secondProgress < 0) {
            secondProgress = 0;
        }
        if (secondProgress > 100) {
            secondProgress = 100;
        }
        mProgressSeekBar.setProgress(progress);
        //   Logger.e("secondProgress===" + secondProgress);
        mProgressSeekBar.setSecondaryProgress(secondProgress);
    }
    
    public void setPlayState(PlayState playState) {
        mPlayImg.setImageResource(playState.equals(PlayState.PLAY) ? R.drawable.ic_video_stop : R.drawable.ic_video_play);
    }
    
    public void setPageType(PageType pageType) {
        //        mExpandImg.setVisibility(pageType.equals(PageType.EXPAND) ? GONE : VISIBLE);
        //        mResolutionTxt.setVisibility(pageType.equals(PageType.SHRINK) ? GONE : VISIBLE);
    }
    
    public void setPlayProgressTxt(int nowSecond, int allSecond) {
        mTimeTxt1.setText(TCUtils.formattedTime(nowSecond));
        mTimeTxt2.setText(TCUtils.formattedTime(allSecond));
    }
    
    public void playFinish(int allTime) {
        setPlayProgressTxt(0, allTime);
        setPlayState(PlayState.PAUSE);
        mPlayImg.setImageResource(R.drawable.ic_vod_play_normal);
    }
    
    /**
     * 返回当前进度
     *
     * @return
     */
    public float getProgress() {
        return mProgressSeekBar.getProgress();
    }
    
    public void setMediaControl(MediaControlImpl mediaControl) {
        mMediaControl = mediaControl;
    }
    
    
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        if (visibility == GONE) {
            if (!ObjectUtils.isEmpty(speedPop)) {
                speedPop.dismiss();
            }
            if (!ObjectUtils.isEmpty(resolutionPop)) {
                resolutionPop.dismiss();
            }
        }
    }
    
    @SuppressLint("SimpleDateFormat")
    private String formatPlayTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }
    
    public void setPlayUrl(String vodUrl) {
        //        mScanVodUrl = vodUrl;
    }
    
    public void updateUI() {
        setPlayState(PlayState.PLAY);
    }
    
    
    public void updateResolutionTxt(String info) {
        mResolutionTxt.setText(info);
    }
    
    /**
     * 播放样式 展开、缩放
     */
    public enum PageType {
        EXPAND, SHRINK
    }
    
    /**
     * 播放状态 播放 暂停
     */
    public enum PlayState {
        PLAY, PAUSE
    }
    
    public enum ProgressState {
        START, DOING, STOP
    }
    
    
    public interface MediaControlImpl {
        void onDanmaku();
        
        void onPlayTurn();
        
        
        void onProgressTurn(ProgressState state, int progress);
        
        //        void onResolutionTurn();
        
        void alwaysShowController();
        
        void onRateChange(float rate);
        
        void onResolutionChange(String index);
        
        void removeHandlerMessage();
        
        void sendHandlerMessage(long time);
        
        void sendMessage(String message);
        
        void changeTextOrEmoji();
        
        void onlySeeTeacher(boolean only);
        
        /**
         * 点赞
         */
        void onPraise();
        
        /**
         * 送花
         */
        void onFlower();
    }
    
}
