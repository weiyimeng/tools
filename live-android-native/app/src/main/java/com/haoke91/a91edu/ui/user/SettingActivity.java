package com.haoke91.a91edu.ui.user;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CleanUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.gaosiedu.live.sdk.android.api.auth.login.LiveLogoutRequest;
import com.gaosiedu.live.sdk.android.base.ApiResponse;
import com.haoke91.a91edu.GlobalConfig;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.utils.imageloader.GlideCacheUtil;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.SwitchButton;
import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.event.RxBus;
import com.haoke91.baselibrary.views.TipDialog;
import com.lzy.okgo.db.CacheManager;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/12 下午4:42
 * 修改人：weiyimeng
 * 修改时间：2018/7/12 下午4:42
 * 修改备注：
 */
public class SettingActivity extends BaseActivity {
    public static final String only_wifi = "only_wifi";
    public static final String notice_3g = "notice_3g";
    
    private SwitchButton sb_setting_wifi;
    private TextView tv_cache_size;
    
    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_setting;
    }
    
    @Override
    public void initialize() {
        SwitchButton sb_setting_3g = findViewById(R.id.sb_setting_3g);
        sb_setting_wifi = findViewById(R.id.sb_setting_wifi);
        boolean is_only_wifi = SPUtils.getInstance().getBoolean(only_wifi, false);
        sb_setting_wifi.setCheckedImmediately(is_only_wifi);
        boolean is_notice_3g = SPUtils.getInstance().getBoolean(notice_3g, false);
        sb_setting_3g.setCheckedImmediately(is_notice_3g);
        findViewById(R.id.rl_setting_cache).setOnClickListener(OnClickListener);
        findViewById(R.id.rl_go_setting).setOnClickListener(OnClickListener);
        sb_setting_wifi.setOnCheckedChangeListener(OnCheckedChangeListener);
        sb_setting_3g.setOnCheckedChangeListener(OnCheckedChangeListener);
        String cacheSize = GlideCacheUtil.getInstance().getCacheSize(Utils.getApp());
        tv_cache_size = findViewById(R.id.tv_setting_cache_size);
        tv_cache_size.setText(cacheSize);
        if (UserManager.getInstance().isLogin()) {
            findViewById(R.id.rl_setting_loginOut).setOnClickListener(OnClickListener);
        } else {
            findViewById(R.id.rl_setting_loginOut).setVisibility(View.GONE);
        }
        findViewById(R.id.rl_setting_about).setOnClickListener(OnClickListener);
    }
    
    private CompoundButton.OnCheckedChangeListener OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView == sb_setting_wifi) {
                SPUtils.getInstance().put(only_wifi, isChecked);
            } else {
                SPUtils.getInstance().put(notice_3g, isChecked);
            }
        }
    };
    
    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_setting_cache:
                    GlideCacheUtil.getInstance().clearImageAllCache(Utils.getApp());
                    ThreadUtils.executeByIo(new ThreadUtils.Task<Boolean>() {
                        @Nullable
                        @Override
                        public Boolean doInBackground() {
                            //                            CleanUtils.cleanInternalCache();
                            CleanUtils.cleanCustomDir(GlobalConfig.album);
                            return CacheManager.getInstance().clear();
                        }
                        
                        @Override
                        public void onSuccess(@Nullable Boolean result) {
                            if (result) {
                                ToastUtils.showShort("清除成功");
                                tv_cache_size.setText("0kb");
                            }
                        }
                        
                        @Override
                        public void onCancel() {
                        
                        }
                        
                        @Override
                        public void onFail(Throwable t) {
                        
                        }
                    });
                    
                    break;
                case R.id.rl_setting_loginOut:
                    isLoginOut();
                    break;
                case R.id.rl_go_setting:
                    Intent intent = IntentUtils.getLaunchAppDetailsSettingsIntent(AppUtils.getAppPackageName(), true);
                    startActivity(intent);
                    break;
                case R.id.rl_setting_about:
                    AboutUsActivity.Companion.start(SettingActivity.this);
                    //                    GeneralWebViewActivity.start(SettingActivity.this, "https://www.91haoke.com/help/6");
                    break;
                default:
            }
        }
    };
    
    private void isLoginOut() {
        TipDialog dialog = new TipDialog(this);
        dialog.setTextDes(getString(R.string.login_out));
        dialog.setButton1(getString(R.string.action_ok), new TipDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, TipDialog dialog) {
                loginOut();
                dialog.dismiss();
            }
        });
        dialog.setButton2(getString(R.string.cancel), new TipDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, TipDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    
    private void loginOut() {
        LiveLogoutRequest request = new LiveLogoutRequest();
        request.setUserId(String.valueOf(UserManager.getInstance().getUserId()));
        request.setToken(UserManager.getInstance().getToken());
        Api.getInstance().post(request, ApiResponse.class, new ResponseCallback<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse date, boolean isFromCache) {
            
            }
        }, "");
        UserManager.getInstance().clearUserInfo();
        MessageItem messageItem = new MessageItem(MessageItem.action_login, null);
        RxBus.getIntanceBus().post(messageItem);
        finish();
    }
    
}
