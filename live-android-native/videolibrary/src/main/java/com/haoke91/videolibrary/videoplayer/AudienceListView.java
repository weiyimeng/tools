package com.haoke91.videolibrary.videoplayer;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.haoke91.baselibrary.recycleview.WrapRecyclerView;
import com.haoke91.videolibrary.MessageClickCallback;
import com.haoke91.videolibrary.R;
import com.haoke91.videolibrary.model.AudienceBean;
import com.haoke91.videolibrary.adapter.AudienceListAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/4 下午2:31
 * 修改人：weiyimeng
 * 修改时间：2018/7/4 下午2:31
 * 修改备注：
 */
public class AudienceListView extends RelativeLayout {
    private AudienceListAdapter adapter;
    protected WrapRecyclerView rv_audience_list;
    private ArrayList<AudienceBean> data;
    private MessageClickCallback mMediaControl;
    private float rawDownY;
    private long lastClickTime;
    
    public AudienceListView(Context context) {
        super(context);
    }
    
    public AudienceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    
    private void initView(Context context) {
        View.inflate(context, R.layout.layout_audience_list, this);
        rv_audience_list = findViewById(R.id.rv_audience_list);
        //   data = new ArrayList();
        
    }
    
    public void setData(ArrayList<AudienceBean> data) {
        this.data = data;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
        rv_audience_list.setLayoutManager(linearLayoutManager);
        adapter = new AudienceListAdapter(getContext());
        rv_audience_list.setAdapter(adapter);
        adapter.setData(data);
        rv_audience_list.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //  handler.sendEmptyMessageDelayed(BaseVideoPlayer.MSG_HIDE_CONTROLLER, BaseVideoPlayer.TIME_SHOW_CONTROLLER);
                    rawDownY = event.getRawY();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (Math.abs((int) (rawDownY - event.getRawY())) < 40) {
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        if (currentTime - lastClickTime > 300) {
                            lastClickTime = currentTime;
                            //  mMediaControl.sendHandlerMessage(0);
                            mMediaControl.onMessViewClick();
                        }
                    }
                    
                }
                
                
                return false;
            }
        });
    }
    
    public void addData() {
        //  adapter.add(new AudienceBean(uid, surfaceV));
        adapter.notifyDataSetChanged();
    }
    
    public void remove(AudienceBean bean) {
        adapter.remove(bean);
    }
    
    public void setMediaControl(MessageClickCallback mediaControl) {
        mMediaControl = mediaControl;
    }
}
