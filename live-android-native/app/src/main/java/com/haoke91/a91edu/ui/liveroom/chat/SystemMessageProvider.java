package com.haoke91.a91edu.ui.liveroom.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haoke91.im.mqtt.entities.Message;
import com.haoke91.videolibrary.R;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/2 上午11:58
 * 修改人：weiyimeng
 * 修改时间：2018/7/2 上午11:58
 * 修改备注：
 */
public class SystemMessageProvider extends BaseMessageProvider<Message, SystemMessageProvider.ViewHolder> {
    public SystemMessageProvider(MultiMessageAdapter adapter) {
        super(adapter);
    }
    
    @NonNull
    @Override
    protected SystemMessageProvider.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_message_system, parent, false);
        return new ViewHolder(root);
        
    }
    
    @Override
    protected void onBindViewHolder(@NonNull final SystemMessageProvider.ViewHolder holder, @NonNull Message s) {
        //  holder.tv_message_content.setText(s);
        String content = (String) s.getContent();
        if (content != null && content.trim().length() > 0) {
            holder.tv_message_system.setText((String) s.getContent());
            holder.tv_message_system.setVisibility(View.VISIBLE);
        } else {
            holder.tv_message_system.setVisibility(View.GONE);
        }
        //        holder.tv_message_system.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                ToastUtils.showShort("" + holder.getAdapterPosition());
        //            }
        //        });
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message_system;
        
        public ViewHolder(View itemView) {
            super(itemView);
            tv_message_system = itemView.findViewById(R.id.tv_message_system);
        }
    }
}
