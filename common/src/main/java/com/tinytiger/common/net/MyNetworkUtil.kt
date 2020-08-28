package com.tinytiger.common.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.tinytiger.common.base.BaseApp


/*
* @author Tamas
* create at 2019/11/17 0017
* Email: ljw_163mail@163.com
* description: 
*/
class MyNetworkUtil{

    companion object {
        /**
         * check NetworkAvailable
         *
         * @param context
         * @return
         */
        @JvmStatic
        fun isNetworkAvailable(context: Context): Boolean {
            val manager = BaseApp.getContext().applicationContext.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = manager.activeNetworkInfo
            return !(null == info || !info.isConnected)
        }


        /**
         * 当前使用WIFI连接网络
         */
        fun currentNetWorkStatusIsWifi(context: Context): Boolean {
            val systemService =
                BaseApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val networkCapabilities: NetworkCapabilities? =
                    systemService?.getNetworkCapabilities(systemService.activeNetwork)
                if (networkCapabilities != null) {
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                } else {
                    false
                }
            } else {
                val connectivityManager = BaseApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager.activeNetworkInfo
                return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
            }
        }
    }


    /**
     * 当前是否连接网络
     */
    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val networkCapabilities: NetworkCapabilities? =
                connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (networkCapabilities != null) {
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } else {
                false
            }
        } else {
            val networkInfo = connectivityManager?.activeNetworkInfo as NetworkInfo
            networkInfo.isConnected && networkInfo.isAvailable
        }
    }

}