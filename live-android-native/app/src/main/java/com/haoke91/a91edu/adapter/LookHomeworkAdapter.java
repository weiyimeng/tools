package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.ImageViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haoke91.a91edu.R;
import com.haoke91.a91edu.utils.imageloader.GlideUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/19 下午2:36
 * 修改人：weiyimeng
 * 修改时间：2018/7/19 下午2:36
 * 修改备注：
 */
public class LookHomeworkAdapter extends BasePagerAdapter<String> {
    
    public LookHomeworkAdapter(Context mContext, List<String> dates){
        super(mContext, R.layout.item_look_homework, dates);
    }
    
    public void setData(List<String> list){
        if (list == null){
            this.dates.clear();
        } else{
            this.dates = list;
        }
        notifyDataSetChanged();
    }
    
    public List<String> getData(){
        return this.dates;
    }
    
    public void addData(List<String> list){
    
    }
    
    @Override
    protected void convert(View view, String item,int position){
        ImageView imageView = view.findViewById(R.id.iv_homework);
        GlideUtils.load(context, item, imageView);
        
    }
    
}
