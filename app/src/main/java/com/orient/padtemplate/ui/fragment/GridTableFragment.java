package com.orient.padtemplate.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.orient.padtemplate.R;
import com.orient.padtemplate.base.fragment.BaseMvpFragment;
import com.orient.padtemplate.contract.presenter.GridTablePresenter;
import com.orient.padtemplate.contract.view.GridTableView;
import com.orient.padtemplate.core.data.db.Cell;
import com.orient.padtemplate.ui.adapter.GridTableAdapter;
import com.orient.padtemplate.widget.placeholder.EmptyView;
import com.orient.padtemplate.widget.scroll.ScrollablePanel;

import java.util.List;

import butterknife.BindView;

/**
 * 网格表格的Fragment
 * 1. 表格布局 ScrollablePanel
 * 2. 分页方法 下拉刷新控件SwipeToLoadLayout + 自定义分页策略
 * create an instance of this fragment.
 */
public class GridTableFragment extends BaseMvpFragment<GridTablePresenter>
        implements GridTableView, ScrollablePanel.OnScrollToTopOrBottomCallback, OnRefreshListener, OnLoadMoreListener {

    // 每次加载四行数据
    private static final int LOADING_LINES = 4;

    @BindView(R.id.swipe_target)
    ScrollablePanel mScrollablePanel;
    @BindView(R.id.empty)
    EmptyView mEmptyView;
    // 刷新的布局
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mLayout;

    private GridTableAdapter mAdapter;
    // 当前的列数
    private int col;
    // 当前的行数
    private int curLine = -1;
    // 当前加载的行数
    private int lastLines = 0;
    // 用来测量上滑还是下滑的距离差

    @Override
    protected int getLayoutId() {
        return R.layout.grid_table_fragment;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        mLayout.setOnRefreshListener(this);
        mLayout.setOnLoadMoreListener(this);
        mLayout.setRefreshEnabled(false);
        mLayout.setLoadMoreEnabled(false);
        mScrollablePanel.addScrollToTopOrBottomListener(this);

        mEmptyView.bind(mLayout);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();

        mPlaceHolderView.triggerOk();
        mLayout.post(() -> {
            mLayout.setRefreshEnabled(true);
            mLayout.setRefreshing(true);
        });
    }

    @Override
    public void onInitLoadResult(List<Cell> titles, List<Cell> contents) {
        if (titles.size() == 0 || contents.size() == 0) {
            mPlaceHolderView.triggerEmpty();
            return;
        }

        col = titles.size();
        lastLines = contents.size() / col;
        curLine = lastLines;

        mAdapter = new GridTableAdapter(titles, contents, getContext(), true, this);
        mScrollablePanel.setPanelAdapter(mAdapter);

        mLayout.setRefreshing(false);
        mLayout.setRefreshEnabled(false);
    }

    @Override
    public void onLoadMoreResult(List<Cell> contents) {
        if (contents.size() == 0) {
            showToast("数据加载完毕");
            mLayout.setLoadingMore(false);
            mLayout.setLoadMoreEnabled(false);
            return;
        }

        lastLines = contents.size() / col;
        curLine += lastLines;

        List<Cell> curCells = mAdapter.getContents();
        curCells.addAll(contents);
        mAdapter = new GridTableAdapter(mAdapter.getTitles()
                , curCells, getContext(), true, this);
        mScrollablePanel.setPanelAdapter(mAdapter);

        if (mLayout.isLoadingMore()) {
            mLayout.setLoadingMore(false);
            mLayout.setLoadMoreEnabled(false);
        }
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

    @Override
    public void onScrollToTopOrBottom(boolean isToTop, boolean isToBottom) {
        if (!isToTop) {
            mLayout.setLoadMoreEnabled(true);
            mLayout.setLoadingMore(true);
        }
    }

    @Override
    public void onLoadMore() {
        if (lastLines < LOADING_LINES && lastLines != -1) {
            showToast("数据加载完毕！");
            mLayout.setLoadingMore(false);
            mLayout.setLoadMoreEnabled(false);
            return;
        }
        mPresenter.loadMore("1-1-1", curLine + 1, curLine + LOADING_LINES);
    }

    @Override
    public void onRefresh() {
        // 加载1行表格标题
        // 加载6行表格头部
        mPresenter.initLoad("1-1-1");
    }
}
