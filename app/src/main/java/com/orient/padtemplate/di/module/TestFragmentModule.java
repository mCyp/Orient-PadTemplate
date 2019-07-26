package com.orient.padtemplate.di.module;

import com.orient.padtemplate.core.data.db.User;

import dagger.Module;
import dagger.Provides;

/**
 * Author WangJie
 * Created on 2019/7/25.
 */
@Module
public class TestFragmentModule {

    @Provides
    public User providerUser(){
        return new User("1","chen","166","166");
    }
}
