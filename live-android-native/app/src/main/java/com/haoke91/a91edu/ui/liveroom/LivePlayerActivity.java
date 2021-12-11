package com.haoke91.a91edu.ui.liveroom;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.haoke91.a91edu.CacheData;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.entities.player.Answer;
import com.haoke91.a91edu.utils.share.UMengAnalytics;
import com.haoke91.a91edu.widget.dialog.DialogUtil;
import com.haoke91.baselibrary.model.VideoUrl;
import com.haoke91.baselibrary.utils.ACallBack;
import com.haoke91.baselibrary.views.emptyview.EmptyView;
import com.haoke91.videolibrary.videoplayer.LivePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：91edu
 * 类描述：直播播放器
 * 创建人：weiyimeng
 * 创建时间：2018/5/8 下午12:01
 * 修改人：weiyimeng
 * 修改时间：2018/5/8 下午12:01
 * 修改备注：
 */
public class LivePlayerActivity extends BaseChatActivity<LivePlayer> {
    //点播播放器
    //    private VideoPlayer play_view;
    
    private EmptyView empty_view;
    
    
    public static void start(Context context, ArrayList<VideoUrl> url, String liveStatus, String liveName) {
        Intent intent = new Intent(context, LivePlayerActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("liveName", liveName);
        intent.putExtra("status", liveStatus);
        context.startActivity(intent);
    }
    
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        empty_view.showContent();
        ArrayList<VideoUrl> url = (ArrayList<VideoUrl>) getIntent().getSerializableExtra("url");
        String liveName = getIntent().getStringExtra("liveName");
        
        //        player.setControlEnable(false);
        setVideoInfo(url, liveName);
        UMengAnalytics.INSTANCE.onEvent(this, UMengAnalytics.INSTANCE.getLook_live_success());
        player.setACallBack(new ACallBack<Integer>() {
            @Override
            public void call(Integer integer) {
                if (integer == ACallBack.ALIYUNLIVE_STREAM && CacheData.isFirstEnter){
                    CacheData.isFirstEnter = false;
                    HashMap<String, String> map = new HashMap<>();
                    map.put("duration", (System.currentTimeMillis() - CacheData.CLICK_TIME) + "");
                    UMengAnalytics.INSTANCE.onEvent(LivePlayerActivity.this, UMengAnalytics.INSTANCE.getDURATION_BEFORE_GETSTREAM_ALIYUNLIVE(), map);
                }
            }
        });
        //        onLiveStatus("suspend");
//        Answer answer=new Answer();
//        answer.setChoose("A");
//        answer.setChooseContent("YES");
//        Answer answer2=new Answer();
//        answer2.setChoose("B");
//        answer2.setChooseContent("NO");
//        ArrayList<Answer> list=new ArrayList<>();
//        list.add(answer);
//        list.add(answer2);
//        DialogUtil.getInstance().choice(mFl_dialogParent, list,200,new ACallBack<String>(){
//            @Override
//            public void call(String s) {
//
//            }
//        });
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && getIntent().hasExtra("status")){
            String status = getIntent().getStringExtra("status");
            onLiveStatus(status);
            getIntent().removeExtra("status");
        }
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_liveplayer;
    }
    
    
    @Override
    public void initialize() {
        player = findViewById(R.id.play_view);
        empty_view = findViewById(R.id.empty_view);
        mFl_dialogParent = findViewById(R.id.fl_dialogParent);
        super.initialize();
        
    }
}
