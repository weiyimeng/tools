package com.haoke91.a91edu.ui.homework;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.correct.add.LiveSccUserWrongQuestionCorrectQuestionRequest;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.correct.add.LiveSccUserWrongQuestionCorrectQuestionResponse;
import com.haoke91.a91edu.GlobalConfig;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.UploadHomeworkAdapter;
import com.haoke91.a91edu.entities.Exam;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.a91edu.utils.imageloader.MediaLoader;
import com.haoke91.a91edu.utils.manager.OssManager;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.exam.ChoiceExamView;
import com.haoke91.a91edu.widget.exam.FillBlankExamView;
import com.haoke91.a91edu.widget.tilibrary.loader.Glide4ImageLoader;
import com.haoke91.a91edu.widget.tilibrary.style.index.NumberIndexIndicator;
import com.haoke91.a91edu.widget.tilibrary.transfer.TransferConfig;
import com.haoke91.a91edu.widget.tilibrary.transfer.Transferee;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.baselibrary.recycleview.adapter.BaseQuickWithPositionAdapter;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.Filter;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.widget.divider.Api21ItemDivider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/8/8 15:31
 */
public class CorrectExamActivity extends BaseActivity {
    public static final int MAX_COUNT = 9;
    private ArrayList<AlbumFile> datas;
    private FrameLayout mExamContainer;
    private ChoiceExamView mChoiceExamView;
    private FillBlankExamView mFillBlankExamView;
    private Exam mExam;
    private View mCommitBtn;
    
    public static void start(Context context, Exam exam) {
        Intent intent = new Intent(context, CorrectExamActivity.class);
        intent.putExtra("id", exam);
        context.startActivity(intent);
    }
    
    WrapRecyclerView mRv_imgs;
    private UploadHomeworkAdapter mUploadHomeworkAdapter;
    
    @Override
    public int getLayout() {
        return R.layout.activity_correctexam;
    }
    
    @Override
    public void initialize() {
        mExam = (Exam) getIntent().getSerializableExtra("id");
        mRv_imgs = findViewById(R.id.recycler_Imgs);
        mExamContainer = id(R.id.examContainer);
        mCommitBtn = id(R.id.commitBtn);
        
        datas = new ArrayList<>();
        mUploadHomeworkAdapter = new UploadHomeworkAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRv_imgs.setAdapter(mUploadHomeworkAdapter);
        mRv_imgs.setLayoutManager(layoutManager);
        int dividerSize = getResources().getDimensionPixelSize(R.dimen.dp_8);
        mRv_imgs.addItemDecoration(new Api21ItemDivider(Color.TRANSPARENT, dividerSize, dividerSize));
        mUploadHomeworkAdapter.setOnItemClickListener(onItemClickListener);
        transferee = Transferee.getDefault(this);
        mUploadHomeworkAdapter.setData(datas);
        //        networkForCorrect(id);
        initExam(mExam);
    }
    
    public void clickEvent(View view) {
        if (view.getId() == R.id.ic_back) {
            onBackPressed();
        } else if (view.getId() == R.id.commitBtn) {
            //            networkForCommit();
            //            ToastUtils.showShort("提交不成功！不返回");
            checkExam();
        }
    }
    
    /**
     * 初始化题目
     *
     * @param exam
     */
    private void initExam(Exam exam) {
        if (mExamContainer.getChildCount() > 0) {
            mExamContainer.removeAllViews();
        }
        boolean isChoice = exam.getQuestionType() != 3;
        boolean isMulti = exam.getQuestionType() == 2;
        if (isChoice) {
            mChoiceExamView = new ChoiceExamView(this);
            mExamContainer.addView(mChoiceExamView);
            mChoiceExamView.setExamHead(exam.getQuestionStem());
            String questionContent = exam.getQuestionContent();
            JSONArray array = null;
            try {
                array = new JSONArray(questionContent);
                String[] s = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject opt = (JSONObject) array.get(i);
                    s[i] = opt.getString("questionOptionValue");
                }
                mChoiceExamView.setOptions(isMulti, true, s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            mFillBlankExamView = new FillBlankExamView(this);
            mFillBlankExamView.setHead(exam.getQuestionStem());
            mFillBlankExamView.setEditAreaVisible(View.VISIBLE, false);
            mExamContainer.addView(mFillBlankExamView);
        }
    }
    
    private Transferee transferee;
    private BaseQuickWithPositionAdapter.OnItemClickListener onItemClickListener = new BaseQuickWithPositionAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (position == 0 && datas.size() != MAX_COUNT) {
                Album.initialize(AlbumConfig.newBuilder(CorrectExamActivity.this)
                    .setAlbumLoader(new MediaLoader())
                    .setLocale(Locale.getDefault())
                    .build());
                Album.image(CorrectExamActivity.this)
                    .multipleChoice()
                    .columnCount(3)
                    .selectCount(MAX_COUNT)
                    .camera(true)
                    //  .camera(false)
                    .checkedList(datas)
                    //                    .filterMimeType(new Filter<String>() {
                    //                        @Override
                    //                        public boolean filter(String attributes) {
                    //                            //                            Logger.e("===attributes======"+attributes);
                    //                            return attributes.contains("octet-stream");//过滤gif
                    //                        }
                    //                    })
                    .widget(
                        Widget.newDarkBuilder(CorrectExamActivity.this)
                            .title("相册")
                            .statusBarColor(Color.parseColor("#666666"))
                            .toolBarColor(Color.parseColor("#666666"))
                            .mediaItemCheckSelector(ContextCompat.getColor(CorrectExamActivity.this, R.color.white), ContextCompat.getColor(CorrectExamActivity.this, R.color.albumColorPrimaryDark))
                            .bucketItemCheckSelector(ContextCompat.getColor(CorrectExamActivity.this, R.color.white), ContextCompat.getColor(CorrectExamActivity.this, R.color.albumColorPrimaryDark))
                            .build()
                    )
                    .onResult(new com.yanzhenjie.album.Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            datas.clear();
                            datas.addAll(result);
                            mUploadHomeworkAdapter.setData(datas);
                        }
                    })
                    .onCancel(new com.yanzhenjie.album.Action<String>() {
                        @Override
                        public void onAction(@NonNull String result) {
                        }
                    })
                    .start();
            } else {
                ArrayList<String> image = new ArrayList<>();
                for (AlbumFile file : datas) {
                    image.add(file.getPath());
                }
                
                if (datas.size() != MAX_COUNT) {
                    position--;
                }
                //         if (transConfig.getNowThumbnailIndex() >= transConfig.getOriginImageList().size())
                
                TransferConfig config = TransferConfig.build()
                    .setSourceImageList(image)
                    .setMissPlaceHolder(R.mipmap.empty_small)
                    .setErrorPlaceHolder(R.mipmap.empty_small)
                    // .setProgressIndicator(new ProgressBarIndicator())
                    .setIndexIndicator(new NumberIndexIndicator())
                    .setJustLoadHitImage(true)
                    .setImageLoader(Glide4ImageLoader.with(getApplicationContext()))
                    .create();
                
                config.setNowThumbnailIndex(position);
                config.setOriginImageList(wrapOriginImageViewList(datas.size()));
                
                transferee.apply(config).show(new Transferee.OnTransfereeStateChangeListener() {
                    @Override
                    public void onShow() {
                        Glide.with(CorrectExamActivity.this).pauseRequests();
                    }
                    
                    @Override
                    public void onDismiss() {
                        Glide.with(CorrectExamActivity.this).resumeRequests();
                    }
                });
            }
            
        }
    };
    
    protected List<ImageView> wrapOriginImageViewList(int size) {
        List<ImageView> originImgList = new ArrayList<>();
        if (datas.size() == MAX_COUNT) {
            for (int i = 0; i < size; i++) {
                ImageView thumImg = (ImageView) ((ViewGroup) mRv_imgs.getChildAt(i)).getChildAt(0);
                originImgList.add(thumImg);
            }
        } else {
            for (int i = 1; i < size + 1; i++) {
                ImageView thumImg = (ImageView) ((ViewGroup) mRv_imgs.getChildAt(i)).getChildAt(0);
                originImgList.add(thumImg);
            }
        }
        return originImgList;
    }
    
    //    /**
    //     * 错题获取
    //     *
    //     * @param id
    //     */
    //    private void networkForCorrect(int id){
    //        livesccwrong request = new LiveSccUserWrongQuestionCorrectQuestionRequest();
    //        request.setUserId("5904");
    //        request.setUserWrongQuestionId(id);
    //        Api.getInstance().postScc(request, LiveSccUserWrongQuestionCorrectQuestionResponse.class, new ResponseCallback<LiveSccUserWrongQuestionCorrectQuestionResponse>() {
    //            @Override
    //            public void onSuccess(LiveSccUserWrongQuestionCorrectQuestionResponse date, boolean isFromCache){
    //
    //            }
    //        }, "for test");
    //    }
    private boolean checkExam() {
        mCommitBtn.setEnabled(false);
        StringBuffer answer = new StringBuffer();
        if (mChoiceExamView != null) {
            boolean[] answerPosition = mChoiceExamView.getAnswerPosition();
            boolean isSelected = false;
            for (boolean b : answerPosition) {
                if (b) {
                    isSelected = true;
                }
            }
            if (!isSelected) {
                ToastUtils.showShort("请选择答案");
                return false;
            }
            
            String[] options = mChoiceExamView.getOptions();
            for (int i = 0; i < options.length; i++) {
                if (answerPosition[i]) {
                    if (i == 0) {
                        answer.append(options[i]);
                    } else {
                        answer.append("," + options[i]);
                    }
                }
            }
            //            networkForCommit(mExam.getId(), answer.toString(), "");
        } else if (mFillBlankExamView != null) {
            String ans = mFillBlankExamView.getEditText();
            if (ObjectUtils.isEmpty(ans.trim())) {
                ToastUtils.showShort("请输入答案");
                return false;
            }
            answer.setLength(0);
            answer.append(ans);
        }
        ossUploadImages(mExam.getId(), answer.toString());
        //        networkForCommit(mExam.getId(), answer.toString(), "");
        return true;
    }
    
    int imgTotal = 0, imgCount = 0;
    
    /**
     * 图片上传oss
     */
    private void ossUploadImages(final int id, final String answer) {
        if (datas == null || datas.size() == 0) {
            networkForCommit(id, answer, new ArrayList<String>());
            return;
        }
        imgTotal = datas.size();
        imgCount = 0;
        final List<String> imgUrls = new ArrayList<>();
        for (int i = 0; i < imgTotal; i++) {
            String path = datas.get(i).getPath();
            if (path == null || path.contains("http://") || path.contains("https://")) {
                imgCount++;
                imgUrls.add(path);
                continue;
            }
            final int finalI = i;
            String objectKey = GlobalConfig.OSSIMGPATH + UserManager.getInstance().getUserId() + File.separator + System.currentTimeMillis() + "." + ImageUtils.getImageType(datas.get(i).getPath());
            OssManager.getInstance().uploadFile(datas.get(i).getPath(), objectKey, new OssManager.ResponseCallback<String>() {
                @Override
                public void onSuccess(String date) {
                    imgCount++;
                    imgUrls.add(date);
                    if (imgCount == imgTotal) {
                        Logger.e("图片oss上传成功");
                        datas.get(finalI).setDisable(true);
                        networkForCommit(id, answer, imgUrls);
                    }
                }
                
                @Override
                public void onError() {
                    datas.get(finalI).setDisable(false);
                    mUploadHomeworkAdapter.setData(datas);
                }
            });
        }
    }
    
    /**
     * 提交订制的错题
     *
     * @param id
     * @param answer
     * @param imgUrls
     */
    private void networkForCommit(int id, String answer, List<String> imgUrls) {
        Utils.loading(this);
        LiveSccUserWrongQuestionCorrectQuestionRequest request = new LiveSccUserWrongQuestionCorrectQuestionRequest();
        request.setUserId(UserManager.getInstance().getUserId() + "");
        request.setUserWrongQuestionId(id);
        request.setCorrectQuestionAnswer(answer);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < imgUrls.size(); i++) {
            if (i == 0) {
                sb.append(imgUrls.get(0));
            } else {
                sb.append("," + imgUrls.get(i));
            }
        }
        request.setCorrectProcessImgPath(sb.toString());
        Api.getInstance().postScc(request, LiveSccUserWrongQuestionCorrectQuestionResponse.class, new ResponseCallback<LiveSccUserWrongQuestionCorrectQuestionResponse>() {
            @Override
            public void onResponse(LiveSccUserWrongQuestionCorrectQuestionResponse date, boolean isFromCache) {
                Utils.dismissLoading();
                ToastUtils.showShort("提交成功");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
            
            @Override
            public void onError() {
                super.onError();
                Utils.dismissLoading();
            }
            
            @Override
            public void onEmpty(LiveSccUserWrongQuestionCorrectQuestionResponse date, boolean isFromCache) {
                super.onEmpty(date, isFromCache);
                Utils.dismissLoading();
            }
        }, "for test");
    }
}
