package com.haoke91.a91edu.ui.order;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.util.Currency;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.TabAdapter;
import com.haoke91.a91edu.fragment.main.HomeFragment;
import com.haoke91.a91edu.fragment.main.StudyFragment;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.baselibrary.smarttab.SmartTabLayout;


/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/10 下午3:29
 * 修改人：weiyimeng
 * 修改时间：2018/7/10 下午3:29
 * 修改备注：
 */
public class OrderCenterActivity extends BaseActivity implements SmartTabLayout.TabProvider {
    
    public static void start(Context context) {
        Intent intent = new Intent(context, OrderCenterActivity.class);
        context.startActivity(intent);
    }
    
    public static void start(Context context, int currentTab) {
        Intent intent = new Intent(context, OrderCenterActivity.class);
        intent.putExtra("currentTab", currentTab);
        context.startActivity(intent);
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_ordercenter;
    }
    
    
    @Override
    public void initialize() {
        int currentTab = getIntent().getIntExtra("currentTab", -1);
        SmartTabLayout tab_order = findViewById(R.id.tab_order);
        ViewPager vp_order = findViewById(R.id.vp_order);
        vp_order.setOffscreenPageLimit(5);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        //        adapter.addFragment(new StudyFragment(), "");
        adapter.addFragment(AllOrderFragment.newInstance(AllOrderFragment.all_order), "");
        adapter.addFragment(AllOrderFragment.newInstance(AllOrderFragment.wait_pay), "");
        adapter.addFragment(AllOrderFragment.newInstance(AllOrderFragment.have_pay), "");
        adapter.addFragment(AllOrderFragment.newInstance(AllOrderFragment.cancel_order), "");
        adapter.addFragment(ReturnOrderFragment.newInstance(AllOrderFragment.back_order), "");
        vp_order.setAdapter(adapter);
        tab_order.setCustomTabView(this);
        tab_order.setViewPager(vp_order);
        if (currentTab != -1) {
            vp_order.setCurrentItem(currentTab);
        }
    }
    
    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        Resources res = container.getContext().getResources();
        View tab = inflater.inflate(R.layout.item_tab, container, false);
        TextView value = tab.findViewById(R.id.custom_text);
        value.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        switch (position) {
            case 0:
                value.setText(res.getString(R.string.all_order));
                break;
            case 1:
                value.setText(res.getString(R.string.wait_pay));
                break;
            case 2:
                value.setText(res.getString(R.string.have_pay));
                break;
            case 3:
                value.setText(res.getString(R.string.cancel_order));
                break;
            case 4:
                value.setText(res.getString(R.string.back_order_list));
                break;
        }
        return tab;
    }
}
