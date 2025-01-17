package com.orient.padtemplate.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orient.me.widget.placeholder.EmptyView;
import com.orient.padtemplate.R;
import com.orient.padtemplate.base.fragment.BaseMvpFragment;
import com.orient.padtemplate.base.recyclerview.RecyclerAdapter;
import com.orient.padtemplate.contract.presenter.FlowPresenter;
import com.orient.padtemplate.contract.view.FlowView;
import com.orient.padtemplate.core.data.db.Table;
import com.orient.padtemplate.utils.DateUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 流程的展示界面
 * <p>
 * create an instance of this fragment.
 */
public class FlowFragment extends BaseMvpFragment<FlowPresenter>
        implements FlowView {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<Table> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.flow_fragment;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter = new RecyclerAdapter<Table>() {
            @Override
            public ViewHolder<Table> onCreateViewHolder(View root, int viewType) {
                return new FlowFragment.ViewHolder(root);
            }

            @Override
            public int getItemLayout(Table table, int position) {
                return R.layout.table_list_recycle_item;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<Table>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder<Table> holder, Table table) {
                super.onItemClick(holder, table);

                Toast.makeText(getContext(), "点击表格：" + table.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(RecyclerAdapter.ViewHolder<Table> holder, Table table) {
                super.onItemLongClick(holder, table);

                // 长按点击事件
            }
        });

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
            mAdapter.remove();
        } else {
            mAdapter.replace(result);
        }
        mPlaceHolderView.triggerOkOrEmpty(result.size() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Table> {

        @BindView(R.id.tv_name)
        TextView mNameTv;
        @BindView(R.id.tv_state)
        TextView mStateTv;
        @BindView(R.id.tv_time)
        TextView mTimeTv;
        @BindView(R.id.iv_state)
        ImageView mStateIv;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Table table) {
            mNameTv.setText(table.getName());
            // 当前未设置状态 偶数位置Finish 奇数位置为待填
            if (getAdapterPosition() % 2 == 0) {
                mStateIv.setImageResource(R.drawable.common_state_finish);
                mStateTv.setText("完成");
            } else {
                mStateIv.setImageResource(R.drawable.common_state_wait);
                mStateTv.setText("待填");
            }
            mTimeTv.setText(DateUtils.date2NormalStr(new Date()));
        }
    }
}
