package com.haoke91.a91edu.ui.learn;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gaosiedu.live.sdk.android.domain.DictionaryDomain;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.fragment.BaseFragment;
import com.haoke91.a91edu.widget.FullyLinearLayoutManager;
import com.haoke91.a91edu.widget.dropdownlayout.MenuLayout;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;

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
public class DropDownFragment extends BaseFragment {
    private static final String key = "keyword";
    private OnItemSelectedClickListener mOnItemSelectedClickListener;
    private int CURRENTTAG = 0;
    private MenuLayout mMenuLayout;
    
    public static DropDownFragment instance(List<String> dates) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(dates);
        DropDownFragment fragment = new DropDownFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("", list);
        fragment.setArguments(bundle);
        return fragment;
    }
    
    public static DropDownFragment getInstance(ArrayList<DictionaryDomain> courseInfo) {
        DropDownFragment fragment = new DropDownFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, courseInfo);
        fragment.setArguments(bundle);
        return fragment;
    }
    
    @Override
    public int getLayout() {
        return R.layout.fragment_search_recommend;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        WrapRecyclerView  rv_search_recommend = view.findViewById(R.id.rv_search_recommend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_search_recommend.setLayoutManager(linearLayoutManager);
        ArrayList<DictionaryDomain> dates = (ArrayList<DictionaryDomain>) getArguments().getSerializable(key);
        Adapter adapter = new Adapter(mContext, dates);
        rv_search_recommend.setAdapter(adapter);
    }
    
    private TextView lastView;
    
    class Adapter extends QuickWithPositionAdapter<DictionaryDomain> {
        public Adapter(Context context, List dates) {
            super(context, R.layout.item_home_course, dates);
        }
        
        @Override
        protected void convert(BaseAdapterHelper helper, final DictionaryDomain item, final int position) {
            final TextView textView = helper.getTextView(R.id.tv_course);
            textView.setText(item.getDicName());
            
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastView == v) {
                        return;
                    }
                    textView.setTextColor(Color.parseColor("#75c82b"));
                    textView.setBackgroundColor(Color.parseColor("#fbfbfb"));
                    if (lastView != null) {
                        lastView.setTextColor(Color.parseColor("#363636"));
                        lastView.setBackgroundColor(Color.parseColor("#ffffff"));
                        
                    }
                    if (mOnItemSelectedClickListener != null) {
                        v.setTag(item);
                        mOnItemSelectedClickListener.onItemSelected(v, CURRENTTAG, position);
                    }
                    lastView = textView;
                }
            });
            
        }
    }
    
    public interface OnItemSelectedClickListener {
        void onItemSelected(View v, int tag, int position);
    }
    
    public DropDownFragment setOnItemClickListener(OnItemSelectedClickListener onItemClickListener, int tag) {
        mOnItemSelectedClickListener = onItemClickListener;
        this.CURRENTTAG = tag;
        return this;
    }
}
