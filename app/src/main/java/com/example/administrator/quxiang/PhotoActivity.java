package com.example.administrator.quxiang;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.quxiang.Utils.BitmapUtil;
import com.example.administrator.quxiang.Utils.PostUtil;
import com.example.administrator.quxiang.adapter.PgAdapter;
import com.example.administrator.quxiang.bean.Photo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoActivity extends Activity {

    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.photo_bt_del)
    Button photoBtDel;
    @Bind(R.id.photo_relativeLayout)
    RelativeLayout photoRelativeLayout;
    public  ArrayList<View> listViews = null;
    private PgAdapter pgAdapter;
    int index;
    int id;
    int from;
    int position;
    final int FROM_LATEST=0;
    final int FROM_ISSUE=1;
    final int FROM_HOT=2;
    final int FROM_RECOMMEND=3;
    final int FROM_MY=4;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", 0);
        from=intent.getIntExtra("from",0);
        position=intent.getIntExtra("position",0);
        if(from==FROM_LATEST){
            for (int i = 0; i < PostUtil.arrayListPost.get(position).getImagelist().size(); i++) {
                initListViews( PostUtil.arrayListPost.get(position).getImagelist().get(i));//
                photoBtDel.setVisibility(View.GONE);
            }
        }else if(from==FROM_ISSUE){
            for (int i = 0; i < BitmapUtil.bitmaps.size(); i++) {
                initListViews(BitmapUtil.bitmaps.get(i));//
                photoBtDel.setVisibility(View.VISIBLE);
            }
        }
        else if(from==FROM_HOT){
            for (int i = 0; i < PostUtil.arrayListPostHot.get(position).getImagelist().size(); i++) {
                initListViews( PostUtil.arrayListPostHot.get(position).getImagelist().get(i));//
                photoBtDel.setVisibility(View.GONE);
            }
        }
        else if(from==FROM_RECOMMEND){
            for (int i = 0; i < PostUtil.arrayListPostRecommend.get(position).getImagelist().size(); i++) {
                initListViews( PostUtil.arrayListPostRecommend.get(position).getImagelist().get(i));//
                photoBtDel.setVisibility(View.GONE);
            }
        }else if (from==FROM_MY){
            for (int i = 0; i < PostUtil.arrayListPostMy.get(position).getImagelist().size(); i++) {
                initListViews( PostUtil.arrayListPostMy.get(position).getImagelist().get(i));//
                photoBtDel.setVisibility(View.GONE);
            }
        }
        pgAdapter = new PgAdapter(listViews);
        viewpager.setAdapter(pgAdapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setCurrentItem(id);
    }

    private void initListViews(Bitmap bm) {
        if (listViews == null) listViews = new ArrayList<View>();
        ImageView img = new ImageView(this);// 构造textView对象
        img.setBackgroundColor(0xff000000);
        img.setImageBitmap(bm);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listViews.add(img);// 添加view
    }


    @OnClick(R.id.photo_bt_del)
    public void onViewClicked() {
        if (BitmapUtil.bitmaps.size() == 1) {
            BitmapUtil.bitmaps.remove(index);
            BitmapUtil.paths.remove(index);
            BitmapUtil.count++;
            BitmapUtil.gridAdapter.notifyDataSetChanged();
            viewpager.removeAllViews();
            listViews.remove(index);
            pgAdapter.setArrayList(listViews);
            pgAdapter.notifyDataSetChanged();
            finish();
        } else {
            BitmapUtil.bitmaps.remove(index);
            BitmapUtil.paths.remove(index);
            BitmapUtil.count++;
            BitmapUtil.gridAdapter.notifyDataSetChanged();
            viewpager.removeAllViews();
            listViews.remove(index);
            pgAdapter.setArrayList(listViews);
            pgAdapter.notifyDataSetChanged();
        }

    }
}

