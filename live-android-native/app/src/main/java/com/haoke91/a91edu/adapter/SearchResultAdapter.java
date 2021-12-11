package com.haoke91.a91edu.adapter;

import android.content.Context;

import com.haoke91.a91edu.R;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/16 下午4:56
 * 修改人：weiyimeng
 * 修改时间：2018/7/16 下午4:56
 * 修改备注：
 */
public class SearchResultAdapter extends QuickWithPositionAdapter<String> {
    public SearchResultAdapter(Context context, List datas) {
        super(context, R.layout.item_search_course, datas);
    }
    
    @Override
    protected void convert(BaseAdapterHelper helper, String item, int position) {
    
    }
}
