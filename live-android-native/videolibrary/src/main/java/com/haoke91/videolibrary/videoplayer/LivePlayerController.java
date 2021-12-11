package com.haoke91.videolibrary.videoplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.haoke91.baselibrary.model.VideoUrl;
import com.haoke91.baselibrary.views.popwindow.CommentPopup;
import com.haoke91.baselibrary.views.popwindow.ListPopup;
import com.haoke91.baselibrary.views.popwindow.XGravity;
import com.haoke91.baselibrary.views.popwindow.YGravity;
import com.haoke91.videolibrary.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ted on 2015/8/4.
 * MediaController
 */
public class LivePlayerController extends MediaControlImpl implements View.OnClickListener {
    private ImageView iv_prise;//播放按钮
    // private SeekBar mProgressSeekBar;//播放进度条
    // private TextView mTimeTxt1, mTimeTxt2;//播放时间
    private ImageView mExpandImg;//最大化播放按钮
    private TextView mResolutionTxt;//清晰度
    private TextView tv_input, tv_only_teacher, mtv_danmu;
    private VideoPlayerController.MediaControlImpl mMediaControl;
    private CommentPopup mCommentPopup;
    private ImageView iv_gift;
    private ListPopup resolutionPop;
    
    public LivePlayerController(Context context) {
        super(context);
        initView(context);
    }
    
    public LivePlayerController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    
    @Override
    public void setPlayState(VideoPlayerController.PlayState playState) {
    
    }
    
    
    public LivePlayerController(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    
    private void initView(Context context) {
        View.inflate(context, R.layout.live_media_controller, this);
        tv_only_teacher = findViewById(R.id.tv_only_teacher);
        mtv_danmu = findViewById(R.id.tv_video_danmu);
        iv_prise = findViewById(R.id.iv_prise);
        mExpandImg = findViewById(R.id.expand);
        tv_input = findViewById(R.id.tv_input);
        iv_gift = findViewById(R.id.iv_gift);
        mResolutionTxt = findViewById(R.id.tv_video_resolution);
        initData();
        com.haoke91.baselibrary.utils.KeyboardUtils.registerSoftInputChangedListener((Activity) getContext(), new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height < 100) {
                    if (!ObjectUtils.isEmpty(mCommentPopup) && commentDismiss) {
                        mCommentPopup.dismiss();
                    }
                } else if (!ObjectUtils.isEmpty(mCommentPopup)) {
                    mCommentPopup.setEmojiLayoutGone();
                    commentDismiss = true;
                }
            }
        });
    }
    
    @Override
    public void onClick(View view) {
        mMediaControl.removeHandlerMessage();
        mMediaControl.sendHandlerMessage(BaseVideoPlayer.TIME_SHOW_CONTROLLER);
        if (view.getId() == R.id.pause) {
            mMediaControl.onPlayTurn();
        } else if (view.getId() == R.id.tv_video_resolution) {
            //                        mMediaControl.onResolutionChange();
            
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
            
            
        } else if (view.getId() == R.id.tv_input) {
            ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            if (ObjectUtils.isEmpty(mCommentPopup)) {
                mCommentPopup = CommentPopup.create(getContext())
                    .setFocusAndOutsideEnable(false)
                    .setOutsideTouchable(false)
                    .setBackgroundDimEnable(false)
                    .setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            mCommentPopup.setEmojiLayoutGone();
                            if (KeyboardUtils.isSoftInputVisible((Activity) getContext())) {
                                KeyboardUtils.toggleSoftInput();
                            }
                        }
                    })
                    //                    .setChangeTextListener(new OnClickListener() {
                    //                        @Override
                    //                        public void onClick(View v) {
                    //                            Logger.e("setChangeTextListener===");
                    //
                    //                        }
                    //                    })
                    .setOnActionClickListener(new CommentPopup.OnSendMessageListener() {
                        @Override
                        public void OnSendMessage(String message) {
                            mCommentPopup.dismiss();
                            tv_input.setText("");
                            mMediaControl.sendMessage(message);
                        }
                        
                        @Override
                        public void onChangeLayout(int type) {
                            commentDismiss = false;
                        }
                    })
                    .apply();
            }
            mCommentPopup
                .showSoftInput()
                .showAtLocation(view, Gravity.BOTTOM, 0, 0);
            // KeyboardUtils.showSoftInput(view);
        } else if (view.getId() == R.id.iv_gift) {
            //            ChoiceGiftDialog.showDialog((AppCompatActivity) getContext());
            mMediaControl.onFlower();
        } else if (view.getId() == R.id.tv_only_teacher) {
            tv_only_teacher.setSelected(!tv_only_teacher.isSelected());
            mMediaControl.onlySeeTeacher(tv_only_teacher.isSelected());
            
        } else if (view.getId() == R.id.iv_prise) {//点赞
            mMediaControl.onPraise();
        } else if (view.getId() == R.id.tv_video_danmu) {
            mMediaControl.onDanmaku();
            BaseVideoPlayer.isOpenDanMu = !BaseVideoPlayer.isOpenDanMu;
            //            mtv_danmu.setSelected(BaseVideoPlayer.isOpenDanMu);
            if (BaseVideoPlayer.isOpenDanMu) {
                mtv_danmu.setText("横屏");
                BaseVideoPlayer.isOpenDanMu = true;
            } else {
                mtv_danmu.setText("竖屏");
                BaseVideoPlayer.isOpenDanMu = false;
                
            }
            
        } else if (view.getId() == R.id.expand) {
            mMediaControl.onDanmaku();
        }
    }
    
    private boolean commentDismiss = true;
    
    public void setMediaControl(VideoPlayerController.MediaControlImpl mediaControl) {
        mMediaControl = mediaControl;
    }
    
    
    @Override
    public void setPageType(VideoPlayerController.PageType pageType) {
        if (pageType == VideoPlayerController.PageType.EXPAND) {
            mtv_danmu.setText("横屏");
        } else {
            mtv_danmu.setText("竖屏");
            
        }
        
    }
    
    private void initData() {
        //  mProgressSeekBar.setOnSeekBarChangeListener(this);
        tv_only_teacher.setOnClickListener(this);
        iv_prise.setOnClickListener(this);
        mResolutionTxt.setOnClickListener(this);
        //        mExpandImg.setOnClickListener(this);
        tv_input.setOnClickListener(this);
        iv_gift.setOnClickListener(this);
        mtv_danmu.setOnClickListener(this);
        mExpandImg.setOnClickListener(this);
        
    }
    
    @SuppressLint("SimpleDateFormat")
    private String formatPlayTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }
    
    public void setPlayUrl(String vodUrl) {
        String mScanVodUrl = vodUrl;
    }
    
    public void updateUI() {
    }
    
    
    public void updateResolutionTxt(String info) {
        mResolutionTxt.setText(info);
        //        }
    }
    
    /**
     * 设置清晰度是否可见
     *
     * @param visible
     */
    public void setResolutionVisible(int visible) {
        if (mResolutionTxt != null) {
            mResolutionTxt.setVisibility(visible);
        }
    }
    
    /**
     * 播放状态 播放 暂停
     */
    public enum PlayState {
        PLAY, PAUSE
    }
    
}

