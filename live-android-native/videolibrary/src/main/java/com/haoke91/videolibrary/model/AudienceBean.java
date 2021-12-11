package com.haoke91.videolibrary.model;

import android.view.SurfaceView;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/6/21 下午4:08
 * 修改人：weiyimeng
 * 修改时间：2018/6/21 下午4:08
 * 修改备注：
 */
public class AudienceBean {
   
   public int uuid;
  public   SurfaceView surfaceView;
    
    public AudienceBean(int uuid, SurfaceView surfaceView) {
        this.uuid = uuid;
        this.surfaceView = surfaceView;
    }
}
