package com.orient.padtemplate.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.activity.BaseMvpActivity;
import com.orient.padtemplate.contract.presenter.LoginPresenter;
import com.orient.padtemplate.contract.view.LoginView;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseMvpActivity<LoginPresenter>
    implements LoginView {

    @BindView(R.id.iv_bg)
    ImageView mBgIv;


    //  TODO 测试ListAdapter的复用性，是否还会出错
    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //GlideUtils.loadResource(this,R.drawable.login_iv_bg,mBgIv);
    }

    @Override
    protected void initData() {
        super.initData();

        /*Wang user = new Wang("1","wangjie","188","188");
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
