package com.tinytiger.titi.mvp.presenter.news



import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.data.video.CommentDetailBean
import com.tinytiger.common.net.data.video.ReplysUserinfoBean
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.im.config.preference.Preferences

import com.tinytiger.titi.mvp.contract.news.CommentTwoContract
import com.tinytiger.titi.mvp.model.news.CommentTowModel




/**
 *
 * @author zhw_luke
 * @date 2020/3/11 0011 下午 2:51
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc
 */
class CommentTwoPresenter : BasePresenter<CommentTwoContract.View>(), CommentTwoContract.Presenter {

    private val mModel: CommentTowModel by lazy {
        CommentTowModel()
    }


    override fun indexComment(content_id:String,comment_id: Int,page:Int,type:Int) {
        if (page==1){
            mRootView?.showLoading()
        }
        val disposable = mModel.indexComment(content_id,comment_id,page,type)
            .subscribe({bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        showCommentDetail(bean)
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

    fun delComment(content_id:String,comment_id: Int) {
        mRootView?.showLoading()
        val disposable = mModel.delComment(content_id,comment_id)
            .subscribe({bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        delComment(comment_id)
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

    fun addCommentLike(is_like:Int,content_id: String,comment_id: Int) {
        val disposable = mModel.addCommentLike(is_like,content_id,comment_id)
            .subscribe({bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                      //  showLikeStatus(is_like,comment_id)
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

    fun addComment(content_id: String,sendBean: CommentDetailBean, content: String) {
        mRootView?.showLoading()
        val disposable = mModel.addComment(content_id,sendBean.id,content)
            .subscribe({bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        val  replyBean= CommentDetailBean()
                        replyBean.id = bean.data.last_insert_comment_id
                        val user=ReplysUserinfoBean()
                        user.nickname = SpUtils.getString(R.string.nickname,"")
                        user.user_id = SpUtils.getString(R.string.user_id,"")
                        user.avatar = SpUtils.getString(R.string.avatar,"")
                        user.netease_id= Preferences.getUserAccount()
                        user.master_type= SpUtils.getInt(R.string.master_type,0)
                        user.medal_image= SpUtils.getString(R.string.medal_image,"")
                        replyBean.replysUserinfo=user
                        replyBean.content= content
                        replyBean.is_like=-1

                        if ( user.user_id!=sendBean.replysUserinfo.user_id){
                            replyBean.commentUserinfo =sendBean.replysUserinfo
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
}