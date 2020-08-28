package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.mine.CollectionContract
import com.tinytiger.titi.mvp.contract.mine.FeedbackContract
import com.tinytiger.titi.mvp.model.mine.CollectionModel
import com.tinytiger.titi.mvp.model.mine.FeedbackModel

/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 我的 Presenter
*/
class FeedbackPresenter : BasePresenter< FeedbackContract.View>(),  FeedbackContract.Presenter {


    private val mModel: FeedbackModel by lazy {
        FeedbackModel()
    }


    override fun getCatList() {
//        mRootView?.showLoading()
        val disposable = mModel.getCatList()
            .subscribe({ bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showCateList(bean.data)
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


    override fun getProposalList(opinion_cat_id:Int,page: Int) {
//        mRootView?.showLoading()
        val disposable = mModel.getProposalList(opinion_cat_id,page)
            .subscribe({ bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showProposalList(bean.data)
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


    override fun addProposal(opinion_cat_id:Int,content: String) {
        mRootView?.showLoading()
        val disposable = mModel.addProposal(opinion_cat_id,content)
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
                mRootView?.dismissLoading()
                mRootView?.showErrorMsg(
                    ExceptionHandle.handleException(t),
                    ExceptionHandle.errorCode
                )
            })
        addSubscription(disposable)

    }




}