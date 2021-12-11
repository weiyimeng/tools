package com.haoke91.a91edu.ui.user;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.gaosiedu.live.sdk.android.api.user.balance.info.LiveUserBalanceInfoRequest;
import com.gaosiedu.live.sdk.android.api.user.balance.info.LiveUserBalanceInfoResponse;
import com.gaosiedu.live.sdk.android.api.user.balance.record.LiveUserBalanceRecordRequest;
import com.haoke91.a91edu.MainActivity;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.ui.order.ConfirmOrderActivity;
import com.haoke91.a91edu.utils.manager.UserManager;

import java.math.BigDecimal;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/22 下午7:53
 * 修改人：weiyimeng
 * 修改时间：2018/7/22 下午7:53
 * 修改备注：
 */
public class MyBalanceActivity extends BaseActivity {
    
    public static void start(Context context) {
        Intent intent = new Intent(context, MyBalanceActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_mybalance;
    }
    
    @Override
    public void initialize() {
        
        ImageView toolbar_more = findViewById(R.id.toolbar_more);
        toolbar_more.setVisibility(View.VISIBLE);
        toolbar_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BalanceRecordActivity.start(MyBalanceActivity.this);
            }
        });
        
        
    }
    
    @Override
    protected void initEvent() {
        LiveUserBalanceInfoRequest liveUserBalanceInfoRequest = new LiveUserBalanceInfoRequest();
        liveUserBalanceInfoRequest.setUserId(UserManager.getInstance().getUserId());
        Api.getInstance().post(liveUserBalanceInfoRequest, LiveUserBalanceInfoResponse.class, new ResponseCallback<LiveUserBalanceInfoResponse>() {
            @Override
            public void onResponse(LiveUserBalanceInfoResponse date, boolean isFromCache) {
                TextView tv_balance = findViewById(R.id.tv_balance);
                tv_balance.setText(date.getData().getAvailableBalance().toString());
            }
        }, "");
    }
}
