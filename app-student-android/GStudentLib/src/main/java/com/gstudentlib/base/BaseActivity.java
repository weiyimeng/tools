package com.gstudentlib.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gaosi.passport.PassportAPI;
import com.gsbaselib.InitBaseLib;
import com.gsbaselib.base.GSBaseApplication;
import com.gsbaselib.base.log.LogUtil;
import com.gsbaselib.utils.ActivityCollector;
import com.gsbaselib.utils.StatusBarUtil;
import com.gsbaselib.utils.dialog.AbsAdapter;
import com.gsbaselib.utils.dialog.DialogUtil;
import com.gsbaselib.utils.dialog.simple.SimpleDialogUtil;
import com.gsbaselib.utils.update.UpdateUtil;
import com.gsbiloglib.bi.BiLogActivity;
import com.gstudentlib.R;
import com.gstudentlib.SDKConfig;
import com.gstudentlib.StatisticsDictionary;
import com.gstudentlib.bean.StudentInfo;
import com.gstudentlib.util.SchemeDispatcher;
import com.qiyukf.nimlib.sdk.NimIntent;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;

/**
 * 作者：created by 逢二进一 on 2019/9/12 13:57
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
public abstract class BaseActivity extends STBaseActivity {

    //用户信息访问类
    protected SharedPreferences userInfo;
    protected boolean isCollectPageLog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (STBaseConstants.userInfo == null) {
            String data = userInfo.getString("data", "");
            if (!TextUtils.isEmpty(data)) {
                STBaseConstants.userInfo = JSON.parseObject(data, StudentInfo.class);
                STBaseConstants.UserInfo = data;
                if (STBaseConstants.userInfo != null) {
                    STBaseConstants.userId = STBaseConstants.userInfo.getId();
                }
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, mRootView, false);
        this.setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        mContentView = view;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        params.addRule(RelativeLayout.BELOW, rl_title.getId());
        mRootView.addView(mContentView, params);
        initView();
    }

    /**
     * 默认初始化标题栏，继承这个方法，不用初始化标题栏，重写，需要初始化其他信息
     */
    protected void initView() {
    }

    /**
     * 退出提示
     */
    private long firstTime = 0;

    protected void existDialog() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime < 2000) {
            try {
                Unicorn.setUserInfo(null);
            } catch (Exception e) {
            }
            ActivityCollector.getInstance().appExit();
        } else {
            Toast toast = Toast.makeText(mContext, "再按一次退出爱学习", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            firstTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onTaskSwitchToBackground() {
        super.onTaskSwitchToBackground();
        collectPageLog(StatisticsDictionary.systemHomePage);
    }

    @Override
    public void onTaskSwitchToForeground() {
        super.onTaskSwitchToForeground();
    }

    /**
     * 检查更新
     * 新流程是，APP从后台切换到前台检查更新，但是非强制更新不弹出更新框
     */
    @Override
    public void checkUpdate() {
        if (InitBaseLib.getInstance().getConfigManager().getUseOnlineH5()) {
            return;
        }
        UpdateUtil.getInstance()
                .inject(this)
                .startCheckUpdate();
    }

    /**
     * 账号在其他设备登录
     * 同步为了防止其他线程进入为处理完成的请求
     */
    @Override
    public synchronized void kickOut(final String message) {
        if (STBaseConstants.isKickOut) {
            return;
        }
        BaseActivity activity = (BaseActivity) GSBaseApplication.getApplication().getCurrentActivity();
        //如果当前页面为null，或者已经销毁了、或者在启动页调用
        if (activity == null
                || activity.isFinishing()) {
            STBaseConstants.isKickOut = false;
            return;
        }
        STBaseConstants.isKickOut = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DialogUtil.getInstance().create(mContext, R.layout.ui_exit_login)
                        .show(new AbsAdapter() {
                            @Override
                            public void bindListener(View.OnClickListener onClickListener) {
                                this.bindText(R.id.tv_content, message);
                                this.bindText(R.id.btn_positive, "确定");
                                this.bindListener(onClickListener, R.id.btn_positive);
                            }

                            @Override
                            public void onClick(View v, DialogUtil dialog) {
                                super.onClick(v, dialog);
                                if (v.getId() == R.id.btn_positive) {
                                    dialog.dismiss();
                                    SDKConfig.INSTANCE.exitLogin();
                                    SchemeDispatcher.jumpPage(BaseActivity.this, "axx://login");
                                }
                            }

                            @Override
                            public void onDismiss() {
                                super.onDismiss();
                                STBaseConstants.isKickOut = false;
                            }
                        });
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.setIntent(intent);
        this.openQiYu();
    }

    /**
     * 打开七鱼消息
     */
    private void openQiYu() {
        Intent intent = getIntent();
        String sourceUrl = "main";
        String title = "爱学习";
        String sourceInfo = "custom information string";
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            // 打开客服窗口
            ConsultSource source = new ConsultSource(sourceUrl, title, sourceInfo);
            Unicorn.openServiceActivity(this, title, source);
            // 最好将intent清掉，以免从堆栈恢复时又打开客服窗口
            this.setIntent(new Intent());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SDKConfig.INSTANCE.setServiceEntranceActivity(getClass());
        if (isCollectPageLog) {
            collectPageLog();
        }
    }

    @Override
    public void gotoBi() {
        super.gotoBi();
        startActivity(new Intent(this , BiLogActivity.class));
    }

    @Override
    protected void setStatusBar() {
        this.setStatusBar(ContextCompat.getColor(mContext, R.color.white), 0);
        StatusBarUtil.setLightMode(this);
    }


    @Override
    protected void onDestroy() {
        dismissLoadingProgressDialog();
        SimpleDialogUtil.dismissDialog();
        if (mShakeHelper != null) {
            mShakeHelper.stop();
            LogUtil.d("晃动helper关闭成功");
        }
        dismissMockDialog();

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

}
