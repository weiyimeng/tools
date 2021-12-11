package com.eduhdsdk.entity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eduhdsdk.tools.CircleView;
import com.eduhdsdk.tools.VolumeView;
import com.eduhdsdk.ui.AutoFitTextView;

import org.tkwebrtc.SurfaceViewRenderer;


/**
 * Created by Administrator on 2017/5/22.
 */

public class VideoItem {
    public SurfaceViewRenderer sf_video;
    public ImageView icon_gif;
    public ImageView img_mic;
    public VolumeView volume;
    public ImageView img_pen;
    public ImageView img_hand;
    public CircleView cv_draw;
    public AutoFitTextView txt_name;
    public AutoFitTextView txt_gift_num;
    public RelativeLayout rel_group;
    public ImageView img_video_back;
    public ImageView bg_video_back;
    public LinearLayout lin_gift;
    public LinearLayout parent;
    public VolumeView volumeView;
    public RelativeLayout lin_name_label;
    public LinearLayout old_lin_name_label;
    public String peerid = "";
    public int role = -1;
    public RelativeLayout rel_video_label;
    public RelativeLayout re_background;
    public RelativeLayout re_bg;
    public LinearLayout ll_mic;
    public LinearLayout ll_draw;
    public TextView tv_home;
    public View view_choose_selected;//选中框背景
    public boolean canMove = false;
    public boolean isMoved = false;
    public boolean isSefMoved = false;
    public boolean isLayoutd = false;
    public boolean isZoomd = false;
    public float oldX = 0;
    public float oldY = 0;
    public float newX = 0;
    public float newY = 0;

    public boolean isSplitScreen = false;
    public int postion;
    public int height;
    public int width;

    public String getPeerid() {
        return peerid;
    }
//    public void scale(float scale){
////        ViewHelper.setScaleX(sf_video,scale);
////        ViewHelper.setScaleY(sf_video,scale);
////        ViewHelper.setScaleX(rel_group,scale);
////        ViewHelper.setScaleY(rel_group,scale);
////        ViewHelper.setScaleX(img_video_back,scale);
////        ViewHelper.setScaleY(img_video_back,scale);
//        ViewHelper.setScaleX(parent,scale);
//        ViewHelper.setScaleY(parent,scale);
////        ViewHelper.setScaleX(lin_name_label,scale);
////        ViewHelper.setScaleY(lin_name_label,scale);
////        ViewHelper.setScaleX(rel_video_label,scale);
////        ViewHelper.setScaleY(rel_video_label,scale);
//
//    }
}
