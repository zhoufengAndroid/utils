package com.example.zf.recyclerview.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zf on 2017/8/4.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private View itemView;

    public CommentViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        mViews = new SparseArray<>();
    }

    public CommentViewHolder setText(int viewId, CharSequence text) {
        View view = getView(viewId);
        if (view instanceof TextView){
            TextView tv= (TextView) view;
            tv.setText(text);
        }else if (view instanceof EditText){
            EditText et= (EditText) view;
            et.setText(text);
        }
        return this;
    }

    public CommentViewHolder setTextHint(int viewId, CharSequence text) {
        View view = getView(viewId);
        if (view instanceof TextView){
            TextView tv= (TextView) view;
            tv.setHint(text);
        }else if (view instanceof EditText){
            EditText et= (EditText) view;
            et.setHint(text);
        }
        return this;
    }

    public  <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }
    public CommentViewHolder setTextColor(int viewId, int color) {
        View view = getView(viewId);
        if (view instanceof TextView){
            TextView tv= (TextView) view;
            tv.setTextColor(color);
        }else if (view instanceof EditText){
            EditText et= (EditText) view;
            et.setTextColor(color);
        }
        return this;
    }

    public CommentViewHolder setTextBackgroundColor(int viewId, int color) {
        View tv = getView(viewId);
        tv.setBackgroundColor(color);
        return this;
    }

    public CommentViewHolder setViewBackgroundResource(int viewId, int resourceId) {
        View view = getView(viewId);
        view.setBackgroundResource(resourceId);
        return this;
    }

    /**
     * 设置view的visibility属性
     *
     * @param viewId
     * @param visibility
     * @return
     */
    public CommentViewHolder setViewVisibility(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

    /**
     * 设置ImageView的资源
     *
     * @param viewId
     * @param resourceId
     * @return
     */
    public CommentViewHolder setImageResource(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    /***
     * 控件点击事件
     *
     */
    public CommentViewHolder setOnBtnClickListener(int viewId, View.OnClickListener onClickListener) {
        View view=getView(viewId);
        view.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 设置条目点击事件
     *
     * @param onItemClickListener
     */
    public CommentViewHolder setOnItemClickListener(View.OnClickListener onItemClickListener) {
        itemView.setOnClickListener(onItemClickListener);
        return this;

    }

    public CommentViewHolder addTextChangeListener(int viewId,TextWatcher watcher){
        EditText editText=getView(viewId);
        editText.addTextChangedListener(watcher);
        return this;
    }

    /**
     * 设置条目长按事件
     *
     * @param onLongClickListener
     */
    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        itemView.setOnLongClickListener(onLongClickListener);
    }

    public CommentViewHolder setImageByUrl(int viewId, HolderImageLoader imageLoader) {
        ImageView imageView = getView(viewId);
        if (imageLoader == null) {
            throw new NullPointerException("imageLoader is null!");
        }
        imageLoader.displayImage(imageView.getContext(), imageView, imageLoader.getImagePath());
        return this;
    }


    public abstract static class HolderImageLoader {
        private String mIamgePath;

        public HolderImageLoader(String mIamgePath) {
            this.mIamgePath = mIamgePath;
        }

        public String getImagePath() {
            return mIamgePath;
        }

        public abstract void displayImage(Context context, ImageView imageView, String imagePath);
    }
}
