package com.example.administrator.quxiang.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2020/5/21.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    private View view;
    RecyclerView recyclerViewLatest;
    LinearLayoutManager linearLayoutManager;
    boolean isSlide=false;
    Bitmap bitmap;
    User user;
    NetWorkReceive.OnNetWorkChange onNetWorkChangeMy;
    public MyAdapter(Context context,RecyclerView recyclerViewLatest, LinearLayoutManager linearLayoutManager){
        this.context=context;
        this.recyclerViewLatest=recyclerViewLatest;
        this.linearLayoutManager=linearLayoutManager;
        user=BmobUser.getCurrentUser(User.class);
        onNetWorkChangeMy=new NetWorkReceive.OnNetWorkChange() {
            @Override
            public void notifyChange() {
                notifyDataSetChanged();
            }
        };
        NetWorkReceive.getInstance().setOnNetWorkChange(onNetWorkChangeMy);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        recyclerViewLatest.setItemViewCacheSize(20);
        recyclerViewLatest.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    public  void downloadFile(final int num, final ViewHolder holder) {
        PostUtil.arrayListPostMy.get(num).canDownload=false;
        for (int i = 0; i <  PostUtil.arrayListPostMy.get(num).getPhotoNumber(); i++) {
            if(CacheUtil.getBitmap( PostUtil.arrayListPostMy.get(num).getPhotoArrayList().get(i).getObjectId())!=null){
                switch (i){
                    case 0:
                        if( PostUtil.arrayListPostMy.get(num).getObjectId()==holder.latestImageOne.getTag()){
                            holder.latestImageOne.setImageBitmap(  CacheUtil.getBitmap( PostUtil.arrayListPostMy.get(num).getPhotoArrayList().get(i).getObjectId()));
                            PostUtil.arrayListPostMy.get(num).getImagelist().add(i,CacheUtil.getBitmap( PostUtil.arrayListPostMy.get(num).getPhotoArrayList().get(i).getObjectId()));
                        }
                        break;
                    case 1:
                        if( PostUtil.arrayListPostMy.get(num).getObjectId()==holder.latestImageTwo.getTag()){
                            holder.latestImageTwo.setImageBitmap(  CacheUtil.getBitmap( PostUtil.arrayListPostMy.get(num).getPhotoArrayList().get(i).getObjectId()));
                            PostUtil.arrayListPostMy.get(num).getImagelist().add(i,CacheUtil.getBitmap( PostUtil.arrayListPostMy.get(num).getPhotoArrayList().get(i).getObjectId()));
                        }
                        break;
                    case 2:
                        if( PostUtil.arrayListPostMy.get(num).getObjectId()==holder.latestImageThree.getTag()){
                            holder.latestImageThree.setImageBitmap( CacheUtil.getBitmap( PostUtil.arrayListPostMy.get(num).getPhotoArrayList().get(i).getObjectId()));
                            PostUtil.arrayListPostMy.get(num).getImagelist().add(i,CacheUtil.getBitmap( PostUtil.arrayListPostMy.get(num).getPhotoArrayList().get(i).getObjectId()));
                        }
                        break;
                    case 3:
                        if( PostUtil.arrayListPostMy.get(num).getObjectId()==holder.latestImageFour.getTag()){
                            holder.latestImageFour.setImageBitmap( CacheUtil.getBitmap( PostUtil.arrayListPostMy.get(num).getPhotoArrayList().get(i).getObjectId()));
                            PostUtil.arrayListPostMy.get(num).getImagelist().add(i,CacheUtil.getBitmap( PostUtil.arrayListPostMy.get(num).getPhotoArrayList().get(i).getObjectId()));
                        }
                        break;
                    default:
                        break;
                }
            }else {
                BmobFile icon =  PostUtil.arrayListPostMy.get(num).getPhotoArrayList().get(i).getPicture();
                final int finalI = i;
                icon.download(new DownloadFileListener() {
                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            PostUtil.arrayListPostMy.get(num).getImagelist().add(BitmapFactory.decodeFile(s));
                            CacheUtil.setBitmap(PostUtil.arrayListPostMy.get(num).getPhotoArrayList().get(finalI).getObjectId(),BitmapFactory.decodeFile(s));
                            switch (  PostUtil.arrayListPostMy.get(num).getImagelist().size()){
                                case 1:
                                    if( PostUtil.arrayListPostMy.get(num).getObjectId()==holder.latestImageOne.getTag()){
                                        holder.latestImageOne.setImageBitmap(  PostUtil.arrayListPostMy.get(num).getImagelist().get(0));
                                    }
                                    break;
                                case 2:
                                    if( PostUtil.arrayListPostMy.get(num).getObjectId()==holder.latestImageTwo.getTag()){
                                        holder.latestImageTwo.setImageBitmap(  PostUtil.arrayListPostMy.get(num).getImagelist().get(1));
                                    }
                                    break;
                                case 3:
                                    if( PostUtil.arrayListPostMy.get(num).getObjectId()==holder.latestImageThree.getTag()){
                                        holder.latestImageThree.setImageBitmap(  PostUtil.arrayListPostMy.get(num).getImagelist().get(2));
                                    }
                                    break;
                                case 4:
                                    if( PostUtil.arrayListPostMy.get(num).getObjectId()==holder.latestImageFour.getTag()){
                                        holder.latestImageFour.setImageBitmap(  PostUtil.arrayListPostMy.get(num).getImagelist().get(3));
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            PostUtil.arrayListPostMy.get(num).canDownload=true;
                            Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
    public void downloadAvatar(final ViewHolder viewHolder, final int position){
        if(PostUtil.arrayListPostMy.get(position).getAvatar()!=null){
            viewHolder.imageView.setImageBitmap(bitmap);
        }
        else {
            if (!TextUtils.isEmpty(user.getNativePicture())) {
                bitmap = BitmapFactory.decodeFile(user.getNativePicture());
            }
            if (bitmap != null) {
                viewHolder.imageView.setImageBitmap(bitmap);
                PostUtil.arrayListPostMy.get(position).setAvatar(bitmap);
            } else if (user.getPicture() != null) {
                user.getPicture().download(new DownloadFileListener() {
                    @Override
                    public void onProgress(Integer integer, long l) {

                    }

                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Bitmap bitmap = BitmapFactory.decodeFile(s);
                            //设置圆形头像并显示
                            viewHolder.imageView.setImageBitmap(bitmap);
                            PostUtil.arrayListPostMy.get(position).setAvatar(bitmap);
                        }
                    }
                });
            }
        }
    }
    public void changeVisable(ViewHolder viewHolder,int position) {
       viewHolder.latestImageOne .setVisibility(View.VISIBLE);
        viewHolder.latestImageTwo.setVisibility(View.VISIBLE);
        viewHolder.latestImageThree.setVisibility(View.VISIBLE);
        viewHolder.latestImageFour.setVisibility(View.VISIBLE);
                switch (PostUtil.arrayListPostMy.get(position).getPhotoNumber()) {
                    case 0:
                        viewHolder.latestImageOne.setVisibility(View.GONE);
                        viewHolder.latestImageTwo.setVisibility(View.GONE);
                        viewHolder.latestImageThree.setVisibility(View.GONE);
                        viewHolder.latestImageFour.setVisibility(View.GONE);
                        break;
                    case 1:
                        viewHolder.latestImageTwo.setVisibility(View.GONE);
                        viewHolder.latestImageOne.setTag(PostUtil.arrayListPostMy.get(position).getObjectId());
                        viewHolder.latestImageThree.setVisibility(View.GONE);
                        viewHolder.latestImageFour.setVisibility(View.GONE);
                        break;
                    case 2:
                        viewHolder.latestImageThree.setVisibility(View.GONE);
                        viewHolder.latestImageOne.setTag(PostUtil.arrayListPostMy.get(position).getObjectId());
                        viewHolder.latestImageTwo.setTag(PostUtil.arrayListPostMy.get(position).getObjectId());
                        viewHolder.latestImageFour.setVisibility(View.GONE);
                        break;
                    case 3:
                        viewHolder.latestImageFour.setVisibility(View.GONE);
                        viewHolder.latestImageOne.setTag(PostUtil.arrayListPostMy.get(position).getObjectId());
                        viewHolder.latestImageTwo.setTag(PostUtil.arrayListPostMy.get(position).getObjectId());
                        viewHolder.latestImageThree.setTag(PostUtil.arrayListPostMy.get(position).getObjectId());
                        break;
                    case 4:
                        viewHolder.latestImageOne.setTag(PostUtil.arrayListPostMy.get(position).getObjectId());
                        viewHolder.latestImageTwo.setTag(PostUtil.arrayListPostMy.get(position).getObjectId());
                        viewHolder.latestImageThree.setTag(PostUtil.arrayListPostMy.get(position).getObjectId());
                        viewHolder.latestImageFour.setTag(PostUtil.arrayListPostMy.get(position).getObjectId());
                        break;
                    default:
                        break;
                }
    }
    public void showDialog(final ViewHolder holder, final int position) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this post!")
                .setCancelText("No,cancel plx!")
                .setConfirmText("Yes,delete it!")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {
                        Post post = new Post();
                        post.delete(PostUtil.arrayListPostMy.get(position).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    sDialog.setTitleText("Deleted!")
                                            .setContentText("Your imaginary file has been deleted!")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    PostUtil.arrayListPostMy.get(position).getImagelist().clear();
                                    PostUtil.arrayListPostMy.get(position).getPhotoArrayList().clear();
                                    PostUtil.arrayListPostMy.remove(position);
                                    holder.deletePost.setVisibility(View.GONE);
                                    notifyDataSetChanged();
                                } else {
                                    sDialog.cancel();
                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("请检查网络!")
                                            .show();
                                }
                            }
                        });
                    }
                })
                .show();
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.flag = true;
        holder.nameView.setText(PostUtil.arrayListPostMy.get(position).getAuthor().getNickname());
        holder.contentView.setText(PostUtil.arrayListPostMy.get(position).getContent().trim());
        if(holder.contentView.getText().toString().trim().length()>36){
            Log.i(position+"控制文件:",holder.contentView.getText().toString());
            holder.contentView.setLines(2);
            holder.contentView.setEllipsize(TextUtils.TruncateAt.END);
            holder.controlView.setText("展开");
            holder.controlView.setVisibility(View.VISIBLE);
        }else {
            holder.controlView.setVisibility(View.GONE);
        }
        holder.imageView.setTag(PostUtil.arrayListPostMy.get(position).getObjectId());
        holder.timeView.setText(PostUtil.arrayListPostMy.get(position).getCreatedAt());
        Log.i(position+"进入头像:", isSlide+":"+holder.contentView.getLineCount()+"");
        holder.deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(holder,position);
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
        changeVisable(holder,position);
        downloadAvatar(holder,position);
        if(PostUtil.arrayListPostMy.get(position).getImagelist().size()<PostUtil.arrayListPostMy.get(position).getPhotoNumber()&&(!isSlide)&&PostUtil.arrayListPostMy.get(position).canDownload){
            Log.i(position+"下载文件:", PostUtil.arrayListPostMy.get(position).getImagelist().size()+"");
            downloadFile(position,holder);
        }else {
            Log.i(position+"文件:", PostUtil.arrayListPostMy.get(position).getImagelist().size()+"");
            switch (PostUtil.arrayListPostMy.get(position).getImagelist().size()){
                case 1:
                    holder.latestImageOne.setImageBitmap(  PostUtil.arrayListPostMy.get(position).getImagelist().get(0));
                    break;
                case 2:
                    holder.latestImageOne.setImageBitmap(  PostUtil.arrayListPostMy.get(position).getImagelist().get(0));
                    holder.latestImageTwo.setImageBitmap(  PostUtil.arrayListPostMy.get(position).getImagelist().get(1));
                    break;
                case 3:
                    holder.latestImageOne.setImageBitmap(  PostUtil.arrayListPostMy.get(position).getImagelist().get(0));
                    holder.latestImageTwo.setImageBitmap(  PostUtil.arrayListPostMy.get(position).getImagelist().get(1));
                    holder.latestImageThree.setImageBitmap(  PostUtil.arrayListPostMy.get(position).getImagelist().get(2));
                    break;
                case 4:
                    holder.latestImageOne.setImageBitmap(  PostUtil.arrayListPostMy.get(position).getImagelist().get(0));
                    holder.latestImageTwo.setImageBitmap(  PostUtil.arrayListPostMy.get(position).getImagelist().get(1));
                    holder.latestImageThree.setImageBitmap(  PostUtil.arrayListPostMy.get(position).getImagelist().get(2));
                    holder.latestImageFour.setImageBitmap(  PostUtil.arrayListPostMy.get(position).getImagelist().get(3));
                    break;
                default:
                    break;
            }
        }
        holder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("from",3);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        holder.latestImageFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PostUtil.arrayListPostMy.get(position).getImagelist().size()==4){
                    Intent intent=new Intent(context, PhotoActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("ID",3);
                    intent.putExtra("from",4);
                    context.startActivity(intent);
                }
            }
        });
        holder.latestImageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PostUtil.arrayListPostMy.get(position).getImagelist().size()>=1){
                    Intent intent=new Intent(context, PhotoActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("ID",0);
                    intent.putExtra("from",4);
                    context.startActivity(intent);
                }
            }
        });
        holder.latestImageTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PostUtil.arrayListPostMy.get(position).getImagelist().size()>=2){
                    Intent intent=new Intent(context, PhotoActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("ID",1);
                    intent.putExtra("from",4);
                    context.startActivity(intent);
                }
            }
        });
        holder.latestImageThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PostUtil.arrayListPostMy.get(position).getImagelist().size()>=3){
                    Intent intent=new Intent(context, PhotoActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("ID",2);
                    intent.putExtra("from",4);
                    context.startActivity(intent);
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
        return PostUtil.arrayListPostMy.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        Boolean flag = true;
        TextView nameView;
        CircleImageView imageView;
        TextView contentView;
        TextView timeView;
        RoundImageView latestImageOne;
        RoundImageView latestImageTwo;
        RoundImageView latestImageThree;
        RoundImageView latestImageFour;
        TextView controlView;
        TextView delete;
        TextView deletePost;
        public ViewHolder(View itemView) {
            super(itemView);
            nameView=(TextView)itemView.findViewById(R.id.my_name);
            contentView=(TextView)itemView.findViewById(R.id.my_content);
            imageView=(CircleImageView)itemView.findViewById(R.id.photo_my);
            controlView=(TextView)itemView.findViewById(R.id.control);
            timeView=(TextView)itemView.findViewById(R.id.my_time);
            deletePost=(TextView)itemView.findViewById(R.id.deletePost);
            delete=(TextView)itemView.findViewById(R.id.delete);
            latestImageOne=(RoundImageView)itemView.findViewById(R.id.my_image_one);
            latestImageTwo=(RoundImageView)itemView.findViewById(R.id.my_image_two);
            latestImageThree=(RoundImageView)itemView.findViewById(R.id.my_image_three);
            latestImageFour=(RoundImageView)itemView.findViewById(R.id.my_image_four);
        }
    }
    public int toDp(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }
}