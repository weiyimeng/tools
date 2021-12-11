package com.haoke91.a91edu.ui.liveroom;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ObjectUtils;
import com.google.gson.Gson;
import com.haoke91.a91edu.CacheData;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.entities.MediaType;
import com.haoke91.a91edu.utils.share.UMengAnalytics;
import com.haoke91.a91edu.widget.webview.APIJSTalkInterface;
import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.model.VideoUrl;
import com.haoke91.baselibrary.utils.ACallBack;
import com.haoke91.baselibrary.views.emptyview.EmptyView;
import com.haoke91.videolibrary.videoplayer.VideoPlayer;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 项目名称：91edu
 * 类描述：点播播放器
 * 创建人：weiyimeng
 * 创建时间：2018/5/8 下午12:01
 * 修改人：weiyimeng
 * 修改时间：2018/5/8 下午12:01
 * 修改备注：
 */
public class VideoActivity extends BasePlayerActivity<VideoPlayer> {
    //点播播放器
    //    private VideoPlayer play_view;
    
    private EmptyView empty_view;
    
    
    public static void start(Context context, ArrayList<VideoUrl> url, String liveName) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("liveName", liveName);
        context.startActivity(intent);
    }
    
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        empty_view.showContent();
        ArrayList<VideoUrl> url = (ArrayList<VideoUrl>) getIntent().getSerializableExtra("url");
        String liveName = getIntent().getStringExtra("liveName");
        setVideoInfo(url, liveName);
        //        DialogUtil.choice(mFl_dialogParent, new String[]{"1", "2", "4", "3"}, 0, null);
        player.setACallBack(new ACallBack<Integer>() {
            @Override
            public void call(Integer integer) {
                if (integer == ACallBack.ALIYUNVIDEO_STREAM && CacheData.isFirstEnter){
                    CacheData.isFirstEnter = false;
                    HashMap<String, String> map = new HashMap<>();
                    map.put("duration", (System.currentTimeMillis() - CacheData.CLICK_TIME) + "");
                    UMengAnalytics.INSTANCE.onEvent(VideoActivity.this, UMengAnalytics.INSTANCE.getDURATION_BEFORE_GETSTREAM_ALIYUNVIDEO(), map);
//                    Log.e("ww===","自定義時間");
                }
            }
        });
        
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_video;
    }
    
    
    @Override
    public void initialize() {
        player = findViewById(R.id.play_view);
        empty_view = findViewById(R.id.empty_view);
        mFl_dialogParent = findViewById(R.id.fl_dialogParent);
        super.initialize();
        
    }
    
    
}
