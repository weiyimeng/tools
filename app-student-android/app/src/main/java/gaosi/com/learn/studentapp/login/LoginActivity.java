package gaosi.com.learn.studentapp.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.OneKeyLoginListener;
import com.chuanglan.shanyan_sdk.listener.OpenLoginAuthListener;
import com.gaosi.passport.Passport;
import com.gaosi.passport.PassportAPI;
import com.gaosi.passport.bean.BaseResponseBean;
import com.gaosi.passport.bean.LoginResponseBean;
import com.gaosi.passport.bean.UserSwitchResponseBean;
import com.github.mzule.activityrouter.annotation.Router;
import com.gsbaselib.InitBaseLib;
import com.gsbaselib.base.log.LogUtil;
import com.gsbaselib.net.GSRequest;
import com.gsbaselib.net.callback.GSHttpResponse;
import com.gsbaselib.net.callback.GSJsonCallback;
import com.gsbaselib.utils.FileUtil;
import com.gsbaselib.utils.LOGGER;
import com.gsbaselib.utils.SharedPreferenceUtil;
import com.gsbaselib.utils.StatusBarUtil;
import com.gsbaselib.utils.ToastUtil;
import com.gsbaselib.utils.customevent.OnNoDoubleClickListener;
import com.gsbaselib.utils.dialog.AbsAdapter;
import com.gsbaselib.utils.dialog.DialogUtil;
import com.gsbaselib.utils.glide.ImageLoader;
import com.gsbiloglib.builder.GSConstants;
import com.gsbiloglib.log.GSLogUtil;
import com.gstudentlib.SDKConfig;
import com.gstudentlib.base.BaseActivity;
import com.gstudentlib.base.STBaseConstants;
import com.gstudentlib.bean.StudentInfo;
import com.gstudentlib.util.SchemeDispatcher;
import com.lzy.okgo.model.Response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gaosi.com.learn.R;
import gaosi.com.learn.application.AppApi;
import gaosi.com.learn.application.AppSDKConfig;
import gaosi.com.learn.bean.login.LoginPageConfigBean;
import gaosi.com.learn.bean.login.StudentInfoBody;
import gaosi.com.learn.view.LoginCardView;

/**
 * 登录界面
 * Created by huangshan on 2020/07/28.
 */
@Router("login")
public class LoginActivity extends BaseActivity {

    private static final int CHOOSE_USER_PAGE_REQUEST = 130;

    //获取SD卡的读写权限、手机状态权限请求
    public static final int PERMISSION_REQUEST = 12;
    //密码登陆验证
    private static final String LOGIN_VERIFICATION_CODE = "1026";

    private AlertDialog mPermissionDialog;

    private RelativeLayout rlCodeLogin;
    private RelativeLayout rlPassLogin;
    private TextView tvCodeLogin;
    private TextView tvPassLogin;
    private View lineCodeLogin;
    private View linePassLogin;

    private LoginCardView vPasswordCard;
    private ImageView btnPasswordLogin;
    private LinearLayout ll_password_verification;
    private TextView tv_tips_verification_code;
    private LinearLayout llOneKeyLoginPass;
    private LinearLayout llOneKeyLoginCode;

    private LoginCardView vSecurityCodeCard;
    private ImageView btnSecurityCodeLogin;
    private TextView tvAgreePolicy;
    private CheckBox checkBox;
    private Button btnLoginByUserId;

    //默认显示那种类型的登录方式：0代表默认显示密码登录，1代表默认显示验证码登录
    private String mLoginType = LoginCardView.CODE_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.chooseLoginType();
        this.requestLoginPageConfig();
        PassportAPI.Companion.getInstance().kickOut();
        requestPermission();
    }

    /**
     * 获取权限
     */
    private void requestPermission() {
        if (!hasPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)) {
            requestPermissions("存储权限&获取手机状态权限申请", PERMISSION_REQUEST, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE);
        } else {
            checkUpdate();
            getPhoneInfo();
        }
}

    @Override
    protected void initView() {
        super.initView();
        String phone = getPhone();
        rlCodeLogin = findViewById(R.id.rlCodeLogin);
        rlPassLogin = findViewById(R.id.rlPassLogin);
        tvCodeLogin = findViewById(R.id.tvCodeLogin);
        tvPassLogin = findViewById(R.id.tvPassLogin);
        lineCodeLogin = findViewById(R.id.lineCodeLogin);
        linePassLogin = findViewById(R.id.linePassLogin);
        vPasswordCard = findViewById(R.id.v_login_pass);
        llOneKeyLoginPass = vPasswordCard.findViewById(R.id.llOneKeyLogin);
        btnPasswordLogin = vPasswordCard.findViewById(R.id.btn_login);
        ll_password_verification = vPasswordCard.findViewById(R.id.ll_password_verification);
        tv_tips_verification_code = vPasswordCard.findViewById(R.id.tv_tips_verification_code);
        rlCodeLogin.setOnClickListener(this);
        rlPassLogin.setOnClickListener(this);
        vPasswordCard.showPasswordLoginView();
        if (!TextUtils.isEmpty(phone)) {
            vPasswordCard.changePhoneNum(phone);
        }

        vSecurityCodeCard = findViewById(R.id.v_login_code);
        llOneKeyLoginCode = vSecurityCodeCard.findViewById(R.id.llOneKeyLogin);
        btnSecurityCodeLogin = vSecurityCodeCard.findViewById(R.id.btn_login);
        vSecurityCodeCard.showSecurityCodeView();
        if (!TextUtils.isEmpty(phone)) {
            vSecurityCodeCard.changePhoneNum(phone);
        }

        llOneKeyLoginPass.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                OneKeyLoginManager.getInstance().setAuthThemeConfig(OneKeyLoginUIConfig.INSTANCE.getConfig(LoginActivity.this));
                openLoginActivity();
                collectClickEvent("XSD_645");
            }
        });
        llOneKeyLoginCode.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                OneKeyLoginManager.getInstance().setAuthThemeConfig(OneKeyLoginUIConfig.INSTANCE.getConfig(LoginActivity.this));
                openLoginActivity();
                collectClickEvent("XSD_645");
            }
        });
        btnPasswordLogin.setOnClickListener(this);
        btnSecurityCodeLogin.setOnClickListener(this);
        btnPasswordLogin.setClickable(false);
        btnSecurityCodeLogin.setClickable(false);

        tvAgreePolicy = findViewById(R.id.tvAgreePolicy);
        checkBox = findViewById(R.id.checkBox);
        btnLoginByUserId = findViewById(R.id.btn_loginByUserId);
        btnLoginByUserId.setOnClickListener(this);
        if (STBaseConstants.isDebug) {
            btnLoginByUserId.setVisibility(View.VISIBLE);
        } else {
            btnLoginByUserId.setVisibility(View.GONE);
        }

        String agreePolicyStr = "请阅读并同意《用户服务协议》《隐私政策》";
        SpannableStringBuilder builder = new SpannableStringBuilder(agreePolicyStr);
        String firstString = "《用户服务协议》";
        String secondString = "《隐私政策》";
        int firstIndex = agreePolicyStr.indexOf(firstString);
        int firstLength = firstString.length();
        int secondIndex = agreePolicyStr.indexOf(secondString);
        int secondLength = secondString.length();
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#1F243D")), firstIndex, firstIndex + firstLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#1F243D")), secondIndex, secondIndex + secondLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置文字点击事件
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = "axx://commonWeb?url=%s";
                String filePath = FileUtil.getJsBundleSaveFilePath("agreement_userService.html");
                url = String.format(url, "file://" + filePath);
                SchemeDispatcher.jumpPage(LoginActivity.this, url);
                GSLogUtil.collectClickLog(GSConstants.Companion.getP().getCurrRefer(), "XSD_355", "");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                //去除连接下划线
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = "axx://commonWeb?url=%s";
                String filePath = FileUtil.getJsBundleSaveFilePath("agreement_privacy.html");
                url = String.format(url, "file://" + filePath);
                SchemeDispatcher.jumpPage(LoginActivity.this, url);
                GSLogUtil.collectClickLog(GSConstants.Companion.getP().getCurrRefer(), "XSD_356", "");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                //去除连接下划线
                ds.setUnderlineText(false);
            }
        };
        builder.setSpan(clickableSpan1, firstIndex, firstIndex + firstLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(clickableSpan2, secondIndex, secondIndex + secondLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvAgreePolicy.setMovementMethod(LinkMovementMethod.getInstance());
        tvAgreePolicy.setHighlightColor(ContextCompat.getColor(this, android.R.color.transparent));
        tvAgreePolicy.setText(builder);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            vPasswordCard.checkSubmitButtonValid();
            vSecurityCodeCard.checkSubmitButtonValid();
        });

        //因为后台需要重新绑定用户的个推信息，所以需要记录一下
        SharedPreferenceUtil.setBooleanDataIntoSP("userInfo", "_isRemoveToken", true);
        chooseLoginType();
        if (InitBaseLib.getInstance().getConfigManager().isMonkey()) {
            chooseMonkeyType();
        }
    }

    /**
     * 选择登录方式，登录方式和上次登录方式保持一致
     */
    private void chooseLoginType() {
        mLoginType = getLastLoginType();
        if (InitBaseLib.getInstance().getConfigManager().isMonkey()) {
            return;
        }
        //移动其中一个card，防止页面重合
        vSecurityCodeCard.post(() -> {
            if (TextUtils.equals(mLoginType, LoginCardView.PASS_TYPE)) {
                setTabStyle();
                showOrHideView(vPasswordCard, vSecurityCodeCard);
                mHandler.postDelayed(() -> vPasswordCard.adjustUserIntent(3), 300);
            } else {
                setTabStyle();
                showOrHideView(vSecurityCodeCard, vPasswordCard);
                mHandler.postDelayed(() -> vSecurityCodeCard.adjustUserIntent(3), 300);
            }
        });
    }

    /**
     * 设配自动化打包
     */
    private void chooseMonkeyType() {
        //移动其中一个card，防止页面重合
        vSecurityCodeCard.post(() -> {
            showOrHideView(vPasswordCard, vSecurityCodeCard);
            vPasswordCard.findViewById(R.id.tvQuestion).setVisibility(View.GONE);
        });
    }

    /**
     * 请求登录配置
     */
    private void requestLoginPageConfig() {
        HashMap<String, String> params = new HashMap<>();
        GSRequest.startRequest(AppApi.loginPageConfig, params, new GSJsonCallback<LoginPageConfigBean>() {
            @Override
            public void onResponseSuccess(Response response, int i, @NonNull GSHttpResponse<LoginPageConfigBean> result) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                if (showResponseErrorMessage(result) == 0) {
                    return;
                }
                if (result.body == null) {
                    GSLogUtil.collectPerformanceLog(GSLogUtil.PerformanceType.HTTP_REQUEST_ERROR, mUrl, mResponse);
                    return;
                }
                LoginPageConfigBean loginPageConfigBean = result.body;
                if (loginPageConfigBean.getRegisterButtonSwitch() == 1) {
                    vPasswordCard.showRegisterView(1);
                    vSecurityCodeCard.showRegisterView(1);
                } else {
                    vPasswordCard.showRegisterView(0);
                    vSecurityCodeCard.showRegisterView(0);
                }
                if (loginPageConfigBean.getOneClickLoginButtonSwitch() == 1) {
                    vPasswordCard.showOneKeyLoginView(true);
                    vSecurityCodeCard.showOneKeyLoginView(true);
                } else {
                    vPasswordCard.showOneKeyLoginView(false);
                    vSecurityCodeCard.showOneKeyLoginView(false);
                }
            }

            @Override
            public void onResponseError(Response response, int i, String s) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                if (!TextUtils.isEmpty(s)) {
                    ToastUtil.showToast(s);
                }
            }
        });
    }

    private void openLoginActivity() {
        OneKeyLoginManager.getInstance().openLoginAuth(false, new OpenLoginAuthListener() {
            @Override
            public void getOpenLoginAuthStatus(int code, String result) {
                if (1000 == code) {
                    LogUtil.i("闪验 拉起授权页成功: code==" + code + "result==" + result);
                } else {
                    LogUtil.i("闪验 拉起授权页失败: code==" + code + "result==" + result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String innerDesc = jsonObject.optString("innerDesc");
                        if (TextUtils.isEmpty(innerDesc)) {
                            ToastUtil.showToast("操作失败请重试");
                        } else {
                            ToastUtil.showToast(innerDesc);
                        }
                    } catch (Exception e) {
                        LOGGER.log(e);
                    }
                }
            }
        }, new OneKeyLoginListener() {
            @Override
            public void getOneKeyLoginStatus(int code, String result) {
                OneKeyLoginManager.getInstance().setLoadingVisibility(false);
                if (code == 1011) {
                    LogUtil.i("闪验 用户点击授权页返回: code==" + code + "result==" + result);
                } else if (code == 1000) {
                    LogUtil.i("闪验 用户点击登录获取token成功: code==" + code + "result==" + result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String token = jsonObject.optString("token");
                        oneKeyLogin(token);
                        OneKeyLoginManager.getInstance().finishAuthActivity();
                    } catch (Exception e) {
                        LOGGER.log(e);
                    }
                } else {
                    LogUtil.i("闪验 用户点击登录获取token失败: code==" + code + "result==" + result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String innerDesc = jsonObject.optString("innerDesc");
                        if (TextUtils.isEmpty(innerDesc)) {
                            ToastUtil.showToast("登录失败请重试");
                        } else {
                            ToastUtil.showToast(innerDesc);
                        }
                    } catch (Exception e) {
                        LOGGER.log(e);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (isFinishing()) {
            return;
        }

        if (v == rlCodeLogin) {
            mLoginType = LoginCardView.CODE_TYPE;
            if (vSecurityCodeCard.getVisibility() == View.GONE) {
                setTabStyle();
                vSecurityCodeCard.adjustUserIntent(4);
                showOrHideView(vSecurityCodeCard, vPasswordCard);
                vSecurityCodeCard.findViewById(R.id.tvForgetPsw).setVisibility(View.GONE);
            }
        } else if (v == rlPassLogin) {
            mLoginType = LoginCardView.PASS_TYPE;
            if (vPasswordCard.getVisibility() == View.GONE) {
                setTabStyle();
                vPasswordCard.adjustUserIntent(4);
                showOrHideView(vPasswordCard, vSecurityCodeCard);
                vPasswordCard.findViewById(R.id.tvForgetPsw).setVisibility(View.VISIBLE);
            }
        } else if (v == btnPasswordLogin) {
            mLoginType = LoginCardView.PASS_TYPE;
            collectClickEvent("XSD_583");
            login();
        } else if (v == btnSecurityCodeLogin) {
            mLoginType = LoginCardView.CODE_TYPE;
            collectClickEvent("XSD_582");
            login();
        } else if (v == btnLoginByUserId) {
            startActivity(new Intent(this, LoginByUserIdActivity.class));
        }
    }

    public Boolean getPolicyCheckedStatus() {
        return checkBox.isChecked();
    }

    /**
     * 设置tab样式
     */
    private void setTabStyle() {
        switch (mLoginType) {
            case LoginCardView.CODE_TYPE:
                tvCodeLogin.setTextColor(Color.parseColor("#1F243D"));
                tvCodeLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F);
                lineCodeLogin.setVisibility(View.VISIBLE);
                tvPassLogin.setTextColor(Color.parseColor("#A3B3C2"));
                tvPassLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F);
                linePassLogin.setVisibility(View.INVISIBLE);
                break;
            case LoginCardView.PASS_TYPE:
                tvPassLogin.setTextColor(Color.parseColor("#1F243D"));
                tvPassLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F);
                linePassLogin.setVisibility(View.VISIBLE);
                tvCodeLogin.setTextColor(Color.parseColor("#A3B3C2"));
                tvCodeLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F);
                lineCodeLogin.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        if (mLoginType.equals(LoginCardView.PASS_TYPE)) {
            if (ll_password_verification.getVisibility() == View.VISIBLE) {
                if (TextUtils.isEmpty(vPasswordCard.getInputPhoneNum()) || TextUtils.isEmpty(vPasswordCard.getInputPasswordVerification())) {
                    return;
                }
            } else {
                if (TextUtils.isEmpty(vPasswordCard.getInputPhoneNum()) || TextUtils.isEmpty(vPasswordCard.getInputPassword())) {
                    return;
                }
            }
        } else {
            if (TextUtils.isEmpty(vSecurityCodeCard.getInputPhoneNum()) || TextUtils.isEmpty(vSecurityCodeCard.getInputPassword())) {
                return;
            }
        }
        showLoadingProgressDialog();
        if (mLoginType.equals(LoginCardView.PASS_TYPE)) {
            if (ll_password_verification.getVisibility() == View.VISIBLE) {
                //验证码登陆
                loginByValidateCode(vPasswordCard.getInputPhoneNum(), vPasswordCard.getInputPasswordVerification());
            } else {
                PassportAPI.Companion.getInstance().loginByPassword(vPasswordCard.getInputPhoneNum()
                        , vPasswordCard.getInputPassword()
                        , new Passport.Callback<LoginResponseBean>() {
                            @Override
                            public void onSuccess(@NotNull BaseResponseBean<LoginResponseBean> response) {
                                if (isFinishing()) {
                                    return;
                                }
                                dismissLoadingProgressDialog();
                                if (response != null) {
                                    if (response.getStatus() == 0) {
                                        if (response.getErrorCode().equals(PassportAPI.CODE_1001) || response.getErrorCode().equals("0")) {
                                            ToastUtil.showToast("登录失败，该账号未在爱学习注册");
                                        } else {
                                            ToastUtil.showToast(response.getErrorMessage() + "");
                                        }
                                        if (response.getErrorCode().equals(LOGIN_VERIFICATION_CODE)) {
                                            ll_password_verification.setVisibility(View.VISIBLE);
                                            tv_tips_verification_code.setVisibility(View.VISIBLE);
                                            vPasswordCard.adjustUserIntent(1);
                                            vPasswordCard.checkSubmitButtonValid();
                                            //发送验证码
                                            vPasswordCard.getSecurityCode();
                                            ToastUtil.showToast("验证码已发送");
                                        }
                                        return;
                                    }
                                }
                                if (response.getBody() == null) {
                                    return;
                                }
                                setLoginType();
                                if (mLoginType.equals(LoginCardView.PASS_TYPE)) {
                                    setPhone(vPasswordCard.getInputPhoneNum());
                                } else {
                                    setPhone(vSecurityCodeCard.getInputPhoneNum());
                                }
                                STBaseConstants.Token = PassportAPI.Companion.getInstance().getToken_S();
                                STBaseConstants.businessUserId = PassportAPI.Companion.getInstance().getUser_Id();
                                checkUserList();
                            }

                            @Override
                            public void onError(@Nullable String message, @Nullable Integer code) {
                                if (isFinishing()) {
                                    return;
                                }
                                dismissLoadingProgressDialog();
                                ToastUtil.showToast(message + "");
                            }
                        });
            }
        } else {
            loginByValidateCode(vSecurityCodeCard.getInputPhoneNum(), vSecurityCodeCard.getInputPassword());
        }
    }

    /**
     * 闪验一键登录
     * @param token 闪验验证token
     */
    private void oneKeyLogin(String token) {
        showLoadingProgressDialog();
        PassportAPI.Companion.getInstance().oneKeyLogin(AppSDKConfig.SHANYAN_APP_ID, token, new Passport.Callback<LoginResponseBean>() {
            @Override
            public void onSuccess(@NotNull BaseResponseBean<LoginResponseBean> response) {
                if (isFinishing()) {
                    return;
                }
                dismissLoadingProgressDialog();
                if (response != null) {
                    if (response.getStatus() == 0) {
                        if (response.getErrorCode().equals(PassportAPI.CODE_1001) || response.getErrorCode().equals("0")) {
                            ToastUtil.showToast("登录失败，该账号未在爱学习注册");
                        } else {
                            ToastUtil.showToast(response.getErrorMessage() + "");
                        }
                        return;
                    }
                }
                if (response.getBody() == null) {
                    return;
                }
                STBaseConstants.Token = PassportAPI.Companion.getInstance().getToken_S();
                STBaseConstants.businessUserId = PassportAPI.Companion.getInstance().getUser_Id();
                checkUserList();
            }

            @Override
            public void onError(@Nullable String message, @Nullable Integer code) {
                if (isFinishing()) {
                    return;
                }
                dismissLoadingProgressDialog();
                ToastUtil.showToast(message + "");
            }
        });
    }

    /**
     * 验证码登陆
     *
     * @param phoneNum     手机号
     * @param validateCode 验证码
     */
    private void loginByValidateCode(String phoneNum, String validateCode) {
        PassportAPI.Companion.getInstance().loginByValidateCode(phoneNum
                , validateCode
                , new Passport.Callback<LoginResponseBean>() {
                    @Override
                    public void onSuccess(@NotNull BaseResponseBean<LoginResponseBean> response) {
                        if (isFinishing()) {
                            return;
                        }
                        dismissLoadingProgressDialog();
                        if (response != null) {
                            if (response.getStatus() == 0) {
                                if (response.getErrorCode().equals(PassportAPI.CODE_1001) || response.getErrorCode().equals("0")) {
                                    ToastUtil.showToast("登录失败，该账号未在爱学习注册");
                                } else {
                                    ToastUtil.showToast(response.getErrorMessage() + "");
                                }
                                return;
                            }
                        }
                        if (response.getBody() == null) {
                            return;
                        }
                        setLoginType();
                        if (mLoginType.equals(LoginCardView.PASS_TYPE)) {
                            setPhone(vPasswordCard.getInputPhoneNum());
                        } else {
                            setPhone(vSecurityCodeCard.getInputPhoneNum());
                        }
                        STBaseConstants.Token = PassportAPI.Companion.getInstance().getToken_S();
                        STBaseConstants.businessUserId = PassportAPI.Companion.getInstance().getUser_Id();
                        checkUserList();
                    }

                    @Override
                    public void onError(@Nullable String message, @Nullable Integer code) {
                        if (isFinishing()) {
                            return;
                        }
                        dismissLoadingProgressDialog();
                        ToastUtil.showToast(message + "");
                    }
                });
    }

    /**
     * 登录成功后，需要判断当前账号下有几个学生
     */
    private void checkUserList() {
        showLoadingProgressDialog();
        Map<String, String> params = new HashMap();
        params.put("pageNum", "1");
        GSRequest.startRequest(AppApi.getLoginSuccessStudentInfo, params, new GSJsonCallback<StudentInfoBody>() {
            @Override
            public void onResponseSuccess(Response response, int code, GSHttpResponse<StudentInfoBody> result) {
                if (isFinishing()) {
                    return;
                }
                dismissLoadingProgressDialog();
                if (result.isSuccess()) {
                    if (result.body != null && result.body.getStudentPage() != null && result.body.getStudentPage().getList() != null
                            && result.body.getStudentPage().getList().size() > 0) {
                        STBaseConstants.isBeixiao = result.body.getBeixiao() ? 1 : 0;
                        STBaseConstants.changedPasswordCode = result.body.getChangedPasswordCode();
                        if (result.body.getStudentPage().getList().size() > 1) {
                            dismissLoadingProgressDialog();
                            gotoUserListPage();
                        } else {
                            result.body.getStudentPage().getList().get(0).setIsBeiXiao(STBaseConstants.isBeixiao);
                            loginForUser(result.body.getStudentPage().getList().get(0), false);
                        }
                    } else {
                        ToastUtil.showToast(result.message);
                    }
                } else {
                    ToastUtil.showToast(result.message);
                }
            }

            @Override
            public void onResponseError(Response response, int code, String message) {
                if (isFinishing()) {
                    return;
                }
                dismissLoadingProgressDialog();
            }
        });
    }

    private void gotoUserListPage() {
        Intent intent = new Intent(mContext, UserListActivity.class);
        startActivityForResult(intent, CHOOSE_USER_PAGE_REQUEST);
    }

    /**
     * 通过userId进行登录
     */
    private void loginForUser(StudentInfo studentInfo, boolean isForce) {
        showLoadingProgressDialog();
        PassportAPI.Companion.getInstance().userSwitch(studentInfo.getUserId(), isForce
                , new Passport.Callback<UserSwitchResponseBean>() {
                    @Override
                    public void onSuccess(@NotNull BaseResponseBean<UserSwitchResponseBean> response) {
                        if (isFinishing()) {
                            return;
                        }
                        dismissLoadingProgressDialog();
                        if (response.getStatus() == 0) {
                            if (response.getErrorCode().equals(PassportAPI.CODE_1003)) {
                                showLoginInAnotherDeviceDialog(studentInfo);
                            } else {
                                STBaseConstants.Token = PassportAPI.Companion.getInstance().getToken_S();
                                checkClientId(studentInfo, STBaseConstants.Token);
                            }
                        } else {
                            STBaseConstants.Token = PassportAPI.Companion.getInstance().getToken_S();
                            checkClientId(studentInfo, STBaseConstants.Token);
                        }
                    }

                    @Override
                    public void onError(@Nullable String message, @Nullable Integer code) {
                        if (isFinishing()) {
                            return;
                        }
                        dismissLoadingProgressDialog();
                        ToastUtil.showToast(message + "");
                    }
                });
    }

    /**
     * 展示当前用户在其它设备登录
     */
    private void showLoginInAnotherDeviceDialog(StudentInfo studentInfo) {
        DialogUtil.getInstance().create(this, R.layout.ui_user_login_another_device)
                .show(new AbsAdapter() {
                    @Override
                    public void bindListener(View.OnClickListener onClickListener) {
                        this.bindText(R.id.tvName, studentInfo.getTruthName());
                        this.bindText(R.id.tvInstitutionName, studentInfo.getInstitutionName());
                        ImageView ivHeader = this.findViewById(R.id.ivHeader);
                        if (studentInfo.getPath() != null) {
                            ImageLoader.INSTANCE.setCircleImageViewResource(ivHeader, studentInfo.getPath(), R.drawable.icon_default_header);
                        } else {
                            ivHeader.setImageResource(R.drawable.icon_default_header);
                        }
                        this.bindListener(onClickListener, R.id.tvCancel, R.id.tvConfirm);
                    }

                    @Override
                    public void onClick(View v, DialogUtil dialog) {
                        super.onClick(v, dialog);
                        switch (v.getId()) {
                            case R.id.tvCancel:
                                dialog.dismiss();
                                break;
                            case R.id.tvConfirm:
                                dialog.dismiss();
                                loginForUser(studentInfo, true);
                                break;
                        }
                    }
                });
    }

    /**
     * 这完全是WebService的设计风格，APP能够这样设计吗？搞笑
     */
    private void checkClientId(StudentInfo studentInfo, String token) {
        dismissLoadingProgressDialog();
        if (studentInfo == null) {
            return;
        }

        studentInfo.setIsBeiXiao(STBaseConstants.isBeixiao);
        SDKConfig.INSTANCE.updateInfo(studentInfo, token);
        SchemeDispatcher.jumpPage(this, "axx://main");
        finish();
    }

    /**
     * view的切换动画
     *
     * @param showView
     * @param hideView
     */
    private void showOrHideView(final LoginCardView showView, final LoginCardView hideView) {
        showView.setVisibility(View.VISIBLE);
        hideView.setVisibility(View.GONE);
    }

    /**
     * 获取登录类型
     */
    public String getLastLoginType() {
        return SharedPreferenceUtil.getStringValueFromSP("userInfo", "loginType", LoginCardView.CODE_TYPE);
    }

    /**
     * 存储登录类型
     */
    public void setLoginType() {
        SharedPreferenceUtil.setStringDataIntoSP("userInfo", "loginType", mLoginType);
    }

    /**
     * 用户手机号
     *
     * @return
     */
    public String getPhone() {
        return SharedPreferenceUtil.getStringValueFromSP("userInfo", "userPhone", "");
    }

    /**
     * 设置手机号
     *
     * @param phone
     */
    public void setPhone(String phone) {
        SharedPreferenceUtil.setStringDataIntoSP("userInfo", "userPhone", phone);
    }

    @Override
    public synchronized void kickOut(String message) {
    }

    /**
     * 闪验 预取号
     */
    private void getPhoneInfo() {
        OneKeyLoginManager.getInstance().getPhoneInfo((code, result) ->
                LogUtil.i("闪验SDK 预取号: code==" + code + "result==" + result)
        );
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == PERMISSION_REQUEST) {
            if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                checkUpdate();
            } else if (perms.contains(Manifest.permission.READ_PHONE_STATE)) {
                getPhoneInfo();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PERMISSION_REQUEST) {
            if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                mPermissionDialog = builder.create();
                mPermissionDialog.setTitle("温馨提示");
                mPermissionDialog.setMessage("爱学习缺少存储权限将无法更新新版应用!\n请在手机设置--权限管理中开启权限!");
                mPermissionDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPermissionDialog.dismiss();
                    }
                });
                mPermissionDialog.show();
                mPermissionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
            } else if (perms.contains(Manifest.permission.READ_PHONE_STATE)) {
                ToastUtil.showToast("爱学习缺少获取手机状态权限可能会导致一键登录无法使用");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_USER_PAGE_REQUEST && resultCode == RESULT_OK) {
            SchemeDispatcher.jumpPage(this, "axx://main");
            finish();
        }
    }

    @Override
    protected void onRightClick(View v) {
        super.onRightClick(v);
    }

    /**
     * 字体的大小不随系统的改变而改变
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1.0f;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }

    @Override
    protected void setStatusBar() {
        this.setStatusBar(ContextCompat.getColor(mContext, R.color.white), 0);
        StatusBarUtil.setLightMode(this);
    }

    @Override
    public void onBackPressed() {
        existDialog();
    }

    @Override
    protected void onDestroy() {
        if (mPermissionDialog != null && mPermissionDialog.isShowing()) {
            mPermissionDialog.dismiss();
        }
        super.onDestroy();
    }
}
