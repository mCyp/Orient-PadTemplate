package com.orient.padtemplate.contract.presenter;

import android.widget.Toast;

import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.contract.view.LoginView;
import com.orient.padtemplate.contract.view.TestView;
import com.orient.padtemplate.core.data.db.User;
import com.orient.padtemplate.core.data.repository.UserRepository;

import javax.inject.Inject;

/**
 * Author WangJie
 * Created on 2019/7/25.
 */
@SuppressWarnings("WeakerAccess")
public class TestPresenter extends BasePresenter<TestView> {

    @Inject
    UserRepository userRepository;

    @Inject
    public TestPresenter() {
    }

    /**
     * 测试
     */
    public void test() {
        // TODO
        // 完善正常的登录写法

        User user = userRepository.login("188", "188");
        if (user != null) {
            Toast.makeText(mContext, "登录处理中...", Toast.LENGTH_SHORT).show();
            mView.onTestResult(user);
        }
    }



}
