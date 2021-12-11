package com.haoke91.a91edu.ui.liveroom.chat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.baselibrary.utils.ACallBack;
import com.haoke91.baselibrary.views.popwindow.CommentPopup;
import com.haoke91.im.mqtt.entities.Constant;
import com.haoke91.im.mqtt.entities.Message;
import com.haoke91.videolibrary.MessageClickCallback;
import com.haoke91.videolibrary.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import me.drakeet.multitype.Linker;


/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/2 上午10:37
 * 修改人：weiyimeng
 * 修改时间：2018/7/2 上午10:37
 * 修改备注：
 */
public class MessageListView extends RelativeLayout {
    private static final int TYPE_TEXT = 0;
    private static final int TYPE_PRISE = 1;
    private static final int TYPE_SYS = 2;
    
    private static final int TYPE_UNKONW = 3;
    
    private WrapRecyclerView rv_chat_message;
    private ImageView iv_chat_back, iv_chat_open;
    private RelativeLayout fl_chat_list;
    private MultiMessageAdapter adapter;
    private ArrayList<Message> datas;
    //private Handler handler;
    private MessageClickCallback mMediaControl;
    private SwipeRefreshLayout mRefreshLayout;
    private float rawDownY;
    private long lastClickTime;
    
    //点赞、送花、input
    private LinearLayout ll_input;
    private TextView tvInput, tvOnlyTeacher;
    private CommentPopup mCommentPopup;
    private boolean commentDismiss = true;
    
    public MessageListView(Context context) {
        super(context);
        initView(context);
    }
    
    public MessageListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        
    }
    
    private void initView(Context context) {
        View.inflate(context, R.layout.layout_chat_message, this);
        fl_chat_list = findViewById(R.id.fl_chat_list);
        rv_chat_message = findViewById(R.id.rv_chat_message);
        iv_chat_open = findViewById(R.id.iv_chat_open);
        iv_chat_back = findViewById(R.id.iv_chat_close);
        tvInput = findViewById(R.id.tv_input);
        ImageView ivFlower = findViewById(R.id.iv_flower);
        ImageView ivLike = findViewById(R.id.iv_like);
        tvOnlyTeacher = findViewById(R.id.tv_onlyTeacher);
        iv_chat_back.setOnClickListener(onClickListener);
        iv_chat_open.setOnClickListener(onClickListener);
        tvInput.setOnClickListener(onClickListener);
        ivLike.setOnClickListener(onClickListener);
        ivFlower.setOnClickListener(onClickListener);
        tvOnlyTeacher.setOnClickListener(onClickListener);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        ll_input = findViewById(R.id.ll_input);
        mDragView = fl_chat_list;
        datas = new ArrayList();
        data_teacher = new ArrayList();
        adapter = new MultiMessageAdapter(datas);
        rv_chat_message.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //  handler.sendEmptyMessageDelayed(BaseVideoPlayer.MSG_HIDE_CONTROLLER, BaseVideoPlayer.TIME_SHOW_CONTROLLER);
                    rawDownY = event.getRawY();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (Math.abs((int) (rawDownY - event.getRawY())) < 40) {
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        if (currentTime - lastClickTime > 300) {
                            lastClickTime = currentTime;
                            //  mMediaControl.sendHandlerMessage(0);
                            mMediaControl.onMessViewClick();
                        }
                    }
                    
                }
                return false;
            }
        });
        adapter.register(Message.class)
            .to(new TextMessageProvider(adapter), new PriseMessageProvider(adapter), new SystemMessageProvider(adapter), new UnknowMessageProvider(adapter))
            .withLinker(new Linker<Message>() {
                @Override
                public int index(int position, @NonNull Message message) {
                    if ("text".equalsIgnoreCase(message.getFlag())) {
                        return TYPE_TEXT;
                    } else if ("like".equalsIgnoreCase(message.getFlag()) || "flower".equalsIgnoreCase(message.getFlag())) {
                        return TYPE_PRISE;
                    } else if ("sys".equalsIgnoreCase(message.getFlag())) {
                        return TYPE_SYS;
                    }
                    return TYPE_UNKONW;
                }
            });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_chat_message.setLayoutManager(linearLayoutManager);
        rv_chat_message.setAdapter(adapter);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mMessageCallBack != null) {
                    mMessageCallBack.onLoadHistory();
                }
            }
        });
        //        fl_chat_list.post(new Runnable() {
        //            @Override
        //            public void run() {
        //                fl_chat_list.setTranslationX(fl_chat_list.getWidth());
        //                iv_chat_open.setVisibility(View.VISIBLE);
        //                changeInputLayout(true);
        //            }
        //        });
        
        com.haoke91.baselibrary.utils.KeyboardUtils.registerSoftInputChangedListener((Activity) getContext(), new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height < 100) {
                    if (!ObjectUtils.isEmpty(mCommentPopup) && commentDismiss) {
                        mCommentPopup.dismiss();
                    }
                } else if (!ObjectUtils.isEmpty(mCommentPopup)) {
                    mCommentPopup.setEmojiLayoutGone();
                    commentDismiss = true;
                }
            }
        });
        
    }
    
    public List<Message> getData() {
        return datas;
    }
    
    /**
     * 加入历史消息
     *
     * @param _messages
     */
    public void addMessages(List<Message> _messages) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        }
        if (_messages == null || _messages.size() == 0) {
            return;
        }
        datas.addAll(0, _messages);
        if (onlySeeTeacher) {
            if (data_teacher == null) {
                data_teacher = new ArrayList<>();
            }
            List<Message> list = new ArrayList<>();
            for (Message m : _messages) {
                if (Constant.Role.TEACHER.getValue().equalsIgnoreCase(m.getRole())) {
                    list.add(m);
                }
            }
            if (list.size() > 0) {
                data_teacher.addAll(0, list);
            }
            adapter.notifyItemRangeChanged(0, _messages.size());
            rv_chat_message.scrollToPosition(data_teacher.size() - 1);
            
        } else {
            if (!ObjectUtils.isEmpty(datas)) {
                adapter.notifyItemRangeChanged(0, _messages.size());
                rv_chat_message.scrollToPosition(datas.size() - 1);
            }
        }
    }
    
    /**
     * 删除消息
     *
     * @param id
     */
    public void deleteMessage(String id) {
        Message origin;
        if (datas != null && datas.size() > 0) {
            for (Message msg : datas) {
                if (msg.getMsgId() != null && msg.getMsgId().equalsIgnoreCase(id)) {
                    datas.remove(msg);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
    
    /**
     * 显示隐藏聊天区
     *
     * @param show
     */
    public void showView(Boolean show) {
        if (show && iv_chat_open.getVisibility() == View.VISIBLE) {
            iv_chat_open.performClick();
        } else {
            if (iv_chat_back.getVisibility() == View.VISIBLE && !show) {
                iv_chat_back.performClick();
            }
        }
    }
    
    /**
     * 插入单条消息
     *
     * @param message
     */
    public void addMessage(Message message) {
        datas.add(message);
        if (onlySeeTeacher) {
            if (data_teacher == null) {
                data_teacher = new ArrayList<>();
            }
            if (Constant.Role.TEACHER.getValue().equalsIgnoreCase(message.getRole())) {
                data_teacher.add(message);
                adapter.notifyItemInserted(data_teacher.size() - 1);
                rv_chat_message.scrollToPosition(data_teacher.size() - 1);
            }
            
        } else {
            if (!ObjectUtils.isEmpty(datas)) {
                adapter.notifyItemInserted(datas.size() - 1);
                rv_chat_message.scrollToPosition(datas.size() - 1);
            }
        }
    }
    
    private boolean onlySeeTeacher = false;
    private ArrayList<Message> data_teacher;
    
    public void onlySeeTeacher(boolean onlySeeTeacher) {
        this.onlySeeTeacher = onlySeeTeacher;
        if (onlySeeTeacher) {
            data_teacher.clear();
            Observable.fromIterable(datas).filter(new Predicate<Message>() {
                @Override
                public boolean test(Message message) throws Exception {
                    return Constant.Role.TEACHER.getValue().equalsIgnoreCase(message.getRole());
                }
            }).subscribe(new Consumer<Message>() {
                @Override
                public void accept(Message message) throws Exception {
                    data_teacher.add(message);
                }
            });
            adapter.setItems(data_teacher);
        } else {
            adapter.setItems(datas);
            
        }
        adapter.notifyDataSetChanged();
    }
    
    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_chat_close) {
                
                ObjectAnimator animator = ObjectAnimator.ofFloat(fl_chat_list, "translationX", 0, fl_chat_list.getWidth());
                
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        iv_chat_open.setVisibility(VISIBLE);
                        changeInputLayout(true);
                    }
                });
                animator.setDuration(300);
                animator.start();
                
                
            } else if (v.getId() == R.id.iv_chat_open) {
                
                ObjectAnimator animator = ObjectAnimator.ofFloat(fl_chat_list, "translationX", fl_chat_list.getWidth(), 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        iv_chat_open.setVisibility(GONE);
                        changeInputLayout(false);
                    }
                });
                animator.setDuration(300);
                animator.start();
            } else if (v.getId() == R.id.tv_input) {
                ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                if (ObjectUtils.isEmpty(mCommentPopup)) {
                    mCommentPopup = CommentPopup.create(getContext())
                        .setFocusAndOutsideEnable(false)
                        .setOutsideTouchable(false)
                        .setBackgroundDimEnable(false)
                        .setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                mCommentPopup.setEmojiLayoutGone();
                                if (KeyboardUtils.isSoftInputVisible((Activity) getContext())) {
                                    KeyboardUtils.toggleSoftInput();
                                }
                            }
                        })
                        .setOnActionClickListener(new CommentPopup.OnSendMessageListener() {
                            @Override
                            public void OnSendMessage(String message) {
                                mCommentPopup.dismiss();
                                tvInput.setText("");
                                if (mMessageCallBack != null) {
                                    mMessageCallBack.onSendMessage(message);
                                }
                            }
                            
                            @Override
                            public void onChangeLayout(int type) {
                                commentDismiss = false;
                            }
                        })
                        .apply();
                }
                mCommentPopup
                    .showSoftInput()
                    .showAtLocation(v, Gravity.BOTTOM, 0, 0);
            } else if (v.getId() == R.id.iv_like) {
                if (mMessageCallBack != null) {
                    mMessageCallBack.onSendLike();
                }
            } else if (v.getId() == R.id.iv_flower) {
                if (mMessageCallBack != null) {
                    mMessageCallBack.onSendFlower();
                }
            } else if (v.getId() == R.id.tv_onlyTeacher) {
                //                tvOnlyTeacher.setText(onlySeeTeacher ? "仅看老师" : "查看全部");
                onlySeeTeacher = !onlySeeTeacher;
                tvOnlyTeacher.setSelected(onlySeeTeacher);
                tvOnlyTeacher.setTextColor(onlySeeTeacher ? ContextCompat.getColor(getContext(), R.color.color_75C82B) : ContextCompat.getColor(getContext(), R.color.white));
                onlySeeTeacher(onlySeeTeacher);
            }
            
        }
        
    };
    
    private void changeInputLayout(boolean isAlpha) {
        if (isAlpha) {
            ObjectAnimator.ofFloat(ll_input, "alpha", 1, 0.6f).setDuration(300).start();
        } else {
            ObjectAnimator.ofFloat(ll_input, "alpha", 0.6f, 1f).setDuration(300).start();
        }
    }
    
    private float downX, downY, endX, endY;
    
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean interceptTouchEvent = false;
        //        super.onInterceptTouchEvent(ev);
        if (isPointOnViews(iv_chat_back, ev) || isPointOnViews(iv_chat_open, ev) || iv_chat_open.getVisibility() == VISIBLE) {
            return false;
        }
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                //                mDragView = fl_chat_list;
                //                //标记状态为拖拽，并记录上次触摸坐标
                mCurrentState = State.DRAGGING;
                mLastPointX = downX;
                mDragViewOrigX = downY;
                //                fl_chat_list.setTranslationX(0);
                invalidate();
                //                ViewCompat.offsetLeftAndRight(fl_chat_list, fl_chat_list.getLeft());
                
                interceptTouchEvent = false;
                break;
            case MotionEvent.ACTION_MOVE:
                endX = ev.getX();
                endY = ev.getY();
                
                if (Math.abs(endY - downY) >= Math.abs(endX - downX)) {
                    interceptTouchEvent = false;
                } else {
                    interceptTouchEvent = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                interceptTouchEvent = false;
                break;
        }
        
        return interceptTouchEvent;
    }
    
    enum State {
        IDLE,
        DRAGGING
    }
    
    // 记录手指上次触摸的坐标
    private float mLastPointX;
    private float mStartPointX;
    private float mDragViewOrigX;
    
    
    //用于识别最小的滑动距离
    // 用于标识正在被拖拽的 child，为 null 时表明没有 child 被拖拽
    private View mDragView;
    State mCurrentState;
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (iv_chat_open.getVisibility() == VISIBLE) {
            return false;
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDragView = fl_chat_list;
                //标记状态为拖拽，并记录上次触摸坐标
                mCurrentState = State.DRAGGING;
                mLastPointX = event.getX();
                mDragViewOrigX = mDragView.getX();
                mStartPointX = event.getX();
                //                return false;
                break;
            
            case MotionEvent.ACTION_MOVE:
                //                Log.e("tag", " mDragView.getTranslationX()==" + mDragView.getTranslationX() + "==getX" + mDragView.getX());
                int deltaX = (int) (event.getX() - mLastPointX);
                if (mDragView != null && mDragView.getX() >= 0) {
                    ViewCompat.offsetLeftAndRight(fl_chat_list, deltaX);
                    mLastPointX = event.getX();
                }
                break;
            
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mCurrentState == State.DRAGGING && Math.abs(event.getX() - mStartPointX) > 20) {
                    int deltaXx = (int) (event.getX() - mDragViewOrigX);
                    if (mDragView != null) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(fl_chat_list, "translationX", mDragView.getTranslationX(), fl_chat_list.getWidth());
                        
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                iv_chat_open.setVisibility(VISIBLE);
                                changeInputLayout(true);
                                //                                invalidate();
                                
                            }
                        });
                        animator.setDuration(300);
                        animator.start();
                    }
                    mLastPointX = 0;
                    mDragViewOrigX = 0;
                    mCurrentState = State.IDLE;
                    
                } else {
                    mLastPointX = 0;
                    mDragViewOrigX = 0;
                    mCurrentState = State.IDLE;
                }
                break;
        }
        return true;
    }
    
    /**
     * 判断触摸的位置是否落在 child 身上
     */
    private boolean isPointOnViews(MotionEvent ev) {
        boolean result = false;
        Rect rect = new Rect();
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View view = getChildAt(i);
            rect.set((int) view.getX(), (int) view.getY(), (int) view.getX() + (int) view.getWidth()
                , (int) view.getY() + view.getHeight());
            
            if (rect.contains((int) ev.getX(), (int) ev.getY())) {
                //标记被拖拽的child
                mDragView = view;
                mDragViewOrigX = mDragView.getX();
                result = true;
                break;
            }
        }
        
        return result && mCurrentState != State.DRAGGING;
    }
    
    private boolean isPointOnViews(View view, MotionEvent ev) {
        boolean result = false;
        Rect rect = new Rect();
        //        for (int i = getChildCount() - 1; i >= 0; i--) {
        //            View view = getChildAt(i);
        rect.set((int) view.getX(), (int) view.getY(), (int) view.getX() + view.getWidth()
            , (int) view.getY() + view.getHeight());
        
        if (rect.contains((int) ev.getX(), (int) ev.getY())) {
            //标记被拖拽的child
            //            mDragView = view;
            //            mDragViewOrigX = mDragView.getX();
            result = true;
            //                break;
        }
        //        }
        
        return result && mCurrentState != State.DRAGGING;
    }
    
    //    @Override
    //    public boolean onInterceptTouchEvent(MotionEvent ev) {
    //        return mDragHelper.shouldInterceptTouchEvent(ev);
    //    }
    //
    //    @Override
    //    public boolean onTouchEvent(MotionEvent event) {
    //        mDragHelper.processTouchEvent(event);
    //        return true;
    //    }
    
    /**
     * 禁言
     *
     * @param isForbidden
     */
    public void forbidden(boolean isForbidden) {
        tvInput.setEnabled(!isForbidden);
        if (isForbidden) {
            tvInput.setHint("您已被禁言");
        } else {
            tvInput.setHint(getResources().getString(R.string.say_something));
        }
    }
    
    public void setMediaControl(MessageClickCallback mediaControl) {
        mMediaControl = mediaControl;
    }
    
    //    public void setHandler(Handler handler) {
    //        this.handler = handler;
    //    }
    
    //    public interface MessageClickCallback {
    //        void onMessViewClick();
    //
    //        void onLoadHistory();
    //    }
    
    public interface MessageCallBack {
        void onLoadHistory();
        
        void onSendMessage(String txt);
        
        void onSendLike();
        
        void onSendFlower();
    }
    
    private MessageCallBack mMessageCallBack;
    
    public void setMessageCallBack(MessageCallBack callBack) {
        mMessageCallBack = callBack;
    }
}

