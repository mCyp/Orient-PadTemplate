package com.orient.padtemplate.widget.delete;

import android.view.View;

/**
 * item点击事件
 *
 * Author WangJie
 * Created on 2018/4/24.
 */

public interface OnItemClickListener {

    /**
     * 点击事件
     * @param view view
     * @param position 位置
     */
    void onItemClick(View view, int position);

    /**
     * 点击删除的时候
     * @param position 位置
     */
    void onDeleteClick(int position);
}
