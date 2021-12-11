package com.haoke91.a91edu.ui.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.gaosiedu.live.sdk.android.api.user.base.profile.update.LiveUserBaseProfileUpdateRequest;
import com.gaosiedu.live.sdk.android.api.user.base.profile.update.LiveUserBaseProfileUpdateResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haoke91.a91edu.GlobalConfig;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.entities.AddressBean;
import com.haoke91.a91edu.entities.StorageInfo;
import com.haoke91.a91edu.entities.UserInfo;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.a91edu.utils.imageloader.GlideUtils;
import com.haoke91.a91edu.utils.imageloader.MediaLoader;
import com.haoke91.a91edu.utils.manager.OssManager;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.ChoicePicDialog;
import com.haoke91.a91edu.widget.NoDoubleClickListener;
import com.haoke91.a91edu.widget.tilibrary.loader.Glide4ImageLoader;
import com.haoke91.a91edu.widget.tilibrary.style.index.NumberIndexIndicator;
import com.haoke91.a91edu.widget.tilibrary.style.progress.ProgressBarIndicator;
import com.haoke91.a91edu.widget.tilibrary.transfer.TransferConfig;
import com.haoke91.a91edu.widget.tilibrary.transfer.Transferee;
import com.haoke91.baselibrary.event.MessageItem;
import com.haoke91.baselibrary.event.RxBus;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.durban.Controller;
import com.yanzhenjie.durban.Durban;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
 * 创建时间：2018/7/12 上午11:17
 * 修改人：weiyimeng
 * 修改时间：2018/7/12 上午11:17
 * 修改备注：
 */
public class UserInfoActivity extends BaseActivity {
    private RelativeLayout rl_info_birthday, rl_userInfo_sex, rl_info_garden, rl_info_address;
    private ImageView iv_info_head;
    private TimePickerView pvCustomLunar;
    private TextView tv_info_birthday, tv_info_garden, tv_info_address, tv_userInfo_sex;
    private OptionsPickerView pvCustomOptions, pvOptions, sexPickView;
    private EditText et_info_nick, et_info_school, et_info_name;
    private List<AddressBean> addressBeans;
    private ArrayList<ArrayList<AddressBean.CityBean>> options2Items;
    private ArrayList<ArrayList<ArrayList<AddressBean.AreaBean>>> options3Items;
    private String proviceId, cityId, areaId;
    //    private String provice, city, area;
    
    public static void start(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }
    
    private UserInfo userInfo;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = UserManager.getInstance().getLoginUser();
        initDate();
    }
    
    private void initDate() {
        GlideUtils.loadHead(this, userInfo.getSmallHeadimg(), iv_info_head);
        et_info_nick.setText(userInfo.getLoginName());
        tv_userInfo_sex.setText(TextUtils.isEmpty(userInfo.getSex()) ? getString(R.string.secret) : "m".equalsIgnoreCase(userInfo.getSex()) ? getString(R.string.sex_man) : getString(R.string.sex_woman));
        et_info_school.setText(userInfo.getSchool());
        tv_info_garden.setText(Utils.getGardenByNumber(userInfo.getGrade()));
        tv_info_birthday.setText(userInfo.getBirthdayShow());
        tv_info_address.setText(new StringBuffer().append(userInfo.getProvinceName()).append(userInfo.getCityName()).append(userInfo.getAreaName()));
        et_info_name.setText(userInfo.getName());
        proviceId = userInfo.getProvince();
        cityId = userInfo.getCity();
        areaId = userInfo.getArea();
    }
    
    //    @Override
    //    protected void registeredEvent() {
    //        mRxBus = RxBus.getIntanceBus();
    //        mRxBus.doSubscribe(UserInfoActivity.class, MessageItem.class, new Consumer<MessageItem>() {
    //            @Override
    //            public void accept(MessageItem messageItem) throws Exception {
    //                if (messageItem.getType() == MessageItem.change_head) {
    //                    GlideUtils.load(UserInfoActivity.this, messageItem.getMessage(), iv_info_head);
    //                }
    //            }
    //        });
    //    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_userinfo;
    }
    
    @Override
    public void initialize() {
        TextView toolbar_right = findViewById(R.id.toolbar_right);
        toolbar_right.setVisibility(View.VISIBLE);
        toolbar_right.setText(getString(R.string.save));
        toolbar_right.setTextColor(Color.parseColor("#75C82B"));
        et_info_name = findViewById(R.id.et_info_name);
        et_info_school = findViewById(R.id.et_info_school);
        tv_userInfo_sex = findViewById(R.id.tv_userInfo_sex);
        et_info_nick = findViewById(R.id.et_info_nick);
        et_info_nick.clearFocus();
        rl_userInfo_sex = findViewById(R.id.rl_userInfo_sex);
        tv_info_address = findViewById(R.id.tv_info_address);
        tv_info_garden = findViewById(R.id.tv_info_garden);
        rl_info_garden = findViewById(R.id.rl_info_garden);
        rl_info_address = findViewById(R.id.rl_info_address);
        rl_info_address.setOnClickListener(onClickListener);
        tv_info_birthday = findViewById(R.id.tv_info_birthday);
        rl_info_birthday = findViewById(R.id.rl_userInfo_birthday);
        RelativeLayout rl_info_head = findViewById(R.id.rl_info_head);
        rl_info_head.setOnClickListener(onClickListener);
        rl_info_birthday.setOnClickListener(onClickListener);
        rl_info_garden.setOnClickListener(onClickListener);
        iv_info_head = findViewById(R.id.iv_info_head);
        iv_info_head.setOnClickListener(onClickListener);
        rl_userInfo_sex.setOnClickListener(onClickListener);
        toolbar_right.setOnClickListener(onClickListener);
        
        
    }
    
    private View.OnClickListener onClickListener = new NoDoubleClickListener() {
        @Override
        public void onDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.rl_info_head:
                    //                    UserImgActivity.start(UserInfoActivity.this);
                    showPicChoice();
                    break;
                case R.id.rl_userInfo_birthday:
                    if (pvCustomLunar == null) {
                        initTimePicker();
                    }
                    pvCustomLunar.show();
                    KeyboardUtils.hideSoftInput(rl_info_birthday);
                    break;
                case R.id.rl_info_garden:
                    if (pvCustomOptions == null) {
                        initClassPicker();
                    }
                    KeyboardUtils.hideSoftInput(rl_info_garden);
                    
                    pvCustomOptions.show();
                    break;
                case R.id.rl_info_address:
                    if (pvOptions == null) {
                        initAddressPicker();
                    } else {
                        pvOptions.show();
                    }
                    KeyboardUtils.hideSoftInput(rl_info_address);
                    break;
                case R.id.rl_userInfo_sex:
                    if (sexPickView == null) {
                        initSexPicker();
                    }
                    KeyboardUtils.hideSoftInput(rl_userInfo_sex);
                    sexPickView.show();
                    break;
                case R.id.toolbar_right:
                    if (isChangeHeadUrl) {
                        uploadImg(userInfo.getSmallHeadimg());
                    } else {
                        updateUserInfo("");
                    }
                    break;
                case R.id.iv_info_head:
                    showHeadImg();
                    break;
                default:
            }
        }
    };
    
    private void showHeadImg() {
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(iv_info_head);
        TransferConfig config = TransferConfig.build()
            //            .setThumbnailImageList(TextUtils.isEmpty(userInfo.getSmallHeadimg()) ? String.valueOf(R.mipmap.study_image_head) : userInfo.getSmallHeadimg() + "?imageView2/0/w/100/h/100")
            .setSourceImageList(TextUtils.isEmpty(userInfo.getSmallHeadimg()) ? String.valueOf(R.mipmap.study_image_head) : userInfo.getSmallHeadimg())
            .setMissPlaceHolder(R.mipmap.empty_small)
            .setErrorPlaceHolder(R.mipmap.empty_small)
            .setProgressIndicator(new ProgressBarIndicator())
            .setIndexIndicator(new NumberIndexIndicator())
            .setJustLoadHitImage(true)
            .setImageLoader(Glide4ImageLoader.with(getApplicationContext()))
            .create();
        
        config.setNowThumbnailIndex(0);
        config.setOriginImageList(imageViews);
        
        Transferee.getDefault(this).apply(config).show(new Transferee.OnTransfereeStateChangeListener() {
            @Override
            public void onShow() {
                Glide.with(UserInfoActivity.this).pauseRequests();
            }
            
            @Override
            public void onDismiss() {
                Glide.with(UserInfoActivity.this).resumeRequests();
            }
        });
    }
    
    /**
     * 更新用户信息
     */
    private void updateUserInfo(String ossPath) {
        final LiveUserBaseProfileUpdateRequest request = new LiveUserBaseProfileUpdateRequest();
        request.setUserId(UserManager.getInstance().getUserId());
        if (!TextUtils.isEmpty(ossPath)) {
            request.setSmallHeadimg(ossPath);
        }
        if (!TextUtils.isEmpty(et_info_nick.getText())) {
            request.setLoginName(et_info_nick.getText().toString().trim());//昵称
        }
        if (!TextUtils.isEmpty(et_info_name.getText())) {
            request.setName(et_info_name.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(tv_userInfo_sex.getText())) {
            request.setSex("男".equals(tv_userInfo_sex.getText().toString().trim()) ? "m" : "s");
        }
        if (!TextUtils.isEmpty(tv_info_birthday.getText())) {
            request.setBirthdayStr(tv_info_birthday.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(tv_info_garden.getText())) {
            request.setGrade(Utils.getNumByGarden(tv_info_garden.getText().toString().trim()));
        }
        if (!TextUtils.isEmpty(et_info_school.getText())) {
            request.setSchool(et_info_school.getText().toString());
        }
        if (proviceId != null) {
            request.setProvince(proviceId);
        }
        if (cityId != null) {
            request.setCity(cityId);
        }
        if (areaId != null) {
            request.setArea(areaId);
        }
        Api.getInstance().post(request, LiveUserBaseProfileUpdateResponse.class, new ResponseCallback<LiveUserBaseProfileUpdateResponse>() {
            @Override
            public void onResponse(LiveUserBaseProfileUpdateResponse date, boolean isFromCache) {
                ToastUtils.showShort(getString(R.string.save_success));
                LiveUserBaseProfileUpdateResponse.ResultData data = date.getData();
                userInfo.setLoginName(data.getLoginName());
                userInfo.setName(data.getName());
                userInfo.setSex(data.getSex());
                userInfo.setBirthdayShow(data.getBirthdayShow());
                userInfo.setGrade(data.getGrade());
                userInfo.setSmallHeadimg(data.getSmallHeadimg());
                
                //                userInfo.setProvince(request.getProvince());
                //                userInfo.setCity(request.getCity());
                userInfo.setProvinceName(data.getProvinceName());
                userInfo.setCityName(data.getCityName());
                userInfo.setAreaName(data.getAreaName());
                userInfo.setSchool(data.getSchool());
                UserManager.getInstance().saveUserInfo(userInfo);
                MessageItem messageItem = new MessageItem(MessageItem.action_login, null);
                RxBus.getIntanceBus().post(messageItem);
                finish();
            }
            
            @Override
            public void onError() {
                dismissLoadingDialog();
                ToastUtils.showShort(getString(R.string.net_error));
            }
        }, "");
    }
    
    private void showPicChoice() {
        ChoicePicDialog choicePicDialog = ChoicePicDialog.showDialog(UserInfoActivity.this);
        choicePicDialog.setOnSelectListener(new ChoicePicDialog.OnCamreaSelectListener() {
            @Override
            public void onPicSelect(boolean isCamrea) {
                if (isCamrea) {
                    Album.camera(UserInfoActivity.this)
                        .image()
                        .onResult(new Action<String>() {
                            @Override
                            public void onAction(@NonNull String result) {
                                Logger.e("result===" + result);
                                //  GlideUtils.load(UserImgActivity.this, result, iv_user_head);
                                cropImage(result);
                            }
                        })
                        .onCancel(new Action<String>() {
                            @Override
                            public void onAction(@NonNull String result) {
                            
                            }
                        })
                        .start();
                    
                } else {
                    Album.initialize(AlbumConfig.newBuilder(UserInfoActivity.this)
                        .setAlbumLoader(new MediaLoader())
                        .setLocale(Locale.getDefault())
                        .build());
                    Album.album(UserInfoActivity.this)
                        .singleChoice()
                        .columnCount(3)
                        .camera(false)
                        .widget(
                            Widget.newDarkBuilder(UserInfoActivity.this)
                                .title("相册")
                                .statusBarColor(Color.parseColor("#666666"))
                                .toolBarColor(Color.parseColor("#666666"))
                                .mediaItemCheckSelector(ContextCompat.getColor(UserInfoActivity.this, R.color.white), ContextCompat.getColor(UserInfoActivity.this, R.color.albumColorPrimaryDark))
                                .bucketItemCheckSelector(ContextCompat.getColor(UserInfoActivity.this, R.color.white), ContextCompat.getColor(UserInfoActivity.this, R.color.albumColorPrimaryDark))
                                .build()
                        )
                        .onResult(new com.yanzhenjie.album.Action<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                                //   GlideUtils.load(UserImgActivity.this, result.get(0).getPath(), iv_user_head);
                                if (!ObjectUtils.isEmpty(result)) {
                                    File file = new File(result.get(0).getPath());
                                    Logger.e("  file.get111111TotalSpace()===" + file.getTotalSpace());
                                    cropImage(result.get(0).getPath());
                                }///storage/emulated/0/91live/images/20180918_18177794171.JPEG
                            }
                        })
                        .onCancel(new com.yanzhenjie.album.Action<String>() {
                            @Override
                            public void onAction(@NonNull String result) {
                            }
                        })
                        .start();
                }
            }
        });
    }
    
    //    private List<AddressBean> addressBeans;
    //    private ArrayList<ArrayList<String>> options2Items;
    //    private ArrayList<ArrayList<ArrayList<String>>> options3Items;
    
    
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
    
    private void showPickerView() {// 弹出选择器
        
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                proviceId = String.valueOf(addressBeans.get(options1).getNum());
                cityId = String.valueOf(options2Items.get(options1).get(options2).getNum());
                areaId = String.valueOf(options3Items.get(options1).get(options2).get(options3).getNum());
                //                provice = addressBeans.get(options1).getName();
                //                city = options2Items.get(options1).get(options2).getName();
                //                area = options3Items.get(options1).get(options2).get(options3).getName();
                //返回的分别是三个级别的选中位置
                String tx = new StringBuilder().append(addressBeans.get(options1).getPickerViewText()).append(" ")
                    .append(options2Items.get(options1).get(options2).getName()).append(" ").append(options3Items.get(options1)
                        .get(options2).get(options3).getName()).toString();
                tv_info_address.setText(tx);
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
        pvOptions.show();
    }
    
    
    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1990, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2010, 11, 31);
        //时间选择器 ，自定义布局
        pvCustomLunar = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tv_info_birthday.setText(TimeUtils.date2String(date, new SimpleDateFormat("yyyy-MM-dd")));
            }
        })
            .setDate(selectedDate)
            .setRangDate(startDate, endDate)
            .isCyclic(true)
            .setLayoutRes(R.layout.pickerview_birthday, new CustomListener() {
                
                @Override
                public void customLayout(final View v) {
                    final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                    ImageView ivCancel = v.findViewById(R.id.iv_cancel);
                    tvSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pvCustomLunar.returnData();
                            pvCustomLunar.dismiss();
                        }
                    });
                    ivCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pvCustomLunar.dismiss();
                        }
                    });
                    //公农历切换
                    
                    
                }
            })
            .setType(new boolean[]{true, true, true, false, false, false})
            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            .setDividerColor(Color.RED)
            .build();
    }
    
    private void initClassPicker() {//条件选择器初始化，自定义布局
        String[] stringArray = getResources().getStringArray(R.array.allClass);
        final List<String> strings = Arrays.asList(stringArray);
        pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                tv_info_garden.setText(strings.get(options1));
            }
        })
            .setLayoutRes(R.layout.pickerview_garden, new CustomListener() {
                @Override
                public void customLayout(View v) {
                    final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                    tvSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pvCustomOptions.returnData();
                            pvCustomOptions.dismiss();
                        }
                    });
                    
                }
            })
            .setCyclic(true, true, true)
            .build();
        pvCustomOptions.setPicker(strings);//添加数据
    }
    
    private void initSexPicker() {//条件选择器初始化，自定义布局
        final List<String> strings = Arrays.asList("男", "女");
        sexPickView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                tv_userInfo_sex.setText(strings.get(options1));
            }
        })
            .setLayoutRes(R.layout.pickerview_sex, new CustomListener() {
                @Override
                public void customLayout(View v) {
                    v.findViewById(R.id.tv_finish).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sexPickView.returnData();
                            sexPickView.dismiss();
                        }
                    });
                }
            })
            //            .setLineSpacingMultiplier(2)
            .setDividerColor(Color.parseColor("#FBFBFB"))
            .setContentTextSize(20)
            .setCyclic(false, false, false)
            .build();
        //        sexPickView.setOnDismissListener(new OnDismissListener() {
        //            @Override
        //            public void onDismiss(Object o) {
        //                //                sexPickView.returnData();
        //            }
        //        });
        sexPickView.setPicker(strings);//添加数据
    }
    
    
    private void cropImage(String... imagePathList) {
        String cropDirectory = GlobalConfig.defaultImage;
        Durban.with(UserInfoActivity.this)
            .statusBarColor(Color.parseColor("#666666"))
            //   .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            // .toolBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .toolBarColor(Color.parseColor("#666666"))
            
            //     .navigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryBlack))
            // Image path list/array.
            .inputImagePaths(imagePathList)
            // Image output directory.
            .outputDirectory(cropDirectory)
            // Image size limit.
            .maxWidthHeight(500, 500)
            // Aspect ratio.
            .aspectRatio(1, 1)
            // Output format: JPEG, PNG.
            .compressFormat(Durban.COMPRESS_JPEG)
            // Compress quality, see Bitmap#compress(Bitmap.CompressFormat, int, OutputStream)
            .compressQuality(50)
            // Gesture: ROTATE, SCALE, ALL, NONE.
            .gesture(Durban.GESTURE_ALL)
            .controller(Controller.newBuilder()
                .enable(false)
                //  .rotation(true)
                .rotationTitle(true)
                .scale(true)
                .scaleTitle(true)
                .build())
            .requestCode(200)
            .start();
    }
    
    private boolean isChangeHeadUrl;
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (!ObjectUtils.isEmpty(data) && !ObjectUtils.isEmpty(Durban.parseResult(data))) {
                GlideUtils.load(UserInfoActivity.this, Durban.parseResult(data).get(0), iv_info_head);
                //                RxBus.getIntanceBus().post(new MessageItem(MessageItem.change_head, Durban.parseResult(data).get(0)));
                userInfo.setSmallHeadimg(Durban.parseResult(data).get(0));
                isChangeHeadUrl = true;
                //                OssManager.getInstance().getUpLoadConfig(Durban.parseResult(data).get(0),null);
            }
            
        }
    }
    
    ///storage/emulated/0/91live/images/20180920_11445271568.JPEG
    private void uploadImg(final String path) {
        showLoadingDialog();
        //        File file = new File(path);
        //        String objectKey = GlobalConfig.OSSIMGPATH + UserManager.getInstance().getUserId() + File.separator + System.currentTimeMillis() + "." + ImageUtils.getImageType(path);
        ArrayList<String> strings = new ArrayList<>();
        strings.add(System.currentTimeMillis() + "." + ImageUtils.getImageType(path));
        OssManager.getInstance().getUpLoadConfig(new OssManager.ResponseCallback<StorageInfo>() {
            @Override
            public void onSuccess(StorageInfo date) {
                if (!ObjectUtils.isEmpty(date.getBody())) {
                    if (!ObjectUtils.isEmpty(date.getBody().get(0))) {
                        OssManager.getInstance().upLoad(date.getBody().get(0), path, new OssManager.ResponseCallback<String>() {
                            @Override
                            public void onSuccess(String date) {
                                updateUserInfo(date);
                                isChangeHeadUrl = false;
                            }
                            
                            @Override
                            public void onError() {
                                updateUserInfo("");
                                
                            }
                        });
                    }
                }
            }
        }, strings);
        
        
        //        OssManager.getInstance().uploadFile(path, objectKey, new OssManager.ResponseCallback<String>() {
        //            @Override
        //            public void onSuccess(String path) {
        //                updateUserInfo(path);
        //                isChangeHeadUrl = false;
        //            }
        //
        //            @Override
        //            public void onError() {
        //                updateUserInfo("");
        //            }
        //
        //        });
        
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRxBus != null) {
            mRxBus.unSubscribe(UserInfoActivity.class);
        }
    }
}
