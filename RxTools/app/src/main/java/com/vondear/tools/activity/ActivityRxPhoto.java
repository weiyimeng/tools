package com.vondear.tools.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.vondear.rxtools.RxBarTool;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.vondear.rxtools.view.dialog.RxDialogScaleView;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.tools.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.TITLE;

/**
 * @author vondear
 */
public class ActivityRxPhoto extends ActivityBase {


    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.tv_bg)
    TextView mTvBg;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.ll_anchor_left)
    LinearLayout mLlAnchorLeft;
    @BindView(R.id.rl_avatar)
    RelativeLayout mRlAvatar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_constellation)
    TextView mTvConstellation;
    @BindView(R.id.tv_birthday)
    TextView mTvBirthday;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_lables)
    TextView mTvLables;
    @BindView(R.id.textView2)
    TextView mTextView2;
    @BindView(R.id.editText2)
    TextView mEditText2;
    @BindView(R.id.btn_exit)
    Button mBtnExit;
    @BindView(R.id.activity_user)
    LinearLayout mActivityUser;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        setContentView(R.layout.activity_von_photo);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        Resources r = mContext.getResources();
        resultUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.drawable.circle_elves_ball) + "/"
                + r.getResourceTypeName(R.drawable.circle_elves_ball) + "/"
                + r.getResourceEntryName(R.drawable.circle_elves_ball));

        mRxTitle.setLeftFinish(mContext);

        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDialogChooseImage();
            }
        });
        mIvAvatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                RxImageTool.showBigImageView(mContext, resultUri);
                RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(mContext);
                rxDialogScaleView.setImage(resultUri);
                rxDialogScaleView.show();
                return false;
            }
        });
    }

    private void initDialogChooseImage() {
        RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(mContext, TITLE);
        dialogChooseImage.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://???????????????????????????
                if (resultCode == RESULT_OK) {
//                    RxPhotoTool.cropImage(ActivityUser.this, );// ????????????
                    initUCrop(data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://??????????????????????????????
                if (resultCode == RESULT_OK) {
                    /* data.getExtras().get("data");*/
//                    RxPhotoTool.cropImage(ActivityUser.this, RxPhotoTool.imageUriFromCamera);// ????????????
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                }

                break;
            case RxPhotoTool.CROP_IMAGE://????????????????????????
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.circle_elves_ball)
                        //???????????????(???????????????????????????????????????)
                        .error(R.drawable.circle_elves_ball)
                        //??????Glide??????????????????
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

                Glide.with(mContext).
                        load(RxPhotoTool.cropImageUri).
                        apply(options).
                        thumbnail(0.5f).
                        into(mIvAvatar);
//                RequestUpdateAvatar(new File(RxPhotoTool.getRealFilePath(mContext, RxPhotoTool.cropImageUri)));
                break;

            case UCrop.REQUEST_CROP://UCrop?????????????????????
                if (resultCode == RESULT_OK) {
                    resultUri = UCrop.getOutput(data);
                    roadImageView(resultUri, mIvAvatar);
                    RxSPTool.putContent(mContext, "AVATAR", resultUri.toString());
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;
            case UCrop.RESULT_ERROR://UCrop???????????????????????????
                final Throwable cropError = UCrop.getError(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //???Uri??????????????? ??????????????????File????????????
    private File roadImageView(Uri uri, ImageView imageView) {
        Glide.with(mContext).
                load(uri).
                thumbnail(0.5f).
                into(imageView);

        return (new File(RxPhotoTool.getImageAbsolutePath(this, uri)));
    }

    private void initUCrop(Uri uri) {
        //Uri destinationUri = RxPhotoTool.createImagePathUri(this);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //????????????????????????????????????
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //???????????????????????????????????????
        //options.setHideBottomControls(true);
        //??????toolbar??????
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //?????????????????????
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark));

        //????????????
        //????????????????????????
        options.setMaxScaleMultiplier(5);
        //???????????????????????????????????????
        options.setImageToCropBoundsAnimDuration(666);
        //?????????????????????????????????
        //options.setOvalDimmedLayer(true);
        //?????????????????????????????????
        // options.setShowCropFrame(false);
        //?????????????????????????????????
        //options.setCropGridStrokeWidth(20);
        //?????????????????????????????????
        //options.setCropGridColor(Color.GREEN);
        //?????????????????????
        //options.setCropGridColumnCount(2);
        //?????????????????????
        //options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }

    @OnClick(R.id.btn_exit)
    public void onClick() {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(this);
        rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rxDialogSureCancel.show();
    }
}
