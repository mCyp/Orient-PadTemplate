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
public interface TableView extends BaseView {

    /**
     * 加载单元格结果
     */
    void onLoadCellResult(List<Cell> titles,List<Cell> contents);
}
