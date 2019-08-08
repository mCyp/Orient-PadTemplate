package com.orient.padtemplate.contract.presenter;

import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.base.rx.BaseObserver;
import com.orient.padtemplate.common.Common;
import com.orient.padtemplate.contract.view.FlowView;
import com.orient.padtemplate.contract.view.TaskView;
import com.orient.padtemplate.core.data.db.Flow;
import com.orient.padtemplate.core.data.db.Table;
import com.orient.padtemplate.core.data.repository.TaskRepository;
import com.orient.padtemplate.utils.AppPrefUtils;
import com.orient.padtemplate.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * Author WangJie
 * Created on 2019/7/25.
 */
@SuppressWarnings("WeakerAccess")
public class TaskPresenter extends BasePresenter<TaskView> {

    @Inject
    TaskRepository taskRepository;

    @Inject
    public TaskPresenter() {
    }

    /**
     * 加载表格
     */
    public void loadFlows(String taskId){
        new RxUtils<>(taskRepository.searchFlowByFlowIdAndUserIdRx(taskId))
                .execute(mLifecycleProvider, new BaseObserver<List<Flow>>(mView) {
                    @Override
                    public void onNext(List<Flow> flows) {
                        super.onNext(flows);

                        mView.onLoadTableResult(flows);
                    }
                });
    }



}
