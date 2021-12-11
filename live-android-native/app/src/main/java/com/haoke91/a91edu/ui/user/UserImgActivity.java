package com.haoke91.a91edu.ui.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.haoke91.a91edu.GlobalConfig;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.entities.UserInfo;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.utils.imageloader.GlideUtils;
import com.haoke91.a91edu.utils.imageloader.MediaLoader;
import com.haoke91.a91edu.utils.manager.OssManager;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.ChoicePicDialog;
import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.event.RxBus;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.durban.Controller;
import com.yanzhenjie.durban.Durban;

import java.util.ArrayList;
import java.util.Locale;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建时间：2018/7/11 下午4:32
 * 修改时间：2018/7/11 下午4:32
 * 修改备注：
 */
public class UserImgActivity extends BaseActivity {
    private ImageView iv_user_head;
    private UserInfo userInfo;
    
    public static void start(Context context) {
        Intent intent = new Intent(context, UserImgActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        userInfo = UserManager.getInstance().getLoginUser();
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_userimage;
    }
    
    @Override
    public void initialize() {
        iv_user_head = findViewById(R.id.iv_user_head);
        ImageView  tv_more = findViewById(R.id.tv_more);
        tv_more.setOnClickListener(onClickListener);
        GlideUtils.loadHead(this, userInfo.getSmallHeadimg(), iv_user_head);
    }
    
    ChoicePicDialog choicePicDialog;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            choicePicDialog = ChoicePicDialog.showDialog(UserImgActivity.this);
            choicePicDialog.setOnSelectListener(new ChoicePicDialog.OnCamreaSelectListener() {
                @Override
                public void onPicSelect(boolean isCamrea) {
                    if (isCamrea) {
                        Album.camera(UserImgActivity.this)
                            .image()
                            .onResult(new Action<String>() {
                                @Override
                                public void onAction(@NonNull String result) {
                                    Logger.e("result===" + result);
                                    cropImage(result);
                                }
                            })
                            .onCancel(new Action<String>() {
                                @Override
                                public void onAction(@NonNull String result) {
                                
                                }
                            })
                            .start();
                        
                    } else {
                        Album.initialize(AlbumConfig.newBuilder(UserImgActivity.this)
                            .setAlbumLoader(new MediaLoader())
                            .setLocale(Locale.getDefault())
                            .build());
                        Album.album(UserImgActivity.this)
                            .singleChoice()
                            .columnCount(3)
                            .camera(false)
                            .widget(
                                Widget.newDarkBuilder(UserImgActivity.this)
                                    .title("相册")
                                    .statusBarColor(Color.parseColor("#666666"))
                                    .toolBarColor(Color.parseColor("#666666"))
                                    .mediaItemCheckSelector(ContextCompat.getColor(UserImgActivity.this, R.color.white), ContextCompat.getColor(UserImgActivity.this, R.color.albumColorPrimaryDark))
                                    .bucketItemCheckSelector(ContextCompat.getColor(UserImgActivity.this, R.color.white), ContextCompat.getColor(UserImgActivity.this, R.color.albumColorPrimaryDark))
                                    .build()
                            )
                            .onResult(new com.yanzhenjie.album.Action<ArrayList<AlbumFile>>() {
                                @Override
                                public void onAction(@NonNull ArrayList<AlbumFile> result) {
                                    //   GlideUtils.load(UserImgActivity.this, result.get(0).getPath(), iv_user_head);
                                    if (!ObjectUtils.isEmpty(result)) {
                                        cropImage(result.get(0).getPath());
                                    }
                                }
                            })
                            .onCancel(new com.yanzhenjie.album.Action<String>() {
                                @Override
                                public void onAction(@NonNull String result) {
                                }
                            })
                            .start();
                    }
                }
            });
            
        }
    };
    
    private void cropImage(String... imagePathList) {
        String cropDirectory = GlobalConfig.defaultImage;
        Durban.with(UserImgActivity.this)
            .statusBarColor(Color.parseColor("#666666"))
            //   .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            // .toolBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .toolBarColor(Color.parseColor("#666666"))
            
            //     .navigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryBlack))
            // Image path list/array.
            .inputImagePaths(imagePathList)
            // Image output directory.
            .outputDirectory(cropDirectory)
            // Image size limit.
            .maxWidthHeight(500, 500)
            // Aspect ratio.
            .aspectRatio(1, 1)
            // Output format: JPEG, PNG.
            .compressFormat(Durban.COMPRESS_JPEG)
            // Compress quality, see Bitmap#compress(Bitmap.CompressFormat, int, OutputStream)
            .compressQuality(90)
            // Gesture: ROTATE, SCALE, ALL, NONE.
            .gesture(Durban.GESTURE_ALL)
            .controller(Controller.newBuilder()
                .enable(false)
                //  .rotation(true)
                .rotationTitle(true)
                .scale(true)
                .scaleTitle(true)
                .build())
            .requestCode(200)
            .start();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //        if (requestCode == 200) {
        //            RxBus.getIntanceBus().post(new MessageItem(MessageItem.change_head, Durban.parseResult(data).get(0)));
        //            GlideUtils.load(UserImgActivity.this, Durban.parseResult(data).get(0), iv_user_head);
        //        }
        
        if (requestCode == 200) {
            if (!ObjectUtils.isEmpty(data) && !ObjectUtils.isEmpty(Durban.parseResult(data))) {
                GlideUtils.loadHead(UserImgActivity.this, Durban.parseResult(data).get(0), iv_user_head);
                RxBus.getIntanceBus().post(new MessageItem(MessageItem.change_head, Durban.parseResult(data).get(0)));
                userInfo.setSmallHeadimg(Durban.parseResult(data).get(0));
                uploadImg(Durban.parseResult(data).get(0));
            }
            
        }
        
    }
    
    private void uploadImg(String path) {
    
//        OssManager.getInstance().uploadFile(path, new OssManager.ResponseCallback<String>() {
//            @Override
//            public void onSuccess(String path) {
//
//            }
//
//            @Override
//            public void onError() {
//
//            }
//
//        });
    
    }
}
