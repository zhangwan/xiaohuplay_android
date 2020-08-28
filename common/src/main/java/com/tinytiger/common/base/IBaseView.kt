package com.tinytiger.common.base

/**
 * @author Jake.Ho
 * created: 2018/10/25
 * desc:
 */
interface IBaseView {

    /**
     * 开始加载
     */
    fun showLoading()

    /**
     * 加载结束
     */
    fun dismissLoading()

    /**
     * 加载失败信息
     */
    fun showErrorMsg(errorMsg: String, errorCode: Int)


}
