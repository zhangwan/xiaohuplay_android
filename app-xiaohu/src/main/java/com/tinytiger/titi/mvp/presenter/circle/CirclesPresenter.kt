package com.tinytiger.titi.mvp.presenter.circle

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.center.HomeDynamicContract
import com.tinytiger.titi.mvp.contract.center.MineCollectContract
import com.tinytiger.titi.mvp.contract.center.MineGameContract
import com.tinytiger.titi.mvp.contract.circle.CirclesContract
import com.tinytiger.titi.mvp.model.center.HomeDynamicModel
import com.tinytiger.titi.mvp.model.center.MineCenterModel
import com.tinytiger.titi.mvp.model.circle.CirclesModel

class CirclesPresenter : BasePresenter<CirclesContract.View>(), CirclesContract.Presenter {

    private val mModel: CirclesModel by lazy {
        CirclesModel()
    }


    override fun getGame(user_id: String, page: Int) {
        // mRootView?.showLoading()

    }

    fun myCircleList(page: Int) {
        // mRootView?.showLoading()
        val disposable = mModel.myCircleList(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        showCircleList(bean)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
                }

            }, { t ->
                mRootView?.dismissLoading()
                mRootView?.showErrorMsg(
                    ExceptionHandle.handleException(t),
                    ExceptionHandle.errorCode
                )
            })
        addSubscription(disposable)
    }

    fun joinCircle(circle_id: String) {
        mRootView?.showLoading()
        val disposable = mModel.joinCircle(circle_id)
            .subscribe({ bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        showCircle(circle_id)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
                }

            }, { t ->
                mRootView?.dismissLoading()
                mRootView?.showErrorMsg(
                    ExceptionHandle.handleException(t),
                    ExceptionHandle.errorCode
                )
            })
        addSubscription(disposable)
    }


    fun recommendedCircleList() {
        // mRootView?.showLoading()
        val disposable = mModel.recommendedCircleList()
            .subscribe({ bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        showCircleRecommendedList(bean)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
                }

            }, { t ->
                mRootView?.dismissLoading()
                mRootView?.showErrorMsg(
                    ExceptionHandle.handleException(t),
                    ExceptionHandle.errorCode
                )
            })
        addSubscription(disposable)
    }

    fun allCircleList(page: Int) {
        // mRootView?.showLoading()
        val disposable = mModel.allCircleList(page)
            .subscribe({ bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        showCircleList(bean)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
                }

            }, { t ->
                mRootView?.dismissLoading()
                mRootView?.showErrorMsg(
                    ExceptionHandle.handleException(t),
                    ExceptionHandle.errorCode
                )
            })
        addSubscription(disposable)
    }
}