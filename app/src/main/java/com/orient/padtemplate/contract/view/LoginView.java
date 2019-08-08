package com.orient.padtemplate.contract.view;

import com.orient.padtemplate.base.contract.view.BaseView;

/**
 * 登录的View
 *
 * Author WangJie
 * Created on 2019/7/25.
 */
public interface LoginView extends BaseView {
    void onLoginResult(boolean result);

    /**
     * 第一次初始化的结果
     */
    void onFirstInitResult(boolean result);
}
