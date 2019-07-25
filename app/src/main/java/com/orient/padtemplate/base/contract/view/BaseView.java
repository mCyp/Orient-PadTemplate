package com.orient.padtemplate.base.contract.view;

/**
 * 基础的视图
 *
 * Author WangJie
 * Created on 2019/7/25.
 */
public interface BaseView {
    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示错误
     *
     * @param msg 错误信息
     */
    void showError(String msg);
}
