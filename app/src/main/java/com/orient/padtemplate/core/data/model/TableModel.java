package com.orient.padtemplate.core.data.model;

import com.orient.padtemplate.core.data.db.Table;

/**
 * 表格Bean
 *
 * Author WangJie
 * Created on 2019/8/5.
 */
public class TableModel {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Table toTable(String flowId,String userId){
        return new Table(id,name,flowId,userId);
    }
}
