package com.haoke91.a91edu;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gaosiedu.live.sdk.android.api.common.dictionary.list.LiveDictionaryListRequest;
import com.gaosiedu.live.sdk.android.api.common.dictionary.list.LiveDictionaryListResponse;
import com.gaosiedu.live.sdk.android.api.user.profile.LiveUserProfileRequest;
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain;
import com.google.gson.Gson;
import com.haoke91.a91edu.entities.LiveUserProfileResponse;
import com.haoke91.a91edu.adapter.TabAdapter;
import com.haoke91.a91edu.fragment.main.StudyFragment;
import com.haoke91.a91edu.fragment.main.FoundFragment;
import com.haoke91.a91edu.fragment.main.HomeFragment;
import com.haoke91.a91edu.fragment.main.MineFragment;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.presenter.update.OkGoUpdateUtil;
import com.haoke91.a91edu.presenter.update.UpdateAppManager;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.ui.liveroom.AgoraActivity;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.utils.share.UMengAnalytics;
import com.haoke91.a91edu.widget.BottomNavigationViewEx;
import com.haoke91.a91edu.widget.dialog.DialogUtil;
import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.event.RxBus;
import com.lzy.okgo.cache.CacheMode;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.AnalyticsConfig;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.zzhoujay.richtext.RichText;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 项目名称：91edu
 * 类描述：
 *
 * @author ：
 *         创建时间：2018/5/9 上午9:22
 *         修改时间：2018/5/9 上午9:22
 *         修改备注：
 */
public class MainActivity extends BaseActivity {
    private ViewPager mvp_content;
    private BottomNavigationViewEx navigation;
    private BottomNavigationViewEx.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {
        
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mvp_content.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    mvp_content.setCurrentItem(1);
                    return true;
                //                case R.id.navigation_notifications:
                //                    mvp_content.setCurrentItem(2);
                //                    return true;
                case R.id.navigation_mine:
                    mvp_content.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };
    
    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }
    
    
    @Override
    public void initialize() {
        mvp_content = findViewById(R.id.vp_content);
        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new StudyFragment());
        //        mFragments.add(new FoundFragment());
        mFragments.add(new MineFragment());
        navigation = findViewById(R.id.navigation);
        navigation.setItemIconTintList(null);
        mvp_content.setOffscreenPageLimit(4);
        mvp_content.setAdapter(new TabAdapter(getSupportFragmentManager(), mFragments));
        navigation.enableAnimation(true);
        navigation.enableItemShiftingMode(false);
        int[] colors = new int[]{getResources().getColor(R.color.color_75C82B), getResources().getColor(R.color.color_363636)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_checked};
        states[1] = new int[]{-android.R.attr.state_checked};
        ColorStateList aa = new ColorStateList(states, colors);
        navigation.setItemTextColor(aa);
        navigation.enableShiftingMode(false);
        navigation.setupWithViewPager(mvp_content, true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (UserManager.getInstance().isLogin()) {
            updateUserInfo();
        }
        getHolidayInfo();
    }
    
    // 学期
    private void getHolidayInfo() {
        LiveDictionaryListRequest request = new LiveDictionaryListRequest();
        request.setDicType("term");
        
        CacheMode mode = ObjectUtils.isEmpty(UserManager.getInstance().getWelTermInfo()) ? CacheMode.NO_CACHE : CacheMode.IF_NONE_CACHE_REQUEST;
        Api.getInstance().post(request, LiveDictionaryListResponse.class, new ResponseCallback<LiveDictionaryListResponse>() {
                @Override
                public void onResponse(LiveDictionaryListResponse date, boolean isFromCache) {
                    if (!isFromCache) {
                        UserManager.getInstance().saveTermInfo((ArrayList<DictionaryDomain>) date.getData().getList());
                    }
                    //                    CacheDiskUtils.getInstance().put(GlobalConfig.COURSE_LIST, date);
                }
                
                @Override
                public void onError() {
                }
            }, mode, ConvertUtils.timeSpan2Millis(30, TimeConstants.DAY)
            , "");
    }
    
    private void updateUserInfo() {
        LiveUserProfileRequest request = new LiveUserProfileRequest();
        request.setUserId(UserManager.getInstance().getUserId());
        Api.getInstance().post(request, LiveUserProfileResponse.class, new ResponseCallback<LiveUserProfileResponse>() {
            @Override
            public void onResponse(LiveUserProfileResponse date, boolean isFromCache) {
                if (!ObjectUtils.isEmpty(date.getData())) {
                    UserManager.getInstance().saveUserInfo(date.getData());
                }
            }
        }, "");
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
        checkAppUpdate();
    }
    
    /**
     * 权限申请
     */
    private void requestPermission() {
        
        AndPermission.with(this)
            .runtime()
            .permission(Permission.Group.STORAGE, Permission.Group.CAMERA, Permission.Group.MICROPHONE)
            .rationale(new Rationale<List<String>>() {
                @Override
                public void showRationale(Context context, List<String> data, final RequestExecutor executor) {
                    executor.execute();
                    
                    // When the user interrupts the request:
                    //executor.cancel();
                    //                    new AlertDialog.Builder()
                    //                    new AlertDialog.Builder(MainActivity.this)
                    //                        .setMessage("请授权使用手机存储、照相、麦克风等权限")
                    //                        .setTitle("权限申请")
                    //                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    //                            @Override
                    //                            public void onClick(DialogInterface dialog, int which) {
                    //
                    //                            }
                    //                        }).create().show();
                }
            })
            .onGranted(new Action<List<String>>() {
                @Override
                public void onAction(List<String> data) {
                    //已授权
                }
            })
            .onDenied(new Action<List<String>>() {
                @Override
                public void onAction(List<String> data) {
                    if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, data)) {
                        // These permissions are always rejected by the user.
                        ToastUtils.showShort(getString(R.string.permission_apply));
                        ToastUtils.showShort(getString(R.string.permission_apply));
                        Uri packageURI = Uri.parse("package:" + MainActivity.this.getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        startActivity(intent);
                    } else {
                        requestPermission();
                    }
                }
            })
            .start();
    }
    
    @Override
    protected void registeredEvent() {
        mRxBus = RxBus.getIntanceBus();
        mRxBus.doSubscribe(MainActivity.class, MessageItem.class, new Consumer<MessageItem>() {
            @Override
            public void accept(MessageItem messageItem) throws Exception {
                if (ObjectUtils.isEmpty(messageItem)) {
                    return;
                }
                if (messageItem.getType() == MessageItem.change_tab) {
                    navigation.setCurrentItem(Integer.parseInt(messageItem.getMessage()));
                }
            }
        });
        
    }
    
    private void checkAppUpdate() {
        new UpdateAppManager.Builder()
            .setActivity(this)
            .setUpdateUrl("https://file-cdn.91haoke.com/app/android/app.json")
            //            .setOnlyWifi()
            .setHttpManager(new OkGoUpdateUtil())
            .build()
            .update();
    }
    
    private long mFirstPressedTime;
    
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mFirstPressedTime < 2000) {
            super.onBackPressed();
        } else {
            ToastUtils.showShort("再按一次退出应用");
            mFirstPressedTime = System.currentTimeMillis();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.recycle();
        if (mRxBus != null) {
            mRxBus.unSubscribe(MainActivity.class);
        }
        
    }
    
    
}
