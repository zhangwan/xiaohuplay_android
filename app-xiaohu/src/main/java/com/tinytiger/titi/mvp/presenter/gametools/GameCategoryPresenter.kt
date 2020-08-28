package com.tinytiger.titi.mvp.presenter.gametools

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.gametools.GameCategoryContract
import com.tinytiger.titi.mvp.model.game.GameCategoryModel

class GameCategoryPresenter : BasePresenter<GameCategoryContract.View>(), GameCategoryContract.Presenter {


    private val mModel: GameCategoryModel by lazy {
        GameCategoryModel()
    }


    override fun getGameCategory() {
        mRootView?.showLoading()
        val disposable = mModel.getGameCategory()
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showGameCategory(bean)
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

    override fun getGameCategoryDetailList(cate_id:Int,type:Int,page:Int) {
        mRootView?.showLoading()
        val disposable = mModel.getGameCategoryDetailList(cate_id,type,page)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        if(bean.data!=null) {
                            showGameCategoryDetailList(bean.data)
                        }
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

    override fun getGameCategoryBanner(cate_id:Int) {
        mRootView?.showLoading()
        val disposable = mModel.getGameCategoryBanner(cate_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        if(bean.data!=null) {
                            showGameCategoryBanner(bean.data)
                        }
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