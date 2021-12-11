package com.haoke91.a91edu.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haoke91.a91edu.R;
import com.haoke91.baselibrary.model.VideoUrl;
import com.haoke91.baselibrary.recycleview.HorizontalDividerItemDecoration;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;
import com.haoke91.baselibrary.utils.DensityUtil;
import com.haoke91.baselibrary.views.popwindow.BasePopup;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.List;


/**
 * Created by zyyoona7 on 2017/8/7.
 */

public class HomeCoursePopup extends BasePopup<HomeCoursePopup> {
    
    
    public static HomeCoursePopup create() {
        return new HomeCoursePopup();
    }
    
    private List<DictionaryDomain> primary;
    private List<DictionaryDomain> middle;
    private List<DictionaryDomain> high;
    private List<DictionaryDomain> all;
    
    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_home_pop);
        setHeight(DensityUtil.dip2px(mContext, 270));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusAndOutsideEnable(true);
        setBackgroundDimEnable(false);
        setAnimationStyle(R.style.TopPopAnim);
        //        all = Arrays.asList("全部课程");
        //        primary = Arrays.asList(Utils.getApp().getResources().getStringArray(R.array.primary_garden));
        //        middle = Arrays.asList(Utils.getApp().getResources().getStringArray(R.array.middle_garden));
        //        high = Arrays.asList(Utils.getApp().getResources().getStringArray(R.array.high_garden));
        
        
        all = new Gson().fromJson("[{\"dicName\":\"全部年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"0\"}]", new TypeToken<List<DictionaryDomain>>() {
        }.getType());
        String pri = "[{\"dicName\":\"一年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"1\"},{\"dicName\":\"二年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"2\"},{\"dicName\":\"三年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"3\"},{\"dicName\":\"四年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"4\"},{\"dicName\":\"五年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"5\"},{\"dicName\":\"六年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"6\"}]";
        primary = new Gson().fromJson(pri, new TypeToken<List<DictionaryDomain>>() {
        }.getType());
        middle = new Gson().fromJson("[{\"dicName\":\"初一\",\"dicTypeValue\":\"grade\",\"dicValue\":\"7\"},{\"dicName\":\"初二\",\"dicTypeValue\":\"grade\",\"dicValue\":\"8\"},{\"dicName\":\"初三\",\"dicTypeValue\":\"grade\",\"dicValue\":\"9\"}]", new TypeToken<List<DictionaryDomain>>() {
        }.getType());
        high = new Gson().fromJson("[{\"dicName\":\"高一\",\"dicTypeValue\":\"grade\",\"dicValue\":\"10\"},{\"dicName\":\"高二\",\"dicTypeValue\":\"grade\",\"dicValue\":\"11\"},{\"dicName\":\"高三\",\"dicTypeValue\":\"grade\",\"dicValue\":\"12\"}]", new TypeToken<List<DictionaryDomain>>() {
        }.getType());
        
    }
    
    @Override
    protected void initViews(View view) {
        RadioGroup rg_choice_garden = findViewById(R.id.rg_choice_garden);
        RecyclerView mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        final PopListAdapter adapter = new PopListAdapter(mContext);
        adapter.setData(all);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).color(
            mContext.getResources().getColor(R.color.FBFBFB))
            .size((int) mContext.getResources().getDimension(R.dimen.dp_2))
            .build());
        rg_choice_garden.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_garden_all:
                        adapter.setData(all);
                        break;
                    case R.id.rb_garden_primary:
                        adapter.setData(primary);
                        break;
                    case R.id.rb_garden_middle:
                        adapter.setData(middle);
                        break;
                    case R.id.rb_garden_high:
                        adapter.setData(high);
                        break;
                }
            }
        });
    }
    
    
    private TextView lastView;
    private String lastText;
    
    public class PopListAdapter extends QuickWithPositionAdapter<DictionaryDomain> {
        
        
        public PopListAdapter(Context context) {
            super(context, R.layout.item_home_course);
        }
        
        @Override
        protected void convert(BaseAdapterHelper helper, final DictionaryDomain item, final int position) {
            TextView textView = helper.getTextView(R.id.tv_course);
            textView.setText(item.getDicName());
            
            if (null != lastView && StringUtils.equals(lastText, item.getDicName())) {
                Logger.e("lastView.getText()===" + lastView.getText());
                textView.setTextColor(Color.parseColor("#75c82b"));
            } else {
                textView.setTextColor(Color.parseColor("#363636"));
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (StringUtils.equals(lastText, item.getDicName())) {
                        return;
                    }
                    if (onViewClickListener != null) {
                        onViewClickListener.onVideoClick(item);
                    }
                    ((TextView) v).setTextColor(Color.parseColor("#75c82b"));
                    if (lastView != null && lastView != v) {
                        lastView.setTextColor(Color.parseColor("#363636"));
                    }
                    lastView = (TextView) v;
                    lastText = item.getDicName();
                }
            });
            
        }
    }
    
    public HomeCoursePopup setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
        return this;
    }
    
    private OnViewClickListener onViewClickListener;
    
    public interface OnViewClickListener {
        void onVideoClick(DictionaryDomain item);
    }
    
}
