package com.haoke91.a91edu.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.baselibrary.event.RxBus;
import com.umeng.analytics.MobclickAgent;


/**
 * 项目名称：91edu
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/5/14 17:09
 */
public abstract class BaseActivity extends AppCompatActivity {
    private final String TAG = BaseActivity.class.getSimpleName();
    public static boolean isForeGround = false;
    
    public abstract int getLayout();
    
    public abstract void initialize();
    
    //    private Unbinder mUnbinder;
    protected RxBus mRxBus;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(getLayout());
        super.onCreate(savedInstanceState);
        //        mUnbinder = ButterKnife.bind(this);
        initialize();
        initEvent();
        registeredEvent();
    }
    
    protected void initEvent() {
    }
    
    protected void registeredEvent() {
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        if (mUnbinder != null) {
        //            mUnbinder.unbind();
        //        }
        
        //        Utils.dismissLoading();
    }
    
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }
    
    /**
     * oppen UI
     *
     * @param clz
     */
    public void startActivity(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        
        isForeGround = true;
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        dismissLoadingDialog();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        isForeGround = false;
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }
    
    private AlertDialog mLoadingDialog;
    
    public void showLoadingDialog() {
        showLoadingDialog(true);
    }
    
    public void showLoadingDialog(boolean cancelable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new AlertDialog.Builder(this, R.style.WrapDialog).create();
            View view = LayoutInflater.from(this).inflate(R.layout.layout_loading, null);
            mLoadingDialog.setView(view);
            mLoadingDialog.setCanceledOnTouchOutside(false);
        }
        mLoadingDialog.setCancelable(cancelable);
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
            ((LottieAnimationView) mLoadingDialog.findViewById(R.id.lottieView)).playAnimation();
        }
    }
    
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            ((LottieAnimationView) mLoadingDialog.findViewById(R.id.lottieView)).cancelAnimation();
            mLoadingDialog.dismiss();
        }
    }
    
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                top = l[1],
                bottom = top + v.getHeight(),
                right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }
    
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    /**
     * 替代 findveiwbyid
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T id(@IdRes int id) {
        return (T) findViewById(id);
    }
    
}
