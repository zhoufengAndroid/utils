package com.utils.zf;

import android.view.View;
import android.widget.TextView;

import com.example.zf.recyclerview.adapters.CommentRecyclerAdapter;
import com.example.zf.recyclerview.adapters.CommentViewHolder;

import java.util.List;

public class MyAdapter extends CommentRecyclerAdapter<MyBean,MyAdapter.MyHolder> {

    public MyAdapter(List<MyBean> mList) {
        super(mList, R.layout.recycler_item);
    }

    @Override
    protected void convert(MyHolder holder, MyBean item) {
        holder.tvName.setText(item.getName());
        holder.tvAge.setText(item.getAge());
    }


    public static class MyHolder extends CommentViewHolder{

        private final TextView tvName,tvAge;

        public MyHolder(View itemView) {
            super(itemView);
            this.tvName=itemView.findViewById(R.id.tv_name);
            this.tvAge=itemView.findViewById(R.id.tv_age);
        }
    }
}
