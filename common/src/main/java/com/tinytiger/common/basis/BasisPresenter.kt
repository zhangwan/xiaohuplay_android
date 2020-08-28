package com.tinytiger.common.basis



/**
 * BasePresenter
 *
 * @author:zwy
 * @date:2020/6/5
 */
open class BasisPresenter<V : BasisView> {

    var mvpView: V? = null

    open fun sendReq() {
    }

    fun attachView(mvpView: BasisView) {
        this.mvpView = mvpView as V
    }

    fun detachView() {
        this.mvpView = null
    }

    fun isViewAttached(): Boolean {
        return mvpView != null
    }

    fun getView(): V? {
        return mvpView
    }

    fun savePoint(map: Map<String,String>){

    }

}