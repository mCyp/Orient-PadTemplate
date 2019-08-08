package com.orient.padtemplate.core.data.model;

import com.orient.padtemplate.core.data.db.Task;

import java.util.List;

/**
 * 任务Bean
 *
 * Author WangJie
 * Created on 2019/8/5.
 */
public class TaskModel {
    private String id;
    private String name;
    private List<FlowModel> flowModels;

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

    public List<FlowModel> getFlowModels() {
        return flowModels;
    }

    public void setFlowModels(List<FlowModel> flowModels) {
        this.flowModels = flowModels;
    }

    public Task toTask(){
        return new Task(id,name);
    }
}
