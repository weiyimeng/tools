package com.haoke91.baselibrary.recycleview.itemtouch;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/5/31 下午2:00
 * 修改人：weiyimeng
 * 修改时间：2018/5/31 下午2:00
 * 修改备注：
 */
public interface ItemTouchHelperAdapter {
    //数据交换
    void onItemMove(int fromPosition, int toPosition);

    //数据删除
    void onItemDissmiss(int position);
}
