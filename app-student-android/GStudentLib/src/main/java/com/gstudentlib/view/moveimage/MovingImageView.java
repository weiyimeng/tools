package com.gstudentlib.view.moveimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.gsbaselib.base.log.LogUtil;
import com.gstudentlib.R;

/**
 * 作者：created by 逢二进一 on 2019/8/5 17:11
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
public class MovingImageView extends AppCompatImageView {

    private float canvasWidth, canvasHeight;
    private float imageWidth, imageHeight;
    private float offsetWidth;

    public MovingImageView(Context context) {
        this(context, null);
    }

    public MovingImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setScaleType(ScaleType.MATRIX);
    }

    /**
     * 更新canvas size
     *
     * @param w    new width.
     * @param h    new height.
     * @param oldW old width.
     * @param oldH old height.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        canvasWidth = (float) w - (float) (getPaddingLeft() + getPaddingRight());
        canvasHeight = (float) h - (float) (getPaddingTop() + getPaddingBottom());
        this.updateAll();
    }

    private void updateAll() {
        if (getDrawable() != null) {
            imageWidth = getDrawable().getIntrinsicWidth();//获取图片高度
            imageHeight = getDrawable().getIntrinsicHeight();//获取图片宽度
            offsetWidth = (imageWidth - canvasWidth) > 0 ? imageWidth - canvasWidth : 0;
            updateAnimatorValues();
        }
    }

    /**
     * 更新动画基本数据
     */
    private void updateAnimatorValues() {
        if (canvasHeight == 0 && canvasWidth == 0)
            return;
        calculateTypeAndScale();
    }

    /**
     * 计算缩放比例
     *
     * @return image scale.
     */
    private float calculateTypeAndScale() {
        Matrix matrix = new Matrix();
        float scale = canvasHeight / imageHeight; //以高度填充作为缩放比例
        matrix.preScale(1.0f, scale);
        setImageMatrix(matrix);
        return scale;
    }

    /**
     * 更新背景位置
     *
     * @param offset
     * @return
     */
    public void updatePosition(int offset) {
        int curr = getScrollX();
        offset = offset + curr;
        if(offset >= offsetWidth) {
            offset = (int) offsetWidth;
        }
        if(offset <= 0) {
            offset = 0;
        }
        this.setScrollX(offset);
    }

    /**
     * 居中显示
     */
    public void showMiddle() {
        this.setScrollX((int) offsetWidth / 2);
    }

    /**
     * 禁止设置ScaleType
     *
     * @param scaleType
     */
    @Override
    @Deprecated
    public void setScaleType(ScaleType scaleType) {
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        this.updateAll();
        this.showMiddle();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        this.updateAll();
        this.showMiddle();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        this.updateAll();
        this.showMiddle();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.updateAll();
        this.showMiddle();
    }
}
