package com.haoke91.a91edu.ui.course;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.gaosiedu.live.sdk.android.api.common.dictionary.list.LiveDictionaryListRequest;
import com.gaosiedu.live.sdk.android.api.common.dictionary.list.LiveDictionaryListResponse;
import com.gaosiedu.live.sdk.android.domain.DictionaryDomain;
import com.haoke91.a91edu.R;
import com.haoke91.a91edu.adapter.SearchContentProvider;
import com.haoke91.a91edu.adapter.SearchTittleProvider;
import com.haoke91.a91edu.fragment.BaseFragment;
import com.haoke91.a91edu.net.Api;
import com.haoke91.a91edu.net.ResponseCallback;
import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.lzy.okgo.cache.CacheMode;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/16 上午10:18
 * 修改人：weiyimeng
 * 修改时间：2018/7/16 上午10:18
 * 修改备注：
 */
public class SearchRecommendFragment extends BaseFragment {
    private MultiTypeAdapter adapter;
    private List dates;
    
    @Override
    public int getLayout() {
        return R.layout.fragment_search_recommend;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WrapRecyclerView rv_search_recommend = view.findViewById(R.id.rv_search_recommend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_search_recommend.setLayoutManager(linearLayoutManager);
        dates = new ArrayList();
        adapter = new MultiTypeAdapter(dates);
        
        
        LiveDictionaryListRequest request = new LiveDictionaryListRequest();
        request.setDicType("kws");
        
        Api.getInstance().post(request, LiveDictionaryListResponse.class, new ResponseCallback<LiveDictionaryListResponse>() {
                @Override
                public void onResponse(LiveDictionaryListResponse date, boolean isFromCache) {
                    if (!ObjectUtils.isEmpty(date.getData().getList())) {
                        ArrayList<String> strings = new ArrayList<>();
                        for (DictionaryDomain item : date.getData().getList()) {
                            strings.add(item.getDicName());
                        }
                        dates.add(0, "热门搜索");
                        dates.add(1, strings);
                        adapter.notifyDataSetChanged();
                    }
                    
                    //                    CacheDiskUtils.getInstance().put(GlobalConfig.COURSE_LIST, date);
                }
                
                @Override
                public void onError() {
                }
            }, CacheMode.IF_NONE_CACHE_REQUEST, ConvertUtils.timeSpan2Millis(1, TimeConstants.DAY)
            , "");
        
        
        //        ArrayList<String> strings = new ArrayList<>();
        //        strings.add("语文");
        //        strings.add("语文");
        //        strings.add("语文11111");
        //        strings.add("语22222文");
        //        strings.add("语22222文");
        //        strings.add("语2222233333333文");
        //        strings.add("语文");
        // Set<String> stringSet = SPUtils.getInstance().getStringSet(SearchActivity.search_history);
        ArrayList<String> history = (ArrayList<String>) CacheDiskUtils.getInstance().getSerializable(SearchActivity.search_history);
        
        if (!ObjectUtils.isEmpty(history)) {
            dates.add("历史搜索");
            dates.add(history);
        }
        rv_search_recommend.setAdapter(adapter);
        adapter.register(String.class, new SearchTittleProvider() {
            @Override
            public void onDelete(int position) {
                Logger.e("position==" + position);
                position++;
                if (position < dates.size()) {
                    dates.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, dates.size());
                }
            }
        });
        adapter.register(ArrayList.class, new SearchContentProvider(getActivity()) {
            @Override
            public void onTagClick(View view, int position, String tag) {
                onTagClickListener.onTagClick(view, position, tag);
            }
        });
        
    }
    
    
    public void setOnTagClickListener(SearchRecommendFragment.onTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }
    
    private onTagClickListener onTagClickListener;
    
    public interface onTagClickListener {
        void onTagClick(View view, int position, String tag);
    }
}
