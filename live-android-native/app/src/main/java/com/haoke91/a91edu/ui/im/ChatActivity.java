package com.haoke91.a91edu.ui.im;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.ui.BaseActivity;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/6/11 19:06
 */
public class ChatActivity extends BaseActivity {
    
    @Override
    public int getLayout() {
        return R.layout.activity_chat;
    }
    
    @Override
    public void initialize() {
    }
}
