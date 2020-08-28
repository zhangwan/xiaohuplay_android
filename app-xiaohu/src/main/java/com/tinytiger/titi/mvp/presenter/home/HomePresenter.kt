package com.tinytiger.titi.mvp.presenter.home


import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.base.BasePresenter
import com.tinytiger.titi.mvp.contract.SearchContract
import com.tinytiger.titi.mvp.contract.home.HomeContract

import com.tinytiger.titi.mvp.model.SearchModel
import com.tinytiger.titi.mvp.model.home.HomeModel


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 下午 3:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索数据处理
 */
class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    private val mModel: HomeModel by lazy {
        HomeModel()
    }


    override fun loadAllInfo(keyWords:String) {

    }

    /**
     * 是否加载过
     */
    private var typeAd = false

     fun loadAdList() {
        if (typeAd) {
            return
        }
        val disposable = mModel.getAdList()
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code == 200) {
                        typeAd = true
                        getAdList(issue)
                    } else {
                        getErrorCode(issue)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })

        addSubscription(disposable)
    }

    fun clickAdRecord(ad_id: String) {
        val disposable = mModel.clickAdRecord(ad_id)
            .subscribe()
        addSubscription(disposable)
    }

    fun amwayWallRecommend(source:String) {
        //  mRootView?.showLoading()
        val disposable = mModel.amwayWallRecommend(source)
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        loadAmwayWall(bean)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }

    fun indexGame() {
        //  mRootView?.showLoading()
        val disposable = mModel.indexGame()
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        loadPushIndex(bean)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }

}