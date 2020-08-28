package com.tinytiger.titi.mvp.presenter.detail


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.event.UserStatusEvent
import com.tinytiger.common.net.exception.ExceptionHandle
import com.netease.nim.uikit.common.UserUtil
import com.tinytiger.titi.mvp.contract.detail.CommentContract
import com.tinytiger.titi.mvp.contract.detail.PlayContract
import com.tinytiger.titi.mvp.model.detail.PlayModel
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 视频 Presente
*/
class CommentPresenter : BasePresenter<CommentContract.View>(), CommentContract.Presenter {

    private val mModel: PlayModel by lazy {
        PlayModel()
    }


    override fun indexComment(content_id:String,page:Int) {
        val disposable = mModel.indexComment(content_id,page)
            .subscribe({bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showComment(bean.data)
                    } else {
                        getErrorCode(bean)
                    }
                }

            }, { t ->
                mRootView?.showErrorMsg(
                    ExceptionHandle.handleException(t),
                    ExceptionHandle.errorCode
                )
            })
        addSubscription(disposable)
    }



    override fun indexCommentDetail(content_id:String,comment_id: Int,page:Int) {
        val disposable = mModel.indexCommentDetail(content_id,comment_id,page)
            .subscribe({bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                      //  showComment(bean.data)
                    } else {
                        getErrorCode(bean)
                    }
                }

            }, { t ->

                mRootView?.showErrorMsg(
                    ExceptionHandle.handleException(t),
                    ExceptionHandle.errorCode
                )
            })
        addSubscription(disposable)
    }

    override fun addComment(content_id: String,comment_id: Int, content: String) {
        val disposable = mModel.addComment(content_id,comment_id,content)
            .subscribe({bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        addCommentTxt(content,bean.data.last_insert_comment_id)
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




    override  fun addCommentLike(is_like:Int,content_id: String,comment_id: Int,position: Int) {
//        mRootView?.showLoading()
        val disposable = mModel.addCommentLike(is_like,content_id,comment_id)
            .subscribe({bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        val  like = if(is_like==1) -1 else 1
                        mRootView?.showCommentLike(like,comment_id,position)
                    } else {
                        getErrorCode(bean)
                    }
                }

            }, { t ->
//                mRootView?.dismissLoading()
                mRootView?.showErrorMsg(
                    ExceptionHandle.handleException(t),
                    ExceptionHandle.errorCode
                )
            })
        addSubscription(disposable)
    }

    override  fun delComment(content_id: String,comment_id: Int,position: Int) {

        val disposable = mModel.delComment(content_id,comment_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showDelete(position)
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