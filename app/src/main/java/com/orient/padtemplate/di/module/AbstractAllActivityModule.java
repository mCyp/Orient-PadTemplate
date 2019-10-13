package com.orient.padtemplate.di.module;

import com.orient.padtemplate.di.component.BaseActivityComponent;
import com.orient.padtemplate.ui.activity.CreateQrCodeActivity;
import com.orient.padtemplate.ui.activity.LoginActivity;
import com.orient.padtemplate.ui.activity.event.AddTroubleActivity;
import com.orient.padtemplate.ui.activity.event.EventActivity;
import com.orient.padtemplate.ui.activity.task.FlowActivity;
import com.orient.padtemplate.ui.activity.task.GridTableActivity;
import com.orient.padtemplate.ui.activity.task.TaskActivity;

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

    @ContributesAndroidInjector(modules = TaskActivityModule.class)
    abstract TaskActivity contributesTaskActivityInjector();

    @ContributesAndroidInjector(modules = FlowActivityModule.class)
    abstract FlowActivity contributesFlowActivityInjector();

    @ContributesAndroidInjector(modules = GridTableActivityModule.class)
    abstract GridTableActivity contributesGridTableActivityInjector();

    @ContributesAndroidInjector(modules = AddTroubleActivityModule.class)
    abstract AddTroubleActivity contributesAddTroubleActivityInjector();

    @ContributesAndroidInjector(modules = EventActivityModule.class)
    abstract EventActivity contributesEventActivityInjector();
}
