package com.example.zf.recyclerview.adapters;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * 可以添加头部和底部的Adapter
 */

public class WrapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "WrapRecyclerAdapter";

    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFooterViews;

    //基本的头部类型开始位置 用于ViewType
    private static int BASE_ITEM_TYPE_HEADER = 10000000;
    //基本的底部类型开始位置 用于ViewType
    private static int BASE_ITEM_TYPE_FOOTER = 20000000;

    private RecyclerView.Adapter mAdapter;

    public WrapRecyclerAdapter(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isHeaderViewType(viewType)) {
            View headerView = mHeaderViews.get(viewType);
            return createHeaderFooterViewHolder(headerView);
        }

        if (isFooterViewType(viewType)) {
            View headerView = mFooterViews.get(viewType);
            return createHeaderFooterViewHolder(headerView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    /**
     * 创建头部或底部的ViewHolder
     *
     * @param headerView
     * @return
     */
    private RecyclerView.ViewHolder createHeaderFooterViewHolder(View headerView) {
        return new RecyclerView.ViewHolder(headerView) {
        };
    }

    /**
     * 是不是底部类型
     *
     * @param viewType
     * @return
     */
    private boolean isFooterViewType(int viewType) {
        int position = mFooterViews.indexOfKey(viewType);//查看键所在的位置  没有返回负数
        return position >= 0;
    }

    /**
     * 是不是头部类型
     *
     * @param viewType
     * @return
     */
    private boolean isHeaderViewType(int viewType) {
        int position = mHeaderViews.indexOfKey(viewType);
        return position >= 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPosition(position) || isFooterViewPosition(position)) {
            return;
        }
        position = position - mHeaderViews.size();
        mAdapter.onBindViewHolder(holder, position);



    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPosition(position)) {
            //直接返回position位置的Key
            return mHeaderViews.keyAt(position);//查看第几个位置的值

        } else if (isFooterViewPosition(position)) {
            //直接返回position位置的Key
            position = position - mHeaderViews.size() - mAdapter.getItemCount();
            return mFooterViews.keyAt(position);
        }
        position = position - mHeaderViews.size();
        return mAdapter.getItemViewType(position);
    }

    /**
     * 是不是底部位置
     *
     * @param position
     * @return
     */
    private boolean isFooterViewPosition(int position) {
        return position >= (mHeaderViews.size() + mAdapter.getItemCount());
    }

    /**
     * 是不是头部位置
     *
     * @param position
     * @return
     */
    private boolean isHeaderViewPosition(int position) {
        return position < mHeaderViews.size();
    }

    @Override
    public int getItemCount() {
        //adapter条数 + 头部数 + 底部数 = 总条目数
        return mHeaderViews.size() + mFooterViews.size() + mAdapter.getItemCount();
    }

    private RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    /**
     * 添加头部
     *
     * @param view
     */
    public void addHeaderView(View view) {
        int position = mHeaderViews.indexOfValue(view);//查看值所在的位置  没有返回-1
        if (position < 0) {
            mHeaderViews.put(BASE_ITEM_TYPE_HEADER++, view);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加底部
     *
     * @param view
     */
    public void addFooterView(View view) {
        int position = mFooterViews.indexOfValue(view);
        if (position < 0) {
            mFooterViews.put(BASE_ITEM_TYPE_FOOTER++, view);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除头部
     *
     * @param view
     */
    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        if (index < 0) return;
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    /**
     * 移除底部
     *
     * @param view
     */
    public void removeFooterView(View view) {
        int index = mFooterViews.indexOfValue(view);
        if (index < 0) return;
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }

    public void adjustSpanSize(RecyclerView recyclerView) {
        if (recyclerView != null && recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter = isHeaderViewPosition(position) || isFooterViewPosition(position);
                    return isHeaderOrFooter ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

}
