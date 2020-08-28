package com.tinytiger.titi.mvp.presenter.props



import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.base.BasePresenter

import com.tinytiger.titi.mvp.contract.props.PropsContract

import com.tinytiger.titi.mvp.model.props.PropsModel


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 下午 3:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页数据处理
 */
class PropsPresenter : BasePresenter<PropsContract.View>(), PropsContract.Presenter {

    private val mDetailModel: PropsModel by lazy {
        PropsModel()
    }

    override fun loadindexGoodsCate(need_group: Int) {
        mRootView?.showLoading()
        val disposable = mDetailModel.indexGoodsCate(need_group)
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code==200){
                        indexGoodsCate(issue)
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

    fun loadindexGoods(cate_id: String,page :Int) {
       // mRootView?.showLoading()
        val disposable = mDetailModel.indexGoods(cate_id,page)
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code==200){
                        indexGoods(issue)
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

    fun searchGoodsTool(title: String,page :Int) {
        // mRootView?.showLoading()
        val disposable = mDetailModel.searchGoodsTool(title,page)
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code==200){
                        indexGoods(issue)
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

    fun getMyMallprops(cate_id: String,page :Int) {
       // mRootView?.showLoading()
        val disposable = mDetailModel.getMyMallprops(cate_id,page)
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code==200){
                        indexGoods(issue)
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


    fun wearProps(tool_id: String,cate_id: String) {
        mRootView?.showLoading()
        val disposable = mDetailModel.wearProps(tool_id,cate_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code==200){
                        wearProps(issue)
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



    fun getMyMallExchangeCode(page :Int) {
       // mRootView?.showLoading()
        val disposable = mDetailModel.getMyMallExchangeCode(page)
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code==200){
                        indexGoods(issue)
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