package com.example.administrator.quxiang.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2020/3/5.
 */

public class PgAdapter extends PagerAdapter {
    ArrayList<View>arrayList;
    public PgAdapter(ArrayList<View>arrayList){
        this.arrayList=arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return  view==object;
    }
    public void setArrayList(ArrayList<View>arrayList){
        this.arrayList=arrayList;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       container.addView(arrayList.get(position));
        return arrayList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
