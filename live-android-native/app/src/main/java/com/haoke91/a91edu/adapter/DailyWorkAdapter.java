package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gaosiedu.scc.sdk.android.api.user.task.sign.SccTaskSignRequest;
import com.gaosiedu.scc.sdk.android.api.user.task.sign.SccTaskSignResponse;
import com.gaosiedu.scc.sdk.android.domain.SccConstants;
import com.gaosiedu.scc.sdk.android.domain.SccUserTaskList;
import com.google.gson.Gson;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.entities.Task;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.homework.UpLoadHomeworkActivity;
import com.haoke91.a91edu.ui.liveroom.LivePlayerActivity;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.NoDoubleClickListener;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;
import com.yanzhenjie.album.impl.DoubleClickWrapper;

import org.jetbrains.annotations.NotNull;

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
public class DailyWorkAdapter extends QuickWithPositionAdapter<SccUserTaskList> {
    Gson mGson;
    
    public DailyWorkAdapter(Context context, List<SccUserTaskList> datas) {
        super(context, R.layout.item_daily_work, datas);
        mGson = new Gson();
    }
    
    @Override
    protected void convert(BaseAdapterHelper helper, final SccUserTaskList item, int position) {
        TextView tvTitle = helper.getTextView(R.id.tv_work_name);
        final TextView tvSignBtn = helper.getTextView(R.id.tv_signBtn);
        final TextView tvCompleted = helper.getTextView(R.id.tv_getAward);
        TextView tvContent = helper.getTextView(R.id.tv_work_detail);
        TextView tvObtain = helper.getTextView(R.id.tv_work_obtain);
        tvSignBtn.setOnClickListener(new ClickEvent(item, position));//签到 or 去完成
        tvTitle.setText(item.getTaskName());
        String type = item.getType();//任务类型
        Award award = mGson.fromJson(item.getRewardInfo(), Award.class);
        tvObtain.setText(String.format("金币  +%s   成长值  +%s", award.getGold(), award.getProgress()));//奖励
        String taskCondition = item.getTaskCondition();
        if (item.getStatus() == 1 || item.getStatus() == 2) {//任务完成
            tvCompleted.setVisibility(View.VISIBLE);
            tvSignBtn.setVisibility(View.GONE);
        } else {
            tvCompleted.setVisibility(View.GONE);
            tvSignBtn.setVisibility(View.VISIBLE);
        }
        if (SccConstants.strategy_name_task_day_sign.equals(type)) {//日签到
            tvContent.setText("日常签到");
            tvSignBtn.setText("签到");
            tvSignBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    networkForSign(tvSignBtn, tvCompleted);
                }
            });
            
        } else if (SccConstants.strategy_name_task_live.equals(type)
            || SccConstants.strategy_name_task_replay.equals(type)
            || SccConstants.strategy_name_task_live_interact.equals(type)) {//直播任务 直播互动  回放课
            tvSignBtn.setText("去完成");
            final Task task = mGson.fromJson(taskCondition, Task.class);
            tvContent.setText(String.format("参加%s课程，并听课%s分钟以上", task.getKnowledgeName(), task.getAgreeTime()));
            tvSignBtn.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onDoubleClick(@NotNull View v) {
                    Utils.startLiveRoom(context, task.getKeyId() + "");
                    
                }
            });
            
        } else if (SccConstants.strategy_name_task_homeWork.equals(type)) {//作业任务
            tvSignBtn.setText("去完成");
            final Task task = mGson.fromJson(taskCondition, Task.class);
            tvContent.setText(String.format("完成‘ %s ’作业", task.getCourseName()));
            tvSignBtn.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onDoubleClick(View v) {
                    UpLoadHomeworkActivity.Companion.start(getContext(), task.getKeyId());
                }
            });
        } else {
            tvContent.setText("每日任务");
            tvSignBtn.setText("签到");
        }
        
        
    }
    
    private class ClickEvent implements View.OnClickListener {
        private SccUserTaskList mBean;
        private int mPosition;
        
        public ClickEvent(SccUserTaskList bean, int position) {
            this.mBean = bean;
            this.mPosition = position;
        }
        
        @Override
        public void onClick(View v) {
        }
    }
    //
    //    private OnUIClickListener mOnUIClickListener;
    //
    //    interface OnUIClickListener {
    //        void onClick(View view, SccUserTaskList bean, int position);
    //    }
    //
    //    public void setOnUIClickListener(OnUIClickListener onUIClickListener){
    //        this.mOnUIClickListener = onUIClickListener;
    //    }
    
    /**
     * goto do it
     */
    private void networkForSign(final View vSign, final View vCompleted) {
        SccTaskSignRequest request = new SccTaskSignRequest();
        request.setAsync(false);//异步
        request.setUserId(UserManager.getInstance().getUserId() + "");
        request.setType(SccConstants.strategy_name_task_day_sign);
        //homework: courseid, knowledgeid,homeworkid
        //直播 courseid ,knowledgeid
        Api.getInstance().postScc(request, SccTaskSignResponse.class, new ResponseCallback<SccTaskSignResponse>() {
            @Override
            public void onResponse(SccTaskSignResponse date, boolean isFromCache) {
                if (Utils.isSuccess(date.getCode())) {
                    ToastUtils.setGravity(Gravity.CENTER, 0, 0);
                    ToastUtils.showShort("签到成功");
                    vSign.setVisibility(View.GONE);
                    vCompleted.setVisibility(View.VISIBLE);
                }
            }
        }, "go to sign");
        
    }
    
    private class Award {
        int gold;
        int progress;
        
        public int getGold() {
            return gold;
        }
        
        public void setGold(int gold) {
            this.gold = gold;
        }
        
        public int getProgress() {
            return progress;
        }
        
        public void setProgress(int progress) {
            this.progress = progress;
        }
    }
}
