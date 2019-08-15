package com.orient.padtemplate.widget.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.orient.padtemplate.R;

/**
 * Created by Aspsine on 2015/9/9.
 */
public class TwitterRefreshHeaderView extends SwipeRefreshHeaderLayout {

    private ImageView mArrowIv;

    private ImageView mSuccessIv;

    private TextView tvRefresh;

    private ImageView mProcessIv;

    private int mHeaderHeight;

    private Animation rotateUp;

    private Animation rotateDown;

    private boolean rotated = false;

    public TwitterRefreshHeaderView(Context context) {
        this(context, null);
    }

    public TwitterRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwitterRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_twitter);
        rotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvRefresh =  findViewById(R.id.tvRefresh);
        mArrowIv =  findViewById(R.id.iv_arrow);
        mSuccessIv =  findViewById(R.id.iv_success);
        mProcessIv =  findViewById(R.id.iv_process);
    }

    @Override
    public void onRefresh() {
        mSuccessIv.setVisibility(GONE);
        mArrowIv.clearAnimation();
        mArrowIv.setVisibility(GONE);
        mProcessIv.setVisibility(VISIBLE);
        tvRefresh.setText("加载中");
    }

    @Override
    public void onPrepare() {
        Log.d("TwitterRefreshHeader", "onPrepare()");
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            mArrowIv.setVisibility(VISIBLE);
            mProcessIv.setVisibility(GONE);
            mSuccessIv.setVisibility(GONE);
            if (y > mHeaderHeight) {
                tvRefresh.setText("松开刷新");
                if (!rotated) {
                    mArrowIv.clearAnimation();
                    mArrowIv.startAnimation(rotateUp);
                    rotated = true;
                }
            } else if (y < mHeaderHeight) {
                if (rotated) {
                    mArrowIv.clearAnimation();
                    mArrowIv.startAnimation(rotateDown);
                    rotated = false;
                }

                tvRefresh.setText("向下拉");
            }
        }
    }

    @Override
    public void onRelease() {
        Log.d("TwitterRefreshHeader", "onRelease()");
    }

    @Override
    public void onComplete() {
        rotated = false;
        mSuccessIv.setVisibility(VISIBLE);
        mArrowIv.clearAnimation();
        mArrowIv.setVisibility(GONE);
        mProcessIv.setVisibility(GONE);
        tvRefresh.setText("完成");
    }

    @Override
    public void onReset() {
        rotated = false;
        mSuccessIv.setVisibility(GONE);
        mArrowIv.clearAnimation();
        mArrowIv.setVisibility(GONE);
        mProcessIv.setVisibility(GONE);
    }

}
