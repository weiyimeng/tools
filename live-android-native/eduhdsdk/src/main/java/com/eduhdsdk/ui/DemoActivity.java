//package com.eduhdsdk.ui;
//
//import android.app.Activity;
//import android.content.pm.ActivityInfo;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.view.WindowManager;
//
//import com.eduhdsdk.R;
//
///**
// * 项目名称：talkplus
// * 类描述：
// * 创建人：shichengxiang
// * 创建时间：2018/11/22 11:28
// */
//public class DemoActivity extends AppCompatActivity {
//    TKPlayer mTKPlayer;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        setContentView(R.layout.activity_demo);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        mTKPlayer = (TKPlayer) findViewById(R.id.tkPlayer);
//        mTKPlayer.onCreate();
//    }
//
//    @Override
//    protected void onStart(){
//        super.onStart();
//        mTKPlayer.onStart();
//    }
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//        mTKPlayer.onResume();
//    }
//
//    @Override
//    protected void onStop(){
//        mTKPlayer.onStop();
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy(){
//        mTKPlayer.onDestroy();
//        super.onDestroy();
//    }
////
////    @Override
////    public void onBackPressed(){
////        mTKPlayer.showExitDialog();
////    }
//}
