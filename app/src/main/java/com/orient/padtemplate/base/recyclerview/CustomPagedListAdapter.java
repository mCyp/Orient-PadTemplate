package com.orient.padtemplate.base.recyclerview;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orient.padtemplate.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基础的RecyclerAdapter
 *
 * Author WangJie
 * Created on 2018/8/27.
 */
@SuppressWarnings("ALL")
public abstract class CustomPagedListAdapter<Data> extends PagedListAdapter<Data,CustomPagedListAdapter.ViewHolder<Data>> {

    protected CustomPagedListAdapter(@NonNull DiffUtil.ItemCallback<Data> diffCallback) {
        super(diffCallback);
    }

    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建ViewHolder
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(viewType,parent,false);
        ViewHolder<Data> viewHolder = onCreateViewHolder(root,viewType);

        // 基础的操作
        root.setTag(R.id.recycler_view_holder,viewHolder);
        viewHolder.unbinder = ButterKnife.bind(viewHolder,root);

        return viewHolder;
    }

    /**
     *  实际的创建ViewHolder的方法
     */
    public abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        Data data = getItem(position);
        holder.bind(data);
    }

    @Override
    public int getItemViewType(int position) {
        Data data = getItem(position);
        return getItemLayout(data,position);
    }

    /**
     * 得到子布局的ID 适合多种子布局的情况下使用
     */
    public abstract int getItemLayout(Data data,int position);

    /**
     *  自定义的ViewHolder
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder{
        public Unbinder unbinder;
        protected Data mData;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(Data data){
            mData = data;
            onBind(data);
        }

        /**
         * 实现数据的绑定
         */
        protected abstract void onBind(Data data);
    }
}
