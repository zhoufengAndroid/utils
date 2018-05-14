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

import com.example.zf.recyclerview.creators.DefaultRefreshViewCreator;
import com.example.zf.recyclerview.creators.RefreshViewCreator;


/**
 * Created by zf on 2017/8/4.
 * 下拉刷新的辅助类
 */

public class RefreshRecyclerView extends WrapRecyclerView {

    /**
     * 下拉刷新的辅助类
     */
    private RefreshViewCreator mRefreshViewCreator;

    /**
     * 下拉刷新头部高度
     */
    private int mRefreshViewHeight = 0;

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
    private int mCurrentRefreshStatus;

    /**
     * 默认状态
     */
    private final int REFRESH_STATUS_NORMAL = 0x0011;
    /**
     * 下拉状态
     */
    private final int REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022;
    /**
     * 释放状态
     */
    private final int REFRESH_STATUS_LOOSEN_REFRESHING = 0x0033;

    /**
     * 正在刷新状态
     */
    private final int REFRESH_STATUS_REFRESHING = 0x0044;

    /**
     * 下拉刷新的头部View
     */
    private View mRefreshView;
    private DefaultRefreshViewCreator creator;


    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addRefreshViewCreator(RefreshViewCreator refreshViewCreator) {
        this.mRefreshViewCreator = refreshViewCreator;
        addRefreshView();
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置 ,之所以写在dispatchTouchEvent那是因为如果我们处理了条目点击事件，
                // 那么就不会进入onTouchEvent里面，所以只能在这里获取
                mFingerDownY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentDrag) {
                    restoreRefreshView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重置当前刷新状态
     */
    private void restoreRefreshView() {
        if (mRefreshView == null) {
            return;
        }
        int currentTopMargin = ((ViewGroup.MarginLayoutParams) mRefreshView.getLayoutParams()).topMargin;
        int finalTopMargin = -mRefreshViewHeight + 1;
        if (mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
            finalTopMargin = 0;
            mCurrentRefreshStatus = REFRESH_STATUS_REFRESHING;
            if (mRefreshViewCreator != null) {
                mRefreshViewCreator.onRefreshing();
            }

            if (mListener != null) {
                mListener.OnRefresh();
            }
        }

        int distance = currentTopMargin - finalTopMargin;

        if (distance>0){
            //回弹到指定位置
            ValueAnimator animator = ObjectAnimator.ofFloat(currentTopMargin, finalTopMargin).setDuration(distance);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float currentTopMargin = (float) animation.getAnimatedValue();
                    setRefreshViewMarginTop((int) currentTopMargin);
                }
            });
            animator.start();
        }
        mCurrentDrag = false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //如果是在最顶部，则不需要处理
                if ((canScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING)&&!isAddBlankView()) {
                    return super.onTouchEvent(e);
                }

                //解决下拉刷新自动滚动问题
                if (mCurrentDrag) {//手指正在拖动
                    scrollToPosition(0);
                }

                //获取手指触摸拖拽的距离
                int distanceY = (int) ((e.getY() - mFingerDownY) * mDragIndex);
//                Log.e("=======","=distanceY="+distanceY+",=mFingerDownY=>"+mFingerDownY+",=y=>"+e.getY());
                if (distanceY > 0) {
                    int marginTop = distanceY - mRefreshViewHeight;
                    setRefreshViewMarginTop(marginTop);
                    updateRefreshStatus(marginTop);
                    mCurrentDrag = true;
                    return true;
                }

                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 更新刷新的状态
     *
     * @param marginTop
     */
    private void updateRefreshStatus(int marginTop) {
        if (marginTop <= -mRefreshViewHeight) {
            mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        } else if (marginTop < 0) {
            mCurrentRefreshStatus = REFRESH_STATUS_PULL_DOWN_REFRESH;
        } else {
            mCurrentRefreshStatus = REFRESH_STATUS_LOOSEN_REFRESHING;
        }

        if (mRefreshViewCreator != null) {
            mRefreshViewCreator.onPull(marginTop, mRefreshViewHeight, mCurrentRefreshStatus);
        }
    }

    /**
     * 判断是不是滚动到了最顶部，这个是从SwipeRefreshLayout里面copy过来的源代码
     */
    private boolean canScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(this, -1);
        }
    }

    /**
     * 添加头部的刷新View
     */
    private void addRefreshView() {
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter != null && mRefreshViewCreator != null && mRefreshView == null) {
            //添加刷新View
            View refreshView = mRefreshViewCreator.getRefreshView(getContext(), this);
            if (refreshView != null) {
                addHeaderView(refreshView);
                this.mRefreshView = refreshView;
            }

        } else if (mRefreshViewCreator == null && mRefreshView != null) {
            removeHeaderView(mRefreshView);
            this.mRefreshView = null;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            if (mRefreshView != null && mRefreshViewHeight <= 0) {
                //获取头部刷新View的高度
                mRefreshViewHeight = mRefreshView.getMeasuredHeight();
                if (mRefreshViewHeight > 0) {
                    //隐藏头部刷新的View,marginTop  多留出1dp防止无法判断是否滚动到头部
                    setRefreshViewMarginTop(-mRefreshViewHeight + 1);
                }

            }
        }

    }

    /**
     * 设置刷新View的marginTop
     *
     * @param marginTop
     */
    public void setRefreshViewMarginTop(int marginTop) {
        if (mRefreshView == null) {
            return;
        }
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mRefreshView.getLayoutParams();
        if (marginTop < -mRefreshViewHeight + 1) {
            marginTop = -mRefreshViewHeight + 1;
        }

        params.topMargin = marginTop;
        mRefreshView.setLayoutParams(params);

    }

    /**
     * 是否刷新
     *
     * @param canRefresh
     */
    public void setRefreshEnable(boolean canRefresh) {
        if (canRefresh) {
            if (creator==null){
                creator = new DefaultRefreshViewCreator();
                addRefreshViewCreator(creator);
            }else {
                addRefreshViewCreator(creator);
            }

        } else {
            addRefreshViewCreator(null);
        }
    }

    /**
     * 停止刷新
     */
    public void onStopRefresh() {
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        restoreRefreshView();
        if (mRefreshViewCreator != null) {
            mRefreshViewCreator.onStopRefresh();
        }
    }

    private OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.mListener = onRefreshListener;
    }

    public interface OnRefreshListener {
        void OnRefresh();
    }


}
