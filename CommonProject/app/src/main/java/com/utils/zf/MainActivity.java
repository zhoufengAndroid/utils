package com.utils.zf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.mymodule.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private TextView tvJumpSlideSelect;

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
    }

    @Override
    public void initListener() {
        tvJumpSlideSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jump(SlideSelectViewActivity.class,null,null);
            }
        });
    }

    @Override
    public void initData() {

    }
}
