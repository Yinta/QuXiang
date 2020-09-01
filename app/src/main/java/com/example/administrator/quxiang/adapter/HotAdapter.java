package com.example.administrator.quxiang.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.quxiang.BroadcastReceive.NetWorkReceive;
import com.example.administrator.quxiang.DetailActivity;
import com.example.administrator.quxiang.PhotoActivity;
import com.example.administrator.quxiang.R;
import com.example.administrator.quxiang.Utils.CacheUtil;
import com.example.administrator.quxiang.Utils.PostUtil;
import com.example.administrator.quxiang.bean.Like;
import com.example.administrator.quxiang.bean.Post;
import com.example.administrator.quxiang.bean.User;
import com.example.administrator.quxiang.view.RoundImageView;
import com.wx.goodview.GoodView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2020/5/18.
 */

public class HotAdapter extends RecyclerView.Adapter<HotAdapter.ViewHolder>  {
    Context context;
    private View view;
    RecyclerView recyclerViewHot;
    LinearLayoutManager linearLayoutManager;
    boolean isSlide=false;
    NetWorkReceive.OnNetWorkChange onNetWorkChangeHot;
    public HotAdapter(Context context, RecyclerView recyclerViewLatest, LinearLayoutManager linearLayoutManager){
        this.context=context;
        this.recyclerViewHot=recyclerViewLatest;
        this.linearLayoutManager=linearLayoutManager;
         onNetWorkChangeHot=new NetWorkReceive.OnNetWorkChange() {
            @Override
            public void notifyChange() {
                notifyDataSetChanged();
            }
        };
        NetWorkReceive.getInstance().setOnNetWorkChange(onNetWorkChangeHot);

    }
    @Override
    public HotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_item,parent,false);
        final HotAdapter.ViewHolder viewHolder=new HotAdapter.ViewHolder(view);
        recyclerViewHot.setItemViewCacheSize(20);
        recyclerViewHot.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    case RecyclerView.SCROLL_STATE_IDLE:
                        isSlide=false;
                        notifyDataSetChanged();
                        break;
                    default:
                        isSlide=true;
                        break;
                }
                Log.i("滑动",isSlide+"");
            }
        });
        return viewHolder;
    }
    public  void downloadFile(final int num, final HotAdapter.ViewHolder holder) {
        PostUtil.arrayListPostHot.get(num).canDownload=false;
        for (int i = 0; i <  PostUtil.arrayListPostHot.get(num).getPhotoNumber(); i++) {
            if(CacheUtil.getBitmap( PostUtil.arrayListPostHot.get(num).getPhotoArrayList().get(i).getObjectId())!=null){
                switch (i){
                    case 0:
                        if( PostUtil.arrayListPostHot.get(num).getObjectId()==holder.latestImageOne.getTag()){
                            holder.latestImageOne.setImageBitmap(  CacheUtil.getBitmap( PostUtil.arrayListPostHot.get(num).getPhotoArrayList().get(i).getObjectId()));
                            PostUtil.arrayListPostHot.get(num).getImagelist().add(i,CacheUtil.getBitmap( PostUtil.arrayListPostHot.get(num).getPhotoArrayList().get(i).getObjectId()));
                        }
                        break;
                    case 1:
                        if( PostUtil.arrayListPostHot.get(num).getObjectId()==holder.latestImageTwo.getTag()){
                            holder.latestImageTwo.setImageBitmap(  CacheUtil.getBitmap( PostUtil.arrayListPostHot.get(num).getPhotoArrayList().get(i).getObjectId()));
                            PostUtil.arrayListPostHot.get(num).getImagelist().add(i,CacheUtil.getBitmap( PostUtil.arrayListPostHot.get(num).getPhotoArrayList().get(i).getObjectId()));
                        }
                        break;
                    case 2:
                        if( PostUtil.arrayListPostHot.get(num).getObjectId()==holder.latestImageThree.getTag()){
                            holder.latestImageThree.setImageBitmap( CacheUtil.getBitmap( PostUtil.arrayListPostHot.get(num).getPhotoArrayList().get(i).getObjectId()));
                            PostUtil.arrayListPostHot.get(num).getImagelist().add(i,CacheUtil.getBitmap( PostUtil.arrayListPostHot.get(num).getPhotoArrayList().get(i).getObjectId()));
                        }
                        break;
                    case 3:
                        if( PostUtil.arrayListPostHot.get(num).getObjectId()==holder.latestImageFour.getTag()){
                            holder.latestImageFour.setImageBitmap( CacheUtil.getBitmap( PostUtil.arrayListPostHot.get(num).getPhotoArrayList().get(i).getObjectId()));
                            PostUtil.arrayListPostHot.get(num).getImagelist().add(i,CacheUtil.getBitmap( PostUtil.arrayListPostHot.get(num).getPhotoArrayList().get(i).getObjectId()));
                        }
                        break;
                    default:
                        break;
                }
            }else {
                BmobFile icon =  PostUtil.arrayListPostHot.get(num).getPhotoArrayList().get(i).getPicture();
                final int finalI = i;
                icon.download(new DownloadFileListener() {
                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            PostUtil.arrayListPostHot.get(num).getImagelist().add(BitmapFactory.decodeFile(s));
                            CacheUtil.setBitmap(PostUtil.arrayListPostHot.get(num).getPhotoArrayList().get(finalI).getObjectId(),BitmapFactory.decodeFile(s));
                            switch (  PostUtil.arrayListPostHot.get(num).getImagelist().size()){
                                case 1:
                                    if( PostUtil.arrayListPostHot.get(num).getObjectId()==holder.latestImageOne.getTag()){
                                        holder.latestImageOne.setImageBitmap(  PostUtil.arrayListPostHot.get(num).getImagelist().get(0));
                                    }
                                    break;
                                case 2:
                                    if( PostUtil.arrayListPostHot.get(num).getObjectId()==holder.latestImageTwo.getTag()){
                                        holder.latestImageTwo.setImageBitmap(  PostUtil.arrayListPostHot.get(num).getImagelist().get(1));
                                    }
                                    break;
                                case 3:
                                    if( PostUtil.arrayListPostHot.get(num).getObjectId()==holder.latestImageThree.getTag()){
                                        holder.latestImageThree.setImageBitmap(  PostUtil.arrayListPostHot.get(num).getImagelist().get(2));
                                    }
                                    break;
                                case 4:
                                    if( PostUtil.arrayListPostHot.get(num).getObjectId()==holder.latestImageFour.getTag()){
                                        holder.latestImageFour.setImageBitmap(  PostUtil.arrayListPostHot.get(num).getImagelist().get(3));
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            PostUtil.arrayListPostHot.get(num).canDownload=true;
                            Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
    public void downloadAvatar(final int i, final HotAdapter.ViewHolder holder){
        if( PostUtil.arrayListPostHot.get(i).getAuthor().getPicture()!=null) {
            BmobFile icon = PostUtil.arrayListPostHot.get(i).getAuthor().getPicture();
            icon.download(new DownloadFileListener() {
                @Override
                public void onProgress(Integer integer, long l) {

                }

                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        PostUtil.arrayListPostHot.get(i).setAvatar(BitmapFactory.decodeFile(s));
                        if (PostUtil.arrayListPostHot.get(i).getAvatar() != null) {
                            PostUtil.arrayListPostHot.get(i).avatarBoolean = false;
                        }
                        if (PostUtil.arrayListPostHot.get(i).getObjectId() == holder.imageView.getTag()) {
                            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(s));
                        }
                    } else {
                        PostUtil.arrayListPostHot.get(i).avatarBoolean = true;
                        Toast.makeText(context, "请检查网络!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void changeParams(final HotAdapter.ViewHolder holder, final int position){
        ViewGroup.LayoutParams latestImageParamsOne=holder.latestImageOne.getLayoutParams();;
        ViewGroup.LayoutParams latestImageParamsTwo= holder.latestImageTwo.getLayoutParams();
        ViewGroup.LayoutParams latestImageParamsThree=holder.latestImageThree.getLayoutParams();
        ViewGroup.LayoutParams latestImageParamsFour=holder.latestImageFour.getLayoutParams();
        holder.latestImageOne.setVisibility(View.VISIBLE);
        holder.latestImageTwo.setVisibility(View.VISIBLE);
        holder.latestImageThree.setVisibility(View.VISIBLE);
        holder.latestImageFour.setVisibility(View.VISIBLE);
        holder.linearLatest.setVisibility(View.VISIBLE);
        switch (PostUtil.arrayListPostHot.get(position).getPhotoNumber()){
            case 0:
                holder.latestImageOne.setVisibility(View.GONE);
                holder.latestImageTwo.setVisibility(View.GONE);
                holder.latestImageThree.setVisibility(View.GONE);
                holder.latestImageFour.setVisibility(View.GONE);
                holder.linearLatest.setVisibility(View.GONE);
                break;
            case 1:
                latestImageParamsOne.height=toDp(175);
                latestImageParamsOne.width=toDp(90);
                holder.latestImageOne.setLayoutParams(latestImageParamsOne);
                holder.latestImageOne.setTag(PostUtil.arrayListPostHot.get(position).getObjectId());
                holder.latestImageTwo.setVisibility(View.GONE);
                holder.latestImageThree.setVisibility(View.GONE);
                holder.latestImageFour.setVisibility(View.GONE);
                break;
            case 2:
                latestImageParamsOne.height=toDp(175);
                latestImageParamsOne.width=toDp(90);
                holder.latestImageOne.setLayoutParams(latestImageParamsOne);
                holder.latestImageOne.setTag(PostUtil.arrayListPostHot.get(position).getObjectId());
                latestImageParamsTwo.height=toDp(175);
                latestImageParamsTwo.width=toDp(90);
                holder.latestImageTwo.setTag(PostUtil.arrayListPostHot.get(position).getObjectId());
                holder.latestImageTwo.setLayoutParams(latestImageParamsTwo);
                holder.latestImageThree.setVisibility(View.GONE);
                holder.latestImageFour.setVisibility(View.GONE);
                break;
            case 3:
                latestImageParamsOne.height=toDp(90);
                latestImageParamsOne.width=toDp(90);
                holder.latestImageOne.setLayoutParams(latestImageParamsOne);
                holder.latestImageOne.setTag(PostUtil.arrayListPostHot.get(position).getObjectId());
                latestImageParamsTwo.height=toDp(90);
                latestImageParamsTwo.width=toDp(90);
                holder.latestImageTwo.setTag(PostUtil.arrayListPostHot.get(position).getObjectId());
                holder.latestImageTwo.setLayoutParams(latestImageParamsTwo);
                latestImageParamsThree.width=toDp(185)+20;
                latestImageParamsThree.height=toDp(90);
                holder.latestImageThree.setLayoutParams(latestImageParamsThree);
                holder.latestImageThree.setTag(PostUtil.arrayListPostHot.get(position).getObjectId());
                holder.latestImageFour.setVisibility(View.GONE);
                break;
            case 4:
                latestImageParamsOne.height=toDp(90);
                latestImageParamsOne.width=toDp(90);
                holder.latestImageOne.setLayoutParams(latestImageParamsOne);
                holder.latestImageOne.setTag(PostUtil.arrayListPostHot.get(position).getObjectId());
                latestImageParamsTwo.height=toDp(90);
                latestImageParamsTwo.width=toDp(90);
                holder.latestImageTwo.setTag(PostUtil.arrayListPostHot.get(position).getObjectId());
                holder.latestImageTwo.setLayoutParams(latestImageParamsTwo);
                latestImageParamsThree.width=toDp(90);
                latestImageParamsThree.height=toDp(90);
                holder.latestImageThree.setLayoutParams(latestImageParamsThree);
                holder.latestImageThree.setTag(PostUtil.arrayListPostHot.get(position).getObjectId());
                latestImageParamsFour.width=toDp(90);
                latestImageParamsFour.height=toDp(90);
                holder.latestImageFour.setLayoutParams(latestImageParamsFour);
                holder.latestImageFour.setTag(PostUtil.arrayListPostHot.get(position).getObjectId());
                break;
            default:
                break;
        }
    }
    @Override
    public void onBindViewHolder(final HotAdapter.ViewHolder holder, final int position) {
        holder.isClick=false;
        holder.flag = true;
        holder.nameView.setText(PostUtil.arrayListPostHot.get(position).getAuthor().getNickname());
        holder.contentView.setText(PostUtil.arrayListPostHot.get(position).getContent().trim());
        if(holder.contentView.getText().toString().trim().length()>36){
            Log.i(position+"控制文件:",holder.contentView.getText().toString());
            holder.contentView.setLines(2);
            holder.contentView.setEllipsize(TextUtils.TruncateAt.END);
            holder.controlView.setText("展开");
            holder.controlView.setVisibility(View.VISIBLE);
        }else {
            holder.controlView.setVisibility(View.GONE);
        }
        holder.imageView.setTag(PostUtil.arrayListPostHot.get(position).getObjectId());
        holder.timeView.setText(PostUtil.arrayListPostHot.get(position).getCreatedAt());
        holder.praiseNumber.setText(PostUtil.arrayListPostHot.get(position).getPraseNumber()+"");
        holder.commentNumber.setText(PostUtil.arrayListPostHot.get(position).getCommentNumber()+"");
        holder.deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostUtil.arrayListPostHot.get(position).getImagelist().clear();
                PostUtil.arrayListPostHot.get(position).getPhotoArrayList().clear();
                PostUtil.arrayListPostHot.get(position).getLikeArrayList().clear();
                PostUtil.arrayListPostHot.remove(position);
                holder.deletePost.setVisibility(View.GONE);
                notifyDataSetChanged();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.deletePost.getVisibility()==View.GONE){
                    holder.deletePost.setVisibility(View.VISIBLE);
                }else {
                    holder.deletePost.setVisibility(View.GONE);
                }
            }
        });
        holder.thingsType.setText(PostUtil.arrayListPostHot.get(position).getType());
        Log.i(position+"进入头像:", isSlide+":"+holder.contentView.getLineCount()+"");
        if( PostUtil.arrayListPostHot.get(position).getAvatar()==null&&(!isSlide)&&PostUtil.arrayListPostHot.get(position).avatarBoolean){
            Log.i(position+"下载头像:", PostUtil.arrayListPostHot.get(position).getImagelist().size()+"");
            downloadAvatar(position,holder);
        }else if( PostUtil.arrayListPostHot.get(position).getAvatar()!=null) {
            Log.i(position+"头像:", PostUtil.arrayListPostHot.get(position).getImagelist().size()+"");
            holder.imageView.setImageBitmap( PostUtil.arrayListPostHot.get(position).getAvatar());
        }
        changeParams(holder,position);
        if(PostUtil.arrayListPostHot.get(position).getImagelist().size()<PostUtil.arrayListPostHot.get(position).getPhotoNumber()&&(!isSlide)&&PostUtil.arrayListPostHot.get(position).canDownload){
            Log.i(position+"下载文件:", PostUtil.arrayListPostHot.get(position).getImagelist().size()+"");
            downloadFile(position,holder);
        }else {
            Log.i(position+"文件:", PostUtil.arrayListPostHot.get(position).getImagelist().size()+"");
            switch (PostUtil.arrayListPostHot.get(position).getImagelist().size()){
                case 1:
                    holder.latestImageOne.setImageBitmap(  PostUtil.arrayListPostHot.get(position).getImagelist().get(0));
                    break;
                case 2:
                    holder.latestImageOne.setImageBitmap(  PostUtil.arrayListPostHot.get(position).getImagelist().get(0));
                    holder.latestImageTwo.setImageBitmap(  PostUtil.arrayListPostHot.get(position).getImagelist().get(1));
                    break;
                case 3:
                    holder.latestImageOne.setImageBitmap(  PostUtil.arrayListPostHot.get(position).getImagelist().get(0));
                    holder.latestImageTwo.setImageBitmap(  PostUtil.arrayListPostHot.get(position).getImagelist().get(1));
                    holder.latestImageThree.setImageBitmap(  PostUtil.arrayListPostHot.get(position).getImagelist().get(2));
                    break;
                case 4:
                    holder.latestImageOne.setImageBitmap(  PostUtil.arrayListPostHot.get(position).getImagelist().get(0));
                    holder.latestImageTwo.setImageBitmap(  PostUtil.arrayListPostHot.get(position).getImagelist().get(1));
                    holder.latestImageThree.setImageBitmap(  PostUtil.arrayListPostHot.get(position).getImagelist().get(2));
                    holder.latestImageFour.setImageBitmap(  PostUtil.arrayListPostHot.get(position).getImagelist().get(3));
                    break;
                default:
                    break;
            }
        }
        holder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("from",1);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        holder.commentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("from",1);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        holder.latestImageFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PostUtil.arrayListPostHot.get(position).getImagelist().size()==4){
                    Intent intent=new Intent(context, PhotoActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("ID",3);
                    intent.putExtra("from",2);
                    context.startActivity(intent);
                }
            }
        });
        holder.latestImageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PostUtil.arrayListPostHot.get(position).getImagelist().size()>=1){
                    Intent intent=new Intent(context, PhotoActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("ID",0);
                    intent.putExtra("from",2);
                    context.startActivity(intent);
                }
            }
        });
        holder.latestImageTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PostUtil.arrayListPostHot.get(position).getImagelist().size()>=2){
                    Intent intent=new Intent(context, PhotoActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("ID",1);
                    intent.putExtra("from",2);
                    context.startActivity(intent);
                }
            }
        });
        holder.latestImageThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PostUtil.arrayListPostHot.get(position).getImagelist().size()>=3){
                    Intent intent=new Intent(context, PhotoActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("ID",2);
                    intent.putExtra("from",2);
                    context.startActivity(intent);
                }
            }
        });
        final GoodView goodView = new GoodView(context);
        if(PostUtil.arrayListPostHot.get(position).getLike()){
            holder.praiseImage.setImageResource(R.mipmap.good_checked);
            holder.isClick =true;
        }else {
            holder.isClick =false;
            holder.praiseImage.setImageResource(R.mipmap.good);
        }
        holder.praiseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.isClick){
                    holder.isClick=true;
                    holder.praiseImage.setImageResource(R.mipmap.good_checked);
                    goodView.setText("+1");
                    goodView.show(v);
                    if(NetWorkReceive.hasNetWork){
                        final Like like=new Like();
                        like.setAuthor(BmobUser.getCurrentUser(User.class));
                        like.setPost(PostUtil.arrayListPostHot.get(position));
                        like.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e==null){
                                    Post post = new Post();
                                    post.setPraseNumber(PostUtil.arrayListPostHot.get(position).getPraseNumber()+1);
                                    post.update(PostUtil.arrayListPostHot.get(position).getObjectId(), new UpdateListener() {

                                        @Override
                                        public void done(BmobException e) {
                                            PostUtil.arrayListPostHot.get(position).setPraseNumber( PostUtil.arrayListPostHot.get(position).getPraseNumber()+1);
                                            holder.praiseNumber.setText(PostUtil.arrayListPostHot.get(position).getPraseNumber()+"");

                                        }

                                    });
                                    PostUtil.arrayListPostHot.get(position).getLikeArrayList().add(like);
                                    PostUtil.arrayListPostHot.get(position).setLike(true);
                                }
                            }
                        });
                    }
                }
                else {
                    holder.isClick = false;
                    holder.praiseImage.setImageResource(R.mipmap.good);
                    goodView.setText("-1");
                    goodView.show(v);
                    if (NetWorkReceive.hasNetWork) {
                        Like like = new Like();
                        like.setObjectId(PostUtil.arrayListPostHot.get(position).getLikeArrayList().get(0).getObjectId());
                        like.delete(new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Post post = new Post();
                                    post.setPraseNumber(PostUtil.arrayListPostHot.get(position).getPraseNumber()-1);
                                    post.update(PostUtil.arrayListPostHot.get(position).getObjectId(), new UpdateListener() {

                                        @Override
                                        public void done(BmobException e) {
                                            PostUtil.arrayListPostHot.get(position).setPraseNumber(  PostUtil.arrayListPostHot.get(position).getPraseNumber()-1);
                                            holder.praiseNumber.setText( PostUtil.arrayListPostHot.get(position).getPraseNumber()+"");
                                        }

                                    });
                                    PostUtil.arrayListPostHot.get(position).getLikeArrayList().clear();
                                    PostUtil.arrayListPostHot.get(position).setLike(false);
                                }
                            }
                        });
                    }
                }
            }
        });
        holder.controlView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(holder.flag){
                    holder.flag = false;
                    holder.contentView.setEllipsize(null); // 展开
                    holder.contentView.setSingleLine(holder.flag);
                    holder.controlView.setText("收起");
                }else{
                    holder.flag = true;
                    holder.contentView.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                    holder.contentView.setLines(2);
                    holder.controlView.setText("展开");
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return  PostUtil.arrayListPostHot.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        boolean isClick=false;
        Boolean flag = true;
        TextView nameView;
        ImageView commentImage;
        ImageView praiseImage;
        CircleImageView imageView;
        TextView contentView;
        TextView timeView;
        TextView delete;
        TextView deletePost;
        RoundImageView latestImageOne;
        RoundImageView latestImageTwo;
        RoundImageView latestImageThree;
        RoundImageView latestImageFour;
        TextView controlView;
        TextView praiseNumber;
        TextView commentNumber;
        TextView thingsType;
        LinearLayout linearLatest;
        public ViewHolder(View itemView) {
            super(itemView);
            nameView=(TextView)itemView.findViewById(R.id.latest_name);
            contentView=(TextView)itemView.findViewById(R.id.latest_content);
            imageView=(CircleImageView)itemView.findViewById(R.id.photo_latest);
            controlView=(TextView)itemView.findViewById(R.id.control);
            praiseImage=(ImageView)itemView.findViewById(R.id.praise);
            timeView=(TextView)itemView.findViewById(R.id.latest_time);
            thingsType=(TextView)itemView.findViewById(R.id.typethings);
            delete=(TextView)itemView.findViewById(R.id.delete);
            deletePost=(TextView)itemView.findViewById(R.id.deletePost);
            praiseNumber=(TextView)itemView.findViewById(R.id.praiseNumber);
            commentNumber=(TextView)itemView.findViewById(R.id.commentNumber);
            commentImage=(ImageView)itemView.findViewById(R.id.comment);
            latestImageOne=(RoundImageView)itemView.findViewById(R.id.latest_image_one);
            latestImageTwo=(RoundImageView)itemView.findViewById(R.id.latest_image_two);
            latestImageThree=(RoundImageView)itemView.findViewById(R.id.latest_image_three);
            latestImageFour=(RoundImageView)itemView.findViewById(R.id.latest_image_four);
            linearLatest=(LinearLayout)itemView.findViewById(R.id.linear_latest);
        }
    }
    public int toDp(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }
}
