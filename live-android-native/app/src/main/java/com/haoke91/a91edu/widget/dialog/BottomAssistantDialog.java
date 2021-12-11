package com.haoke91.a91edu.widget.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.haoke91.a91edu.GlobalConfig;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.utils.imageloader.GlideUtils;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * Created by wdy on 2017/9/9.
 */

public class BottomAssistantDialog extends DialogFragment {
    private static final String TAG = "BottomAssistantDialog";
    private static String IMGURL = "";
    private static String NAME = "";
    private static String TEACHERWXID = "";
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Dialog dialog = new Dialog(getActivity(), R.style.Assistant_BottomDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        View view = View.inflate(getActivity(), R.layout.dialig_bottom_assistant, null);
        dialog.setContentView(view);
        initView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        //设置没有效果
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        return dialog;
    }
    
    
    private void initView(View view){
        final ImageView imageView = view.findViewById(R.id.iv_assistant_wx);
        GlideUtils.load(getContext(), IMGURL, imageView);
        TextView tv_name = view.findViewById(R.id.tv_assistant_name);
        final TextView tv_teacherWxId = view.findViewById(R.id.tv_teacherWxId);
        tv_name.setText("助教老师  " + NAME);
        tv_teacherWxId.setText(Html.fromHtml("<p>助教微信号：" + TEACHERWXID + "&nbsp;&nbsp;  <span style='color:#979797'>复制</span></p>"));
        tv_teacherWxId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("text", TEACHERWXID);
                cm.setPrimaryClip(data);
                ToastUtils.showShort("复制成功");
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                File file = new File(GlobalConfig.defaultImage + "222.jpg");
                boolean save = ImageUtils.save(ImageUtils.view2Bitmap(imageView), file, Bitmap.CompressFormat.JPEG);
                if (save){
                    ToastUtils.showLong(save ? "二维码保存成功" : "");
                    try {
                        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                            GlobalConfig.defaultImage + "222.jpg", "222.jpg", null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //最后通知图库更新
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//扫描单个文件
                    intent.setData(Uri.fromFile(file));//给图片的绝对路径
                    getActivity().sendBroadcast(intent);
                }
            }
        });
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dismiss();
            }
        });
    }
    
    
    public static BottomAssistantDialog showDialog(AppCompatActivity appCompatActivity, String url, String name, String wxId){
        IMGURL = url;
        NAME = name;
        TEACHERWXID = wxId;
        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
        BottomAssistantDialog bottomDialogFragment =
            (BottomAssistantDialog) fragmentManager.findFragmentByTag(TAG);
        if (null == bottomDialogFragment){
            bottomDialogFragment = new BottomAssistantDialog();
        }
        
        if (!appCompatActivity.isFinishing()
            && null != bottomDialogFragment
            && !bottomDialogFragment.isAdded()){
            fragmentManager.beginTransaction()
                .add(bottomDialogFragment, TAG)
                .commitAllowingStateLoss();
        }
        
        return bottomDialogFragment;
    }
    
}
