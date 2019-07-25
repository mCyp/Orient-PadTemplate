package com.orient.padtemplate.contract.presenter;

import android.widget.Toast;

import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.contract.view.LoginView;
import com.orient.padtemplate.core.data.db.User;
import com.orient.padtemplate.core.data.repository.UserRepository;

import javax.inject.Inject;

/**
 * Author WangJie
 * Created on 2019/7/25.
 */
@SuppressWarnings("WeakerAccess")
public class LoginPresenter extends BasePresenter<LoginView> {

    @Inject
    UserRepository userRepository;

    @Inject
    public LoginPresenter() {
    }

    /**
     * 登录
     *
     * @param account  账号
     * @param password 密码
     */
    public void login(String account, String password) {
        // TODO
        // 完善正常的登录写法

        User user = userRepository.login(account, password);
        if (user != null) {
            Toast.makeText(mContext, "登录处理中...", Toast.LENGTH_SHORT).show();
            mView.onLoginResult(true);
        }
    }

    public void saveUser(User user){
        userRepository.insertUser(user);
    }


}
