package com.example.administrator.quxiang;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.quxiang.Utils.BitmapUtil;
import com.example.administrator.quxiang.Utils.PostUtil;
import com.example.administrator.quxiang.Utils.UtilsStyle;
import com.example.administrator.quxiang.adapter.DetailAdapter;
import com.example.administrator.quxiang.adapter.LatestAdapter;
import com.example.administrator.quxiang.bean.Comment;
import com.example.administrator.quxiang.bean.Post;
import com.example.administrator.quxiang.bean.User;
import com.example.administrator.quxiang.view.KeyDialog;
import com.example.administrator.quxiang.view.RoundImageView;

import java.lang.reflect.Method;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {

    @Bind(R.id.finish_detail)
    Button finishDetail;
    @Bind(R.id.photo_detail)
    CircleImageView photoDetail;
    @Bind(R.id.detail_name)
    TextView detailName;
    @Bind(R.id.detail_time)
    TextView detailTime;
    @Bind(R.id.detail_content)
    TextView detailContent;
    @Bind(R.id.detail_image_one)
    RoundImageView detailImageOne;
    @Bind(R.id.detail_image_two)
    RoundImageView detailImageTwo;
    @Bind(R.id.detail_image_three)
    RoundImageView detailImageThree;
    @Bind(R.id.detail_image_four)
    RoundImageView detailImageFour;
    int from;
    boolean issetAdapter=false;
    int position;
    final int FROM_LATEST = 0;
    final int FROM_HOT = 1;
    final int FROM_RECOMMEND = 2;
    final int FROM_MY = 3;
    String createdAt = "";
    @Bind(R.id.detail_recyclerview)
    RecyclerView detailRecyclerview;
    DetailAdapter detailAdapter;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    @Bind(R.id.detail_comment)
    EditText detailComment;
    KeyDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        UtilsStyle.statusBarLightMode(this);
        from = getIntent().getIntExtra("from", 0);
        position = getIntent().getIntExtra("position", 0);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        detailRecyclerview.setLayoutManager(linearLayoutManager);
        changeVisable();
        initData();
        queryData();
         dialog =new KeyDialog("写下你的看法", new KeyDialog.SendBackListener() {
            @Override
            public void sendBack(String inputText) {
                final Comment comment = new Comment();
                comment.setAuthor(BmobUser.getCurrentUser(User.class));
                comment.setAvatarBoolean(true);
                switch (from){
                    case 0:
                        comment.setPost(PostUtil.arrayListPost.get(position));
                        break;
                    case 1:
                        comment.setPost(PostUtil.arrayListPostHot.get(position));
                        break;
                    case 2:
                        comment.setPost(PostUtil.arrayListPostRecommend.get(position));
                        break;
                    case 3:
                        comment.setPost(PostUtil.arrayListPostMy.get(position));
                        break;
                }
                comment.setContent(inputText.trim());
                comment.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId,BmobException e) {
                        if(e==null){
             dialog.clearText();
                            PostUtil.comments.add(0,comment);
                            switch (from){
                                case 0:
                                    PostUtil.arrayListPost.get(position).setCommentNumber( PostUtil.arrayListPost.get(position).getCommentNumber()+1);
                                    Post postLatest=new Post();
                                    postLatest.setCommentNumber(PostUtil.arrayListPost.get(position).getCommentNumber()+1);
                                    postLatest.update( PostUtil.arrayListPost.get(position).getObjectId(), new UpdateListener() {

                                        @Override
                                        public void done(BmobException e) {

                                        }

                                    });
                                    break;
                                case 1:
                                    PostUtil.arrayListPostHot.get(position).setCommentNumber( PostUtil.arrayListPostHot.get(position).getCommentNumber()+1);
                                    Post postHot=new Post();
                                    postHot.setCommentNumber(PostUtil.arrayListPostHot.get(position).getCommentNumber()+1);
                                    postHot.update( PostUtil.arrayListPostHot.get(position).getObjectId(), new UpdateListener() {

                                        @Override
                                        public void done(BmobException e) {

                                        }

                                    });
                                    break;
                                case 2:
                                    PostUtil.arrayListPostRecommend.get(position).setCommentNumber( PostUtil.arrayListPostRecommend.get(position).getCommentNumber()+1);
                                    Post postRecommend=new Post();
                                    postRecommend.setCommentNumber(PostUtil.arrayListPostRecommend.get(position).getCommentNumber()+1);
                                    postRecommend.update( PostUtil.arrayListPostRecommend.get(position).getObjectId(), new UpdateListener() {

                                        @Override
                                        public void done(BmobException e) {

                                        }

                                    });
                                    break;
                                case 3:
                                    PostUtil.arrayListPostMy.get(position).setCommentNumber( PostUtil.arrayListPostMy.get(position).getCommentNumber()+1);
                                    Post postMy=new Post();
                                    postMy.setCommentNumber(PostUtil.arrayListPostMy.get(position).getCommentNumber()+1);
                                    postMy.update( PostUtil.arrayListPostMy.get(position).getObjectId(), new UpdateListener() {

                                        @Override
                                        public void done(BmobException e) {

                                        }

                                    });
                                    break;
                            }
                            if(BitmapFactory.decodeFile(BmobUser.getCurrentUser(User.class).getNativePicture())!=null){
                                PostUtil.comments.get(0).setAvatar(BitmapFactory.decodeFile(BmobUser.getCurrentUser(User.class).getNativePicture()));
                               if(issetAdapter==false){
                                   issetAdapter=true;
                                   detailRecyclerview.setAdapter(detailAdapter);
                               }
                                detailAdapter.notifyDataSetChanged();
                            }else if (BmobUser.getCurrentUser(User.class).getPicture() != null) {
                                BmobUser.getCurrentUser(User.class).getPicture().download(new DownloadFileListener() {
                                    @Override
                                    public void onProgress(Integer integer, long l) {

                                    }

                                    @Override
                                    public void done(String s, BmobException e) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(s);
                                            PostUtil.comments.get(0).setAvatar(bitmap);
                                        if(issetAdapter==false){
                                            issetAdapter=true;
                                            detailRecyclerview.setAdapter(detailAdapter);
                                        }
                                            detailAdapter.notifyDataSetChanged();
                                    }
                                });
                            }else {
                                if(issetAdapter==false){
                                    issetAdapter=true;
                                    detailRecyclerview.setAdapter(detailAdapter);
                                }
                                detailAdapter.notifyDataSetChanged();
                            }
                        }else{

                        }
                    }
                });
            }
        });
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            detailComment.setInputType(InputType.TYPE_NULL);

        } else {

            Class<EditText> cls = EditText.class;

            Method method;

            try {

                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);

                method.setAccessible(true);

                method.invoke(detailComment, false);

            } catch (Exception e) {

            }

        }

    }
    public void queryData(){
        final BmobQuery<Comment> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.setLimit(10).order("-createdAt");
        commentBmobQuery.include("author");
        switch (from){
            case 0:
                commentBmobQuery.addWhereEqualTo("post",PostUtil.arrayListPost.get(position));
                break;
            case 1:
                commentBmobQuery.addWhereEqualTo("post",PostUtil.arrayListPostHot.get(position));
                break;
            case 2:
                commentBmobQuery.addWhereEqualTo("post",PostUtil.arrayListPostRecommend.get(position));
                break;
            case 3:
                commentBmobQuery.addWhereEqualTo("post",PostUtil.arrayListPostMy.get(position));
                break;
        }
        commentBmobQuery.findObjects(new FindListener<Comment>() {

            @Override
            public void done(List<Comment> object, BmobException e) {
                detailAdapter = new DetailAdapter(DetailActivity.this,detailRecyclerview,linearLayoutManager);
                detailAdapter.setHasStableIds(true);
                if(e==null){
                    if(object.size()!=0){
                        createdAt = object.get(object.size() - 1).getCreatedAt();
                    }
                    PostUtil.comments.clear();
                    PostUtil.comments.addAll(object);
                    issetAdapter=true;
                    detailRecyclerview.setAdapter(detailAdapter);
                }
               else {
                    Toast.makeText(DetailActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    @OnClick(R.id.finish_detail)
    public void onViewClicked() {
        finish();
    }

    public void changeVisable() {
        detailImageOne.setVisibility(View.VISIBLE);
        detailImageTwo.setVisibility(View.VISIBLE);
        detailImageThree.setVisibility(View.VISIBLE);
        detailImageFour.setVisibility(View.VISIBLE);
        switch (from) {
            case 0:
                switch (PostUtil.arrayListPost.get(position).getPhotoNumber()) {
                    case 0:
                        detailImageOne.setVisibility(View.GONE);
                        detailImageTwo.setVisibility(View.GONE);
                        detailImageThree.setVisibility(View.GONE);
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 1:
                        detailImageTwo.setVisibility(View.GONE);
                        detailImageThree.setVisibility(View.GONE);
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 2:
                        detailImageThree.setVisibility(View.GONE);
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 3:
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 4:

                        break;
                    default:
                        break;
                }
                break;
            case 1:
                switch (PostUtil.arrayListPostHot.get(position).getImagelist().size()) {
                    case 0:
                        detailImageOne.setVisibility(View.GONE);
                        detailImageTwo.setVisibility(View.GONE);
                        detailImageThree.setVisibility(View.GONE);
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 1:
                        detailImageTwo.setVisibility(View.GONE);
                        detailImageThree.setVisibility(View.GONE);
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 2:
                        detailImageThree.setVisibility(View.GONE);
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 3:
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 4:

                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch (PostUtil.arrayListPostRecommend.get(position).getImagelist().size()) {
                    case 0:
                        detailImageOne.setVisibility(View.GONE);
                        detailImageTwo.setVisibility(View.GONE);
                        detailImageThree.setVisibility(View.GONE);
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 1:
                        detailImageTwo.setVisibility(View.GONE);
                        detailImageThree.setVisibility(View.GONE);
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 2:
                        detailImageThree.setVisibility(View.GONE);
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 3:
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 4:

                        break;
                    default:
                        break;
                }
                break;
            case 3:
                Toast.makeText(DetailActivity.this, PostUtil.arrayListPostMy.get(position).canDownload + "", Toast.LENGTH_SHORT).show();
                switch (PostUtil.arrayListPostMy.get(position).getImagelist().size()) {
                    case 0:
                        detailImageOne.setVisibility(View.GONE);
                        detailImageTwo.setVisibility(View.GONE);
                        detailImageThree.setVisibility(View.GONE);
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 1:
                        detailImageTwo.setVisibility(View.GONE);
                        detailImageThree.setVisibility(View.GONE);
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 2:
                        detailImageThree.setVisibility(View.GONE);
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 3:
                        detailImageFour.setVisibility(View.GONE);
                        break;
                    case 4:

                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

    }

    public void initData() {
        switch (from) {
            case 0:
                photoDetail.setImageBitmap(PostUtil.arrayListPost.get(position).getAvatar());
                detailName.setText(PostUtil.arrayListPost.get(position).getAuthor().getNickname());
                detailTime.setText(PostUtil.arrayListPost.get(position).getCreatedAt());
                detailContent.setText(PostUtil.arrayListPost.get(position).getContent());
                switch (PostUtil.arrayListPost.get(position).getImagelist().size()) {
                    case 0:
                        break;
                    case 1:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPost.get(position).getImagelist().get(0));
                        break;
                    case 2:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPost.get(position).getImagelist().get(0));
                        detailImageTwo.setImageBitmap(PostUtil.arrayListPost.get(position).getImagelist().get(1));
                        break;
                    case 3:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPost.get(position).getImagelist().get(0));
                        detailImageTwo.setImageBitmap(PostUtil.arrayListPost.get(position).getImagelist().get(1));
                        detailImageThree.setImageBitmap(PostUtil.arrayListPost.get(position).getImagelist().get(2));
                        break;
                    case 4:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPost.get(position).getImagelist().get(0));
                        detailImageTwo.setImageBitmap(PostUtil.arrayListPost.get(position).getImagelist().get(1));
                        detailImageThree.setImageBitmap(PostUtil.arrayListPost.get(position).getImagelist().get(2));
                        detailImageFour.setImageBitmap(PostUtil.arrayListPost.get(position).getImagelist().get(3));
                        break;
                    default:
                        break;
                }
                break;
            case 1:
                photoDetail.setImageBitmap(PostUtil.arrayListPostHot.get(position).getAvatar());
                detailName.setText(PostUtil.arrayListPostHot.get(position).getAuthor().getNickname());
                detailTime.setText(PostUtil.arrayListPostHot.get(position).getCreatedAt());
                detailContent.setText(PostUtil.arrayListPostHot.get(position).getContent());
                switch (PostUtil.arrayListPostHot.get(position).getImagelist().size()) {
                    case 0:
                        break;
                    case 1:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPostHot.get(position).getImagelist().get(0));
                        break;
                    case 2:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPostHot.get(position).getImagelist().get(0));
                        detailImageTwo.setImageBitmap(PostUtil.arrayListPostHot.get(position).getImagelist().get(1));
                        break;
                    case 3:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPostHot.get(position).getImagelist().get(0));
                        detailImageTwo.setImageBitmap(PostUtil.arrayListPostHot.get(position).getImagelist().get(1));
                        detailImageThree.setImageBitmap(PostUtil.arrayListPostHot.get(position).getImagelist().get(2));
                        break;
                    case 4:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPostHot.get(position).getImagelist().get(0));
                        detailImageTwo.setImageBitmap(PostUtil.arrayListPostHot.get(position).getImagelist().get(1));
                        detailImageThree.setImageBitmap(PostUtil.arrayListPostHot.get(position).getImagelist().get(2));
                        detailImageFour.setImageBitmap(PostUtil.arrayListPostHot.get(position).getImagelist().get(3));
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                photoDetail.setImageBitmap(PostUtil.arrayListPostRecommend.get(position).getAvatar());
                detailName.setText(PostUtil.arrayListPostRecommend.get(position).getAuthor().getNickname());
                detailTime.setText(PostUtil.arrayListPostRecommend.get(position).getCreatedAt());
                detailContent.setText(PostUtil.arrayListPostRecommend.get(position).getContent());
                switch (PostUtil.arrayListPostRecommend.get(position).getImagelist().size()) {
                    case 0:
                        break;
                    case 1:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPostRecommend.get(position).getImagelist().get(0));
                        break;
                    case 2:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPostRecommend.get(position).getImagelist().get(0));
                        detailImageTwo.setImageBitmap(PostUtil.arrayListPostRecommend.get(position).getImagelist().get(1));
                        break;
                    case 3:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPostRecommend.get(position).getImagelist().get(0));
                        detailImageTwo.setImageBitmap(PostUtil.arrayListPostRecommend.get(position).getImagelist().get(1));
                        detailImageThree.setImageBitmap(PostUtil.arrayListPostRecommend.get(position).getImagelist().get(2));
                        break;
                    case 4:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPostRecommend.get(position).getImagelist().get(0));
                        detailImageTwo.setImageBitmap(PostUtil.arrayListPostRecommend.get(position).getImagelist().get(1));
                        detailImageThree.setImageBitmap(PostUtil.arrayListPostRecommend.get(position).getImagelist().get(2));
                        detailImageFour.setImageBitmap(PostUtil.arrayListPostRecommend.get(position).getImagelist().get(3));
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                photoDetail.setImageBitmap(PostUtil.arrayListPostMy.get(position).getAvatar());
                detailName.setText(PostUtil.arrayListPostMy.get(position).getAuthor().getNickname());
                detailTime.setText(PostUtil.arrayListPostMy.get(position).getCreatedAt());
                detailContent.setText(PostUtil.arrayListPostMy.get(position).getContent());
                switch (PostUtil.arrayListPostMy.get(position).getImagelist().size()) {
                    case 0:
                        break;
                    case 1:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPostMy.get(position).getImagelist().get(0));
                        break;
                    case 2:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPostMy.get(position).getImagelist().get(0));
                        detailImageTwo.setImageBitmap(PostUtil.arrayListPostMy.get(position).getImagelist().get(1));
                        break;
                    case 3:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPostMy.get(position).getImagelist().get(0));
                        detailImageTwo.setImageBitmap(PostUtil.arrayListPostMy.get(position).getImagelist().get(1));
                        detailImageThree.setImageBitmap(PostUtil.arrayListPostMy.get(position).getImagelist().get(2));
                        break;
                    case 4:
                        detailImageOne.setImageBitmap(PostUtil.arrayListPostMy.get(position).getImagelist().get(0));
                        detailImageTwo.setImageBitmap(PostUtil.arrayListPostMy.get(position).getImagelist().get(1));
                        detailImageThree.setImageBitmap(PostUtil.arrayListPostMy.get(position).getImagelist().get(2));
                        detailImageFour.setImageBitmap(PostUtil.arrayListPostMy.get(position).getImagelist().get(3));
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

    }

    @OnClick({R.id.detail_image_one, R.id.detail_image_two, R.id.detail_image_three, R.id.detail_image_four})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detail_image_one:
                switch (from) {
                    case 0:
                        Intent intentOne = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentOne.putExtra("position", position);
                        intentOne.putExtra("ID", 0);
                        intentOne.putExtra("from", 0);
                        startActivity(intentOne);
                        break;
                    case 1:
                        Intent intentOneHot = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentOneHot.putExtra("position", position);
                        intentOneHot.putExtra("ID", 0);
                        intentOneHot.putExtra("from", 2);
                        startActivity(intentOneHot);
                        break;
                    case 2:
                        Intent intentOneRecommend = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentOneRecommend.putExtra("position", position);
                        intentOneRecommend.putExtra("ID", 0);
                        intentOneRecommend.putExtra("from", 3);
                        startActivity(intentOneRecommend);
                        break;
                    case 3:
                        Intent intentOneMy = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentOneMy.putExtra("position", position);
                        intentOneMy.putExtra("ID", 0);
                        intentOneMy.putExtra("from", 4);
                        startActivity(intentOneMy);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.detail_image_two:
                switch (from) {
                    case 0:
                        Intent intentTwo = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentTwo.putExtra("position", position);
                        intentTwo.putExtra("ID", 1);
                        intentTwo.putExtra("from", 0);
                        startActivity(intentTwo);
                        break;
                    case 1:
                        Intent intentTwoHot = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentTwoHot.putExtra("position", position);
                        intentTwoHot.putExtra("ID", 1);
                        intentTwoHot.putExtra("from", 2);
                        startActivity(intentTwoHot);
                        break;
                    case 2:
                        Intent intentTwoRecommend = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentTwoRecommend.putExtra("position", position);
                        intentTwoRecommend.putExtra("ID", 1);
                        intentTwoRecommend.putExtra("from", 3);
                        startActivity(intentTwoRecommend);
                        break;
                    case 3:
                        Intent intentTwoMy = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentTwoMy.putExtra("position", position);
                        intentTwoMy.putExtra("ID", 1);
                        intentTwoMy.putExtra("from", 4);
                        startActivity(intentTwoMy);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.detail_image_three:
                switch (from) {
                    case 0:
                        Intent intentThree = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentThree.putExtra("position", position);
                        intentThree.putExtra("ID", 2);
                        intentThree.putExtra("from", 0);
                        startActivity(intentThree);
                        break;
                    case 1:
                        Intent intentThreeHot = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentThreeHot.putExtra("position", position);
                        intentThreeHot.putExtra("ID", 2);
                        intentThreeHot.putExtra("from", 2);
                        startActivity(intentThreeHot);
                        break;
                    case 2:
                        Intent intentThreeRecommend = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentThreeRecommend.putExtra("position", position);
                        intentThreeRecommend.putExtra("ID", 2);
                        intentThreeRecommend.putExtra("from", 3);
                        startActivity(intentThreeRecommend);
                        break;
                    case 3:
                        Intent intentThreeMy = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentThreeMy.putExtra("position", position);
                        intentThreeMy.putExtra("ID", 2);
                        intentThreeMy.putExtra("from", 4);
                        startActivity(intentThreeMy);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.detail_image_four:
                switch (from) {
                    case 0:
                        Intent intentFour = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentFour.putExtra("position", position);
                        intentFour.putExtra("ID", 1);
                        intentFour.putExtra("from", 0);
                        startActivity(intentFour);
                        break;
                    case 1:
                        Intent intentFourHot = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentFourHot.putExtra("position", position);
                        intentFourHot.putExtra("ID", 3);
                        intentFourHot.putExtra("from", 2);
                        startActivity(intentFourHot);
                        break;
                    case 2:
                        Intent intentFourRecommend = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentFourRecommend.putExtra("position", position);
                        intentFourRecommend.putExtra("ID", 3);
                        intentFourRecommend.putExtra("from", 3);
                        startActivity(intentFourRecommend);
                        break;
                    case 3:
                        Intent intentFourMy = new Intent(DetailActivity.this, PhotoActivity.class);
                        intentFourMy.putExtra("position", position);
                        intentFourMy.putExtra("ID", 3);
                        intentFourMy.putExtra("from", 4);
                        startActivity(intentFourMy);
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (from) {
            case 0:
                if ((PostUtil.arrayListPost.get(position).getPhotoNumber() - PostUtil.arrayListPost.get(position).getImagelist().size()) != 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                        }
                    }, 2500 * (PostUtil.arrayListPost.get(position).getPhotoNumber() - PostUtil.arrayListPost.get(position).getImagelist().size()));
                }
                break;
            case 1:
                if ((PostUtil.arrayListPostHot.get(position).getPhotoNumber() - PostUtil.arrayListPostHot.get(position).getImagelist().size()) != 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                        }
                    }, 2500 * (PostUtil.arrayListPostHot.get(position).getPhotoNumber() - PostUtil.arrayListPostHot.get(position).getImagelist().size()));
                }
                break;
            case 2:
                if ((PostUtil.arrayListPostRecommend.get(position).getPhotoNumber() - PostUtil.arrayListPostRecommend.get(position).getImagelist().size()) != 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                        }
                    }, 2500 * (PostUtil.arrayListPostRecommend.get(position).getPhotoNumber() - PostUtil.arrayListPostRecommend.get(position).getImagelist().size()));
                }
                break;
            case 3:
                if ((PostUtil.arrayListPostMy.get(position).getPhotoNumber() - PostUtil.arrayListPostMy.get(position).getImagelist().size()) != 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                        }
                    }, 2500 * (PostUtil.arrayListPostMy.get(position).getPhotoNumber() - PostUtil.arrayListPostMy.get(position).getImagelist().size()));
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @OnClick(R.id.detail_comment)
    public void onCommentViewClicked() {
      dialog.show(getSupportFragmentManager(),"eef");
    }
}
