package com.orient.padtemplate.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.orient.padtemplate.ui.fragment.DeleteFragment;
import com.orient.padtemplate.ui.fragment.FlowFragment;
import com.orient.padtemplate.ui.fragment.GridTableFragment;
import com.orient.padtemplate.ui.fragment.LinearTableFragment;
import com.orient.padtemplate.ui.fragment.TaskFragment;

/**
 * Author WangJie
 * Created on 2019/6/4.
 */
public class TableShowAdapter extends FragmentPagerAdapter {
    private String[] titles;

    public TableShowAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new GridTableFragment();
            case 1:
                return new LinearTableFragment();
            default:
                throw new UnsupportedOperationException("不支持的类型");
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
