package com.orient.padtemplate.ui.fragment;

import android.widget.TextView;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.fragment.BaseMvpFragment;
import com.orient.padtemplate.contract.presenter.TestPresenter;
import com.orient.padtemplate.contract.view.TestView;
import com.orient.padtemplate.core.data.db.User;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.support.AndroidSupportInjection;

/**
 * create an instance of this fragment.
 */
public class TestFragment extends BaseMvpFragment<TestPresenter>
        implements TestView {

    @BindView(R.id.tv_content)
    TextView mContent;

    //@Inject
    User user;

    @Override
    protected int getLayoutId() {
        return R.layout.test_fragment;
    }



    @Override
    protected void initData() {
        super.initData();
        //mPresenter.test();
        mContent.setText("Wang：" + user.getName() + "，account：" + user.getAccount());
    }

    @Override
    public void onTestResult(User user) {
        mContent.setText("Wang：" + user.getName() + "，account：" + user.getAccount());
    }
}
