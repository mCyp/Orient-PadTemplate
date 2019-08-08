package com.orient.padtemplate.core.data.repository;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 基础的仓库名称
 *
 * Author WangJie
 * Created on 2019/8/8.
 */
public class BaseRepository {

    /**
     * 查询一个数据
     */
    protected  <T> Observable<T> queryToTx(final QueryBuilder<T> queryBuilder) {
        return Observable.create(e -> {
            T data = queryBuilder.list().get(0);
            e.onNext(data);
            e.onComplete();
        });
    }

    /**
     * 查询列表 返回一个Observable
     */
    protected <T> Observable<List<T>> queryListToTx(final QueryBuilder<T> queryBuilder) {
        return Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> e) throws Exception {
                List<T> data = queryBuilder.list();
                e.onNext(data);
                e.onComplete();
            }
        });
    }
}
