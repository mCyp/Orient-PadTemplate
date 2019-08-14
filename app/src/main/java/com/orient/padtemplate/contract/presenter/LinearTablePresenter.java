package com.orient.padtemplate.contract.presenter;

import android.arch.paging.PagedList;
import android.arch.paging.RxPagedListBuilder;

import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.base.rx.BaseObserver;
import com.orient.padtemplate.contract.view.LinearTableView;
import com.orient.padtemplate.core.data.db.Cell;
import com.orient.padtemplate.core.data.paging.CellDataSourceFactory;
import com.orient.padtemplate.core.data.repository.TaskRepository;
import com.orient.padtemplate.utils.RxUtils;

import javax.inject.Inject;

/**
 * 线性表格的逻辑
 * <p>
 * Author WangJie
 * Created on 2019/8/14.
 */
public class LinearTablePresenter extends BasePresenter<LinearTableView> {

    @Inject
    TaskRepository taskRepository;

    @Inject
    public LinearTablePresenter() {
    }

    public void loadCells(String tableId) {
        // 计算列的数量
        int size = (int) taskRepository.countTableCol(tableId);
        if (size == 0)
            return;

        new RxUtils<>(new RxPagedListBuilder<>(new CellDataSourceFactory(taskRepository, tableId, size)
                , new PagedList.Config.Builder()
                .setPageSize(size * 2)
                .setInitialLoadSizeHint(size * 4)
                .setEnablePlaceholders(false)
                .build())
                .buildObservable()
        ).execute(mLifecycleProvider,
                new BaseObserver<PagedList<Cell>>(mView) {
                    @Override
                    public void onNext(PagedList<Cell> cells) {
                        super.onNext(cells);

                        mView.onLoadCellsResult(cells);
                    }
                });
    }
}
