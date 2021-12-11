package com.haoke91.a91edu.ui;

import android.content.Intent;
import android.view.View;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.gaosiedu.live.sdk.android.api.common.dictionary.list.LiveDictionaryListRequest;
import com.gaosiedu.live.sdk.android.api.common.dictionary.list.LiveDictionaryListResponse;
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain;
import com.google.gson.Gson;
import com.haoke91.a91edu.GlobalConfig;
import com.haoke91.a91edu.MainActivity;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.lzy.okgo.cache.CacheMode;
import com.orhanobut.logger.Logger;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 项目名称：91edu
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/5/14 17:04
 */
public class WelcomeActivity extends BaseActivity {
    
    
    @Override
    public int getLayout() {
        return R.layout.activity_welcome;
    }
    
    @Override
    public void initialize() {
        LiveDictionaryListRequest request = new LiveDictionaryListRequest();
        request.setDicType("subject");
        
        CacheMode mode = ObjectUtils.isEmpty(UserManager.getInstance().getWelCourseInfo()) ? CacheMode.NO_CACHE : CacheMode.IF_NONE_CACHE_REQUEST;
        Api.getInstance().post(request, LiveDictionaryListResponse.class, new ResponseCallback<LiveDictionaryListResponse>() {
                @Override
                public void onResponse(LiveDictionaryListResponse date, boolean isFromCache) {
                    //                    com.orhanobut.logger.Logger.e("isFromCache==" + isFromCache);
                    //                    String s = new Gson().toJson(date);
                    if (!isFromCache) {
                        UserManager.getInstance().saveCourseInfo((ArrayList<DictionaryDomain>) date.getData().getList());
                    }
                    //                    CacheDiskUtils.getInstance().put(GlobalConfig.COURSE_LIST, date);
                    goMain();
                }
        
                @Override
                public void onEmpty(LiveDictionaryListResponse date, boolean isFromCache) {
                    goMain();
    
                }
        
                @Override
                public void onError() {
                    goMain();
                }
            }, mode, ConvertUtils.timeSpan2Millis(5, TimeConstants.DAY)
            , "");
    }
    
    private int arriveCount = 0;
    
    private void goMain() {
        arriveCount++;
        if (arriveCount != 2) {
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        arriveCount = 0;
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        
        long appAttachTime = SPUtils.getInstance().getLong(GlobalConfig.APP_ATTACHE_TIME);
        long countTime = System.currentTimeMillis() - appAttachTime;//从application到入口Acitity的时间
        //所以闪屏页展示的时间为 2000ms - diffTime.
        startTimeCount(2 - countTime / 1000 < 0 ? 2 : 2 - countTime / 1000);
    }
    
    private Disposable mdDisposable;
    
    /**
     * 倒计时
     *
     * @param countTime
     */
    private void startTimeCount(long countTime) {
        mdDisposable = Flowable.intervalRange(0, countTime, 0, 1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
//                    Logger.e("aLong====" + aLong);
                }
            })
            .doOnComplete(new Action() {
                @Override
                public void run() throws Exception {
                    goMain();
                }
            })
            .subscribe();
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!ObjectUtils.isEmpty(mdDisposable)) {
            mdDisposable.dispose();
        }
        getWindow().setBackgroundDrawable(null);//清除图片缓存
    }
}
