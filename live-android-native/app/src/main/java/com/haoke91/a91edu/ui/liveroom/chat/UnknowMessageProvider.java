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
public class UnknowMessageProvider extends BaseMessageProvider<Message, UnknowMessageProvider.ViewHolder> {
    public UnknowMessageProvider(MultiMessageAdapter adapter) {
        super(adapter);
    }
    
    @NonNull
    @Override
    protected UnknowMessageProvider.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_message_unkonw, parent, false);
        return new ViewHolder(root);
        
    }
    
    @Override
    protected void onBindViewHolder(@NonNull final UnknowMessageProvider.ViewHolder holder, @NonNull Message s) {
        //  holder.tv_message_content.setText(s);
        //        holder.tv_message_system.setText("ssss");
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
