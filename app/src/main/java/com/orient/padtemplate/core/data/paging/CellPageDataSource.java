package com.orient.padtemplate.core.data.paging;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.orient.padtemplate.core.data.db.Cell;
import com.orient.padtemplate.core.data.repository.TaskRepository;

import java.util.List;

/**
 * 分页的数据源
 * <p>
 * Author WangJie
 * Created on 2019/8/14.
 */
public class CellPageDataSource extends PageKeyedDataSource<Integer, Cell> {

    private TaskRepository mTaskRepository;
    private String mTableId;
    // 一页的数量
    private int singlePageSize;

    public CellPageDataSource(TaskRepository taskRepository, String tableId, int singlePageSize) {
        this.mTaskRepository = taskRepository;
        this.mTableId = tableId;
        this.singlePageSize = singlePageSize;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Cell> callback) {
        int size = params.requestedLoadSize;
        List<Cell> cells = mTaskRepository.searchCellsByTableId(mTableId, size);

        // 计算加载的下一页
        int nextKey = size / singlePageSize + 1;
        callback.onResult(cells, null, nextKey);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Cell> callback) {
        // 正常用不到
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Cell> callback) {
        int startPage = params.key;
        int endPage = params.key + params.requestedLoadSize / singlePageSize;
        List<Cell> cells = mTaskRepository.searchCellByTableInRange(mTableId, startPage, endPage);
        // TODO 这边是否要加一
        callback.onResult(cells, endPage);
    }
}
