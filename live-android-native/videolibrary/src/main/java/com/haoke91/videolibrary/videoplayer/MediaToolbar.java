package com.haoke91.videolibrary.videoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoke91.videolibrary.R;

/**
 * Created by Ted on 2015/8/4.
 * MediaController
 */
public class MediaToolbar extends FrameLayout implements View.OnClickListener {
    
    //private ImageView mDanmukuBtn;
    private boolean mDanmukuOn;
    private MediaToolbarImpl mMediaToolbarImpl;
    private ImageView mMoreBtn;
    public TextView tvResolution, tvSpeed;
    //private ImageView mSnapshotBtn;
    private TextView mTitle;
    private TextView mtv_goldNum, mtv_growNum;
    
    
    public MediaToolbar(Context context) {
        super(context);
        initView(context);
    }
    
    public MediaToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    
    public MediaToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    
    private void initView(Context context) {
        View.inflate(context, R.layout.biz_video_media_toolbar, this);
        
        //  mDanmukuBtn = findViewById(R.id.danmuku);
        mMoreBtn = findViewById(R.id.menu_more);
        ImageView mBackBtn = findViewById(R.id.back_pl);
        mtv_goldNum = findViewById(R.id.tv_goldNum);
        mtv_growNum = findViewById(R.id.tv_growNum);
        tvResolution = findViewById(R.id.tv_resolution);
        tvSpeed = findViewById(R.id.tv_speed);
        //        mtv_onlineNum = findViewById(R.id.tv_onLineNum);
        //   mSnapshotBtn = findViewById(R.id.snapshoot);
        mTitle = findViewById(R.id.title);
        // mDanmukuBtn.setOnClickListener(this);
        mMoreBtn.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        tvResolution.setOnClickListener(this);
        tvSpeed.setOnClickListener(this);
        //    mSnapshotBtn.setOnClickListener(this);
        initData();
    }
    
    @Override
    public void onClick(View view) {
        //        if (view.getId() == R.id.danmuku) {
        //            mDanmukuOn = !mDanmukuOn;
        //            if (mDanmukuOn) {
        //                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_danmuku_on);
        //              //  mDanmukuBtn.setImageBitmap(bmp);
        //            } else {
        //                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_danmuku_off);
        //              //  mDanmukuBtn.setImageBitmap(bmp);
        //            }
        //            mMediaToolbarImpl.onDanmaku(mDanmukuOn);
        //        } else
        //        if (view.getId() == R.id.snapshoot) {
        //            mMediaToolbarImpl.onSnapshoot();
        //        } else
        if (view.getId() == R.id.menu_more) {
            mMediaToolbarImpl.onMoreSetting();
        } else if (view.getId() == R.id.back_pl) {
            mMediaToolbarImpl.onBack();
        } else {//追加的点击事件
            mMediaToolbarImpl.onclick(view);
        }
    }
    
    
    public void setMediaControl(MediaToolbarImpl mediaControl) {
        mMediaToolbarImpl = mediaControl;
    }
    
    private void initData() {
    
    }
    
    public void updateTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }
    
    /**
     * 更新右上角内容
     *
     * @param gold
     * @param grow
     * @param peoples
     */
    public void updateNums(String gold, String grow, String peoples) {
        if (mtv_goldNum != null && mtv_growNum != null) {
            mtv_goldNum.setText(gold);
            mtv_growNum.setText(grow);
            //            mtv_onlineNum.setText(peoples);
        }
    }
    
    public void setMoreViewVis(int vis) {
        mMoreBtn.setVisibility(vis);
    }
    
    
    public interface MediaToolbarImpl {
        
        void onMoreSetting();
        
        void onBack();
        
        /**
         * 点击事件
         *
         * @param view
         */
        void onclick(View view);
    }
    
}
