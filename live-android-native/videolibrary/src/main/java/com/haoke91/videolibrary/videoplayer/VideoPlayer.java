package com.haoke91.videolibrary.videoplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.haoke91.baselibrary.utils.ACallBack;
import com.haoke91.videolibrary.utils.TCUtils;
import com.orhanobut.logger.Logger;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXBitrateItem;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;

import java.util.ArrayList;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/6/12 下午8:25
 * 修改人：weiyimeng
 * 修改时间：2018/6/12 下午8:25
 * 修改备注：
 */
public class VideoPlayer extends BaseVideoPlayer<VideoPlayerController> {
    
    private TXVodPlayer mTxplayer;
    private ACallBack<Integer> mACallBack;
    
    public VideoPlayer(Context context) {
        super(context);
    }
    
    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void setACallBack(ACallBack<Integer> callBack) {
        this.mACallBack = callBack;
    }
    
    @Override
    protected void initPlayerConfig() {
        isLiving = false;
        mTxplayer = new TXVodPlayer(mContext);
        TXVodPlayConfig mPlayConfig = new TXVodPlayConfig();
        //        mPlayConfig.setCacheFolderPath(getInnerSDCardPath() + "/txcache");
        //        mPlayConfig.setMaxCacheItems(5);
        mTxplayer.setConfig(mPlayConfig);
        mTxplayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        mTxplayer.setPlayerView(mCloudVideoView);
        mTxplayer.setVodListener(new ITXVodPlayListener() {
            @Override
            public void onPlayEvent(TXVodPlayer player, int event, Bundle param) {
                String playEventLog = "receive event: " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION);
                //                Logger.e(playEventLog);
                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) { //播放开始
                    if (mACallBack != null) {
                        mACallBack.call(ACallBack.ALIYUNVIDEO_STREAM);
                    }
                    if (mPhoneListener.isInBackground()) {
                        mTxplayer.pause();
                    }
                    //                    ArrayList<TXBitrateItem> bitrates = mTxplayer.getSupportedBitrates();
                    //                    Log.e("tag","bitrates====="+bitrates);
                    hideProgressbar();
                    if (mVideoPlayCallback != null) {
                        mVideoPlayCallback.onBeginPlay();
                    }
                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
                    //                    int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
                    //                    int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);
                    //                    mMediaController.setPlayProgressTxt(progress, duration);
                    updatePlayTime();
                    updatePlayProgress();
                    return;
                } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND) {
                    hideProgressbar();//隐藏加载中
                    btn_load_fail.setVisibility(VISIBLE);
                    //                        }
                    //                    }
                    return;
                    //                }
                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                    onCompletion();
                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {//2007缓冲开始
                    showProgressView(true);
                } else if (event == TXLiveConstants.PLAY_EVT_VOD_LOADING_END) {//2014 缓冲结束
                    hideProgressbar();
                } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) { //点播显示首帧画面
                    if (mPhoneListener.isInBackground()) {
                        mTxplayer.pause();
                    }
                    hideProgressbar();
                    
                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) { // 分辨率改变
                } else if (event == TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED) {//2013, 点播准备完成
                    if (lastPlayTime != 0) {
                        mTxplayer.seek(lastPlayTime);
                        updatePlayTime();
                        lastPlayTime = 0;
                    }
                } else if (event == TXLiveConstants.PLAY_WARNING_RECONNECT) {
                } else if (event == TXLiveConstants.PLAY_WARNING_RECV_DATA_LAG) {//缓冲结束
                    hideProgressbar();
                }
                
                if (event < 0) {
                    Toast.makeText(mContext, param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onNetStatus(TXVodPlayer player, Bundle status) {
            }
        });
        mTxplayer.enableHardwareDecode(true);
    }
    
    @Override
    protected View getController() {
        VideoPlayerController mediaController = new VideoPlayerController(mContext);
        mediaController.setVisibility(GONE);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mediaController.setLayoutParams(layoutParams);
        return mediaController;
    }
    
    
    public void setPlayUrl(String vodUrl) {
        super.setPlayUrl(vodUrl);
        mTxplayer.stopPlay(true);
        mTxplayer.setAutoPlay(true);
        mTxplayer.startPlay(vodUrl); // result返回值：0 success;  -1 empty url;
    }
    
    @Override
    protected void pausePlay() {
        mTxplayer.pause();
    }
    
    @Override
    protected void resumePlay() {
        mTxplayer.resume();
        
    }
    
    @Override
    protected void stopPlay() {
        mTxplayer.stopPlay(false);
    }
    
    @Override
    protected boolean isPlaying() {
        return mTxplayer.isPlaying();
    }
    
    @Override
    public void changeRate(float rate) {
        mTxplayer.setRate(rate);
    }
    
    @Override
    protected void onCompletion() {
        mMediaController.playFinish((int) mTxplayer.getDuration());
        super.onCompletion();
    }
    
    
    @Override
    protected void onProgressChange(float percent) {
        float progress = mMediaController.getProgress() / 100;
        percent = (progress + percent);
        
        if (percent > 1) {
            percent = 1;
        }
        if (percent < 0) {
            percent = 0;
        }
        
        time = percent * mTxplayer.getDuration();
        //        mTxplayer.seek(time);
        
        tv_change_time.setVisibility(View.VISIBLE);
        ll_volume.setVisibility(View.GONE);
        ll_brightness.setVisibility(View.GONE);
        String s = TCUtils.formattedTime((long) time);
        //        mTimeTxt2.setText(TCUtils.formattedTime(allSecond / 1000));
        tv_change_time.setText(s + " / " + TCUtils.formattedTime((long) mTxplayer.getDuration()));
    }
    
    float time;
    
    @Override
    protected void setProgressChange() {
        mTxplayer.seek(time);
        
    }
    
    @Override
    protected void onResolutionChange(String url) {
        setPlayUrl(url);
    }
    
    @Override
    protected float getCurrentPlaybackTime() {
        return mTxplayer.getCurrentPlaybackTime();
    }
    
    @Override
    protected void onProgressTurn(int progress) {
        float time = progress * mTxplayer.getDuration() / 100;
        mTxplayer.seek(time);
    }
    
    /**
     * 更新播放的进度时间
     */
    protected void updatePlayTime() {
        int allTime = (int) (mTxplayer.getDuration());
        int playTime = (int) (mTxplayer.getCurrentPlaybackTime());
        //  Logger.e("updatePlayTime===" + playTime);
        mMediaController.setPlayProgressTxt(playTime, allTime);
    }
    
    /**
     * 更新播放进度条
     */
    @Override
    protected void updatePlayProgress() {
        int allTime = (int) (mTxplayer.getDuration() * 1000);
        int playTime = (int) (mTxplayer.getCurrentPlaybackTime() * 1000);
        int bufferTime = (int) (mTxplayer.getBufferDuration() * 1000);
        if (allTime > 0) {
            int progress = playTime * 100 / allTime;
            int loadProgress = bufferTime * 100 / allTime;
            mMediaController.setProgressBar(progress, loadProgress);
            //            Log.e("tag", "updatePlayProgress====" + progress);
        }
    }
}
