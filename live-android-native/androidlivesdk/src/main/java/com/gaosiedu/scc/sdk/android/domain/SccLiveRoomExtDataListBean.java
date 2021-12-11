/*
 * Copyright (c) 2016,gaosiedu.com
 */
package com.gaosiedu.scc.sdk.android.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author duankaiyang
 * @describe
 * @date 2018/9/11 9:48
 * @since 2.1.0
 */
public class SccLiveRoomExtDataListBean implements Serializable {
  List<SccLiveRoomExtdataConfigWithBLOBs> list;

  public List<SccLiveRoomExtdataConfigWithBLOBs> getList() {
    return list;
  }

  public void setList(List<SccLiveRoomExtdataConfigWithBLOBs> list) {
    this.list = list;
  }
}
