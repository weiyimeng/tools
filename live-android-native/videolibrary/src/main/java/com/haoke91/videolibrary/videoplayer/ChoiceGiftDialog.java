package com.haoke91.videolibrary.videoplayer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.videolibrary.R;
import com.haoke91.videolibrary.adapter.GiftListAdapter;

import java.util.ArrayList;


/**
 * Created by wdy on 2017/9/9.
 */

public class ChoiceGiftDialog extends DialogFragment {
    private static final String TAG = "ChoiceSexDialog";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        View view = View.inflate(getActivity(), R.layout.dialog_gift, null);
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
        WrapRecyclerView rv_gift_list = view.findViewById(R.id.rv_gift_list);
        GiftListAdapter adapter = new GiftListAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
        rv_gift_list.setLayoutManager(linearLayoutManager);
        rv_gift_list.setAdapter(adapter);
        ArrayList<String> objects = new ArrayList<>();
        objects.add("1");
        objects.add("1");
        objects.add("1");
        objects.add("1");
        objects.add("1");
        objects.add("1");
        objects.add("1");
        objects.add("1");
        objects.add("1");
        objects.add("1");
        objects.add("1");
        objects.add("1");
        adapter.addAll(objects);
    }
    
    
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            
        }
    };
    
    public OnSexSelectListener getOnSexSelectListener() {
        return onSexSelectListener;
    }
    
    public void setOnSexSelectListener(OnSexSelectListener onSexSelectListener) {
        if (this.onSexSelectListener != null) {
            return;
        }
        this.onSexSelectListener = onSexSelectListener;
    }
    
    private OnSexSelectListener onSexSelectListener;
    
    public interface OnSexSelectListener {
        void onSexSelect(String sex);
    }
    
    public static ChoiceGiftDialog showDialog(AppCompatActivity appCompatActivity) {
        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
        ChoiceGiftDialog bottomDialogFragment =
            (ChoiceGiftDialog) fragmentManager.findFragmentByTag(TAG);
        if (null == bottomDialogFragment) {
            bottomDialogFragment = new ChoiceGiftDialog();
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
