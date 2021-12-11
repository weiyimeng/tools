package gaosi.com.learn.studentapp.dresscity.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Base64;
import com.gsbaselib.utils.BitmapUtil;
import com.gsbaselib.utils.TypeValue;
import com.gstudentlib.util.hy.HyConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import gaosi.com.learn.bean.dress.dressself.DressSelfMyItem;

/**
 * 装扮自己形象合成
 * Created by dingyz on 2018/11/29.
 */
public class DressSelfMergeView extends AppCompatImageView {

    private Bitmap mShowBitmap;
    private String base64MergeHeader;
    //缓存用户读取图片时使用到的Bitmap图片，减少读取时长
    private HashMap<String, SoftReference<Bitmap>> mDressSelfHashmap = new HashMap<>();

    public DressSelfMergeView(Context context) {
        super(context);
    }

    public DressSelfMergeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DressSelfMergeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 展示
     */
    public synchronized void showDress(DressSelfMyItem dressSelfMyItem) {
        this.base64MergeHeader = "";
        if (mShowBitmap != null && !mShowBitmap.isRecycled()) {
            try {
                mShowBitmap.recycle();
                mShowBitmap = null;
            }catch (Exception e){}
        }
        int i = 0;
        if(!TextUtils.isEmpty(dressSelfMyItem.getBackground())) {
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getEye())) {
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getMouth())) {
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getNose())) {
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getHairStyle())) {
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getCloth())) {
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getGlass())) {
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getHandShow())) {
            i ++;
        }
        Bitmap[] bitmaps = new Bitmap[i];
        i = 0;
        if(!TextUtils.isEmpty(dressSelfMyItem.getBackground())) {
            bitmaps[i] = getBitmapForCode(dressSelfMyItem.getBackground());
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getEye())) {
            bitmaps[i] = getBitmapForCode(dressSelfMyItem.getEye());
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getMouth())) {
            bitmaps[i] = getBitmapForCode(dressSelfMyItem.getMouth());
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getNose())) {
            bitmaps[i] = getBitmapForCode(dressSelfMyItem.getNose());
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getHairStyle())) {
            bitmaps[i] = getBitmapForCode(dressSelfMyItem.getHairStyle());
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getCloth())) {
            bitmaps[i] = getBitmapForCode(dressSelfMyItem.getCloth());
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getGlass())) {
            bitmaps[i] = getBitmapForCode(dressSelfMyItem.getGlass());
            i ++;
        }
        if(!TextUtils.isEmpty(dressSelfMyItem.getHandShow())) {
            bitmaps[i] = getBitmapForCode(dressSelfMyItem.getHandShow());
            i ++;
        }
        try{
            this.mergeBitmap(bitmaps);
            this.setImageBitmap(mShowBitmap);
        } catch (Exception e){
        } catch (OutOfMemoryError e) {
        }
    }

    /**
     * 获取合成图片的base64
     * @return
     */
    public String getBitmapBase64() {
        if(!TextUtils.isEmpty(base64MergeHeader)) {
            return "data:image/png;base64," + base64MergeHeader;
        }
        return "";
    }

    /**
     * 通过图片的code回去bitmap
     * @return
     */
    private Bitmap getBitmapForCode(String code) {
        String filePath = HyConfig.INSTANCE.getHyResourceImagePath() + File.separator + code;
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            if (mDressSelfHashmap.containsKey(code)) {
                SoftReference<Bitmap> softReference = mDressSelfHashmap.get(code);
                bitmap = softReference.get();
                if (bitmap == null || bitmap.isRecycled()) {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                }
            } else {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            }
            mDressSelfHashmap.put(code, new SoftReference(bitmap));
        }catch (Exception e){
            bitmap = null;
        }catch (OutOfMemoryError e) {
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 多张照片合成,至少两张
     * @return
     */
    private void mergeBitmap(Bitmap... bitmaps) {
        if(bitmaps == null || bitmaps.length < 2) {
            return;
        }
        //检测是否存在被回收的图片
        for (Bitmap bitmap : bitmaps) {
            if (bitmap.isRecycled()) {
                return;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitmaps[0].getWidth(), bitmaps[0].getHeight(), bitmaps[0].getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmaps[0], new Matrix(), null);
        for(int i = 0 ; i < bitmaps.length - 1 ; i ++) {
            canvas.drawBitmap(bitmaps[i + 1], 0, 0, null);
        }
        base64MergeHeader = bitmapToString(bitmap);
        if (isPad()) {
            mShowBitmap = BitmapUtil.resizeBitmap(TypeValue.dp2px(320) , TypeValue.dp2px(320) , bitmap);
        } else {
            mShowBitmap = BitmapUtil.resizeBitmap(TypeValue.dp2px(300) , TypeValue.dp2px(300) , bitmap);
        }
    }

    /**
     * 将bitmap生成base64
     * @param bitmap
     * @return
     */
    @SuppressLint("WrongThread")
    private String bitmapToString(Bitmap bitmap) {
        if (bitmap == null) {
            return "";
        }
        // 将Bitmap转换成字符串
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bStream);
        byte[] bytes = bStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 是否为平板true false
     */
    private boolean isPad() {
        return (getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
