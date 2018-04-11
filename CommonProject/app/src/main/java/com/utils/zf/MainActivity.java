package com.utils.zf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void jump(Class cls,String[]keys,String [] values){
        Intent intent=new Intent(this,cls);
        if (keys!=null&&values!=null&&keys.length==values.length){
            for (int i = 0; i < keys.length; i++) {
                intent.putExtra(keys[i],values[i]);
            }
        }
        startActivity(intent);

    }

    public void jumpToSlideSelect(View view) {
        jump(SlideSelectViewActivity.class,null,null);
    }
}
