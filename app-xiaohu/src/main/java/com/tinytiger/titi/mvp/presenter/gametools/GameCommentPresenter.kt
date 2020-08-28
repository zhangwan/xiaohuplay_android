package com.tinytiger.titi.mvp.presenter.gametools


import android.content.Context
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.gametools.GameCommentContract
import com.tinytiger.titi.mvp.contract.gametools.GameReviewContract

import com.tinytiger.titi.mvp.contract.login.LoginContract
import com.tinytiger.titi.mvp.model.LoginModel
import com.tinytiger.titi.mvp.model.gametools.GameCommentModel
import com.tinytiger.titi.mvp.model.gametools.GameReviewModel


/**
 *
 * @author zhw_luke
 * @date 2020/3/2 0002 下午 2:36
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc 评价
 */
class GameCommentPresenter : BasePresenter<GameCommentContract.View>(), GameCommentContract.Presenter {


    private val mModel: GameCommentModel by lazy {
        GameCommentModel()
    }

    var mContext: Context? = null


    override fun indexAssessComment(game_id: String, assess_id: String, comment_id: String,page: Int) {
        //  mRootView?.showLoading()
        val disposable = mModel.indexAssessComment(game_id, assess_id,comment_id, page)
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        showAssessList(bean)
                    } else {
                        showErrorMsg(bean.msg, bean.code)
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


    fun handleLike(game_id: String, assess_id: String, comment_id: Int) {
        //  mRootView?.showLoading()
        val disposable = mModel.handleLike(game_id,assess_id, ""+comment_id)
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

}