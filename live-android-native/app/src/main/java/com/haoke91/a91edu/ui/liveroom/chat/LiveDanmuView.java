package com.haoke91.a91edu.ui.liveroom.chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

import com.blankj.utilcode.util.Utils;
import com.haoke91.baselibrary.emoji.MoonUtil;
import com.haoke91.baselibrary.utils.DensityUtil;
import com.haoke91.videolibrary.R;

import java.util.HashMap;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/10/20 下午3:49
 * 修改人：weiyimeng
 * 修改时间：2018/10/20 下午3:49
 * 修改备注：
 */
public class LiveDanmuView extends DanmakuView {
    private Context mContext;
    //    private boolean mShowDanmu;
    //    private HandlerThread mHandlerThread;
    //    private LiveDanmuView.DanmuHandler mDanmuHandler;
    private BaseDanmakuParser mParser = new BaseDanmakuParser() {
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };
    private DanmakuContext mDanmuContext;
    
    public LiveDanmuView(Context context) {
        super(context);
        this.init(context);
    }
    
    public LiveDanmuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }
    
    public LiveDanmuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context);
    }
    
    private void init(Context context) {
        this.mContext = context;
        configDanmu();
        
        //        this.mDanmakuContext = DanmakuContext.create();
        
        
    }
    
    private void configDanmu() {
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        mDanmuContext = DanmakuContext.create();
        mDanmuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
            .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
            //        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
            .setMaximumLines(maxLinesPair)
            .preventOverlapping(overlappingEnablePair)
            .setDanmakuMargin(40);
        
        //
        //        mDanmuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
        //            .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
        //            //        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
        //            .setMaximumLines(maxLinesPair)
        //            .preventOverlapping(overlappingEnablePair).setDanmakuMargin(40);
        
        //        setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
        //
        //            @Override
        //            public boolean onDanmakuClick(IDanmakus danmakus) {
        //                com.orhanobut.logger.Logger.e("onDanmakuClick====");
        //
        //                BaseDanmaku latest = danmakus.last();
        //                if (null != latest) {
        //                    return false;
        //                }
        //                return false;
        //            }
        //
        //            @Override
        //            public boolean onDanmakuLongClick(IDanmakus danmakus) {
        //                com.orhanobut.logger.Logger.e("onDanmakuLongClick====");
        //
        //                return false;
        //            }
        //
        //            @Override
        //            public boolean onViewClick(IDanmakuView view) {
        //                com.orhanobut.logger.Logger.e("onViewClick====");
        //                return false;
        //            }
        //        });
        this.setCallback(new DrawHandler.Callback() {
            public void prepared() {
                //                LiveDanmuView.this.mShowDanmu = true;
                LiveDanmuView.this.start();
                //                LiveDanmuView.this.generateDanmaku();
            }
            
            public void updateTimer(DanmakuTimer timer) {
            }
            
            public void danmakuShown(BaseDanmaku danmaku) {
            }
            
            public void drawingFinished() {
            }
        });
        this.prepare(mParser, mDanmuContext);
        this.enableDanmakuDrawingCache(true);
    }
    
    
    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {
        
        private Drawable mDrawable;
        
        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
        }
        
        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
        }
    };
    
    public void release() {
        super.release();
        //        this.mShowDanmu = false;
        //        if (this.mDanmuHandler != null) {
        //            this.mDanmuHandler.removeCallbacksAndMessages((Object) null);
        //            this.mDanmuHandler = null;
        //        }
        //
        //        if (this.mHandlerThread != null) {
        //            this.mHandlerThread.quit();
        //            this.mHandlerThread = null;
        //        }
        
    }
    
    private void generateDanmaku() {
        //        this.mHandlerThread = new HandlerThread("Danmu");
        //        this.mHandlerThread.start();
        //        this.mDanmuHandler = new LiveDanmuView.DanmuHandler(this.mHandlerThread.getLooper());
    }
    
    public void addDanmaku(String content, boolean withBorder) {
        BaseDanmaku danmaku = this.mDanmuContext.mDanmakuFactory.createDanmaku(1);
        if (danmaku != null) {
            danmaku.text = MoonUtil.getDrawableSpan(Utils.getApp(), content, ImageSpan.ALIGN_BOTTOM);
            //            danmaku.text = content;
            danmaku.padding = 5;
            danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
            danmaku.textSize = (float) DensityUtil.sp2px(this.mContext, 14.0F);
            danmaku.textColor = -1;
            danmaku.setTime(this.getCurrentTime());
            if (withBorder) {
                danmaku.borderColor = -16711936;
            }
            
            this.addDanmaku(danmaku);
        }
        
    }
    
    
    public void addDanmaKuShowTextAndImage(boolean islive) {
        BaseDanmaku danmaku = mDanmuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        Drawable drawable = getResources().getDrawable(R.mipmap.nim_emoji_del);
        drawable.setBounds(0, 0, 100, 100);
        SpannableStringBuilder spannable = createSpannable(drawable);
        danmaku.text = spannable;
        danmaku.padding = 5;
        danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = islive;
        danmaku.setTime(this.getCurrentTime());
        
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        danmaku.underlineColor = Color.GREEN;
        addDanmaku(danmaku);
    }
    
    private SpannableStringBuilder createSpannable(Drawable drawable) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("图文混排");
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }
    
    public void toggle(boolean on) {
        //        if (on) {
        //            this.mDanmuHandler.sendEmptyMessageAtTime(1001, 100L);
        //        } else {
        //            this.mDanmuHandler.removeMessages(1001);
        //        }
        
    }
    
    //    public class DanmuHandler extends Handler {
    //        public static final int MSG_SEND_DANMU = 1001;
    //
    //        public DanmuHandler(Looper looper) {
    //            super(looper);
    //        }
    //
    //        public void handleMessage(Message msg) {
    //            switch (msg.what) {
    //                case 1001:
    //                    this.sendDanmu();
    //                    int time = (new Random()).nextInt(1000);
    //                    LiveDanmuView.this.mDanmuHandler.sendEmptyMessageDelayed(1001, (long) time);
    //                default:
    //            }
    //        }
    //
    //        private void sendDanmu() {
    //            int time = (new Random()).nextInt(300);
    //            String content = "弹幕" + time + time;
    //            LiveDanmuView.this.addDanmaku(content, false);
    //        }
    //    }
}
