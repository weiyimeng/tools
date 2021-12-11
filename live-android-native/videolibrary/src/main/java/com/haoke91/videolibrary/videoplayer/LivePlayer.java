package com.haoke91.videolibrary.videoplayer;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.haoke91.baselibrary.utils.ACallBack;
import com.orhanobut.logger.Logger;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/6/13 上午11:30
 * 修改人：weiyimeng
 * 修改时间：2018/6/13 上午11:30
 * 修改备注：
 */
public class LivePlayer extends BaseVideoPlayer<LivePlayerController> {
    private TXLivePlayer mTxplayer;
    private ACallBack<Integer> mACallBack;
    
    public LivePlayer(Context context) {
        super(context);
    }
    
    public LivePlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public LivePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void setACallBack(ACallBack<Integer> callBack) {
        this.mACallBack = callBack;
    }
    
    @Override
    protected void initPlayerConfig() {
        mTxplayer = new TXLivePlayer(mContext);
        TXLivePlayConfig mPlayConfig = new TXLivePlayConfig();
        //        mPlayConfig.setCacheFolderPath(getInnerSDCardPath() + "/txcache");
        //        mPlayConfig.setMaxCacheItems(5);
        mPlayConfig.setConnectRetryCount(1);
        mPlayConfig.setConnectRetryInterval(10);//SDK重连间隔
        
        //自动模式
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(5);
        
        mTxplayer.setConfig(mPlayConfig);
        mTxplayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        mTxplayer.setPlayerView(mCloudVideoView);
        mTxplayer.setPlayListener(new ITXLivePlayListener() {
            
            @Override
            public void onPlayEvent(int event, Bundle param) {
                String playEventLog = "receive event: " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION);
                Logger.e(playEventLog);
                //                Log.e("tag", "playEventLog===" + playEventLog);
                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {//播放开始
                    if (mACallBack != null) {
                        mACallBack.call(ACallBack.ALIYUNLIVE_STREAM);
                    }
                    hideProgressbar();
                    setControlEnable(true);
                    btn_load_fail.setVisibility(GONE);
                } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {// 经多次自动重连失败，放弃连接
                    hideProgressbar();
                    btn_load_fail.setVisibility(VISIBLE);
                    //                        }
                    //                    }
                    return;
                    //                }
                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {//2006	视频播放结束
                    onCompletion();
                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {//视频播放 loading，如果能够恢复，之后会有 BEGIN 事件
                    // startLoadingAnimation();
                    showProgressView(true);
                    
                } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
                    hideProgressbar();
                    // stopLoadingAnimation();
                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
                    // streamRecord(false);
                } else if (event == TXLiveConstants.PLAY_EVT_STREAM_SWITCH_SUCC) {
                    hideProgressbar();
                    btn_load_fail.setVisibility(VISIBLE);
                    
                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_ROTATION) {
                    return;
                }
                if (event < 0) {
                    Toast.makeText(mContext, param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onNetStatus(Bundle bundle) {
            
            }
        });
        mTxplayer.enableHardwareDecode(true);
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
    protected void onResolutionChange(String url) {
        showProgressView(true);
        btn_load_fail.setVisibility(GONE);
        mTxplayer.switchStream(url);
    }
    
    public void setPlayUrl(String videoUrl) {
        super.setPlayUrl(videoUrl);
        if (TextUtils.isEmpty(videoUrl)) {
            return;
        }
        btn_load_fail.setVisibility(View.GONE);
        if (videoUrl.startsWith("rtmp")){
            mTxplayer.startPlay(videoUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
        } else {
            mTxplayer.startPlay(videoUrl, TXLivePlayer.PLAY_TYPE_LIVE_FLV);
        }
        // resetHideTimer();
        //        setBgImageVis(GONE);
        mMediaController.setPlayState(VideoPlayerController.PlayState.PLAY);
    }
    
    
    @Override
    public void pausePlay() {
        if (mTxplayer != null) {
            mTxplayer.pause();
        }
    }
    
    public void resumePlay() {
        if (mTxplayer != null) {
            mTxplayer.resume();
        }
    }
    
    @Override
    protected void stopPlay() {
        if (mTxplayer != null) {
            mTxplayer.stopPlay(false);
        }
        
    }
    
    @Override
    protected boolean isPlaying() {
        if (mTxplayer != null) {
            return mTxplayer.isPlaying();
        } else {
            return false;
        }
    }
    
    
}
