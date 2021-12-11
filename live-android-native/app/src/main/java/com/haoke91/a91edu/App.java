package com.haoke91.a91edu;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.Utils;
import com.eduhdsdk.tools.ScreenScale;
import com.haoke91.a91edu.db.ObjectBoxUtils;
import com.haoke91.a91edu.utils.CrashHandler;
import com.haoke91.videolibrary.VideoApp;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.zzhoujay.richtext.RichText;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import okhttp3.OkHttpClient;

/**
 * 项目名称：91Live
 * 类描述：11111
 * 创建人：weiyimeng
 * 创建时间：2018/5/7 下午7:20
 * 修改人：weiyimeng
 * 修改时间：2018/5/7 下午7:20
 * 修改备注：
 */
public class App extends Application {
    
    
    //    static {
    //        //设置全局的Header构建器
    //        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
    //            @Override
    //            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
    //                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
    //                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
    //            }
    //        });
    //        //设置全局的Footer构建器
    //        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
    //            @Override
    //            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
    //                //指定为经典Footer，默认是 BallPulseFooter
    //                return new ClassicsFooter(context).setDrawableSize(20);
    //            }
    //        });
    //    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        initUm();
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        initOk();
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.LOG_DEBUG;
            }
        });
        if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(ObjectBoxUtils.getInstance().getBox(222)).start(this);
        }
        VideoApp.setApp(this);
        CrashHandler.getInstance().init(this);//crash
        /**
         * im
         */
        //        client.initialize(this);
        //        Client.initialize(this);
        /**
         * 拓课
         */
        ScreenScale.init(this);
        
    }
    
    private void initUm() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置线程的优先级，不与主线程抢资源
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                //                try {
                //                    // TODO: 2018/9/17
                //                    Thread.sleep(5000);//建议延迟初始化，可以发现是否影响其它功能，或者是崩溃！
                //                } catch (InterruptedException e) {
                //                    e.printStackTrace();
                //                }
                //子线程初始化第三方组件
                RichText.initCacheDir(App.this);
                UMConfigure.init(App.this, "5afcee3ff29d98251f00000f", AnalyticsConfig.getChannel(App.this), UMConfigure.DEVICE_TYPE_PHONE, null);
                MobclickAgent.openActivityDurationTrack(false);
                //                PlatformConfig.setQQZone("101409108", "e9861618c877e19711d8f2582cba5bd4");
                //                //    PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
                //                PlatformConfig.setWeixin("wx7c1df1136554af6a", "127feb4fbcaabb57f02e83feec103735");
                //                PlatformConfig.setSinaWeibo("3032181475", "f1417e95479ba9c3c994e70fb0e04706", "http://sns.whalecloud.com");
            }
        }).start();
    }
    
    private void initOk() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);
        HttpsUtils.SSLParams sslSocketFactory = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslSocketFactory.sSLSocketFactory, sslSocketFactory.trustManager);
        
        HttpHeaders headers = new HttpHeaders();
        HttpParams params = new HttpParams();
        OkGo.getInstance().init(this)                       //必须调用初始化
            .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
            .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
            .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
            .setRetryCount(0)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
            .addCommonHeaders(headers)                      //全局公共头
            .addCommonParams(params);
        
    }
    
    
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //记录Application初始化时间
        Utils.init(this);
        SPUtils.getInstance().put(GlobalConfig.APP_ATTACHE_TIME, System.currentTimeMillis());
    }
    
    
    private final ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
            if (activity.findViewById(R.id.toolbar) != null) { //找到 Toolbar 并且替换 Actionbar
                if (activity instanceof AppCompatActivity) {
                    ((AppCompatActivity) activity).setSupportActionBar((Toolbar) activity.findViewById(R.id.toolbar));
                    ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activity.setActionBar((android.widget.Toolbar) activity.findViewById(R.id.toolbar));
                        activity.getActionBar().setDisplayShowTitleEnabled(false);
                    }
                }
            }
            if (activity.findViewById(R.id.toolbar_title) != null) { //找到 Toolbar 的标题栏并设置标题名
                ((TextView) activity.findViewById(R.id.toolbar_title)).setText(activity.getTitle());
            }
            if (activity.findViewById(R.id.toolbar_back) != null) { //找到 Toolbar 的返回按钮,并且设置点击事件,点击关闭这个 Activity
                activity.findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                });
            }
        }
        
        
        @Override
        public void onActivityStarted(Activity activity) {
        
        }
        
        @Override
        public void onActivityResumed(Activity activity) {
        
        }
        
        @Override
        public void onActivityPaused(Activity activity) {
        
        }
        
        @Override
        public void onActivityStopped(Activity activity) {
        
        }
        
        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        
        }
        
        @Override
        public void onActivityDestroyed(Activity activity) {
        
        }
    };
    
}
