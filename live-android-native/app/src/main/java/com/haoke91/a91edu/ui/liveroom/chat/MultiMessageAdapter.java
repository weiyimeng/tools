package com.haoke91.a91edu.ui.liveroom.chat;

import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/7/2 下午4:16
 * 修改人：weiyimeng
 * 修改时间：2018/7/2 下午4:16
 * 修改备注：
 */
public class MultiMessageAdapter extends MultiTypeAdapter {
//    private final HashSet<Object> timedItems;
    
    public MultiMessageAdapter(@NonNull List<?> items) {
        super(items);
//        timedItems = new HashSet<>();
    }
    
//    private void setShowTime(Message message, boolean show) {
//        if (show) {
//            timedItems.add(message.getId());
//        } else {
//            timedItems.remove(message.getId());
//        }
//    }
//
//    public Map<String, User> getUserContainer() {
//        return ImManager.getInstance().getUserContainer();
//    }
}
