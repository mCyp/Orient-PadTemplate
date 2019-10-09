package com.orient.padtemplate.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orient.me.widget.rv.layoutmanager.DoubleSideLayoutManager;
import com.orient.padtemplate.R;
import com.orient.padtemplate.base.fragment.BaseMvpFragment;
import com.orient.padtemplate.base.recyclerview.RecyclerAdapter;
import com.orient.padtemplate.contract.presenter.TaskPresenter;
import com.orient.padtemplate.contract.view.TaskView;
import com.orient.padtemplate.core.data.db.Flow;
import com.orient.padtemplate.utils.DateUtils;
import com.orient.padtemplate.utils.UIUtils;
import com.orient.padtemplate.widget.itemdecoration.DotItemDecoration;
import com.orient.padtemplate.widget.placeholder.EmptyView;

import java.util.Date;
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

    @Override
    protected int getLayoutId() {
        return R.layout.common_fragment;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        //mRecyclerView.setBackgroundColor(Color.parseColor("#9575cd"));

        mRecyclerView.setLayoutManager(new DoubleSideLayoutManager(DoubleSideLayoutManager.START_LEFT));
        //mRecyclerView.setLayoutManager(new TwoSideLayoutManager(getContext()));
        mAdapter = new RecyclerAdapter<Flow>() {
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
       /* DotItemDecoration dotItemDecoration = providesDotItemDecoration();
        mRecyclerView.addItemDecoration(dotItemDecoration);*/

        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();

        // 占位布局加载
        mPlaceHolderView.triggerLoading();
        mPresenter.loadFlows("1");
    }

    private DotItemDecoration providesDotItemDecoration(){
        return new DotItemDecoration.Builder(getContext())
                .setOrientation(DotItemDecoration.VERTICAL)//if you want a horizontal item decoration,remember to set horizontal orientation to your LayoutManager
                .setItemStyle(DotItemDecoration.STYLE_DRAW)//choose to draw or use resource
                .setDotColor(Color.parseColor("#673AB7"))
                .setDotRadius(8)//dp
                .setDotPaddingTop(2)
                .setDotInItemOrientationCenter(true)//set true if you want the dot align center
                .setLineWidth(2)//dp
                .setEndText("结束")
                .setTextSize(14)
                .setTextColor(Color.parseColor("#673AB7"))
                //.setBottomRes(R.drawable.ic_ma_1)
                .setDotPaddingText(2)//dp.The distance between the last dot and the end text
                .create();
    }

    @Override
    public void onLoadTableResult(List<Flow> result) {
        if (result.size() == 0) {
            mAdapter.remove();
        } else {
            mAdapter.replace(result);
        }
        mPlaceHolderView.triggerOkOrEmpty(result.size() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Flow> {

        @BindView(R.id.tv_name)
        TextView mNameTv;
        @BindView(R.id.tv_process)
        TextView mProcessTv;
        @BindView(R.id.tv_time)
        TextView mTimeTv;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onBind(Flow flow) {
            mNameTv.setText(flow.getName());
            mProcessTv.setText("进度：" + getAdapterPosition() + "/" + mAdapter.getItemCount());
            mTimeTv.setText(DateUtils.date2NormalStr(new Date()));
        }
    }
}
