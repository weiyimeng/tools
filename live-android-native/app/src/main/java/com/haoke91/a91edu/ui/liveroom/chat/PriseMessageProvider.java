package com.haoke91.a91edu.ui.liveroom.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.Utils;
import com.haoke91.im.mqtt.IMManager;
import com.haoke91.im.mqtt.entities.Constant;
import com.haoke91.im.mqtt.entities.Message;
import com.haoke91.videolibrary.R;


/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/2 上午11:58
 * 修改人：weiyimeng
 * 修改时间：2018/7/2 上午11:58
 * 修改备注： 点赞消息
 */
public class PriseMessageProvider extends BaseMessageProvider<Message, PriseMessageProvider.ViewHolder> {
    public PriseMessageProvider(MultiMessageAdapter adapter) {
        super(adapter);
    }
    
    @NonNull
    @Override
    protected PriseMessageProvider.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_message_prise, parent, false);
        return new ViewHolder(root);
        
    }
    
    @Override
    protected void onBindViewHolder(@NonNull PriseMessageProvider.ViewHolder holder, @NonNull Message s) {
        // holder.tv_message_content.setText(s);
        if (s == null) {
            return;
        }
        //        Map<String, User> userContainer = adapter.getUserContainer();
        String name = s.getFrom();
        if (s.getFrom() != null && !s.getFrom().equals("")) {
            if (s.getFrom().equals(IMManager.Companion.getInstance().getSessionUser().getUserId())) {
                name = "我";
            } else {
                if (s.getFromUser() != null && s.getFromUser().getProp() != null) {
                    name = s.getFromUser().getProp().getName();
                }
            }
        }
        SpanUtils spanUtils = new SpanUtils();
        int resourcesId;
        if (Constant.MessageFlag.LIKE.getValue().equalsIgnoreCase(s.getFlag())) {
            resourcesId = R.drawable.img_like;
        } else {
            resourcesId = R.drawable.img_rose;
            
        }
        SpannableStringBuilder xxx = spanUtils.append(name).setAlign(Layout.Alignment.ALIGN_CENTER).append(":").appendImage(getEmotDrawable(Utils.getApp(), resourcesId), SpanUtils.ALIGN_CENTER).create();
        holder.tv_message_prise.setText(xxx);
    }
    
    private static Drawable getEmotDrawable(Context context, int resources) {
        Drawable drawable = context.getResources().getDrawable(resources);
        
        // scale
        if (drawable != null) {
            int width = (int) (drawable.getIntrinsicWidth() * 0.3);
            int height = (int) (drawable.getIntrinsicHeight() * 0.3);
            drawable.setBounds(0, 0, width, height);
        }
        
        return drawable;
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message_prise;
        
        public ViewHolder(View itemView) {
            super(itemView);
            tv_message_prise = itemView.findViewById(R.id.tv_message_prise);
        }
    }
}
