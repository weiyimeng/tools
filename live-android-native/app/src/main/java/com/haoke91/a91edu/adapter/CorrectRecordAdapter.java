package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.gaosiedu.scc.sdk.android.domain.SccUserWrongQuestionCorrectBean;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.utils.imageloader.GlideUtils;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/9/11 11:43
 */
public class CorrectRecordAdapter extends QuickWithPositionAdapter<SccUserWrongQuestionCorrectBean> {
    public CorrectRecordAdapter(Context context){
        super(context, R.layout.item_lv_correctrecord, new ArrayList<SccUserWrongQuestionCorrectBean>());
    }
    
    @Override
    protected void convert(BaseAdapterHelper helper, SccUserWrongQuestionCorrectBean item, int position){
        TextView mComment = helper.getTextView(R.id.tv_comment);//讲评
        WrapRecyclerView recyclerView = helper.getView(R.id.rv_imgs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        ImgAdapter imgAdapter = new ImgAdapter(getContext());
        recyclerView.setAdapter(imgAdapter);
        String correctAnswer = item.getCorrectAnswer();
        if (ObjectUtils.isEmpty(correctAnswer)){
            mComment.setVisibility(View.GONE);
        } else{
            mComment.setText(String.format("教师讲评： ", correctAnswer));
        }
        String correctProcessImg = item.getCorrectProcessImg();
        if (ObjectUtils.isEmpty(correctProcessImg)){
            recyclerView.setVisibility(View.GONE);
        } else{
            String[] split = correctProcessImg.split(",");
            imgAdapter.setData(Arrays.asList(split));
        }
        //        imgAdapter.setData(Arrays.asList("1", "2", "3", "4"));
        //        imgAdapter.setData();
    }
    
    private static class ImgAdapter extends QuickWithPositionAdapter<String> {
        public ImgAdapter(Context context){
            super(context, R.layout.item_image, new ArrayList<String>());
        }
        
        @Override
        protected void convert(BaseAdapterHelper helper, String item, int position){
            ImageView iv = helper.getImageView(R.id.image);
            GlideUtils.load(getContext(), item, iv);
        }
    }
}
