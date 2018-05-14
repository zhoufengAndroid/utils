package com.utils.zf;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.zf.recyclerview.utils.RecyclerViewUtils;
import com.example.zf.recyclerview.views.LoadRefreshRecyclerView;
import com.example.zf.recyclerview.views.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends FragmentActivity implements RefreshRecyclerView.OnRefreshListener, LoadRefreshRecyclerView.OnLoadMoreListener {

    private LoadRefreshRecyclerView recyclerView;
    private MyAdapter myAdapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        handler = new Handler();

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setOnRefreshListener(this);
        recyclerView.setOnLoadMoreListener(this);
        recyclerView.setRefreshEnable(true);
        recyclerView.setLoadEnable(true);
        List<MyBean> beans = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            beans.add(new MyBean("name - " + i, "age - " + i));
        }
        myAdapter = new MyAdapter(beans);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void OnRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<MyBean> beans = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    beans.add(new MyBean("name - " + i, "age - " + i));
                }
                myAdapter.notifyData(beans);
                recyclerView.onStopRefresh();
            }
        }, 2000);

    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<MyBean> beans = new ArrayList<>();
                for (int i = myAdapter.getItemCount(); i < myAdapter.getItemCount() + 10; i++) {
                    beans.add(new MyBean("name - " + i, "age - " + i));
                }
                myAdapter.loadMoreData(beans);
                recyclerView.onStopLoad();
            }
        }, 2000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);//清除所有
        }
    }
}
