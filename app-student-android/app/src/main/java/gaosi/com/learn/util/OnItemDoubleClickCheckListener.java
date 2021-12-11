package gaosi.com.learn.util;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import java.util.Calendar;

/**
 * description: BaseQuickAdapter item双击检测
 * created by huangshan on 2019-12-26 15:25
 */
public abstract class OnItemDoubleClickCheckListener implements BaseQuickAdapter.OnItemClickListener {

    private static int MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0L;

    public OnItemDoubleClickCheckListener(int MIN_CLICK_DELAY_TIME) {
        OnItemDoubleClickCheckListener.MIN_CLICK_DELAY_TIME = MIN_CLICK_DELAY_TIME;
    }

    public OnItemDoubleClickCheckListener() {
    }

    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - this.lastClickTime > MIN_CLICK_DELAY_TIME) {
            this.lastClickTime = currentTime;
            this.onDoubleClick(baseQuickAdapter, view, i);
        }

    }

    protected abstract void onDoubleClick(BaseQuickAdapter var1, View var2, int var3);
}
