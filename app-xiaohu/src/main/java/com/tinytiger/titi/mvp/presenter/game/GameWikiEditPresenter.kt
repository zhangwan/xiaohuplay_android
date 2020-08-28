package com.tinytiger.titi.mvp.presenter.game

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.game.GameWikiEditContract
import com.tinytiger.titi.mvp.model.game.GameWikiEditModel

/**
 * @author lmq001
 * @date 2020/06/01 11:10
 * @copyright 小虎互联科技
 * @doc 查看词条编辑者
 */
class GameWikiEditPresenter : BasePresenter<GameWikiEditContract.View>(), GameWikiEditContract.Presenter {

    private val mModel: GameWikiEditModel by lazy {
        GameWikiEditModel()
    }


    override fun getGameWikiList(content_id: String, page: Int) {
        mRootView?.showLoading()
        val disposable = mModel.getGameList(content_id, page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showGameWikiList(bean)
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