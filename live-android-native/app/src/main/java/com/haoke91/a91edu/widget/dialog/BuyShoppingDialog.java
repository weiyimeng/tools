package com.haoke91.a91edu.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gaosiedu.live.sdk.android.api.user.address.list.LiveUserAddressListRequest;
import com.gaosiedu.live.sdk.android.api.user.address.list.LiveUserAddressListResponse;
import com.gaosiedu.live.sdk.android.api.user.gold.exchange.LiveUserGoldExchangeRequest;
import com.gaosiedu.live.sdk.android.api.user.gold.exchange.LiveUserGoldExchangeResponse;
import com.gaosiedu.live.sdk.android.api.user.gold.product.LiveUserGoldListResponse;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.address.AddressManagerActivity;
import com.haoke91.a91edu.ui.address.EditAddressActivity;
import com.haoke91.a91edu.ui.found.GoldGoodsActivity;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.a91edu.utils.imageloader.GlideUtils;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.baselibrary.event.RxBus;

import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * Created by wdy on 2017/9/9.
 */

public class BuyShoppingDialog extends DialogFragment {
    private static final String TAG = "BuyShoppingDialog";
    private View vNoAddress;
    private TextView tvUserName;
    private TextView tvPhone, tvAddressDes;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LiveUserGoldListResponse.ListData bean = (LiveUserGoldListResponse.ListData) getArguments().getSerializable("data");
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER;
        //  dialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(getActivity(), R.layout.dialog_buy_shopping, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams((int) (getResources().getDisplayMetrics().widthPixels * 0.8), -2);
        dialog.setContentView(view, layoutParams);
        initView(view, bean);
        initData();
        return dialog;
    }
    
    
    private void initData(){
        
        //        mIv_wake_qq.setOnClickListener(onClickListener);
    }
    
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.iv_close:
                    break;
                
            }
        }
    };
    
    private void initView(View view, final LiveUserGoldListResponse.ListData bean){
        ImageView ivImg = view.findViewById(R.id.iv_goods);
        TextView tvName = view.findViewById(R.id.tv_goodsName);
        TextView tvRemainNum = view.findViewById(R.id.tv_remainNum);
        final TextView tvGolds = view.findViewById(R.id.tv_goods_money);
        TextView tvDes = view.findViewById(R.id.tv_goods_des);
        final TextView tvGoldsNum = view.findViewById(R.id.tv_goods_num);
        
        GlideUtils.load(getContext(), bean.getCoverImg(), ivImg);
        tvName.setText(bean.getName());
        if (bean.getCount() > 999){
            tvRemainNum.setText("剩余  999+");
        } else{
            tvRemainNum.setText("剩余  " + bean.getCount());
        }
        tvGolds.setText(bean.getGold() + "");
        tvDes.setText(bean.getProductDesc());
        
        View flAddress = view.findViewById(R.id.fl_address);
        vNoAddress = view.findViewById(R.id.v_noAddress);
        final View vDefaultAddress = view.findViewById(R.id.v_defaultAddress);
        tvUserName = view.findViewById(R.id.tv_userName);
        tvPhone = view.findViewById(R.id.tv_phone);
        tvAddressDes = view.findViewById(R.id.tv_address_detail);
        
        view.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dismiss();
            }
        });
        view.findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dismiss();
                if (checkInputInfo(tvGoldsNum, vDefaultAddress)){
                    networkForExchange(bean.getId(), Integer.parseInt(tvGoldsNum.getText().toString().trim()), (Integer) vDefaultAddress.getTag());
                }
            }
        });
        flAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dismiss();
                AddressManagerActivity.start(getContext(), false);
            }
        });
        view.findViewById(R.id.tv_num_reduce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String trim = tvGoldsNum.getText().toString().trim();
                int num = Integer.parseInt(trim);
                if (num > 0){
                    num--;
                    tvGoldsNum.setText(num + "");
                }
            }
        });
        view.findViewById(R.id.tv_num_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String trim = tvGoldsNum.getText().toString().trim();
                int num = Integer.parseInt(trim);
                if(num<bean.getCount()){
                    num++;
                    tvGoldsNum.setText(num + "");
                }
            }
        });
        
        
        networkForAddress(vNoAddress, vDefaultAddress, tvUserName, tvPhone, tvAddressDes);
        RxBus.getIntanceBus().doSubscribe(GoldGoodsActivity.class, LiveUserAddressListResponse.ListData.class, new Consumer<LiveUserAddressListResponse.ListData>() {
            @Override
            public void accept(LiveUserAddressListResponse.ListData listData) throws Exception{
                if (vNoAddress == null || vDefaultAddress == null || tvUserName == null || tvPhone == null || tvAddressDes == null){
                    return;
                }
                vNoAddress.setVisibility(View.GONE);
                vDefaultAddress.setVisibility(View.VISIBLE);
                tvUserName.setText(listData.getContactPeople());
                tvPhone.setText(listData.getContactMobile());
                tvAddressDes.setText(listData.getGeneral());
                vDefaultAddress.setTag(listData.getId());
            }
        });
        
    }
    
    private boolean checkInputInfo(TextView tvNum, View vDefaultAddress){
        String trim = tvNum.getText().toString().trim();
        if (Integer.parseInt(trim) <= 0){
            ToastUtils.showShort("兑换数量不能为0");
            return false;
        }
        if (vDefaultAddress.getVisibility() != View.VISIBLE || vDefaultAddress.getTag().equals("0")){
            ToastUtils.showShort("请选择收货地址");
            return false;
        }
        return true;
    }
    
    @Override
    public void onStart(){
        super.onStart();
    }
    
    public static BuyShoppingDialog showDialog(AppCompatActivity appCompatActivity, LiveUserGoldListResponse.ListData date){
        BuyShoppingDialog dialog = new BuyShoppingDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", date);
        dialog.setArguments(bundle);
        appCompatActivity.getSupportFragmentManager().beginTransaction().add(dialog, TAG).commitAllowingStateLoss();
        return dialog;
    }
    
    /**
     * 请求收货地址
     */
    private void networkForAddress(final View vNoAddress, final View vDefaultAddress, final TextView tvUserName, final TextView tvPhone, final TextView tvAddressDes){
        LiveUserAddressListRequest request = new LiveUserAddressListRequest();
        request.setUserId(UserManager.getInstance().getUserId());
        request.setPageNum(1);
        Api.getInstance().post(request, LiveUserAddressListResponse.class, new ResponseCallback<LiveUserAddressListResponse>() {
            @Override
            public void onResponse(LiveUserAddressListResponse date, boolean isFromCache){
                List<LiveUserAddressListResponse.ListData> list = date.getData().getList();
                if (list != null && list.size() > 0){
                    LiveUserAddressListResponse.ListData bean = list.get(0);
                    if (bean != null){
                        vNoAddress.setVisibility(View.GONE);
                        vDefaultAddress.setVisibility(View.VISIBLE);
                        tvUserName.setText(bean.getContactPeople());
                        tvPhone.setText(bean.getContactMobile());
                        tvAddressDes.setText(bean.getGeneral());
                        vDefaultAddress.setTag(bean.getId());
                    }
                }
                
            }
            
            @Override
            public void onEmpty(LiveUserAddressListResponse date, boolean isFromCache){
            }
            
            @Override
            public void onError(){
            }
        }, "for list of addresses");
    }
    
    /**
     * 兑换礼物
     */
    private void networkForExchange(int id, int num, int addressId){
        LiveUserGoldExchangeRequest request = new LiveUserGoldExchangeRequest();
        request.setUserId(UserManager.getInstance().getUserId());
        request.setProductId(id);
        request.setCount(num);
        request.setAddressId(addressId);
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
