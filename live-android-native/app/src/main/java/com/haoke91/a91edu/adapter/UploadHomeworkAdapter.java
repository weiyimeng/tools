package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.R.id;
import com.haoke91.a91edu.ui.homework.UpLoadHomeworkActivity;
import com.haoke91.a91edu.utils.imageloader.GlideUtils;
import com.haoke91.a91edu.widget.tilibrary.style.progress.ProgressBarIndicator;
import com.haoke91.a91edu.widget.tilibrary.style.progress.ProgressPieIndicator;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;
import com.haoke91.baselibrary.recycleview.itemtouch.ItemTouchHelperAdapter;
import com.yanzhenjie.album.AlbumFile;

import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/5/28 上午9:51
 * 修改人：weiyimeng
 * 修改时间：2018/5/28 上午9:51
 * 修改备注：
 */
public class UploadHomeworkAdapter extends QuickWithPositionAdapter<AlbumFile> implements ItemTouchHelperAdapter {
    
    private ProgressPieIndicator indicator;
    
    public UploadHomeworkAdapter(Context context) {
        super(context, R.layout.item_uplaod_homework);
        indicator = new ProgressPieIndicator();
    }
    
    @Override
    protected void convert(BaseAdapterHelper helper, final AlbumFile item, final int position) {
        ImageView imageView = helper.getImageView(id.iv_homework);
        View delete_view = helper.getView(id.tv_homework_delete);
        View resubmitBtn = helper.getView(id.tvResubmitBtn);
        if (position == 0 && elem.size() != UpLoadHomeworkActivity.Companion.getMAX_COUNT()) {
            imageView.setImageResource(R.mipmap.icon_add_homework);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            resubmitBtn.setVisibility(View.GONE);
            delete_view.setVisibility(View.GONE);
        } else {
            GlideUtils.load(getContext(), item.getThumbPath(), imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (!item.isDisable()) {
                delete_view.setVisibility(View.GONE);
                resubmitBtn.setVisibility(View.VISIBLE);
            } else {
                delete_view.setVisibility(View.VISIBLE);
                
                resubmitBtn.setVisibility(View.GONE);
                
            }
        }
        resubmitBtn.setTag(position);
        resubmitBtn.setOnClickListener(this);
        delete_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elem.size() == UpLoadHomeworkActivity.Companion.getMAX_COUNT()) {
                    getAll().add(0, new AlbumFile());
                }
                getAll().remove(item);
                elem.remove(item);
                notifyDataSetChanged();
                //                notifyItemRemoved(position);
                //                notifyItemRangeChanged(position, getAll().size());
            }
        });
        indicator.attach(position, (FrameLayout) helper.itemView);
        //        helper.setText(id.txt_user_name, item.getTitle());
        //        helper.getView(id.tv_text).setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                getAll().remove(item);
        //                notifyItemRemoved(position);
        //            }
        //        });
        
    }
    
    public ProgressPieIndicator getIndicator() {
        return indicator;
    }
    
    
    private List<AlbumFile> elem;
    
    @Override
    public void setData(List<AlbumFile> elem) {
        this.elem = elem;
        data.clear();
        data.addAll(elem);
        if (data.size() < UpLoadHomeworkActivity.Companion.getMAX_COUNT()) {
            AlbumFile file = new AlbumFile();
            file.setDisable(true);
            data.add(0, file);
        }
        if (!ObjectUtils.isEmpty(indicator)) {
            indicator.clearListDate();
        }
        notifyDataSetChanged();
    }
    
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
    
    }
    
    @Override
    public void onItemDissmiss(int position) {
    
    }
}

