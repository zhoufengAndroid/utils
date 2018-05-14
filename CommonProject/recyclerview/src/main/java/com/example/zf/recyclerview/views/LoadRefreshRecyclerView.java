package com.example.zf.recyclerview.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.zf.recyclerview.creators.DefaultLoadViewCreator;
import com.example.zf.recyclerview.creators.LoadViewCreator;


/**
 * Created by zf on 2017/8/4.
 * 上拉加载和下拉刷新的RecyclerView
 */

public class LoadRefreshRecyclerView extends RefreshRecyclerView {
    private static final String TAG = "LoadRefreshRecyclerView";

    private RecyclerView.Adapter mAdapter;
    /**
     * 上拉加载的辅助类
     */
    private LoadViewCreator mLoadViewCreator;

    /**
     * 上拉加载底部高度
     */
    private int mLoadViewHeight = 0;

    /**
     * 手指按下的Y位置
     */
    private int mFingerDownY;

    /**
     * 手指拖拽的阻力指数
     */
    private float mDragIndex = 0.35f;

    /**
     * 手指当前是否正在拖动
     */
    private boolean mCurrentDrag = false;

    /**
     * 当前刷新状态
     */
    private int mCurrentLoadStatus;

    /**
     * 默认状态
     */
    private final int LOAD_STATUS_NORMAL = 0x0011;
    /**
     * 上拉状态
     */
    private final int LOAD_STATUS_PULL_DOWN_LAOD = 0x0022;
    /**
     * 释放状态
     */
    private final int LOAD_STATUS_LOOSEN_LOADING = 0x0033;

    /**
     * 正在加载状态
     */
    private final int LOAD_STATUS_LAODING = 0x0044;

    /**
     * 下拉刷新的头部View
     */
    private View mLoadView;
    private DefaultLoadViewCreator loadViewCreator;

    public LoadRefreshRecyclerView(Context context) {
        super(context);
    }

    public LoadRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addLoadViewCreator(LoadViewCreator loadViewCreator) {
        this.mLoadViewCreator = loadViewCreator;
        addLoadView();
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        mAdapter=adapter;
        addLoadView();
    }

    @Override
    protected void addBlankView() {
        super.addBlankView();
        if (mAdapter != null && mAdapter.getItemCount() == 0) {
            setLoadEnable(false);
        }else if (mAdapter != null && mAdapter.getItemCount() != 0) {
            //setLoadEnable(true);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置 ,之所以写在dispatchTouchEvent那是因为如果我们处理了条目点击事件，
                // 那么就不会进入onTouchEvent里面，所以只能在这里获取
                mFingerDownY = (int) ev.getY();
                //Log.e("==========","====mFingerDownY==>"+mFingerDownY);
                break;
            case MotionEvent.ACTION_UP:
                //Log.e("==========","====mCurrentDrag==>"+mCurrentDrag);
                if (mCurrentDrag) {
                    restoreLoadView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重置当前加载状态
     */
    private void restoreLoadView() {
        if (mLoadView == null) {
            return;
        }
        final int currentBottomMargin = ((ViewGroup.MarginLayoutParams) mLoadView.getLayoutParams()).bottomMargin;
        int finalBottomMargin = 0;
        if (mCurrentLoadStatus == LOAD_STATUS_LOOSEN_LOADING) {
            mCurrentLoadStatus = LOAD_STATUS_LAODING;
            if (mLoadViewCreator != null) {
                mLoadViewCreator.onLoading();
            }

            if (mListener != null) {
                mListener.onLoadMore();
            }

            int distance = currentBottomMargin - finalBottomMargin;
            //弹回到指定位置
            ValueAnimator animator = ObjectAnimator.ofFloat(currentBottomMargin, finalBottomMargin).setDuration(distance);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float currentBottomMargin = (float) animation.getAnimatedValue();
                    setLoadViewMarginBottom((int) currentBottomMargin);
                }
            });
            animator.start();
            mCurrentDrag = false;

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //如果是在最底部，则不需要处理
                if (canScrollDown() || mCurrentLoadStatus == LOAD_STATUS_LAODING) {
                    //如果不在最底部
                    return super.onTouchEvent(e);
                }

                if (mLoadViewCreator != null) {
                    mLoadViewHeight = mLoadView.getMeasuredHeight();
                }

                //解决上拉加载自动滚的的问题
                if (mCurrentDrag) {
                    scrollToPosition(getAdapter().getItemCount() - 1);
                }

                //获取手指触摸拖拽的距离
                int distanceY = (int) ((e.getY() - mFingerDownY) * mDragIndex);
//                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
//                Log.e(TAG,"====distanceY=>"+distanceY);
                if (distanceY < 0) {
                    setLoadViewMarginBottom(-distanceY);
                    updateLoadStatus(-distanceY);
                    mCurrentDrag = true;
                    return true;
                }

                break;
        }
        return super.onTouchEvent(e);
    }

    private void updateLoadStatus(int distanceY) {
        if (distanceY <= 0) {
            mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        }/*else if (distanceY<mLoadViewHeight){
            mCurrentLoadStatus=LOAD_STATUS_PULL_DOWN_LAOD;
        }else */
        {
            mCurrentLoadStatus = LOAD_STATUS_LOOSEN_LOADING;
        }

        if (mLoadViewCreator != null) {
            mLoadViewCreator.onPull(distanceY, mLoadViewHeight, mCurrentLoadStatus);
        }
    }

    private boolean canScrollDown() {
        return ViewCompat.canScrollVertically(this, 1);
    }

    /**
     * 添加加载更多View
     */
    private void addLoadView() {
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter != null && adapter.getItemCount() != 0 && mLoadViewCreator != null && mLoadView == null) {
            View loadView = mLoadViewCreator.getLoadView(getContext(), this);
            if (loadView != null) {
                addFooterView(loadView);
                this.mLoadView = loadView;
            }
        } else if (mLoadViewCreator == null && mLoadView != null) {
            removeFooterView(mLoadView);
            this.mLoadView = null;
        }
    }

    public void setLoadViewMarginBottom(int marginBottom) {
        if (mLoadView == null) {
            return;
        }
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLoadView.getLayoutParams();
        if (marginBottom < 0) {
            marginBottom = 0;
        }

        params.bottomMargin = marginBottom;
        mLoadView.setLayoutParams(params);

    }

    public void setLoadEnable(boolean canLoad) {
        if (canLoad) {
            if (loadViewCreator == null) {
                loadViewCreator = new DefaultLoadViewCreator();
                addLoadViewCreator(loadViewCreator);
            } else {
                addLoadViewCreator(loadViewCreator);
            }

        } else {
            addLoadViewCreator(null);
        }

    }

    /**
     * 停止加载
     */
    public void onStopLoad() {
        mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        restoreLoadView();
        if (mLoadViewCreator != null) {
            mLoadViewCreator.onStopLoad();
        }
    }

    private OnLoadMoreListener mListener;

    public void setOnLoadMoreListener(OnLoadMoreListener mListener) {
        this.mListener = mListener;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
