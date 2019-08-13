package com.orient.padtemplate.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.orient.padtemplate.R;
import com.orient.padtemplate.base.app.App;
import com.orient.padtemplate.base.listener.TextWatcherListener;
import com.orient.padtemplate.base.recyclerview.RecyclerAdapter;
import com.orient.padtemplate.common.Common;
import com.orient.padtemplate.core.data.db.Cell;
import com.orient.padtemplate.core.data.repository.TaskRepository;
import com.orient.padtemplate.utils.DateUtils;
import com.orient.padtemplate.utils.PhotoUtils;
import com.orient.padtemplate.utils.StringsUtils;
import com.orient.padtemplate.utils.UIUtils;
import com.orient.padtemplate.widget.dialog.DetailTextDialog;
import com.orient.padtemplate.widget.scroll.PanelAdapter;
import com.orient.photopagerview.listener.DeleteListener;
import com.orient.photopagerview.widget.IPhotoPager;
import com.orient.photopagerview.widget.PhotoPagerViewProxy;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 表格的适配器
 * <p>
 * Author WangJie
 * Created on 2018/4/12.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class GridTableAdapter extends PanelAdapter {

    public static final int TYPE_TAKE_PHOTO = 100;

    private LayoutInflater mLayoutInflater;
    // 标题的cell
    private List<Cell> titles, contents;
    private Context mContext;
    // 当前是否是可以编辑的状态
    private boolean isEdit;
    private int row, col;
    private Fragment mFragment;
    // 每个cell的平均宽度
    private int aveWidth;
    private HashMap<String, PhotoObserver> map = new HashMap<>();
    private String takePhotoCellId;

    public GridTableAdapter() {
    }

    public GridTableAdapter(List<Cell> titles, List<Cell> contents, Context context, boolean isEdit, int screenWidth
            , Fragment fragment) {
        this.titles = titles;
        this.contents = contents;
        this.mContext = context;
        this.isEdit = isEdit;
        this.mFragment = fragment;

        init(screenWidth);
    }

    public List<Cell> getTitles() {
        return titles;
    }

    public List<Cell> getContents() {
        return contents;
    }

    // 基础的初始化
    private void init(int screenWidth) {
        mLayoutInflater = LayoutInflater.from(mContext);
        row = (titles.size() + contents.size()) / titles.size();
        col = titles.size();
        aveWidth = Math.max((screenWidth - UIUtils.dip2px(mContext, 40)) / titles.size(), UIUtils.dip2px(mContext, 200));
    }

    public List<Cell> getItems() {
        return contents;
    }

    @Override
    public int getRowCount() {
        return titles == null ? 0 : row;
    }

    @Override
    public int getColumnCount() {
        return col;
    }

    public void notifyPhotoChanged() {
        PhotoObserver observer = map.get(takePhotoCellId);
        if (observer != null) {
            observer.onNumberChanged();
        }
    }

    @Override
    public int getItemViewType(int row, int column) {
        if (row == 0) {
            return R.layout.grid_table_title_panel_item;
        } else {
            Cell cell = contents.get((row - 1) * col + column);
            switch (cell.getType()) {
                case Cell.CELL_DESC:
                    return R.layout.grid_table_desc_panel_item;
                case Cell.CELL_CHECK:
                    return R.layout.grid_table_check_panel_item;
                case Cell.CELL_EDIT:
                    return R.layout.grid_table_edit_panel_item;
                case Cell.CELL_DATE:
                    return R.layout.grid_table_date_panel_item;
                case Cell.CELL_SELECTION:
                    return R.layout.grid_table_selection_panel_item;
                case Cell.CELL_PHOTO:
                    return R.layout.grid_table_photo_panel_item;
                default:
                    throw new UnsupportedOperationException("不支持的类型！");
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
        if (row == 0) {
            Cell title = titles.get(column);
            ((GridTableAdapter.DescHolder) holder).onBind(title);
        } else {
            int pos = column + (row - 1) * getColumnCount();
            Cell content = contents.get(pos);

            if (holder instanceof DescHolder) {
                ((DescHolder) holder).onBind(content);
            } else if (holder instanceof CheckHolder) {
                ((CheckHolder) holder).onBind(content);
            } else if (holder instanceof EditHolder) {
                ((EditHolder) holder).onBind(content);
            } else if (holder instanceof PhotoHolder) {
                ((PhotoHolder) holder).onBind(content);
            } else if (holder instanceof DateHolder) {
                ((DateHolder) holder).onBind(content);
            } else {
                ((SelectionHolder) holder).onBind(content);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, @LayoutRes int viewType) {
        View root = mLayoutInflater.inflate(viewType, parent, false);
        RecyclerAdapter.ViewHolder<Cell> holder;
        switch (viewType) {
            case R.layout.grid_table_desc_panel_item:
            case R.layout.grid_table_title_panel_item:
                holder = new DescHolder(root);
                ((GridTableAdapter.DescHolder) holder).unbinder = ButterKnife.bind(holder, root);
                break;
            case R.layout.grid_table_check_panel_item:
                holder = new CheckHolder(root);
                ((GridTableAdapter.CheckHolder) holder).unbinder = ButterKnife.bind(holder, root);
                break;
            case R.layout.grid_table_edit_panel_item:
                holder = new EditHolder(root);
                ((GridTableAdapter.EditHolder) holder).unbinder = ButterKnife.bind(holder, root);
                break;
            case R.layout.grid_table_date_panel_item:
                holder = new DateHolder(root);
                ((GridTableAdapter.DateHolder) holder).unbinder = ButterKnife.bind(holder, root);
                break;
            case R.layout.grid_table_selection_panel_item:
                holder = new SelectionHolder(root);
                ((GridTableAdapter.SelectionHolder) holder).unbinder = ButterKnife.bind(holder, root);
                break;
            default:
                holder = new PhotoHolder(root);
                ((GridTableAdapter.PhotoHolder) holder).unbinder = ButterKnife.bind(holder, root);
                break;
        }
        return holder;
    }

    /**
     * 描述文本Holder
     */
    class DescHolder extends RecyclerAdapter.ViewHolder<Cell> {
        @BindView(R.id.tv_desc)
        TextView mContent;

        @BindView(R.id.lay)
        View layout;

        DescHolder(View itemView) {
            super(itemView);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onBind(final Cell cell) {
            layout.getLayoutParams().width = aveWidth;
            if (cell.getIsTitle()) {
                mContent.setText(cell.getLabelName());
            } else {
                mContent.setText(cell.getLabelName());
            }

            // 文本内容不够的时候处理
            float height = StringsUtils.getTotalHeight(aveWidth, 2, 14, mContent, cell.getLabelName());
            float limitHeight = UIUtils.dip2px(mContext, 100);
            if (height > limitHeight) {
                String simpleStr = StringsUtils.getSimpleString(aveWidth, mContent, cell.getLabelName());
                mContent.setText(simpleStr);
                mContent.setOnClickListener(v -> {
                    // 弹出框
                    DetailTextDialog dialog = new DetailTextDialog(mContext, R.style.DialogTheme);
                    dialog.setContent(cell.getLabelName());
                    dialog.show();
                });
            }
        }
    }

    /**
     * CheckHolder
     */
    @SuppressWarnings("ConstantConditions")
    class CheckHolder extends RecyclerAdapter.ViewHolder<Cell> {

        @BindView(R.id.lay)
        View layout;

        @BindView(R.id.cb_content)
        CheckBox mCheckYes;

        CheckHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Cell cell) {
            layout.getLayoutParams().width = aveWidth;

            String value = cell.getInputValue();
            if (!TextUtils.isEmpty(value)) {
                mCheckYes.setChecked(Boolean.parseBoolean(value));
            }
            mCheckYes.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    cell.setInputValue("true");
                } else {
                    cell.setInputValue("false");
                }
            });
        }
    }

    /**
     * 文本编辑Holder
     */
    @SuppressWarnings("ConstantConditions")
    class EditHolder extends RecyclerAdapter.ViewHolder<Cell> {
        @BindView(R.id.et_content)
        EditText mEdit;

        @BindView(R.id.lay)
        View layout;

        public EditHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final Cell cell) {
            layout.getLayoutParams().width = aveWidth;

            String value = cell.getInputValue();
            if (!TextUtils.isEmpty(value)) {
                mEdit.setText(value);
            } else {
                mEdit.setText("");
            }
            mEdit.addTextChangedListener(new TextWatcherListener() {
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);

                    String content = mEdit.getText().toString();
                    cell.setInputValue(content);
                }
            });

            if (!isEdit) {
                mEdit.setEnabled(isEdit);
            }
        }
    }

    /**
     * 日期Holder
     */
    class DateHolder extends RecyclerAdapter.ViewHolder<Cell> {
        @BindView(R.id.tv_time)
        TextView mTime;

        public DateHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Cell cell) {
            String time = cell.getInputValue();
            if (!TextUtils.isEmpty(time))
                mTime.setText(time);

            mTime.setOnClickListener(v -> {
                TimePickerView pvTime = new TimePickerBuilder(mContext, (date, v1) -> {
                    String str = DateUtils.date2dayStr(date);
                    mTime.setText(str);
                    cell.setInputValue(str);
                }).setSubmitColor(Color.parseColor("#673AB7"))
                        .setCancelColor(Color.parseColor("#673AB7"))
                        .setTitleText("时间选择")
                        .build();
                pvTime.show();
            });
        }
    }

    /**
     * 选择列表Holder
     */
    class SelectionHolder extends RecyclerAdapter.ViewHolder<Cell> {

        @BindView(R.id.tv_list)
        TextView mList;
        private OptionsPickerView<String> pvCustomOptions = null;

        public SelectionHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Cell cell) {
            String context = cell.getInputValue();
            // 项目不可这么写
            String[] array = new String[]{"螺丝帽", "扳手", "军刀", "锤子", "螺丝刀", "电灯"};

            mList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pvCustomOptions = new OptionsPickerBuilder(mContext,
                            new OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                                    //返回的分别是三个级别的选中位置
                                    String tx = array[options1];
                                    mList.setText(tx);
                                    cell.setInputValue(tx);
                                }
                            })
                            .setLayoutRes(R.layout.pickerview_custom_options_view, new CustomListener() {
                                @Override
                                public void customLayout(View v) {
                                    Button tvSubmit = v.findViewById(R.id.mBtnYes);
                                    Button tvCancel = v.findViewById(R.id.mBtnNo);
                                    tvSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            pvCustomOptions.returnData();
                                            pvCustomOptions.dismiss();
                                        }
                                    });

                                    tvCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            pvCustomOptions.dismiss();
                                        }
                                    });
                                }
                            })
                            .isDialog(true)
                            .setOutSideCancelable(false)
                            .build();
                    pvCustomOptions.setPicker(Arrays.asList(array));//添加数据
                    pvCustomOptions.show();
                }
            });
        }
    }

    /**
     * 图片Holder
     */
    class PhotoHolder extends RecyclerAdapter.ViewHolder<Cell>
            implements PhotoObserver {

        // 角标
        Badge badge;
        @BindView(R.id.iv_camera)
        ImageView mCamera;
        @BindView(R.id.iv_photo)
        ImageView mPhoto;
        @BindView(R.id.lay)
        View layout;

        private String mPath;

        PhotoHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final Cell cell) {
            layout.getLayoutParams().width = aveWidth;
            String id = cell.getId();
            if (!map.containsKey(id)) {
                map.put(id, this);
            }

            mPath = cell.getPath();

            mCamera.setOnClickListener(v -> {
                if (TextUtils.isEmpty(mPath)) {
                    mPath = PhotoUtils.getTableStoragePath(cell.getTableId(), id);
                    File file = new File(mPath);
                    if (!file.exists())
                        file.mkdirs();
                    cell.setPath(mPath);
                    // 更新Cell
                    new TaskRepository(App.getInstance().getDaoSession()).insertCell(cell);
                }
                String path = mPath;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = PhotoUtils.getMediaUriFromFile(path);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                takePhotoCellId = id;
                mFragment.startActivityForResult(intent, TYPE_TAKE_PHOTO);
            });
            mPhoto.setOnClickListener(v -> {
                // 查询图片
                String path = cell.getPath();
                List<Bitmap> bitmaps = PhotoUtils.getAlbumByPath(path, Common.Constant.PHOTO_NAME_SUFFIX);
                if (bitmaps == null || bitmaps.size() == 0) {
                    return;
                }
                IPhotoPager pageView = new PhotoPagerViewProxy.Builder(mFragment.getActivity())
                        .addBitmaps(bitmaps)// 添加图片
                        .showDelete(true)// 是否删除图片按钮 普通主题特有
                        .setDeleteListener(new DeleteListener() {
                            @Override
                            public void onDelete(int position) {
                                // 自定义删除逻辑
                            }
                        })
                        .showAnimation(true)// 是否显示开始和退出动画
                        .setAnimationType(PhotoPagerViewProxy.ANIMATION_SCALE_ALPHA)// 动画类型
                        .setStartPosition(0)// 设置初始位置
                        .create();
                pageView.show();
            });

            numberChanged();
        }

        @Override
        public void onNumberChanged() {
            numberChanged();
        }

        private void numberChanged() {
            if (TextUtils.isEmpty(mPath) || !new File(mPath).exists())
                return;
            int count = PhotoUtils.getAlbumCountByPath(mPath, Common.Constant.PHOTO_NAME_SUFFIX);
            //Log.e(TAG, mData.getId() + ":" + count);
            if (count > 0) {
                if (badge != null) {
                    badge.setBadgeNumber(count);
                } else {
                    badge = new QBadgeView(mContext)
                            .bindTarget(mPhoto)
                            .setBadgeNumber(count)
                            .setBadgeGravity(Gravity.END | Gravity.TOP)
                            .setShowShadow(true);
                }
                mPhoto.requestLayout();
            }
        }
    }

    // 照片的监听器
    public interface PhotoObserver {
        void onNumberChanged();
    }
}
