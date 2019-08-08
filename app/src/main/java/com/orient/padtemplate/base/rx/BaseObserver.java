package com.orient.padtemplate.base.rx;



import com.orient.padtemplate.base.contract.view.BaseView;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 基础的观察者
 *
 * Author WangJie
 * Created on 2018/9/4.
 */
public abstract class BaseObserver<T> implements Observer<T> {

    private BaseView view;

    public BaseObserver(BaseView view) {
        this.view = view;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
    }

    @Override
    public void onError(Throwable e) {
        //view.hideLoading();
        if(e instanceof BaseException){
            view.showError(((BaseException)e).msg);
        }
    }

    @Override
    public void onComplete() {
        //view.hideLoading();
    }
}
