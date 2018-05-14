package com.example.zf.recyclerview.creators;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zf on 2017/8/4.
 * 下拉刷新的辅助类
 */

public abstract class RefreshViewCreator {

    /**
     * 获取下拉刷新的使用
     * @param context    上下文
     * @param parent     RecyclerView
     */
    public abstract View getRefreshView(Context context, ViewGroup parent);

    /**
     * 正在下拉
     * @param currentDragHeight      当前拖动的高度
     * @param refreshViewHeight      总的刷新高度
     * @param currentRefreshStatus   当前的状态
     */
    public abstract void onPull(int currentDragHeight, int refreshViewHeight,int currentRefreshStatus);

    /**
     * 正在刷新
     */
    public abstract void onRefreshing();

    /**
     * 停止刷新
     */
    public abstract void onStopRefresh();


}
