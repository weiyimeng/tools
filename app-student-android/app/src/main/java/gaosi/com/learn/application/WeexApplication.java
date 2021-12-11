package gaosi.com.learn.application;

import android.app.Activity;
import android.os.Looper;
import com.bumptech.glide.Glide;
import com.gaosi.axxdtlive.DTLiveSDKConfig;
import com.gaosi.englishhomework.EnglishHomeworkSDKConfig;
import com.gaosi.englishrecite.EnglishReciteSDKConfig;
import com.gaosi.homework.HomeworkSDKConfig;
import com.gaosi.largeclasshomework.LargeClassHomeworkSDKConfig;
import com.gaosi.newgrouplive.config.NewGroupLiveSDKConfig;
import com.gaosi.preclass.PreClassSDKConfig;
import com.gaosi.specialcourse.SpecialCourseSDKConfig;
import com.gaosi.studycenter.StudyCenterSDKConfig;
import com.gaosi.teacheronline.TeacherOnLineSDKConfig;
import com.gaosi.webresource_uploader.WebResourceUploader;
import com.github.mzule.activityrouter.annotation.Modules;
import com.gsbaselib.InitBaseLib;
import com.gsbaselib.base.BaseTaskSwitch;
import com.gsbaselib.base.GSBaseActivity;
import com.gsbaselib.base.GSBaseApplication;
import com.gsbaselib.base.log.LogUtil;
import com.gsbaselib.utils.ActivityCollector;
import com.gsbaselib.utils.FileUtil;
import com.gsbaselib.utils.LOGGER;
import com.gstudentlib.SDKConfig;
import com.gstudentlib.base.STBaseApplication;
import com.gstudentlib.base.STBaseConstants;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import gaosi.com.learn.BuildConfig;
import gaosi.com.learn.push.GSUmengManager;
import gaosi.com.learn.studentapp.loading.SplashingActivity;
import static com.gstudentlib.base.STBaseConstants.Umeng_Push_Type;
import static com.gstudentlib.base.STBaseConstants.Umeng_Push_Type_Test;

/**
 * 初始化Weex
 */
@Modules({"app"
        , "GSHomeworkLib"
        , "GSEnglishHomeworkLib"
        , "GSEnglishRecite"
        , "GSTeacherOnLineLib"
        , "GSPreClassLib"
        , "GSpecialCourseLib"
        , "GStudentLib"
        , "RNStudentLib"
        , "GSWeexLib"
        , "GSDTLive"
        , "GSLargeClassHomework"
        , "GStudyCenter"
        , "GSNewGroupLive"})
public class WeexApplication extends STBaseApplication implements BaseTaskSwitch.OnTaskSwitchListener {

    @Override
    public void onCreate() {
        super.onCreate();
        //不收集友盟错误信息 初始化umeng统计与分析方面的信息
        GSUmengManager.INSTANCE.initUmeng(this);
    }

    /**
     * 一个APP可能有多个进程，每个进程都可能有全局的Application
     * 初始化应该使用主线程的Application
     */
    @Override
    protected void initInMainThread() {
        SDKConfig.INSTANCE
                .with(this)
                .configApplicationId(BuildConfig.APPLICATION_ID)
                .configBuildType(BuildConfig.BUILD_TYPE)
                .configCallbackErrorListener(this)
                .configInterceptor(null, this)
                .configIsDebug(BuildConfig.ISDEBUG)
                .configOnTaskSwitchListener(this)
                .configProjectName("asa")
                .configStudentServer(BuildConfig.BASEURL_STUDENT)
                .configUpdateServer(BuildConfig.BASEURL_UPDATE)
                .configQiYuTestKey("b02bec1ce695a85f850d40b4cc7eae6c")
                .configQiYuReleaseKey("e6d21d0ba36dec116d9744797a26eee8")
                .configUpdateAppId("2")
                .configUpdateSerialNumber("bd0f3ba42dc727662a6a")
                .configGatewayId("G20191120102329661" , "7239ee54d864874b599886b8618e703f")
                .configSignSecret("h3kdxqqqvwyu8luomfv39z1v8z8x215a")
                .configPassportFlag("ptaxxxsapp")
                .configApiVersion("v20")
                .configRefreshTokenListener(this)
                .registerSDK(AppSDKConfig.INSTANCE)
                .registerSDK(HomeworkSDKConfig.INSTANCE)
                .registerSDK(EnglishHomeworkSDKConfig.INSTANCE)
                .registerSDK(SpecialCourseSDKConfig.INSTANCE)
                .registerSDK(TeacherOnLineSDKConfig.INSTANCE)
                .registerSDK(PreClassSDKConfig.INSTANCE)
                .registerSDK(DTLiveSDKConfig.INSTANCE)
                .registerSDK(LargeClassHomeworkSDKConfig.INSTANCE)
                .registerSDK(StudyCenterSDKConfig.INSTANCE)
                .registerSDK(NewGroupLiveSDKConfig.INSTANCE)
                .registerSDK(EnglishReciteSDKConfig.INSTANCE)
                .initLib();
        WebResourceUploader.init(BuildConfig.ISDEBUG,
                FileUtil.getH5ResourceDir(),
                FileUtil.getRnResourceDir());
    }

    /**
     * 删除Umeng标识
     */
    @Override
    public void deleteAlias() {
        super.deleteAlias();
        PushAgent.getInstance(GSBaseApplication.getApplication()).deleteAlias(STBaseConstants.userId
                , InitBaseLib.getInstance().getConfigManager().isRelease() ? Umeng_Push_Type : Umeng_Push_Type_Test
                , new UTrack.ICallBack(){
                    @Override
                    public void onMessage(boolean isSuccess, String message) {
                        LogUtil.d(isSuccess ? "删除umeng tag成功" : "删除umeng tag失败");
                    }
                });
    }

    /**
     * 后台切换到前台
     */
    @Override
    public void onTaskSwitchToForeground() {
        GSBaseActivity activity = getCurrentActivity();
        if (activity != null && !(activity instanceof SplashingActivity)) {
            activity.onTaskSwitchToForeground();
        }
    }
    
    /**
     * 前台切换到后台
     */
    @Override
    public void onTaskSwitchToBackground() {
        GSBaseActivity activity = getCurrentActivity();
        if (activity != null) {
            activity.onTaskSwitchToBackground();
        }
    }

    @Override
    public void onActivityCreated(Activity activity) {
        if (activity instanceof GSBaseActivity) {
            //应用数据统计接口
            PushAgent.getInstance(activity).onAppStart();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity instanceof GSBaseActivity) {
            MobclickAgent.onResume(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (activity instanceof GSBaseActivity) {
            MobclickAgent.onPause(activity);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            //只能在主线程执行,低内存时清空图片内存缓存
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Glide.get(this).clearMemory();
            }
        } catch (Exception e) {
            LOGGER.log(e);
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        //低内存时释放相应资源，暂没发现别的需要清理的场景
        try {
            //只能在主线程执行,低内存时清空图片内存缓存
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Glide.get(this).trimMemory(level);
            }
        } catch (Exception e) {
            LOGGER.log(e);
        }
        if (level == TRIM_MEMORY_MODERATE) {//内存不足，并且该进程在后台进程列表的中部
            //主动杀进程，防止内存回收后，打开应用加载静态变量时为null
            ActivityCollector.getInstance().finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            System.gc();
        }
    }
}
