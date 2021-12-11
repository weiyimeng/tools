package com.haoke91.a91edu.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haoke91.a91edu.R;
import com.haoke91.a91edu.widget.flowlayout.FlowLayout;
import com.haoke91.a91edu.widget.flowlayout.TagAdapter;
import com.haoke91.a91edu.widget.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/8/8 10:54
 */
public class AnswerAndQuestionExamView extends FrameLayout {
    
    private TextView mExamHead;//题干
    private TagFlowLayout mTagLayout;
    private String[] optPrefixes = {"A", "B", "C", "D", "E", "F"};
    private TextView[] mOptViews = null;
    
    public AnswerAndQuestionExamView(@NonNull Context context) {
        super(context, null);
        init(context);
    }
    
    public AnswerAndQuestionExamView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    private void init(Context context) {
        View content = LayoutInflater.from(context).inflate(R.layout.layout_exam_answerandquestion, null);
        addView(content);
        mExamHead = content.findViewById(R.id.tv_examHead);
        mTagLayout = content.findViewById(R.id.tagLayout);
    }
    
    /**
     * 设置题干
     *
     * @param content
     */
    public void setExamHead(String content) {
        if (mExamHead != null) {
            mExamHead.setText("题干：" + content);
        }
    }
    
    //    /**
    //     * 设置选项
    //     *
    //     * @param opts
    //     */
    //    public void setOptions(String... opts) {
    //        if (opts == null || opts.length == 0) {
    //            return;
    //        }
    //        int length = opts.length > 6 ? 6 : opts.length;
    //        for (int i = 0; i < length; i++) {
    //            mOptViews[i].setText(optPrefixes[i] + "  " + opts[i]);
    //        }
    //
    //    }
    
    /**
     * 设置标签
     *
     * @param tags
     */
    public void setTags(List<String> tags) {
        if (tags == null && tags.size() == 0) {
            mTagLayout.setVisibility(GONE);
            return;
        }
        mTagLayout.setAdapter(new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tag = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_tag_select, parent, false);
                tag.setBackgroundResource(R.drawable.bg_tag_green_20);
                tag.setText(s);
                return tag;
            }
        });
        mTagLayout.setVisibility(VISIBLE);
    }
}
