package gaosi.com.learn.studentapp.loading;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.gaosi.passport.PassportAPI;
import com.gaosi.rn.base.GSReactManager;
import com.gaosi.rn.student.util.ReactConfig;
import com.gaosi.rn.student.util.RnInfo;
import com.github.mzule.activityrouter.annotation.Router;
import com.gsbaselib.base.log.LogUtil;
import com.gsbaselib.net.GSRequest;
import com.gsbaselib.net.callback.GSHttpResponse;
import com.gsbaselib.net.callback.GSJsonCallback;
import com.gsbaselib.utils.ActivityCollector;
import com.gsbaselib.utils.FileUtil;
import com.gsbaselib.utils.LOGGER;
import com.gsbaselib.utils.SharedPreferenceUtil;
import com.gsbaselib.utils.StatusBarUtil;
import com.gsbaselib.utils.dialog.AbsAdapter;
import com.gsbaselib.utils.dialog.DialogUtil;
import com.gsbaselib.utils.update.IResUpdateListener;
import com.gsbiloglib.builder.GSConstants;
import com.gsbiloglib.log.GSLogUtil;
import com.gstudentlib.GSAPI;
import com.gstudentlib.SDKConfig;
import com.gstudentlib.base.BaseActivity;
import com.gstudentlib.base.STBaseConstants;
import com.gstudentlib.util.SchemeDispatcher;
import com.gstudentlib.util.hy.HyConfig;
import com.lzy.okgo.model.Response;
import java.util.HashMap;
import java.util.Map;
import gaosi.com.learn.R;
import gaosi.com.learn.application.StatisticsApi;
import gaosi.com.learn.bean.login.LoginResponse;
import gaosi.com.learn.studentapp.login.LoginActivity;
import gaosi.com.learn.studentapp.main.MainActivity;

/**
 * Created by ZhangXu on 2017/3/13.
 * 闪屏页
 */
@Router("splash")
public class SplashingActivity extends BaseActivity {

    //闪屏页播放时间
    private static final int SPLASH_DISPLAY_LENGTH = 3000;
    private long currentTime;
    private boolean isFirstVisiable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            setContentView(R.layout.activity_splashing_v19);
        } else {
            setContentView(R.layout.activity_splashing);
        }
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            LogUtil.e("start secondTime");
            finish();
            return;
        }
//        GSBaseConstants.channel = getChannelId();
        this.registerUpdate();
        currentTime = System.currentTimeMillis();
        LogUtil.d("start start->" + currentTime);
        mHandler.post(() -> {
            //上传上次收集的日志
            GSLogUtil.initLog();
            //下载个人形象
            HyConfig.INSTANCE.init();
            HyConfig.INSTANCE.updateHyResource();
        });
        this.checkTokenValidity();
    }

    /**
     * 校验Token的有效性
     */
    private void checkTokenValidity() {
        if (STBaseConstants.hasLogin()) {
            //保证在app升级后可以重新登录，判断条件是首先确定passport中是否存在token，同时验证passport是否是使用状态
            //使用状态的话只要passport没有token则进行重新登录操作
            if (PassportAPI.Companion.getInstance().getIsUsePassport()
                    && TextUtils.isEmpty(PassportAPI.Companion.getInstance().getToken_S_User())) {
                SDKConfig.INSTANCE.exitLogin();
            }
        }
    }

    /**
     * 注冊升级方案
     */
    private void registerUpdate() {
        //初始化升级更新配置
        SDKConfig.INSTANCE.configUpdate(this, new IResUpdateListener() {
            @Override
            public void onUpdating() {
                LogUtil.i("update-->onUpdating");
            }

            @Override
            public void onUpdateFail() {
                LogUtil.i("update-->onUpdateFail");
            }

            @Override
            public void onUpdateSuccess(Context context) {
                LogUtil.i("update-->onUpdateSuccess");
                Bundle bundle = new Bundle();
                String url = "myGold";
                bundle.putString("entrance", url);
                RnInfo rnInfo = new RnInfo(ReactConfig.mainComponentName, ReactConfig.INSTANCE.getJsBundleFile(), bundle);
                GSReactManager.init(context, rnInfo);
//                    RNRootViewPreLoader.preLoad((Activity) context, ReactConfig.mainComponentName, bundle);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * 隐私协议弹框
     */
    private void showPrivacyPolicy() {
        collectPageLog(StatisticsApi.privacyPolicyDialog);
        if (isFirstVisiable) {
            DialogUtil.getInstance().create(this, R.layout.ui_privacy_policy_dialog)
                    .show(new AbsAdapter() {
                        @Override
                        public void bindListener(View.OnClickListener onClickListener) {
                            String s = "欢迎使用爱学习，请您仔细阅读并同意《用户服务协议》及《隐私政策》";
                            SpannableStringBuilder builder = new SpannableStringBuilder(s);
                            String firstString = "《用户服务协议》";
                            String secondString = "《隐私政策》";
                            int firstIndex = s.indexOf(firstString);
                            int firstLength = firstString.length();
                            int secondIndex = s.indexOf(secondString);
                            int secondLength = secondString.length();
                            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#4B94FF")), firstIndex, firstIndex + firstLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#4B94FF")), secondIndex, secondIndex + secondLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            //设置文字点击事件
                            ClickableSpan clickableSpan1 = new ClickableSpan() {
                                @Override
                                public void onClick(@NonNull View view) {
                                    String url = "axx://commonWeb?url=%s";
                                    String filePath = FileUtil.getJsBundleSaveFilePath("agreement_userService.html");
                                    url = String.format(url, "file://" + filePath);
                                    SchemeDispatcher.jumpPage(SplashingActivity.this, url);
                                    GSLogUtil.collectClickLog(GSConstants.Companion.getP().getCurrRefer(), "XSD_355", "");
                                }

                                @Override
                                public void updateDrawState(@NonNull TextPaint ds) {
                                    //去除连接下划线
                                    ds.setUnderlineText(false);
                                }
                            };
                            ClickableSpan clickableSpan2 = new ClickableSpan() {
                                @Override
                                public void onClick(@NonNull View view) {
                                    String url = "axx://commonWeb?url=%s";
                                    String filePath = FileUtil.getJsBundleSaveFilePath("agreement_privacy.html");
                                    url = String.format(url, "file://" + filePath);
                                    SchemeDispatcher.jumpPage(SplashingActivity.this, url);
                                    GSLogUtil.collectClickLog(GSConstants.Companion.getP().getCurrRefer(), "XSD_356", "");
                                }

                                @Override
                                public void updateDrawState(@NonNull TextPaint ds) {
                                    //去除连接下划线
                                    ds.setUnderlineText(false);
                                }
                            };
                            builder.setSpan(clickableSpan1, firstIndex, firstIndex + firstLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            builder.setSpan(clickableSpan2, secondIndex, secondIndex + secondLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            TextView tvMainIdea = findViewById(R.id.tvMainIdea);
                            tvMainIdea.setMovementMethod(LinkMovementMethod.getInstance());
                            tvMainIdea.setHighlightColor(getResources().getColor(android.R.color.transparent));
                            tvMainIdea.setText(builder);
                            this.bindListener(onClickListener, R.id.tvAgree);
                            this.bindListener(onClickListener, R.id.tvCancel);
                        }

                        @Override
                        public void onClick(View v, DialogUtil dialog) {
                            super.onClick(v, dialog);
                            switch (v.getId()) {
                                case R.id.tvAgree:
                                    GSLogUtil.collectClickLog(GSConstants.Companion.getP().getCurrRefer(), "XSD_354", "");
                                    dialog.dismiss();
                                    SharedPreferenceUtil
                                            .setBooleanDataIntoSP("userInfo", "_isAgreePrivacyPolicy", true);
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!STBaseConstants.hasLogin()) {
                                                startActivity(new Intent(mContext, LoginActivity.class));
                                            } else {
                                                Intent intent = new Intent(mContext, MainActivity.class);
                                                startActivity(intent);
                                            }
                                            finish();
                                        }
                                    }, 500);
                                    break;
                                case R.id.tvCancel:
                                    GSLogUtil.collectClickLog(GSConstants.Companion.getP().getCurrRefer(), "XSD_353", "");
                                    dialog.dismiss();
                                    try {
                                        ActivityCollector.getInstance().appExit();
                                    } catch (Exception e) {
                                        LOGGER.log(e);
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onDismiss() {
                            super.onDismiss();
                            collectPageLog();
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        isFirstVisiable = false;
    }

    private void initData() {
        boolean isAgreePrivacyPolicy = SharedPreferenceUtil
                .getBooleanValueFromSP("userInfo", "_isAgreePrivacyPolicy", false);
        if (STBaseConstants.hasLogin() && isFirstVisiable) {
            Map<String, String> params = new HashMap<>();
            params.put("username", STBaseConstants.userInfo.getPhone());
            GSRequest.startRequest(GSAPI.checkUpdatePassword, params, new GSJsonCallback<LoginResponse>() {
                @Override
                public void onResponseSuccess(Response response, int i, @NonNull GSHttpResponse<LoginResponse> result) {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }
                    if (result.body == null) {
                        GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse);
                        return;
                    }
                    LoginResponse loginResponse = result.body;
                    STBaseConstants.isBeixiao = loginResponse.isBeiXiao();
                    STBaseConstants.changedPasswordCode = loginResponse.getChangedPasswordCode();
                    if (STBaseConstants.userInfo != null) {
                        STBaseConstants.userInfo.setIsBeiXiao(STBaseConstants.isBeixiao);
                        STBaseConstants.UserInfo = JSON.toJSONString(STBaseConstants.userInfo);
                    }
                }

                @Override
                public void onResponseError(Response response, int i, String message) {
                }
            });
        }
        long networkTime = System.currentTimeMillis() - currentTime;
        long delayedTime = 0;
        LogUtil.d("start end->" + System.currentTimeMillis() + ", net->" + networkTime);
        if (networkTime < SPLASH_DISPLAY_LENGTH) {
            delayedTime = SPLASH_DISPLAY_LENGTH - networkTime;
        }
        if (!isAgreePrivacyPolicy) {
            showPrivacyPolicy();
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!STBaseConstants.hasLogin()) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                } else {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, delayedTime);
    }

    @Override
    public synchronized void kickOut(String message) {
    }

    /**
     * 字体的大小不随系统的改变而改变
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1.0f;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, null);
        StatusBarUtil.setLightMode(this);
    }

    /**
     * 获取渠道value
     * @return
     */
    private String getChannelId() {
        ApplicationInfo applicationInfo = null;
        String value = "aixuexi";
        try {
            applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo == null) {
                return "aixuexi";
            }
            value = applicationInfo.metaData.getString("UMENG_CHANNEL");
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

}