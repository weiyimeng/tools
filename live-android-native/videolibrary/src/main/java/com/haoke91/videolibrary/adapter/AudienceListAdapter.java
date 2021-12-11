package com.haoke91.videolibrary.adapter;

import android.content.Context;
import android.widget.FrameLayout;

import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;
import com.haoke91.videolibrary.R;
import com.haoke91.videolibrary.model.AudienceBean;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/6/21 下午3:49
 * 修改人：weiyimeng
 * 修改时间：2018/6/21 下午3:49
 * 修改备注：
 */
public class AudienceListAdapter extends QuickWithPositionAdapter<AudienceBean> {

    public AudienceListAdapter(Context context) {
        super(context, R.layout.item_audicnce_list);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, AudienceBean item, int position) {
        if (((FrameLayout) helper.itemView).getChildCount() == 0) {
            item.surfaceView.setZOrderOnTop(true);
            ((FrameLayout) helper.itemView).addView(item.surfaceView);
        }
    }

}
