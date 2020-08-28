package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.titi.mvp.contract.mine.MineContract
import com.tinytiger.titi.mvp.model.mine.MineModel
import com.tinytiger.common.net.exception.ExceptionHandle

/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 我的 Presenter
*/
class MinePresenter : BasePresenter<MineContract.View>(), MineContract.Presenter {

    private val mModel: MineModel by lazy {
        MineModel()
    }


    override fun getUserCenter() {
        val disposable = mModel.getUserCenter()
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    showUserCenter(bean.code==200, bean.data)
                    if (bean.code==3008){
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

    fun shareApp() {
        mRootView?.showLoading()
        val disposable = mModel.shareApp()
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        getShareAppBean(bean)
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