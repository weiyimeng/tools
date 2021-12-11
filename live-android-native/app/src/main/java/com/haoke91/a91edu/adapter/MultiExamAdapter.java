package com.haoke91.a91edu.adapter;

import android.support.annotation.NonNull;

import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.list.LiveSccUserWrongQuestionListResponse;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/8/31 10:15
 */
public class MultiExamAdapter extends MultiTypeAdapter {
    public MultiExamAdapter(List<LiveSccUserWrongQuestionListResponse.ListData> data){
        super(data);
    }
    
    @Override
    public void setItems(@NonNull List<?> items){
        if (items == null){
            items = new ArrayList<>();
        }
        super.setItems(items);
        notifyDataSetChanged();
    }
    
    public void addItems(@Nonnull List items){
        List<?> list = getItems();
        list.addAll(items);
        setItems(list);
        notifyDataSetChanged();
    }
}
