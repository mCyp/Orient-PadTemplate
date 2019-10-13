package com.orient.padtemplate.contract.presenter;

import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.base.rx.BaseObserver;
import com.orient.padtemplate.contract.view.EventView;
import com.orient.padtemplate.core.data.db.Trouble;
import com.orient.padtemplate.core.data.repository.TroubleRepository;
import com.orient.padtemplate.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

public class EventPresenter extends BasePresenter<EventView> {

    @Inject
    TroubleRepository repository;

    @Inject
    public EventPresenter() {
    }

    public void loadTroubles(String userId){
        new RxUtils<>(repository.searchTroublesByUserId(userId))
                .execute(mLifecycleProvider, new BaseObserver<List<Trouble>>(mView) {
                    @Override
                    public void onNext(List<Trouble> troubles) {
                        super.onNext(troubles);

                        mView.onLoadTroubles(troubles);
                    }
                });
    }
}
