package com.orient.padtemplate.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.fragment.BaseMvpFragment;
import com.orient.padtemplate.base.recyclerview.DeleteRecyclerAdapter;
import com.orient.padtemplate.contract.presenter.FlowPresenter;
import com.orient.padtemplate.contract.view.FlowView;
import com.orient.padtemplate.core.data.db.Table;
import com.orient.padtemplate.utils.DateUtils;
import com.orient.padtemplate.widget.placeholder.EmptyView;
import com.orient.padtemplate.widget.delete.DeleteRecyclerView;
import com.orient.padtemplate.widget.delete.OnItemClickListener;

import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 流程的展示界面
 * <p>
 * create an instance of this fragment.
 */
public class DeleteFragment extends BaseMvpFragment<FlowPresenter>
        implements FlowView, OnItemClickListener {

    @BindView(R.id.rv_content)
    DeleteRecyclerView mRecyclerView;
    @BindView(R.id.empty)
    EmptyView mEmptyView;


    private DeleteRecyclerAdapter<Table> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.delete_fragment;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setOnItemClickListener(this);
        mAdapter = new DeleteRecyclerAdapter<Table>() {
            @Override
            protected DeleteViewHolder<Table> onCreateViewHolder(View root, int viewType) {
                return new ViewHolder(root);
            }

            @Override
            protected int getItemViewType(Table table, int position) {
                return R.layout.delete_reycle_item;
            }
        };
        mRecyclerView.setAdapter(mAdapter);

        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();

        // 占位布局加载
        mPlaceHolderView.triggerLoading();
        mPresenter.loadTables("1-1");
    }

    @Override
    public void onLoadTableResult(List<Table> result) {
        if (result.size() == 0) {
            mAdapter.clear();
        } else {
            mAdapter.replace(result);
        }
        mPlaceHolderView.triggerOkOrEmpty(result.size() > 0);
    }

    @Override
    public void onItemClick(View view, int position) {
        showToast("点击了一次位置："+position);
    }

    @Override
    public void onDeleteClick(int position) {
        mAdapter.getItems().remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    class ViewHolder extends DeleteRecyclerAdapter.DeleteViewHolder<Table> {

        @BindView(R.id.tv_title)
        TextView mTitleTv;
        @BindView(R.id.tv_time)
        TextView mTimeTv;
        @BindView(R.id.tv_state)
        TextView mStateTv;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Table table) {
            mTitleTv.setText(table.getName());
            mTimeTv.setText(DateUtils.date2NormalStr(new Date()));
            if(getAdapterPosition() % 2 == 0) {
                mStateTv.setText("未填");
            }else {
                mStateTv.setText("完成");
            }
        }
    }
}
