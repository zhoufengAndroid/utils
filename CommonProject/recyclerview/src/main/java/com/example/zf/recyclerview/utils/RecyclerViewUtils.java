package com.example.zf.recyclerview.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.zf.recyclerview.views.WrapRecyclerView;

/**
 * Created by zf on 2017/8/14.
 */

public class RecyclerViewUtils {

    private static volatile RecyclerViewUtils instance;
    private RecyclerViewUtils(){}

    public static RecyclerViewUtils getInstance(){
        if (instance==null){
            synchronized (RecyclerViewUtils.class){
                if (instance==null){
                    instance = new RecyclerViewUtils();
                }
            }
        }
        return instance;
    }

    /**
     * LinearLayoutManager -> ListView风格
     */
    public LinearLayoutManager setLinearManager(RecyclerView mRecyclerView, Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        return linearLayoutManager;
    }

    /**
     * GridLayoutManager -> GridView风格
     */
    public GridLayoutManager setGridManager(RecyclerView mRecyclerView, Context context, int count) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, count);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        return gridLayoutManager;
    }

    /**
     * StaggeredGridLayoutManager -> 瀑布流风格
     */
    public StaggeredGridLayoutManager setStaggeredGridManager(RecyclerView mRecyclerView, int count) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(count, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        return staggeredGridLayoutManager;
    }
}
