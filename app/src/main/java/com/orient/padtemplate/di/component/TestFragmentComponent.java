package com.orient.padtemplate.di.component;

import com.orient.padtemplate.di.module.TestFragmentModule;
import com.orient.padtemplate.di.scope.PerActivityScope;
import com.orient.padtemplate.ui.fragment.TestFragment;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Author WangJie
 * Created on 2019/7/26.
 */
@Subcomponent(modules = {TestFragmentModule.class})
public interface TestFragmentComponent extends AndroidInjector<TestFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<TestFragment> {}
}
