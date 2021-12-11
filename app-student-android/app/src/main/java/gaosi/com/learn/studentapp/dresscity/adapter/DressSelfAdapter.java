package gaosi.com.learn.studentapp.dresscity.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gsbaselib.utils.glide.ImageLoader;
import com.gstudentlib.util.hy.HyConfig;

import java.io.File;
import java.util.ArrayList;
import gaosi.com.learn.R;
import gaosi.com.learn.bean.dress.dressself.DressSelfFaceUMyItem;

/**
 * Created by dingyz on 2018/12/3.
 */
public class DressSelfAdapter extends RecyclerView.Adapter<DressSelfAdapter.ViewHolder> implements View.OnClickListener {

    private Activity mContext;
    private boolean isCancel;
    private ArrayList<DressSelfFaceUMyItem> mData;

    public DressSelfAdapter(@Nullable ArrayList<DressSelfFaceUMyItem> data, Activity mContext) {
        this.mContext = mContext;
        this.mData = data;
    }

    /**
     * 设置是否可以取消
     *
     * @param isCancel
     */
    public void setIsCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }

    public void setNewData(@Nullable ArrayList<DressSelfFaceUMyItem> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int count = mData == null ? 0 : mData.size();
        if (count != 0 && isCancel) {
            count++;
        }
        // 补空格
        count = count % 3 == 0 ? count : (3 - (count % 3)) + count;
        if (count != 0) {
            count += 3;
        }
        return count;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_dress_self_item, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        int position;
        if (isCancel) {
            position = i - 1;
        } else {
            position = i;
        }
        if (position == -1) {
            viewHolder.tvName.setVisibility(View.GONE);
            viewHolder.ivStatus.setVisibility(View.GONE);
            viewHolder.ivUrl.setVisibility(View.GONE);
            viewHolder.tvNew.setVisibility(View.GONE);
            viewHolder.ivCancel.setVisibility(View.VISIBLE);
        } else {
            if (position < mData.size()) {
                viewHolder.ivCancel.setVisibility(View.GONE);
                viewHolder.tvName.setVisibility(View.VISIBLE);
                viewHolder.ivStatus.setVisibility(View.VISIBLE);
                viewHolder.ivUrl.setVisibility(View.VISIBLE);
                DressSelfFaceUMyItem data = mData.get(position);
                if (data.getToBeUsed() == 0) {
                    viewHolder.tvNew.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tvNew.setVisibility(View.INVISIBLE);
                }
                if (data.getToBeUsed() == 2) {
                    viewHolder.tvName.setSelected(true);
                } else {
                    viewHolder.tvName.setSelected(false);
                }
                viewHolder.tvName.setText(data.getName());
                if ("S".equals(data.getLevel())) {
                    viewHolder.ivStatus.setImageResource(R.drawable.icon_s);
                } else if ("B".equals(data.getLevel())) {
                    viewHolder.ivStatus.setImageResource(R.drawable.icon_b);
                } else if ("A".equals(data.getLevel())) {
                    viewHolder.ivStatus.setImageResource(R.drawable.icon_a);
                } else if ("SS".equals(data.getLevel())) {
                    viewHolder.ivStatus.setImageResource(R.drawable.icon_special_s);
                } else if ("SA".equals(data.getLevel())) {
                    viewHolder.ivStatus.setImageResource(R.drawable.icon_s);
                }
                ImageLoader.INSTANCE.setImageViewFileNoCache(viewHolder.ivUrl, HyConfig.INSTANCE.getHyResourceImagePath() + File.separator + data.getSmallCode());
            } else {
                viewHolder.tvName.setVisibility(View.GONE);
                viewHolder.ivStatus.setVisibility(View.GONE);
                viewHolder.ivUrl.setVisibility(View.GONE);
                viewHolder.tvNew.setVisibility(View.GONE);
                viewHolder.ivCancel.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvNew;
        ImageView ivUrl, ivStatus, ivCancel;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvNew = itemView.findViewById(R.id.tvNew);
            ivUrl = itemView.findViewById(R.id.ivUrl);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            ivCancel = itemView.findViewById(R.id.ivCancel);
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
