package com.orient.padtemplate.utils;

import com.orient.padtemplate.base.rx.BaseException;
import com.orient.padtemplate.base.rx.BaseObserver;
import com.orient.padtemplate.core.data.model.BaseModel;
import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Rxjava的辅助工具类
 * <p>
 * Author WangJie
 * Created on 2018/9/4.
 */
@SuppressWarnings("ALL")
public class RxUtils<T> {

    private Observable<T> observable;

    public RxUtils(Observable<T> observable) {
        this.observable = observable;
    }

    public void execute(LifecycleProvider lifecycleProvider, BaseObserver<T> observer) {
        observable.observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleProvider.<T>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    /**
     * 将BaseModel<R> 的数据转化为R
     *
     * @param source 数据源
     * @param <R>    泛型
     * @return 值
     */
    public static <R> Observable<R> convert(Observable<BaseModel<R>> source) {
        return source.flatMap(new BaseFunc<>());
    }

    /**
     * 将BaseModel<R> 的数据转化为R
     *
     * @param source 数据源
     * @param <R>    泛型
     * @return 是否上传成功
     */
    public static <R> Observable<Boolean> convertBool(Observable<BaseModel<R>> source) {
        return source.flatMap(new BaseBooleanFunc<>());
    }


    /**
     * 基本的转换函数
     */
    public static class BaseFunc<R> implements Function<BaseModel<R>, Observable<R>> {
        @Override
        public Observable<R> apply(BaseModel<R> rBaseModel) throws Exception {
            if ("success".equals(rBaseModel.getResult())) {
                return Observable.just(rBaseModel.getData());
            }
            return Observable.error(new BaseException(rBaseModel.getResult(), rBaseModel.getMsg()));
        }
    }

    /**
     * 判断的转换函数
     */
    public static class BaseBooleanFunc<R> implements Function<BaseModel<R>, Observable<Boolean>> {

        @Override
        public Observable<Boolean> apply(BaseModel<R> rBaseModel) throws Exception {
            if ("success".equals(rBaseModel.getResult())) {
                return Observable.just(true);
            }
            return Observable.error(new BaseException(rBaseModel.getResult(), rBaseModel.getMsg()));
        }
    }
}
