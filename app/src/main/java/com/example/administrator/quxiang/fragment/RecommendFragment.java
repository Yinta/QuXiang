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
import com.example.administrator.quxiang.adapter.RecommendAdapter;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2020/3/11.
 */

public class RecommendFragment extends Fragment {
    @Bind(R.id.recyclerView_recommend)
    RecyclerView recyclerViewRecommend;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    RecommendAdapter recommendAdapter;
    LinearLayoutManager linearLayoutManager;
    Boolean issetAdapter = false;
    int avatarNum = 0;
    String createdAt = "";
    String[] thingType = {"生活", "历史", "艺术", "科学", "自然", "文化", "地理", "社会", "人物", "经济", "体育", "其他"};
    int randNumberOne;
    int randNumberTwo;
    @Bind(R.id.titanic_recommend)
    TitanicTextView titanicRecommend;
Titanic titanic;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        ButterKnife.bind(this, view);
        titanic=new Titanic();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewRecommend.setLayoutManager(linearLayoutManager);
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
        User user = BmobUser.getCurrentUser(User.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createdAtDate = sdf.parse(createdAt);
        BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);
        BmobQuery<Post> PostBmobQuery = new BmobQuery<>();
        if (user.getLikethings().size() != 0) {
            PostBmobQuery.addWhereContainedIn("type", user.getLikethings());
        } else {
            randNumberOne = randType();
            randNumberTwo = randType();
            PostBmobQuery.addWhereContainedIn("type", Arrays.asList(new String[]{thingType[randNumberOne], thingType[randNumberTwo]}));
        }
        PostBmobQuery.setLimit(10).order("-createdAt");
        PostBmobQuery.include("author");
        PostBmobQuery.addWhereLessThan("createdAt", bmobCreatedAtDate);
        PostBmobQuery.setLimit(10).order("-createdAt");
        PostBmobQuery.findObjects(new FindListener<Post>() {

            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        if (e == null) {
                            createdAt = object.get(object.size() - 1).getCreatedAt();
                            int length = PostUtil.arrayListPostRecommend.size();
                            PostUtil.arrayListPostRecommend.addAll(object);
                            for (int i = length, j = 0; i < PostUtil.arrayListPostRecommend.size(); i++, j++) {
                                queryPhoto(object.get(j), i, 1, PostUtil.arrayListPostRecommend.size() - length);
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
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Post> PostBmobQuery = new BmobQuery<>();
        if (user.getLikethings().size() != 0) {
            PostBmobQuery.addWhereContainedIn("type", user.getLikethings());
        } else {
            randNumberOne = randType();
            randNumberTwo = randType();
            PostBmobQuery.addWhereContainedIn("type", Arrays.asList(new String[]{thingType[randNumberOne], thingType[randNumberTwo]}));
        }
        PostBmobQuery.setLimit(10).order("-createdAt");
        PostBmobQuery.include("author");
        PostBmobQuery.findObjects(new FindListener<Post>() {

            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if(titanicRecommend.getVisibility()==View.VISIBLE){
                        titanic.cancel();
                        titanicRecommend.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < PostUtil.arrayListPostRecommend.size(); i++) {
                        PostUtil.arrayListPostRecommend.get(i).getImagelist().clear();
                        PostUtil.arrayListPostRecommend.get(i).getPhotoArrayList().clear();
                        PostUtil.arrayListPostRecommend.get(i).getLikeArrayList().clear();
                    }
                    PostUtil.arrayListPostRecommend.clear();
                    if(object.size()!=0){
                        createdAt = object.get(object.size() - 1).getCreatedAt();
                    }
                    PostUtil.arrayListPostRecommend.addAll(object);
                    if (!issetAdapter) {
                        recommendAdapter = new RecommendAdapter(getActivity(), recyclerViewRecommend, linearLayoutManager);
                        recommendAdapter.setHasStableIds(true);
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

    public int randType() {
        Random random = new Random();
        return random.nextInt(12);
    }

    public void initData() {
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Post> PostBmobQuery = new BmobQuery<>();
        if (user.getLikethings().size() != 0) {
            PostBmobQuery.addWhereContainedIn("type", user.getLikethings());
        } else {
            randNumberOne = randType();
            randNumberTwo = randType();
            PostBmobQuery.addWhereContainedIn("type", Arrays.asList(new String[]{thingType[randNumberOne], thingType[randNumberTwo]}));
        }
        PostBmobQuery.setLimit(10).order("-createdAt");
        PostBmobQuery.include("author");
        PostBmobQuery.findObjects(new FindListener<Post>() {

            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    if(object.size()!=0){
                        createdAt = object.get(object.size() - 1).getCreatedAt();
                    }
                    PostUtil.arrayListPostRecommend.addAll(object);
                    recommendAdapter = new RecommendAdapter(getActivity(), recyclerViewRecommend, linearLayoutManager);
                    recommendAdapter.setHasStableIds(true);
                    for (int i = 0; i < object.size(); i++) {
                        queryPhoto(object.get(i), i, 2, object.size());
                    }
                } else {
                    titanic.start(titanicRecommend);
                    titanicRecommend.setVisibility(View.VISIBLE);
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
                    PostUtil.arrayListPostRecommend.get(num).getPhotoArrayList().addAll(list);
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
                            PostUtil.arrayListPostRecommend.get(num).setLike(true);
                            PostUtil.arrayListPostRecommend.get(num).getLikeArrayList().add(list.get(0));
                        }
                        if (avatarNum == total) {
                            avatarNum = 0;
                            if (bool == 2) {
                                recyclerViewRecommend.setAdapter(recommendAdapter);
                            } else if (bool == 1) {

                                recommendAdapter.notifyDataSetChanged();
                                refreshLayout.finishLoadMore(true);//加载完成
                            } else {
                                if (!issetAdapter) {
                                    issetAdapter = true;
                                    recyclerViewRecommend.setAdapter(recommendAdapter);
                                }
                                recommendAdapter.notifyDataSetChanged();
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
