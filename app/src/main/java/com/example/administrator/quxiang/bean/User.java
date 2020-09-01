package com.example.administrator.quxiang.bean;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by Administrator on 2020/3/1.
 */

public class User extends BmobUser {


    /**
     * 昵称
     */
    private String nickname;

    public ArrayList<String> getLikethings() {
        return likethings;
    }

    public void setLikethings(ArrayList<String> likethings) {
        this.likethings = likethings;
    }

    /**
     * 国家
     */
    private ArrayList<String>likethings=new ArrayList<>();
    private String country;

    public String getNativePicture() {
        return nativePicture;
    }

    public void setNativePicture(String nativePicture) {
        this.nativePicture = nativePicture;
    }

    private String nativePicture;
    /**
     * 年龄
     */
    private int age;


    /**
     * 性别
     */
    private String gender;


    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }


    public void setAge(int age) {
        this.age = age;
    }

    /**
     * 头像

     */
    private BmobFile picture;



private String objectid;
    public String getNickname() {
        return nickname;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public User setCountry(String country) {
        this.country = country;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public User setGender(String gender) {
        this.gender = gender;
        return this;
    }
    public BmobFile getPicture() {
        return picture;
    }

    public User setPicture(BmobFile picture) {
        this.picture = picture;
        return this;
    }
}

