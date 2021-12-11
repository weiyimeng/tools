package com.haoke91.a91edu.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.haoke91.a91edu.R;


/**
 * Created by wdy on 2017/9/9.
 */

public class ChoicePicDialog extends DialogFragment {
    private static final String TAG = "ChoiceSexDialog";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialogStyle);
        //   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        View view = View.inflate(getActivity(), R.layout.dialog_choicepic, null);
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
    
    private void initView(View view) {
        TextView tv_camera = view.findViewById(R.id.tv_camera);
        TextView tv_album = view.findViewById(R.id.tv_album);
        tv_album.setOnClickListener(onClickListener);
        tv_camera.setOnClickListener(onClickListener);
        view.findViewById(R.id.tv_cancel).setOnClickListener(onClickListener);
    }
    
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            switch (v.getId()) {
                case R.id.tv_camera:
                    if (onSexSelectListener != null) {
                        onSexSelectListener.onPicSelect(true);
                    }
                    break;
                case R.id.tv_album:
                    if (onSexSelectListener != null) {
                        onSexSelectListener.onPicSelect(false);
                    }
                    break;
                case R.id.tv_cancel:
                    dismiss();
                default:
            }
        }
    };
    
    
    public void setOnSelectListener(OnCamreaSelectListener onSexSelectListener) {
        this.onSexSelectListener = onSexSelectListener;
    }
    
    private OnCamreaSelectListener onSexSelectListener;
    
    public interface OnCamreaSelectListener {
        void onPicSelect(boolean isCamrea);
    }
    
    //防止重复弹出
    public static ChoicePicDialog showDialog(AppCompatActivity appCompatActivity) {
        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
        ChoicePicDialog bottomDialogFragment =
            (ChoicePicDialog) fragmentManager.findFragmentByTag(TAG);
        if (null == bottomDialogFragment) {
            bottomDialogFragment = new ChoicePicDialog();
        }
        
        if (!appCompatActivity.isFinishing()
            && null != bottomDialogFragment
            && !bottomDialogFragment.isAdded()) {
            fragmentManager.beginTransaction()
                .add(bottomDialogFragment, TAG)
                .commitAllowingStateLoss();
        }
        
        return bottomDialogFragment;
    }
    
    
}
