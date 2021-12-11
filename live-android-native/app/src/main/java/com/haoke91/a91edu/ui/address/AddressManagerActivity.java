package com.haoke91.a91edu.ui.address;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gaosiedu.live.sdk.android.api.user.address.list.LiveUserAddressListRequest;
import com.gaosiedu.live.sdk.android.api.user.address.list.LiveUserAddressListResponse;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.AddressListAdapter;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.baselibrary.event.RxBus;
import com.haoke91.baselibrary.recycleview.HorizontalDividerItemDecoration;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.baselibrary.recycleview.adapter.BaseQuickWithPositionAdapter;
import com.haoke91.baselibrary.views.emptyview.EmptyView;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/13 上午9:38
 * 修改人：weiyimeng
 * 修改时间：2018/7/13 上午9:38
 * 修改备注：
 */
public class AddressManagerActivity extends BaseActivity {
    public static final String DATE = "DATE";
    private AddressListAdapter adapter;
    private EmptyView emptyView;
    
    public static void start(Context context, ArrayList<LiveUserAddressListResponse.ListData> addressList) {
        Intent intent = new Intent(context, AddressManagerActivity.class);
        intent.putExtra(DATE, addressList);
        context.startActivity(intent);
    }
    
    public static void start(Context context, boolean isEdit) {
        Intent intent = new Intent(context, AddressManagerActivity.class);
        intent.putExtra("isEdit", isEdit);
        context.startActivity(intent);
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_addressmanager;
    }
    
    //    private List<LiveUserAddressListResponse.ListData> addressList;
    private boolean isEdit;
    
    @Override
    public void initialize() {
        isEdit = getIntent().getBooleanExtra("isEdit", true);
        emptyView = findViewById(R.id.emptyView);
        WrapRecyclerView wr_address_list = findViewById(R.id.wr_address_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        wr_address_list.setLayoutManager(linearLayoutManager);
        wr_address_list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(
            getResources().getColor(R.color.FBFBFB))
            .size((int) getResources().getDimension(R.dimen.dp_1))
            .build());
        adapter = new AddressListAdapter(AddressManagerActivity.this, list);
        wr_address_list.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickWithPositionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                postChoiceAddress(position);
            }
        });
        findViewById(R.id.tv_add_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAddressActivity.startForResult(AddressManagerActivity.this, false, null);
            }
        });
        getAddressList();
        
        
    }
    
    private void postChoiceAddress(int position) {
        if (isEdit) {
            
            EditAddressActivity.startForResult(this, true, list.get(position));
        } else {
            if (list.size() > position) {
                RxBus.getIntanceBus().post(list.get(position));
            }
            finish();
        }
    }
    
    private List<LiveUserAddressListResponse.ListData> list = new ArrayList<>();
    
    private void getAddressList() {
        emptyView.showLoading();
        LiveUserAddressListRequest liveUserAddressListRequest = new LiveUserAddressListRequest();
        liveUserAddressListRequest.setUserId(UserManager.getInstance().getUserId());
        Api.getInstance().post(liveUserAddressListRequest, LiveUserAddressListResponse.class, new ResponseCallback<LiveUserAddressListResponse>() {
            @Override
            public void onResponse(LiveUserAddressListResponse date, boolean isFromCache) {
                emptyView.showContent();
                list = date.getData().getList();
                //                String s = new Gson().toJson(date);
                adapter.setData(list);
                //                adapter.notifyDataSetChanged();
            }
            
            @Override
            public void onEmpty(LiveUserAddressListResponse date, boolean isFromCache) {
                emptyView.showEmpty();
            }
            
            @Override
            public void onError() {
                emptyView.showError();
            }
        }, "");
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EditAddressActivity.requestCode && resultCode == RESULT_OK) {
            getAddressList();
            
            //            com.orhanobut.logger.Logger.e("1111111");
        }
        //        else if (requestCode == EditAddressActivity.requestCode && resultCode == EditAddressActivity.resultCode) {
        //            com.orhanobut.logger.Logger.e("2222");
        //
        //        }
        
        //       UserProfieResponse userProfieResponse  = LiveUserProfileApi.getInsance().userProfile(userProfilerequest);
    }
    
}
