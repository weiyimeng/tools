package com.gstudentlib.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gsbaselib.InitBaseLib;
import com.gsbaselib.base.GSBaseActivity;
import com.gsbaselib.base.GSBaseConstants;
import com.gsbaselib.base.inject.GSAnnotation;
import com.gsbaselib.base.log.LogUtil;
import com.gsbaselib.net.callback.GSHttpResponse;
import com.gsbaselib.utils.LOGGER;
import com.gsbaselib.utils.LangUtil;
import com.gsbaselib.utils.ShakeHelper;
import com.gsbaselib.utils.ToastUtil;
import com.gsbaselib.utils.glide.ImageLoader;
import com.gsbiloglib.builder.GSConstants;
import com.gsbiloglib.log.GSLogUtil;
import com.gstudentlib.R;
import com.gstudentlib.util.LoadingDialog;
import com.gstudentlib.util.MockDialog;

import java.util.Map;

/**
 * 针对学生端制定的BaseActivity
 */
public abstract class STBaseActivity extends GSBaseActivity implements ShakeHelper.OnShakeListener , View.OnClickListener {

    //标题栏
    public RelativeLayout rl_title;//标题栏最外层布局
    protected ImageView iv_title_left;//左侧返回按钮
    protected TextView tv_title_left;//左侧返回按钮
    protected TextView tv_title_text;//文字标题栏
    protected ImageView iv_title_right;//右侧按钮图片
    protected TextView tv_title_right;//右侧按钮图片
    public TextView tv_navigate_cover; //遮罩层
    protected View vStatusBar;//状态栏占位
    protected RelativeLayout mRootView;
    protected LoadingDialog mLoadingDialog;
    protected String mPageId = "";
    //晃动回调
    private boolean mIsShowMockDialog = false;
    private long spaceTime;
    private int MIN_SPACE = 1000;
    private MockDialog mMockDialog;
    protected ShakeHelper mShakeHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setStatusBar();
        super.setContentView(R.layout.activity_base);
        mRootView = findViewById(R.id.rl_container);
        this.initTitleBar();
        if (!InitBaseLib.getInstance().getConfigManager().isRelease()
                && !InitBaseLib.getInstance().getConfigManager().isMonkey()) {
            mShakeHelper = new ShakeHelper(this);
            mShakeHelper.setOnShakeListener(this);
            LogUtil.d("晃动helper创建成功");
        }
    }

    @Override
    public void onClick(View v) {
        if (isFinishing()) {
            return;
        }
        if (v == iv_title_left) {
            onBackPressed();
        } else if (v == tv_title_right || v == iv_title_right) {
            onRightClick(v);
        }
    }

    /**
     * 右侧按钮点击
     *
     * @param v 点击的View
     */
    protected void onRightClick(View v) {
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        rl_title = findViewById(R.id.rl_title);
        //左边按钮
        iv_title_left = findViewById(R.id.iv_title_left);
        tv_title_left = findViewById(R.id.tv_title_left);
        iv_title_left.setVisibility(View.VISIBLE);
        //标题栏
        tv_title_text = findViewById(R.id.tv_title_text);
        //右边按钮
        iv_title_right = findViewById(R.id.iv_title_right);
        tv_title_right = findViewById(R.id.tv_title_right);
        //标题遮罩层
        tv_navigate_cover = findViewById(R.id.tv_navigate_cover);
        vStatusBar = findViewById(R.id.vStatusBar);
        rl_title.setVisibility(View.GONE);
        iv_title_left.setOnClickListener(this);
    }

    public void gotoBi() {

    }

    /**
     * 设置Navbar 默认存在返回箭头（黑色）
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        setTitle(title, true);
    }

    /**
     * 设置Navbar 用户自行设置返回箭头
     * @param title
     * @param leftIcon
     */
    public void setTitle(CharSequence title , String leftIcon) {
        if(TextUtils.isEmpty(leftIcon)) {
            this.setTitle(title, true);
        }else {
            int drawable = getResources().getIdentifier(leftIcon, "drawable", getPackageName());
            this.setTitle(title, drawable);
        }
    }

    /**
     * 设置Navbar 用户自行设置返回箭头与标题颜色
     * @param title
     * @param titleColor
     * @param leftIcon
     */
    public void setTitle(CharSequence title , String titleColor , String leftIcon){
        setTitle(title ,leftIcon);
        if(!TextUtils.isEmpty(titleColor)) {
            try{
                tv_title_text.setTextColor(Color.parseColor(titleColor));
            }catch (Exception e){}
        }
    }

    /**
     * 设置Navbar
     * @param title
     * @param hasBackPressedIcon 是否存在返回键（默认黑色）
     */
    public void setTitle(CharSequence title, boolean hasBackPressedIcon) {
        int leftIconResId = -1;
        if (hasBackPressedIcon) {
            leftIconResId = R.drawable.gs_icon_back_blank_smooth;
        }
        setTitle(title, leftIconResId);
    }

    /**
     * 设置Navbar 标题 以及返回键ID
     * @param title
     * @param leftIconResId
     */
    public void setTitle(CharSequence title, @DrawableRes int leftIconResId) {
        setTitle(title, leftIconResId, "");
    }

    public void setTitle(CharSequence title, int leftIconResId, CharSequence rightTitle) {
        if (TextUtils.isEmpty(title) && leftIconResId < 0 && TextUtils.isEmpty(rightTitle)) {
            rl_title.setVisibility(View.GONE);
            return;
        }
        this.adjustStatusBarMargin(vStatusBar);
        rl_title.setVisibility(View.VISIBLE);
        tv_title_text.setText(title);
        tv_title_text.setVisibility(View.VISIBLE);
        if (leftIconResId > 0) {
            iv_title_left.setVisibility(View.VISIBLE);
            iv_title_left.setImageResource(leftIconResId);
        } else {
            iv_title_left.setVisibility(View.GONE);
        }

        setRightTitle(rightTitle, -1);
    }

    public void setTitle(CharSequence title, int leftIconResId, int rightIconResId) {
        if (TextUtils.isEmpty(title) && leftIconResId < 0 && rightIconResId < 0) {
            rl_title.setVisibility(View.GONE);
            return;
        }

        rl_title.setVisibility(View.VISIBLE);
        tv_title_text.setText(title);
        tv_title_text.setVisibility(View.VISIBLE);
        if (leftIconResId > 0) {
            iv_title_left.setVisibility(View.VISIBLE);
            iv_title_left.setImageResource(leftIconResId);
        } else {
            iv_title_left.setVisibility(View.GONE);
        }
        if (rightIconResId > 0) {
            iv_title_right.setVisibility(View.VISIBLE);
            iv_title_right.setImageResource(rightIconResId);
        } else {
            iv_title_right.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左侧返回按钮的图标
     *
     * @param icon     左侧返回按钮的图标
     * @param leftText 左侧的标题，默认是没有的
     */
    public void setLeftTitle(String icon, String leftText) {
        if (TextUtils.isEmpty(icon) && TextUtils.isEmpty(leftText)) {
            iv_title_left.setVisibility(View.GONE);
            tv_title_left.setVisibility(View.GONE);
            return;
        }

        rl_title.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(icon)) {
            iv_title_left.setVisibility(View.VISIBLE);
            iv_title_left.setImageResource(R.drawable.icon_back_blank_smooth);
        }
    }

    /**
     * 设置右侧标题的图标
     *
     * @param rightTitle     右侧标题
     * @param rightIconResId 右侧图片
     */
    public void setRightTitle(CharSequence rightTitle, int rightIconResId) {
        if (TextUtils.isEmpty(rightTitle) && rightIconResId <= 0) {
            iv_title_right.setVisibility(View.GONE);
            tv_title_right.setVisibility(View.GONE);
            return;
        }
        iv_title_left.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        rl_title.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(rightTitle)) {
            tv_title_right.setVisibility(View.GONE);
            iv_title_right.setVisibility(View.VISIBLE);
            iv_title_right.setImageResource(rightIconResId);
        } else {
            iv_title_right.setVisibility(View.GONE);
            tv_title_right.setVisibility(View.VISIBLE);
            tv_title_right.setText(rightTitle);
        }
    }

    /**
     * H5页面调用的setTitle方法
     *
     * @param isHidden   是否隐藏标题
     * @param icon       左侧按钮的图标
     * @param title      标题
     * @param rightIcon  右侧按钮的图标
     * @param rightTitle 右侧标题
     */
    public void setNavBarTitle(boolean isHidden, String icon, String title,
                               String rightIcon, String rightTitle) {
        if (isHidden) {
            rl_title.setVisibility(View.GONE);
            return;
        }
        this.adjustStatusBarMargin(vStatusBar);
        rl_title.setVisibility(View.VISIBLE);
        ImageLoader.INSTANCE.setImageViewResource(iv_title_left, icon);
        iv_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tv_title_text.setText(title);

        int drawable = 0;
        if (!TextUtils.isEmpty(icon)) {
            drawable = getResources().getIdentifier(rightIcon, "drawable", getPackageName());
        }
        setRightTitle(rightTitle, drawable);
        iv_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tv_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    /**
     * 设置导航栏背景颜色
     * @param color
     */
    public void setNavBarBackgroundColor(String color) {
        if(!TextUtils.isEmpty(color)) {
            try{
                rl_title.setBackgroundColor(Color.parseColor(color));
                this.setStatusBar(Color.parseColor(color));
            }catch (Exception e){}
        }
    }

    /**
     * 通过注解获取pageId 埋点使用
     * @return
     */
    public String getPageId() {
        if (TextUtils.isEmpty(mPageId)) {
            try {
                GSAnnotation annotation = getClass().getAnnotation(GSAnnotation.class);
                if (annotation != null) {
                    mPageId = annotation.pageId();
                }
            } catch (Exception e) {
                LOGGER.log("context" , e);
            }
        }

        return mPageId;
    }

/*****************************************************埋点**********************************************************/
    /**
     * 统计点击事件
     *
     * @param key    点击事件的key
     */
    public void collectClickEvent(String key) {
        collectClickEvent(key, "");
    }

    /**
     * 页面埋点
     */
    protected void collectPageLog() {
        if(TextUtils.isEmpty(getPageId())) {
            return;
        }
        if(getPageId().equals(GSConstants.Companion.getP().getPreviousRefer())) {
            return;
        }
        this.collectPageLog(getPageId(), GSConstants.Companion.getP().getPreviousRefer());
    }

    protected void collectPageLog(String mPageId) {
        if(mPageId.equals(GSConstants.Companion.getP().getPreviousRefer())) {
            return;
        }
        this.collectPageLog(mPageId, GSConstants.Companion.getP().getPreviousRefer());
    }

    protected void collectPageLog(String mPageId, String mRefer) {
        LogUtil.i("pad = " + mPageId + ":mRefer = " + mRefer);
        GSLogUtil.collectPageLog(mPageId, mRefer);
        GSConstants.Companion.getP().setCurrRefer(mPageId);
    }

    /**
     * 统计点击事件
     *
     * @param key    点击事件的key
     * @param value  点击事件的value
     */
    public void collectClickEvent(String key, String value) {
        if (LangUtil.isEmpty(key)) {
            return;
        }
        GSLogUtil.collectClickLog(GSConstants.Companion.getP().getCurrRefer(), key, value);
    }

    /**
     * 统计性能
     *
     * @param type
     * @param url
     * @param value
     */
    public void collectPerformanceLog(String type, String url, String value) {
        GSLogUtil.collectPerformanceLog(type, url, value);
    }

    public void collectPerformanceLog(String type , Map<String, String> extParam) {
        GSLogUtil.collectPerformanceLog(GSConstants.Companion.getP().getCurrRefer() , type , extParam);
    }

    /**
     * 吐司提醒status==0的情况下的message消息
     * @param gsHttpResponse
     */
    public int showResponseErrorMessage(GSHttpResponse gsHttpResponse) {
        if(gsHttpResponse != null) {
            if(gsHttpResponse.status == 0) {
                ToastUtil.showToast(gsHttpResponse.message + "");
            }
            return gsHttpResponse.status;
        }
        return -1;
    }

    public void showLoadingProgressDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }

        if (!mLoadingDialog.isShowing() && !(isFinishing() || isDestroyed())) {
            mLoadingDialog.show();
        }
    }

    public void dismissLoadingProgressDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing() && !(isFinishing() || isDestroyed())) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onShake() {
        LogUtil.d("晃动helper出现晃动");
        if (System.currentTimeMillis() - spaceTime < MIN_SPACE) {
            return;
        }
        LogUtil.d("晃动helper出现晃动 -->开始弹起");
        spaceTime = System.currentTimeMillis();
        mIsShowMockDialog = !mIsShowMockDialog;
        if (mIsShowMockDialog) {
            showMockDialog();
        } else {
            dismissMockDialog();
        }
    }
    protected void showMockDialog() {
        if (mMockDialog == null) {
            mMockDialog = new MockDialog(this);
            mMockDialog.show();
            return;
        }

        if (!mMockDialog.isShowing()) {
            mMockDialog.refreshUI();
            mMockDialog.show();
        }
    }
    protected void dismissMockDialog() {
        if (mMockDialog != null && mMockDialog.isShowing()) {
            mMockDialog.dismiss();
        }
    }

    /**
     * 字体的大小不随系统的改变而改变
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if ("as412".equals(getPageId())
                || "as413".equals(getPageId())
                || "as600".equals(getPageId())
                || "as601".equals(getPageId())
                || "as602".equals(getPageId())
                || "as4055".equals(getPageId())) {
            Configuration configuration = resources.getConfiguration();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            return resources;
        } else if (resources.getConfiguration().fontScale != GSBaseConstants.deviceInfoBean.getFontScale()) {
            Configuration configuration = resources.getConfiguration();
            configuration.fontScale = GSBaseConstants.deviceInfoBean.getFontScale();
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            return resources;
        }
        return resources;
    }

    /**
     * 是否为平板true false
     */
    public boolean isPad() {
        return (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
