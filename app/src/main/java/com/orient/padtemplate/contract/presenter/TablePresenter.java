package com.orient.padtemplate.contract.presenter;

import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.base.rx.BaseObserver;
import com.orient.padtemplate.contract.view.TableView;
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
public class TablePresenter extends BasePresenter<TableView> {

    @Inject
    TaskRepository taskRepository;

    @Inject
    public TablePresenter() {
    }

    public void loadCells(String tableId) {
        new RxUtils<>(createTableBodyObservable(tableId))
                .execute(mLifecycleProvider, new BaseObserver<TableBody>(mView) {
                    @Override
                    public void onNext(TableBody tableBody) {
                        super.onNext(tableBody);

                        List<Cell> titles = tableBody.titles;
                        List<Cell> contents = tableBody.contents;
                        mView.onLoadCellResult(titles, contents);
                    }
                });
    }

    /**
     * 创建表格的Observable
     */
    private Observable<TableBody> createTableBodyObservable(String tableId) {
        List<Cell> titles = taskRepository.searchCellsByTableId(tableId, true);
        List<Cell> contents = taskRepository.searchCellsByTableId(tableId, false);
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
