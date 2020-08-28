package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.mine.HistoryContract
import com.tinytiger.titi.mvp.model.mine.HistoryModel

/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 我的 Presenter
*/
class HistoryPresenter : BasePresenter<HistoryContract.View>(), HistoryContract.Presenter {


    private val mModel: HistoryModel by lazy {
        HistoryModel()
    }


    override fun getViewHistory(page: Int) {
//        mRootView?.showLoading()
        val disposable = mModel.getViewHistory(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showViewHistory(bean.data)
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


    override fun getUserGameAmwayViewHistory(page: Int) {
        mRootView?.showLoading()
        val disposable = mModel.getUserGameAmwayViewHistory(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showAmwayHistory(bean.data)
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


    override fun delViewHistory(id: Int) {
        mRootView?.showLoading()
        val disposable = mModel.delViewHistory(id)
            .subscribe({ bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showResult()
                        showErrorMsg("删除成功",bean.code)
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

    //清除类型 1资讯 2安利文
    override fun clearViewHistory(type:Int) {
        mRootView?.showLoading()
        val disposable = mModel.clearViewHistory(type)
            .subscribe({ bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showResult()
                        showErrorMsg("清理成功",bean.code)
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

    //点赞评价|标签
    fun likeAssessOrTag(game_id: String, assess_id: String, tag_id: String) {
        //  mRootView?.showLoading()
        val disposable = mModel.likeAssessOrTag(game_id, assess_id, tag_id)
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

    /**
     * 关注
     */
    fun follow(uid: String, is_mutual: Int) {
        if (is_mutual == -1) {
            doFollow(uid)
        } else {
            cancelFans(uid)
        }
    }

    private fun doFollow(uid: String) {
        mRootView?.showLoading()
        val disposable = mModel.doFollow(uid)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        getUserFollow(uid, issue.data.is_mutual)
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

    private fun cancelFans(uid: String) {
        mRootView?.showLoading()
        val disposable = mModel.cancelFans(uid)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        getUserFollow(uid, issue.data.is_mutual)
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


}