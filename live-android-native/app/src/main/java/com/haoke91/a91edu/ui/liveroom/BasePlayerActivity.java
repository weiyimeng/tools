package com.haoke91.a91edu.ui.liveroom;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haoke91.a91edu.CacheData;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.presenter.player.PlayerPresenter;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.ui.liveroom.chat.LiveDanmuView;
import com.haoke91.a91edu.view.PlayerView;
import com.haoke91.a91edu.widget.dialog.DialogUtil;
import com.haoke91.baselibrary.model.VideoUrl;
import com.haoke91.baselibrary.utils.ACallBack;
import com.haoke91.baselibrary.utils.ICallBack;
import com.haoke91.baselibrary.views.TipDialog;
import com.haoke91.im.mqtt.IMManager;
import com.haoke91.im.mqtt.LogU;
import com.haoke91.im.mqtt.entities.Message;
import com.haoke91.im.mqtt.entities.Prop;
import com.haoke91.im.mqtt.entities.User;
import com.haoke91.videolibrary.VideoPlayCallbackImpl;
import com.haoke91.videolibrary.videoplayer.BaseVideoPlayer;
import com.haoke91.videolibrary.videoplayer.VideoPlayerController;


import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/6/19 上午10:24
 * 修改人：weiyimeng
 * 修改时间：2018/6/19 上午10:24
 * 修改备注：
 */
public abstract class BasePlayerActivity<T extends BaseVideoPlayer> extends BaseActivity implements PlayerView {
    protected T player;
    protected LiveDanmuView mDanmuView;
    protected FrameLayout mFl_dialogParent;
    protected PlayerPresenter mPlayerPresenter;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    
    @Override
    public void initialize() {
        //        hideNavitionView();
        mDanmuView = findViewById(R.id.view_danmaku);
        mFl_dialogParent = findViewById(R.id.fl_dialogParent);
        player.setVideoPlayCallback(mVideoPlayCallback);
        player.setPageType(VideoPlayerController.PageType.EXPAND);
        mPlayerPresenter = new PlayerPresenter(this);
        getLifecycle().addObserver(mPlayerPresenter);
        //        getLifecycle().addObserver(player);
        
        mPlayerPresenter.connect();
        //连接成功后 onconnected()登录
        mPlayerPresenter.fillBoardView(mFl_dialogParent);
        //        DialogUtil.getInstance().showTimer(mFl_dialogParent, 60, new ACallBack<String>() {
        //        });
        //直播隐藏倍速
        if (CacheData.isLivingPlay) {
            player.findViewById(R.id.tv_speed).setVisibility(View.GONE);
        } else {
            player.findViewById(R.id.tv_speed).setVisibility(View.VISIBLE);
        }
    }
    
    protected void hideNavitionView() {
        //隐藏虚拟按键，并且全屏
        View decorView = getWindow().getDecorView();
        if (decorView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            decorView.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    
    private VideoPlayCallbackImpl mVideoPlayCallback = new VideoPlayCallbackImpl() {
        @Override
        public void onBeginPlay() {
        
        }
        
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tv_resolution) {
                if (!player.isEnablePlaying) {
                    return;
                }
                DialogUtil.getInstance().showRightList(BasePlayerActivity.this, view, player.getDataSource(), new ICallBack<VideoUrl, String>() {
                    @Override
                    public String onPrev(VideoUrl videoUrl) {
                        return videoUrl.getFormatName();
                    }
                    
                    @Override
                    public void call(VideoUrl videoUrl, int p) {
                        player.setPlayUrl(videoUrl.getFormatUrl());
                    }
                });
            } else if (view.getId() == R.id.tv_speed) {
                if (!player.isEnablePlaying) {
                    return;
                }
                DialogUtil.getInstance().showRightList(BasePlayerActivity.this, view, Arrays.asList("0.8", "1.0", "1.25", "1.5", "2.0"), new ICallBack<String, String>() {
                    @Override
                    public String onPrev(String s) {
                        return s;
                    }
                    
                    @Override
                    public void call(String s, int p) {
                        //                        ToastUtils.showShort(s);
                        player.changeRate(Float.parseFloat(s));
                    }
                });
            }
            
        }
        
        @Override
        public void onPlayFinish() {
        }
        
        @Override
        public void onBack() {
            onBackPressed();
        }
        
        @Override
        public void onSwitchPageType() {
        
        }
        
    };
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (mDanmuView != null && mDanmuView.isPrepared() && mDanmuView.isPaused()) {
            mDanmuView.resume();
        }
        if (player != null && player.isEnablePlaying) {
            mPlayerPresenter.onResume();
        }
        if (player != null && player.isEnablePlaying) {
            player.onResume();
        }
        player.showOrHideController(this);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        if (mDanmuView != null && mDanmuView.isPrepared()) {
            mDanmuView.pause();
        }
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        mPlayerPresenter.onStop();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDanmuView != null) {
            mDanmuView.release();
            mDanmuView = null;
        }
        if (player != null) {
            player.onDestroy();
        }
        dismissLoadingDialog();
        mPlayerPresenter.onDestory();
    }
    
    
    protected void setVideoInfo(ArrayList<VideoUrl> videoUrls, String liveName) {
        if (ObjectUtils.isEmpty(videoUrls)) {
            // TODO: 2018/12/10
            return;
        }
        if (!ObjectUtils.isEmpty(player)) {
            player.setDataSource(videoUrls);
            player.setPlayUrl(videoUrls.get(0).getFormatUrl());
            player.mMediaToolbar.tvResolution.setText(videoUrls.get(0).getFormatName());//设置正在播放的视频清晰度
            //            player.setPlayUrl("http://5815.liveplay.myqcloud.com/live/5815_89aad37e06ff11e892905cb9018cf0d4.flv");
            player.updateUI(liveName);
        }
    }
    
    @Override
    public void onConnected(int code) {
        if (code == 0) {
            //连接成功
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPlayerPresenter.login();
                    Prop prop = IMManager.Companion.getInstance().getSessionUser().getProp();
                    player.updateNums(prop.getPartGold() + "", prop.getPartProgress() + "", "");
                }
            });
            mPlayerPresenter.onLoginSuccess(0);//加载历史
            mPlayerPresenter.onLineTime();
        }
    }
    
    
    @Override
    public void onUserLogin(User user) {
    
    }
    
    @Override
    public void onKickOut() {
        if (isDestroyed()) {
            return;
        }
        TipDialog dialog = new TipDialog(this);
        dialog.setTextDes("您已被踢出房间");
        dialog.setButton1(getString(R.string.action_ok), new TipDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, TipDialog dialog) {
                finish();
            }
        });
        dialog.setCancleGone();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setWidth(Math.max(getResources().getDisplayMetrics().heightPixels / 2, getResources().getDisplayMetrics().widthPixels / 2));
        mPlayerPresenter.onDestory();
        if (player != null) {
            player.onDestroy();
        }
    }
    
    @Override
    public void onUserLogout(User user) {
    
    }
    
    @Override
    public void onTextMessage(Message msg) {
    
    }
    
    @Override
    public void onGetHistory(List<Message> messages) {
    
    }
    
    @Override
    public void onCustomMessage(Message msg) {
    }
    
    @Override
    public void onChangeUserProp(int gold, int progress) {
        if (player != null) {
            player.updateUI(PlayerPresenter.Companion.getTopicName());
            player.updateNums(String.valueOf(gold), String.valueOf(progress), "");
        }
    }
    
    @Override
    public void onBarrageStatus(String status) {
        if ("on".equalsIgnoreCase(status)) {
            if (mDanmuView != null) {
                mDanmuView.toggle(true);
            }
            //            LogU.log("弹幕开启");
        } else {
            if (mDanmuView != null) {
                mDanmuView.toggle(false);
            }
            //            LogU.log("弹幕关闭");
        }
    }
    
    @Override
    public void onLiveStatus(String status) {
        //        Log.e("onlivestatus===", status == null ? "" : status);
        if (ObjectUtils.isEmpty(status)) {
            if (!ObjectUtils.isEmpty(player)) {
                player.onRestMoment(BaseVideoPlayer.LIVING_STATUS_UNSTART, "");
            }
            return;
        }
        player.enablePlay("start".equalsIgnoreCase(status));
        switch (status) {
            case "start":
                if (!ObjectUtils.isEmpty(player)) {
                    player.setBgImageVis(View.GONE);
                    player.onResume();
                }
                break;
            case "suspend":
                if (!ObjectUtils.isEmpty(player)) {
                    player.onRestMoment(BaseVideoPlayer.LIVING_STATUS_PAUSE, "");
                }
                break;
            case "end":
                if (!ObjectUtils.isEmpty(player)) {
                    player.onRestMoment(BaseVideoPlayer.LIVING_STATUS_END, "");
                }
                break;
            default:
                if (!ObjectUtils.isEmpty(player)) {
                    player.onRestMoment(BaseVideoPlayer.LIVING_STATUS_UNSTART, "");
                }
                break;
        }
        if ("start".equalsIgnoreCase(status)) {
            player.onResume();
        } else {
            player.onPause();
        }
    }
    
    private long mFirstPressedTime;
    
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mFirstPressedTime < 2000) {
            super.onBackPressed();
        } else {
            ToastUtils.showShort("再按一次退出直播间");
            mFirstPressedTime = System.currentTimeMillis();
        }
    }
    
    @Override
    public void onForbidden(boolean isForbidden) {
    
    }
    
    @Override
    public void onWithdrewText(String id) {
    
    }
    
    @Override
    public boolean onEnablePlayAudio() {
        return !player.isEnablePlaying;
    }
}
