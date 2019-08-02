package com.orient.padtemplate.di.module;

import com.orient.padtemplate.di.component.BaseActivityComponent;
import com.orient.padtemplate.ui.activity.CreateQrCodeActivity;
import com.orient.padtemplate.ui.activity.LoginActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * 抽象的所有的活动的Module
 *
 * Author WangJie
 * Created on 2019/7/25.
 */
@Module(subcomponents = {BaseActivityComponent.class})
public abstract class AbstractAllActivityModule {

    @ContributesAndroidInjector(modules = LoginActivityModule.class)
    abstract LoginActivity contributesLoginActivityInjector();

    @ContributesAndroidInjector(modules = CreateQrCodeActivityModule.class)
    abstract CreateQrCodeActivity contributesCreateQrCodeActivityInjector();
}
