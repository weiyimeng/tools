package com.gstudentlib.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.mzule.activityrouter.annotation.Router;
import com.gsbaselib.base.inject.GSAnnotation;
import com.gsbaselib.base.log.LogUtil;
import com.gsbaselib.utils.BitmapUtil;
import com.gsbaselib.utils.LOGGER;
import com.gsbaselib.utils.StatusBarUtil;
import com.gsbaselib.utils.ToastUtil;
import com.gstudentlib.R;
import com.gstudentlib.StatisticsDictionary;
import com.gstudentlib.base.BaseActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

/**
 * 作者：created by 逢二进一 on 2019/9/12 16:24
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
@Router("zoomImage")
@GSAnnotation(pageId = StatisticsDictionary.zoomImage)
public class ZoomImageActivity extends BaseActivity {
    private PhotoView photo_view;
    private ImageView ivSubjectiveState;
    private RelativeLayout mRoot;
    private String url = "";
    //老师点评状态 0老师没评 1对 2半对 3错
    private int answerResult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        mRoot = findViewById(R.id.rootview);
        initView();
        initData();
        photo_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        if (getIntent().hasExtra("data")) {
            url = getIntent().getStringExtra("data");
        }
        if (getIntent().hasExtra("answerResult")) {
            answerResult = getIntent().getIntExtra("answerResult", 0);
        }
        if (url == null) {
            ToastUtil.showToast("图片地址错误");
            return;
        }
        if (url.contains("tiku-static.aixuexi.com")
                || url.contains("ksrc.gaosiedu.com")
                || url.contains("tiku.aixuexi.com/coco/student/latexToSvg")) {
            mRoot.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }
        LogUtil.i("url:" + url);
        if(url.contains("tiku.aixuexi.com/coco/student/latexToSvg")) {
            base64ToBitmapAndShow(url + "&size=13&answer=0&pngimage=1");
        }else {
            Glide.with(mContext).load(url).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                    photo_view.setImageDrawable(drawable);
                    switch (answerResult) {
                        case 1:
                            ivSubjectiveState.setImageResource(R.drawable.icon_subjective_right);
                            ivSubjectiveState.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            ivSubjectiveState.setImageResource(R.drawable.icon_subjective_halfright);
                            ivSubjectiveState.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            ivSubjectiveState.setImageResource(R.drawable.icon_subjective_wrong);
                            ivSubjectiveState.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            });
        }
    }

    /**
     * base64转bitmap
     * @param url
     */
    private void base64ToBitmapAndShow(final String url) {
        OkGo.get(url).execute(new AbsCallback<Object>() {
            @Override
            public Object convertResponse(okhttp3.Response response) throws Throwable {
                return response.body().string();
            }
            @Override
            public void onStart(Request<Object, ? extends Request> request) {
                super.onStart(request);
            }
            @Override
            public void onSuccess(Response<Object> response) {
                if(response != null && response.body() != null) {
                    try {
                        LogUtil.i("response:" + response.body().toString());
                        Bitmap bitmap = BitmapUtil.base64ToBitmap(response.body().toString());
                        photo_view.setImageBitmap(bitmap);
                    }catch (Exception e){
                        LOGGER.log("" , e);
                    }
                }else {
                    ToastUtil.showToast("图片加载失败，请重试！");
                }
            }

            @Override
            public void onError(Response<Object> response) {
                super.onError(response);
                ToastUtil.showToast("图片加载失败，请重试！");
            }
        });
    }

    protected void initView() {
        photo_view = findViewById(R.id.photo_view);
        ivSubjectiveState = findViewById(R.id.ivSubjectiveState);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, mRootView);
    }

}
