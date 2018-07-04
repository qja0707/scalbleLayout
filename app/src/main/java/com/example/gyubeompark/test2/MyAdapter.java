package com.example.gyubeompark.test2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gyubeom.park on 2018-02-28.
 */

public class MyAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<ListData> listData = new ArrayList<ListData>();
    private LayoutInflater inflater;

    public MyAdapter(Context context, ArrayList<ListData> listData){
        super();
        this.context=context;
        this.listData=listData;

    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        Holder holder;

        if (convertView == null){
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom,container,false);

            holder = new Holder();
            holder.personImg = (ImageView)convertView.findViewById(R.id.personImg);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.phoneNum = (TextView)convertView.findViewById(R.id.phoneNum);

            convertView.setTag(holder);

        }
        else{
            holder = (Holder)convertView.getTag();
        }
        holder.personImg.setImageBitmap(listData.get(position).personImg);
        holder.phoneNum.setText(listData.get(position).phoneNum);
        holder.name.setText(listData.get(position).name);
        
        return convertView;
    }
}
