package com.tinytiger.common.view.web;



import com.tinytiger.common.base.BaseApp;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import java.util.HashMap;

/**
 *
 * @author zhw_luke
 * @date 2019/10/23 0023 下午 6:23
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc tbs初始化
 */
public class TbsInit {

    public static void initX5() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);
        QbSdk.initX5Environment(BaseApp._instance, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                //Log.w("初始化", "X5浏览器已执行初始化");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                // Log.i("初始化", "X5浏览器初始化：" + (b ? "初始化成功" : "初始化失败"));
            }
        });
    }

}
