package com.example.administrator.quxiang.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2020/5/14.
 */

public class Like extends BmobObject {
    private User author;
    private Post post;

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
}
