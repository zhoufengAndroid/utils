package com.example.mymodule.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

public interface UIBase {
    String TAG = "TAG";

    int getContentView();

    void initView(@Nullable Bundle savedInstanceState);

    void initListener();

    void initData();

    void toast(String msg);
}
