package com.orient.padtemplate.base.app;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.orient.padtemplate.core.data.dao.DaoMaster;
import com.orient.padtemplate.core.data.dao.DaoSession;
import com.orient.padtemplate.injection.component.AppComponent;
import com.orient.padtemplate.injection.component.DaggerAppComponent;
import com.orient.padtemplate.injection.module.AppModule;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.DaggerApplication;

/**
 * Author WangJie
 * Created on 2019/7/24.
 */
public class App extends Application implements HasActivityInjector {
    private static App INSTANCE;

    @Inject
    DispatchingAndroidInjector<Activity> mAndroidInjector;
    private DaoSession mDaoSession;
    public AppComponent mAppComponent;

    public static App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;
        // 提供GreenDao初始化
        initDatabase();
        initInjection();
    }

    /**
     * 初始化数据库
     */
    private void initDatabase() {
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(this, "dive", null);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        DaoMaster mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * 初始化Dagger
     */
    private void initInjection() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this, mDaoSession))
                .build();
        mAppComponent.inject(this);
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mAndroidInjector;
    }
}
