package com.tinytiger.titi.mvp.presenter.game

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.game.GameWikiErrorContract
import com.tinytiger.titi.mvp.model.game.GameWikiErrorModel

class GameWikiErrorPresenter : BasePresenter<GameWikiErrorContract.View>(), GameWikiErrorContract.Presenter {


    private val mModel: GameWikiErrorModel by lazy {
        GameWikiErrorModel()
    }

    override fun loadQiniuToken() {
        mRootView?.showLoading()
        val disposable = mModel.getQiniuToken()
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code==200){
                        getQiniuToken(issue.data.qiniu_token)
                    }else{
                        dismissLoading()
                        getErrorCode(issue)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }

    fun commitEntryError(content_id: String, error_desc: String, images: String) {
        mRootView?.showLoading()
        val disposable = mModel.commitEntryError(content_id,error_desc,images)
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code==200){
                        commitSuccess()
                    }else{
                        dismissLoading()
                        getErrorCode(issue)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }
}