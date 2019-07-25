package com.orient.padtemplate.base.contract.presenter;

import android.content.Context;

import com.orient.padtemplate.base.contract.view.BaseView;

import javax.inject.Inject;

/**
 * 基础的Presenter
 *
 * Author WangJie
 * Created on 2019/7/25.
 */
public class BasePresenter<T extends BaseView> {

    protected T mView;

    // 注入的上下文
    @Inject
    protected Context mContext;

    public BasePresenter() {
    }

    public void setView(T view){
        this.mView = view;
    }

    public void removeView(){
        mView.hideLoading();
        if(mView != null)
            mView = null;
    }

}
