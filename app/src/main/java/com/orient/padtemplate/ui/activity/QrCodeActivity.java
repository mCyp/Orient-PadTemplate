package com.orient.padtemplate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.activity.BaseActivity;
import com.orient.padtemplate.common.Common;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * 二维码的扫描界面
 */
public class QrCodeActivity extends BaseActivity implements QRCodeView.Delegate{

    // 平时使用的时候，发现ZBarView的扫描速度比ZXingView更快
    // 推荐使用ZBarView
    // ZXingView库推荐生成二维码
    @BindView(R.id.zb_view)
    ZBarView mZBarView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_open)
    TextView mOpenBtn;


    @Override
    protected int getLayoutId() {
        return R.layout.qr_code_activity;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        // 请求码
        int requestCode = bundle.getInt(Common.Constant.QR_REQUEST_CODE);
        return requestCode != -1;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        initQrCode();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mOpenBtn.setOnClickListener(v -> {
            if ("打开闪光灯" == mOpenBtn.getText()) {
                mOpenBtn.setText("关闭闪光灯");
                mZBarView.openFlashlight();
            } else {
                mOpenBtn.setText( "打开闪光灯");
                mZBarView.closeFlashlight();
            }
        });
    }

    /**
     * 初始化二维码
     */
    private void initQrCode(){
        mZBarView.setDelegate(this);
        mZBarView.getScanBoxView().setOnlyDecodeScanBoxArea(false); // 识别整个屏幕中的码
    }

    @Override
    protected void onStart() {
        super.onStart();

        mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        super.onStop();

        mZBarView.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        setTitle("扫描结果为：" + result);
        vibrate();
        Intent intent = new Intent();
        intent.putExtra(Common.Constant.QR_REQUEST_RESULT, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 震动函数
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null)
            vibrator.vibrate(200L);
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZBarView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZBarView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZBarView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }
}
