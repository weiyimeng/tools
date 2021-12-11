package com.haoke91.a91edu.ui.course;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.ObjectUtils;
import com.gaosiedu.live.sdk.android.api.course.list.LiveCourseListRequest;
import com.gaosiedu.live.sdk.android.api.course.list.LiveCourseListResponse;
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.SpecialClassAdapter;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.ui.learn.DropDownFragment;
import com.haoke91.a91edu.ui.learn.GardenFragment;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.dropdownlayout.ClassifyCourseView;
import com.haoke91.a91edu.widget.dropdownlayout.DropDownLayout;
import com.haoke91.a91edu.widget.dropdownlayout.MenuLayout;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.baselibrary.views.emptyview.EmptyView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：91haoke
 * 类描述： 公开课
 * 创建人：weiyimeng
 * 创建时间：2018/8/3 上午11:47
 * 修改人：weiyimeng
 * 修改时间：2018/8/3 上午11:47
 * 修改备注：
 */
public class PublicClassActivity extends BaseActivity {
    private DropDownLayout dropDownLayout;
    private ClassifyCourseView view_classify;
    private SpecialClassAdapter searchResultAdapter;
    private EmptyView emptyView;
    
    public static void start(Context context) {
        Intent intent = new Intent(context, PublicClassActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_special_class;
    }
    
    @Override
    public void initialize() {
        SmartRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        emptyView = findViewById(R.id.emptyView);
        WrapRecyclerView rv_course_list = findViewById(R.id.rv_course_list);
        dropDownLayout = findViewById(R.id.dropdown_layout);
        MenuLayout menuLayout = findViewById(R.id.menuLayout);
        view_classify = findViewById(R.id.view_classify);
        
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(GardenFragment.instance().setOnItemClickListener(OnItemClickListener, 3));
        ArrayList<DictionaryDomain> courseInfo = UserManager.getInstance().getCourseInfo();
        fragments.add(DropDownFragment.getInstance(courseInfo).setOnItemClickListener(OnItemClickListener, 0));
        fragments.add(DropDownFragment.getInstance(UserManager.getInstance().getTermInfo()).setOnItemClickListener(OnItemClickListener, 1));
        menuLayout.setFragmentManager(getSupportFragmentManager());
        menuLayout.bindFragments(fragments);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_course_list.setLayoutManager(linearLayoutManager);
        //        List<String> strings = Arrays.asList("", "", "", "", "");
        searchResultAdapter = new SpecialClassAdapter(this, null);
        rv_course_list.setAdapter(searchResultAdapter);
        view_classify.setText(getString(R.string.all_garden), getString(R.string.all_course), getString(R.string.all_data));
        view_classify.setOnClassifyChangeListener(new ClassifyCourseView.ClassifyChangeListener() {
            @Override
            public void onClassifyChange(int position) {
                dropDownLayout.showMenuAt(position);
                
            }
        });
        refreshLayout.setOnLoadMoreListener(onLoadMoreListener);
        
        getDate();
        menuLayout.setCloseMenuListner(new MenuLayout.CloseMenuListener() {
            @Override
            public void closeMenu() {
                view_classify.resetStatus();
            }
        });
    }
    
    private OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
            num++;
            LiveCourseListRequest request = new LiveCourseListRequest();
            request.setPageNum(num);
            request.setType(String.valueOf(3));
            if (subjectId != 0) {
                request.setSubjectId(subjectId);
            }
            if (grade != 0) {
                request.setGrade(grade);
            }
            if (term != 0) {
                request.setTerm(term);
            }
            Api.getInstance().post(request, LiveCourseListResponse.class, new ResponseCallback<LiveCourseListResponse>() {
                @Override
                public void onResponse(LiveCourseListResponse date, boolean isFromCache) {
                    setCourseDate(date.getData().getList(), true);
                    refreshLayout.finishLoadMore();
                    if (date.getData().isLastPage()) {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                }
                
                @Override
                public void onEmpty(LiveCourseListResponse date, boolean isFromCache) {
                    if (!ObjectUtils.isEmpty(searchResultAdapter.getAll())) {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                }
            }, "");
        }
    };
    private DropDownFragment.OnItemSelectedClickListener OnItemClickListener = new DropDownFragment.OnItemSelectedClickListener() {
        @Override
        public void onItemSelected(View v, int tag, int position) {
            DictionaryDomain item = (DictionaryDomain) v.getTag();
            if (ObjectUtils.isEmpty(item)) {
                return;
            }
            if (tag == 0) {
                subjectId = Integer.parseInt(item.getDicValue());
                view_classify.setText("", item.getDicName(), "");
                
                
            } else if (tag == 1) {
                term = Integer.parseInt(item.getDicValue());
                view_classify.setText("", "", item.getDicName());
                
            } else if (tag == 3) {
                grade = Integer.parseInt(item.getDicValue());
                view_classify.setText(item.getDicName(), "", "");
                
            }
            dropDownLayout.closeMenu();
            getDate();
        }
    };
    
    private int num = 1;
    private int grade;
    private int term;
    private int subjectId;
    
    private void getDate() {
        emptyView.showLoading();
        
        LiveCourseListRequest request = new LiveCourseListRequest();
        request.setPageNum(num);
        if (subjectId != 0) {
            request.setSubjectId(subjectId);
        }
        if (grade != 0) {
            request.setGrade(grade);
        }
        if (term != 0) {
            request.setTerm(term);
        }
        request.setType(String.valueOf(3));
        
        //        request.setPassCourse(0);
        
        //        request.setType(String.valueOf(3));
        Api.getInstance().post(request, LiveCourseListResponse.class, new ResponseCallback<LiveCourseListResponse>() {
            @Override
            public void onResponse(LiveCourseListResponse date, boolean isFromCache) {
                //                String s = new Gson().toJson(date);
                setCourseDate(date.getData().getList(), false);
                emptyView.showContent();
            }
            
            @Override
            public void onEmpty(LiveCourseListResponse date, boolean isFromCache) {
                emptyView.showEmpty();
                
            }
            
            @Override
            public void onError() {
                emptyView.showError();
            }
        }, "");
        
    }
    
    private void setCourseDate(List<LiveCourseListResponse.ListData> date, boolean loadMore) {
        if (loadMore) {
            searchResultAdapter.addAll(date);
        } else {
            searchResultAdapter.setData(date);
        }
    }
}
