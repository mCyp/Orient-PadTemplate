package com.orient.padtemplate.base.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import com.orient.padtemplate.common.AppManager;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * 抽象的基础的活动
 * <p>
 * Author WangJie
 * Created on 2019/7/23.
 */
@SuppressWarnings("unused")
public abstract class BaseActivity extends AppCompatActivity {

    AppManager appManager;
    private Unbinder unbinder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appManager = AppManager.getInstance();
        initWindows();
        if (initArgs(getIntent().getExtras())) {
            setContentView(getLayoutId());
            initWidget();
            initData();
            appManager.addActivity(this);
        } else {
            finish();
        }
    }

    /**
     * 初始化窗体
     */
    private void initWindows() {
    }

    /**
     * 初始化参数
     *
     * @return 默认返回True 不然就结束当前的界面
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 得到资源文件
     *
     * @return 资源文件的Id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget() {
        unbinder = ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        appManager.finishActivity(this);
        // 释放
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    /**
     * 控制界面
     */
    protected void controlKeyBoardLayout(final View root, final View scrollView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获取视图的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                if (rootInvisibleHeight > 100) {
                    // 获取要滚动的坐标的位置
                    int[] location = new int[2];
                    scrollView.getLocationInWindow(location);
                    int scrollHeight = (location[1] + scrollView.getHeight()) - rect.bottom;
                    scrollView.scrollTo(0, scrollHeight);
                } else {
                    scrollView.scrollTo(0, 0);
                }
            }
        });
    }
}
