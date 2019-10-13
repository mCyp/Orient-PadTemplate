package com.orient.padtemplate.contract.presenter;


import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.contract.view.AddTroubleView;
import com.orient.padtemplate.core.data.db.Trouble;
import com.orient.padtemplate.core.data.repository.TroubleRepository;

import javax.inject.Inject;

/**
 * 故障提交的逻辑
 * Author WangJie
 * Created on 2019/2/18.
 */
public class AddTroublePresenter extends BasePresenter<AddTroubleView> {

    @SuppressWarnings("WeakerAccess")
    @Inject
    public AddTroublePresenter() {
    }

    @Inject
    TroubleRepository repository;

    /**
     * 提交一个故障记录
     * @param trouble 故障记录
     */
    public void addTrouble(Trouble trouble) {
        final AddTroubleView view = mView;
        if (view == null) {
            return;
        }

        repository.insertTrouble(trouble);

        view.onAddResult(true);
    }

    /**
     * 通过Id去获取故障记录
     * @param id 故障记录的id
     */
    public Trouble searchTroubleById(String id){
        return repository.searchTroubleById(id);
    }

}
