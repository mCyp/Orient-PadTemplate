package com.orient.padtemplate.ui.activity.event;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.placeholder.EmptyView;
import com.orient.padtemplate.R;
import com.orient.padtemplate.base.activity.BaseMvpActivity;
import com.orient.padtemplate.base.app.App;
import com.orient.padtemplate.base.fragment.BaseMvpFragment;
import com.orient.padtemplate.base.recyclerview.DeleteRecyclerAdapter;
import com.orient.padtemplate.common.Common;
import com.orient.padtemplate.contract.presenter.EventPresenter;
import com.orient.padtemplate.contract.presenter.FlowPresenter;
import com.orient.padtemplate.contract.view.EventView;
import com.orient.padtemplate.contract.view.FlowView;
import com.orient.padtemplate.core.data.db.Table;
import com.orient.padtemplate.core.data.db.Trouble;
import com.orient.padtemplate.utils.AppPrefUtils;
import com.orient.padtemplate.utils.DateUtils;
import com.orient.padtemplate.widget.delete.DeleteRecyclerView;
import com.orient.padtemplate.widget.delete.OnItemClickListener;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 流程的展示界面
 * <p>
 * create an instance of this fragment.
 */
public class EventActivity extends BaseMvpActivity<EventPresenter>
        implements EventView, OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_content)
    DeleteRecyclerView mRecyclerView;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private DeleteRecyclerAdapter<Trouble> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.event_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mToolbar.setNavigationOnClickListener(v-> onBackPressed());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setOnItemClickListener(this);
        mAdapter = new DeleteRecyclerAdapter<Trouble>() {
            @Override
            protected DeleteViewHolder<Trouble> onCreateViewHolder(View root, int viewType) {
                return new ViewHolder(root);
            }

            @Override
            protected int getItemViewType(Trouble table, int position) {
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

        String userId = AppPrefUtils.getString(Common.Constant.KEY_USER_ID);

        // 占位布局加载
        mPlaceHolderView.triggerLoading();
        mPresenter.loadTroubles(userId);
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

    @Override
    public void onLoadTroubles(List<Trouble> troubles) {
        if (troubles.size() == 0) {
            mAdapter.clear();
        } else {
            mAdapter.replace(troubles);
        }
        mPlaceHolderView.triggerOkOrEmpty(troubles.size() > 0);
    }

    @OnClick(R.id.tv_add)
    public void onAdd(){
        AddTroubleActivity.show(this,null);
    }

    class ViewHolder extends DeleteRecyclerAdapter.DeleteViewHolder<Trouble> {

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
        protected void onBind(Trouble trouble) {
            mTitleTv.setText(trouble.getPosition());
            mTimeTv.setText(DateUtils.date2NormalStr(trouble.getCreateDate()));
            if(getAdapterPosition() % 2 == 0) {
                mStateTv.setText("未填");
            }else {
                mStateTv.setText("完成");
            }
        }
    }
}
