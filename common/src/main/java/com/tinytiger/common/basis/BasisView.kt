package com.tinytiger.common.basis

import android.content.Context
import android.view.View

/**
 * BaseView
 *
 * @author:zwy
 * @date:2020/6/5
 */
interface BasisView {

    fun refresh()

    fun showLoading()

    fun hideLoading()

    fun showToast(msg: String)

    fun showToast(msg: Int)

    fun getMyContext(): Context
}