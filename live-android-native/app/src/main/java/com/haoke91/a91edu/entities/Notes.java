package com.haoke91.a91edu.entities;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * 项目名称：91haoke
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/5/28 下午6:02
 * 修改人：weiyimeng
 * 修改时间：2018/5/28 下午6:02
 * 修改备注：
 */
@Entity
public class Notes {
    @Id
    long id;
    String name;
    
    public Notes(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
