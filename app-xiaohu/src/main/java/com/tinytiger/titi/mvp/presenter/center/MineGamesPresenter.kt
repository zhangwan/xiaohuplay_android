package com.tinytiger.titi.mvp.presenter.center

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.center.MineGameContract
import com.tinytiger.titi.mvp.model.center.MineCenterModel

class MineGamesPresenter : BasePresenter<MineGameContract.View>(), MineGameContract.Presenter {


    private val mModel: MineCenterModel by lazy {
        MineCenterModel()
    }


    override fun getUserFollowGameList(page: Int) {
        // mRootView?.showLoading()
        val disposable = mModel.getUserFollowGameList(page)
            .subscribe({ bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        mRootView?.showFollowGameList(bean.data)
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

    //关注操作：cancel参数不用传，取消关注操作：cancel=1 必传
     fun GameFollow(game_id: String, cancel: Int) {
        val disposable = mModel.GameFollow(game_id, cancel)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                       showGameFollow(game_id)
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