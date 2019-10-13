package com.orient.padtemplate.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.activity.BaseMvpActivity;
import com.orient.padtemplate.common.Common;
import com.orient.padtemplate.contract.presenter.LoginPresenter;
import com.orient.padtemplate.contract.view.LoginView;
import com.orient.padtemplate.utils.AppPrefUtils;

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

    }

    @Override
    protected void initData() {
        super.initData();

        // 第一次打开App的时候初始化数据
        boolean isFirstInit = AppPrefUtils.getBoolean(Common.Constant.IS_FIRST_INIT);
        if (isFirstInit) {
            // TODO 展示加载框
            mPresenter.onFirstInit();
        }
    }

    @Override
    public void onLoginResult(boolean result) {
        Log.e("login", "login11111");

        if(result) {
            Intent intent = new Intent(this, ModuleActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onFirstInitResult(boolean result) {
        if(result)
            AppPrefUtils.putBoolean(Common.Constant.IS_FIRST_INIT, true);
    }

    @OnClick(R.id.btn_login)
    public void login() {
        mPresenter.login("guest","123456");
    }
}
