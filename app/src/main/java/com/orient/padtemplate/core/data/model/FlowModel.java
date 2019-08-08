package com.orient.padtemplate.core.data.model;

import com.orient.padtemplate.core.data.db.Flow;

import java.util.List;

/**
 * 流程Bean
 *
 * Author WangJie
 * Created on 2019/8/5.
 */
public class FlowModel {
    private String id;
    private String name;
    private List<TableModel> tableModels;

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

    public List<TableModel> getTableModels() {
        return tableModels;
    }

    public void setTableModels(List<TableModel> tableModels) {
        this.tableModels = tableModels;
    }

    // 返回一个流程
    public Flow toFlow(String taskId){
        return new Flow(id,name,taskId);
    }
}
