package com.orient.padtemplate.ui.activity;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.activity.BaseMvpActivity;
import com.orient.padtemplate.contract.presenter.CreateQrCodePresenter;
import com.orient.padtemplate.contract.view.CreateQrCodeView;

import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建二维码的界面
 */
public class CreateQrCodeActivity extends BaseMvpActivity<CreateQrCodePresenter>
        implements CreateQrCodeView {

    // ******** 介绍 ********
    // 此界面通常用来利用蓝牙连接打印机进行打印二维码
    // 本Demo没有接入具体的打印SDK

    @BindView(R.id.et_name)
    EditText mNameEt;
    @BindView(R.id.et_serial_num)
    EditText mSerialEt;
    @BindView(R.id.et_person)
    EditText mPersonEt;
    @BindView(R.id.tv_qr_name)
    TextView mNameTv;
    @BindView(R.id.tv_qr_serial_num)
    TextView mSerialNumTv;
    @BindView(R.id.tv_qr_person)
    TextView mPersonTv;

    @Override
    protected int getLayoutId() {
        return R.layout.create_qr_code_activity;
    }

    @Override
    public void onCreateQrCodeResult(Bitmap result) {

    }

    // 预览方法
    @OnClick(R.id.btn_preview)
    public void preview() {
        String name = mNameEt.getText().toString();
        String serialNum = mSerialEt.getText().toString();
        String person = mPersonEt.getText().toString();

        if (TextUtils.isEmpty(name)
                || TextUtils.isEmpty(serialNum)
                || TextUtils.isEmpty(person)) {
            showToast("名称、编号或者负责人不能为空！");
            return;
        }

        // 创建二维码
        String id = UUID.randomUUID().toString();
        mPresenter.createQrCode(id);

        mNameTv.setText("名称："+name);
        mSerialNumTv.setText("序列号："+serialNum);
        mPersonTv.setText("负责人："+person);
    }
}
