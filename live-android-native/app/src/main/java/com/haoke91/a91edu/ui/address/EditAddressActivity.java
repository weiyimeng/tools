package com.haoke91.a91edu.ui.address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gaosiedu.live.sdk.android.api.user.address.add.LiveUserAddressAddRequest;
import com.gaosiedu.live.sdk.android.api.user.address.add.LiveUserAddressAddResponse;
import com.gaosiedu.live.sdk.android.api.user.address.list.LiveUserAddressListResponse;
import com.gaosiedu.live.sdk.android.api.user.address.update.LiveUserAddressUpdateRequest;
import com.gaosiedu.live.sdk.android.api.user.address.update.LiveUserAddressUpdateResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.entities.AddressBean;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.NoDoubleClickListener;
import com.haoke91.a91edu.widget.PhoneEditText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/13 上午11:11
 * 修改人：weiyimeng
 * 修改时间：2018/7/13 上午11:11
 * 修改备注：
 */
public class EditAddressActivity extends BaseActivity {
    private static final String IsEdit = "isEdit";
    public static final int requestCode = 111;
    public static final int resultCode = 222;
    
    private LinearLayout ll_address_region;
    private OptionsPickerView pvOptions;
    @SuppressWarnings("FieldCanBeLocal")
    private TextView tv_address_region;
    private List<AddressBean> addressBeans;
    private ArrayList<ArrayList<AddressBean.CityBean>> options2Items;
    private ArrayList<ArrayList<ArrayList<AddressBean.AreaBean>>> options3Items;
    private EditText et_address_detail, et_address_name;
    private PhoneEditText tv_address_phone;
    private CheckBox cb_default;
    
    /**
     * @param context
     * @param isEdit  true 编辑  else 新增
     */
    public static void start(Context context, boolean isEdit) {
        Intent intent = new Intent(context, EditAddressActivity.class);
        intent.putExtra(IsEdit, isEdit);
        context.startActivity(intent);
    }
    
    public static void startForResult(Activity context, boolean isEdit, LiveUserAddressListResponse.ListData address) {
        Intent intent = new Intent(context, EditAddressActivity.class);
        intent.putExtra(IsEdit, isEdit);
        intent.putExtra("address", address);
        context.startActivityForResult(intent, requestCode);
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_editaddress;
    }
    
    private boolean isEdit;
    private LiveUserAddressListResponse.ListData address;
    
    @Override
    public void initialize() {
        isEdit = getIntent().getBooleanExtra(IsEdit, false);
        address = (LiveUserAddressListResponse.ListData) getIntent().getSerializableExtra("address");
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        if (isEdit) {
            toolbar_title.setText(getString(R.string.address_edit));
        } else {
            toolbar_title.setText(getString(R.string.ad_address));
            
        }
        TextView toolbar_right = findViewById(R.id.toolbar_right);
        toolbar_right.setVisibility(View.VISIBLE);
        toolbar_right.setText(getString(R.string.save));
        toolbar_right.setTextColor(Color.parseColor("#75C82B"));
//        tv_address_detail = findViewById(R.id.tv_address_detail);
        tv_address_region = findViewById(R.id.tv_address_region);
        ll_address_region = findViewById(R.id.ll_address_region);
        et_address_detail = findViewById(R.id.et_address_detail);
        cb_default = findViewById(R.id.cb_default);
        et_address_name = findViewById(R.id.et_address_name);
        tv_address_phone = findViewById(R.id.tv_address_phone);
        ll_address_region.setOnClickListener(OnClickListener);
        toolbar_right.setOnClickListener(OnClickListener);
        
        if (!ObjectUtils.isEmpty(address)) {
            et_address_name.setText(address.getContactPeople());
            tv_address_phone.setText(address.getContactMobile());
            StringBuilder append = new StringBuilder().append(address.getProvince()).append(" ").append(address.getCity()).append(" ").append(address.getArea());
            tv_address_region.setText(append);
            et_address_detail.setText(address.getAddress());
            cb_default.setChecked(address.getType() == 1);
            
            provice = address.getProvince();
            city = address.getCity();
            area = address.getArea();
            proviceId = address.getProvinceCode();
            cityId = address.getCityCode();
            areaId = address.getAreaCode();
        }
        
        
        //        LiveAreaAllRequest liveAreaAllRequest = new LiveAreaAllRequest();
        //        Api.getInstance().post(liveAreaAllRequest, LiveAreaAllResponse.class, new ResponseCallback<LiveAreaAllResponse>() {
        //            @Override
        //            public void onSuccess(LiveAreaAllResponse date, boolean isFromCache) {
        //                new Gson().toJson(date);
        //            }
        //        }, "");
    }
    
    private View.OnClickListener OnClickListener = new NoDoubleClickListener() {
        @Override
        public void onDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.ll_address_region:
                    if (pvOptions == null) {
                        initAddressPicker();
                    } else {
                        KeyboardUtils.hideSoftInput(ll_address_region);
                        pvOptions.show();
                    }
                    break;
                case R.id.toolbar_right:
                    if (ObjectUtils.isEmpty(et_address_name.getText().toString())) {
                        ToastUtils.showShort("请填写收件人姓名");
                        return;
                    }
                    if (ObjectUtils.isEmpty(tv_address_phone.getPhoneText())) {
                        ToastUtils.showShort("请填写收件人手机号");
                        return;
                    }
                    if (!RegexUtils.isMobileExact(tv_address_phone.getPhoneText()) && !RegexUtils.isTel(tv_address_phone.getPhoneText())) {
                        ToastUtils.showShort("请填写正确手机号");
                        return;
                    }
                    
                    if (ObjectUtils.isEmpty(tv_address_region.getText().toString())) {
                        ToastUtils.showShort("请选择收货地址");
                        return;
                    }
                    
                    if (ObjectUtils.isEmpty(et_address_detail.getText().toString())) {
                        ToastUtils.showShort("请填写详细收货地址");
                        return;
                    }
                    
                    if (isEdit) {//编辑
                        LiveUserAddressUpdateRequest request = new LiveUserAddressUpdateRequest();
                        request.setAddress(et_address_detail.getText().toString().trim());
                        //                        request.setArea();
                        request.setUserId(UserManager.getInstance().getUserId());
                        request.setContactMobile(tv_address_phone.getText().toString().trim());
                        request.setContactPeople(et_address_name.getText().toString().trim());
                        request.setId(address.getId());
                        request.setStatus(1);
                        request.setType(cb_default.isChecked() ? 1 : 0);
                        if (!ObjectUtils.isEmpty(tv_address_region.getText().toString().trim())) {
                            //                            String[] split = tv_address_region.getText().toString().trim().split(" ");
                            request.setProvince(provice);
                            request.setProvinceCode(proviceId);
                            
                            request.setCity(city);
                            request.setCityCode(cityId);
                            
                            request.setArea(area);
                            request.setAreaCode(areaId);
                        }
                        showLoadingDialog();
                        Api.getInstance().post(request, LiveUserAddressUpdateResponse.class, new ResponseCallback<LiveUserAddressUpdateResponse>() {
                            @Override
                            public void onResponse(LiveUserAddressUpdateResponse date, boolean isFromCache) {
                                dismissLoadingDialog();
                                //                                ToastUtils.showShort(date.getMessage());
                                setResult(RESULT_OK);
                                finish();
                            }
                            
                            @Override
                            public void onError() {
                                ToastUtils.showShort("修改失败");
                                dismissLoadingDialog();
                            }
                        }, "");
                        setResult(RESULT_OK);
                    } else {//新增
                        LiveUserAddressAddRequest request = new LiveUserAddressAddRequest();
                        request.setAddress(et_address_detail.getText().toString().trim());
                        //                        request.setArea();
                        request.setUserId(UserManager.getInstance().getUserId());
                        request.setContactMobile(tv_address_phone.getText().toString().trim());
                        request.setContactPeople(et_address_name.getText().toString().trim());
                        request.setType(cb_default.isChecked() ? "1" : "0");
                        
                        if (!ObjectUtils.isEmpty(tv_address_region.getText().toString().trim())) {
                            //                            String[] split = tv_address_region.getText().toString().trim().split(" ");
                            request.setProvince(provice);
                            request.setProvinceCode(proviceId);
                            
                            request.setCity(city);
                            request.setCityCode(cityId);
                            
                            request.setArea(area);
                            request.setAreaCode(areaId);
                        }
                        
                        Api.getInstance().post(request, LiveUserAddressAddResponse.class, new ResponseCallback<LiveUserAddressAddResponse>() {
                            @Override
                            public void onResponse(LiveUserAddressAddResponse date, boolean isFromCache) {
                                ToastUtils.showShort(date.getMessage());
                                setResult(RESULT_OK);
                                finish();
                                
                            }
                        }, "");
                    }
                    break;
                default:
            }
        }
    };
    
    
    private void initAddressPicker() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                String s = ResourceUtils.readAssets2String("province.json");
                addressBeans = new Gson().fromJson(s, new TypeToken<List<AddressBean>>() {
                }.getType());
                options2Items = new ArrayList<>();
                options3Items = new ArrayList<>();
                
                for (int i = 0; i < addressBeans.size(); i++) {//遍历省份
                    ArrayList<AddressBean.CityBean> CityList = new ArrayList<>();//该省的城市列表（第二级）
                    ArrayList<ArrayList<AddressBean.AreaBean>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
                    
                    for (int c = 0; c < addressBeans.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                        AddressBean.CityBean CityName = addressBeans.get(i).getCityList().get(c);
                        CityList.add(CityName);//添加城市
                        ArrayList<AddressBean.AreaBean> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                        
                        //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                        if (addressBeans.get(i).getCityList().get(c).getArea() == null
                            || addressBeans.get(i).getCityList().get(c).getArea().size() == 0) {
                            City_AreaList.add(new AddressBean.AreaBean());
                        } else {
                            City_AreaList.addAll(addressBeans.get(i).getCityList().get(c).getArea());
                        }
                        Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                    }
                    
                    /**
                     * 添加城市数据
                     */
                    options2Items.add(CityList);
                    
                    /**
                     * 添加地区数据
                     */
                    options3Items.add(Province_AreaList);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(Disposable d) {
                
                }
                
                @Override
                public void onNext(Object o) {
                
                }
                
                @Override
                public void onError(Throwable e) {
                
                }
                
                @Override
                public void onComplete() {
                    if (!isDestroyed()) {
                        showPickerView();
                    }
                }
            });
    }
    
    private int proviceId, cityId, areaId;
    private String provice, city, area;
    
    private void showPickerView() {// 弹出选择器
        
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                proviceId = addressBeans.get(options1).getNum();
                cityId = options2Items.get(options1).get(options2).getNum();
                areaId = options3Items.get(options1).get(options2).get(options3).getNum();
                provice = addressBeans.get(options1).getName();
                city = options2Items.get(options1).get(options2).getName();
                area = options3Items.get(options1).get(options2).get(options3).getName();
                //返回的分别是三个级别的选中位置
                String tx = new StringBuilder().append(provice).append(" ")
                    .append(city).append(" ").append(area).toString();
                tv_address_region.setText(tx);
            }
        })
            .setTitleText("城市选择")
            .setDividerColor(Color.BLACK)
            .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
            .setContentTextSize(20)
            .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(addressBeans, options2Items, options3Items);//三级选择器
        KeyboardUtils.hideSoftInput(ll_address_region);
        pvOptions.show();
    }
}
