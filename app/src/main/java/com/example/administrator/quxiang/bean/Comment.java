package com.example.administrator.quxiang.bean;

import android.graphics.Bitmap;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2020/5/23.
 */

public class Comment extends BmobObject {
    private User author;
    private Post post;
    private Bitmap avatar;

    public boolean isAvatarBoolean() {
        return avatarBoolean;
    }

    public void setAvatarBoolean(boolean avatarBoolean) {
        this.avatarBoolean = avatarBoolean;
    }

    public boolean avatarBoolean=true;
    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;
}
