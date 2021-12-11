package com.haoke91.a91edu.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.gaosiedu.live.sdk.android.api.order.create.LiveOrderCreateResponse;
import com.gaosiedu.live.sdk.android.api.user.order.detail.orderNo.LiveUserOrderDetailOrderNOResponse;
import com.gaosiedu.live.sdk.android.domain.OrderDeliverDomain;
import com.gaosiedu.live.sdk.android.domain.OrderItemDomain;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.FlowListAdapter;
import com.haoke91.baselibrary.views.emptyview.EmptyView;
import com.yanzhenjie.album.AlbumFolder;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.OnItemClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by wdy on 2017/9/9.
 */

public class SeeFlowDialog extends BottomSheetDialog {
    
    
    public SeeFlowDialog(Context context, LiveOrderCreateResponse.ResultData data) {
        super(context, R.style.flow_dialog);
        setContentView(R.layout.dialog_see_flow);
        setCanceledOnTouchOutside(true);
        RecyclerView recyclerView = getDelegate().findViewById(R.id.recyclerView);
        EmptyView empty_view = getDelegate().findViewById(R.id.empty_view);
        List<OrderDeliverDomain> orderDeliverDomains = new ArrayList<>();
        for (OrderItemDomain item : data.getOrderItemList()) {
            List<OrderDeliverDomain> orderDeliverDomain = item.orderDeliverDomains;
            if (!ObjectUtils.isEmpty(orderDeliverDomain)) {
                orderDeliverDomains.addAll(orderDeliverDomain);
            }
        }
        if (ObjectUtils.isEmpty(orderDeliverDomains)) {
            empty_view.showEmpty();
        } else {
            empty_view.showContent();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            FlowListAdapter mFolderAdapter = new FlowListAdapter(context, orderDeliverDomains);
            recyclerView.setAdapter(mFolderAdapter);
        }
        
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            Display display = window.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= 17) {
                display.getRealMetrics(metrics);
            } else {
                display.getMetrics(metrics);
            }
            int minSize = Math.min(metrics.widthPixels, metrics.heightPixels);
            window.setLayout(minSize, -1);
            if (Build.VERSION.SDK_INT >= 21) {
                window.setStatusBarColor(Color.TRANSPARENT);
                //                window.setNavigationBarColor(mWidget.getNavigationBarColor());
            }
        }
    }
    
}
