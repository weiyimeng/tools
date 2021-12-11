package com.haoke91.a91edu.ui.liveroom;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.haoke91.a91edu.CacheData;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.entities.MediaType;
import com.haoke91.a91edu.presenter.player.PlayerPresenter;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.utils.share.UMengAnalytics;
import com.haoke91.a91edu.widget.webview.APIJSTalkInterface;
import com.haoke91.a91edu.widget.webview.TalkWebView;
import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.event.RxBus;
import com.haoke91.baselibrary.model.VideoUrl;
import com.haoke91.baselibrary.utils.ACallBack;
import com.haoke91.baselibrary.utils.DensityUtil;
import com.haoke91.baselibrary.views.TipDialog;
import com.haoke91.baselibrary.views.emptyview.EmptyView;
import com.haoke91.videolibrary.VideoPlayCallbackImpl;
import com.haoke91.videolibrary.model.VodRspData;
import com.haoke91.videolibrary.videoplayer.BaseVideoPlayer;
import com.haoke91.videolibrary.videoplayer.LivePlayer;
import com.haoke91.videolibrary.videoplayer.LivePlayerController;
import com.haoke91.videolibrary.videoplayer.VideoPlayer;
import com.haoke91.videolibrary.videoplayer.VideoPlayerController;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 项目名称：91edu
 * 类描述： 兼容阿里2.0 包含了直播回放功能
 * 创建人：weiyimeng
 * 创建时间：2018/5/8 下午12:01
 * 修改人：weiyimeng
 * 修改时间：2018/5/8 下午12:01
 * 修改备注：
 */
public class VideoPlayerActivity extends BaseActivity {
    private VideoPlayer play_view;
    private LivePlayer live_view;
    private TalkWebView wb_content;
    //   private ImageView play_btn;
    // private RxBus mRxbus;
    //private ImageView iv_play_bg;
    private EmptyView empty_view;
    
    public static void start(Context context, String url) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
    
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        wb_content = findViewById(R.id.wb_content);
        //  iv_play_bg = findViewById(R.id.bg_play);
        //  iv_play_bg.setVisibility(View.GONE);
        empty_view.showLoading();
        String url = getIntent().getStringExtra("url");
        //        wb_content.loadUrl("http://192.168.0.238:9000/viewer/init/5820?appId=91haoke&userId=38261&role=student&subGroupId=1&random=f340dd7a210148e6a29d7096841af527&expire=1526692704&appSign=E720CE790F37C569A83EE19C33CB909A");
        //  wb_content.loadUrl("http://192.168.0.175:8081/app_sub.html");
        mRxBus = RxBus.getIntanceBus();
        regiestRx();
        wb_content.loadUrl(url);
        //  choicePlayer(true, null, "111");
        //  showLoadingDialog()
        
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_videoplayer;
    }
    
    @Override
    public void initialize() {
        empty_view = findViewById(R.id.empty_view);
    }
    
    
    private void regiestRx() {
        mRxBus.doSubscribe(VideoPlayerActivity.class, MessageItem.class, new Consumer<MessageItem>() {
            @Override
            public void accept(MessageItem s) throws Exception {
                Logger.e("regiestRx=====" + s);
                if (ObjectUtils.isEmpty(s)) {
                    return;
                }
                empty_view.showContent();
                switch (s.getType()) {
                    case APIJSTalkInterface.VIDEO_PLAY:
                        MediaType mediaType = new Gson().fromJson(s.getMessage(), MediaType.class);
                        if ("aliyun".equals(mediaType.getPaltformType())) {
                            List<MediaType.PcUrlsBean> pcUrls = mediaType.getPcUrls();
                            // if (!ObjectUtils.isEmpty(pcUrls) && pcUrls.size() > 0) {
                            choicePlayer(StringUtils.equals("1", mediaType.getIsLive()), mediaType.getLiveName());
                            //  choicePlayer(true, pcUrls.get(0), mediaType.getLiveName());
                            if (!ObjectUtils.isEmpty(pcUrls)) {
                                setVideoInfo(pcUrls);
                            } else {
                                String liveStatus = mediaType.getLiveStatus();
                                int status = 3;
                                if ("suspend".equalsIgnoreCase(liveStatus)) {
                                    status = BaseVideoPlayer.LIVING_STATUS_PAUSE;
                                } else if ("end".equalsIgnoreCase(liveStatus)) {
                                    status = BaseVideoPlayer.LIVING_STATUS_END;
                                } else {
                                    status = BaseVideoPlayer.LIVING_STATUS_UNSTART;
                                }
                                if (!ObjectUtils.isEmpty(play_view)) {
                                    play_view.onRestMoment(status, mediaType.getBgImg());
                                }
                                if (!ObjectUtils.isEmpty(live_view)) {
                                    live_view.onRestMoment(status, mediaType.getBgImg());
                                }
                            }
                        }
                        break;
                    case APIJSTalkInterface.SCREEN_CHANGE:
                        if (VideoPlayerActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(s.getMessage());
                        boolean isShow = jsonObject.getBoolean("isShow");
                        if (isShow) {
                            wb_content.setVisibility(View.VISIBLE);
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) wb_content.getLayoutParams();
                            layoutParams.width = (int) DensityUtil.getWidthInPx(VideoPlayerActivity.this) / 2;
                            layoutParams.height = (int) DensityUtil.getHeightInPx(VideoPlayerActivity.this);
                            layoutParams.leftMargin = (int) (DensityUtil.getWidthInPx(VideoPlayerActivity.this) * 2 / 4);
                            layoutParams.topMargin = (int) -(DensityUtil.getHeightInPx(VideoPlayerActivity.this));
                        } else {
                            wb_content.setVisibility(View.GONE);
                        }
                        break;
                    case APIJSTalkInterface.KICK_OUT:
                        kickOut(s.getMessage());
                        break;
                }
            }
        });
    }
    
    
    private void choicePlayer(boolean isLive, String liveName) {
        if (isLive) {
            live_view = findViewById(R.id.live_view);
            live_view.isLiving = false;
            live_view.setVisibility(View.VISIBLE);
            live_view.setVideoPlayCallback(mVideoPlayCallback);
            live_view.updateUI(liveName);
            live_view.findViewById(R.id.tv_resolution).setVisibility(View.GONE);
            live_view.findViewById(R.id.tv_speed).setVisibility(View.GONE);
            live_view.findViewById(R.id.tv_goldNum).setVisibility(View.GONE);
            live_view.findViewById(R.id.tv_growNum).setVisibility(View.GONE);
            live_view.findViewById(R.id.tv_video_resolution).setVisibility(View.VISIBLE);//清晰度
            live_view.setACallBack(new ACallBack<Integer>() {
                @Override
                public void call(Integer integer) {
                    if (integer == ACallBack.ALIYUNLIVE_STREAM && CacheData.isFirstEnter) {
                        CacheData.isFirstEnter = false;
                        HashMap<String, String> map = new HashMap<>();
                        map.put("type", "aliyun2.0_live");
                        map.put("duration", (System.currentTimeMillis() - CacheData.CLICK_TIME) + "");
                        UMengAnalytics.INSTANCE.onEvent(VideoPlayerActivity.this, UMengAnalytics.INSTANCE.getDURATION_BEFORE_GETSTREAM(), map);
                    }
                }
            });
        } else {
            play_view = findViewById(R.id.play_view);
            play_view.setVisibility(View.VISIBLE);
            play_view.updateUI(liveName);
            play_view.setVisibility(View.VISIBLE);
            play_view.setVideoPlayCallback(mVideoPlayCallback);
            play_view.loadVideo();
            play_view.findViewById(R.id.tv_resolution).setVisibility(View.GONE);
            play_view.findViewById(R.id.tv_speed).setVisibility(View.GONE);
            play_view.findViewById(R.id.tv_goldNum).setVisibility(View.GONE);
            play_view.findViewById(R.id.tv_growNum).setVisibility(View.GONE);
            play_view.findViewById(R.id.ll_bottomTools).setVisibility(View.VISIBLE);
            play_view.setACallBack(new ACallBack<Integer>() {
                @Override
                public void call(Integer integer) {
                    if (integer == ACallBack.ALIYUNVIDEO_STREAM && CacheData.isFirstEnter) {
                        CacheData.isFirstEnter = false;
                        HashMap<String, String> map = new HashMap<>();
                        map.put("type", "aliyun2.0_video");
                        map.put("duration", (System.currentTimeMillis() - CacheData.CLICK_TIME) + "");
                        UMengAnalytics.INSTANCE.onEvent(VideoPlayerActivity.this, UMengAnalytics.INSTANCE.getDURATION_BEFORE_GETSTREAM(), map);
                    }
                }
            });
        }
        
    }
    
    private void setVideoInfo(List<MediaType.PcUrlsBean> mediaInfos) {
        ArrayList<VideoUrl> videoUrls = new ArrayList<>();
        for (int i = 0, j = mediaInfos.size(); i < j; i++) {
            VideoUrl videoUrl = new VideoUrl();
            videoUrl.setFormatName(mediaInfos.get(i).getDefinitionText());
            videoUrl.setFormatUrl(mediaInfos.get(i).getUrl());
            videoUrls.add(videoUrl);
        }
        if (!ObjectUtils.isEmpty(play_view)) {
            play_view.setDataSource(videoUrls);
            play_view.setPlayUrl(mediaInfos.get(0).getUrl());
            play_view.setBgImageVis(View.GONE);
        }
        if (!ObjectUtils.isEmpty(live_view)) {
            live_view.setDataSource(videoUrls);
            live_view.setPlayUrl(videoUrls.get(0).getFormatUrl());
            //            player.setPlayUrl("http://5815.liveplay.myqcloud.com/live/5815_89aad37e06ff11e892905cb9018cf0d4.flv");
            //播放
            //            live_view.loadAndPlay(mediaInfos.get(0).getUrl());
            live_view.setBgImageVis(View.GONE);
            
        }
    }
    
    private VideoPlayCallbackImpl mVideoPlayCallback = new VideoPlayCallbackImpl() {
        @Override
        public void onBeginPlay() {
        }
        
        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                //    mLyTop.setVisibility(View.VISIBLE);
                if (play_view != null) {
                    play_view.setPageType(VideoPlayerController.PageType.SHRINK);
                }
                if (live_view != null) {
                    live_view.setPageType(VideoPlayerController.PageType.SHRINK);
                }
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //    mLyTop.setVisibility(View.GONE);
                if (play_view != null) {
                    play_view.setPageType(VideoPlayerController.PageType.EXPAND);
                }
                if (live_view != null) {
                    live_view.setPageType(VideoPlayerController.PageType.EXPAND);
                    
                }
            }
        }
        
        @Override
        public void onPlayFinish() {
            Logger.e("onPlayFinish");
            //iv_play_bg.setVisibility(View.VISIBLE);
            // Glide.with(VideoPlayerActivity.this).load("http://file1.teacherv.top/static/lmc/resources/images/placeholder/player_bg_start.jpg").into(iv_play_bg);
            //            if (play_view != null) {
            //                play_view.setBgImageVis(View.VISIBLE);
            //                play_view.setImageBg("http://file1.teacherv.top/static/lmc/resources/images/placeholder/player_bg_start.jpg");
            //
            //            }
            //            if (live_view != null) {
            //                live_view.setBgImageVis(View.VISIBLE);
            //                live_view.setImageBg("http://file1.teacherv.top/static/lmc/resources/images/placeholder/player_bg_start.jpg");
            //            }
        }
        
        @Override
        public void onBack() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                //   mLyTop.setVisibility(View.VISIBLE);
                if (!ObjectUtils.isEmpty(live_view)) {
                    live_view.setPageType(VideoPlayerController.PageType.SHRINK);
                }
            } else {
                finish();
            }
        }
        
        @Override
        public void onClick(View view) {
        
        }
        
        
    };
    
    @Override
    protected void onResume() {
        super.onResume();
        if (play_view != null) {
            play_view.onResume();
        }
        if (live_view != null) {
            live_view.onResume();
        }
        if (wb_content != null) {
            //  wb_content.onResume();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (play_view != null) {
            play_view.onPause();
        }
        if (live_view != null) {
            live_view.onPause();
            live_view.pausePlay(true);
        }
        if (wb_content != null) {
            //   wb_content.onPause();
        }
    }
    
    @Override
    public void onBackPressed() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (!ObjectUtils.isEmpty(play_view)) {
                //切换竖屏
                play_view.setPageType(VideoPlayerController.PageType.SHRINK);
            } else if (!ObjectUtils.isEmpty(live_view)) {
                live_view.setPageType(VideoPlayerController.PageType.SHRINK);
            }
        } else {
            super.onBackPressed();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (play_view != null) {
            play_view.onDestroy();
        }
        if (live_view != null) {
            live_view.onDestroy();
        }
        if (wb_content != null) {
            wb_content.onDestroy();
        }
        dismissLoadingDialog();
        mRxBus.unSubscribe(VideoPlayerActivity.class);
        //  VideoDataMgr.getInstance().setGetVideoInfoListListener(null);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        View view = null;
        if (!ObjectUtils.isEmpty(play_view)) {
            view = play_view;
        } else if (!ObjectUtils.isEmpty(live_view)) {
            view = live_view;
        }
        //        if (ObjectUtils.isEmpty(view)) {
        //            return;
        //        }
        /***
         * 根据屏幕方向重新设置播放器的大小
         */
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().invalidate();
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.getHeightInPx(this);
            if (!ObjectUtils.isEmpty(view)) {
                view.getLayoutParams().height = (int) height;
                view.getLayoutParams().width = (int) width;
            }
            //            if (!ObjectUtils.isEmpty(iv_play_bg)) {
            //                iv_play_bg.getLayoutParams().height = (int) height;
            //                iv_play_bg.getLayoutParams().width = (int) width;
            //            }
            if (!ObjectUtils.isEmpty(wb_content)) {
                wb_content.setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) wb_content.getLayoutParams();
                layoutParams.width = (int) DensityUtil.getWidthInPx(VideoPlayerActivity.this) / 2;
                //   layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
                layoutParams.height = (int) DensityUtil.getHeightInPx(VideoPlayerActivity.this);
                layoutParams.leftMargin = (int) (DensityUtil.getWidthInPx(VideoPlayerActivity.this) * 2 / 4);
                layoutParams.topMargin = (int) -(DensityUtil.getHeightInPx(VideoPlayerActivity.this));
                wb_content.loadUrl("javascript:screenOrientationChange('" + "H" + "')");
            }
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //    BarUtils.setStatusBarColor(this, Color.parseColor("#0000"));
            
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200.f);
            if (!ObjectUtils.isEmpty(view)) {
                view.getLayoutParams().height = (int) height;
                view.getLayoutParams().width = (int) width;
            }
            //            if (!ObjectUtils.isEmpty(iv_play_bg)) {
            //                iv_play_bg.getLayoutParams().height = (int) height;
            //                iv_play_bg.getLayoutParams().width = (int) width;
            //            }
            //            view.getLayoutParams().height = (int) height;
            //            view.getLayoutParams().width = (int) width;
            if (!ObjectUtils.isEmpty(wb_content)) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) wb_content.getLayoutParams();
                layoutParams.topMargin = 0;
                layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                layoutParams.leftMargin = 0;
                wb_content.setVisibility(View.VISIBLE);
                wb_content.loadUrl("javascript:screenOrientationChange('" + "S" + "')");
                
            }
            
        }
    }
    
    
    private void kickOut(String message) {
        if (isDestroyed()) {
            return;
        }
        TipDialog dialog = new TipDialog(this);
        dialog.setTextDes(message);
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
        if (!ObjectUtils.isEmpty(wb_content)) {
            ViewParent parent = wb_content.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(wb_content);
            }
            
            wb_content.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            wb_content.getSettings().setJavaScriptEnabled(false);
            wb_content.removeAllViews();
            wb_content.onDestroy();
            
            // wb_content.onDestroy();
        }
        if (play_view != null) {
            play_view.onDestroy();
        }
        if (live_view != null) {
            live_view.onDestroy();
        }
    }
    
    private void goBack() {
        Logger.e("尝试关闭activity中。。。");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 600);
    }
    
}
