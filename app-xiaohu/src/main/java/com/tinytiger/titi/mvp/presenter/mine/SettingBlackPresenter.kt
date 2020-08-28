package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.titi.mvp.contract.mine.SettingBlackContract

import com.tinytiger.titi.mvp.model.mine.SettingBlackModel
import com.tinytiger.common.net.exception.ExceptionHandle

/**
 *
 * @author zhw_luke
 * @date 2019/11/18 0018 下午 8:02
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
class SettingBlackPresenter : BasePresenter<SettingBlackContract.View>(),
    SettingBlackContract.Presenter {


    private val mModel: SettingBlackModel by lazy {
        SettingBlackModel()
    }


    override fun loadUserBlackList(page: Int) {
        //mRootView?.showLoading()
        val disposable = mModel.getUserBlackList(page)
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code == 200) {
                        getUserBlackList(issue)
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
    override fun loadCancelBlack(user_id:String) {
        mRootView?.showLoading()
        val disposable = mModel.cancelBlack(user_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    mRootView?.dismissLoading()
                    if (issue.code == 200) {
                        showCancelBlack()
                    } else {
                        getErrorCode(issue)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    mRootView?.dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }

}