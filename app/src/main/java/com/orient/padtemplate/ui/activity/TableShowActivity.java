package com.orient.padtemplate.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.activity.BaseActivity;
import com.orient.padtemplate.ui.adapter.TableShowAdapter;
import com.orient.padtemplate.widget.NoScrollViewPager;

import butterknife.BindView;

public class TableShowActivity extends BaseActivity {

    public static final String[] titles = {"网格表格", "线性表格"};

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tby_title)
    TabLayout mTabLayout;
    @BindView(R.id.vp_content)
    NoScrollViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.common_tab_viewpager_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mViewPager.setNoScroll(true);
        for (int i = 0; i < titles.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }

        mTabLayout.setupWithViewPager(mViewPager, true);
        TableShowAdapter mAdapter = new TableShowAdapter(getSupportFragmentManager(), titles);
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        });
    }
}
