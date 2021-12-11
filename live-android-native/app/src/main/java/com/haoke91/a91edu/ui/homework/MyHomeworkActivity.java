package com.haoke91.a91edu.ui.homework;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.ui.order.AllOrderFragment;
import com.haoke91.baselibrary.smarttab.SmartTabLayout;

import net.hockeyapp.android.UpdateFragment;


/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/10 下午3:29
 * 修改人：weiyimeng
 * 修改时间：2018/7/10 下午3:29
 * 修改备注：
 */
public class MyHomeworkActivity extends BaseActivity implements SmartTabLayout.TabProvider {
    
    public static void start(Context context) {
        Intent intent = new Intent(context, MyHomeworkActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_myhomework;
    }
    
    @Override
    public void initialize() {
        SmartTabLayout tab_order = findViewById(R.id.tab_order);
        ViewPager vp_order = findViewById(R.id.vp_order);
        vp_order.setOffscreenPageLimit(5);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(WaitUploadFragment.newInstance(WaitUploadFragment.wait), "");
        adapter.addFragment(WaitUploadFragment.newInstance(WaitUploadFragment.checking), "");
        adapter.addFragment(WaitUploadFragment.newInstance(WaitUploadFragment.checked), "");
        vp_order.setAdapter(adapter);
        //        vp_order.setOffscreenPageLimit(1);
        tab_order.setCustomTabView(this);
        tab_order.setViewPager(vp_order);
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
                value.setText(res.getString(R.string.waite_upload));
                break;
            case 1:
                value.setText(res.getString(R.string.checking));
                break;
            case 2:
                value.setText(res.getString(R.string.checked));
                break;
        }
        return tab;
    }
}
