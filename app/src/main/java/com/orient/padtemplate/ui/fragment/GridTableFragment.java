package com.orient.padtemplate.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.fragment.BaseMvpFragment;
import com.orient.padtemplate.base.recyclerview.RecyclerAdapter;
import com.orient.padtemplate.contract.presenter.FlowPresenter;
import com.orient.padtemplate.contract.presenter.TablePresenter;
import com.orient.padtemplate.contract.view.FlowView;
import com.orient.padtemplate.contract.view.TableView;
import com.orient.padtemplate.core.data.db.Cell;
import com.orient.padtemplate.core.data.db.Table;
import com.orient.padtemplate.ui.adapter.GridTableAdapter;
import com.orient.padtemplate.utils.DateUtils;
import com.orient.padtemplate.widget.placeholder.EmptyView;
import com.orient.padtemplate.widget.scroll.ScrollablePanel;

import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 流程的展示界面
 * <p>
 * create an instance of this fragment.
 */
public class GridTableFragment extends BaseMvpFragment<TablePresenter>
        implements TableView {

    @BindView(R.id.sp_content)
    ScrollablePanel mScrollablePanel;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private GridTableAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.grid_table_fragment;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        mEmptyView.bind(mScrollablePanel);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();

        mPlaceHolderView.triggerLoading();
        mPresenter.loadCells("1-1-1");
    }

    @Override
    public void onLoadCellResult(List<Cell> titles, List<Cell> contents) {
        if (titles.size() == 0 || contents.size() == 0) {
            mPlaceHolderView.triggerEmpty();
            return;
        }
        //  得到设备信息
        DisplayMetrics d = getResources().getDisplayMetrics();

        mAdapter = new GridTableAdapter(titles, contents, getContext(), true, d.widthPixels, this);
        mScrollablePanel.setPanelAdapter(mAdapter);
        mPlaceHolderView.triggerOk();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GridTableAdapter.TYPE_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                mAdapter.notifyPhotoChanged();
            }
        }
    }
}
