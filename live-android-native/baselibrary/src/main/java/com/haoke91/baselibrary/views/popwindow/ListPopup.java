package com.haoke91.baselibrary.views.popwindow;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.blankj.utilcode.util.ObjectUtils;
import com.haoke91.baselibrary.R;
import com.haoke91.baselibrary.model.VideoUrl;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;
import com.haoke91.baselibrary.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyyoona7 on 2017/8/7.
 */

public class ListPopup extends BasePopup<ListPopup> {
    
    
    public static ListPopup create() {
        return new ListPopup();
    }
    
    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_list_popwindow);
        List<VideoUrl> list = sourse;
        setHeight((list.size()) * DensityUtil.dip2px(mContext, 25));
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusAndOutsideEnable(true);
    }
    
    @Override
    protected void initViews(View view) {
        RecyclerView mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        List<VideoUrl> list = sourse;
        PopListAdapter adapter = new PopListAdapter(mContext);
        adapter.addAll(list);
        mRecyclerView.setAdapter(adapter);
    }
    
    
    private ArrayList<VideoUrl> sourse;
    
    public ListPopup setPlayInfo(ArrayList<VideoUrl> sourc) {
        if (sourc == null) {
            sourc = new ArrayList<>();
        }
        this.sourse = sourc;
        return this;
    }
    
    private TextView lastResolutionView;
    
    public class PopListAdapter extends QuickWithPositionAdapter<VideoUrl> {
        
        
        public PopListAdapter(Context context) {
            super(context, R.layout.item_video_speed);
        }
        
        @Override
        protected void convert(BaseAdapterHelper helper, final VideoUrl item, final int position) {
            helper.setText(R.id.tv_speed_5, item.getFormatName());
            if (position == 0 && ObjectUtils.isEmpty(lastResolutionView)) {
                lastResolutionView = helper.getTextView(R.id.tv_speed_5);
                lastResolutionView.setTextColor(Color.parseColor("#00ff00"));
            }
            helper.getView(R.id.tv_speed_5).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                    if (!ObjectUtils.isEmpty(onViewClickListener)) {
                        dismiss();
                        if (lastResolutionView == v) {
                            return;
                        }
                        lastResolutionView.setTextColor(Color.parseColor("#000000"));
                        lastResolutionView = (TextView) v;
                        lastResolutionView.setTextColor(Color.parseColor("#00ff00"));
                        onViewClickListener.onVideoClick(sourse.get(position));
                    }
                }
            });
            
        }
    }
    
    public ListPopup setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
        return this;
    }
    
    private OnViewClickListener onViewClickListener;
    
    public interface OnViewClickListener {
        void onVideoClick(VideoUrl videoUrl);
    }
    
}
