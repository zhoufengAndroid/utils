package com.example.mymodule.utils;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.mymodule.R;

/**
 * Created by zf on 2018/1/5.
 */

public class EarthNoNetLayout extends FrameLayout {
    public EarthNoNetLayout(@NonNull Context context) {
        this(context, null);
    }

    public EarthNoNetLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EarthNoNetLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        View view = View.inflate(getContext(), R.layout.common_default_no_network_earth, null);
//        ImageView ivError = (ImageView) view.findViewById(R.id.ivError);
//        GlideUtil.displayNoCrop(getContext(), R.drawable.nodata_internet_icon_earth, ivError);
//        addView(view);
    }
}
