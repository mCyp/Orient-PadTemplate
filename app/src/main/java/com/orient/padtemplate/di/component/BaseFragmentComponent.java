package com.orient.padtemplate.di.component;

import com.orient.padtemplate.base.fragment.BaseMvpFragment;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * 基础活动的ActivityComponent
 *
 * Author WangJie
 * Created on 2019/7/25.
 */
@Subcomponent(modules = {AndroidInjectionModule.class})
public interface BaseFragmentComponent extends AndroidInjector<BaseMvpFragment>{

    @Subcomponent.Builder
    abstract class BaseBuilder extends AndroidInjector.Builder<BaseMvpFragment>{

    }
}
