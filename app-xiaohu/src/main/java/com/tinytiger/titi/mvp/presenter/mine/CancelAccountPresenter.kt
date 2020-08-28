package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.mine.CancelAccountContract

import com.tinytiger.titi.mvp.model.mine.CancelAccountModel


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 设置 Presenter
*/
class CancelAccountPresenter : BasePresenter<CancelAccountContract.View>(), CancelAccountContract.Presenter {


    private val mModel: CancelAccountModel by lazy {
        CancelAccountModel()
    }


    override fun cancellationAccount() {
        mRootView?.showLoading()
        val disposable = mModel.cancellationAccount()
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        mRootView?.showResult(bean)
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