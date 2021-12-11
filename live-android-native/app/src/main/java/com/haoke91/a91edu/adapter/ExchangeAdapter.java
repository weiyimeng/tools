package com.haoke91.a91edu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.gaosiedu.live.sdk.android.api.user.gold.exchange.LiveUserGoldExchangeRequest;
import com.gaosiedu.live.sdk.android.api.user.gold.exchange.LiveUserGoldExchangeResponse;
import com.gaosiedu.live.sdk.android.api.user.gold.product.LiveUserGoldListResponse;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.utils.imageloader.GlideUtils;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.dialog.BuyShoppingDialog;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;
import com.haoke91.baselibrary.views.TipDialog;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/13 下午2:16
 * 修改人：weiyimeng
 * 修改时间：2018/8/13 下午2:16
 * 修改备注：
 */
public class ExchangeAdapter extends QuickWithPositionAdapter<LiveUserGoldListResponse.ListData> {
    private int mType;// 1,实物兑换    2,：虚拟物品兑换
    
    public ExchangeAdapter(Context context, int type){
        super(context, R.layout.item_giftgrid);
        mType = type;
    }
    
    @Override
    protected void convert(BaseAdapterHelper helper, final LiveUserGoldListResponse.ListData item, int position){
        //        GlideUtils.load(context, item.getCoverImg(), helper.getImageView(R.id.iv_gift));
        final ImageView ivGift = helper.getImageView(R.id.iv_gift);
        Glide.with(context)
            .load(item.getCoverImg())
            .apply(new RequestOptions().error(R.mipmap.empty_small).placeholder(R.mipmap.empty_small)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).fitCenter())
            .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource){
                    return false;
                }
                
                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource){
                    int scale = resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
                    ViewGroup.LayoutParams lp = ivGift.getLayoutParams();
                    lp.height = ivGift.getWidth() * scale;
                    ivGift.setLayoutParams(lp);
                    return false;
                }
            }).into(ivGift);
        //
        //            .into(new SimpleTarget<Drawable>() {
        //                @Override
        //                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition){
        //                    Rect bounds = resource.getBounds();
        //                    ViewGroup.LayoutParams lp = ivGift.getLayoutParams();
        //                    lp.height = lp.width * bounds.width() / bounds.height();
        //                    ivGift.setLayoutParams(lp);
        //                    ivGift.setImageDrawable(resource);
        //                    resource.setCallback(null);//内存优化？
        //                }
        //
        //                @Override
        //                public void onLoadFailed(@Nullable Drawable errorDrawable){
        //                    super.onLoadFailed(errorDrawable);
        //                    Rect bounds = errorDrawable.getBounds();
        //                    ViewGroup.LayoutParams lp = ivGift.getLayoutParams();
        //                    lp.height = lp.width * bounds.width() / bounds.height();
        //                    ivGift.setLayoutParams(lp);
        //                    ivGift.setImageDrawable(errorDrawable);
        //                    errorDrawable.setCallback(null);//内存优化？
        //                }
        //            });
        
        TextView tvGiftName = helper.getTextView(R.id.tv_giftName);
        TextView tvGold = helper.getTextView(R.id.tv_gold);
        TextView tvRemain = helper.getTextView(R.id.tv_remain);
        tvGiftName.setText(item.getName());
        tvGold.setText(item.getGold() + "金币");
        tvRemain.setText(String.format("剩余%s件", item.getCount()));
        helper.getView(R.id.tv_toExchange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (mType == 1){
                    BuyShoppingDialog.showDialog((AppCompatActivity) context, item);
                } else{
                    showTip(item.getGold(), item.getId());
                }
            }
        });
        //        helper.getView(R.id.iv_gift).setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v){
        //                BuyShoppingDialog.showDialog((AppCompatActivity) context, item);
        //            }
        //        });
    }
    
    private void showTip(int gold, final int id){
        TipDialog tipDialog = new TipDialog(context);
        tipDialog.setPromptTitle("确认是否兑换");
        //        tipDialog.setTextDesSize();
        tipDialog.setTextDes("本次兑换将扣除您" + gold + "金币");
        tipDialog.setButton1("确定", new TipDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, TipDialog dialog){
                networkForExchange(id);
            }
        });
        tipDialog.setButton2("取消", new TipDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, TipDialog dialog){
            }
        });
        tipDialog.show();
        
    }
    
    /**
     * 兑换虚拟礼物
     */
    private void networkForExchange(int id){
        LiveUserGoldExchangeRequest request = new LiveUserGoldExchangeRequest();
        request.setUserId(UserManager.getInstance().getUserId());
        request.setProductId(id);
        request.setCount(1);
        //        request.setAddressId(addressId);
        Api.getInstance().post(request, LiveUserGoldExchangeResponse.class, new ResponseCallback<LiveUserGoldExchangeResponse>() {
            @Override
            public void onResponse(LiveUserGoldExchangeResponse date, boolean isFromCache){
                if (date.getData().getFlag()){
                    ToastUtils.showShort("兑换成功！");
                } else{
                    ToastUtils.showShort(date.getData().getMsg());
                }
            }
            
            @Override
            public void onError(){
                super.onError();
            }
        }, "for exchange");
    }
}
