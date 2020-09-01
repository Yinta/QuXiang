package com.example.administrator.quxiang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2020/3/3.
 */

public class FgAdapter extends FragmentPagerAdapter{
    ArrayList<Fragment> list;
    private String[] titles = new String[]{"最新", "热门", "推荐"};
    //通过构造获取list集合
    public FgAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list=list;
    }
    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }
    //设置有多少内容
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];

    }
}
