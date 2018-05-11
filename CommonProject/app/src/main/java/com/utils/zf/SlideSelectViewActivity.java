package com.utils.zf;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.utils.zf.myviews.views.SlideSelectView;

public class SlideSelectViewActivity extends FragmentActivity {
    private SlideSelectView slideSelectView;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_select_view);

        this.slideSelectView=  findViewById(R.id.slideSelectView);
        text=  findViewById(R.id.text);
        text.setText(slideSelectView.getData());
        this.slideSelectView.setOnSelectListener(new SlideSelectView.OnSelectListener() {
            @Override
            public void onSelect(int position) {
                text.setText(slideSelectView.getData());
                Log.e("============","=========data=>"+slideSelectView.getData()+",==position==>"+position);
            }
        });
    }
}
