package com.haoke91.a91edu.ui.liveroom.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.haoke91.a91edu.R;
import com.haoke91.baselibrary.emoji.MoonUtil;
import com.haoke91.im.mqtt.IMManager;
import com.haoke91.im.mqtt.entities.Constant;
import com.haoke91.im.mqtt.entities.Message;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/2 上午11:58
 * 修改人：weiyimeng
 * 修改时间：2018/7/2 上午11:58
 * 修改备注：
 */
public class TextMessageProvider extends BaseMessageProvider<Message, TextMessageProvider.ViewHolder> {
    public TextMessageProvider(MultiMessageAdapter adapter) {
        super(adapter);
    }
    
    @NonNull
    @Override
    protected TextMessageProvider.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_chat_text, parent, false);
        return new ViewHolder(root);
        
    }
    
    @Override
    protected void onBindViewHolder(@NonNull TextMessageProvider.ViewHolder holder, @NonNull Message s) {
        // holder.tv_message_content.setText(s);
        if (s == null) {
            return;
        }
        try {
            //            SpanUtils spanUtils = new SpanUtils();
            //            int resourcesId;
            //            if (Constant.MessageFlag.LIKE.getValue().equalsIgnoreCase(s.getFlag())){
            //                resourcesId = com.haoke91.baselibrary.R.drawable.img_like;
            //                SpannableStringBuilder xxx = spanUtils.appendImage(getEmotDrawable(Utils.getApp(), resourcesId)).create();
            //                holder.tv_message_content.setText(xxx);
            //            } else if (Constant.MessageFlag.FLOWER.getValue().equalsIgnoreCase(s.getFlag())){
            //                resourcesId = com.haoke91.baselibrary.R.drawable.img_rose;
            //                SpannableStringBuilder xxx = spanUtils.appendImage(getEmotDrawable(Utils.getApp(), resourcesId)).create();
            //                holder.tv_message_content.setText(xxx);
            //            } else{
            JSONObject obj = new JSONObject(new Gson().toJson(s.getContent() == null ? "" : s.getContent()));
            MoonUtil.identifyFaceExpression(Utils.getApp(), holder.tv_message_content, obj.optString("text"), ImageSpan.ALIGN_BOTTOM);
            //            }
            String name;
            if (s.getFromUser() != null && s.getFromUser().getUserId() != null && s.getFromUser().getUserId().equals(IMManager.Companion.getInstance().getSessionUser().getUserId())) {
                name = "我";
            } else {
                if (s.getFromUser() != null && s.getFromUser().getProp() != null) {
                    name = s.getFromUser().getProp().getName();
                } else {
                    name = "未知用户";
                }
            }
            
            holder.tv_message_name.setText(name);
            if (s.getRole().equals(Constant.Role.STUDENT.getValue())) {
                holder.tv_message_name.setBackgroundResource(R.drawable.bg_others_trans_radius1);
            } else if (s.getRole().equals(Constant.Role.TEACHER.getValue())) {
                holder.tv_message_name.setBackgroundResource(R.drawable.bg_teacher_red);
                holder.tv_message_name.setText("主讲");
            } else {
                holder.tv_message_name.setBackgroundResource(R.drawable.bg_zujiao_yellow);
                holder.tv_message_name.setText("助教");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
    }
    
    private static Drawable getEmotDrawable(Context context, int resources) {
        Drawable drawable = context.getResources().getDrawable(resources);
        
        // scale
        if (drawable != null) {
            int width = (int) (drawable.getIntrinsicWidth() * 0.4);
            int height = (int) (drawable.getIntrinsicHeight() * 0.4);
            drawable.setBounds(0, 0, width, height);
        }
        
        return drawable;
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message_name, tv_message_content;
        
        public ViewHolder(View itemView) {
            super(itemView);
            tv_message_name = itemView.findViewById(R.id.tv_message_name);
            tv_message_content = itemView.findViewById(R.id.tv_message_content);
        }
    }
}
