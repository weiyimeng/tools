package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.collect.LiveSccUserWrongQuestionCollectRequest;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.collect.LiveSccUserWrongQuestionCollectResponse;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.correct.list.LiveSccUserWrongQuestionCorrectListRequest;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.correct.list.LiveSccUserWrongQuestionCorrectListResponse;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.delete.LiveSccUserWrongQuestionDeleteRequest;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.delete.LiveSccUserWrongQuestionDeleteResponse;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.list.LiveSccUserWrongQuestionListResponse;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.reason.LiveSccUserWrongQuestionSaveReasonRequest;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.reason.LiveSccUserWrongQuestionSaveReasonResponse;
import com.gaosiedu.scc.sdk.android.domain.SccUserWrongQuestionCorrectBean;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.entities.Exam;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.homework.CorrectExamActivity;
import com.haoke91.a91edu.ui.learn.AnswerAnalysisActivity;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.exam.ChoiceExamView;
import com.haoke91.a91edu.widget.exam.FillBlankExamView;
import com.haoke91.a91edu.widget.flowlayout.FlowLayout;
import com.haoke91.a91edu.widget.flowlayout.TagAdapter;
import com.haoke91.a91edu.widget.flowlayout.TagFlowLayout;
import com.haoke91.baselibrary.views.CenterDialog;
import com.haoke91.baselibrary.views.TipDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/8/6 10:54
 */
public class ChoiceExamProvider extends ItemViewBinder<LiveSccUserWrongQuestionListResponse.ListData, ChoiceExamProvider.ViewHolder> {
    
    
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_layout_exam, null);
        return new ViewHolder(view);
    }
    
    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final LiveSccUserWrongQuestionListResponse.ListData item) {
        
        final Exam exam = new Exam();
        exam.setId(item.getId());
        exam.setQuestionType(item.getQuestionType());
        exam.setQuestionStem(item.getQuestionStem());
        exam.setQuestionContent(item.getQuestionContent());
        holder.deleteBtn.setTag(item.getId());
        holder.deleteBtn.setOnClickListener(new ClickLisen());
        holder.lookOrEditReasonBtn.setTag(item.getId());
        holder.correctBtn.setTag(exam);
        holder.lookOrEditReasonBtn.setOnClickListener(new ClickLisen());
        holder.correctBtn.setOnClickListener(new ClickLisen());
        holder.cb_showAnalyzeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //                if (isChecked){
                //                    holder.cb_showAnalyzeBtn.setText("收起解析");
                //                    holder.ll_answerArea.setVisibility(View.VISIBLE);
                //                    holder.ll_answerArea.animate().translationY(1).setDuration(600).start();
                //                } else{
                //                    holder.cb_showAnalyzeBtn.setText("答案解析");
                //                    holder.ll_answerArea.setVisibility(View.GONE);
                //                    holder.ll_answerArea.animate().translationY(0).setDuration(600).start();
                //                }
                AnswerAnalysisActivity.Companion.start(buttonView.getContext(), exam.id);
            }
        });
        //        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.mChoiceExamView.getContext()));
        //        final CorrectRecordAdapter recordAdapter = new CorrectRecordAdapter(holder.recyclerView.getContext());
        //        holder.recyclerView.setAdapter(recordAdapter);
        //        holder.recyclerView.setNestedScrollingEnabled(false);
        //        recordAdapter.setData(list);
        //        holder.cb_correctionBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        //            @Override
        //            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        //                if (isChecked){
        //                    networkForCorrectionList(item.getId(), holder.ll_correctRecordArea, recordAdapter);
        //                    //                    holder.ll_correctRecordArea.setVisibility(View.VISIBLE);
        //                    //                    holder.ll_correctRecordArea.animate().translationY(1).setDuration(600).start();
        //                    //                    recordAdapter.setData(list);
        //                } else{
        //                    holder.ll_correctRecordArea.setVisibility(View.GONE);
        //                    //                    holder.ll_correctRecordArea.animate().translationY(0).setDuration(600).start();
        //                    //                    recordAdapter.setData(null);
        //                }
        //            }
        //        });
        //是否收藏
        holder.isCollectedBtn.setSelected(1 == item.getCollect());
        holder.isCollectedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                networkForCollect(v.isSelected() ? 1 : 0, item.getId());
                Utils.loading(v.getContext());
            }
        });
        //答案
        //        holder.mYourAnswer.setText(String.format("你的答案： %s", item.getUserAnswer()));
        //        holder.mQuestionAnswer.setText(String.format("正确答案： %s", item.getQuestionAnswer()));
        holder.mQuestionAnalysis.setText(item.getQuestionAnalysis());
        //        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.mChoiceExamView.getContext()));
        //        CorrectRecordAdapter recordAdapter = new CorrectRecordAdapter(holder.recyclerView.getContext());
        //        holder.recyclerView.setAdapter(recordAdapter);
        //        //订正记录
        //        if (item.getCorrect() > 0){
        //            holder.cb_correctionBtn.setVisibility(View.VISIBLE);
        //        } else{
        //            holder.cb_correctionBtn.setVisibility(View.GONE);
        //        }
        holder.mSourceFrom.setText(String.format(holder.mSourceFrom.getText().toString().trim(), item.getQuestionSource()));
        //区分选择填空 1.单选 2.多选 3 填空
        if (item.getQuestionType() == 3) {
            holder.mFillBlankExamView.setVisibility(View.VISIBLE);
            holder.mChoiceExamView.setVisibility(View.GONE);
            holder.mFillBlankExamView.setHead(item.getQuestionStem());
            holder.mFillBlankExamView.setEditAreaVisible(View.GONE, true);
        } else {
            holder.mFillBlankExamView.setVisibility(View.GONE);
            holder.mChoiceExamView.setVisibility(View.VISIBLE);
            holder.mChoiceExamView.setExamHead(item.getQuestionStem());
            if (item.getQuestionWrongReason() != null) {
                holder.mChoiceExamView.setTags(Arrays.asList(item.getQuestionWrongReason().split(",")));
            }
            String questionContent = item.getQuestionContent();
            try {
                JSONArray array = new JSONArray(questionContent);
                String[] s = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject opt = (JSONObject) array.get(i);
                    s[i] = opt.getString("questionOptionValue");
                }
                holder.mChoiceExamView.setOptions(false, false, s);//选项
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        //        private ViewGroup mExamContainer;
        private TextView mSourceFrom;//来源
        private TextView mQuestionAnalysis;//解析
        private View isCollectedBtn;//收藏
        private View deleteBtn, lookOrEditReasonBtn, correctBtn;
        private CheckBox cb_showAnalyzeBtn;
        private ChoiceExamView mChoiceExamView;
        private FillBlankExamView mFillBlankExamView;
        //        private RecyclerView recyclerView;
        
        public ViewHolder(View itemView) {
            super(itemView);
            //            mExamContainer = itemView.findViewById(R.id.examContainer);
            mSourceFrom = itemView.findViewById(R.id.tv_sourceFrom);
            deleteBtn = itemView.findViewById(R.id.tv_delete);
            lookOrEditReasonBtn = itemView.findViewById(R.id.tv_lookOrEditReason);
            correctBtn = itemView.findViewById(R.id.tv_CorrectBtn);
            cb_showAnalyzeBtn = itemView.findViewById(R.id.cb_showAnalyzeBtn);
            //            recyclerView = itemView.findViewById(R.id.recyclerView);
            isCollectedBtn = itemView.findViewById(R.id.iv_isCollected);
            mChoiceExamView = itemView.findViewById(R.id.choiceExamView);
            mFillBlankExamView = itemView.findViewById(R.id.fillBlankExamView);
            mQuestionAnalysis = itemView.findViewById(R.id.tv_questionAnalysis);
        }
    }
    
    private class ClickLisen implements View.OnClickListener {
        
        private ClickLisen instance;
        
        public ClickLisen() {
        }
        //
        //        public ClickLisen(int id){
        //            questionId = id;
        //        }
        
        @Override
        public void onClick(final View v) {
            if (v.getId() == R.id.tv_delete) {//删除
                TipDialog dialog = new TipDialog(v.getContext());
                dialog.setTextDes("确认删除这道题？\n删除后无法恢复");
                dialog.setTextDesSize(14);
                dialog.setButton1Color(Color.parseColor("#75C82B"));
                dialog.setButton1("确认", new TipDialog.DialogButtonOnClickListener() {
                    @Override
                    public void onClick(View button, TipDialog dialog) {
                        Utils.loading(v.getContext());
                        networkForDelete((Integer) v.getTag());
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
                mWrongReasonDialog.setExtra((Integer) v.getTag());
                mWrongReasonDialog.show();
                WindowManager.LayoutParams attr = mWrongReasonDialog.getWindow().getAttributes();
                attr.width = (int) (v.getResources().getDisplayMetrics().widthPixels - 40 * 2 * v.getResources().getDisplayMetrics().density);
                //                attr.horizontalMargin = 40 * v.getResources().getDisplayMetrics().density;
                mWrongReasonDialog.getWindow().setAttributes(attr);
            } else if (v.getId() == R.id.cancelBtn) {
                if (mWrongReasonDialog != null && mWrongReasonDialog.isShowing()) {
                    mWrongReasonDialog.dismiss();
                }
            } else if (v.getId() == R.id.okBtn) {
                if (mWrongReasonDialog != null && mWrongReasonDialog.isShowing()) {
                    mWrongReasonDialog.dismiss();
                }
                Utils.loading(v.getContext());
                networkForReasonSummary(mWrongReasonDialog.getId(), mWrongReasonDialog.getSelectedText());
            } else if (v.getId() == R.id.tv_CorrectBtn) {
                //                v.getContext().startActivity(new Intent(v.getContext(), CorrectExamActivity.class));
                CorrectExamActivity.start(v.getContext(), (Exam) v.getTag());
            }
        }
    }
    
    private WrongReasonDialog mWrongReasonDialog;
    
    /**
     * 错题原因总结
     */
    private class WrongReasonDialog extends CenterDialog {
        List<String> mStringList;
        List<String> mTags;
        private EditText mInputView;
        private int questionId;
        
        public WrongReasonDialog(Context context, int layout) {
            super(context, layout);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        
        public void setExtra(int questionId) {
            this.questionId = questionId;
        }
        
        public int getId() {
            return questionId;
        }
        
        @Override
        public void initView(final View view) {
            mStringList = new ArrayList<>();
            mStringList.clear();
            //            mTags = Arrays.asList("题目抄错", "题目不明白", "就是猜的啊");
            mTags = new ArrayList<>();
            TagFlowLayout flowLayout = view.findViewById(R.id.tagFlowLawout_reason);
            mInputView = view.findViewById(R.id.et_inputReason);
            final View cancelBtn = view.findViewById(R.id.cancelBtn);
            final View okBtn = view.findViewById(R.id.okBtn);
            cancelBtn.setOnClickListener(new ClickLisen());
            okBtn.setOnClickListener(new ClickLisen());
            flowLayout.setAdapter(new TagAdapter<String>(mTags) {
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
                    if (view.isSelected()) {
                        mStringList.add(mTags.get(position));
                    } else {
                        mStringList.remove(mTags.get(position));
                    }
                    return true;
                }
            });
        }
        
        public String getSelectedText() {
            StringBuffer sb = new StringBuffer();
            String inputText = mInputView == null ? "" : mInputView.getText().toString().trim();
            if (mStringList == null) {
                mStringList = new ArrayList<>();
            }
            if (inputText != null && inputText.trim().length() > 0) {
                mStringList.add(inputText);
            }
            if (mStringList.size() > 0) {
                for (int i = 0; i < mStringList.size(); i++) {
                    if (i == 0) {
                        sb.append(mStringList.get(0));
                    } else {
                        sb.append("," + mStringList.get(i));
                    }
                }
            }
            return sb.toString();
        }
    }
    
    /**
     * 收藏
     *
     * @param collect
     * @param questionId
     */
    private void networkForCollect(final int collect, int questionId) {
        LiveSccUserWrongQuestionCollectRequest request = new LiveSccUserWrongQuestionCollectRequest();
        request.setUserId(UserManager.getInstance().getUserId() + "");
        request.setCollect(collect);
        request.setUserWrongQuestionId(questionId);
        Api.getInstance().postScc(request, LiveSccUserWrongQuestionCollectResponse.class, new ResponseCallback<LiveSccUserWrongQuestionCollectResponse>() {
            @Override
            public void onResponse(LiveSccUserWrongQuestionCollectResponse date, boolean isFromCache) {
                Utils.dismissLoading();
                if (collect == 1) {
                    ToastUtils.showShort("已收藏");
                } else {
                    ToastUtils.showShort("取消收藏");
                }
            }
        }, "");
    }
    
    /**
     * 删除题目
     *
     * @param questionId
     */
    private void networkForDelete(int questionId) {
        LiveSccUserWrongQuestionDeleteRequest request = new LiveSccUserWrongQuestionDeleteRequest();
        request.setUserId(UserManager.getInstance().getUserId() + "");
        request.setUserWrongQuestionId(questionId);
        Api.getInstance().postScc(request, LiveSccUserWrongQuestionDeleteResponse.class, new ResponseCallback<LiveSccUserWrongQuestionDeleteResponse>() {
            @Override
            public void onResponse(LiveSccUserWrongQuestionDeleteResponse date, boolean isFromCache) {
                ToastUtils.showShort("删除成功");
                Utils.dismissLoading();
            }
        }, "");
    }
    
    /**
     * 错因总结
     *
     * @param questionId
     * @param reason
     */
    private void networkForReasonSummary(int questionId, String reason) {
        LiveSccUserWrongQuestionSaveReasonRequest request = new LiveSccUserWrongQuestionSaveReasonRequest();
        request.setUserId(UserManager.getInstance().getUserId() + "");
        request.setUserWrongQuestionId(questionId);
        request.setReason(reason);
        Api.getInstance().postScc(request, LiveSccUserWrongQuestionSaveReasonResponse.class, new ResponseCallback<LiveSccUserWrongQuestionSaveReasonResponse>() {
            @Override
            public void onResponse(LiveSccUserWrongQuestionSaveReasonResponse date, boolean isFromCache) {
                ToastUtils.showShort("提交成功");
                Utils.dismissLoading();
            }
        }, "");
    }
    
    /**
     * 订正记录
     *
     * @param questionId 错题id
     */
    private void networkForCorrectionList(int questionId, final ViewGroup area, final CorrectRecordAdapter adapter) {
        Utils.loading(area.getContext());
        LiveSccUserWrongQuestionCorrectListRequest request = new LiveSccUserWrongQuestionCorrectListRequest();
        request.setUserId(UserManager.getInstance().getUserId() + "");
        request.setUserWrongQuestionId(questionId);
        Api.getInstance().postScc(request, LiveSccUserWrongQuestionCorrectListResponse.class, new ResponseCallback<LiveSccUserWrongQuestionCorrectListResponse>() {
            @Override
            public void onResponse(LiveSccUserWrongQuestionCorrectListResponse date, boolean isFromCache) {
                if (date.getData() == null) {
                    return;
                }
                area.setVisibility(View.VISIBLE);
                List<SccUserWrongQuestionCorrectBean> list = date.getData().getList();
                adapter.setData(list);
            }
        }, "correct list");
    }
}
