package com.tinytiger.titi.mvp.presenter.Video


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.event.UserStatusEvent
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.video.MsgCommentContract

import com.tinytiger.titi.mvp.model.video.MsgCommentModel

import com.netease.nim.uikit.common.UserUtil
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 视频 Presente
*/
class MsgCommentPresenter : BasePresenter<MsgCommentContract.View>(), MsgCommentContract.Presenter {

    private val mModel: MsgCommentModel by lazy {
        MsgCommentModel()
    }

    override fun indexMsgComment(content_id:String,comment_id: Int,type:Int,game_id:String,page:Int,gt_or_lt:Int) {
        val disposable = mModel.indexMsgComment(content_id, comment_id,type,game_id, page,gt_or_lt)
            .subscribe({ bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        showComment(bean.data)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }

    fun addComment(content_id: String, comment_id: Int, content: String) {
        mRootView?.showLoading()
        val disposable = mModel.addComment(content_id, comment_id, content)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        addCommentTxt(content,bean.data.last_insert_comment_id)
                    } else {
                        getErrorCode(bean)
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

    fun delComment(content_id: String, comment_id: Int) {
        mRootView?.showLoading()
        val disposable = mModel.delComment(content_id, comment_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        delComment(comment_id)
                    } else {
                        getErrorCode(bean)
                    }
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


    /**
     * 拉黑 取消拉黑
     * @param is_black Boolean  -1 拉黑  1 取消拉黑
     * @param user_id String
     */
    fun addBlack(is_black: Int, user_id: String, netease_id: String?) {
        mRootView?.showLoading()
        val disposable = mModel.addBlack(is_black, user_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        if (is_black == -1) {
                            UserUtil.addFromBlack(netease_id)
                            EventBus.getDefault().post(UserStatusEvent(user_id, -1))
                        } else {
                            UserUtil.removeFromBlack(netease_id)
                        }
                        val black = if (is_black != 1) 1 else -1
                        showBlackStatus(black,user_id)
                    } else {
                        getErrorCode(bean)
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
     * 点赞
     */
    fun loadLike(type: Int, content_id: String, comment_id: Int) {
        if (type == -1) {
            addCommentLike(content_id, comment_id)
        } else {
            cancelCommentLike(content_id, comment_id)
        }
    }

    fun addCommentLike(content_id: String, comment_id: Int) {
       //  mRootView?.showLoading()
        val disposable = mModel.addCommentLike(content_id, comment_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        isLike(1,comment_id)
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

    fun cancelCommentLike(content_id: String, comment_id: Int) {
       // mRootView?.showLoading()
        val disposable = mModel.cancelCommentLike(content_id, comment_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        isLike(-1,comment_id)
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

    //------a安利文--------------------------------------------------------
    fun addComment(game_id: String, assess_id: String, comment_id: String, content: String) {
        mRootView?.showLoading()
        val disposable = mModel.addComment(game_id,assess_id, comment_id, content)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        addCommentTxt(content,bean.data.last_insert_comment_id)
                    } else {
                        getErrorCode(bean)
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

    fun delCommentMe(comment_id: Int) {
        mRootView?.showLoading()
        val disposable = mModel.delComment(""+comment_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        delComment(comment_id)
                    } else {
                        getErrorCode(bean)
                    }
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

    //点赞|取消点赞评价下的评论
    fun handleLike(game_id: String, assess_id: String, comment_id: Int) {
        //  mRootView?.showLoading()
        val disposable = mModel.handleLike(game_id,assess_id,""+comment_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        isLike(issue.data.is_like,comment_id)

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


    fun addCommentPost(post_id:String,comment_id: String, content: String) {
        mRootView?.showLoading()
        val disposable = mModel.addComment(post_id,comment_id,content)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        addCommentTxt(content,bean.data.last_insert_comment_id)
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

    fun delCommentPost(comment_id: Int) {
        mRootView?.showLoading()
        val disposable = mModel.delCommentPost(""+comment_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        delComment(comment_id)
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
    fun likePostComment(comment_id:String) {
        // mRootView?.showLoading()
        val disposable = mModel.likePostComment(comment_id)
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
}