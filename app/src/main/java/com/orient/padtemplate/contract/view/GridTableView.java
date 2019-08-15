package com.orient.padtemplate.contract.view;

import com.orient.padtemplate.base.contract.view.BaseView;
import com.orient.padtemplate.core.data.db.Cell;

import java.util.List;

/**
 * GridTable的视图约束
 *
 * Author WangJie
 * Created on 2019/8/13.
 */
public interface GridTableView extends BaseView {

    /**
     * 加载单元格结果
     */
    void onInitLoadResult(List<Cell> titles, List<Cell> contents);

    /**
     * 加载更多的结果
     */
    void onLoadMoreResult(List<Cell> contents);
}
