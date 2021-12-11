package gaosi.com.learn.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gsbaselib.utils.LangUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gaosi.com.learn.R;

/**
 * Created by pingfu on 2018/4/27.
 */
public class UpdatePasswordView extends LinearLayout {
    private EditText edtPass;
    private ImageView ivClearPass;
    private EditText edtPass2;
    private ImageView ivClearPass2;
    private String content;
    private Button btnLogin;

    public UpdatePasswordView(Context context) {
        super(context);
        init();
    }

    public UpdatePasswordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.ui_update_password, this);
        edtPass = findViewById(R.id.edt_pass);
        ivClearPass = findViewById(R.id.iv_clear_pass);
        edtPass2 = findViewById(R.id.edt_pass2);
        ivClearPass2 = findViewById(R.id.iv_clear_pass2);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setClickable(false);

        setEditTextInhibitInputSpaChat(edtPass);
        setEditTextInhibitInputSpaChat(edtPass2);
        edtPass.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !TextUtils.isEmpty(content)) {
                    ivClearPass.setVisibility(VISIBLE);
                } else {
                    ivClearPass.setVisibility(INVISIBLE);
                }
            }
        });

        edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                content = s.toString();
                checkSubmitButtonValid();
                if (!TextUtils.isEmpty(content)) {
                    ivClearPass.setVisibility(VISIBLE);
                } else {
                    ivClearPass.setVisibility(INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivClearPass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPass.setText("");
            }
        });

        edtPass2.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !LangUtil.isEmpty(edtPass2.getText())) {
                    ivClearPass2.setVisibility(VISIBLE);
                } else {
                    ivClearPass2.setVisibility(GONE);
                }
            }
        });

        edtPass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkSubmitButtonValid();
                if (LangUtil.isEmpty(s)) {
                    ivClearPass2.setVisibility(INVISIBLE);
                } else {
                    ivClearPass2.setVisibility(VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivClearPass2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPass2.setText("");
            }
        });
    }

    public String getPassword1() {
        if (LangUtil.isEmpty(edtPass.getText())) {
            return "";
        }

        return edtPass.getText().toString();
    }

    public String getPassword2() {
        if (LangUtil.isEmpty(edtPass2.getText())) {
            return "";
        }

        return edtPass2.getText().toString();
    }

    private void checkSubmitButtonValid() {
        boolean flag = edtPass.getText() != null && edtPass.getText().length() >= 6
                && edtPass2.getText() != null && edtPass2.getText().length() >= 6;

        btnLogin.setClickable(flag);

        Drawable drawable = getResources().getDrawable(R.drawable.app_login_shape_unenable);
        if (flag) {
            drawable = getResources().getDrawable(R.drawable.app_login_shape_enable);
        }
        btnLogin.setBackground(drawable);
    }

    private boolean isValidChar(char c) {
        return (c - 'a' >= 0 && c - 'z' <= 0)
                || (c - '0' >= 0 && c - '9' <= 0)
                || (c - 'A' >= 0 && c - 'Z' <= 0);
    }

    /**
     * 禁止EditText输入空格、特殊符号
     * 设置自定义InputFilter后，xml文件中的maxlength属性失效，需手动添加LengthFilter
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpaChat(EditText editText) {
        InputFilter filter_space = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        InputFilter filter_speChat = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                String speChat = "[`~!@#_$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）— +|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(charSequence.toString());
                if (matcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter_space, filter_speChat, new InputFilter.LengthFilter(16)});
    }
}
