package com.tinytiger.titi.utils.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.tinytiger.common.base.BaseApp;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Tamas
 * create at 2019/12/5 0005
 * Email: ljw_163mail@163.com
 * description:
 */
public class NetStateChangeReceiver extends BroadcastReceiver {

    private NetworkType mType = NetworkUtil.getNetworkType(BaseApp.getContext());

    private static class InstanceHolder{
        private static final NetStateChangeReceiver INSTANCE = new NetStateChangeReceiver();
    }

    private List<NetStateChangeObserver> mObservers = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            NetworkType networkType = NetworkUtil.getNetworkType(context);
            notifyObservers(networkType);
        }
    }

    public static void registerReceiver(Context context){
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver( InstanceHolder.INSTANCE,intentFilter);
    }

    public static void unRegisterReceiver(Context context){
        context.unregisterReceiver( InstanceHolder.INSTANCE);
    }

    public static void registerObserver(NetStateChangeObserver observer){
        if (observer == null) {
            return;
        }
        if (!InstanceHolder.INSTANCE.mObservers.contains(observer)){
            InstanceHolder.INSTANCE.mObservers.add(observer);
        }
    }

    public static void unRegisterObserver(NetStateChangeObserver observer){
        if (observer == null) {
            return;
        }
        if (InstanceHolder.INSTANCE.mObservers == null) {
            return;
        }
        InstanceHolder.INSTANCE.mObservers.remove(observer);
    }

    private void notifyObservers(NetworkType networkType){
        if (mType == networkType) {
            return;
        }
        mType = networkType;
            for (NetStateChangeObserver observer : mObservers){
                observer.onNetConnected(networkType);
            }
    }
}
