package com.haoke91.baselibrary.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.haoke91.baselibrary.R;
import com.haoke91.baselibrary.utils.DensityUtil;
import com.haoke91.baselibrary.views.popwindow.BasePopup;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/12/11 20:11
 */
public class RightListDialog extends BasePopup<RightListDialog> {
    
    
    public static RightListDialog create() {
        return new RightListDialog();
    }
    
    @Override
    protected void initAttributes() {
        setContentView(R.layout.dialog_fillblank);
        setHeight(DensityUtil.dip2px(mContext, 270));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusAndOutsideEnable(true);
        setBackgroundDimEnable(false);
        setAnimationStyle(R.style.BottomDialogStyle);
    }
    
    @Override
    protected void initViews(View view) {
    
    }
}
