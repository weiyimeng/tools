package gaosi.com.learn.studentapp.dresscity.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gsbaselib.utils.customevent.OnNoDoubleClickListener;
import com.gsbaselib.utils.glide.ImageLoader;
import com.gstudentlib.util.hy.HyConfig;

import java.io.File;
import java.util.ArrayList;
import gaosi.com.learn.R;
import gaosi.com.learn.bean.dress.FaceUMyItem;

/**
 * Created by test on 2018/5/22.
 */
public class DressAdapter extends RecyclerView.Adapter<DressAdapter.ViewHolder> {

    private Activity mContext;
    private ArrayList<FaceUMyItem> mData;

    public DressAdapter(@Nullable ArrayList<FaceUMyItem> data, Activity mContext) {
        this.mContext = mContext;
        this.mData = data;
    }

    public void setNewData(@Nullable ArrayList<FaceUMyItem> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int count = mData == null ? 0 : mData.size();
        count = count % 3 == 0 ? count : (3 - (count % 3)) + count;
        return count;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_dress_center_fm_item, null);
        view.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick((Integer) v.getTag());
                }
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        if (i < mData.size()) {
            viewHolder.tvName.setVisibility(View.VISIBLE);
            viewHolder.tvCountStr.setVisibility(View.VISIBLE);
            viewHolder.ivStatus.setVisibility(View.VISIBLE);
            viewHolder.ivUrl.setVisibility(View.VISIBLE);
            FaceUMyItem data = mData.get(i);
            viewHolder.tvName.setText(data.getName());
            viewHolder.tvCountStr.setText(data.getCountStr());
            viewHolder.tvCountStr.setSelected(0 != data.getCount());
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
            viewHolder.tvCountStr.setVisibility(View.GONE);
            viewHolder.ivStatus.setVisibility(View.GONE);
            viewHolder.ivUrl.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvCountStr;
        ImageView ivUrl, ivStatus;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCountStr = itemView.findViewById(R.id.tvCountStr);
            ivUrl = itemView.findViewById(R.id.ivUrl);
            ivStatus = itemView.findViewById(R.id.ivStatus);
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
