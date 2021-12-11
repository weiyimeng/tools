package com.haoke91.a91edu.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.gaosiedu.live.sdk.android.api.user.address.list.LiveUserAddressListResponse;
import com.gaosiedu.live.sdk.android.api.user.address.update.LiveUserAddressUpdateRequest;
import com.gaosiedu.live.sdk.android.api.user.address.update.LiveUserAddressUpdateResponse;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.address.EditAddressActivity;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.event.RxBus;
import com.haoke91.baselibrary.recycleview.adapter.BaseAdapterHelper;
import com.haoke91.baselibrary.recycleview.adapter.QuickWithPositionAdapter;
import com.haoke91.baselibrary.views.TipDialog;

import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/13 上午9:48
 * 修改人：weiyimeng
 * 修改时间：2018/7/13 上午9:48
 * 修改备注：
 */
public class AddressListAdapter extends QuickWithPositionAdapter<LiveUserAddressListResponse.ListData> {
    
    public AddressListAdapter(Context context, List<LiveUserAddressListResponse.ListData> dates) {
        super(context, R.layout.item_address, dates);
    }
    
    @Override
    protected void convert(BaseAdapterHelper helper, final LiveUserAddressListResponse.ListData item, final int position) {
        final ImageView ck_address_default = helper.getView(R.id.ck_address_default);
        TextView tv_address_name = helper.getTextView(R.id.tv_address_name);
        tv_address_name.setText(item.getContactPeople());
        TextView tv_address_phone = helper.getTextView(R.id.tv_address_phone);
        tv_address_phone.setText(item.getContactMobile());
        boolean isDefault = item.getType() == 1;
        //        ck_address_default.setChecked(isDefault);
        if (isDefault) {
            ck_address_default.setBackgroundResource(R.mipmap.study_icon_right);
            SpanUtils spanUtils = new SpanUtils();
            SpannableStringBuilder spannableStringBuilder = spanUtils
                .append("[默认]").setFontSize(12, true).setForegroundColor(context.getResources().getColor(R.color.FF4F00))
                .append(new StringBuffer(item.getGeneral()))
                //   .append(item.getCity()).append(item.getArea()).append(item.getAddress()))
                .setFontSize(12, true).setForegroundColor(context.getResources().getColor(R.color.color_363636)).create();
            helper.getTextView(R.id.tv_address_detail).setText(spannableStringBuilder);
            helper.getTextView(R.id.tv_setDefault).setTextColor(Color.parseColor("#75C82B"));
        } else {
            ck_address_default.setBackgroundResource(R.drawable.ic_mine_shop_icon_sel_nor);
            //            helper.getTextView(R.id.tv_address_detail).setText(item.getAddress());
            helper.getTextView(R.id.tv_address_detail).setText(item.getGeneral());
            helper.getTextView(R.id.tv_setDefault).setTextColor(Color.parseColor("#979797"));
            
        }
        View.OnClickListener OnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_address_delete:
                        showDeleteDialog(item, position);
                        //                        deleteAddress(item, position);
                        
                        break;
                    case R.id.tv_address_edit:
                        EditAddressActivity.startForResult((Activity) context, true, item);
                        break;
                    case R.id.ck_address_default:
                        changeDefaultAddress(item, position);
                        
                        
                        //    ck_address_default.setChecked(true);
                        
                        break;
                    case R.id.tv_setDefault:
                        ck_address_default.performClick();
                        break;
                    default:
                }
            }
        };
        ck_address_default.setOnClickListener(OnClickListener);
        helper.getView(R.id.tv_address_delete).setOnClickListener(OnClickListener);
        helper.getView(R.id.tv_address_edit).setOnClickListener(OnClickListener);
        helper.getTextView(R.id.tv_setDefault).setOnClickListener(OnClickListener);
        
    }
    
    private void showDeleteDialog(final LiveUserAddressListResponse.ListData item, final int position) {
        TipDialog dialog = new TipDialog(context);
        dialog.setTextDes("确定删除");
        dialog.setButton1(context.getString(R.string.action_ok), new TipDialog.DialogButtonOnClickListener() {
            
            @Override
            public void onClick(View button, TipDialog dialog) {
                deleteAddress(item, position);
                dialog.dismiss();
            }
        });
        dialog.setButton2(context.getString(R.string.cancel), new TipDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, TipDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    
    private void deleteAddress(final LiveUserAddressListResponse.ListData item, final int position) {
        LiveUserAddressUpdateRequest liveUserAddressUpdateRequest = new LiveUserAddressUpdateRequest();
        liveUserAddressUpdateRequest.setStatus(0);
        liveUserAddressUpdateRequest.setUserId(UserManager.getInstance().getUserId());
        liveUserAddressUpdateRequest.setId(item.getId());
        liveUserAddressUpdateRequest.setType(0);
        Api.getInstance().post(liveUserAddressUpdateRequest, LiveUserAddressUpdateResponse.class, new ResponseCallback<LiveUserAddressUpdateResponse>() {
            @Override
            public void onResponse(LiveUserAddressUpdateResponse date, boolean isFromCache) {
                getAll().remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getAll().size());
            }
        }, "");
        
    }
    
    private void changeDefaultAddress(final LiveUserAddressListResponse.ListData item, final int position) {
        
        LiveUserAddressUpdateRequest liveUserAddressUpdateRequest = new LiveUserAddressUpdateRequest();
        liveUserAddressUpdateRequest.setStatus(1);
        liveUserAddressUpdateRequest.setUserId(UserManager.getInstance().getUserId());
        liveUserAddressUpdateRequest.setId(item.getId());
        liveUserAddressUpdateRequest.setType(1);
        Api.getInstance().post(liveUserAddressUpdateRequest, LiveUserAddressUpdateResponse.class, new ResponseCallback<LiveUserAddressUpdateResponse>() {
            @Override
            public void onResponse(LiveUserAddressUpdateResponse date, boolean isFromCache) {
                getAll().get(0).setType(0);
                item.setType(1);
                getAll().add(0, item);
                getAll().remove(position + 1);
                notifyDataSetChanged();
                
                notifyChange();
            }
        }, "");
    }
    
    private void notifyChange() {
        MessageItem messageItem = new MessageItem(MessageItem.deafautlAddressChange, null);
        RxBus.getIntanceBus().post(messageItem);
        
    }
    
}
