package com.haoke91.a91edu.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：MyHaoke1
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/6/4 15:25
 */
public class TabAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private List<T> mFragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    
    public TabAdapter(FragmentManager fm, List<T> _fragments) {
        super(fm);
        this.mFragments = _fragments;
    }
    
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }
    
    public void addFragment(T fragment, String title) {
        mFragments.add(fragment);
        titles.add(title);
    }
    
    @Override
    public Fragment getItem(int position) {
        //        Fragment fragment = null;
        //        if (position < mFragments.size()) {
        //            fragment = mFragments.get(position);
        //        }
        //        if (fragment == null) {
        //            switch (position) {
        //                case 0:
        //                    fragment = new HomeFragment();
        //                    break;
        //                case 1:
        //                    fragment = new StudyFragment();
        //                    break;
        //                case 2:
        //                    fragment = new FoundFragment();
        //                    break;
        //                case 3:
        //                    fragment = new MineFragment();
        //                    break;
        //            }
        //            mFragments.add(position, fragment);
        //        }
        return mFragments.get(position);
    }
    
    public List<T> getmFragments() {
        return mFragments;
    }
    
    @Override
    public int getCount() {
        return mFragments.size();
    }
    
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position < titles.size()) {
            return titles.get(position);
        }
        return "";
    }
}
