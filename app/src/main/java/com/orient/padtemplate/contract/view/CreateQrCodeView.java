package com.orient.padtemplate.contract.view;

import android.graphics.Bitmap;

import com.orient.padtemplate.base.contract.view.BaseView;

/**
 * CreateQrCode视图
 *
 * Author WangJie
 * Created on 2019/8/2.
 */
public interface CreateQrCodeView extends BaseView {

    /**
     * 生成二维码返回的结果
     *
     * @param result Bitmap
     */
    void onCreateQrCodeResult(Bitmap result);
}
