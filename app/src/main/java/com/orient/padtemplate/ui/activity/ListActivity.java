package com.orient.padtemplate.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.orient.padtemplate.R;
import com.orient.padtemplate.ui.adapter.ListAdapter;
import com.orient.padtemplate.widget.NoScrollViewPager;

import butterknife.BindView;

public class ListActivity extends AppCompatActivity {

    public static final String[] titles = {"故障记录", "更换记录", "损耗记录"};

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tby_title)
    TabLayout mTabLayout;
    @BindView(R.id.vp_content)
    NoScrollViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mViewPager.setNoScroll(true);
        for (int i = 0; i < titles.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }

        mTabLayout.setupWithViewPager(mViewPager, true);
        ListAdapter mAdapter = new ListAdapter(getSupportFragmentManager(), titles);
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
