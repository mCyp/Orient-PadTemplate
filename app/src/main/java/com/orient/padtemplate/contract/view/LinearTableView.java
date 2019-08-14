package com.orient.padtemplate.contract.view;

import android.arch.paging.PagedList;

import com.orient.padtemplate.base.contract.view.BaseView;
import com.orient.padtemplate.core.data.db.Cell;

/**
 * Author WangJie
 * Created on 2019/8/14.
 */
public interface LinearTableView extends BaseView {

    /**
     * 加载回调的结果
     */
    void onLoadCellsResult(PagedList<Cell> cells);
}
