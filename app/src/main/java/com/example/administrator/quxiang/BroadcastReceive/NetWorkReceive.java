package com.example.administrator.quxiang.BroadcastReceive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 2020/5/14.
 */

public class NetWorkReceive extends BroadcastReceiver {
    ArrayList<OnNetWorkChange>onNetWorkChangeArrayList=new ArrayList<>();
    public static boolean hasNetWork=true;
    private static NetWorkReceive netWorkReceive;
    public interface OnNetWorkChange {
        void notifyChange();
    }
    public static NetWorkReceive getInstance() {
        if (netWorkReceive == null) {
            netWorkReceive = new NetWorkReceive();
        }
        return netWorkReceive;
    }
    public void setOnNetWorkChange(OnNetWorkChange onNetWorkChange) {
        if (this.onNetWorkChangeArrayList.contains(onNetWorkChange)) {
            return;
        }
        this.onNetWorkChangeArrayList.add(onNetWorkChange);
    }
public void notifyAllListener(){
    for (OnNetWorkChange change : onNetWorkChangeArrayList) {
        change.notifyChange();
    }
}
    /**
     * 取消网络变化监听监听回调
     *
     * @param onNetWorkChange 回调对象
     */
    public void delOnNetWorkChange(OnNetWorkChange onNetWorkChange) {
        if (this.onNetWorkChangeArrayList.contains(onNetWorkChange)) {
            this.onNetWorkChangeArrayList.remove(onNetWorkChange);
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (onNetWorkChangeArrayList.size()==0) {
            return;
        }
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            hasNetWork=false;
            Toast.makeText(context,"请检查网络!",Toast.LENGTH_SHORT).show();
        }
          else{
            hasNetWork=true;
            notifyAllListener();
        }
    }
}
