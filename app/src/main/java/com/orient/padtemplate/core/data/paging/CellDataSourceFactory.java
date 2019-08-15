package com.orient.padtemplate.core.data.paging;

import android.arch.paging.DataSource;

import com.orient.padtemplate.core.data.db.Cell;
import com.orient.padtemplate.core.data.repository.TaskRepository;

/**
 * CellPageDataSource的工厂类
 * Author WangJie
 * Created on 2019/8/14.
 */
public class CellDataSourceFactory extends DataSource.Factory<Integer, Cell> {

    private TaskRepository taskRepository;
    private String tableId;
    private int pageSize;

    public CellDataSourceFactory(TaskRepository taskRepository, String tableId, int pageSize) {
        this.taskRepository = taskRepository;
        this.tableId = tableId;
        this.pageSize = pageSize;
    }

    @Override
    public DataSource<Integer, Cell> create() {
        return new CellPageDataSource(taskRepository, tableId, pageSize);
    }
}
