package com.orient.padtemplate.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.base.contract.view.BaseView;
import com.orient.padtemplate.widget.PlaceHolderView;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * MVP模式的Fragment
 * <p>
 * Author WangJie
 * Created on 2019/7/25.
 */
@SuppressWarnings("unchecked")
public abstract class BaseMvpFragment<T extends BasePresenter> extends BaseFragment implements BaseView {

    @Inject
    protected T mPresenter;
    // 占位布局
    protected PlaceHolderView mPlaceHolderView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        AndroidSupportInjection.inject(this);
        if (mPresenter != null) {
            mPresenter.setView(this);
            mPresenter.setLifecycleProvider(this);
        }
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null)
            mPlaceHolderView.triggerLoading();
    }

    @Override
    public void hideLoading() {

    }

    /**
     * 设置占位布局
     */
    protected void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }

    @Override
    public void showError(String msg) {

    }
}
