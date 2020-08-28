package com.tinytiger.titi.mvp.presenter.center

import com.google.gson.Gson
import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.center.MineWorkContract
import com.tinytiger.titi.mvp.model.center.MineCenterModel

class MineWorksPresenter : BasePresenter<MineWorkContract.View>(), MineWorkContract.Presenter {


    private val mModel: MineCenterModel by lazy {
        MineCenterModel()
    }





    override fun getUserGameAmwayList(page:Int) {
//        mRootView?.showLoading()
        val disposable = mModel.getUserGameAmwayList(page)
            .subscribe({bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        mRootView?.showGameAmwayList(bean.data)
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


    override fun delUserGameAmwayList(ids:String) {
        mRootView?.showLoading()
        val disposable = mModel.delUserGameAmwayList(ids)
            .subscribe({bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        delUserGameAmway()
                        showErrorMsg(bean.msg,bean.code)
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

    //点赞评价|标签
    fun likeAssessOrTag(game_id: String, assess_id: String, tag_id: String) {
        //  mRootView?.showLoading()
        val disposable = mModel.likeAssessOrTag(game_id,assess_id,tag_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        // getUserFollow(issue.data.is_mutual)
                    } else {
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

    fun getDynamicList(page: Int){
        val disposable = mModel.getDynamicList(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        mRootView?.showDynamicNodeList(bean)
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
    fun getAnswerList(page: Int){
        val disposable = mModel.getAnswerList(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        mRootView?.showAnswersNodeList(bean)
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
    fun likePost(post_id:String) {
        // mRootView?.showLoading()
        val disposable = mModel.likePost(post_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        // showMutual(bean.data.is_mutual)
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
    fun delPost(postIds: MutableList<String>) {
        mRootView?.showLoading()
        var ids=Gson().toJson(postIds)
        val disposable = mModel.delPost(ids,"-1")
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code==200){
                        mRootView?.delUserGameAmway()
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