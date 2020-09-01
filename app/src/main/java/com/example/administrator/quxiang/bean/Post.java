package com.example.administrator.quxiang.bean;

import android.graphics.Bitmap;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2020/4/25.
 */

public class Post extends BmobObject {
    public boolean getLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    /**
     * 帖子内容

     */

public boolean canDownload=true;
public boolean avatarBoolean=true;
public boolean like;
    public boolean isAvatarBoolean() {
        return avatarBoolean;
    }

    public void setAvatarBoolean(boolean avatarBoolean) {
        this.avatarBoolean = avatarBoolean;
    }

    public boolean isCanDownload() {
        return canDownload;

    }

    public void setCanDownload(boolean canDownload) {
        this.canDownload = canDownload;
    }

    private String content;
    /**
     * 发布者
     */
    private User author;

    public int getPhotoNumber() {
        return photoNumber;
    }

    public void setPhotoNumber(int photoNumber) {
        this.photoNumber = photoNumber;
    }
    public ArrayList<Like> getLikeArrayList() {
        return likeArrayList;
    }

    public void setLikeArrayList(ArrayList<Like> likeArrayList) {
        this.likeArrayList = likeArrayList;
    }

    /**

     * 图片
     */
    private ArrayList<Bitmap> imagelist=new ArrayList<>();
    private Bitmap avatar;
    private String type;
private int photoNumber;
    public  ArrayList<Like>likeArrayList=new ArrayList<>();
    public ArrayList<Photo> getPhotoArrayList() {
        if(photoArrayList==null){
            photoArrayList=new ArrayList<>();
        }
        return photoArrayList;
    }

    public void setPhotoArrayList(ArrayList<Photo> photoArrayList) {
        this.photoArrayList = photoArrayList;
    }

    private int commentNumber;
    private int praseNumber;
    private ArrayList<Photo>photoArrayList=new ArrayList<>();
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
public void setAvatar(Bitmap bitmap){
    avatar=bitmap;
}
public Bitmap getAvatar(){
    return avatar;
}
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public ArrayList<Bitmap> getImagelist() {
        return imagelist;
    }

    public void setImagelist(ArrayList<Bitmap> imagelist) {
        this.imagelist = imagelist;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getPraseNumber() {
        return praseNumber;
    }

    public void setPraseNumber(int praseNumber) {
        this.praseNumber = praseNumber;
    }
}
