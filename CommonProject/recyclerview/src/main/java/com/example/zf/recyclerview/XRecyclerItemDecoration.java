package com.example.zf.recyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.zf.recyclerview.views.WrapRecyclerView;


/**
 * Created by zf on 2017/8/3.
 */

public class XRecyclerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDrawable;

    public XRecyclerItemDecoration(Drawable drawable){
        mDrawable=drawable;
    }

    /**
     * 绘制分割线
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount()-1;
        if (parent instanceof WrapRecyclerView){
            WrapRecyclerView recyclerView= (WrapRecyclerView) parent;
            if (recyclerView.isAddBlankView()){
                childCount--;
            }
        }
        // 获取需要绘制的区域
        Rect rect = new Rect();
        rect.left = parent.getPaddingLeft();
        rect.right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            rect.top = childView.getBottom();
            rect.bottom = rect.top + mDrawable.getIntrinsicHeight();
            // 直接利用Canvas去绘制
            mDrawable.draw(c);
        }

    }

    /**
     * 设置偏移量
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            int lastCount = parent.getAdapter().getItemCount() - 1;
            int childAdapterPosition = parent.getChildAdapterPosition(view);

            if (parent instanceof WrapRecyclerView){
                WrapRecyclerView recyclerView= (WrapRecyclerView) parent;
                if (recyclerView.isAddBlankView()){
                    lastCount--;
                }
            }

        //Log.e("=========","===lastCount==>"+lastCount+",==childAdapterPosition=>"+childAdapterPosition);
            if (childAdapterPosition==0){
                return;
            }
            //如果当前条目是最后一个条目，就不设置divider padding
            if (childAdapterPosition > lastCount) {
                return;
            }
            // 在每个子View的下面留出来画分割线
            outRect.bottom += mDrawable.getIntrinsicHeight();


    }
}
