package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.list.LiveSccUserWrongQuestionListResponse;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.ui.homework.CorrectExamActivity;
import com.haoke91.a91edu.widget.flowlayout.FlowLayout;
import com.haoke91.a91edu.widget.flowlayout.TagAdapter;
import com.haoke91.a91edu.widget.flowlayout.TagFlowLayout;
import com.haoke91.baselibrary.views.CenterDialog;
import com.haoke91.baselibrary.views.TipDialog;

import java.util.Arrays;

import me.drakeet.multitype.ItemViewBinder;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/8/6 10:54
 */
public class FillBlankExamProvider extends ItemViewBinder<LiveSccUserWrongQuestionListResponse.ListData, FillBlankExamProvider.ViewHolder> {
    
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_exam_fillblank, null);
        return new ViewHolder(view);
    }
    
    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final LiveSccUserWrongQuestionListResponse.ListData item) {
        holder.deleteBtn.setOnClickListener(clickListener);
        holder.deleteBtn.setTag(item.getId());
        holder.lookOrEditReasonBtn.setOnClickListener(clickListener);
        holder.correctBtn.setOnClickListener(clickListener);
        holder.cb_showAnalyzeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.cb_showAnalyzeBtn.setText("收起解析");
                    holder.ll_answerArea.setVisibility(View.VISIBLE);
                    holder.ll_answerArea.animate().translationY(1).setDuration(600).start();
                } else {
                    holder.cb_showAnalyzeBtn.setText("答案解析");
                    holder.ll_answerArea.setVisibility(View.GONE);
                    holder.ll_answerArea.animate().translationY(0).setDuration(600).start();
                }
            }
        });
        holder.cb_correctionBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.ll_correctRecordArea.setVisibility(View.VISIBLE);
                    holder.ll_correctRecordArea.animate().translationY(1).setDuration(600).start();
                } else {
                    holder.ll_correctRecordArea.setVisibility(View.GONE);
                    holder.ll_correctRecordArea.animate().translationY(0).setDuration(600).start();
                }
            }
        });
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.cb_correctionBtn.getContext()));
        //        holder.recyclerView.setAdapter(new MultiTypeAdapter(Arrays.asList("1","2","3")));
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        private View deleteBtn, lookOrEditReasonBtn, correctBtn;
        private CheckBox cb_showAnalyzeBtn, cb_correctionBtn;
        private View ll_answerArea, ll_correctRecordArea;
        private RecyclerView recyclerView;
        
        public ViewHolder(View itemView) {
            super(itemView);
            deleteBtn = itemView.findViewById(R.id.tv_delete);
            lookOrEditReasonBtn = itemView.findViewById(R.id.tv_lookOrEditReason);
            correctBtn = itemView.findViewById(R.id.tv_CorrectBtn);
            cb_showAnalyzeBtn = itemView.findViewById(R.id.cb_showAnalyzeBtn);
            cb_correctionBtn = itemView.findViewById(R.id.cb_correctionBtn);
            ll_answerArea = itemView.findViewById(R.id.ll_answerArea);
            ll_correctRecordArea = itemView.findViewById(R.id.correctRecordArea);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }
    
    private ClickListener clickListener = new ClickListener();
    
    private class ClickListener implements View.OnClickListener {
        
        
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_delete) {//删除
                TipDialog dialog = new TipDialog(v.getContext());
                dialog.setTextDes("确认删除这道题？\n删除后无法恢复");
                dialog.setTextDesSize(14);
                dialog.setButton1Color(Color.parseColor("#75C82B"));
                dialog.setButton1("确认", new TipDialog.DialogButtonOnClickListener() {
                    @Override
                    public void onClick(View button, TipDialog dialog) {
                        dialog.dismiss();
                    }
                });
                dialog.setButton2Color(Color.parseColor("#363636"));
                dialog.setButton2("取消", new TipDialog.DialogButtonOnClickListener() {
                    @Override
                    public void onClick(View button, TipDialog dialog) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } else if (v.getId() == R.id.tv_lookOrEditReason) {
                mWrongReasonDialog
                    = new WrongReasonDialog(v.getContext(), R.layout.dialog_wrongreasonsummary);
                mWrongReasonDialog.show();
                WindowManager.LayoutParams attr = mWrongReasonDialog.getWindow().getAttributes();
                attr.width = (int) (v.getResources().getDisplayMetrics().widthPixels - 40 * 2 * v.getResources().getDisplayMetrics().density);
                //                attr.horizontalMargin = 40 * v.getResources().getDisplayMetrics().density;
                mWrongReasonDialog.getWindow().setAttributes(attr);
            } else if (v.getId() == R.id.cancelBtn || v.getId() == R.id.okBtn) {
                if (mWrongReasonDialog != null && mWrongReasonDialog.isShowing()) {
                    mWrongReasonDialog.dismiss();
                }
            } else if (v.getId() == R.id.tv_CorrectBtn) {
                v.getContext().startActivity(new Intent(v.getContext(), CorrectExamActivity.class));
            }
        }
    }
    
    private CenterDialog mWrongReasonDialog;
    
    /**
     * 错题原因分析
     */
    private class WrongReasonDialog extends CenterDialog {
        
        public WrongReasonDialog(Context context, int layout) {
            super(context, layout);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        
        @Override
        public void initView(final View view) {
            TagFlowLayout flowLayout = view.findViewById(R.id.tagFlowLawout_reason);
            final EditText et_input = view.findViewById(R.id.et_inputReason);
            final View cancelBtn = view.findViewById(R.id.cancelBtn);
            final View okBtn = view.findViewById(R.id.okBtn);
            cancelBtn.setOnClickListener(clickListener);
            okBtn.setOnClickListener(clickListener);
            flowLayout.setAdapter(new TagAdapter<String>(Arrays.asList("题目抄错", "题目不明白", "就是猜的啊")) {
                @Override
                public View getView(FlowLayout parent, final int position, String s) {
                    TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_select, parent, false);
                    v.setText(s);
                    return v;
                }
            });
            flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    view.setSelected(!view.isSelected());
                    return true;
                }
            });
        }
    }
}
