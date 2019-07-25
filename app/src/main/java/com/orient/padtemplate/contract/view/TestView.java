package com.orient.padtemplate.contract.view;

import com.orient.padtemplate.base.contract.view.BaseView;
import com.orient.padtemplate.core.data.db.User;

/**
 * Author WangJie
 * Created on 2019/7/25.
 */
public interface TestView extends BaseView {
    void onTestResult(User user);
}
