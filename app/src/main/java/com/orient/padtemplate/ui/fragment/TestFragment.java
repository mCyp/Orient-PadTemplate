package com.orient.padtemplate.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.fragment.BaseMvpFragment;
import com.orient.padtemplate.contract.presenter.TestPresenter;
import com.orient.padtemplate.contract.view.TestView;
import com.orient.padtemplate.core.data.db.User;
import com.orient.padtemplate.di.scope.Wang;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;

/**
 * create an instance of this fragment.
 */
public class TestFragment extends BaseMvpFragment<TestPresenter>
        implements TestView {

    @BindView(R.id.tv_content)
    TextView mContent;

    @Inject
    User user;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }



    @Override
    protected void initData() {
        super.initData();
        AndroidSupportInjection.inject(this);
        //mPresenter.test();
        mContent.setText("Wang：" + user.getName() + "，account：" + user.getAccount());
    }

    @Override
    public void onTestResult(User user) {
        mContent.setText("Wang：" + user.getName() + "，account：" + user.getAccount());
    }
}
