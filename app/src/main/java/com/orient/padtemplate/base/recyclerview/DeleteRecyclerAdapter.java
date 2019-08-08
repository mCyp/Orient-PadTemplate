package com.orient.padtemplate.base.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.orient.padtemplate.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 删除的适配器
 *
 * Author WangJie
 * Created on 2018/4/26.
 */

public abstract class DeleteRecyclerAdapter<Data> extends RecyclerView.Adapter<DeleteRecyclerAdapter.DeleteViewHolder<Data>> {
    private List<Data> mDataList;

    public DeleteRecyclerAdapter(List<Data> mDataList) {
        this.mDataList = mDataList;
    }

    public DeleteRecyclerAdapter() {
        mDataList = new ArrayList<>();
    }

    /**
     * 得到一个新的Viewholder
     *
     * @param root     根布局
     * @param viewType 布局类型 就是xml的Id
     * @return 返回一个Viewholder
     */
    protected abstract DeleteViewHolder<Data> onCreateViewHolder(View root, int viewType);

    @Override
    public DeleteViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        //得到LayoutId用于把xml事件初始化为View
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 将xml文件转化成root View
        View root = inflater.inflate(viewType,parent,false);
        // 通过子类必须实现的一个方法，得到一个ViewHolder
        DeleteViewHolder<Data> viewHolder = onCreateViewHolder(root,viewType);

        // 对Tag进行双向绑定
        root.setTag(R.id.recycler_view_holder,viewHolder);

        // 进行界面注释的绑定
        viewHolder.unbinder = ButterKnife.bind(viewHolder,root);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeleteViewHolder<Data> holder, int position) {
        // 得到需要绑定的数据
        Data data = mDataList.get(position);
        // 绑定数据
        holder.bind(data);
    }

    /**
     * 获取资源文件id
     * @param data 数据
     * @param position 位置
     * @return 资源文件的id
     */
    protected abstract int getItemViewType(Data data,int position);

    @Override
    public int getItemViewType(int position) {
        Data data = mDataList.get(position);
        return getItemViewType(data,position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<Data> getItems(){
        return mDataList;
    }

    /**
     * 添加一个数据
     *
     * @param data Data
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 清除所有的数据
     */
    public void clear() {
        mDataList.clear();
        // 通知刷新
        notifyDataSetChanged();
    }

    /**
     * 添加一个数据的集合
     *
     * @param dataList 数组
     */
    public void addAll(Data... dataList){
        if(dataList != null && dataList.length >0){
            int startPos = mDataList.size();
            Collections.addAll(mDataList,dataList);
            notifyItemRangeChanged(startPos,dataList.length);
        }
    }

    /**
     * 添加一个List的数据
     *
     * @param dataList List
     */
    public void addAll(Collection<Data> dataList){
        if(dataList != null && dataList.size() >0){
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeChanged(startPos,dataList.size());
        }
    }

    /**
     * 整个集合替换成了一个新的集合，其中包括了清空
     *
     * @param dataList Collection
     */
    public void replace(Collection<Data> dataList){
        mDataList.clear();
        if(dataList != null && dataList.size() != 0){
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }


    public static abstract class DeleteViewHolder<Data> extends RecyclerAdapter.ViewHolder<Data> {

        // 显示的View
        public LinearLayout itemLayout;
        // 删除的View
        public FrameLayout itemDelete;

        public DeleteViewHolder(View itemView) {
            super(itemView);

            itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
            itemDelete = (FrameLayout) itemView.findViewById(R.id.item_delete);
        }


    }

}
