package com.example.zf.recyclerview.creators;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zf.recyclerview.R;


/**
 * Created by zf on 2017/8/4.
 */

public class DefaultRefreshViewCreator extends RefreshViewCreator {

    /**
     * 加载数据的ImageView
     */
    private View mRefreshIv;
    private ProgressBar mProgressBar;
    private TextView mHintTextView;

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

    @Override
    public View getRefreshView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.widget_recycler_header, parent, false);
        mRefreshIv = refreshView.findViewById(R.id.recycler_header_arrow);
        mProgressBar = refreshView.findViewById(R.id.recycler_header_progressbar);
        mHintTextView =  refreshView.findViewById(R.id.recycler_header_hint_textview);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {
        int currentHeight = currentDragHeight + refreshViewHeight;

        switch (currentRefreshStatus) {
            case REFRESH_STATUS_NORMAL://默认状态
                break;
            case REFRESH_STATUS_PULL_DOWN_REFRESH://下拉状态
                mHintTextView.setText("下拉刷新");
                changeStatus(false);
                break;
            case REFRESH_STATUS_LOOSEN_REFRESHING://释放状态
                mHintTextView.setText("松开刷新数据");
                changeStatus(false);
                break;
        }

        if (currentHeight >= refreshViewHeight) {
            mRefreshIv.setRotation(180);
        } else {
            mRefreshIv.setRotation(0);
        }

    }

    @Override
    public void onRefreshing() {
        changeStatus(true);
        mHintTextView.setText("正在刷新...");

    }

    private void changeStatus(boolean isRefresh) {
        if (isRefresh) {
            mRefreshIv.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mRefreshIv.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStopRefresh() {
        // 停止加载的时候清除动画
        mRefreshIv.setRotation(0);
        mRefreshIv.clearAnimation();
    }
}
