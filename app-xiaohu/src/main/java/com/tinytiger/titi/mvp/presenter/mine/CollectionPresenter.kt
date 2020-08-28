package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.mine.CollectionContract
import com.tinytiger.titi.mvp.model.mine.CollectionModel

/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 我的 Presenter
*/
class CollectionPresenter : BasePresenter<CollectionContract.View>(), CollectionContract.Presenter {


    private val mModel: CollectionModel by lazy {
        CollectionModel()
    }


    override fun collectList(page: Int) {
//        mRootView?.showLoading()
        val disposable = mModel.collectList(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showCollection(bean.data)
                    }else{
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


    override fun cancelCollect(content_id: Int) {
        mRootView?.showLoading()
        val disposable = mModel.cancelCollect(content_id)
            .subscribe({ bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showResult()
                        showErrorMsg("取消收藏成功",bean.code)
                    }else{
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