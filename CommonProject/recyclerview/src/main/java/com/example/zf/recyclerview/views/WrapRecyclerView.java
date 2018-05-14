package com.example.zf.recyclerview.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zf.recyclerview.R;
import com.example.zf.recyclerview.adapters.WrapRecyclerAdapter;


/**
 * 可以添加头部和底部的recyclerView
 */

public class WrapRecyclerView extends RecyclerView {

    //包裹了一层头部和底部的Adapter
    private WrapRecyclerAdapter mWrapRecyclerAdapter;
    //列表数据的Adapter
    private Adapter mAdapter;
    private View blankView;
    protected boolean isAddBlankView;
    protected boolean isBlankViewGone;

    public WrapRecyclerView(Context context) {
        super(context);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        //为了防止多次设置Adapter
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
            mAdapter = null;
        }

        this.mAdapter = adapter;

        if (mAdapter instanceof WrapRecyclerAdapter) {
            mWrapRecyclerAdapter = (WrapRecyclerAdapter) adapter;
        } else {
            mWrapRecyclerAdapter = new WrapRecyclerAdapter(adapter);
        }

        super.setAdapter(mWrapRecyclerAdapter);

        //注册一个观察者
        mAdapter.registerAdapterDataObserver(mDataObserver);
        // 解决GridLayout添加头部和底部也要占据一行
        mWrapRecyclerAdapter.adjustSpanSize(this);
        if (blankView == null) {
            blankView = LayoutInflater.from(getContext()).inflate(R.layout.blank_views, this, false);
        }
        addBlankView();

    }


    /**
     * 隐藏空白页
     */
    public void clearBlankImage() {
        if (blankView == null) {
            blankView = LayoutInflater.from(getContext()).inflate(R.layout.blank_views, this, false);
        }
        LinearLayout llContent = (LinearLayout) blankView.findViewById(R.id.ll_content);
        llContent.setVisibility(GONE);
    }

    /**
     * 显示空白页
     */
    public void setBlankImageVisible() {
        if (blankView == null) {
            blankView = LayoutInflater.from(getContext()).inflate(R.layout.blank_views, this, false);
        }
        LinearLayout llContent = (LinearLayout) blankView.findViewById(R.id.ll_content);
        llContent.setVisibility(View.VISIBLE);
    }

    /**
     * @param res     使用默认空白页图片就传 -1
     * @param content 使用默认文字就传空
     */
    public void setBlankImage(int res, String content) {
        if (blankView == null) {
            blankView = LayoutInflater.from(getContext()).inflate(R.layout.blank_views, this, false);
        }
        //LinearLayout llContent = (LinearLayout) blankView.findViewById(R.id.ll_content);
        //llContent.setVisibility(VISIBLE);

        if (res != -1) {
            ImageView imageView = (ImageView) blankView.findViewById(R.id.iv_blank);
            imageView.setImageResource(res);
        }

        if (!TextUtils.isEmpty(content)) {
            TextView tvContent = (TextView) blankView.findViewById(R.id.tv_content);
            tvContent.setText(content);
        }
    }

    public void removeBlankView() {
        if (mAdapter != null && isAddBlankView) {
            removeFooterView(blankView);
            isAddBlankView = true;
        }
        isBlankViewGone = true;
    }

    protected void addBlankView() {
        if (isBlankViewGone){
            removeFooterView(blankView);
        }else if (mAdapter != null && mAdapter.getItemCount() == 0 && !isAddBlankView) {
            addFooterView(blankView);
            isAddBlankView = true;
        } else if (mAdapter != null && mAdapter.getItemCount() != 0 && isAddBlankView) {
            removeFooterView(blankView);
            isAddBlankView = false;
        }

    }

    public boolean isAddBlankView() {
        return isAddBlankView;
    }

    /**
     * 添加头部
     */
    public void addHeaderView(View view) {
        //如果没有adapter就不让添加
        //这里使仿照ListView处理的
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addHeaderView(view);
        }
    }

    /**
     * 添加尾部
     */
    public void addFooterView(View view) {
        //如果没有adapter就不让添加
        //这里使仿照ListView处理的
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addFooterView(view);
        }
    }

    /**
     * 移除头部
     *
     * @param view
     */
    public void removeHeaderView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.removeHeaderView(view);
        }
    }

    /**
     * 移除尾部
     *
     * @param view
     */
    public void removeFooterView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.removeFooterView(view);
        }
    }

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mAdapter == null) return;

            //如果列表的Adapter更新，则包裹的Adapter也需要更新，否则没有效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyDataSetChanged();
            }
            addBlankView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null) return;

            //如果列表的Adapter更新，则包裹的Adapter也需要更新，否则没有效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemChanged(positionStart);
            }
            addBlankView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter == null) return;

            //如果列表的Adapter更新，则包裹的Adapter也需要更新，否则没有效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemChanged(positionStart, payload);
            }
            addBlankView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null) return;

            //如果列表的Adapter更新，则包裹的Adapter也需要更新，否则没有效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemInserted(positionStart);
            }
            addBlankView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null) return;

            //如果列表的Adapter更新，则包裹的Adapter也需要更新，否则没有效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemRemoved(positionStart);
            }
            addBlankView();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null) return;

            //如果列表的Adapter更新，则包裹的Adapter也需要更新，否则没有效果
            if (mWrapRecyclerAdapter != mAdapter) {
                mWrapRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);
            }
            addBlankView();
        }
    };
}
