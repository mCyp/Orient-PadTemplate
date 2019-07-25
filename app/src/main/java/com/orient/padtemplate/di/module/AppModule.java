package com.orient.padtemplate.di.module;

import android.content.Context;

import com.orient.padtemplate.core.data.dao.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger中的Module
 *
 * Author WangJie
 * Created on 2019/7/24.
 */
@Module
public class AppModule {
    private Context context;
    private DaoSession daoSession;

    public AppModule(Context context, DaoSession daoSession) {
        this.context = context;
        this.daoSession = daoSession;
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return context;
    }

    @Singleton
    @Provides
    public DaoSession provideDaoSession() {
        return daoSession;
    }
}
