package com.haoke91.a91edu.fragment.main;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.gaosiedu.live.sdk.android.api.content.banner.LiveContentBannerRequest;
import com.gaosiedu.live.sdk.android.api.content.banner.LiveContentBannerResponse;
import com.gaosiedu.live.sdk.android.domain.ContentDomain;
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain;
import com.haoke91.a91edu.MainActivity;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.HomeBannerAdapter;
import com.haoke91.a91edu.adapter.TabAdapter;
import com.haoke91.a91edu.fragment.BaseFragment;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.course.HomeCourseFragment;
import com.haoke91.a91edu.ui.course.PublicClassActivity;
import com.haoke91.a91edu.ui.course.SearchActivity;
import com.haoke91.a91edu.ui.course.SpecialClassActivity;
import com.haoke91.a91edu.utils.AnimationTool;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.CirclePageIndicator;
import com.haoke91.a91edu.widget.HomeCoursePopup;
import com.haoke91.a91edu.widget.HomeScrollView;
import com.haoke91.a91edu.widget.HomeViewPager;
import com.haoke91.a91edu.widget.dropdownlayout.MaskView;
import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.event.RxBus;
import com.haoke91.baselibrary.smarttab.SmartTabLayout;
import com.lzy.okgo.cache.CacheMode;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 项目名称：MyHaoke1
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/6/4 15:26
 */
public class HomeFragment extends BaseFragment implements SmartTabLayout.TabProvider {
    
    private CirclePageIndicator mPageIndicator;
    private HomeBannerAdapter mImagePagerAdapter;
    private UltraViewPager vp_home_banner;
    private TextView tv_search, tv_home_garden, tv_special_class;
    private RelativeLayout rl_scroller_classify, rv_top_classify;
    private HomeScrollView sv_home;
    private HomeViewPager vp_home_course;
    private SmartTabLayout smartTabLayout;
    private Toolbar toolbar;
    private View view_left, view_right, mGotoTopBtn;
    //课程信息
    private List<DictionaryDomain> courseInfo;
    
    @NonNull
    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }
    
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        view_right = view.findViewById(R.id.view_right);
        view_left = view.findViewById(R.id.view_left);
        tv_special_class = view.findViewById(R.id.tv_special_class);
        vp_home_course = view.findViewById(R.id.vp_home_course);
        tv_home_garden = view.findViewById(R.id.tv_home_garden);
        vp_home_banner = view.findViewById(R.id.vp_home_banner);
        smartTabLayout = view.findViewById(R.id.smartTabLayout);
        view.findViewById(R.id.tv_public_class).setOnClickListener(OnClickListener);
        view.findViewById(R.id.iv_public_clz).setOnClickListener(OnClickListener);
        view.findViewById(R.id.iv_special_clz).setOnClickListener(OnClickListener);
        mPageIndicator = view.findViewById(R.id.indicator);
        toolbar = view.findViewById(R.id.toolbar);
        mGotoTopBtn = view.findViewById(R.id.gotoTopBtn);
        ViewGroup.LayoutParams vplp = vp_home_banner.getLayoutParams();
        vplp.width = getResources().getDisplayMetrics().widthPixels;
        vplp.height = vplp.width / 2;
        vp_home_banner.setLayoutParams(vplp);
        rv_top_classify = view.findViewById(R.id.rv_top_classify);
        rl_scroller_classify = view.findViewById(R.id.rl_scroller_classify);
        sv_home = view.findViewById(R.id.sv_home);
        tv_search = view.findViewById(R.id.searchView);
        
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            
            @Override
            public void onGlobalLayout() {
                int marginTop = Math.max(sv_home.getScrollY(), rl_scroller_classify.getTop());
                rv_top_classify.layout(0, marginTop, smartTabLayout.getWidth(), marginTop + smartTabLayout.getHeight());
                //                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                
            }
        });
        
        
        sv_home.setOnScrollListener(new HomeScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                int marginTop = Math.max(scrollY, rl_scroller_classify.getTop());
                rv_top_classify.layout(0, marginTop, smartTabLayout.getWidth(), marginTop + smartTabLayout.getHeight());
                if (scrollY > 100) {
                    mGotoTopBtn.setVisibility(View.VISIBLE);
                } else {
                    mGotoTopBtn.setVisibility(View.GONE);
                }
            }
        });
        init();
        getBannerDate();
        registeredEvent();
    }
    
    protected void registeredEvent() {
        mRxBus = RxBus.getIntanceBus();
        mRxBus.doSubscribe(MainActivity.class, MessageItem.class, new Consumer<MessageItem>() {
            @Override
            public void accept(MessageItem messageItem) throws Exception {
                if (ObjectUtils.isEmpty(messageItem)) {
                    return;
                }
                if (messageItem.getType() == MessageItem.change_tab && !ObjectUtils.isEmpty(messageItem.getTargetClass())) {
                    vp_home_course.setCurrentItem((Integer) messageItem.getTargetClass());
                }
            }
        });
        
    }
    
    
    private void getBannerDate() {
        LiveContentBannerRequest request = new LiveContentBannerRequest();
        request.setType(2);
        Api.getInstance().post(request, LiveContentBannerResponse.class, new ResponseCallback<LiveContentBannerResponse>() {
            @Override
            public void onResponse(LiveContentBannerResponse date, boolean isFromCache) {
                if (ObjectUtils.isEmpty(date.getData().getContentDomains())) {
                    return;
                }
                mImagePagerAdapter = new HomeBannerAdapter(mContext, date.getData().getContentDomains());
                vp_home_banner.setAdapter(mImagePagerAdapter);
                mPageIndicator.setViewPager(vp_home_banner.getViewPager());
            }
        }, CacheMode.FIRST_CACHE_THEN_REQUEST, ConvertUtils.timeSpan2Millis(1, TimeConstants.HOUR), "");
        
    }
    
    private void init() {
        
        tv_search.setOnClickListener(OnClickListener);
        tv_home_garden.setOnClickListener(OnClickListener);
        tv_special_class.setOnClickListener(OnClickListener);
        mGotoTopBtn.setOnClickListener(OnClickListener);
        vp_home_banner.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        mImagePagerAdapter = new HomeBannerAdapter(mContext, new ArrayList<ContentDomain>());
        vp_home_banner.setAdapter(mImagePagerAdapter);
        
        vp_home_banner.setInfiniteLoop(true);
        vp_home_banner.setAutoScroll(5000);
        tabAdapteradapter = new TabAdapter<HomeCourseFragment>(((AppCompatActivity) mContext).getSupportFragmentManager());
        courseInfo = UserManager.getInstance().getCourseInfo();
        for (DictionaryDomain info : courseInfo) {
            tabAdapteradapter.addFragment(HomeCourseFragment.Companion.newInstance(info.getDicValue()), "");
        }
        vp_home_course.setOffscreenPageLimit(6);
        vp_home_course.setAdapter(tabAdapteradapter);
        smartTabLayout.setCustomTabView(this);
        smartTabLayout.setViewPager(vp_home_course);
        //        smartTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(mContext.getApplicationContext(),R.color.color_75C82B),ContextCompat.getColor(mContext.getApplicationContext(),R.color.Yellow));
        smartTabLayout.setOnScrollChangeListener(new SmartTabLayout.OnScrollChangeListener() {
            @Override
            public void onScrollChanged(int scrollX, int oldScrollX) {
                if (totalWidth == 0) {
                    totalWidth = smartTabLayout.getTabAt(0).getWidth() * (smartTabLayout.getTabsCount());
                    width = smartTabLayout.getWidth();
                }
                if (scrollX <= 0) {
                    view_left.setVisibility(View.INVISIBLE);
                    view_right.setVisibility(View.VISIBLE);
                } else if (scrollX >= 0) {
                    view_left.setVisibility(View.VISIBLE);
                    
                    if (totalWidth - width - scrollX / 1.2 >= 0) {
                        view_right.setVisibility(View.VISIBLE);
                    } else {
                        view_right.setVisibility(View.INVISIBLE);
                    }
                }
                
            }
        });
        vp_home_course.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            
            }
            
            @Override
            public void onPageSelected(int position) {
                TextView viewById = smartTabLayout.getTabAt(position).findViewById(R.id.custom_text);
                viewById.setTextColor(getResources().getColor(R.color.color_181818));
                AnimationTool.textScaleAnimation(viewById, 1f, 1.2f);
                for (int i = 0, size = smartTabLayout.getTabsCount(); i < size; i++) {
                    if (position == i) {
                        continue;
                    }
                    TextView textView = smartTabLayout.getTabAt(i).findViewById(R.id.custom_text);
                    textView.setScaleX(1);
                    textView.setScaleY(1);
                    textView.setTextColor(getResources().getColor(R.color.color_636363));
                }
                
            }
            
            @Override
            public void onPageScrollStateChanged(int state) {
            
            }
        });
    }
    
    private int totalWidth, width;
    private TabAdapter tabAdapteradapter;
    private HomeCoursePopup gardenPop;
    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.searchView:
                    ActivityOptionsCompat compat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(mContext,
                            toolbar, getString(R.string.sign));
                    ActivityCompat.startActivity(mContext, new Intent(mContext,
                        SearchActivity.class), compat.toBundle());
                    
                    
                    break;
                case R.id.tv_home_garden:
                    final FrameLayout container = getActivity().findViewById(android.R.id.content);
                    if (maskView == null) {
                        maskView = new MaskView(mContext);
                    }
                    if (ObjectUtils.isEmpty(gardenPop)) {
                        gardenPop = HomeCoursePopup.create()
                            .setContext(mContext)
                            .setFocusAndOutsideEnable(true)
                            .setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    container.removeView(maskView);
                                    
                                }
                            })
                            .setOnViewClickListener(new HomeCoursePopup.OnViewClickListener() {
                                @Override
                                public void onVideoClick(DictionaryDomain garden) {
                                    tv_home_garden.setText(garden.getDicName());
                                    int currentItem = vp_home_course.getCurrentItem();
                                    List<HomeCourseFragment> fragments = tabAdapteradapter.getmFragments();
                                    for (int i = 0, j = fragments.size(); i < j; i++) {
                                        if (i == currentItem) {
                                            fragments.get(i).refreshDate(garden.getDicValue());
                                        } else {
                                            fragments.get(i).prepareRefreshDate(garden.getDicValue());
                                        }
                                        
                                    }
                                }
                            })
                            .apply();
                    }
                    
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.topMargin = 200;
                    maskView.setLayoutParams(layoutParams);
                    
                    if (!gardenPop.isShowing()) {
                        container.addView(maskView);
                    }
                    gardenPop.showAsDropDown(tv_home_garden, 0, 10);
                    break;
                
                case R.id.tv_special_class:
                case R.id.iv_special_clz:
                    SpecialClassActivity.start(mContext);
                    break;
                case R.id.tv_public_class:
                case R.id.iv_public_clz:
                    PublicClassActivity.start(mContext);
                    break;
                case R.id.gotoTopBtn:
                    sv_home.smoothScrollTo(0, 0);
                    break;
                default:
            }
        }
    };
    private MaskView maskView;
    
    public void scrollToTop() {
        mGotoTopBtn.performClick();
    }
    
    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        Resources res = container.getContext().getResources();
        View tab = inflater.inflate(R.layout.item_home_tab, container, false);
        TextView value = tab.findViewById(R.id.custom_text);
        
        value.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        value.setTextColor(getResources().getColor(R.color.color_636363));
        // ColorStateList csl = getResources().getColorStateList(R.color.text_white_black_selector);
        // value.setTextColor(csl);
        String dicName = courseInfo.get(position).getDicName();
        value.setText(dicName);
        switch (position) {
            case 0:
                value.setTextColor(getResources().getColor(R.color.color_181818));
                value.setScaleX(1.2f);
                value.setScaleY(1.2f);
                //                value.setText(res.getString(R.string.all));
                
                break;
        }
        return tab;
    }
    
}
