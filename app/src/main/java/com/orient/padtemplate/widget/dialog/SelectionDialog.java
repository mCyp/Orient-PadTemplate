package com.orient.padtemplate.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orient.padtemplate.R;
import com.orient.padtemplate.base.recyclerview.RecyclerAdapter;
import com.orient.padtemplate.common.Common;
import com.orient.padtemplate.utils.AppPrefUtils;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 自定义设置的Dialog
 *
 * @Auther WangJie on 2018/2/2.
 */

@SuppressWarnings({"RegExpRedundantEscape", "ConstantConditions"})
public class SelectionDialog extends Dialog {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter<SelectionItem> mAdapter;

    public SelectionDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_dialog);

        initWidget();
        initData();
        initWindow();
    }

    // 初始化控件
    private void initWidget() {
        Button mSure = findViewById(R.id.btn_yes);
        Button mNo = findViewById(R.id.btn_no);
        mRecyclerView = findViewById(R.id.rv_content);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<SelectionItem>() {
            @Override
            public ViewHolder<SelectionItem> onCreateViewHolder(View root, int viewType) {
                return new SelectionDialog.ViewHolder(root);
            }

            @Override
            public int getItemLayout(SelectionItem selectionItem, int position) {
                return R.layout.selection_recycle_item;
            }
        });

        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<SelectionItem>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder<SelectionItem> holder, SelectionItem selectionItem) {
                super.onItemClick(holder, selectionItem);

                selectionItem.setSelect(!selectionItem.isSelect());
                mAdapter.notifyItemChanged(holder.getAdapterPosition());
            }
        });


        mNo.setOnClickListener(v -> {
            dismiss();
        });

        mSure.setOnClickListener(v -> {
            List<SelectionItem> items = mAdapter.getItems();
            String result = "";
            for (SelectionItem item : items) {
                if (item.isSelect()) {
                    if (TextUtils.isEmpty(result))
                        result += item.getName();
                    else
                        result += "-" + item.getName();
                }
            }
            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    private void initData() {
        List<SelectionItem> list = new ArrayList<>();
        list.add(new SelectionItem("选项一", false));
        list.add(new SelectionItem("选项二", false));
        list.add(new SelectionItem("选项三", false));
        list.add(new SelectionItem("选项四", false));
        list.add(new SelectionItem("选项五", false));
        list.add(new SelectionItem("选项六", false));
        mAdapter.replace(list);
    }

    private void initWindow() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.35);
        lp.height = (int) (d.heightPixels * 0.5);
        dialogWindow.setAttributes(lp);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<SelectionItem> {

        @BindView(R.id.tv_name)
        TextView mNameTv;
        @BindView(R.id.cb_name)
        CheckBox mNameCbx;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(SelectionItem selectionItem) {
            mNameTv.setText(selectionItem.getName());
            mNameCbx.setChecked(selectionItem.isSelect());

            //mNameCbx.setOnCheckedChangeListener((buttonView, isChecked) -> selectionItem.setSelect(isChecked));
        }
    }
}
