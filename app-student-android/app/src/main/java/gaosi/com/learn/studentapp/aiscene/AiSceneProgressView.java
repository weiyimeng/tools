package gaosi.com.learn.studentapp.aiscene;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import com.gsbaselib.base.view.BaseDrawView;
import com.gsbaselib.utils.TypeValue;

import gaosi.com.learn.studentapp.aiscene.core.OnVoiceProgressListener;

/**
 * Created by test on 2018/6/20.
 * 录音进度控制
 */
public class AiSceneProgressView extends BaseDrawView {

    private Paint mPaint;
    private float offset = 0;
    private int mTotal = 38;
    private int width;
    private int height;
    private int lineWidth;
    private PointF middlePosition = new PointF();
    private ValueAnimator mValueAnimator;

    public AiSceneProgressView(Context context) {
        super(context);
    }

    public AiSceneProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AiSceneProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        lineWidth = TypeValue.dp2px(4);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#49BB58"));
        mPaint.setAlpha(150);
        mPaint.setStrokeWidth(lineWidth);
        mValueAnimator = ValueAnimator.ofFloat(0);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (float)animation.getAnimatedValue();
                if(mOnVoiceProgressListener != null) {
                    mOnVoiceProgressListener.onProgress(offset);
                }
                invalidate();
                if(offset == mTotal) {
                    stop();
                }
            }
        });
        this.setTotal(38);
    }

    @Override
    protected void drawView(Canvas canvas) {
            RectF rectF = new RectF();
            rectF.left = lineWidth / 2.0f;
            rectF.top = lineWidth / 2.0f;
            rectF.right = (float) width - lineWidth / 2.0f;
            rectF.bottom = (float) height - lineWidth / 2.0f;
            canvas.drawArc(rectF
                    , -90
                    , (offset / mTotal) * 360
                    , false
                    , mPaint);
    }

    /**
     * 设置总秒数
     * @param total
     */
    private void setTotal(int total) {
        if(total <= 0) {
            total = 0;
        }
        this.mTotal = total;
        mValueAnimator.setFloatValues(0f , this.mTotal);
        mValueAnimator.setDuration(this.mTotal * 1000);
    }

    @Override
    protected void initSize(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
        middlePosition.set(width / 2 , height / 2);
    }

    /**
     * 开始
     */
    public void start() {
        if(mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
        mValueAnimator.start();
    }

    /**
     * 结束
     */
    public void stop() {
        if(mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
            if(mOnVoiceProgressListener != null) {
                mOnVoiceProgressListener.onStop();
            }
        }
        offset = 0;
        invalidate();
    }

    /**
     * 重置
     */
    public void reset() {
        if(mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
        offset = 0;
        invalidate();
    }

    /**
     * 暂停 ，暂时不使用
     */
    public void pause() {
        if(!mValueAnimator.isRunning()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mValueAnimator.pause();
        }
    }

    /**
     * 继续 ，暂时不使用
     */
    public void resume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(!mValueAnimator.isPaused()) {
                return;
            }
            mValueAnimator.resume();
        }
    }

    /**
     * 录音进度监听
     * @param onVoiceProgressListener
     */
    public void setOnVoiceProgressListener(OnVoiceProgressListener onVoiceProgressListener) {
        this.mOnVoiceProgressListener = onVoiceProgressListener;
    }
    private OnVoiceProgressListener mOnVoiceProgressListener;
}
