package com.example.administrator.quxiang.Utils;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;


import com.example.administrator.quxiang.adapter.GridAdapter;
import com.example.administrator.quxiang.adapter.LatestAdapter;
import com.example.administrator.quxiang.adapter.MyAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2020/3/6.
 */

public class BitmapUtil {
    public static int count=4;
    public static GridAdapter gridAdapter;
    public static RecyclerView recyclerViewLatest;
    public static LatestAdapter latestAdapter;
    public static MyAdapter myAdapter;
    public static RecyclerView recyclerViewMy;
    public static ArrayList<Bitmap>bitmaps=new ArrayList<Bitmap>();
    public static ArrayList<String>paths=new ArrayList<>();
}
