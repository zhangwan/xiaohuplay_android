package com.tinytiger.titi.mvp.presenter.circle

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.event.AttentionEvent
import com.tinytiger.titi.mvp.contract.center.HomeDynamicContract
import com.tinytiger.titi.mvp.contract.center.MineCollectContract
import com.tinytiger.titi.mvp.contract.center.MineGameContract
import com.tinytiger.titi.mvp.contract.circle.CirclesContract
import com.tinytiger.titi.mvp.contract.circle.CirclesHomeContract
import com.tinytiger.titi.mvp.model.center.HomeDynamicModel
import com.tinytiger.titi.mvp.model.center.MineCenterModel
import com.tinytiger.titi.mvp.model.circle.CirclesHomeModel
import com.tinytiger.titi.mvp.model.circle.CirclesModel
import org.greenrobot.eventbus.EventBus

class CircleshomePresenter : BasePresenter<CirclesHomeContract.View>(), CirclesHomeContract.Presenter {

    private val mModel: CirclesHomeModel by lazy {
        CirclesHomeModel()
    }

    override fun focusList(page: Int) {
        // mRootView?.showLoading()
        val disposable = mModel.focusList(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        showFocusList(bean)
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

    fun recommendedUserList(page: Int) {
        // mRootView?.showLoading()
        val disposable = mModel.recommendedUserList(page)
            .subscribe({ bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        showUserList(bean)
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

    fun myCircleList(page: Int) {
        // mRootView?.showLoading()
        val disposable = mModel.myCircleList(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showCircleList(bean)
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

    fun recommendedCircleList() {
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

    fun recommendedPost(page: Int) {
        // mRootView?.showLoading()
        val disposable = mModel.recommendedPost(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showFocusList(bean)
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

    fun doFollow(is_mutual: Int, user_id: String) {
        mRootView?.showLoading()
        val disposable = mModel.doFollow(is_mutual, user_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        EventBus.getDefault().post(AttentionEvent(user_id, bean.data.is_mutual))
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

    fun likePost(post_id:String) {
        // mRootView?.showLoading()
        val disposable = mModel.likePost(post_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        // showMutual(bean.data.is_mutual)
                    }else{
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