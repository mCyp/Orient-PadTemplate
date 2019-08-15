package com.orient.padtemplate.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.orient.padtemplate.R;
import com.orient.padtemplate.base.app.App;
import com.orient.padtemplate.base.fragment.BaseMvpFragment;
import com.orient.padtemplate.base.listener.TextWatcherListener;
import com.orient.padtemplate.base.recyclerview.CustomPagedListAdapter;
import com.orient.padtemplate.common.Common;
import com.orient.padtemplate.contract.presenter.LinearTablePresenter;
import com.orient.padtemplate.contract.view.LinearTableView;
import com.orient.padtemplate.core.data.db.Cell;
import com.orient.padtemplate.core.data.paging.CellPageDataSource;
import com.orient.padtemplate.core.data.repository.TaskRepository;
import com.orient.padtemplate.ui.adapter.GridTableAdapter;
import com.orient.padtemplate.utils.DateUtils;
import com.orient.padtemplate.utils.PhotoUtils;
import com.orient.padtemplate.utils.StringsUtils;
import com.orient.padtemplate.utils.UIUtils;
import com.orient.padtemplate.widget.dialog.DetailTextDialog;
import com.orient.padtemplate.widget.placeholder.EmptyView;
import com.orient.padtemplate.widget.suspension.SuspensionDecoration;
import com.orient.photopagerview.widget.IPhotoPager;
import com.orient.photopagerview.widget.PhotoPagerViewProxy;

import net.qiujuer.genius.ui.widget.CheckBox;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 直线表格布局
 * 1. 表格布局 RecyclerView
 * 2. 分页策略 Android Jetpack Paging库
 * create an instance of this fragment.
 */
@SuppressWarnings("ConstantConditions")
@SuppressLint("SetTextI18n")
public class LinearTableFragment extends BaseMvpFragment<LinearTablePresenter>
        implements LinearTableView {

    public static final int TYPE_TAKE_PHOTO = 200;

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private CustomPagedListAdapter<Cell> mAdapter;
    // 当前的列数
    private boolean isEdit = true;
    private HashMap<String, GridTableAdapter.PhotoObserver> map = new HashMap<>();
    private String takePhotoCellId;
    // 分隔线
    private SuspensionDecoration mDecoration;

    @Override
    protected int getLayoutId() {
        return R.layout.linear_table_fragment;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new CustomPagedListAdapter<Cell>(new CellDiffCallback()) {
            @Override
            public ViewHolder<Cell> onCreateViewHolder(View root, int viewType) {
                switch (viewType) {
                    case R.layout.linear_table_recycle_item_desc:
                        return new DescHolder(root);
                    case R.layout.linear_table_recycle_item_edit:
                        return new EditHolder(root);
                    case R.layout.linear_table_recycle_item_check:
                        return new CheckHolder(root);
                    case R.layout.linear_table_recycle_item_date:
                        return new DateHolder(root);
                    case R.layout.linear_table_recycle_item_selection:
                        return new SelectionHolder(root);
                    case R.layout.linear_table_recycle_item_camrea:
                        return new PhotoHolder(root);
                    default:
                        throw new UnsupportedOperationException("不支持的类型！");
                }
            }

            @Override
            public int getItemLayout(Cell cell, int position) {
                switch (cell.getType()) {
                    case Cell.CELL_DESC:
                        return R.layout.linear_table_recycle_item_desc;
                    case Cell.CELL_CHECK:
                        return R.layout.linear_table_recycle_item_check;
                    case Cell.CELL_EDIT:
                        return R.layout.linear_table_recycle_item_edit;
                    case Cell.CELL_DATE:
                        return R.layout.linear_table_recycle_item_date;
                    case Cell.CELL_SELECTION:
                        return R.layout.linear_table_recycle_item_selection;
                    case Cell.CELL_PHOTO:
                        return R.layout.linear_table_recycle_item_camrea;
                    default:
                        throw new UnsupportedOperationException("不支持的类型！");
                }
            }
        });

        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();

        // 加载
        mPlaceHolderView.triggerLoading();
        mPresenter.loadCells("1-1-2");
    }

    @Override
    public void onLoadCellsResult(PagedList<Cell> cells) {
        if (cells.size() > 0) {
            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    if (mDecoration != null)
                        mDecoration.setmDatas(mAdapter.getCurrentList().snapshot());
                }
            });
            mAdapter.submitList(cells);
            mDecoration = new SuspensionDecoration(getContext(), mAdapter.getCurrentList().snapshot());
            mRecyclerView.addItemDecoration(mDecoration);
        }
        mEmptyView.triggerOkOrEmpty(cells.size() > 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TYPE_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                GridTableAdapter.PhotoObserver observer = map.get(takePhotoCellId);
                if (observer != null) {
                    observer.onNumberChanged();
                }
            }
        }
    }

    class CellDiffCallback extends DiffUtil.ItemCallback<Cell> {
        @Override
        public boolean areItemsTheSame(@NonNull Cell cell, @NonNull Cell t1) {
            return cell.getId().equals(t1.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Cell cell, @NonNull Cell t1) {
            return cell.getInputValue().equals(t1.getInputValue())
                    && cell.getLabelName().equals(t1.getLabelName())
                    && cell.getType() == t1.getType()
                    && cell.getId().equals(t1.getId());
        }
    }

    /**
     * 文本Holder
     */
    class DescHolder extends CustomPagedListAdapter.ViewHolder<Cell> {

        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.tv_content)
        TextView mContent;
        @BindView(R.id.lay_bg)
        ConstraintLayout layout;

        DescHolder(View itemView) {
            super(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onBind(Cell cell) {
            mTitle.setText(cell.getLabelName() + ":");
            mContent.setText(cell.getInputValue());

            mContent.post(() -> {
                // 文本内容不够的时候处理
                float height = StringsUtils.getTotalHeight(mContent.getMeasuredWidth(), 2, 14, mContent, cell.getInputValue());
                float limitHeight = UIUtils.dip2px(getContext(), 72);
                if (height > limitHeight) {
                    String simpleStr = StringsUtils.getSimpleString(mContent.getWidth(), mContent, cell.getInputValue());
                    mContent.setText(simpleStr);
                    mContent.setOnClickListener(v -> {
                        // 弹出框
                        DetailTextDialog dialog = new DetailTextDialog(getContext(), R.style.DialogTheme);
                        dialog.setContent(cell.getInputValue());
                        dialog.show();
                    });
                }
            });

        }
    }

    /**
     * 编辑的Holder
     */
    class EditHolder extends CustomPagedListAdapter.ViewHolder<Cell> {

        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.et_content)
        TextView mEditContent;

        EditHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final Cell cell) {
            mTitle.setText(cell.getLabelName() + ":");
            mEditContent.setText(cell.getInputValue());

            mEditContent.addTextChangedListener(new TextWatcherListener() {
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);

                    cell.setInputValue(mEditContent.getText().toString());
                }
            });

            if (!isEdit) {
                mEditContent.setEnabled(false);
            }
        }
    }

    /**
     * 检查框的Holder
     */
    @SuppressWarnings("WeakerAccess")
    class CheckHolder extends CustomPagedListAdapter.ViewHolder<Cell> {

        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.tv_yes)
        CheckBox mYes;
        @BindView(R.id.tv_no)
        CheckBox mNo;

        CheckHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final Cell cell) {
            mTitle.setText(cell.getLabelName() + ":");
            // 设置选中的状态
            if (cell.getInputValue().equals("false")) {
                setValue(false, true);
            } else {
                setValue(true, false);
                cell.setInputValue("true");
            }

            if (!isEdit) {
                mYes.setEnabled(false);
                mNo.setEnabled(false);
            }
        }

        @OnCheckedChanged(R.id.tv_yes)
        public void onYesClick() {
            if (mYes.isChecked()) {
                mNo.setChecked(false);
                mData.setInputValue("true");
            }
        }

        @OnCheckedChanged(R.id.tv_no)
        public void onNoClick() {
            if (mNo.isChecked()) {
                mYes.setChecked(false);
                mData.setInputValue("false");
            }
        }

        /**
         * 设置状态
         */
        private void setValue(boolean yes, boolean no) {
            mYes.setChecked(yes);
            mNo.setChecked(no);
        }
    }

    /**
     * 照片的Holder
     */
    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    class PhotoHolder extends CustomPagedListAdapter.ViewHolder<Cell> implements GridTableAdapter.PhotoObserver {

        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.tv_camera)
        TextView mCamera;
        @BindView(R.id.tv_photo)
        TextView mPhoto;

        private String mPath;
        private Badge badge;

        PhotoHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final Cell cell) {
            String id = cell.getId();
            if (!map.containsKey(id)) {
                map.put(id, this);
            }
            mPath = cell.getPath();
            mTitle.setText(cell.getLabelName());

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
                startActivityForResult(intent, TYPE_TAKE_PHOTO);
            });
            mPhoto.setOnClickListener(v -> {
                // 查询图片
                String path = cell.getPath();
                if (TextUtils.isEmpty(path) || !new File(path).exists())
                    return;
                List<Bitmap> bitmaps = PhotoUtils.getAlbumByPath(path, Common.Constant.PHOTO_NAME_SUFFIX);
                if (bitmaps == null || bitmaps.size() == 0) {
                    return;
                }
                IPhotoPager pageView = new PhotoPagerViewProxy.Builder(getActivity())
                        .addBitmaps(bitmaps)// 添加图片
                        .showDelete(true)// 是否删除图片按钮 普通主题特有
                        .setDeleteListener(position -> {
                            // 自定义删除逻辑
                        })
                        .showAnimation(true)// 是否显示开始和退出动画
                        .setAnimationType(PhotoPagerViewProxy.ANIMATION_SCALE_ALPHA)// 动画类型
                        .setStartPosition(0)// 设置初始位置
                        .create();
                pageView.show();
            });

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
                    badge = new QBadgeView(getContext())
                            .bindTarget(mPhoto)
                            .setBadgeNumber(count)
                            .setBadgeGravity(Gravity.END | Gravity.TOP)
                            .setShowShadow(true);
                }
                mPhoto.requestLayout();
            }
        }

        @Override
        public void onNumberChanged() {
            numberChanged();
        }
    }

    /**
     * 日期Holder
     */
    class DateHolder extends CustomPagedListAdapter.ViewHolder<Cell> {
        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.tv_date)
        TextView mDateTv;
        @BindView(R.id.tv_year)
        TextView mYearTv;
        @BindView(R.id.tv_month)
        TextView mMonthTv;

        DateHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Cell cell) {
            mTitle.setText(cell.getLabelName());
            String time = cell.getInputValue();
            if (!TextUtils.isEmpty(time))
                setText(new Date());
            else {
                setText(DateUtils.str2DayDate(time));
            }

            mDateTv.setOnClickListener(v -> {
                TimePickerView pvTime = new TimePickerBuilder(getContext(), (date, v1) -> {
                    setText(date);
                    cell.setInputValue(DateUtils.date2NormalStr(date));
                }).setSubmitColor(Color.parseColor("#673AB7"))
                        .setCancelColor(Color.parseColor("#673AB7"))
                        .setTitleText("时间选择")
                        .build();
                pvTime.show();
            });
        }

        private void setText(Date date) {
            mDateTv.setText(DateUtils.date2MM_dd(date));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            mYearTv.setText(Integer.toString(calendar.get(Calendar.YEAR)));
            mMonthTv.setText(DateUtils.num2dayWeek(calendar.get(Calendar.DAY_OF_WEEK)));
        }
    }

    /**
     * 选择列表Holder
     */
    class SelectionHolder extends CustomPagedListAdapter.ViewHolder<Cell> {

        @BindView(R.id.tv_selection)
        TextView mList;
        @BindView(R.id.tv_title)
        TextView mTitle;

        private OptionsPickerView<String> pvCustomOptions = null;

        SelectionHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Cell cell) {
            mTitle.setText(cell.getLabelName());
            String content = cell.getInputValue();
            if (!TextUtils.isEmpty(content)) {
                mList.setText(content);
            }
            // 项目不可这么写
            String[] array = new String[]{"螺丝帽", "扳手", "军刀", "锤子", "螺丝刀", "电灯"};

            mList.setOnClickListener(v -> {
                pvCustomOptions = new OptionsPickerBuilder(getContext(),
                        (options1, options2, options3, v1) -> {
                            //返回的分别是三个级别的选中位置
                            String tx = array[options1];
                            mList.setText(tx);
                            cell.setInputValue(tx);
                        })
                        .setLayoutRes(R.layout.pickerview_custom_options_view, v12 -> {
                            Button tvSubmit = v12.findViewById(R.id.mBtnYes);
                            Button tvCancel = v12.findViewById(R.id.mBtnNo);
                            tvSubmit.setOnClickListener(v121 -> {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            });

                            tvCancel.setOnClickListener(v1212 -> pvCustomOptions.dismiss());
                        })
                        .isDialog(true)
                        .setOutSideCancelable(false)
                        .build();
                pvCustomOptions.setPicker(Arrays.asList(array));//添加数据
                pvCustomOptions.show();
            });
        }
    }
}
