package com.haoke91.videolibrary.videoplayer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.haoke91.baselibrary.model.VideoUrl;

import java.util.ArrayList;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/6/13 下午2:47
 * 修改人：weiyimeng
 * 修改时间：2018/6/13 下午2:47
 * 修改备注：
 */
public abstract class MediaControlImpl extends FrameLayout {
    private ArrayList<VideoUrl> source;
    
    public MediaControlImpl(Context context) {
        super(context);
    }
    
    public MediaControlImpl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public MediaControlImpl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public abstract void setPlayState(VideoPlayerController.PlayState playState);
    
    public abstract void setMediaControl(VideoPlayerController.MediaControlImpl mediaControl);
    
    public abstract void updateUI();
    
    public void updateResolutionTxt(String info) {
    }
    
    public void setPageType(VideoPlayerController.PageType pageType) {
    }
    
    public void playFinish(int allTime) {
    }
    
    public ArrayList<VideoUrl> getDataSource() {
        return source == null ? new ArrayList<VideoUrl>() : source;
    }
    
    public void setDataSource(ArrayList<VideoUrl> source) {
        this.source = source;
    }
}
