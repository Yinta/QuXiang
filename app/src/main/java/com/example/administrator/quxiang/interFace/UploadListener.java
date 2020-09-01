package com.example.administrator.quxiang.interFace;

/**
 * Created by Administrator on 2020/4/28.
 */

public interface UploadListener {
     int getProgress();
    void setProgress(int progress);
    void setProgressBarVisable();
    void setProgressBarGone();
}
