package com.example.administrator.quxiang.Utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Administrator on 2020/5/18.
 */

public class CacheUtil {
    static int maxMemory=(int)(Runtime.getRuntime().maxMemory()/1024);
    static int casheSize=maxMemory/8;
   public static LruCache<String ,Bitmap> mMemoryCashe=new LruCache<String, Bitmap>(casheSize){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes()*value.getHeight()/1024;
        }
    };
    public static Bitmap getBitmap(String string){
        return mMemoryCashe.get(string);
    }
    public static void setBitmap(String s,Bitmap bitmap){
        mMemoryCashe.put(s,bitmap);
    }
    public static void clear(){
        mMemoryCashe.evictAll();
    }
}
