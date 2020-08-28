package com.tinytiger.titi.utils.net;

/*
 * @author Tamas
 * create at 2019/12/5 0005
 * Email: ljw_163mail@163.com
 * description:
 */
public interface NetStateChangeObserver {
    void onNetDisconnected();
    void onNetConnected(NetworkType networkType);
}
