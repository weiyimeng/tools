package com.haoke91.a91edu.ui.course;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.ObjectUtils;
import com.gaosiedu.live.sdk.android.api.course.aggregation.LiveCourseAggregationResponse;
import com.gaosiedu.live.sdk.android.api.course.list.LiveCourseListRequest;
import com.gaosiedu.live.sdk.android.api.course.list.LiveCourseListResponse;
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.SpecialClassAdapter;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.ui.learn.DropDownFragment;
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
 * 类描述： 系列课
 * 创建人：weiyimeng
 * 创建时间：2018/8/3 上午11:47
 * 修改人：weiyimeng
 * 修改时间：2018/8/3 上午11:47
 * 修改备注：
 */
public class SeriesClassActivity extends BaseActivity {
    private DropDownLayout dropDownLayout;
    private ClassifyCourseView view_classify;
    private SmartRefreshLayout refreshLayout;
    private EmptyView empty_layout;
    
    public static void start(Context context, LiveCourseAggregationResponse.ListData courseType) {
        Intent intent = new Intent(context, SeriesClassActivity.class);
        intent.putExtra("courseDate", courseType);
        context.startActivity(intent);
    }
    
    @Override
    public int getLayout() {
        return R.layout.activity_special_class;
    }
    
    
    @Override
    public void initialize() {
        LiveCourseAggregationResponse.ListData courseDate = (LiveCourseAggregationResponse.ListData) getIntent().getSerializableExtra("courseDate");
        grade = courseDate.getGrade();
        term = courseDate.getTerm();
        subjectId = courseDate.getSubjectId();
        refreshLayout = findViewById(R.id.refreshLayout);
        WrapRecyclerView rv_course_list = findViewById(R.id.rv_course_list);
        dropDownLayout = findViewById(R.id.dropdown_layout);
        MenuLayout menuLayout = findViewById(R.id.menuLayout);
        view_classify = findViewById(R.id.view_classify);
        empty_layout = findViewById(R.id.emptyView);
        List<Fragment> fragments = new ArrayList<>();
        ArrayList<DictionaryDomain> courseInfo = UserManager.getInstance().getCourseInfo();
        fragments.add(DropDownFragment.getInstance(courseInfo).setOnItemClickListener(OnItemClickListener, 0));
        fragments.add(DropDownFragment.getInstance(UserManager.getInstance().getTermInfo()).setOnItemClickListener(OnItemClickListener, 1));
        menuLayout.setFragmentManager(getSupportFragmentManager());
        menuLayout.bindFragments(fragments);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_course_list.setLayoutManager(linearLayoutManager);
        searchResultAdapter = new SpecialClassAdapter(this, courseList);
        rv_course_list.setAdapter(searchResultAdapter);
        view_classify.setText(courseDate.getSubjectName(), courseDate.getTermName());
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
    
    private DropDownFragment.OnItemSelectedClickListener OnItemClickListener = new DropDownFragment.OnItemSelectedClickListener() {
        @Override
        public void onItemSelected(View v, int tag, int position) {
            DictionaryDomain item = (DictionaryDomain) v.getTag();
            if (ObjectUtils.isEmpty(item)) {
                return;
            }
            if (tag == 0) {
                subjectId = Integer.parseInt(item.getDicValue());
                view_classify.setText(item.getDicName(), "");
                
                
            } else if (tag == 1) {
                term = Integer.parseInt(item.getDicValue());
                view_classify.setText("", item.getDicName());
                
            }
            dropDownLayout.closeMenu();
            getDate();
        }
    };
    private OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
            num++;
            LiveCourseListRequest request = new LiveCourseListRequest();
            request.setPageNum(num);
            request.setSubjectId(subjectId);
            request.setPassCourse(1);
            request.setGrade(grade);
            request.setTerm(term);
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
                
                @Override
                public void onError() {
                    empty_layout.showError();
                }
            }, "");
        }
    };
    
    private int num = 1;
    private int grade;
    private int term;
    private int subjectId;
    
    private List<LiveCourseListResponse.ListData> courseList = new ArrayList<>();
    private SpecialClassAdapter searchResultAdapter;
    
    
    private void getDate() {
        empty_layout.showLoading();
        LiveCourseListRequest request = new LiveCourseListRequest();
        request.setPageNum(num);
        request.setSubjectId(subjectId);
        request.setGrade(grade);
        request.setPassCourse(1);
        request.setTerm(term);
        Api.getInstance().post(request, LiveCourseListResponse.class, new ResponseCallback<LiveCourseListResponse>() {
            @Override
            public void onResponse(LiveCourseListResponse date, boolean isFromCache) {
                empty_layout.showContent();
                //                String s = new Gson().toJson(date);
                setCourseDate(date.getData().getList(), false);
                if (date.getData().isLastPage()) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
            
            @Override
            public void onEmpty(LiveCourseListResponse date, boolean isFromCache) {
                empty_layout.showEmpty();
            }
            
            @Override
            public void onError() {
                empty_layout.showError();
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
