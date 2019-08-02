package com.orient.padtemplate.contract.presenter;

import android.graphics.Bitmap;

import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.contract.view.CreateQrCodeView;

import javax.inject.Inject;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;

/**
 * CreateQrCode逻辑
 * <p>
 * Author WangJie
 * Created on 2019/8/2.
 */
public class CreateQrCodePresenter extends BasePresenter<CreateQrCodeView> {

    @Inject
    public CreateQrCodePresenter() {
    }

    /**
     * 创建打印二维码的方法
     *
     * @param id 期望扫出来的结果
     */
    public void createQrCode(String id) {
        // TODO 利用RxJava回调，防止内存泄漏
        // 参考别人的RxJava的用法
    }

    private Observable<Bitmap> getCreateQrCodeObservable(String content) {
        Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(content, BGAQRCodeUtil.dp2px(mContext, 144f));
        return Observable.just(bitmap);
    }

}
