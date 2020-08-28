package com.tinytiger.titi.mvp.presenter.game


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.titi.mvp.contract.game.GameContract
import com.tinytiger.titi.mvp.model.game.GameModel

import com.tinytiger.common.net.exception.ExceptionHandle

/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 下午 3:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页数据处理
 */
class GamePresenter : BasePresenter<GameContract.View>(), GameContract.Presenter {

    private val videoDetailModel: GameModel by lazy {
        GameModel()
    }


    
    override fun loadCategoryList() {
        val disposable = videoDetailModel.getCategoryList()
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code == 200) {
                        getCategoryList(issue)
                    } else {
                        getErrorCode(issue)
                    }
                    dismissLoading()
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })

        addSubscription(disposable)
    }

    override fun loadGameContentList(category_id:Int) {
        val disposable = videoDetailModel.getGameContentList(category_id)
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code == 200) {
                            getGameContentList(issue)
                    } else {
                        getErrorCode(issue)
                    }
                    dismissLoading()
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