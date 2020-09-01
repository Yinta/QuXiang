package com.example.administrator.quxiang.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.quxiang.Utils.BitmapUtil;
import com.example.administrator.quxiang.Utils.PostUtil;
import com.example.administrator.quxiang.bean.Photo;
import com.example.administrator.quxiang.bean.Post;
import com.example.administrator.quxiang.fragment.SquareFragment;
import com.example.administrator.quxiang.interFace.UploadListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class UploadService extends Service {
    Handler handler = new Handler();
    int currentIndex=1;

    public UploadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return uploadBinder;
    }
    UploadBinder uploadBinder=new UploadBinder();
    public class UploadBinder extends Binder{
        public UploadListener uploadListener;
        public void startUpload(Post post, final View view){
            uploadListener.setProgressBarVisable();
            insertObjectWithPicture(post, view);
        }
        public void setProgressListener(UploadListener uploadListener){
            this.uploadListener=uploadListener;
        }
        private void saveMany(List<BmobFile> files, List<String> urls, final Post post, final View view) {
            final List<BmobObject> categories = new ArrayList<>();
            final ArrayList<Photo>photos=new ArrayList<>();
            for (int i = 0; i < urls.size(); i++) {
                Photo photo = new Photo();
                photo.setPicture(files.get(i));
                photo.setPost(post);
                categories.add(photo);
                photos.add(photo);
            }
            new BmobBatch().insertBatch(categories).doBatch(new QueryListListener<BatchResult>() {

                @Override
                public void done(List<BatchResult> results, BmobException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(),"发布帖子成功",Toast.LENGTH_SHORT).show();
                        PostUtil.arrayListPost.add(0,post);
                        PostUtil.arrayListPost.get(0).setPhotoArrayList(photos);
                        ArrayList<Bitmap>bitmaps=new ArrayList<Bitmap>();
for (int i=0;i<BitmapUtil.paths.size();i++){
    bitmaps.add(BitmapFactory.decodeFile(BitmapUtil.paths.get(i)));
}
                        PostUtil.arrayListPost.get(0).setImagelist(bitmaps);
                        PostUtil.arrayListPostMy.add(0,post);
                        PostUtil.arrayListPostMy.get(0).setPhotoArrayList(photos);
                        PostUtil.arrayListPostMy.get(0).setImagelist(bitmaps);
                        BitmapUtil.latestAdapter.notifyDataSetChanged();
                        BitmapUtil.recyclerViewLatest.smoothScrollToPosition(0);
                       BitmapUtil.myAdapter.notifyDataSetChanged();
                        BitmapUtil.recyclerViewMy.smoothScrollToPosition(0);
                    } else {
                        Toast.makeText(getApplicationContext(),"发布失败",Toast.LENGTH_SHORT).show();
                    }
                    BitmapUtil.paths.clear();
                    BitmapUtil.count = 4;
                    uploadListener.setProgressBarGone();
                }
            });
        }
        public void insertObjectWithPicture(final Post post, final View view) {
            post.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        final String[] filePaths = BitmapUtil.paths.toArray(new String[BitmapUtil.paths.size()]);
                        final Runnable runnable = new Runnable(){
                            @Override
                            public void run() {
                                if(uploadListener.getProgress()<(100/filePaths.length*currentIndex-1)){
                                    uploadListener.setProgress(uploadListener.getProgress()+1);
                                }
                                handler.postDelayed(this, 50);
                            }
                        };
                        handler.postDelayed(runnable, 50);// 打开定时器，执行操作*/
                        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

                            @Override
                            public void onSuccess(List<BmobFile> files, List<String> urls) {
                                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                                //2、urls-上传文件的完整url地址
                                if (urls.size() == BitmapUtil.paths.size()) {//如果数量相等，则代表文件全部上传完成
                                    saveMany(files, urls, post, view);
                                    uploadListener.setProgress(0);
                                }
                            }

                            @Override
                            public void onError(int statuscode, String errormsg) {
                                uploadListener.setProgress(0);
                                uploadListener.setProgressBarGone();
                                BitmapUtil.paths.clear();
                                BitmapUtil.count = 4;
                                Snackbar.make(view, "发布失败", Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                                //1、curIndex--表示当前第几个文件正在上传
                                //2、curPercent--表示当前上传文件的进度值（百分比）
                                //3、total--表示总的上传文件数
                                //4、totalPercent--表示总的上传进度（百分比）
                                if(uploadListener.getProgress()<totalPercent){
                                    uploadListener.setProgress(totalPercent);
                                }
                                currentIndex=curIndex+1;
                                if (totalPercent==100){
                                    handler.removeCallbacks(runnable);
                                }
                            }
                        });
                    } else {
                        BitmapUtil.paths.clear();
                        BitmapUtil.count = 4;
                        uploadListener.setProgressBarGone();
                        Snackbar.make(view, "发布失败", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
