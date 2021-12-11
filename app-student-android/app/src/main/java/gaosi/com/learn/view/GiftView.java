package gaosi.com.learn.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.gsbaselib.base.view.BaseDrawView;
import com.gsbaselib.utils.TypeValue;

/**
 * Created by test on 2018/5/9.
 * 宝箱球进度view
 */
public class GiftView extends BaseDrawView {

    private Paint bgPaint;//背景
    private Paint pgPaint;//进度条
    private int currValue = 0;//当前进度
    private int totalValue = 9;//总进度表
    private float pWidth;//进度条的宽度
    private float radius;//圆的半径
    private float width;//当前view的宽度
    private float height;//当前view的高度
    private static int DEFAULT_ANIMATION_LENGTH = 1000;//动画时长
    private ObjectAnimator mArrivePercentAnimator;
    private float sweepAngle = 0;

    public GiftView(Context context) {
        super(context);
    }

    public GiftView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GiftView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        pWidth = TypeValue.dp2px(2f);//定义进度条宽度
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true); //设置画笔为无锯齿
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setColor(Color.GRAY);
        bgPaint.setAlpha(90);

        pgPaint = new Paint();
        pgPaint.setAntiAlias(true);
        pgPaint.setStyle(Paint.Style.STROKE);
        pgPaint.setColor(Color.parseColor("#52CC52"));

        setLayerToSW(this);//启动软件加速
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mArrivePercentAnimator = ObjectAnimator.ofFloat(GiftView.this,
                    "sweepAngle", 0f);
            mArrivePercentAnimator.setDuration(DEFAULT_ANIMATION_LENGTH);
        }
    }

    @Override
    protected void drawView(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.left = 0 + pWidth;
        rectF.top = 0 + pWidth;
        rectF.right = width - pWidth;
        rectF.bottom = height - pWidth;
        canvas.drawArc(rectF
                , - 90
                , sweepAngle
                , false
                , pgPaint);
    }

    @SuppressLint("NewApi")
    private void stopChangingPercent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mArrivePercentAnimator.isRunning())
                mArrivePercentAnimator.cancel();
        }
    }

    /**
     * 刷新当前的进度值
     * @param currValue
     */
    public void refreshGifeView(int currValue) {
        this.currValue = currValue;
        if(currValue < 0){
            this.currValue = 0;
        }
        if(currValue > totalValue){
            this.currValue = totalValue;
        }
        float sweepAngle = 360 * ((float)currValue / totalValue);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerToHW(this);
            stopChangingPercent();
            mArrivePercentAnimator.setFloatValues(0f , sweepAngle);
            mArrivePercentAnimator.start();
        } else {
            setSweepAngle(sweepAngle);
        }
    }

    /**
     * 设置进度总值
     * @param totalValue
     */
    public void setTotalValue(int totalValue)
    {
        this.totalValue = totalValue;
        if(totalValue <= 0) {
            this.totalValue = 9;
        }
        refreshGifeView(currValue);
    }

    /**
     * 设置旋转角度
     * @param sweepAngle
     */
    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
    }

    @Override
    protected void initSize(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
        if (w != h) {
            new RuntimeException("GifeView定义宽高不一致");
        }
        radius = (width - pWidth * 2) / 2;//计算进度条的半径
        bgPaint.setStrokeWidth(pWidth);
        pgPaint.setStrokeWidth(pWidth);
    }
}
