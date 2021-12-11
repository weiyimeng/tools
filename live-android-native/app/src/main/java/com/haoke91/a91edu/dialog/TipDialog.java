package com.haoke91.a91edu.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.haoke91.a91edu.R;
import com.haoke91.baselibrary.utils.ICallBack;
import com.haoke91.baselibrary.views.CenterDialog;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/12/23 18:15
 */
public class TipDialog extends CenterDialog {
    
    String mDesc, mLeft, mRight;
    ICallBack<Integer, Integer> mICallBack;
    
    public TipDialog(Context context, int layout, String desc, String left, String right, ICallBack<Integer, Integer> callBack) {
        super(context, layout);
        this.mDesc = desc;
        this.mLeft = left;
        this.mRight = right;
        mICallBack = callBack;
    }
    
    @Override
    public void initView(View view) {
        TextView tvDesc = view.findViewById(R.id.tv_des);
        TextView tv_left = view.findViewById(R.id.btn_2);
        TextView tv_right = view.findViewById(R.id.btn_1);
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mICallBack != null){
                    mICallBack.call(0, 1);
                }
                dismiss();
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mICallBack != null){
                    mICallBack.call(0, 0);
                }
                dismiss();
            }
        });
    }
}
