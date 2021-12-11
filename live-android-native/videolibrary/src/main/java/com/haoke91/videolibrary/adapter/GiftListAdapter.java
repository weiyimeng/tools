package com.haoke91.videolibrary.adapter;

import android.content.Context;
import android.widget.FrameLayout;

import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;
import com.haoke91.videolibrary.R;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/6/21 下午3:49
 * 修改人：weiyimeng
 * 修改时间：2018/6/21 下午3:49
 * 修改备注：
 */
public class GiftListAdapter extends QuickWithPositionAdapter<String> {
    
    public GiftListAdapter(Context context) {
        super(context, R.layout.item_gift_list);
    }
    
    @Override
    protected void convert(BaseAdapterHelper helper, String item, int position) {
    
    }
    
}
