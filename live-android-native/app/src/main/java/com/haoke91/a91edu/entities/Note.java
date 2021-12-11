package com.haoke91.a91edu.entities;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

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
//@Uid
public class Note {
    @Id
    long id;
    String name;
    //升级
    @Uid(5655777436160536808L)
    String age;
    
    public Note(String name, String age) {
        this.name = name;
        this.age = age;
    }
}
