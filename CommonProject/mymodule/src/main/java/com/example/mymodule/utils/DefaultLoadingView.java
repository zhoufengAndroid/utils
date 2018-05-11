package com.example.mymodule.utils;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.example.mymodule.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;


/**
 * Created by zf on 2017/10/26.
 */

public class DefaultLoadingView extends SimpleDraweeView {
    private AbstractDraweeController build;

    public DefaultLoadingView(@NonNull Context context) {
        this(context, null);
    }

    public DefaultLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        build = Fresco.newDraweeControllerBuilder()
                .setUri("res://" + getContext().getPackageName() + "/" + R.drawable.common_loading_video)
                .setAutoPlayAnimations(true)
                .build();
        setController(build);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (build == null)
            return;
        try {
            build.release();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
