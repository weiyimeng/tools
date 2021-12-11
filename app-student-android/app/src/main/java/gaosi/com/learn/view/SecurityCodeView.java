package gaosi.com.learn.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import gaosi.com.learn.R;

/**
 * 验证码登录View
 * <p/>
 * Created by pingfu on 2018/4/25.
 */
public class SecurityCodeView extends LinearLayout {

    //间隔时间60秒，因为第一次需要减一，所以gapNum = 61
    private int gapNum = 60 + 1;
    private TextView tvHint;
    private LinearLayout bgSecurityCode;
    private boolean isRunning = false;

    public SecurityCodeView(Context context) {
        super(context);
        initView();
    }

    public SecurityCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.ui_security_code, this);
        bgSecurityCode = findViewById(R.id.bg_security_code);
        tvHint = findViewById(R.id.tv_hint);
    }

    public void setState(boolean hasPressed) {
        setClickable(!hasPressed);
        if (hasPressed) {
            isRunning = true;
            bgSecurityCode.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.app_bg_get_code_shape_unenable));
            mCountDownNum = gapNum;
            getHandler().post(mCountDownRunnable);
        }else {
            isRunning = false;
            bgSecurityCode.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.app_bg_get_code_shape_enable));
        }
    }

    /**
     * 设置可用状态
     * @param isCanUse
     */
    public void setCanUseStatus(boolean isCanUse) {
        if(isRunning) {
            return;
        }
        setClickable(isCanUse);
        if(isCanUse) {
            bgSecurityCode.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.app_bg_get_code_shape_enable));
        }else {
            bgSecurityCode.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.app_bg_get_code_shape_unenable));
        }
    }

    /**
     * 立刻执行停止
     */
    public void stop() {
        if(isRunning) {
            mCountDownNum = 1;
            if (getHandler() != null && mCountDownRunnable != null) {
                getHandler().post(mCountDownRunnable);
            }
        }
    }

    private volatile int mCountDownNum = gapNum;
    private Runnable mCountDownRunnable = new Runnable() {
        @Override
        public void run() {
            mCountDownNum --;
            if (mCountDownNum == 0) {
                getHandler().removeCallbacks(mCountDownRunnable);
                tvHint.setText("重新获取");
                setState(false);
                return;
            }
            tvHint.setText(mCountDownNum + "s重新获取");
            bgSecurityCode.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.app_bg_get_code_shape_unenable));
            if (getHandler() != null && mCountDownRunnable != null) {
                getHandler().postDelayed(mCountDownRunnable, 1000);
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (getHandler() != null) {
            getHandler().removeCallbacksAndMessages(null);
        }
    }
}
