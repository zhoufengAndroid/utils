package com.utils.zf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.mymodule.base.BaseActivity;

public class MainActivity extends BaseActivity<MainPresenter> {

    private TextView tvJumpSlideSelect,tvJumpRecyclerView;

    private void jump(Class cls, String[]keys, String [] values){
        Intent intent=new Intent(this,cls);
        if (keys!=null&&values!=null&&keys.length==values.length){
            for (int i = 0; i < keys.length; i++) {
                intent.putExtra(keys[i],values[i]);
            }
        }
        startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        tvJumpSlideSelect = findViewById(R.id.tvJumpSlideSelect);
        tvJumpRecyclerView = findViewById(R.id.tvJumpRecyclerView);
    }

    @Override
    public void initListener() {
        tvJumpSlideSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jump(SlideSelectViewActivity.class,null,null);
            }
        });
        tvJumpRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jump(RecyclerViewActivity.class,null,null);
            }
        });
    }

    @Override
    public void onPrepare() {
        presenter.initData();
    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter();
    }
}
