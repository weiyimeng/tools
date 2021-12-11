package com.haoke91.baselibrary.views.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haoke91.baselibrary.R;
import com.haoke91.baselibrary.emoji.EmoticonPickerView;
import com.haoke91.baselibrary.emoji.IEmoticonSelectedListener;
import com.haoke91.baselibrary.emoji.MoonUtil;

import java.lang.reflect.Field;

/**
 * Created by zyyoona7 on 2018/3/12.
 * <p>
 * PopupWindow 中存在 EditText 隐藏键盘方法不起作用，只有 toggle 键盘方法才起作用
 * 注：建议由 EditText 需求的弹窗使用 DialogFragment
 */
public class CommentPopup extends BasePopup<CommentPopup> implements IEmoticonSelectedListener {
    
    private OnSendMessageListener mOkListener;
    private EditText mEditText;
    private ImageView iv_face_text;
    private EmoticonPickerView fl_emoji_container;
    
    public static CommentPopup create(Context context) {
        return new CommentPopup(context);
    }
    
    public CommentPopup(Context context) {
        setContext(context);
    }
    
    
    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_cmmt, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getSoftHeight();
        setFocusAndOutsideEnable(true)
            .setBackgroundDimEnable(false)
            //  .setAnimationStyle(R.style.BottomPopAnim)
            .setDimValue(0.5f)
            .setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED)
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setOutsideTouchable(false);
    }
    
    @Override
    protected void initViews(View view) {
        fl_emoji_container = findViewById(R.id.fl_emoji_container);
        TextView tv_send_message = findViewById(R.id.tv_send_message);
        iv_face_text = findViewById(R.id.iv_face_text);
        mEditText = findViewById(R.id.et_cmmt);
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //  mEditText.setInputType(InputType.TYPE_NULL);
        mEditText.setCursorVisible(true);
        fl_emoji_container.show(CommentPopup.this);
        KeyboardUtils.registerSoftInputChangedListener((Activity) mContext, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                //                if (height > 100) {//认为显示软键盘
                //                    fl_emoji_container.setVisibility(View.GONE);
                //                    mPopupWindow.showAtLocation(iv_face_text, Gravity.BOTTOM, 0, keyboardHeight);
                //                    //                    ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 1.0f, 0f);
                //                    //                    scaleAnimation.setDuration(600);
                //                    //                    fl_emoji_container.startAnimation(scaleAnimation);
                //                } else {//认为隐藏软键盘
                //                    if (keyboardHeight > 0) {
                //                        ViewGroup.LayoutParams lp = fl_emoji_container.getLayoutParams();
                //                        lp.height = keyboardHeight;
                //                        fl_emoji_container.setLayoutParams(lp);
                //                    }
                //                    fl_emoji_container.setVisibility(View.VISIBLE);
                //                    mPopupWindow.showAtLocation(iv_face_text, Gravity.BOTTOM, 0, 0);
                //                }
            }
        });
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (fl_emoji_container.getVisibility() == View.VISIBLE) {
                        fl_emoji_container.setVisibility(View.GONE);
                        //                        ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        mEditText.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //  KeyboardUtils.toggleSoftInput();
                                iv_face_text.setSelected(false);
                                KeyboardUtils.showSoftInput(mEditText);
                                
                            }
                        }, 100);
                        //                        return true;
                    }
                }
                return false;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            private int start;
            private int count;
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                this.start = start;
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.count = count;
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                //                Logger.e("s====" + s.toString());
                MoonUtil.replaceEmoticons(mContext, s, start, count);
                
                //                if (watcher != null) {
                //                    watcher.afterTextChanged(s, start, count);
                //                }
                
                int editEnd = mEditText.getSelectionEnd();
                //   mEditText.removeTextChangedListener(this);
                //                while (StringUtil.counterChars(s.toString()) > 5000 && editEnd > 0) {
                //                    s.delete(editEnd - 1, editEnd);
                //                    editEnd--;
                //                }
                //   MoonUtil.identifyFaceExpression(Utils.getApp(), mEditText, mEditText.getText().toString().trim(), ImageSpan.ALIGN_BOTTOM);
                mEditText.setSelection(editEnd);
                //mEditText.addTextChangedListener(this);
            }
        });
        iv_face_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fl_emoji_container.getVisibility() == View.VISIBLE) {
                    fl_emoji_container.setVisibility(View.GONE);
                    iv_face_text.setSelected(false);
                    //                    mPopupWindow.showAtLocation(iv_face_text, Gravity.BOTTOM, 0, keyboardHeight);
                    iv_face_text.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            KeyboardUtils.toggleSoftInput();
                            
                        }
                    }, 100);
                    //                    ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                } else {
                    KeyboardUtils.toggleSoftInput();
                    fl_emoji_container.setVisibility(View.VISIBLE);
                    iv_face_text.setSelected(true);
                    //                    mPopupWindow.showAtLocation(iv_face_text, Gravity.BOTTOM, 0, 0);
                    //                                        if (keyboardHeight > 0) {
                    //                                            ViewGroup.LayoutParams lp = fl_emoji_container.getLayoutParams();
                    //                                            lp.height = keyboardHeight;
                    //                                            fl_emoji_container.setLayoutParams(lp);
                    //                                        }
                    
                }
                mOkListener.onChangeLayout(v.isSelected() ? 1 : 2);
            }
        });
        tv_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ObjectUtils.isEmpty(mOkListener)) {
                    // TODO: 2018/6/29   校验 文本
                    String message = mEditText.getText().toString().trim();
                    if (ObjectUtils.isEmpty(message)) {
                        ToastUtils.showShort("发送的文字不能为空");
                        return;
                    }
                    if (message != null && message.length() > 100) {
                        ToastUtils.showShort("发送的文字过多，请重试");
                        return;
                    }
                    
                    mOkListener.OnSendMessage(message);
                    mEditText.setText("");
                }
            }
        });
    }
    
    public String getText() {
        return mEditText.getText().toString().trim();
    }
    
    //    public CmmtPopup setChangeTextListener(View.OnClickListener listener) {
    //        mCancelListener = listener;
    //        return this;
    //    }
    
    public CommentPopup setOnActionClickListener(OnSendMessageListener listener) {
        mOkListener = listener;
        return this;
    }
    
    public CommentPopup showSoftInput() {
        //        Logger.e("showSoftInput======");
        //  if (mEditText != null) {
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                KeyboardUtils.showSoftInput(mEditText);
            }
        });
        //  }
        return this;
    }
    
    public CommentPopup hideSoftInput() {
        if (mEditText != null) {
            mEditText.post(new Runnable() {
                @Override
                public void run() {
                    KeyboardUtils.hideSoftInput(mEditText);
                }
            });
        }
        return this;
    }
    
    public void setEmojiLayoutGone() {
        if (fl_emoji_container.getVisibility() == View.VISIBLE) {
            fl_emoji_container.setVisibility(View.GONE);
            iv_face_text.setSelected(false);
            
        }
    }
    
    @Override
    public void onEmojiSelected(String key) {
        Editable mEditable = mEditText.getText();
        if ("/DEL".equals(key)) {
            mEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            int start = mEditText.getSelectionStart();
            int end = mEditText.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            mEditable.replace(start, end, key);
            // MoonUtil.identifyFaceExpression(Utils.getApp(), mEditText, mEditText.getText().toString().trim(), ImageSpan.ALIGN_BOTTOM);
            
        }
    }
    
    @Override
    public void onStickerSelected(String categoryName, String stickerName) {
    
    }
    
    public interface OnSendMessageListener {
        void OnSendMessage(String message);
        
        /**
         * @param type 1 键盘  2emoji
         */
        void onChangeLayout(int type);
    }
    
    private static int keyboardHeight;
    private static boolean isVisiableForLast;
    
    private void getSoftHeight() {
        final View decorView = ((Activity) mContext).getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                //计算出可见屏幕的高度
                int displayHight = rect.bottom - rect.top;
                //获得屏幕整体的高度
                int hight = decorView.getHeight();
                boolean visible = (double) displayHight / hight < 0.8;
                int statusBarHeight = 0;
                try {
                    Class<?> c = Class.forName("com.android.internal.R$dimen");
                    Object obj = c.newInstance();
                    Field field = c.getField("status_bar_height");
                    int x = Integer.parseInt(field.get(obj).toString());
                    statusBarHeight = mContext.getResources().getDimensionPixelSize(x);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                if (visible && visible != isVisiableForLast) {
                    //获得键盘高度
                    keyboardHeight = hight - displayHight;
                }
                isVisiableForLast = visible;
            }
        });
    }
}


