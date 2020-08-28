package com.tinytiger.titi.mvp.presenter.props



import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.base.BasePresenter

import com.tinytiger.titi.mvp.contract.props.PropsExchangeContract

import com.tinytiger.titi.mvp.model.props.PropsExchangeModel


/**
 *
 * @Author luke
 * @Date 2020-02-05 16:17
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 道具兑换
 *
 */
class PropsExchangePresenter : BasePresenter<PropsExchangeContract.View>(), PropsExchangeContract.Presenter {

    private val mDetailModel: PropsExchangeModel by lazy {
        PropsExchangeModel()
    }

    override fun submitExchangePreview(exchange_code_json: String) {
        mRootView?.showLoading()
        val disposable = mDetailModel.submitExchangePreview(exchange_code_json)
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code==200){
                       getSubmitExchange(issue)
                    }else{
                        getErrorCode(issue)
                    }
                    dismissLoading()
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                }
            })

        addSubscription(disposable)
    }

    fun submitExchange(exchange_code_json: String) {
        mRootView?.showLoading()
        val disposable = mDetailModel.submitExchange(exchange_code_json)
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code==200){
                        getSubmit(issue)
                    }else{
                        getErrorCode(issue)
                    }
                    dismissLoading()
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