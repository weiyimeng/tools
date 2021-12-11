package com.haoke91.a91edu.widget.exam;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoke91.a91edu.R;
import com.haoke91.a91edu.widget.flowlayout.FlowLayout;
import com.haoke91.a91edu.widget.flowlayout.TagAdapter;
import com.haoke91.a91edu.widget.flowlayout.TagFlowLayout;
import com.haoke91.baselibrary.utils.DensityUtil;
import com.haoke91.baselibrary.views.CenterDialog;

import java.util.Arrays;
import java.util.List;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/8/8 10:54
 */
public class ChoiceExamView extends FrameLayout {
    
    private TextView mExamHead;//题干
    //    private TextView mOptA, mOptB, mOptC, mOptD, mOptE;//选项
    private TagFlowLayout mTagLayout;
    private String[] optPrefixes = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"};
    private TextView[] mOptViews = null;
    private boolean[] answer = null;
    private String[] options = null;
    private ViewGroup mOptionContainer;
    private boolean isMultiSelect = false;
    
    public ChoiceExamView(@NonNull Context context){
        super(context, null);
        init(context);
    }
    
    public ChoiceExamView(@NonNull Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    
    private void init(Context context){
        View content = LayoutInflater.from(context).inflate(R.layout.layout_exam_choice, null);
        addView(content);
        mExamHead = content.findViewById(R.id.tv_exam_head);
        mTagLayout = content.findViewById(R.id.exam_tag);
        mOptionContainer = content.findViewById(R.id.mOptionContainer);
        //        mOptA = content.findViewById(R.id.tv_optA);
        //        mOptB = content.findViewById(R.id.tv_optB);
        //        mOptC = content.findViewById(R.id.tv_optC);
        //        mOptD = content.findViewById(R.id.tv_optD);
        //        mOptE = content.findViewById(R.id.tv_optE);
        //        mOptViews = new TextView[]{mOptA, mOptB, mOptC, mOptD, mOptE};
    }
    
    private TextView newOption(){
        TextView opt = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        opt.setPadding(0, DensityUtil.dip2px(getContext(), 4), 0, DensityUtil.dip2px(getContext(), 4));
        //        opt.setTextColor(ContextCompat.getColor(getContext(), R.color.color_75C82B));
        opt.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.text_363636_75c82b_selector));
        opt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        return opt;
    }
    
    /**
     * 设置题干
     *
     * @param content
     */
    public void setExamHead(String content){
        if (mExamHead != null){
            mExamHead.setText("题干：" + content);
        }
    }
    
    /**
     * 设置选项
     *
     * @param isMulti 是否多选
     * @param opts
     */
    public void setOptions(boolean isMulti, boolean canClick, String... opts){
        if (opts == null || opts.length == 0){
            return;
        }
        isMultiSelect = isMulti;
        int length = opts.length;
        mOptViews = new TextView[length];
        answer = new boolean[length];
        options = opts;
        if (mOptionContainer.getChildCount() > 0){
            mOptionContainer.removeAllViews();
        }
        for (int i = 0; i < length; i++) {
            mOptViews[i] = newOption();
            mOptViews[i].setTag(i);
            mOptionContainer.addView(mOptViews[i]);
            String res = "<strong>" + optPrefixes[i] + "</strong>&nbsp;&nbsp;" + opts[i];
            mOptViews[i].setText(Html.fromHtml(res));
        }
        //点击时间
        if (canClick){
            for (TextView v : mOptViews) {
                v.setOnClickListener(new ClickEvent());
            }
        }
        
    }
    
    /**
     * 点击触发
     */
    private class ClickEvent implements OnClickListener {
        @Override
        public void onClick(View v){
            for (TextView i : mOptViews) {
                if (i != v){
                    if (!isMultiSelect){
                        i.setSelected(false);
                        answer[(int) i.getTag()] = false;
                        i.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363636));
                    }
                } else{
                    i.setSelected(!i.isSelected());
                    answer[(int) i.getTag()] = i.isSelected();
                    if (i.isSelected()){
                        i.setTextColor(ContextCompat.getColor(getContext(), R.color.color_75C82B));
                    } else{
                        i.setTextColor(ContextCompat.getColor(getContext(), R.color.color_363636));
                    }
                }
            }
        }
    }
    
    /**
     * 获取选择答案
     *
     * @return
     */
    public boolean[] getAnswerPosition(){
        return answer;
    }
    
    /**
     * 获取选项
     *
     * @return
     */
    public String[] getOptions(){
        return options;
    }
    
    /**
     * 设置标签
     *
     * @param tags
     */
    public void setTags(List<String> tags){
        if (tags == null || tags.size() == 0){
            mTagLayout.setVisibility(GONE);
            return;
        }
        if (tags.get(0) == null || tags.get(0).equals("")){
            mTagLayout.setVisibility(GONE);
            return;
        }
        mTagLayout.setAdapter(new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, String s){
                TextView tag = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_tag_select, parent, false);
                tag.setBackgroundResource(R.drawable.bg_tag_green_20);
                tag.setText(s);
                return tag;
            }
        });
        mTagLayout.setVisibility(VISIBLE);
        //        mTagLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
        //            @Override
        //            public boolean onTagClick(View view, int position, FlowLayout parent){
        //                if (position == 2){
        //                    MoreTagDialog dialog = new MoreTagDialog(view.getContext(), R.layout.layout_morewrongreason);
        //                    dialog.show();
        //                    WindowManager.LayoutParams attr = dialog.getWindow().getAttributes();
        //                    attr.width = (int) (view.getResources().getDisplayMetrics().widthPixels - 40 * 2 * view.getResources().getDisplayMetrics().density);
        //                    //                attr.horizontalMargin = 40 * v.getResources().getDisplayMetrics().density;
        //                    dialog.getWindow().setAttributes(attr);
        //                }
        //                return false;
        //            }
        //        });
    }
    
    private class MoreTagDialog extends CenterDialog {
        
        public MoreTagDialog(Context context, int layout){
            super(context, layout);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        
        @Override
        public void initView(View view){
            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    dismiss();
                }
            });
            TagFlowLayout tagFlowLayout = view.findViewById(R.id.tagFlowLawout_reason);
            tagFlowLayout.setAdapter(new TagAdapter<String>(Arrays.asList("1", "2", "3", "4")) {
                @Override
                public View getView(FlowLayout parent, int position, String s){
                    TextView tag = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_tag_select, parent, false);
                    tag.setBackgroundResource(R.drawable.bg_tag_green_20);
                    tag.setText(s);
                    return tag;
                }
            });
            
        }
    }
}
