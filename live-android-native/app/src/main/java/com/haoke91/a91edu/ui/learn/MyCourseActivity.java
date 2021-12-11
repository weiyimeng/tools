package com.haoke91.a91edu.ui.learn;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.ObjectUtils;
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain;
import com.gaosiedu.scc.sdk.android.api.user.course.list.LiveSccUserCourseListRequest;
import com.gaosiedu.scc.sdk.android.api.user.course.list.LiveSccUserCourseListResponse;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.MyCourseAdapter;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.ui.course.CourseDetailActivity;
import com.haoke91.a91edu.utils.Utils;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.dropdownlayout.ClassifyCourseView;
import com.haoke91.a91edu.widget.dropdownlayout.DropDownLayout;
import com.haoke91.a91edu.widget.dropdownlayout.MenuLayout;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.baselibrary.recycleview.adapter.BaseQuickWithPositionAdapter;
import com.haoke91.baselibrary.views.emptyview.EmptyView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/24 下午4:29
 * 修改人：weiyimeng
 * 修改时间：2018/7/24 下午4:29
 * 修改备注：
 */
public class MyCourseActivity extends BaseActivity implements DropDownFragment.OnItemSelectedClickListener, OnRefreshLoadMoreListener {
    private final int STARTPAGE = 1;
    private DropDownLayout dropDownLayout;
    private ClassifyCourseView view_classify;
    private int mCurrentPage = STARTPAGE, mCurrentCourse, mCurrentSubject, mCurrentTerm;
    
    private SmartRefreshLayout mRefreshLayout;
    private MyCourseAdapter mMyCourseAdapter;
    private EmptyView mEmptyView;
    
    public static void start(Context context) {
        Intent intent = new Intent(context, MyCourseActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    public int getLayout() {
        return R.layout.activit_my_course;
    }
    
    @Override
    public void initialize() {
        mRefreshLayout = id(R.id.refreshLayout);
        mEmptyView = id(R.id.emptyView);
        WrapRecyclerView  rv_course_list = findViewById(R.id.rv_course_list);
        dropDownLayout = findViewById(R.id.dropdown_layout);
        MenuLayout menuLayout = findViewById(R.id.menuLayout);
        view_classify = findViewById(R.id.view_classify);
        List<Fragment> fragments = new ArrayList<>();
        //  fragments.add(GardenFragment.instance());
        fragments.add(DropDownFragment.getInstance(UserManager.getInstance().getCourseType()).setOnItemClickListener(this, 0));
        ArrayList<DictionaryDomain> courseInfo = UserManager.getInstance().getCourseInfo();
        
        fragments.add(DropDownFragment.getInstance(courseInfo).setOnItemClickListener(this, 1));
        fragments.add(DropDownFragment.getInstance(UserManager.getInstance().getTermInfo()).setOnItemClickListener(this, 2));
        menuLayout.setFragmentManager(getSupportFragmentManager());
        menuLayout.bindFragments(fragments);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_course_list.setLayoutManager(linearLayoutManager);
        mMyCourseAdapter = new MyCourseAdapter(this, new ArrayList());
        mMyCourseAdapter.setOnItemClickListener(OnItemClickListener);
        rv_course_list.setAdapter(mMyCourseAdapter);
        view_classify.setText(getString(R.string.all_class), getString(R.string.all_course), getString(R.string.all_data));
        view_classify.setOnClassifyChangeListener(new ClassifyCourseView.ClassifyChangeListener() {
            @Override
            public void onClassifyChange(int position) {
                dropDownLayout.showMenuAt(position);
                
            }
        });
        //        mRefreshLayout.autoRefresh(600);
        mEmptyView.showLoading();
        onRefresh(null);
        menuLayout.setCloseMenuListner(new MenuLayout.CloseMenuListener() {
            @Override
            public void closeMenu() {
                view_classify.resetStatus();
            }
        });
    }
    
    private BaseQuickWithPositionAdapter.OnItemClickListener OnItemClickListener = new BaseQuickWithPositionAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            LiveSccUserCourseListResponse.ListData listData = mMyCourseAdapter.getAll().get(position);
            //            CourseDetailActivity.Companion.start(MyCourseActivity.this, listData.getId());
            String s = listData.getId() + "," + listData.getUserCourseId();
            CourseOrderActivity.Companion.start(MyCourseActivity.this, s);
        }
    };
    
    private void networkForMyCourse(int course, int subject, int term, int page, final boolean isLoadingMore) {
        LiveSccUserCourseListRequest request = new LiveSccUserCourseListRequest();
        request.setUserId(UserManager.getInstance().getUserId() + "");
        request.setSearchStatus(course);
        if (course == 0) {
            request.setSearchSort(1);
        }
        request.setSubject(subject);
        request.setTerm(term);
        request.setPageNum(page);
        request.setPageSize(10);
        Api.getInstance().postScc(request, LiveSccUserCourseListResponse.class, new ResponseCallback<LiveSccUserCourseListResponse>() {
            @Override
            public void onResponse(LiveSccUserCourseListResponse date, boolean isFromCache) {
                mEmptyView.showContent();
                if (date.getData() == null) {
                    return;
                }
                List<LiveSccUserCourseListResponse.ListData> list = date.getData().getList();
                if (list != null && list.size() > 0) {
                    if (!isLoadingMore) {
                        mMyCourseAdapter.setData(list);
                        mRefreshLayout.finishRefresh();
                    } else {
                        mMyCourseAdapter.addAll(list);
                    }
                } else {
                    if (!isLoadingMore) {
                        mEmptyView.showEmpty();
                    }
                }
                if (date.getData().isLastPage()) {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    mRefreshLayout.finishLoadMore();
                }
            }
            
            @Override
            public void onEmpty(LiveSccUserCourseListResponse date, boolean isFromCache) {
                super.onEmpty(date, isFromCache);
                if (!isLoadingMore) {
                    mEmptyView.showEmpty();
                }
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                
            }
            
            @Override
            public void onError() {
                super.onError();
                if (!isLoadingMore) {
                    mEmptyView.showError();
                }
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        }, "");
    }
    
    @Override
    public void onItemSelected(View v, int tag, int position) {
        dropDownLayout.closeMenu();
        DictionaryDomain item = (DictionaryDomain) v.getTag();
        if (ObjectUtils.isEmpty(item)) {
            return;
        }
        switch (tag) {
            case 0:
                mCurrentCourse = Integer.parseInt(item.getDicValue());
                view_classify.setText(item.getDicName(), "", "");
                
                break;
            case 1:
                mCurrentSubject = Integer.parseInt(item.getDicValue());
                view_classify.setText("", item.getDicName(), "");
                
                break;
            case 2:
                mCurrentTerm = Integer.parseInt(item.getDicValue());
                view_classify.setText("", "", item.getDicName());
                
                break;
        }
//        mMyCourseAdapter.setData(null);
        //        mRefreshLayout.autoRefresh(400);
        onRefresh(null);
        //        networkForMyCourse(mCurrentCourse, mCurrentSubject, mCurrentTerm, mCurrentPage, false);
    }
    
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage++;
        networkForMyCourse(mCurrentCourse, mCurrentSubject, mCurrentTerm, mCurrentPage, true);
    }
    
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage = STARTPAGE;
        networkForMyCourse(mCurrentCourse, mCurrentSubject, mCurrentTerm, mCurrentPage, false);
    }
}
