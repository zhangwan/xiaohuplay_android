package com.netease.nim.uikit.mvp



import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 下午 3:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页数据处理
 */
class UserKitPresenter : BasePresenter<UserKitContract.View>(), UserKitContract.Presenter {

    private val videoDetailModel: UserKitModel by lazy {
        UserKitModel()
    }

    override fun loadCancelBlack(user_id: String) {
        mRootView?.showLoading()
        val disposable = videoDetailModel.cancelBlack(user_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        getBlack(false)
                    } else {
                        getErrorCode(issue)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })

        addSubscription(disposable)
    }

    override fun loadAddBlack(user_id: String) {
        mRootView?.showLoading()
        val disposable = videoDetailModel.addBlack(user_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        getBlack(true)
                    } else {
                        getErrorCode(issue)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })

        addSubscription(disposable)
    }


    fun getUserRelation(netease_id: String) {
        mRootView?.showLoading()
        val disposable = videoDetailModel.getUserRelation(netease_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        getUserRelation(issue)
                    } else {
                        getErrorCode(issue)
                    }
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