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

import javax.inject.Inject;

import butterknife.BindView;

/**
 * create an instance of this fragment.
 */
public class TestFragment extends BaseMvpFragment<TestPresenter>
        implements TestView {

    @BindView(R.id.tv_content)
    TextView mContent;

  /*  @Inject
    User user;*/

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initData() {
        super.initData();

        //mPresenter.test();
        //mContent.setText("User：" + user.getName() + "，account：" + user.getAccount());
    }

    @Override
    public void onTestResult(User user) {
        mContent.setText("User：" + user.getName() + "，account：" + user.getAccount());
    }
}
