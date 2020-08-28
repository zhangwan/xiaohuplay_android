package com.tinytiger.titi.mvp.presenter.center

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.center.HomeDynamicContract
import com.tinytiger.titi.mvp.model.center.HomeDynamicModel

class HomeDynamicPresenter : BasePresenter<HomeDynamicContract.View>(), HomeDynamicContract.Presenter {


    private val mModel: HomeDynamicModel by lazy {
        HomeDynamicModel()
    }


    override fun getDynamic(user_id:String,page:Int) {
        val disposable = mModel.getUserDynamic(user_id,page)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        showHomeDynamicList(bean)
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


    override fun getGame(user_id:String,page:Int) {
        val disposable = mModel.getGame(user_id,page)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        mRootView?.showHomeGameList(bean.data)
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