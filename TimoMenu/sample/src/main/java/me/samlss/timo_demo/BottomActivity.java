package me.samlss.timo_demo;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import me.samlss.timomenu.TimoMenu;
import me.samlss.timomenu.animation.BombItemAnimation;
import me.samlss.timomenu.animation.FlipItemAnimation;
import me.samlss.timomenu.interfaces.OnTimoItemClickListener;
import me.samlss.timomenu.interfaces.TimoMenuListener;
import me.samlss.timomenu.view.TimoItemView;

/**
 * @author SamLeung
 * @e-mail samlssplus@gmail.com
 * @github https://github.com/samlss
 * @description Demo that the menu is displayed at the bottom
 */
public class BottomActivity extends AppCompatActivity {
    private TimoMenu mTimoMenu;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        MenuHelper.setupToolBarBackAction(this, (Toolbar) findViewById(R.id.toolbar));
        
        init();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mTimoMenu.isShowing()) {
                mTimoMenu.dismissImmediately();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    
    private void init() {
    
    
    }
    
    public void onShow(View view) {
        int itemViewWidth = (getWindow().getWindowManager().getDefaultDisplay().getWidth() - 40) / 5;
    
        mTimoMenu = new TimoMenu.Builder(this)
            .setGravity(Gravity.BOTTOM)
            .setTimoMenuListener(new TimoMenuListener() {
                @Override
                public void onShow() {
                    Toast.makeText(getApplicationContext(), "Show", Toast.LENGTH_SHORT).show();
                }
            
                @Override
                public void onDismiss() {
                    Toast.makeText(getApplicationContext(), "Dismiss", Toast.LENGTH_SHORT).show();
                }
            })
            .setTimoItemClickListener(new OnTimoItemClickListener() {
                @Override
                public void onItemClick(int row, int index, TimoItemView view) {
                    Toast.makeText(getApplicationContext(), String.format("%s click~", getString(MenuHelper.ROW_TEXT[row][index])), Toast.LENGTH_SHORT).show();
                }
            })
        
            .setMenuMargin(new Rect(20, 20, 20, 20))
            .setMenuPadding(new Rect(0, 10, 0, 10))
            .addRow(FlipItemAnimation.create(), MenuHelper.getTopList(itemViewWidth))
            .addRow(FlipItemAnimation.create(), MenuHelper.getBottomList(itemViewWidth))
            .build();
        mTimoMenu.setItemAnimation(0, BombItemAnimation.create());
        mTimoMenu.show();
    }
}
