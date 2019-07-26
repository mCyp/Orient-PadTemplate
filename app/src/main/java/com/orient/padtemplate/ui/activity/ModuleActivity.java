package com.orient.padtemplate.ui.activity;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.activity.BaseActivity;
import com.orient.padtemplate.base.recyclerview.RecyclerAdapter;
import com.orient.padtemplate.utils.GlideUtils;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class ModuleActivity extends BaseActivity {

    // TODO
    //  展示功能
    // 1. 表格流程
    // 2. 创建填写
    // 3. 扫码
    // 4. 二维码生成
    // 5. 列表展示
    // 6. 表格展示
    // 7. 弹出框 可以使用腾讯的Dialog
    // 8. 其他

    @BindView(R.id.rv_module)
    RecyclerView mModuleRv;
    @BindView(R.id.iv_bg)
    ImageView mBgIv;
    private RecyclerAdapter<ModuleItem> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.module_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

       // GlideUtils.loadResource(this,R.drawable.module_iv_bg,mBgIv);

        mModuleRv.setLayoutManager(
                new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        );
        mAdapter = new RecyclerAdapter<ModuleItem>() {
            @Override
            public ViewHolder<ModuleItem> onCreateViewHolder(View root, int viewType) {
                return new ModuleActivity.ViewHolder(root);
            }

            @Override
            public int getItemLayout(ModuleItem moduleItem, int position) {
                return R.layout.module_recycler_item;
            }
        };
        mModuleRv.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();

        List<ModuleItem> list = new LinkedList<>();
        for (int i = 0; i < 8; i++) {
            list.add(new ModuleItem("扫码", R.drawable.module_ic_qrcode));
        }
        mAdapter.addAllData(list);
        mAdapter.notifyDataSetChanged();
    }


    class ModuleItem {
        String name;
        @DrawableRes
        int source;

        public ModuleItem(String name, int source) {
            this.name = name;
            this.source = source;
        }
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<ModuleItem> {

        @BindView(R.id.iv_content)
        ImageView mHeaderIv;

        @BindView(R.id.tv_name)
        TextView mNameTv;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(ModuleItem moduleItem) {
            mHeaderIv.setImageResource(moduleItem.source);
            mNameTv.setText(moduleItem.name);
        }
    }

}
