package com.tinytiger.common.http

import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.widget.LoadingUtils


/**
 * Created by zwy on 2020/6/5.
 */
abstract class HttpLoadingCallback<T> : HttpCallback<T> {
    override fun onStart() {
        super.onStart()
        LoadingUtils.getInstance().show(AppManager.getAppManager().currentActivity())
    }

    override fun onFailure(errorCode: String, errorMsg: String) {
        LoadingUtils.getInstance().dismiss()

    }

    override fun onResponse(any: T) {
        LoadingUtils.getInstance().dismiss()
    }
}