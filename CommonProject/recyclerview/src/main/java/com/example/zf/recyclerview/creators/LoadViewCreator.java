package com.example.zf.recyclerview.creators;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zf on 2017/8/4.
 * 上拉加载的辅助类
 */

public abstract class LoadViewCreator {

    /**
     * 获取上拉加载更多的View
     * @param context
     * @param parent
     * @return
     */
    public abstract View getLoadView(Context context, ViewGroup parent);

    /**
     * 正在上拉
     * @param currentDragHeight
     * @param loadViewHeight
     * @param currentLoadStatus
     */
    public abstract void onPull(int currentDragHeight,int loadViewHeight,int currentLoadStatus);

    /**
     * 正在加载
     */
    public abstract void onLoading();

    /**
     * 停止加载
     */
    public abstract void onStopLoad();

}
