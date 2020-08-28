package com.tinytiger.titi.mvp.presenter.circle

import android.app.Activity
import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.titi.mvp.contract.center.HomeDynamicContract
import com.tinytiger.titi.mvp.contract.center.MineCollectContract
import com.tinytiger.titi.mvp.contract.center.MineGameContract
import com.tinytiger.titi.mvp.contract.circle.CirclesContract
import com.tinytiger.titi.mvp.contract.circle.CirclesPostContract
import com.tinytiger.titi.mvp.model.center.HomeDynamicModel
import com.tinytiger.titi.mvp.model.center.MineCenterModel
import com.tinytiger.titi.mvp.model.circle.CirclesModel
import com.tinytiger.titi.mvp.model.circle.CirclesPostModel

class CirclesPostPresenter : BasePresenter<CirclesPostContract.View>(), CirclesPostContract.Presenter {


    private val mModel: CirclesPostModel by lazy {
        CirclesPostModel()
    }

    fun indexCircleByCate(keywords: String, page: Int) {
        if (page == 1) {
            mRootView?.showLoading()
        }

        if (keywords.isEmpty()) {
            indexCircleByCate(page)
            return
        }

        val disposable = mModel.indexCircleByCate(keywords, page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showCircleByCate(bean)
                    } else {
                        getErrorCode(bean)
                    }
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

    override fun indexCircleByCate(page: Int) {
        val disposable = mModel.indexCircleByCate(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showCircleByCate(bean)
                    } else {
                        getErrorCode(bean)
                    }
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

    fun indexCircleByModular(keywords: String, page: Int) {
        if (page == 1) {
            mRootView?.showLoading()
        }

        val disposable = mModel.indexCircleByModular(keywords, page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showCircleByModular(bean)
                    } else {
                        getErrorCode(bean)
                    }
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

    fun indexCircleByModular1(keywords: String, page: Int) {

        mRootView?.showLoading()
        val disposable = mModel.indexCircleByModular1(keywords, page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showCircleByModular(bean)
                    } else {
                        getErrorCode(bean)
                    }
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


    fun getUserList(post_id: String) {

        mRootView?.showLoading()
        val disposable = mModel.getUserList(post_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showUserList(bean)
                    } else {
                        getErrorCode(bean)
                    }
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

    fun getFollowUserList(page: Int) {
        //  mRootView?.showLoading()
        val disposable = mModel.getFollowUserList(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showFollowUserList(bean)
                    } else {
                        getErrorCode(bean)
                    }
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


    fun inviteUser(post_id: String, user_ids: String, context: Activity) {
        mRootView?.showLoading()
        val disposable = mModel.inviteUser(post_id, user_ids)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        ToastUtils.show(context,"邀请成功")
                        context.finish()
                    } else {
                        getErrorCode(bean)
                    }
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