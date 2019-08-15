package com.orient.padtemplate.contract.presenter;

import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.base.rx.BaseObserver;
import com.orient.padtemplate.contract.view.GridTableView;
import com.orient.padtemplate.core.data.db.Cell;
import com.orient.padtemplate.core.data.repository.TaskRepository;
import com.orient.padtemplate.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * 网格布局的逻辑
 * <p>
 * Author WangJie
 * Created on 2019/8/13.
 */
@SuppressWarnings("WeakerAccess")
public class GridTablePresenter extends BasePresenter<GridTableView> {

    @Inject
    TaskRepository taskRepository;

    @Inject
    public GridTablePresenter() {
    }

    /**
     * 第一次加载
     */
    public void initLoad(String tableId) {
        new RxUtils<>(createTableBodyObservable(tableId))
                .execute(mLifecycleProvider, new BaseObserver<TableBody>(mView) {
                    @Override
                    public void onNext(TableBody tableBody) {
                        super.onNext(tableBody);

                        List<Cell> titles = tableBody.titles;
                        List<Cell> contents = tableBody.contents;
                        mView.onInitLoadResult(titles, contents);
                    }
                });
    }

    /**
     * 加载更多的时候
     */
    public void loadMore(String tableId, int startRow, int endRow) {
        new RxUtils<>(taskRepository.searchCellByTableInRangeRx(tableId, startRow, endRow))
                .execute(mLifecycleProvider, new BaseObserver<List<Cell>>(mView) {
                    @Override
                    public void onNext(List<Cell> cells) {
                        super.onNext(cells);

                        mView.onLoadMoreResult(cells);
                    }
                });
    }


    /**
     * 创建表格的Observable
     */
    private Observable<TableBody> createTableBodyObservable(String tableId) {
        List<Cell> titles = taskRepository.searchCellsByTableId(tableId, true);
        List<Cell> contents = taskRepository.searchCellByTableInRange(tableId, 1, 6);
        TableBody tableBody = new TableBody(titles, contents);
        return Observable.just(tableBody);
    }

    static class TableBody {
        List<Cell> titles;
        List<Cell> contents;

        public TableBody(List<Cell> titles, List<Cell> contents) {
            this.titles = titles;
            this.contents = contents;
        }
    }

}
