package com.tinytiger.titi.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.titi.data.MyUserData
import org.greenrobot.eventbus.EventBus

/**
 * @author lmq001
 * @date 2020/07/20 10:43
 * @copyright 小虎互联科技
 * @doc Activity跳转工具类
 */
object ActivityUtil {

    /**
     * 页面跳转方法
     * @param cls           需跳转的Activity
     * @param isNeedLogin   是否需要登录（默认不需要）
     * @param bundle        跳转携带参数
     */
    fun startActivityKx(
        context: Context, cls: Class<*>, isNeedLogin: Boolean = false, bundle: Bundle = Bundle()
    ) {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
            ToastUtils.show(context, "无网络")
            return
        }
        if (isNeedLogin && MyUserData.isEmptyToken()) {
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
            return
        }

        context.startActivity(Intent(context, cls).apply {
            putExtras(bundle)
        })
    }

}