package com.example.administrator.quxiang.adapter;

import android.content.Context;
import android.content.Intent;
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

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder>  {
    Context context;
    private View view;
    RecyclerView recyclerViewLatest;
    LinearLayoutManager linearLayoutManager;
    boolean isSlide=false;
    NetWorkReceive.OnNetWorkChange onNetWorkChangeDetail;
    public DetailAdapter(Context context,RecyclerView recyclerViewLatest, LinearLayoutManager linearLayoutManager){
        this.context=context;
        this.recyclerViewLatest=recyclerViewLatest;
        this.linearLayoutManager=linearLayoutManager;
        onNetWorkChangeDetail=new NetWorkReceive.OnNetWorkChange() {
            @Override
            public void notifyChange() {
                notifyDataSetChanged();
            }
        };
        NetWorkReceive.getInstance().setOnNetWorkChange(onNetWorkChangeDetail);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item,parent,false);
        final DetailAdapter.ViewHolder viewHolder=new DetailAdapter.ViewHolder(view);
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
    public void downloadAvatar(final int i, final DetailAdapter.ViewHolder holder){
        if( PostUtil.comments.get(i).getAuthor().getPicture()!=null) {
            BmobFile icon = PostUtil.comments.get(i).getAuthor().getPicture();
            icon.download(new DownloadFileListener() {
                @Override
                public void onProgress(Integer integer, long l) {

                }

                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        PostUtil.comments.get(i).setAvatar(BitmapFactory.decodeFile(s));
                        if (PostUtil.comments.get(i).getAvatar() != null) {
                            PostUtil.comments.get(i).avatarBoolean = false;
                        }
                        if (PostUtil.comments.get(i).getObjectId() == holder.imageView.getTag()) {
                            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(s));
                        }
                    } else {
                        PostUtil.comments.get(i).avatarBoolean = true;
                        Toast.makeText(context, "请检查网络!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameView.setText(PostUtil.comments.get(position).getAuthor().getNickname());
        holder.contentView.setText(PostUtil.comments.get(position).getContent().trim());
        holder.imageView.setTag(PostUtil.comments.get(position).getObjectId());
        holder.timeView.setText(PostUtil.comments.get(position).getCreatedAt());
        holder.textViewLou.setText(position+"楼");
        Log.i(position + "进入头像:", isSlide + ":" + holder.contentView.getLineCount() + "");
        if (PostUtil.comments.get(position).getAvatar() == null && (!isSlide) && PostUtil.comments.get(position).avatarBoolean) {
            downloadAvatar(position, holder);
        } else if (PostUtil.comments.get(position).getAvatar() != null) {
            holder.imageView.setImageBitmap(PostUtil.comments.get(position).getAvatar());
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return PostUtil.comments.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        CircleImageView imageView;
        TextView contentView;
        TextView timeView;
TextView textViewLou;
        public ViewHolder(View itemView) {
            super(itemView);
            nameView=(TextView)itemView.findViewById(R.id.latest_name);
            contentView=(TextView)itemView.findViewById(R.id.latest_content);
            imageView=(CircleImageView)itemView.findViewById(R.id.photo_latest);
textViewLou=(TextView)itemView.findViewById(R.id.lou);

            timeView=(TextView)itemView.findViewById(R.id.latest_time);

        }
    }
}
