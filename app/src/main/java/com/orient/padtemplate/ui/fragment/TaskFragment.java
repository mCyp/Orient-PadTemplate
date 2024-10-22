package com.orient.padtemplate.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.placeholder.EmptyView;
import com.orient.me.widget.rv.itemdocration.timeline.TimeLine;
import com.orient.me.widget.rv.layoutmanager.DoubleSideLayoutManager;
import com.orient.padtemplate.R;
import com.orient.padtemplate.base.fragment.BaseMvpFragment;
import com.orient.padtemplate.base.recyclerview.RecyclerAdapter;
import com.orient.padtemplate.contract.presenter.TaskPresenter;
import com.orient.padtemplate.contract.view.TaskView;
import com.orient.padtemplate.core.data.db.Flow;
import com.orient.padtemplate.widget.timeline.DateInfoDTL;

import java.util.List;

import butterknife.BindView;

/**
 * 流程的展示界面
 * <p>
 * create an instance of this fragment.
 */
public class TaskFragment extends BaseMvpFragment<TaskPresenter>
        implements TaskView {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<Flow> mAdapter;
    private TimeLine timeLine;

    @Override
    protected int getLayoutId() {
        return R.layout.common_fragment;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        //mRecyclerView.setBackgroundColor(Color.parseColor("#9575cd"));
        mRecyclerView.setLayoutManager(new DoubleSideLayoutManager(DoubleSideLayoutManager.START_LEFT));
        mAdapter = new RecyclerAdapter<Flow>(null) {
            @Override
            public ViewHolder<Flow> onCreateViewHolder(View root, int viewType) {
                return new TaskFragment.ViewHolder(root);
            }

            @Override
            public int getItemLayout(Flow flow, int position) {
                int remainder = position % 2;
                if (remainder == 0) {
                    return R.layout.task_left_recycle_item;
                }
                return R.layout.task_right_recycle_item;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        // 分隔线
        timeLine = provideTimeLine(mAdapter.getItems());
        mRecyclerView.addItemDecoration(timeLine);

        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }

    private TimeLine provideTimeLine(List<Flow> timeItems) {
        return new TimeLine.Builder(getContext(), timeItems)
                .setTitleStyle(TimeLine.FLAG_TITLE_POS_NONE, 0)
                .setLine(TimeLine.FLAG_LINE_BEGIN_TO_END, 60, Color.parseColor("#757575"), 2)
                .setDot(TimeLine.FLAG_DOT_DRAW)
                .build(DateInfoDTL.class);
    }

    @Override
    protected void initData() {
        super.initData();

        // 占位布局加载
        mPlaceHolderView.triggerLoading();
        mPresenter.loadFlows("1");
    }

    @Override
    public void onLoadTableResult(List<Flow> result) {
        if (result.size() == 0) {
            mAdapter.remove();
            timeLine.remove();
        } else {
            timeLine.replace(result);
            mAdapter.replace(result);
        }
        mPlaceHolderView.triggerOkOrEmpty(result.size() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Flow> {

        @BindView(R.id.tv_name)
        TextView mNameTv;
        @BindView(R.id.tv_process)
        TextView mProcessTv;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onBind(Flow flow) {
            mNameTv.setText(flow.getName());
            mProcessTv.setText("进度：" + getAdapterPosition() + "/" + mAdapter.getItemCount());
        }
    }
}
