package com.gstudentlib.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.gsbaselib.base.GSBaseFragment;
import com.gsbaselib.base.inject.GSAnnotation;
import com.gsbaselib.net.callback.GSHttpResponse;
import com.gsbaselib.utils.LOGGER;
import com.gsbaselib.utils.LangUtil;
import com.gsbaselib.utils.ToastUtil;
import com.gsbiloglib.builder.GSConstants;
import com.gsbiloglib.log.GSLogUtil;

public abstract class STBaseFragment extends GSBaseFragment {

    protected boolean mIsFirstVisible = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 当Fragment再次可见时
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint() && getView() != null && !mIsFirstVisible) {
            lazyLoad();
            mIsFirstVisible = true;
        }
    }

    /**
     * 懒加载
     */
    protected void lazyLoad() {
    }

    public void showLoadingProcessDialog() {
        if (getBaseActivity() != null) {
            getBaseActivity().showLoadingProgressDialog();
        }
    }

    public void dismissLoadingProcessDialog() {
        if (getBaseActivity() != null) {
            getBaseActivity().dismissLoadingProgressDialog();
        }
    }

    public STBaseActivity getBaseActivity() {
        if (getActivity() != null && getActivity() instanceof STBaseActivity) {
            return (STBaseActivity) getActivity();
        }

        return null;
    }

    /**
     * 统计点击事件
     *
     * @param key    点击事件的key
     */
    public void collectClickEvent(String key) {
        collectClickEvent(key, "");
    }

    /**
     * 统计点击事件
     *
     * @param key    点击事件的key
     * @param value  点击事件的value
     */
    public void collectClickEvent(String key, String value) {
        if (LangUtil.isEmpty(key) || getBaseActivity() == null) {
            return;
        }
        GSLogUtil.collectClickLog(GSConstants.Companion.getP().getCurrRefer(), key, value);
    }

    /**
     * 吐司提醒status==0的情况下的message消息
     * @param gsHttpResponse
     */
    public int showResponseErrorMessage(GSHttpResponse gsHttpResponse) {
        if(gsHttpResponse != null) {
            if(gsHttpResponse.status == 0) {
                ToastUtil.showToast(gsHttpResponse.message + "");
                return gsHttpResponse.status;
            }
            return gsHttpResponse.status;
        }
        return -1;
    }

    protected String mPageId = "";
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
        if (TextUtils.isEmpty(mPageId) && getBaseActivity() != null) {
            mPageId = getBaseActivity().getPageId();
        }

        return mPageId;
    }

    /**
     * 账号在其他设备登录
     * 同步为了防止其他线程进入为处理完成的请求
     */
    @Override
    public synchronized void kickOut() {
    }

    /**
     * 是否为平板true false
     */
    public boolean isPad() {
        return (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
