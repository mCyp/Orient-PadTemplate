package com.orient.padtemplate.contract.view;

import com.orient.padtemplate.base.contract.view.BaseView;
import com.orient.padtemplate.core.data.db.Trouble;

import java.util.List;

/**
 * 时间视图
 */
public interface EventView extends BaseView {
    void onLoadTroubles(List<Trouble> troubles);
}
