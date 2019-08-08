package com.orient.padtemplate.contract.view;

import com.orient.padtemplate.base.contract.view.BaseView;
import com.orient.padtemplate.core.data.db.Table;

import java.util.List;

/**
 * TableList
 *
 * Author WangJie
 * Created on 2019/8/8.
 */
public interface FlowView extends BaseView {
    void onLoadTableResult(List<Table> result);
}
