package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle

import com.tinytiger.titi.mvp.contract.mine.TalentContract

import com.tinytiger.titi.mvp.model.mine.TalentModel

/**
 *
 * @author zhw_luke
 * @date 2020/5/5 0005 下午 4:48
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 达人认证
 */
class TalentPresenter : BasePresenter<TalentContract.View>(), TalentContract.Presenter {


    private val mModel: TalentModel by lazy {
        TalentModel()
    }


    fun loadQiniuToken() {
        mRootView?.showLoading()
        val disposable = mModel.getQiniuToken()
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code == 200) {
                        getQiniuToken(issue.data.qiniu_token)
                    } else {
                        dismissLoading()
                        getErrorCode(issue)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }


    override fun masterApply() {
        mRootView?.showLoading()
        val disposable = mModel.masterApply()
            .subscribe({ bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showResult(bean)
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


    fun submitMasterApply(
        key: String, real_name: String, id_number: String,
        image_one: String, image_two: String, profile: String
    ) {
        val disposable = mModel.submitMasterApply(key, real_name, id_number, image_one, image_two, profile)
            .subscribe({ bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showResult(null)
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

    fun submitMasterApply1(
        key: String,
        external_name: String,
        site_name: String,
        image_one: String,
        image_two: String,
        profile: String
    ) {
        val disposable = mModel.submitMasterApply1(key, external_name, site_name, image_one, image_two, profile)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showResult(null)
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