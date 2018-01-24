package com.example.student.training;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Student on 2018/1/11.
 */

public class MyAdapter extends BaseAdapter {
    Context context;
    ArrayList<EttodayNewsItem> mylist;
    public MyAdapter(Context context,ArrayList<EttodayNewsItem>mylist)
    {
        this.context=context;
        this.mylist=mylist;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if(view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.myitem,null);
            viewHolder = new ViewHolder();
            viewHolder.tv1 = view.findViewById(R.id.textView);
            viewHolder.tv2 = view.findViewById(R.id.textView2);
            viewHolder.img = view.findViewById(R.id.imageView);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder =(ViewHolder) view.getTag();
        }
        Log.d("NET", "title:" + mylist.get(position).title + ", img:" + mylist.get(position).imgurl);   //發生了抓不到圖閃退的bug
        viewHolder.tv1.setText(mylist.get(position).title);
        viewHolder.tv2.setText(mylist.get(position).description);

        Picasso.with(context).load(mylist.get(position).imgurl).into(viewHolder.img);
        return view;
    }

    static class ViewHolder{
        TextView tv1,tv2;
        ImageView img;
    }
}
