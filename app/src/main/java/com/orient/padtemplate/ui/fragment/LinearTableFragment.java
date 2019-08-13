package com.orient.padtemplate.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.fragment.BaseFragment;
import com.orient.padtemplate.widget.placeholder.EmptyView;

import butterknife.BindView;

/**
 * 直线表格布局
 * <p>
 * create an instance of this fragment.
 */
public class LinearTableFragment extends BaseFragment {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @Override
    protected int getLayoutId() {
        return R.layout.common_fragment;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);


        mEmptyView.bind(mRecyclerView);
        //setPlaceHolderView(mEmptyView);
    }


}
