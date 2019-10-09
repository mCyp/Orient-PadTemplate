package com.orient.padtemplate.ui.activity;

import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.activity.BaseActivity;
import com.orient.padtemplate.base.recyclerview.RecyclerAdapter;
import com.orient.padtemplate.common.Common;
import com.orient.padtemplate.utils.GlideUtils;
import com.orient.padtemplate.utils.UIUtils;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 模块选择界面
 */
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
    private int aveWidth = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.module_activity;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        // 获取屏幕宽度
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        // Recycler leftMargin-10 rightMargin-30
        aveWidth = (width - UIUtils.dip2px(10 + 30)) / 4;

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
        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<ModuleItem>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder<ModuleItem> holder, ModuleItem moduleItem) {
                super.onItemClick(holder, moduleItem);

                switch (moduleItem.name) {
                    case "表格流程":
                        break;
                    case "事件流程":
                        break;
                    case "二维码": {
                        startActivity(new Intent(ModuleActivity.this, CreateQrCodeActivity.class));
                        break;
                    }
                    case "相机": {
                        startActivity(new Intent(ModuleActivity.this, CameraActivity.class));
                        break;
                    }
                    case "列表": {
                        startActivity(new Intent(ModuleActivity.this, ListActivity.class));
                        break;
                    }
                    case "表格": {
                        startActivity(new Intent(ModuleActivity.this, TableShowActivity.class));
                        break;
                    }
                    case "弹出框": {
                        startActivity(new Intent(ModuleActivity.this, DialogActivity.class));
                        break;
                    }
                    default:
                        break;
                }
            }
        });
        mModuleRv.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();

        List<ModuleItem> list = new LinkedList<>();
        list.add(new ModuleItem("表格流程", R.drawable.module_item_head_1));
        list.add(new ModuleItem("事件流程", R.drawable.module_item_head_2));
        list.add(new ModuleItem("二维码", R.drawable.module_item_head_3));
        list.add(new ModuleItem("相机", R.drawable.module_item_head_4));
        list.add(new ModuleItem("列表", R.drawable.module_item_head_5));
        list.add(new ModuleItem("表格", R.drawable.module_item_head_6));
        list.add(new ModuleItem("弹出框", R.drawable.module_item_head_7));
        list.add(new ModuleItem("其他", R.drawable.module_item_head_8));
        mAdapter.addAllData(list);
        mAdapter.notifyDataSetChanged();
    }



    @OnClick(R.id.tv_back)
    public void back() {
        onBackPressed();
    }


    class ModuleItem {
        String name;
        @DrawableRes
        int source;

        ModuleItem(String name, int source) {
            this.name = name;
            this.source = source;
        }
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<ModuleItem> {

        @BindView(R.id.lay_bg)
        View mBgView;

        @BindView(R.id.iv_content)
        ImageView mHeaderIv;

        @BindView(R.id.tv_name)
        TextView mNameTv;

        @BindView(R.id.cd_view)
        CardView mCardView;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(ModuleItem moduleItem) {
            if (aveWidth != -1) {
                mBgView.getLayoutParams().width = aveWidth;
                //mCardView.getLayoutParams().width = aveWidth;
            }

            GlideUtils.loadResource(ModuleActivity.this, moduleItem.source, mHeaderIv);
            mNameTv.setText(moduleItem.name);
        }
    }

}
