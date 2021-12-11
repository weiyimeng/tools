package com.gstudentlib.util;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.gaosi.webresource_uploader.IClickEventInterceptorListener;
import com.gaosi.webresource_uploader.WebResourceUploader;
import com.gsbaselib.InitBaseLib;
import com.gsbaselib.net.GSRequest;
import com.gsbaselib.net.callback.AbsGsCallback;
import com.gsbaselib.utils.FileUtil;
import com.gsbaselib.utils.LangUtil;
import com.gsbaselib.utils.ToastUtil;
import com.gstudentlib.R;
import com.gstudentlib.base.STBaseActivity;
import com.gstudentlib.base.STBaseConfigManager;
import com.gstudentlib.base.STBaseConstants;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * mock工具面板
 */
public class MockDialog extends Dialog implements View.OnClickListener {

    private STBaseActivity mActivity;
    private View mContentView;
    private Button btnSave;
    private Button btnFinish;
    private Button btnBi;
    private Button btnDabao;
    private Button btnUpdateRes;

    private CheckBox cbUserServer;//自定义
    private EditText edtMockIp;//mock地址

    private CheckBox cbH5;//使用在线h5
    private EditText edtH5Ip;//在线h5调试地址

    private CheckBox cbDev;//开发环境
    private CheckBox cbRelease;//release正式环境

    private CheckBox cbAliTest1;//阿里test1

    /**
     * 实例化mock面板
     * @param context
     */
    public MockDialog(Context context) {
        super(context, com.gsbaselib.R.style.GSBL_BaseDialogStyle);
        mActivity = (STBaseActivity) context;
        prepareContentView();
        setContentView(mContentView);
    }

    /**
     * 初始化view
     */
    private void prepareContentView() {
        mContentView = LayoutInflater.from(getContext()).inflate(R.layout.ui_mock_dialog,null);
        btnSave = mContentView.findViewById(R.id.btnSave);
        btnFinish = mContentView.findViewById(R.id.btnFinish);
        btnBi = mContentView.findViewById(R.id.btnBi);
        btnDabao = mContentView.findViewById(R.id.btnDabao);
        btnUpdateRes = mContentView.findViewById(R.id.btnUpdateRes);

        cbUserServer = mContentView.findViewById(R.id.cbUserServer);
        edtMockIp = mContentView.findViewById(R.id.edtMock);

        cbH5 = mContentView.findViewById(R.id.cbUseOnlineH5);
        edtH5Ip = mContentView.findViewById(R.id.edtH5Ip);

        cbDev = mContentView.findViewById(R.id.cbDebug);
        cbRelease = mContentView.findViewById(R.id.cbRelease);
        cbAliTest1 = mContentView.findViewById(R.id.cbAliTest1);
        refreshUI();
        btnSave.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnBi.setOnClickListener(this);
        btnDabao.setOnClickListener(this);
        cbUserServer.setOnClickListener(this);
        cbH5.setOnClickListener(this);
        cbDev.setOnClickListener(this);
        cbRelease.setOnClickListener(this);
        cbAliTest1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    STBaseConstants.isUseIp = true;
                } else {
                    STBaseConstants.isUseIp = false;
                }
            }
        });
    }

    /**
     * 显示已经配置的信息
     */
    public void refreshUI() {
        //自定义
        cbUserServer.setChecked((InitBaseLib.getInstance().getConfigManager()).getUserServer());
        //mock地址
        edtMockIp.setText(InitBaseLib.getInstance().getConfigManager().getMockServerIp());

        //使用在线h5
        cbH5.setChecked((InitBaseLib.getInstance().getConfigManager()).getUseOnlineH5());
        //在线h5调试地址
        edtH5Ip.setText(InitBaseLib.getInstance().getConfigManager().getH5ServerIp());

        boolean isRelease = InitBaseLib.getInstance().getConfigManager().getServerType().equals("release");
        cbDev.setChecked(!isRelease);
        cbRelease.setChecked(isRelease);
        WebResourceUploader.init(true, FileUtil.getH5ResourceDir(), FileUtil.getRnResourceDir());
        WebResourceUploader.setClickTriggerView(btnUpdateRes, mActivity, new IClickEventInterceptorListener(){
            @Override
            public void onClickReady() {
            }

            @Override
            public void onClickBefore() {
                dismiss();
            }

            @Override
            public void onClickAfter() {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            saveIp();
            dismiss();
        } else if (v == btnFinish) {
            dismiss();
            mActivity.finish();
        } else if (v == btnBi) {
            dismiss();
            mActivity.gotoBi();
        }else if(v == btnDabao) {
            dismiss();
            ToastUtil.showToast("请安静等待10分钟，包正生产中...");
            Map<String , String> panams = new HashMap<>();
            panams.put("token" , "9ec87375e9710ce68d352f13bc6c5a");
            panams.put("ref" , "release");
            GSRequest.startRequest("http://gitlab.ops.aixuexi.com/api/v4/projects/17/trigger/pipeline", panams, new AbsGsCallback() {
                @Override
                public void onResponseSuccess(Response response, int i, @NonNull Object o) {
                }
                @Override
                public void onResponseError(Response response, int i, String s) {
                }
            });
        }else if (v == cbUserServer) {
            ((STBaseConfigManager)InitBaseLib.getInstance().getConfigManager()).setUserServer(cbUserServer.isChecked());
            if ((InitBaseLib.getInstance().getConfigManager()).getUserServer()) {
                updateServer();
            }
        } else if (v == cbH5) {
            ((STBaseConfigManager)InitBaseLib.getInstance().getConfigManager()).setUseOnlineH5(cbH5.isChecked());
        } else if (v == cbDev) {
            updateServerType(false);
        } else if (v == cbRelease) {
            updateServerType(true);
        }
    }

    /**
     * 保存mock的服务器地址与h5在线调试地址与作业web调试地址
     */
    private void saveIp() {
        if (!TextUtils.isEmpty(edtMockIp.getText())){
            InitBaseLib.getInstance().getConfigManager().updateMockServerIp(edtMockIp.getText().toString());
        }

        if (!TextUtils.isEmpty(edtH5Ip.getText())) {
            String h5Ip = edtH5Ip.getText().toString();
            InitBaseLib.getInstance().getConfigManager().updateH5ServerIp(h5Ip);
        }
    }

    /**
     * 更新环境（release 代表正式环境，dev代表开发环境）
     * @param isRelease
     */
    private void updateServerType(boolean isRelease) {
        cbDev.setChecked(!isRelease);
        cbRelease.setChecked(isRelease);
        InitBaseLib.getInstance().getConfigManager().updateServerType(isRelease ? "release" : "debug");
    }

    /**
     * 更新服务器地址
     */
    private void updateServer() {
        if (LangUtil.isEmpty(edtMockIp.getText())) {
            ToastUtil.showToast("请输入Server地址");
            return;
        }

        String ip = edtMockIp.getText().toString().trim();
        String server = "";
        if (!ip.startsWith("http")) {
            server += "http://";
        }
        server = server + ip;
        if (!server.endsWith("/")) {
            server += "/";
        }
        InitBaseLib.getInstance().getConfigManager().updateServer(server);
    }

    /**
     * 设置宽度全屏，要设置在show的后面
     */
    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setWindowAnimations(com.gsbaselib.R.style.BottomAnim);

        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
