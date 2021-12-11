package com.haoke91.a91edu.widget.exam;

import android.content.Context;
import android.service.quicksettings.Tile;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haoke91.a91edu.R;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/8/29 3:19
 */
public class FillBlankExamView extends FrameLayout {
    
    private TextView tv_head;
    private EditText et_inputReason;
    private TextView tvTip_tag;
    
    public FillBlankExamView(@NonNull Context context){
        super(context);
        init(context);
    }
    
    public FillBlankExamView(@NonNull Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    
    private void init(Context context){
        View content = LayoutInflater.from(context).inflate(R.layout.layout_exam_fillblank, null);
        addView(content);
        tv_head = content.findViewById(R.id.tv_exam_head);
        et_inputReason = content.findViewById(R.id.et_inputReason);
        tvTip_tag = content.findViewById(R.id.tv_tip_tag);
    }
    
    /**
     * 设置题干
     *
     * @param head
     */
    public void setHead(String head){
        if (head != null && tv_head != null){
            tv_head.setText("题干：" + head);
        }
    }
    
    /**
     * 获取输入答案
     *
     * @return
     */
    public String getEditText(){
        return et_inputReason.getText().toString().trim();
    }
    
    /**
     * 设置编辑框是否可见
     *
     * @param visible
     */
    public void setEditAreaVisible(int visible, boolean onlyLook){
        et_inputReason.setVisibility(visible);
        if (onlyLook){
            tvTip_tag.setVisibility(GONE);
        } else{
            tvTip_tag.setVisibility(VISIBLE);
        }
    }
}
