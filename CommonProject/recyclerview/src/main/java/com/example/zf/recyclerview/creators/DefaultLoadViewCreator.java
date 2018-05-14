package com.example.zf.recyclerview.creators;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zf.recyclerview.R;


/**
 * Created by zf on 2017/8/4.
 */

public class DefaultLoadViewCreator extends LoadViewCreator {

    private LinearLayout mProgressBar;
    private TextView mHintView;

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

    @Override
    public View getLoadView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.widget_recycler_footer, parent, false);
        mProgressBar =  refreshView.findViewById(R.id.recycler_footer_progressbar);
        mHintView =  refreshView.findViewById(R.id.recycler_footer_hint_textview);
        changeStatus(false);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {
        switch (currentRefreshStatus){
            case LOAD_STATUS_NORMAL:
                changeStatus(false);
                mHintView.setText("查看更多");
                break;
            case LOAD_STATUS_LOOSEN_LOADING://释放状态
                changeStatus(false);
                mHintView.setText("松开加载更多");
                break;
        }
    }

    @Override
    public void onLoading() {
        changeStatus(true);
    }

    @Override
    public void onStopLoad() {
        changeStatus(false);
        mHintView.setText("查看更多");
    }

    private void changeStatus(boolean isLoad){
        if (isLoad){
            mProgressBar.setVisibility(View.VISIBLE);
            mHintView.setVisibility(View.GONE);
        }else {
            mProgressBar.setVisibility(View.GONE);
            mHintView.setVisibility(View.VISIBLE);
        }
    }
}
