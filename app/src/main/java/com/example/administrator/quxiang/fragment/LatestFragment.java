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
import com.example.administrator.quxiang.Utils.BitmapUtil;
import com.example.administrator.quxiang.Utils.CalendarUtil;
import com.example.administrator.quxiang.Utils.PostUtil;
import com.example.administrator.quxiang.adapter.LatestAdapter;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class LatestFragment extends Fragment {
    @Bind(R.id.recyclerView_latest)
    RecyclerView recyclerViewLatest;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    public LatestAdapter latestAdapter;
    LinearLayoutManager linearLayoutManager;
    int avatarNum = 0;
    String createdAt = "";
    Boolean issetAdapter = false;
    @Bind(R.id.titanic_latest)
    TitanicTextView titanicLatest;
    Titanic titanic;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest, container, false);
        ButterKnife.bind(this, view);
        titanic=new Titanic();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLatest.setLayoutManager(linearLayoutManager);
        initData();
        refreshLayout.setEnableAutoLoadMore(false);
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                try {
                    loadMore();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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

    public void loadMore() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createdAtDate = sdf.parse(createdAt);
        BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);
        final BmobQuery<Post> postBmobQuery = new BmobQuery<>();
        postBmobQuery.addWhereLessThan("createdAt", bmobCreatedAtDate);
        postBmobQuery.setLimit(10).order("-createdAt");
        postBmobQuery.include("author");
        postBmobQuery.findObjects(new FindListener<Post>() {

            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        if (e == null) {
                            createdAt = object.get(object.size() - 1).getCreatedAt();
                            int length = PostUtil.arrayListPost.size();
                            PostUtil.arrayListPost.addAll(object);
                            for (int i = length, j = 0; i < PostUtil.arrayListPost.size(); i++, j++) {
                                queryPhoto(object.get(j), i, 1, PostUtil.arrayListPost.size() - length);
                            }
                        } else {
                            Toast.makeText(getContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    Toast.makeText(getContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void refleshData() {
        final BmobQuery<Post> postBmobQuery = new BmobQuery<>();
        postBmobQuery.setLimit(10).order("-createdAt");
        postBmobQuery.include("author");
        postBmobQuery.findObjects(new FindListener<Post>() {

            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if(titanicLatest.getVisibility()==View.VISIBLE){
                        titanic.cancel();
                        titanicLatest.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < PostUtil.arrayListPost.size(); i++) {
                        PostUtil.arrayListPost.get(i).getImagelist().clear();
                        PostUtil.arrayListPost.get(i).getPhotoArrayList().clear();
                        PostUtil.arrayListPost.get(i).getLikeArrayList().clear();
                    }
                    PostUtil.arrayListPost.clear();
                    if(object.size()!=0){
                        createdAt = object.get(object.size() - 1).getCreatedAt();
                    }
                    PostUtil.arrayListPost.addAll(object);
                    if (!issetAdapter) {
                        latestAdapter = new LatestAdapter(getActivity(), recyclerViewLatest, linearLayoutManager);
                        latestAdapter.setHasStableIds(true);
                        BitmapUtil.latestAdapter = latestAdapter;
                        BitmapUtil.recyclerViewLatest = recyclerViewLatest;
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

    public void initData() {
        final BmobQuery<Post> postBmobQuery = new BmobQuery<>();
        postBmobQuery.setLimit(10).order("-createdAt");
        postBmobQuery.include("author");
        postBmobQuery.findObjects(new FindListener<Post>() {

            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if(object.size()!=0){
                        createdAt = object.get(object.size() - 1).getCreatedAt();
                    }
                    PostUtil.arrayListPost.addAll(object);
                    latestAdapter = new LatestAdapter(getActivity(), recyclerViewLatest, linearLayoutManager);
                    latestAdapter.setHasStableIds(true);
                    BitmapUtil.latestAdapter = latestAdapter;
                    BitmapUtil.recyclerViewLatest = recyclerViewLatest;
                    for (int i = 0; i < object.size(); i++) {
                        queryPhoto(object.get(i), i, 2, object.size());
                    }
                } else {
                    titanic.start(titanicLatest);
                    titanicLatest.setVisibility(View.VISIBLE);
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
                    PostUtil.arrayListPost.get(num).getPhotoArrayList().addAll(list);
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
                            PostUtil.arrayListPost.get(num).setLike(true);
                            PostUtil.arrayListPost.get(num).getLikeArrayList().add(list.get(0));
                        }
                        if (avatarNum == total) {
                            avatarNum = 0;
                            if (bool == 2) {
                                issetAdapter = true;
                                recyclerViewLatest.setAdapter(latestAdapter);
                            } else if (bool == 1) {

                                latestAdapter.notifyDataSetChanged();
                                refreshLayout.finishLoadMore(true);//加载完成
                            } else {
                                if (!issetAdapter) {
                                    issetAdapter = true;
                                    recyclerViewLatest.setAdapter(latestAdapter);
                                }
                                latestAdapter.notifyDataSetChanged();
                                refreshLayout.finishRefresh(true);//刷新完成
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
