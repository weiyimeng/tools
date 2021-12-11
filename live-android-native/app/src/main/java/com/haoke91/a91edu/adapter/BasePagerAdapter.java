package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.utils.imageloader.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/26 上午10:35
 * 修改人：weiyimeng
 * 修改时间：2018/7/26 上午10:35
 * 修改备注：
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter {
    protected List<T> dates;
    private int layoutId;
    protected Context context;
    
    public BasePagerAdapter(Context context, int layoutId, List<T> dates) {
        this.dates = dates;
        this.layoutId = layoutId;
        this.context = context;
    }
    
    public void setDates(List<T> dates) {
        this.dates = dates;
//        notifyDataSetChanged();
    }
    
    public List<T> getAll() {
        return ObjectUtils.isEmpty(dates) ? new ArrayList<T>() : dates;
    }
    
    @Override
    public int getCount() {
        return dates.size();
    }
    
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(layoutId, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPagerItemClickListener != null) {
                    onPagerItemClickListener.onPagerItemClick(position);
                }
            }
        });
        convert(view, dates.get(position),position);
        container.addView(view);
        return view;
    }
    
    protected abstract void convert(View view, T item,int position);
    
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
        
    }
    
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
    
    public void setOnPagerItemClickListener(OnPagerItemClickListener onPagerItemClickListener) {
        this.onPagerItemClickListener = onPagerItemClickListener;
    }
    
    private OnPagerItemClickListener onPagerItemClickListener;
    
    public interface OnPagerItemClickListener {
        void onPagerItemClick(int position);
    }
}
