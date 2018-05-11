package com.example.mymodule.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.mymodule.R;


/**
 * Created by zf on 2018/1/8.
 */

public class DefaultEmptyView extends ImageView {
    public DefaultEmptyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultEmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        //GlideUtil.displayNoCrop(getContext(), R.drawable.nograde, this);
    }
}
