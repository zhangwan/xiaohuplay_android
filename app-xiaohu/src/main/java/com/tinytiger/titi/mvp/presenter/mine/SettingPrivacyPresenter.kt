package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.base.BasePresenter

import com.tinytiger.titi.mvp.contract.mine.SettingPrivacyContract

import com.tinytiger.titi.mvp.model.mine.SettingPrivacyModel
import com.tinytiger.common.net.exception.ExceptionHandle

/**
 *
 * @author zhw_luke
 * @date 2019/11/18 0018 下午 8:02
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
class SettingPrivacyPresenter : BasePresenter<SettingPrivacyContract.View>(),
    SettingPrivacyContract.Presenter {


    private val mModel: SettingPrivacyModel by lazy {
        SettingPrivacyModel()
    }


    override fun modifyPrivateConfig(private_type: Int, modify_val: Int) {
        mRootView?.showLoading()
        val disposable = mModel.modifyPrivateConfig(private_type, modify_val)
            .subscribe({ issue ->
                mRootView?.apply {
                    mRootView?.dismissLoading()
                    if (issue.code == 200) {
                        mRootView?.showPrivacyData()
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
    override fun loadUserPrivateConfig() {
        mRootView?.showLoading()
        val disposable = mModel.getUserPrivateConfig()
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code == 200) {
                        mRootView?.getUserPrivateConfig(issue)
                    } else {
                        getErrorCode(issue)
                    }
                    mRootView?.dismissLoading()
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