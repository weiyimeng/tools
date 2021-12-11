package com.gstudentlib.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gsbaselib.utils.Variables;
import com.gstudentlib.R;
import com.gstudentlib.view.clipimage.ClipImageLayout;

/**
 * 作者：created by 逢二进一 on 2019/9/12 16:11
 * 邮箱：dingyuanzheng@gaosiedu.com
 */
public class CutPhotoActivity extends Activity implements View.OnClickListener {

    private ClipImageLayout mClipImageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Call requestWindowFeature before calling super.onCreate
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(R.layout.activity_cut_photo);
        mClipImageLayout = findViewById(R.id.id_clipImageLayout);
        TextView tvCancel = findViewById(R.id.tv_quxiao);
        TextView tvConfirm = findViewById(R.id.tv_queding);
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_quxiao) {
            this.setResult(0);
            finish();
        } else if (i == R.id.tv_queding) {
            Bitmap bitmap = mClipImageLayout.clip();
            Variables.setBitmap(bitmap);
            this.setResult(RESULT_OK);
            finish();
        }

    }
}
