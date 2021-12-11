package com.haoke91.a91edu.ui.course;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.baselibrary.utils.KeyboardUtils;

import java.util.ArrayList;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/7/12 17:27
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    public static final int history_maxCount = 8;
    
    public static final String search_history = "search_history";
    private EditText mSearchView;
    
    
    public static final void start(Context context, String keyWord) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("keyWord", keyWord);
        context.startActivity(intent);
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_search;
    }
    
    @Override
    public void initialize() {
        mSearchView = findViewById(R.id.searchView);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        mSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //  FragmentManager fm = getFragmentManager();
                    FragmentManager fm = getSupportFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                    }
                }
            }
        });
        mSearchView.setOnTouchListener(OnTouchListener);
        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (ObjectUtils.isEmpty(mSearchView.getText().toString().trim())) {
                        return true;
                    }
                    saveSearchHistory(mSearchView.getText().toString().trim());
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, SearchResultFragment.Companion.newInstance(mSearchView.getText().toString().trim())).addToBackStack(null).commitAllowingStateLoss();
                    mSearchView.clearFocus();
                    clearSearchView();
                    KeyboardUtils.hideSoftInput(mSearchView);
                    return true;
                }
                return false;
            }
        });
        
        
        SearchRecommendFragment searchRecommendFragment = new SearchRecommendFragment();
        searchRecommendFragment.setOnTagClickListener(new SearchRecommendFragment.onTagClickListener() {
            @Override
            public void onTagClick(View view, int position, String tag) {
                
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, SearchResultFragment.Companion.newInstance(tag)).addToBackStack(null).commitAllowingStateLoss();
                mSearchView.clearFocus();
                clearSearchView();
                saveSearchHistory(tag);
                KeyboardUtils.hideSoftInput(mSearchView);
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, searchRecommendFragment).commitAllowingStateLoss();
        if (getIntent().hasExtra("keyWord")) {
            saveSearchHistory(getIntent().getStringExtra("keyWord"));
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, SearchResultFragment.Companion.newInstance(getIntent().getStringExtra("keyWord"))).addToBackStack(null).commitAllowingStateLoss();
            mSearchView.clearFocus();
            KeyboardUtils.hideSoftInput(mSearchView);
            
        }
    }
    
    private View.OnTouchListener OnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    Drawable drawableRight = mSearchView.getCompoundDrawables()[2];
                    if (drawableRight != null && event.getRawX() + 20 >= (mSearchView.getRight() - drawableRight.getBounds().width())) {
                        clearSearchView();
                        return true;
                    }
                    Drawable drawableLeft = mSearchView.getCompoundDrawables()[0];
                    if (drawableLeft != null && event.getRawX() <= (mSearchView.getLeft() + drawableLeft.getBounds().width())) {
                        if (ObjectUtils.isEmpty(mSearchView.getText().toString().trim())) {
                            return true;
                        }
                        
                        saveSearchHistory(mSearchView.getText().toString().trim());
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, SearchResultFragment.Companion.newInstance(mSearchView.getText().toString().trim())).addToBackStack(null).commitAllowingStateLoss();
                        mSearchView.clearFocus();
                        clearSearchView();
                        KeyboardUtils.hideSoftInput(mSearchView);
                        return true;
                    }
                    break;
            }
            return false;
        }
    };
    
    private void saveSearchHistory(String text) {
        ArrayList<String> history = (ArrayList<String>) CacheDiskUtils.getInstance().getSerializable(search_history);
        if (history == null) {
            history = new ArrayList<>();
        }
        if (!ObjectUtils.isEmpty(text) && !history.contains(text)) {
            if (history.size() == history_maxCount) {
                history.set(0, text);
            } else {
                history.add(text);
            }
        }
        CacheDiskUtils.getInstance().put(search_history, history);
    }
    
    private void clearSearchView() {
        mSearchView.setText("");
    }
    
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
            onBackPressed();
        }
        
    }
    
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            setResult(RESULT_CANCELED);
            super.onBackPressed();
        }
    }
}
