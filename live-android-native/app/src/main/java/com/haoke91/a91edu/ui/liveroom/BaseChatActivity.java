package com.haoke91.a91edu.ui.liveroom;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.BasePagerAdapter;
import com.haoke91.a91edu.adapter.LiveRoomAdapter;
import com.haoke91.a91edu.entities.GetImHistoryResponse;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.liveroom.chat.MessageBuilder;
import com.haoke91.a91edu.ui.liveroom.chat.MessageListView;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.dialog.DialogUtil;
import com.haoke91.a91edu.widget.gift.GiftModel;
import com.haoke91.a91edu.widget.gift.LiveGiftLayout;
import com.haoke91.baselibrary.utils.ACallBack;
import com.haoke91.baselibrary.utils.ICallBack;
import com.haoke91.im.mqtt.IMManager;
import com.haoke91.im.mqtt.entities.Message;
import com.haoke91.videolibrary.videoplayer.BaseVideoPlayer;
import com.haoke91.videolibrary.videoplayer.LiveMessageCallBack;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/10/29 上午11:26
 * 修改人：weiyimeng
 * 修改时间：2018/10/29 上午11:26
 * 修改备注：
 */
public abstract class BaseChatActivity<T extends BaseVideoPlayer> extends BasePlayerActivity<T> implements MessageListView.MessageCallBack {
    protected MessageListView messageList;
    //    protected FrameLayout mFl_dialogParent;
    protected LiveGiftLayout gift_layout;
    
    @Override
    public void initialize() {
        BaseVideoPlayer.MediaControl mediaControl = player.getMediaControl();
        messageList = findViewById(R.id.view_chat_list);
        messageList.setMediaControl(mediaControl);
        player.setMediaCallBak(callBack);
        gift_layout = findViewById(R.id.gift_layout);
        //        showList();
        //        showQuestion();
        super.initialize();
        messageList.setMessageCallBack(this);
    }
    
    
    private LiveMessageCallBack callBack = new LiveMessageCallBack() {
        @Override
        public void onDanmuToggle() {
            if (mDanmuView != null) {
                mDanmuView.toggle();
            }
        }
        
        @Override
        public void onPraise() {
            Message msg = MessageBuilder.createPriseMessage();
            if (mPlayerPresenter.sendLike(msg)) {
                GiftModel giftModel = new GiftModel(R.drawable.ic_prise, "赞", UserManager.getInstance().getLoginUser().getName(), 1, UserManager.getInstance().getLoginUser().getSmallHeadimg());
                gift_layout.addGift(giftModel);
                messageList.addMessage(msg);
            }
        }
        
        @Override
        public void onFlower() {
            Message msg = MessageBuilder.createFlowerMessage();
            if (mPlayerPresenter.sendFlower(msg)) {
                GiftModel giftModel = new GiftModel(R.drawable.ic_flower, "花", UserManager.getInstance().getLoginUser().getName(), 1, UserManager.getInstance().getLoginUser().getSmallHeadimg());
                gift_layout.addGift(giftModel);
                messageList.addMessage(msg);
            }
        }
        
        @Override
        public void onSendMessage(@NotNull String message) {
            Message msg = MessageBuilder.createTextMessage(message);
            if (mPlayerPresenter.sendMessage(msg)) {
                messageList.addMessage(msg);
                mDanmuView.addDanmaku(message, false);
            }
        }
    };
    
    @Override
    public void onTextMessage(Message msg) {
        messageList.addMessage(msg);
    }
    
    @Override
    public void onGetHistory(List<Message> messages) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        Collections.reverse(messages);
        messageList.addMessages(messages);
    }
    
    @Override
    public void onLoadHistory() {
        long time = 0L;
        if (messageList.getData() == null || messageList.getData().size() == 0) {
            time = 0L;
        } else {
            time = messageList.getData().get(0).getTime();
        }
        mPlayerPresenter.onLoginSuccess(time);
    }
    
    @Override
    public void onSendMessage(String txt) {
        Message msg = MessageBuilder.createTextMessage(txt);
        if (mPlayerPresenter.sendMessage(msg)) {
            messageList.addMessage(msg);
            messageList.showView(true);
//            mDanmuView.addDanmaku(txt, false);
        }
    }
    
    @Override
    public void onSendLike() {
        Message msg = MessageBuilder.createPriseMessage();
        if (mPlayerPresenter.sendLike(msg)) {
            GiftModel giftModel = new GiftModel(R.drawable.ic_prise, "赞", UserManager.getInstance().getLoginUser().getName(), 1, UserManager.getInstance().getLoginUser().getSmallHeadimg());
            gift_layout.addGift(giftModel);
            messageList.addMessage(msg);
            messageList.showView(true);
        }
    }
    
    @Override
    public void onSendFlower() {
        Message msg = MessageBuilder.createFlowerMessage();
        if (mPlayerPresenter.sendFlower(msg)) {
            GiftModel giftModel = new GiftModel(R.drawable.ic_flower, "花", UserManager.getInstance().getLoginUser().getName(), 1, UserManager.getInstance().getLoginUser().getSmallHeadimg());
            gift_layout.addGift(giftModel);
            messageList.addMessage(msg);
            messageList.showView(true);
        }
    }
    
    @Override
    public void onForbidden(boolean isForbidden) {
        messageList.forbidden(isForbidden);
    }
    
    @Override
    public void onWithdrewText(String id) {
        messageList.deleteMessage(id);
    }
    
    
    @Override
    public void onBackPressed() {
        if (mFl_dialogParent != null && mFl_dialogParent.getVisibility() == View.VISIBLE) {
            if (mFl_dialogParent.getChildCount() > 0) {
                mFl_dialogParent.removeAllViews();
            }
            mFl_dialogParent.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
        
        
    }
}
