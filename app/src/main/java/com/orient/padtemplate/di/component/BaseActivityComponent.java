package com.orient.padtemplate.di.component;

import com.orient.padtemplate.base.activity.BaseMvpActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * 基础Fragment的Component
 *
 * Author WangJie
 * Created on 2019/7/25.
 */
@Subcomponent(modules = {AndroidInjectionModule.class})
public interface BaseActivityComponent extends AndroidInjector<BaseMvpActivity>{

    @Subcomponent.Builder
    abstract class BaseBuilder extends AndroidInjector.Builder<BaseMvpActivity>{

    }
}
