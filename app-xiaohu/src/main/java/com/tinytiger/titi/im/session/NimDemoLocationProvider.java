package com.tinytiger.titi.im.session;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.tinytiger.common.utils.AppManager;
import com.tinytiger.common.utils.permission.DefaultRationale;
import com.tinytiger.common.utils.permission.PermissionSetting;
import com.tinytiger.titi.im.location.activity.LocationAmapActivity;
import com.tinytiger.titi.im.location.activity.LocationExtras;
import com.tinytiger.titi.im.location.activity.NavigationAmapActivity;
import com.netease.nim.uikit.api.model.location.LocationProvider;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

/**
 *
 * @author zhw_luke
 * @date 2019/11/13 0013 上午 11:59
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 云信获取定位
 */
public class NimDemoLocationProvider implements LocationProvider {
    @Override
    public void requestLocation(final Context context, Callback callback) {
        AndPermission.with(context)
                .runtime()
                .permission(Permission.Group.LOCATION)
                .rationale(new DefaultRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        LocationAmapActivity.start(context, callback);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(AppManager.getAppManager().currentActivity(), permissions)) {
                            new PermissionSetting().showSettingDialog(AppManager.getAppManager().currentActivity(),permissions);
                        }
                    }
                })
                .start();

    }


    @Override
    public void openMap(Context context, double longitude, double latitude, String address) {
        Intent intent = new Intent(context, NavigationAmapActivity.class);
        intent.putExtra(LocationExtras.LONGITUDE, longitude);
        intent.putExtra(LocationExtras.LATITUDE, latitude);
        intent.putExtra(LocationExtras.ADDRESS, address);
        context.startActivity(intent);
    }
}
