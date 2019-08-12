package com.orient.padtemplate.base.listener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 监听文本的适配器
 *
 * @Author WangJie on 2018/3/5.
 */

public abstract class TextWatcherListener implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
