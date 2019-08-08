package com.orient.padtemplate.contract.presenter;

import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.base.rx.BaseObserver;
import com.orient.padtemplate.common.Common;
import com.orient.padtemplate.contract.view.FlowView;
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
public class FlowPresenter extends BasePresenter<FlowView> {

    @Inject
    TaskRepository taskRepository;

    @Inject
    public FlowPresenter() {
    }

    /**
     * 加载表格
     */
    public void loadTables(String flowId){
        String userId = AppPrefUtils.getString(Common.Constant.SP_USER_ID);
        new RxUtils<>(taskRepository.searchTableByFlowIdAndUserIdRx(flowId,userId))
                .execute(mLifecycleProvider, new BaseObserver<List<Table>>(mView) {
                    @Override
                    public void onNext(List<Table> tables) {
                        super.onNext(tables);

                        mView.onLoadTableResult(tables);
                    }
                });
    }



}
