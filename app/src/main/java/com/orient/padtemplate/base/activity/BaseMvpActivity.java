package com.orient.padtemplate.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.orient.padtemplate.base.contract.view.BaseView;
import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.widget.placeholder.PlaceHolderView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * 基础的Mvp活动
 * <p>
 * Author WangJie
 * Created on 2019/7/25.
 */
@SuppressWarnings({"unchecked", "unused"})
public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity
        implements BaseView, HasSupportFragmentInjector {


    @Inject
    protected T mPresenter;
    // 占位布局
    protected PlaceHolderView mPlaceHolderView;

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentDispatchingAndroidInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

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

    @Override
    public void showError(String msg) {

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentDispatchingAndroidInjector;
    }

    /**
     * 设置占位布局
     */
    protected void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }


}
