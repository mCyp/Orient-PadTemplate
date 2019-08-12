package com.orient.padtemplate.widget.scroll;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author WangJie
 * Created on 2019/4/26.
 */
public class StopAutoLayoutManger extends LinearLayoutManager {
    public StopAutoLayoutManger(Context context) {
        super(context);
    }

    public StopAutoLayoutManger(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public StopAutoLayoutManger(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) {
        return true;
    }
}
