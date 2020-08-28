package com.tinytiger.titi.mvp.presenter.Video


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.event.UserStatusEvent
import com.tinytiger.common.net.data.video.ReplyDetailBean
import com.tinytiger.common.net.data.video.ReplysUserinfoBean
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.im.config.preference.Preferences
import com.tinytiger.titi.mvp.contract.video.CommentContract

import com.tinytiger.titi.mvp.model.video.CommentModel
import com.netease.nim.uikit.common.UserUtil
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 视频 Presente
*/
class CommentPresenter : BasePresenter<CommentContract.View>(), CommentContract.Presenter {



    private val mModel: CommentModel by lazy {
        CommentModel()
    }

    override fun indexCommentDetail(content_id:String,comment_id: Int,page:Int) {
        mRootView?.showLoading()
        val disposable = mModel.indexCommentDetail(content_id,comment_id,page)
            .subscribe({bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        showCommentDetail(bean.data)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
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

    override fun addComment(content_id: String,comment_id: Int, content: String,hint:String) {
        mRootView?.showLoading()
        val disposable = mModel.addComment(content_id,comment_id,content)
            .subscribe({bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        val  replyBean= ReplyDetailBean()
                        replyBean.id = bean.data.last_insert_comment_id
                        replyBean.nickname = SpUtils.getString(R.string.nickname,"")
                        replyBean.user_id = SpUtils.getString(R.string.user_id,"")
                        replyBean.avatar =SpUtils.getString(R.string.avatar,"")
                        replyBean.netease_id= Preferences.getUserAccount()
                        replyBean.content= content
                        replyBean.is_like=-1
                        replyBean.master_type=SpUtils.getInt(R.string.master_type,0)
                        replyBean.medal_image=SpUtils.getString(R.string.medal_image,"")
                        if(hint.isNotEmpty()) {
                            replyBean.commentUserinfo = ReplysUserinfoBean(hint)
                        }

                        mRootView?.addCommentTxt(replyBean)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
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






    override  fun addCommentLike(is_like:Int,content_id: String,comment_id: Int) {
        val disposable = mModel.addCommentLike(is_like,content_id,comment_id)
            .subscribe({bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        val  like = if(is_like==1) -1 else 1
                        mRootView?.showLikeStatus(like,comment_id)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
                }

            }, { t ->
                mRootView?.showErrorMsg(
                    ExceptionHandle.handleException(t),
                    ExceptionHandle.errorCode
                )
            })
        addSubscription(disposable)
    }

    override  fun delComment(position:Int,content_id: String,comment_id: Int) {
        val disposable = mModel.delComment(content_id,comment_id)
            .subscribe({bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        mRootView?.showDelComment(position)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
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
     * 拉黑 取消拉黑
     * @param is_black Boolean  true 拉黑  false 取消拉黑
     * @param user_id String
     */
    override  fun addBlack(is_black:Int,user_id: String,netease_id: String?) {
        val disposable = mModel.addBlack(is_black,user_id)
            .subscribe({bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        if(is_black==0){
                            mRootView?.showErrorMsg("拉黑成功",bean.code)
                            UserUtil.addFromBlack(netease_id)
                            EventBus.getDefault().post(UserStatusEvent(user_id,1))
                        }else{
                            EventBus.getDefault().post(UserStatusEvent(user_id,-1))
                            mRootView?.showErrorMsg("取消拉黑成功",bean.code)
                            UserUtil.removeFromBlack(netease_id)
                        }
                        val black = if(is_black == 0) 1 else 0
                        mRootView?.showBlackStatus(black,user_id)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
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