package com.haoke91.a91edu.ui.mycollect;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import com.haoke91.a91edu.fragment.main.StudyFragment;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.baselibrary.smarttab.SmartTabLayout;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/11 上午10:21
 * 修改人：weiyimeng
 * 修改时间：2018/7/11 上午10:21
 * 修改备注：
 */
public class MycollectActivity extends BaseActivity implements SmartTabLayout.TabProvider {
    
    public static void start(Context context) {
        Intent intent = new Intent(context, MycollectActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_mycollect;
    }
    
    @Override
    public void initialize() {
        SmartTabLayout  tab_order = findViewById(R.id.tab_mycollect);
        ViewPager   vp_order = findViewById(R.id.vp_order);
        vp_order.setOffscreenPageLimit(2);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyCollectCourseFragment(), "课程");
        //        adapter.addFragment(new MyCollectCourseFragment(), "教师");
        //        adapter.addFragment(new MyCollectCourseFragment(), "文章");
        tab_order.setVisibility(View.GONE);
        vp_order.setAdapter(adapter);
        tab_order.setCustomTabView(this);
        tab_order.setViewPager(vp_order);
    }
    
    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        Resources res = container.getContext().getResources();
        View tab = inflater.inflate(R.layout.item_tab, container, false);
        TextView value = tab.findViewById(R.id.custom_text);
        value.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        //        ColorStateList csl = getResources().getColorStateList(R.color.text_white_black_selector);
        //        value.setTextColor(csl);
        switch (position) {
            case 0:
                value.setText(res.getString(R.string.collect_class));
                break;
            case 1:
                value.setText(res.getString(R.string.teacher));
                break;
            case 2:
                value.setText(res.getString(R.string.article));
                break;
        }
        return tab;
    }
}
