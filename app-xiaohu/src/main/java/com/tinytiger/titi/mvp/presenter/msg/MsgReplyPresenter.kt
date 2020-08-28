package com.tinytiger.titi.mvp.presenter.msg


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.titi.mvp.contract.msg.MsgReplyContract
import com.tinytiger.titi.mvp.model.msg.MsgReplyModel
import com.tinytiger.common.net.exception.ExceptionHandle

/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 下午 3:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页数据处理
 */
class MsgReplyPresenter : BasePresenter<MsgReplyContract.View>(), MsgReplyContract.Presenter {

    private val mModel: MsgReplyModel by lazy {
        MsgReplyModel()
    }

    override fun loadReplyList(page:Int) {
       // mRootView?.showLoading()
        val disposable = mModel.getReplyList(page)
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code==200){
                        getReplyList(issue)
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

    override fun loadLikeList(page:Int) {
        mRootView?.showLoading()
        val disposable = mModel.getLike(page)
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code==200){
                        getReplyList(issue)
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

    fun indexMsgComment(content_id:String,comment_id: Int,type:Int,game_id:String,page:Int) {
        mRootView?.showLoading()
        val disposable = mModel.indexMsgComment(content_id,comment_id,type,game_id,page)
            .subscribe({bean ->
                mRootView?.apply {
                    if (bean.code==200){
                        showComment(bean)
                    }else{
                        getErrorCode(bean)
                    }
                    dismissLoading()
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(
                        ExceptionHandle.handleException(t),
                        ExceptionHandle.errorCode
                    )
                }
            })
        addSubscription(disposable)
    }



    fun editReplyRead(comment_id: Int) {
        val disposable = mModel.editReplyRead(comment_id)
            .subscribe()
        addSubscription(disposable)
    }
    fun editLikeRead(comment_id: Int,read_type: Int) {
        val disposable = mModel.editLikeRead(comment_id,read_type)
            .subscribe()
        addSubscription(disposable)
    }
}