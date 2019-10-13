package com.orient.padtemplate.contract.view;


import com.orient.padtemplate.base.contract.view.BaseView;

/**
 * Author WangJie
 * Created on 2019/2/18.
 */
public interface AddTroubleView extends BaseView {

    /**
     * 添加结果
     * @param result 失败或者成功
     */
    void onAddResult(boolean result);
}
