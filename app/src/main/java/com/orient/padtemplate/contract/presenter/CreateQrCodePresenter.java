package com.orient.padtemplate.contract.presenter;

import android.graphics.Bitmap;

import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.base.rx.BaseObserver;
import com.orient.padtemplate.contract.view.CreateQrCodeView;
import com.orient.padtemplate.utils.RxUtils;

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
@SuppressWarnings("WeakerAccess")
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
        new RxUtils<>(getCreateQrCodeObservable(id))
                .execute(mLifecycleProvider, new BaseObserver<Bitmap>(mView) {
                    @Override
                    public void onNext(Bitmap bitmap) {
                        super.onNext(bitmap);

                        mView.onCreateQrCodeResult(bitmap);
                    }
                });
    }

    /**
     * 发射一个Bitmap
     */
    private Observable<Bitmap> getCreateQrCodeObservable(String content) {
        Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(content, BGAQRCodeUtil.dp2px(mContext, 144f));
        return Observable.just(bitmap);
    }

}
