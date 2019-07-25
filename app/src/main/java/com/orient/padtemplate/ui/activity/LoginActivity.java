package com.orient.padtemplate.ui.activity;

import android.content.Intent;
import android.util.Log;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.activity.BaseMvpActivity;
import com.orient.padtemplate.contract.presenter.LoginPresenter;
import com.orient.padtemplate.contract.view.LoginView;

import butterknife.OnClick;

public class LoginActivity extends BaseMvpActivity<LoginPresenter>
    implements LoginView {

    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    protected void initData() {
        super.initData();

        /*User user = new User("1","wangjie","188","188");
        mPresenter.saveUser(user);*/
    }

    @Override
    public void onLoginResult(boolean result) {
        Log.e("login","login11111");
    }

    @OnClick(R.id.btn_login)
    public void login(){
        //mPresenter.login("188","188");

        Intent intent = new Intent(this,ModuleActivity.class);
        startActivity(intent);
    }
}
