package com.haoke91.a91edu.db;

import android.util.SparseArray;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.Utils;
import com.haoke91.a91edu.App;
import com.haoke91.a91edu.entities.MyObjectBox;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.relation.ToMany;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/5/28 下午6:26
 * 修改人：weiyimeng
 * 修改时间：2018/5/28 下午6:26
 * 修改备注：
 */
public class ObjectBoxUtils {
    private static ObjectBoxUtils instance;
    
    public static ObjectBoxUtils getInstance() {
        if (instance == null) {
            synchronized (ObjectBoxUtils.class) {
                if (instance == null) {
                    instance = new ObjectBoxUtils();
                }
            }
        }
        return instance;
    }
    
    private ObjectBoxUtils() {
    
    }
    
    private SparseArray<BoxStore> boxStores = new SparseArray<>();
    
    public BoxStore getBox(int userId) {
        //  int userId = 111;
        if (ObjectUtils.isEmpty(userId)) {
            for (int i = 0; i < boxStores.size(); i++) {
                BoxStore boxStore = boxStores.get(i);
                boxStore.closeThreadResources();
                // boxStore.close();
            }
            boxStores.clear();
            return null;
        }
        BoxStore boxStore = boxStores.get(userId);
        if (ObjectUtils.isEmpty(boxStore)) {
            BoxStore newBox = MyObjectBox.builder().androidContext(Utils.getApp()).name(String.valueOf(userId)).build();
            boxStores.put(userId, newBox);
            return newBox;
        } else {
            return boxStore;
        }
    }
    
    public <T> Box<T> getDb(int userId, Class<T> clazz) {
        return getBox(userId).boxFor(clazz);
    }
}
