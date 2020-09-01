package com.example.administrator.quxiang.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.quxiang.R;
import com.example.administrator.quxiang.Utils.CalendarUtil;
import com.example.administrator.quxiang.Utils.PostUtil;
import com.example.administrator.quxiang.adapter.HotAdapter;
import com.example.administrator.quxiang.bean.Like;
import com.example.administrator.quxiang.bean.Photo;
import com.example.administrator.quxiang.bean.Post;
import com.example.administrator.quxiang.bean.User;
import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2020/3/3.
 */

public class HotFragment extends Fragment {
    @Bind(R.id.recyclerView_hot)
    RecyclerView recyclerViewHot;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    HotAdapter hotAdapter;
    LinearLayoutManager linearLayoutManager;
    int avatarNum = 0;
    boolean issetAdapter = false;
    @Bind(R.id.titanic_hot)
    TitanicTextView titanicHot;
Titanic titanic;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        ButterKnife.bind(this, view);
        titanic=new Titanic();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewHot.setLayoutManager(linearLayoutManager);
        initData();
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore();
            }
        });
        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.resetNoMoreData();
                refleshData();
            }
        });
        return view;
    }

    public void initData() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BmobDate bmobCreatedAtDateStart = new BmobDate(CalendarUtil.getBeforeYesterday());
        final BmobDate bmobCreatedAtDateEnd = new BmobDate(CalendarUtil.getNow());
        BmobQuery<Post> PostBmobQueryStart = new BmobQuery<>();
        PostBmobQueryStart.addWhereGreaterThanOrEqualTo("createdAt", bmobCreatedAtDateStart);
        BmobQuery<Post> PostBmobQueryEnd = new BmobQuery<>();
        PostBmobQueryEnd.addWhereLessThanOrEqualTo("createdAt", bmobCreatedAtDateEnd);
        List<BmobQuery<Post>> queries = new ArrayList<>();
        queries.add(PostBmobQueryStart);
        queries.add(PostBmobQueryEnd);
        BmobQuery<Post> PostBmobQuery = new BmobQuery<>();
        PostBmobQuery.and(queries);
        PostBmobQuery.setLimit(40).order("-praseNumber");
        PostBmobQuery.include("author");
        PostBmobQuery.findObjects(new FindListener<Post>() {

            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    PostUtil.arrayListPostHot.addAll(object);
                    hotAdapter = new HotAdapter(getActivity(), recyclerViewHot, linearLayoutManager);
                    hotAdapter.setHasStableIds(true);
                    for (int i = 0; i < object.size(); i++) {
                        queryPhoto(object.get(i), i, 2, object.size());
                    }
                } else {
                    titanic.start(titanicHot);
                    titanicHot.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void queryPhoto(final Post post, final int num, final int bool, final int total) {
        BmobQuery<Photo> query = new BmobQuery<>();
        query.addWhereEqualTo("post", post);
        query.findObjects(new FindListener<Photo>() {
            @Override
            public void done(List<Photo> list, BmobException e) {
                if (list.size() > 0) {
                    PostUtil.arrayListPostHot.get(num).getPhotoArrayList().addAll(list);
                }
                Date zero = CalendarUtil.getToday();
                final BmobQuery<Like> likeBmobQuery = new BmobQuery<Like>();
                likeBmobQuery.addWhereEqualTo("post", post);
                likeBmobQuery.addWhereEqualTo("author", BmobUser.getCurrentUser(User.class));
                likeBmobQuery.include("author");
                likeBmobQuery.include("post");
                likeBmobQuery.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(zero));
                likeBmobQuery.findObjects(new FindListener<Like>() {
                    @Override
                    public void done(List<Like> list, BmobException e) {
                        avatarNum++;
                        if (list.size() != 0) {
                            PostUtil.arrayListPostHot.get(num).setLike(true);
                            PostUtil.arrayListPostHot.get(num).getLikeArrayList().add(list.get(0));
                        }
                        if (avatarNum == total) {
                            avatarNum = 0;
                            if (bool == 2) {
                                recyclerViewHot.setAdapter(hotAdapter);
                            } else {
                                if (!issetAdapter) {
                                    issetAdapter = true;
                                    recyclerViewHot.setAdapter(hotAdapter);
                                }
                                hotAdapter.notifyDataSetChanged();
                                refreshLayout.finishRefresh(true);//刷新完成
                            }
                        }
                    }
                });
            }
        });
    }

    public void loadMore() {
        refreshLayout.finishLoadMoreWithNoMoreData();
    }

    public void refleshData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BmobDate bmobCreatedAtDateStart = new BmobDate(CalendarUtil.getBeforeYesterday());
        BmobDate bmobCreatedAtDateEnd = new BmobDate(CalendarUtil.getNow());
        BmobQuery<Post> PostBmobQueryStart = new BmobQuery<>();
        PostBmobQueryStart.addWhereGreaterThanOrEqualTo("createdAt", bmobCreatedAtDateStart);
        BmobQuery<Post> PostBmobQueryEnd = new BmobQuery<>();
        PostBmobQueryEnd.addWhereLessThanOrEqualTo("createdAt", bmobCreatedAtDateEnd);
        List<BmobQuery<Post>> queries = new ArrayList<>();
        queries.add(PostBmobQueryStart);
        queries.add(PostBmobQueryEnd);
        BmobQuery<Post> PostBmobQuery = new BmobQuery<>();
        PostBmobQuery.and(queries);
        PostBmobQuery.setLimit(40).order("-praseNumber");
        PostBmobQuery.include("author");
        PostBmobQuery.findObjects(new FindListener<Post>() {

            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if(titanicHot.getVisibility()==View.VISIBLE){
                        titanic.cancel();
                        titanicHot.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < PostUtil.arrayListPostHot.size(); i++) {
                        PostUtil.arrayListPostHot.get(i).getImagelist().clear();
                        PostUtil.arrayListPostHot.get(i).getPhotoArrayList().clear();
                        PostUtil.arrayListPostHot.get(i).getLikeArrayList().clear();
                    }
                    PostUtil.arrayListPostHot.clear();
                    PostUtil.arrayListPostHot.addAll(object);
                    if (!issetAdapter) {
                        hotAdapter = new HotAdapter(getActivity(), recyclerViewHot, linearLayoutManager);
                        hotAdapter.setHasStableIds(true);
                    }
                    for (int i = 0; i < object.size(); i++) {
                        queryPhoto(object.get(i), i, 0, object.size());
                    }
                } else {
                    Toast.makeText(getContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
