package com.orient.padtemplate.core.data.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 任务
 *
 * Author WangJie
 * Created on 2019/8/5.
 */
@Entity
public class Task {
    @Id
    private String id;
    private String name; // 任务名称
    @Generated(hash = 1776528014)
    public Task(String id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 733837707)
    public Task() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
