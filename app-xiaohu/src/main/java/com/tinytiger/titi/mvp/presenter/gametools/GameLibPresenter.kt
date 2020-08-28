package com.tinytiger.titi.mvp.presenter.gametools

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.gametools.GameLibContract
import com.tinytiger.titi.mvp.model.gametools.GameLibModel

class GameLibPresenter : BasePresenter<GameLibContract.View>(), GameLibContract.Presenter {


    private val mModel: GameLibModel by lazy {
        GameLibModel()
    }


    override fun getGameList(type: Int) {
        mRootView?.showLoading()
        val disposable = mModel.getGameList(type)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showGameList(bean)
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