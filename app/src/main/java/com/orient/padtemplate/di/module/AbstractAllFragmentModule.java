package com.orient.padtemplate.di.module;

import com.orient.padtemplate.di.component.BaseFragmentComponent;
import com.orient.padtemplate.ui.fragment.DeleteFragment;
import com.orient.padtemplate.ui.fragment.FlowFragment;
import com.orient.padtemplate.ui.fragment.GridTableFragment;
import com.orient.padtemplate.ui.fragment.LinearTableFragment;
import com.orient.padtemplate.ui.fragment.TaskFragment;
import com.orient.padtemplate.ui.fragment.TestFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * 抽象的所有的活动的Module
 * <p>
 * Author WangJie
 * Created on 2019/7/25.
 */
@Module(subcomponents = {BaseFragmentComponent.class})
public abstract class AbstractAllFragmentModule {

    @ContributesAndroidInjector(modules = TestFragmentModule.class)
    abstract TestFragment contributesTestFragmentInjector();

    @ContributesAndroidInjector(modules = FlowFragmentModule.class)
    abstract FlowFragment contributesFlowFragmentInjector();

    @ContributesAndroidInjector(modules = TaskFragmentModule.class)
    abstract TaskFragment contributesTaskFragmentInjector();

    @ContributesAndroidInjector(modules = DeleteFragmentModule.class)
    abstract DeleteFragment contributesDeleteFragmentInjector();

    @ContributesAndroidInjector(modules = GridTableFragmentModule.class)
    abstract GridTableFragment contributesGridTableFragmentModuleInjector();

    @ContributesAndroidInjector(modules = LinearTableFragmentModule.class)
    abstract LinearTableFragment contributesLinearTableFragmentModuleInjector();
}
