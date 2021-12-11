package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.gaosiedu.scc.sdk.android.api.user.task.complete.list.SccGetUserTasksCompleteListResponse;
import com.gaosiedu.scc.sdk.android.domain.SccConstants;
import com.google.gson.Gson;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.entities.Task;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;

import java.util.List;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/9 上午10:12
 * 修改人：weiyimeng
 * 修改时间：2018/8/9 上午10:12
 * 修改备注：
 */
public class DailyWorkHistoryAdapter extends QuickWithPositionAdapter<SccGetUserTasksCompleteListResponse.ListData> {
    Gson mGson;
    
    public DailyWorkHistoryAdapter(Context context, List<SccGetUserTasksCompleteListResponse.ListData> dates){
        super(context, R.layout.item_work_history, dates);
        mGson = new Gson();
    }
    
    @Override
    protected void convert(BaseAdapterHelper helper, SccGetUserTasksCompleteListResponse.ListData item, int position){
        TextView tvTitle = helper.getTextView(R.id.tv_work_tittle);
        TextView tvObtain = helper.getTextView(R.id.tv_gold);
        TextView tvContent = helper.getTextView(R.id.tv_work_time);
        String type = item.getType();
        Task task = mGson.fromJson(item.getTaskCondition(), Task.class);
//        Award award = mGson.fromJson(item.getRewardInfo(), Award.class);
        if (SccConstants.strategy_name_task_day_sign.equals(type)){
            tvTitle.setText("日常签到");
            tvContent.setText(item.getCreateTime());
        } else if (SccConstants.strategy_name_task_live.equals(type)){
            tvTitle.setText(String.format("你参加了《%s》的直播课", task.getKnowledgeName()));
            tvContent.setText(String.format("%s  参加直播课,并听课%s分钟以上", item.getCreateTime(), task.getAgreeTime()));
        } else if (SccConstants.strategy_name_task_live_interact.equals(type)){
            tvTitle.setText(String.format("你参加了《%s》的直播互动", task.getKnowledgeName()));
            tvContent.setText(String.format("%s  参加直播互动并听课%s分钟以上", item.getCreateTime(), task.getAgreeTime()));
        } else if (SccConstants.strategy_name_task_replay.equals(type)){
            tvTitle.setText(String.format("你参加了《%s》的回放课", task.getKnowledgeName()));
            tvContent.setText(String.format("%s  参加回放课,并听课%s分钟以上", item.getCreateTime(), task.getAgreeTime()));
        }else if(SccConstants.strategy_name_task_homeWork.equals(type)){
            tvTitle.setText("作业任务");
            tvContent.setText(item.getCreateTime());
        }
        tvObtain.setText(String.format("金币 +%s  成长值 +%s", item.getGetGold(), item.getGetProgress()));
        //        tvContent.setText(String.format("%s  参加%s,并听课%s分钟以上","time",replaceTitle,"limit"));
    }
    
    //    private class Award {
    //        int gold;
    //        int progress;
    //
    //        public int getGold(){
    //            return gold;
    //        }
    //
    //        public void setGold(int gold){
    //            this.gold = gold;
    //        }
    //
    //        public int getProgress(){
    //            return progress;
    //        }
    //
    //        public void setProgress(int progress){
    //            this.progress = progress;
    //        }
    //    }
}
