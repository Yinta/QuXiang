package com.example.administrator.quxiang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.quxiang.R;
import com.example.administrator.quxiang.Utils.BitmapUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2020/3/5.
 */

public class GridAdapter extends BaseAdapter {
    private LayoutInflater inflater; // 视图容器
    Context context;
    public GridAdapter(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return  BitmapUtil.bitmaps == null ? 0 : BitmapUtil.bitmaps.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return BitmapUtil.bitmaps == null ? null : BitmapUtil.bitmaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.gride_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView)convertView.findViewById(R.id.gride_image);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        if (position == BitmapUtil.bitmaps.size())
        {
            holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.icon_addpic_unfocused));
//                if (position == 9)
//                {
//                    holder.image.setVisibility(View.GONE);
//                }
        }
        else
        {
            holder.imageView.setImageBitmap(BitmapUtil.bitmaps.get(position));
        }
        return convertView;
    }
    public class ViewHolder
    {
        public ImageView imageView;
    }
}
