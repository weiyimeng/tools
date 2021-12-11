package com.haoke91.a91edu.ui.liveroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.blankj.utilcode.util.ToastUtils;
import com.haoke91.a91edu.CacheData;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.utils.share.UMengAnalytics;
import com.haoke91.a91edu.widget.dialog.DialogUtil;
import com.haoke91.baselibrary.utils.ACallBack;
import com.haoke91.videolibrary.videoplayer.AgoraPlayer;
import com.haoke91.videolibrary.videoplayer.AudienceListView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;


/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/6/15 下午12:12
 * 修改人：weiyimeng
 * 修改时间：2018/6/15 下午12:12
 * 修改备注：声网测试
 */
public class AgoraActivity extends BaseChatActivity<AgoraPlayer> {
    
    public static void start(Context context, String liveStatus, String token, String appId, String channel, int uId) {
        Intent intent = new Intent(context, AgoraActivity.class);
        intent.putExtra("status", liveStatus);
        intent.putExtra("token", token);
        intent.putExtra("appId", appId);
        intent.putExtra("channel", channel);
        intent.putExtra("uId", uId);
        context.startActivity(intent);
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        player.setACallBack(new ACallBack<Integer>() {
            @Override
            public void call(Integer integer) {
                if (integer == ACallBack.AGORA_STREAM && CacheData.isFirstEnter) {
                    CacheData.isFirstEnter = false;
                    HashMap<String, String> map = new HashMap<>();
                    map.put("duration", (System.currentTimeMillis() - CacheData.CLICK_TIME) + "");
                    UMengAnalytics.INSTANCE.onEvent(AgoraActivity.this, UMengAnalytics.INSTANCE.getDURATION_BEFORE_GETSTREAM_AGORA(), map);
                }
            }
        });
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_agora;
    }
    
    @Override
    public void initialize() {
        AudienceListView view_audience_list = findViewById(R.id.view_audience_list);
        player = findViewById(R.id.view_play);
        player.setListView(view_audience_list);
        mFl_dialogParent = findViewById(R.id.fl_dialogParent);
        view_audience_list.setMediaControl(player.getMediaControl());
        //        showList();
        super.initialize();
        Intent intent = getIntent();
        
        player.create(intent.getStringExtra("token"), intent.getStringExtra("appId"), intent.getStringExtra("channel"), intent.getIntExtra("uId", 0));
        //        onLiveStatus(intent.getStringExtra("status"));
    }
    
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && getIntent().hasExtra("status")) {
            String status = getIntent().getStringExtra("status");
            onLiveStatus(status);
            getIntent().removeExtra("status");
            player.enablePlay("start".equalsIgnoreCase(status));
        }
        
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
    }
    
    
}
