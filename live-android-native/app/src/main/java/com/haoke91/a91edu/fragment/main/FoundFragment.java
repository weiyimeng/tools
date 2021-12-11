package com.haoke91.a91edu.fragment.main;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.TabAdapter;
import com.haoke91.a91edu.fragment.BaseFragment;
import com.haoke91.a91edu.ui.found.ExchangePhyFragment;
import com.haoke91.a91edu.ui.found.GoldGoodsActivity;
import com.haoke91.a91edu.widget.NoDoubleClickListener;
import com.haoke91.baselibrary.smarttab.SmartTabLayout;
import com.haoke91.videolibrary.adapter.GiftListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 项目名称：MyHaoke1
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/6/4 15:26
 */
public class FoundFragment extends BaseFragment {
    
    //    private SmartTabLayout mTabLayout;
    //    private ViewPager vp_order;
    //    //    private RecyclerView mrv_giftGrid;
    //    //    private GridAdapter mGridAdapter;
    //
    //    @NonNull
    @Override
    public int getLayout() {
        return R.layout.fragment_found;
    }
    
    //
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.ll_gotoGoldStore).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                GoldGoodsActivity.Companion.start(mContext);
            }
        });
        //        MqttPublishRequest publishRequest = new MqttPublishRequest();
        
    }
    //
    
    
    //    private void init() {
    //
    //        vp_order.setOffscreenPageLimit(2);
    //        TabAdapter adapter = new TabAdapter(((AppCompatActivity) mContext).getSupportFragmentManager());
    //        adapter.addFragment(new ExchangePhyFragment(), "");
    //        adapter.addFragment(new ExchangePhyFragment(), "");
    //        vp_order.setAdapter(adapter);
    //        mTabLayout.setCustomTabView(this);
    //        mTabLayout.setViewPager(vp_order);
    //
    //
    //    }
    //
    //
    //    @Override
    //    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
    //
    //        LayoutInflater inflater = LayoutInflater.from(container.getContext());
    //        Resources res = container.getContext().getResources();
    //        View tab = inflater.inflate(R.layout.item_tab, container, false);
    //        TextView value = tab.findViewById(R.id.custom_text);
    //        //        value.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
    //        //        ColorStateList csl = getResources().getColorStateList(R.color.text_white_black_selector);
    //        //        value.setTextColor(csl);
    //        switch (position) {
    //            case 0:
    //                value.setText(res.getString(R.string.exchange_physical));
    //                break;
    //            case 1:
    //                value.setText(res.getString(R.string.exchange_virtual));
    //                break;
    //
    //        }
    //        return tab;
    //    }
    
    
}
