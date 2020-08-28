package com.tinytiger.common.base


/**
 * @author Jake.Ho
 * created: 2018/10/25
 * desc: Presenter 基类
 */
interface IPresenter<in V: IBaseView> {

    /**
     * 绑定view
     */
    fun attachView(mRootView: V)

    /**
     * 解绑,销毁
     */
    fun detachView()

}
