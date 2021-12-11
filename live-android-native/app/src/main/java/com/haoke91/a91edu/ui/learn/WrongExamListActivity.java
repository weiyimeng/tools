package com.haoke91.a91edu.ui.learn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.list.LiveSccUserWrongQuestionListRequest;
import com.gaosiedu.scc.sdk.android.api.user.wrongquestions.list.LiveSccUserWrongQuestionListResponse;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.ChoiceExamProvider;
import com.haoke91.a91edu.adapter.MultiExamAdapter;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.a91edu.ui.BaseActivity;
import com.haoke91.a91edu.utils.manager.UserManager;
import com.haoke91.a91edu.widget.dropdownlayout.MaskView;
import com.haoke91.baselibrary.recycleview.HorizontalDividerItemDecoration;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.baselibrary.views.emptyview.EmptyView;
import com.haoke91.baselibrary.views.popwindow.BasePopup;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.drakeet.multitype.Linker;

/**
 * 项目名称：91HaoKe_Android
 * 类描述：
 * 创建人：shichengxiang
 * 创建时间：2018/8/3 17:35
 */
public class WrongExamListActivity extends BaseActivity implements OnRefreshLoadMoreListener {
    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRv_examList;
    private MultiExamAdapter mAdapter;
    private EmptyView mEmptyView;
    private CourseListPop mCourseListPop;
    private MaskView mMaskView;
    private TextView mFilterSubjectBtn, mFilterExamTypeBtn;
    private boolean isSubjectFilter = true;
    private int currentPage = 1, currentSubject = 0, currentType = 0;
    //    private List<LiveSccUserWrongQuestionListResponse.ListData> mListData;
    private List<String> mSubjectArrays = Arrays.asList("语文", "数学", "英语", "物理", "化学", "生物", "历史", "地理", "政治", "科学", "升学");//1开始
    private List<String> mExamTypeArrays = Arrays.asList("全部错题", "收藏错题", "已订正", "待订正");
    private final int INITSTART = 1;
    
    @Override
    public int getLayout(){
        return R.layout.activity_wrongexam;
    }
    
    @Override
    protected void onResume(){
        super.onResume();
        mRefreshLayout.autoRefresh(200);
    }
    
    @Override
    public void initialize(){
        Intent intent = getIntent();
        int defualtSubjectId = 0;
        if (intent.hasExtra("subject")){
            defualtSubjectId = intent.getIntExtra("subject", 1);
            defualtSubjectId--;
        }
        mRefreshLayout = id(R.id.refreshLayout);
        mRv_examList = findViewById(R.id.rv_examList);
        mEmptyView = id(R.id.emptyView);
        mFilterSubjectBtn = id(R.id.filterSubjectBtn);
        mFilterExamTypeBtn = id(R.id.filterExamTypeBtn);
        mRv_examList.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayout.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.line_divider));
        mRv_examList.addItemDecoration(dividerItemDecoration);
        //        List<LiveSccUserWrongQuestionListResponse.ListData> datas = new ArrayList<>();
        mAdapter = new MultiExamAdapter(new ArrayList<LiveSccUserWrongQuestionListResponse.ListData>());
        mRv_examList.setAdapter(mAdapter);
        mAdapter.register(LiveSccUserWrongQuestionListResponse.ListData.class)
            .to(new ChoiceExamProvider())
            .withLinker(new Linker<LiveSccUserWrongQuestionListResponse.ListData>() {
                @Override
                public int index(int position, @NonNull LiveSccUserWrongQuestionListResponse.ListData listData){
                    return 0;
                }
            });
        //        mEmptyView.showLoading();
        mFilterSubjectBtn.setText(mSubjectArrays.get(defualtSubjectId));
        mFilterExamTypeBtn.setText(mExamTypeArrays.get(0));
        currentSubject = defualtSubjectId;
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
//        mRefreshLayout.autoRefresh(200);
    }
    
    public void clickEvent(View view){
        int id = view.getId();
        if (id == R.id.backBtn){
            onBackPressed();
        }
        switch (id) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.filterSubjectBtn:
                final FrameLayout container = findViewById(android.R.id.content);
                if (mMaskView == null){
                    mMaskView = new MaskView(this);
                }
                if (ObjectUtils.isEmpty(mCourseListPop)){
                    mCourseListPop = new CourseListPop()
                        .setContext(this)
                        .setFocusAndOutsideEnable(true)
                        .setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss(){
                                container.removeView(mMaskView);
                                
                            }
                        })
                        .apply();
                }
                
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.topMargin = 400;
                mMaskView.setLayoutParams(layoutParams);
                
                if (!mCourseListPop.isShowing()){
                    container.addView(mMaskView);
                }
                mCourseListPop.showAsDropDown(mFilterSubjectBtn, 0, 0);
                //                mFilterSubjectBtn.setText("");
                mCourseListPop.setData(mSubjectArrays);
                isSubjectFilter = true;
                break;
            case R.id.filterExamTypeBtn:
                final FrameLayout container2 = findViewById(android.R.id.content);
                if (mMaskView == null){
                    mMaskView = new MaskView(this);
                }
                if (ObjectUtils.isEmpty(mCourseListPop)){
                    mCourseListPop = new CourseListPop()
                        .setContext(this)
                        .setFocusAndOutsideEnable(true)
                        .setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss(){
                                container2.removeView(mMaskView);
                                
                            }
                        })
                        .apply();
                }
                
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.topMargin = 400;
                mMaskView.setLayoutParams(params);
                
                if (!mCourseListPop.isShowing()){
                    container2.addView(mMaskView);
                }
                mCourseListPop.showAsDropDown(mFilterSubjectBtn, 0, 0);
                mCourseListPop.setData(mExamTypeArrays);
                isSubjectFilter = false;
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout){
        currentPage++;
        networkForExamList(currentSubject, currentType, currentPage, true);
    }
    
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout){
        currentPage = INITSTART;
        mAdapter.setItems(null);
        mRefreshLayout.setEnableLoadMore(true);
        networkForExamList(currentSubject, currentType, currentPage, false);
    }
    
    public class CourseListPop extends BasePopup<CourseListPop> {
        List<String> data = new ArrayList<>();
        RecyclerView.Adapter mAdapter;
        
        public void setData(List<String> data){
            if (data == null || data.size() == 0){
                return;
            }
            this.data = data;
            mAdapter.notifyDataSetChanged();
        }
        
        @Override
        protected void initAttributes(){
            setContentView(R.layout.layout_list);
            //            View content = LayoutInflater.from(mContext).inflate(R.layout.layout_list, null);
            //            setContentView(content, -1, 1000);
            //            setHeight(DensityUtil.dip2px(mContext, 400));
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setFocusAndOutsideEnable(true);
            setBackgroundDimEnable(false);
            setAnimationStyle(R.style.TopPopAnim);
        }
        
        @Override
        protected void initViews(View view){
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mAdapter = new RecyclerView.Adapter<Holder>() {
                @NonNull
                @Override
                public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                    View view = LayoutInflater.from(mContext).inflate(R.layout.item_text, null);
                    return new Holder(view);
                }
                
                @Override
                public void onBindViewHolder(@NonNull Holder holder, final int position){
                    holder.mTextView.setText(data.get(position));
                    holder.mTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v){
                            if (isSubjectFilter){
                                mFilterSubjectBtn.setText(data.get(position));
                                currentSubject = position;
                            } else{
                                mFilterExamTypeBtn.setText(data.get(position));
                                currentType = position;
                            }
                            networkForExamList(currentSubject, currentType, currentPage, true);
                            dismiss();
                        }
                    });
                }
                
                @Override
                public int getItemCount(){
                    return data.size();
                }
            };
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).color(
                mContext.getResources().getColor(R.color.FBFBFB))
                .size((int) mContext.getResources().getDimension(R.dimen.dp_2))
                .build());
            recyclerView.setAdapter(mAdapter);
        }
        
        class Holder extends RecyclerView.ViewHolder {
            
            public TextView mTextView;
            
            public Holder(View itemView){
                super(itemView);
                mTextView = itemView.findViewById(R.id.text);
            }
        }
    }
    
    private void networkForExamList(int subjectId, int type, int currentPage, final boolean isMore){
        LiveSccUserWrongQuestionListRequest request = new LiveSccUserWrongQuestionListRequest();
        request.setUserId(UserManager.getInstance().getUserId()+"");
        request.setSubjectId(subjectId + 1);
        request.setWrongType(type);
        request.setPageNum(currentPage);
        request.setPageSize(10);
        Api.getInstance().postScc(request, LiveSccUserWrongQuestionListResponse.class, new ResponseCallback<LiveSccUserWrongQuestionListResponse>() {
            @Override
            public void onResponse(LiveSccUserWrongQuestionListResponse date, boolean isFromCache){
                if (date.getData() != null && date.getData().getList() != null){
                    if (date.getData().getList().size() == 0){
                        mEmptyView.showEmpty();
                        return;
                    }
                    if (isMore){
                        mAdapter.addItems(date.getData().getList());
                        if (date.getData().isLastPage()){
                            mRefreshLayout.finishLoadMoreWithNoMoreData();
                        } else{
                            mRefreshLayout.finishLoadMore();
                        }
                    } else{
                        mAdapter.setItems(date.getData().getList());
                        mRefreshLayout.finishRefresh();
                        if (date.getData().isLastPage()){
                            mRefreshLayout.finishLoadMoreWithNoMoreData();
                        } else{
                            mRefreshLayout.setNoMoreData(false);
                        }
                    }
                    mEmptyView.showContent();
                } else{
                    mEmptyView.showEmpty();
                }
            }
        }, "");
    }
}
