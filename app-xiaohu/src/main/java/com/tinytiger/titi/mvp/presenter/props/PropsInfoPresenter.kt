package com.tinytiger.titi.mvp.presenter.props



import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.base.BasePresenter

import com.tinytiger.titi.mvp.contract.props.PropsInfoContract

import com.tinytiger.titi.mvp.model.props.PropsModel


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 下午 3:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页数据处理
 */
class PropsInfoPresenter : BasePresenter<PropsInfoContract.View>(), PropsInfoContract.Presenter {



    private val mDetailModel: PropsModel by lazy {
        PropsModel()
    }


    override fun getPropsInfo(tool_id :String) {
        // mRootView?.showLoading()
        val disposable = mDetailModel.getPropsInfo(tool_id)
            .subscribe({ bean ->
                mRootView?.apply {

                    if (bean.code==200){
                        showPropsInfo(bean.data)
                    }else{
                        getErrorCode(bean)
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


    override  fun buyTool(tool_id: String, num: Int){
        // mRootView?.showLoading()
        val disposable = mDetailModel.buyTool(tool_id,num)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showResult()
                    }else{
                        getErrorCode(bean)
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

    override fun wearProps(tool_id: String,cate_id: String) {
        mRootView?.showLoading()
        val disposable = mDetailModel.wearProps(tool_id,cate_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showWearResult(bean.msg)
                    }else{
                        getErrorCode(bean)
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

    override fun exchangeTools(tool_id: String, num: Int) {
        mRootView?.showLoading()
        val disposable = mDetailModel.exchangeTools(tool_id,num)
            .subscribe({ bean ->
                mRootView?.apply {

                    if (bean.code==200){
                        showExchange(bean.data)
                    }else{
                        getErrorCode(bean)
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


    override fun getExchangeInfo(id: String) {
        mRootView?.showLoading()
        val disposable = mDetailModel.getExchangeInfo(id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showExchange(bean.data)
                    }else{
                        getErrorCode(bean)
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