package com.haoke91.a91edu.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.haoke91.baselibrary.event.RxBus;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/6/4 17:42
 */
public abstract class BaseFragment extends Fragment {
    private final String TAG = BaseFragment.class.getSimpleName();
    protected Activity mContext;
    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;
    protected RxBus mRxBus;
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getActivity();
    }
    
    
    //    private Unbinder mUnbinder;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(getLayout(), null);
        //        mUnbinder = ButterKnife.bind(view);
        return view;
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        //        mUnbinder.unbind();
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
        //    Logger.e("onActivityCreated=====");
    }
    
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }
    
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //        Logger.e("setUserVisibleHint]====" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        
    }
    
    @Override
    public void onDestroy() {
        if (mRxBus != null) {
            mRxBus.unSubscribe(getActivity().getClass());
        }
        super.onDestroy();
    }
    
    public abstract int getLayout();
    
    /**
     * 就是当前UI可见，并且fragment已经初始化完毕，如果网络数据未加载，那么请求数据，或者需要强制刷新页面
     */
    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }
    
    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }
    
    /**
     * 懒加载
     */
    public void fetchData() {
    }
    
    public void startActivity(Class clz) {
        mContext.startActivity(new Intent(mContext, clz));
    }
    
}
