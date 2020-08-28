package com.tinytiger.titi.mvp.presenter.game


import com.tinytiger.common.base.BasePresenter

import com.tinytiger.titi.mvp.contract.game.HomeGameContract
import com.tinytiger.titi.mvp.model.game.HomeGameModel

import com.tinytiger.common.net.exception.ExceptionHandle

/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 下午 3:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页数据处理
 */
class HomeGamePresenter : BasePresenter<HomeGameContract.View>(), HomeGameContract.Presenter {

    private val videoDetailModel: HomeGameModel by lazy {
        HomeGameModel()
    }

    /**
     * 是否加载过
     */
    private var typeAd = false

    override fun loadAdList() {
        if (typeAd) {
            return
        }
        val disposable = videoDetailModel.getAdList()
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

    override fun loadRecommendContentList() {
        val disposable = videoDetailModel.getRecommendContentList()
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code == 200 && issue.data != null) {
                        getRecommendList(issue.data)
                    } else {
                        getErrorCode(issue)
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