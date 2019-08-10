package com.orient.padtemplate.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.orient.padtemplate.R;
import com.orient.padtemplate.base.activity.BaseActivity;
import com.orient.padtemplate.base.recyclerview.RecyclerAdapter;
import com.orient.padtemplate.utils.DateUtils;
import com.orient.padtemplate.widget.dialog.NetDialog;
import com.orient.padtemplate.widget.dialog.SelectionDialog;
import com.orient.padtemplate.widget.suspension.Item;
import com.orient.padtemplate.widget.suspension.SuspensionDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogActivity extends BaseActivity {

    // 建议
    // SweetDialog 可以替换为其他Dialog

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

                switch (item.getName()) {
                    case "正常": {
                        createSweetDialog(SweetAlertDialog.NORMAL_TYPE, "提交", "确认提交吗", "确定", "取消");
                        break;
                    }
                    case "错误": {
                        createSweetDialog(SweetAlertDialog.ERROR_TYPE, "驳回", "确认驳回吗", "取消", "驳回");
                        break;
                    }
                    case "警告": {
                        createSweetDialog(SweetAlertDialog.WARNING_TYPE, "警告", "继续可能会触发异常", "继续", "取消");
                        break;
                    }
                    case "加载": {
                        createProgressDialog();
                        break;
                    }
                    case "时间选择": {
                        createDateDialog();
                        break;
                    }
                    case "多列联动": {
                        // 数据或者容器都行
                        createLinkDialog();
                        break;
                    }
                    case "多列不联动": {
                        // 数据或者容器都行
                        createNoLinkDialog();
                        break;
                    }
                    case "IP端口选择": {
                        createNetDialog();
                        break;
                    }
                    case "列表多选": {
                        createSelectionDialog();
                        break;
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

        List<Item> items = new ArrayList<>();
        // 通用样式
        items.add(new Item("正常", "通用样式"));
        items.add(new Item("错误", "通用样式"));
        items.add(new Item("警告", "通用样式"));
        items.add(new Item("加载", "通用样式"));
        // 滚轮样式
        items.add(new Item("时间选择", "滚轮样式"));
        items.add(new Item("多列联动", "滚轮样式"));
        items.add(new Item("多列不联动", "滚轮样式"));
        // 自定义
        items.add(new Item("IP端口选择", "自定义"));
        items.add(new Item("列表多选", "自定义"));

        SuspensionDecoration mDecoration = new SuspensionDecoration(this, mAdapter.getItems());
        mDialogRv.addItemDecoration(mDecoration);

        mAdapter.replace(items);
    }

    // 创建createSweetDialog
    private void createSweetDialog(int type, String title, String context, String confirmText, String cancelText) {
        SweetAlertDialog dialog = new SweetAlertDialog(DialogActivity.this, type);
        dialog.setTitleText(title)
                .setContentText(context)
                .setConfirmButton(confirmText, sweetAlertDialog -> {
                    // 设置事件
                    dialog.dismiss();
                })
                .setCancelButton(cancelText, sweetAlertDialog -> {
                    // 设置事件
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void createProgressDialog() {
        SweetAlertDialog dialog = new SweetAlertDialog(DialogActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("加载中");
        // .setContentText() 设置文本内容
        dialog.show();
    }

    /**
     * 创建日期底部框
     */
    private void createDateDialog() {
        TimePickerView pvTime = new TimePickerBuilder(DialogActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String str = DateUtils.date2NormalStr(date);
                showToast(str);
            }
        }).setSubmitColor(Color.parseColor("#673AB7"))
                .setCancelColor(Color.parseColor("#673AB7"))
                .setTitleText("时间选择")
                .build();
        pvTime.show();
    }

    /**
     * 创建关联的底部框
     */
    private void createLinkDialog() {
        List<String> first = new ArrayList<>();
        first.add("江苏");
        first.add("安徽");
        first.add("浙江");

        List<List<String>> second = new ArrayList<>();
        second.add(new ArrayList<>(Arrays.asList("南京", "无锡", "苏州", "南通", "盐城")));
        second.add(new ArrayList<>(Arrays.asList("合肥", "滁州")));
        second.add(new ArrayList<>(Arrays.asList("杭州", "宁波", "温州")));

        OptionsPickerView<String> optionsPickerView = new OptionsPickerBuilder(DialogActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String province = first.get(options1);
                String city = second.get(options1).get(options2);
                String content = province + "-" + city;
                showToast(content);
            }
        }).setTitleText("城市选择")
                .setSubmitColor(Color.parseColor("#673AB7"))
                .setCancelColor(Color.parseColor("#673AB7"))
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        optionsPickerView.setPicker(first, second);
        optionsPickerView.show();
    }

    /**
     * 创建不关联的底部框
     */
    private void createNoLinkDialog() {
        List<String> main = new ArrayList<>();
        main.add("红烧大排");
        main.add("油炸鸡柳");
        main.add("清蒸鲈鱼");

        List<String> second = new ArrayList<>();
        second.add("豆角炒肉");
        second.add("蒸蛋");
        second.add("番茄炒蛋");

        List<String> drinks = new ArrayList<>();
        drinks.add("红茶");
        drinks.add("酸奶");
        drinks.add("可乐");
        OptionsPickerBuilder pvNoLinkOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String str = "选择了：" + main.get(options1)
                    + "-" + second.get(options2)
                    + "-" + drinks.get(options3);
            showToast(str);
        }).setSubmitColor(Color.parseColor("#673AB7"))
                .setCancelColor(Color.parseColor("#673AB7"))
                .setTitleText("午饭套餐")
                .setSelectOptions(0, 1, 1); // 默认位置

        OptionsPickerView<String> foodView = pvNoLinkOptions.build();
        foodView.setNPicker(main, second, drinks);
        foodView.show();
    }

    /**
     * 创建自定义网络设置框
     */
    private void createNetDialog() {
        NetDialog dialog = new NetDialog(this, R.style.DialogTheme);
        dialog.show();
    }

    /**
     * 创建自定义选择框
     */
    private void createSelectionDialog() {
        SelectionDialog dialog = new SelectionDialog(this, R.style.DialogTheme);
        dialog.show();
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
