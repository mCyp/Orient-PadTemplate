package com.orient.padtemplate.injection.component;

import android.content.Context;

import com.orient.padtemplate.base.app.App;
import com.orient.padtemplate.core.data.dao.DaoSession;
import com.orient.padtemplate.injection.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Author WangJie
 * Created on 2019/7/24.
 */
@Singleton
@Component(modules = {
        AndroidInjectionModule.class
        , AndroidSupportInjectionModule.class
        , AppModule.class
})
public interface AppComponent {

    /**
     * 注入App
     */
    void inject(App app);

    /**
     * 获取一个全局的Context
     *
     * @return Application
     */
    Context getContext();

    /**
     * 获取DaoSession
     *
     * @return DaoSession
     */
    DaoSession getDaoSession();
}
