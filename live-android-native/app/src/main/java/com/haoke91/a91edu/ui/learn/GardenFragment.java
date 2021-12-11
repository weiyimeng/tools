package com.haoke91.a91edu.ui.learn;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.fragment.BaseFragment;
import com.haoke91.a91edu.widget.HomeCoursePopup;
import com.haoke91.baselibrary.recycleview.HorizontalDividerItemDecoration;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/26 下午5:54
 * 修改人：weiyimeng
 * 修改时间：2018/7/26 下午5:54
 * 修改备注：
 */
public class GardenFragment extends BaseFragment {
    private static final String key = "keyword";
    private List<DictionaryDomain> primary;
    private List<DictionaryDomain> middle;
    private List<DictionaryDomain> high;
    private List<DictionaryDomain> all;
    
    public static GardenFragment instance() {
        return new GardenFragment();
    }
    
    @Override
    public int getLayout() {
        return R.layout.layout_home_pop;
    }
    //    {"code":"SUCCESS","data":{"list":[{"dicName":"一年级","dicTypeValue":"grade","dicValue":"1"},{"dicName":"二年级","dicTypeValue":"grade","dicValue":"2"},{"dicName":"三年级","dicTypeValue":"grade","dicValue":"3"},{"dicName":"四年级","dicTypeValue":"grade","dicValue":"4"},{"dicName":"五年级","dicTypeValue":"grade","dicValue":"5"},{"dicName":"六年级","dicTypeValue":"grade","dicValue":"6"},{"dicName":"初一","dicTypeValue":"grade","dicValue":"7"},{"dicName":"初二","dicTypeValue":"grade","dicValue":"8"},{"dicName":"初三","dicTypeValue":"grade","dicValue":"9"},{"dicName":"高一","dicTypeValue":"grade","dicValue":"10"},{"dicName":"高二","dicTypeValue":"grade","dicValue":"11"},{"dicName":"高三","dicTypeValue":"grade","dicValue":"12"}]},"msg":"成功"}
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //        all = Arrays.asList("全部课程");
        all = new Gson().fromJson("[{\"dicName\":\"全部年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"0\"}]", new TypeToken<List<DictionaryDomain>>() {
        }.getType());
        String pri = "[{\"dicName\":\"一年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"1\"},{\"dicName\":\"二年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"2\"},{\"dicName\":\"三年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"3\"},{\"dicName\":\"四年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"4\"},{\"dicName\":\"五年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"5\"},{\"dicName\":\"六年级\",\"dicTypeValue\":\"grade\",\"dicValue\":\"6\"}]";
        primary = new Gson().fromJson(pri, new TypeToken<List<DictionaryDomain>>() {
        }.getType());
        middle = new Gson().fromJson("[{\"dicName\":\"初一\",\"dicTypeValue\":\"grade\",\"dicValue\":\"7\"},{\"dicName\":\"初二\",\"dicTypeValue\":\"grade\",\"dicValue\":\"8\"},{\"dicName\":\"初三\",\"dicTypeValue\":\"grade\",\"dicValue\":\"9\"}]", new TypeToken<List<DictionaryDomain>>() {
        }.getType());
        high = new Gson().fromJson("[{\"dicName\":\"高一\",\"dicTypeValue\":\"grade\",\"dicValue\":\"10\"},{\"dicName\":\"高二\",\"dicTypeValue\":\"grade\",\"dicValue\":\"11\"},{\"dicName\":\"高三\",\"dicTypeValue\":\"grade\",\"dicValue\":\"12\"}]", new TypeToken<List<DictionaryDomain>>() {
        }.getType());
        //        [{"dicName":"一年级","dicTypeValue":"grade","dicValue":"1"},{"dicName":"二年级","dicTypeValue":"grade","dicValue":"2"},{"dicName":"三年级","dicTypeValue":"grade","dicValue":"3"},{"dicName":"四年级","dicTypeValue":"grade","dicValue":"4"},{"dicName":"五年级","dicTypeValue":"grade","dicValue":"5"},{"dicName":"六年级","dicTypeValue":"grade","dicValue":"6"}]
        //        primary = Arrays.asList(Utils.getApp().getResources().getStringArray(R.array.primary_garden));
        //        middle = Arrays.asList(Utils.getApp().getResources().getStringArray(R.array.middle_garden));
        //        high = Arrays.asList(Utils.getApp().getResources().getStringArray(R.array.high_garden));
        RadioGroup   rg_choice_garden = view.findViewById(R.id.rg_choice_garden);
        RecyclerView mRecyclerView = view.findViewById(R.id.rv_list);
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
            
            if (StringUtils.equals(lastText, item.getDicName())) {
                textView.setTextColor(Color.parseColor("#75c82b"));
            } else {
                textView.setTextColor(Color.parseColor("#363636"));
            }
            helper.getView(R.id.tv_course).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                    // if (!ObjectUtils.isEmpty(onViewClickListener)) {
                    //  dismiss();
                    if (StringUtils.equals(lastText, item.getDicName())) {
                        return;
                    }
                    ((TextView) v).setTextColor(Color.parseColor("#75c82b"));
                    if (lastView != null && lastView != v) {
                        lastView.setTextColor(Color.parseColor("#363636"));
                    }
                    lastView = (TextView) v;
                    lastText = item.getDicName();
                    if (mOnItemSelectedClickListener != null) {
                        v.setTag(item);
                        mOnItemSelectedClickListener.onItemSelected(v, 3, position);
                    }
                    //    onViewClickListener.onVideoClick(sourse.get(position));
                    //    }
                }
            });
            
        }
    }
    
    private DropDownFragment.OnItemSelectedClickListener mOnItemSelectedClickListener;
    
    //    public interface OnItemSelectedClickListener {
    //        void onItemSelected(View v, int position);
    //    }
    
    public GardenFragment setOnItemClickListener(DropDownFragment.OnItemSelectedClickListener onItemClickListener, int tag) {
        mOnItemSelectedClickListener = onItemClickListener;
        return this;
    }
}
