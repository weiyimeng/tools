package com.eduhdsdk.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.eduhdsdk.R;
import com.eduhdsdk.adapter.FaceGVAdapter;
import com.eduhdsdk.entity.ChatData;
import com.eduhdsdk.message.RoomSession;
import com.eduhdsdk.tools.HttpTextView;
import com.eduhdsdk.tools.KeyBoardUtil;
import com.eduhdsdk.tools.ScreenScale;
import com.eduhdsdk.tools.SoftKeyBoardListener;
import com.eduhdsdk.viewutils.ThreeChatWindowPop;
import com.talkcloud.room.RoomUser;
import com.talkcloud.room.TKRoomManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称：talkplus
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/11/7 18:34
 */
public class ChatView extends FrameLayout implements View.OnClickListener {
    private ListView mListView;
    private ChatAdapter mChatAdapter;
    private ArrayList<ChatData> mData;
    private int TYPE_TOPMSG = 1, TYPE_COMMONMSG = 0;
    
    private TextView tv_sayMsg;
    private ImageView iv_send_flower, iv_send_like;
    private View ll_bottom_msg;
    
    /**
     * 编辑文字输入
     */
    private View smallContentView;
    private PopupWindow chat_input_popup;
    private ArrayList<String> staticFacesList;
    
    
    public ChatView(@NonNull Context context) {
        super(context);
        initialize(context);
    }
    
    public ChatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }
    
    
    private void initialize(Context context) {
        mData = new ArrayList();
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_lv_chat, null);
        addView(contentView);
        mListView = contentView.findViewById(R.id.lv_chat);
        mChatAdapter = new ChatAdapter();
        mListView.setAdapter(mChatAdapter);
        tv_sayMsg = contentView.findViewById(R.id.tv_sayMsg);
        ll_bottom_msg = contentView.findViewById(R.id.ll_bottom_msg);
        tv_sayMsg.setOnClickListener(this);
        iv_send_flower = contentView.findViewById(R.id.iv_send_flower);
        iv_send_flower.setOnClickListener(this);
        iv_send_like = contentView.findViewById(R.id.iv_send_like);
        iv_send_like.setOnClickListener(this);
        TextView tvHead = new TextView(context);
        tvHead.setText("进入房间");
        tvHead.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvHead.setTextColor(ContextCompat.getColor(context, R.color.white));
        tvHead.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        tvHead.setGravity(Gravity.CENTER);
        mListView.addHeaderView(tvHead);
    }
    
    /**
     * add one data
     *
     * @param chat
     */
    public void putData(ChatData chat) {
        mData.add(chat);
        mListView.smoothScrollToPosition(mData.size() - 1);
    }
    
    /**
     * add data to list
     */
    public void setData(ArrayList<ChatData> data) {
        if (data == null || data.size() == 0) {
            mData.clear();
        } else {
            mData = data;
        }
        mChatAdapter.notifyDataSetChanged();
        mListView.setSelection(mData.size() - 1);
    }
    
    /**
     * 添加list
     *
     * @param data
     */
    public void addData(ArrayList<ChatData> data) {
        if (data != null) {
            mData.addAll(data);
        }
        mChatAdapter.notifyDataSetChanged();
        mListView.setSelection(mData.size() - 1);
    }
    
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_sayMsg) {
            if (mChatViewClickListener != null) {
                //                mChatViewClickListener.onClickView(v);
                showChatInputPopupWindow(v, true);
            }
        } else if (i == R.id.iv_send_flower) {
            if (mChatViewClickListener != null) {
                mChatViewClickListener.onSendFlower(v);
            }
        } else if (i == R.id.iv_send_like) {
            mChatViewClickListener.onSendLike(v);
        }
        
    }
    
    private TextView txt_send;
    private ImageView iv_chat, iv_broad;
    private EditText edt_input_small_window;
    private String edt_input_content = "";
    
    /**
     * 显示软键盘上方一小条输入栏
     *
     * @param is_show_broad true 显示键盘   false 显示表情
     */
    public void showChatInputPopupWindow(View view, final boolean is_show_broad) {
        if (chat_input_popup == null) {
            initStaticFaces();
            smallContentView = LayoutInflater.from(getContext()).inflate(R.layout.three_layout_chat_message_edit_input, null);
            chat_input_popup = new PopupWindow(getContext());
            chat_input_popup.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            chat_input_popup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            
            txt_send = (TextView) smallContentView.findViewById(R.id.txt_send);
            iv_chat = (ImageView) smallContentView.findViewById(R.id.iv_chat);
            iv_broad = (ImageView) smallContentView.findViewById(R.id.iv_broad);
            edt_input_small_window = (EditText) smallContentView.findViewById(R.id.edt_input_chat);
            //解决部分机型获取不到焦点问题
            edt_input_small_window.setFocusable(true);
            edt_input_small_window.setFocusableInTouchMode(true);
            
            final GridView chart_face_gv = (GridView) smallContentView.findViewById(R.id.chart_face_gv);
            
            FaceGVAdapter mGvAdapter = new FaceGVAdapter(staticFacesList, getContext());
            chart_face_gv.setAdapter(mGvAdapter);
            chart_face_gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
            chart_face_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        //                        if (position == 41){
                        //                            delete();
                        //                        }
                        if (position < 8) {
                            insert(getFace(staticFacesList.get(position)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            iv_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示表情的时候，设置不可获取焦点，否则会出现表情框与键盘同时存在的情况
                    edt_input_small_window.setFocusable(false);
                    chart_face_gv.setVisibility(View.VISIBLE);
                    iv_chat.setVisibility(View.GONE);
                    iv_broad.setVisibility(View.VISIBLE);
                    KeyBoardUtil.hideKeyBoard(getContext(), edt_input_small_window);
                }
            });
            
            iv_broad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //用户想要弹出键盘的时候，让edittext继续获得焦点
                    edt_input_small_window.setFocusableInTouchMode(true);
                    chart_face_gv.setVisibility(View.GONE);
                    iv_chat.setVisibility(View.VISIBLE);
                    iv_broad.setVisibility(View.GONE);
                    KeyBoardUtil.showKeyBoard(getContext(), edt_input_small_window);
                }
            });
        }
        
        
        //
        txt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edt_input_small_window.getText().toString().trim();
                
                if (msg != null && !msg.isEmpty()) {
                    boolean isSend = false;
                    edt_input_small_window.setText("");
                    edt_input_content = "";
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(System.currentTimeMillis()));
                    String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                    
                    Map<String, Object> msgMap = new HashMap<String, Object>();
                    msgMap.put("type", 0);
                    msgMap.put("time", time);
                    TKRoomManager.getInstance().sendMessage(msg, "__all", msgMap);
                }
                chat_input_popup.dismiss();
            }
        });
        
        edt_input_small_window.setText(edt_input_content);
        
        chat_input_popup.setContentView(smallContentView);
        chat_input_popup.setBackgroundDrawable(null);
        
        chat_input_popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        chat_input_popup.setFocusable(true);
        chat_input_popup.setBackgroundDrawable(new BitmapDrawable());
        chat_input_popup.setOutsideTouchable(false);
        chat_input_popup.setTouchable(true);
        
        chat_input_popup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        
        chat_input_popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                edt_input_content = edt_input_small_window.getText().toString();
                //                if (is_forbid_chat){
                //                    edt_input.setText("");
                //                } else{
                //                    edt_input.setText(edt_input_content);
                //                }
                if (SoftKeyBoardListener.isSoftShowing((Activity) getContext())) {
                    KeyBoardUtil.hideKeyBoard(getContext(), edt_input_small_window);
                }
            }
        });
        
        if (!is_show_broad) {
            //显示表情的时候，设置不可获取焦点，否则会出现表情框与键盘同时存在的情况
            edt_input_small_window.setFocusable(false);
            //            chart_face_gv.setVisibility(View.VISIBLE);
            iv_chat.setVisibility(View.GONE);
            iv_broad.setVisibility(View.VISIBLE);
            KeyBoardUtil.hideKeyBoard(getContext(), edt_input_small_window);
        } else {
            KeyBoardUtil.showKeyBoard(getContext(), edt_input_small_window);
        }
        //        ((Activity)getContext()).getWindow().getDecorView().addOnLayoutChangeListener(new OnLayoutChangeListener() {
        //            @Override
        //            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //                Rect rect = new Rect();
        //                ((Activity)getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //                if(bottom!=0 && oldBottom!=0 && bottom - rect.bottom <= 0){
        //                    chat_input_popup.dismiss();
        //                }else {
        //                }
        //            }
        //        });
    }
    
    private void initStaticFaces() {
        try {
            staticFacesList = new ArrayList<String>();
            staticFacesList.clear();
            String[] faces = getContext().getAssets().list("face");
            for (int i = 0; i < faces.length; i++) {
                staticFacesList.add(faces[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private SpannableStringBuilder getFace(String png) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            String[] splitText = png.split("\\.");
            String tempText = "[" + splitText[0] + "]";
            Bitmap bitmap = BitmapFactory.decodeStream(getContext().getAssets().open("face/" + png));
           /* Drawable drawable = new BitmapDrawable(bitmap);
            drawable.setBounds(0, 0, 50, 50);
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);*/
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(16 * getResources().getDisplayMetrics().density / width, 16 * getResources().getDisplayMetrics().density / height);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            MyIm imageSpan = new MyIm(getContext(), bitmap);
            sb.append(tempText);
            sb.setSpan(imageSpan, sb.length() - tempText.length(), sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }
    
    /**
     * 设置禁言
     *
     * @param enableSay
     */
    public void enableSay(boolean enableSay) {
        tv_sayMsg.setEnabled(!enableSay);
        if (enableSay) {
            tv_sayMsg.setHint(R.string.no_say_something);
        } else {
            tv_sayMsg.setHint(R.string.say_something);
        }
    }
    
    /**
     * 设置是否可发送消息（文本，送花，点赞）
     *
     * @param enableSendMessage
     */
    public void enableSendMessage(boolean enableSendMessage) {
        tv_sayMsg.setEnabled(enableSendMessage);
        iv_send_flower.setEnabled(enableSendMessage);
        iv_send_like.setEnabled(enableSendMessage);
        if (enableSendMessage) {
            ObjectAnimator.ofFloat(ll_bottom_msg, "alpha", 0.4f, 1f).start();
        } else {
            ObjectAnimator.ofFloat(ll_bottom_msg, "alpha", 1f, 0.4f).start();
        }
    }
    
    private SpannableStringBuilder getFaceString(String content) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "(\\[em_)\\d{1}(\\])";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            try {
                String png = tempText.substring("[".length(), tempText.length() - "]".length()) + ".png";
                Bitmap bitmap = BitmapFactory.decodeStream(getContext().getAssets().open("face/" + png));
               /* Drawable drawable = new BitmapDrawable(bitmap);
                drawable.setBounds(0, 0, 30, 30);*/
               /* ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);*/
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                Matrix matrix = new Matrix();
                matrix.postScale(16 * getResources().getDisplayMetrics().density / width, 16 * getResources().getDisplayMetrics().density / height);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                MyIm imageSpan = new MyIm(getContext(), bitmap);
                sb.setSpan(imageSpan, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb;
    }
    
    private void insert(CharSequence text) {
        if (edt_input_small_window == null) {
            return;
        }
        int iCursorStart = Selection.getSelectionStart((edt_input_small_window.getText()));
        int iCursorEnd = Selection.getSelectionEnd((edt_input_small_window.getText()));
        if (iCursorStart != iCursorEnd) {
            ((Editable) edt_input_small_window.getText()).replace(iCursorStart, iCursorEnd, "");
        }
        int iCursor = Selection.getSelectionEnd((edt_input_small_window.getText()));
        ((Editable) edt_input_small_window.getText()).insert(iCursor, text);
    }
    
    private class ChatAdapter extends BaseAdapter {
        
        @Override
        public int getCount() {
            return mData.size();
        }
        
        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }
        
        @Override
        public long getItemId(int position) {
            return position;
        }
        
        @Override
        public int getItemViewType(int position) {
            //            if (position == 0){
            //                return TYPE_TOPMSG;
            //            } else{
            //                return TYPE_COMMONMSG;
            //            }
            return 0;
        }
        
        @Override
        public int getViewTypeCount() {
            return 2;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //            if (getItemViewType(position) == TYPE_TOPMSG){
            //                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lv_tip, null);
            //                return convertView;
            //            } else{
            //
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_item_chat, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (mData != null && mData.size() <= position) {
                return convertView;
            }
            ChatData bean = mData.get(position);
            String name = "";
            RoomUser user = bean.getUser();
            holder.tvName.setBackgroundResource(R.drawable.bg_ffc200_radius2);
            if (user == null) {
                return convertView;
            }
            if (user.role == 2) {
                name = user.nickName;
                holder.tvName.setBackgroundResource(R.drawable.bg_others_trans_radius1);
            } else if (user.role == 0) {
                name = "主讲";
                holder.tvName.setBackgroundResource(R.drawable.bg_teacher_red);
            } else {
                name = "助教";
                holder.tvName.setBackgroundResource(R.drawable.bg_zujiao_yellow);
            }
            holder.tvName.setText(name);
            if (bean.getMessage() != null) {
                SpannableStringBuilder sb = getFaceString(bean.getMessage());
                holder.tvContent.setUrlText(sb);
            }
            //            //区分其他消息
            //                if (user != null){
            //                    if (bean.isStystemMsg()){
            //                        if (bean.getState() == 1){
            //                            holder.tvContent.setText(user.nickName + "进入教室");
            //                        }
            //                    }
            //                } else{
            //                    if (bean.isStystemMsg()){
            //                        holder.tvContent.setText(bean.getMessage());
            //                    }
            //                }
            //            }
            
            
            return convertView;
        }
        
        private class ViewHolder {
            TextView tvName;
            HttpTextView tvContent;
            
            ViewHolder(View item) {
                tvName = (TextView) item.findViewById(R.id.tvName);
                tvContent = (HttpTextView) item.findViewById(R.id.tvContent);
            }
        }
        
    }
    
    private ChatViewClickListener mChatViewClickListener;
    
    public interface ChatViewClickListener {
        void onSendFlower(View view);
        
        void onSendLike(View view);
    }
    
    public void setCallBack(ChatViewClickListener listener) {
        mChatViewClickListener = listener;
    }
}
