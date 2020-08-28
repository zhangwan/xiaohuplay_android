package com.tinytiger.titi.mvp.presenter.gametools

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.gametools.GameContentContract
import com.tinytiger.titi.mvp.model.gametools.GameDetailModel

class GameContentPresenter : BasePresenter<GameContentContract.View>(), GameContentContract.Presenter {



    private val mModel: GameDetailModel by lazy {
        GameDetailModel()
    }

    override fun getGameDetailContentData(game_id:String,page:Int) {
        //   mRootView?.showLoading()
        val disposable = mModel.getGameDetailContentData(game_id,page)
            .subscribe({bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        getGameDetailContentData(bean.data)
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