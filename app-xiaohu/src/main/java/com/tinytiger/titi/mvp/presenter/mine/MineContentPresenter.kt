package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.mine.MineContentContract
import com.tinytiger.titi.mvp.model.mine.MineContentModel

/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 我的 Presenter
*/
class MineContentPresenter : BasePresenter<MineContentContract.View>(), MineContentContract.Presenter {


    private val mModel: MineContentModel by lazy {
        MineContentModel()
    }


    override fun getFocusUserWorks(page: Int) {
//        mRootView?.showLoading()
        val disposable = mModel.getFocusUserWorks(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code==200){
                        showFocusUserWorks(bean.data)
                    }else{
                        dismissLoading()
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


    override fun cancelLike(content_id: Int) {
        mRootView?.showLoading()
        val disposable = mModel.cancelLike(content_id)
            .subscribe({ bean ->

                mRootView?.apply {
                    if (bean.code==200){
                        showResult()
                    }else{
                        dismissLoading()
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

    override fun addLike(content_id: Int) {
        mRootView?.showLoading()
        val disposable = mModel.addLike(content_id)
            .subscribe({ bean ->

                mRootView?.apply {
                    if (bean.code==200){
                        showResult()
                    }else{
                        dismissLoading()
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
    /**
     * 点赞
     */
    fun loadLike(type:Int,position:Int,content_id:String){
        if (type==1){
            addContentLike(position,content_id)
        }else{
            canceContentLike(position,content_id)
        }
    }
    fun addContentLike(position:Int,content_id:String) {
        // mRootView?.showLoading()
        val disposable = mModel.addContentLike(content_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        // getUserFollow(position,uid,issue.data.is_mutual)
                    }else{
                        getErrorCode(issue)
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

    fun canceContentLike(position:Int,content_id:String) {
        // mRootView?.showLoading()
        val disposable = mModel.canceContentLike(content_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        // getUserFollow(position,uid,issue.data.is_mutual)
                    }else{
                        getErrorCode(issue)
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
    /**
     * 收藏
     */
    fun loadCollect(is_collect:Int,position:Int,content_id:String){
        if (is_collect==1){
            cancelContentCollect(position,content_id)
        }else{
            addContentCollect(position,content_id)
        }
    }

    fun addContentCollect(position:Int,content_id:String) {
        //  mRootView?.showLoading()
        val disposable = mModel.addContentCollect(content_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getUserCollect(position,1)
                    }else{
                        getErrorCode(issue)
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
    fun cancelContentCollect(position:Int,content_id:String) {
        //  mRootView?.showLoading()
        val disposable = mModel.cancelContentCollect(content_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getUserCollect(position,-1)
                    }else{
                        getErrorCode(issue)
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