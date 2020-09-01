package com.example.administrator.quxiang.Application;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.example.administrator.quxiang.BroadcastReceive.NetWorkReceive;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;


/**
 * Created by Administrator on 2020/3/1.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Bmob.initialize(this, "c8daca7d241d6bbe31fc8a40c514fa27");
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(NetWorkReceive.getInstance(), filter);
    }

    public static Context getContext() {
        return context;
    }
}
