package com.orient.padtemplate.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.activity.BaseActivity;
import com.orient.padtemplate.base.recyclerview.RecyclerAdapter;
import com.orient.padtemplate.utils.UIUtils;
import com.orient.padtemplate.widget.suspension.Item;
import com.orient.padtemplate.widget.suspension.SuspensionDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DialogActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_dialog)
    RecyclerView mDialogRv;

    private RecyclerAdapter<Item> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mDialogRv.setLayoutManager(new LinearLayoutManager(this));
        mDialogRv.setAdapter(mAdapter = new RecyclerAdapter<Item>() {
            @Override
            public ViewHolder<Item> onCreateViewHolder(View root, int viewType) {
                return new DialogActivity.ViewHolder(root);
            }

            @Override
            public int getItemLayout(Item item, int position) {
                return R.layout.dialog_recycle_item;
            }
        });
        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<Item>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder<Item> holder, Item item) {
                super.onItemClick(holder, item);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

        List<Item> items = new ArrayList<>();
        // 通用样式
        items.add(new Item("正常","通用样式"));
        items.add(new Item("错误","通用样式"));
        items.add(new Item("警告","通用样式"));
        items.add(new Item("加载","通用样式"));
        // 滚轮样式
        items.add(new Item("时间选择","滚轮样式"));
        items.add(new Item("多列选择","滚轮样式"));
        // 自定义
        items.add(new Item("IP端口选择","自定义"));
        items.add(new Item("列表多选","自定义"));

        SuspensionDecoration mDecoration = new SuspensionDecoration(this, mAdapter.getItems());
        mDialogRv.addItemDecoration(mDecoration);

        mAdapter.replace(items);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Item> {

        @BindView(R.id.tv_name)
        TextView mName;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Item item) {
            mName.setText(item.getName());
        }
    }
}
