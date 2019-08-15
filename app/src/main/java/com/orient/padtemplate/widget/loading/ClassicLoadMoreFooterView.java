package com.orient.padtemplate.widget.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.orient.padtemplate.R;

/**
 * Created by Aspsine on 2015/9/2.
 */
public class ClassicLoadMoreFooterView extends SwipeLoadMoreFooterLayout {
    private TextView mLoadMoreTv;
    private ImageView mSuccessIv;
    private ImageView mProgressIv;

    private int mFooterHeight;

    public ClassicLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public ClassicLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.load_more_footer_height_classic);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLoadMoreTv = findViewById(R.id.tv_load_more);
        mSuccessIv = findViewById(R.id.iv_success);
        mProgressIv = findViewById(R.id.iv_process);
    }

    @Override
    public void onPrepare() {
        mSuccessIv.setVisibility(GONE);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            mSuccessIv.setVisibility(GONE);
            mProgressIv.setVisibility(GONE);
            if (-y >= mFooterHeight) {
                mLoadMoreTv.setText("松开加载更多");
            } else {
                mLoadMoreTv.setText("向上拉");
            }
        }
    }

    @Override
    public void onLoadMore() {
        mLoadMoreTv.setText("加载中");
        mProgressIv.setVisibility(VISIBLE);
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        mProgressIv.setVisibility(GONE);
        mSuccessIv.setVisibility(VISIBLE);
    }

    @Override
    public void onReset() {
        mSuccessIv.setVisibility(GONE);
    }
}
