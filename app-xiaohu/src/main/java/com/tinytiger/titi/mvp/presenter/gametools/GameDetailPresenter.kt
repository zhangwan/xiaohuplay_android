package com.tinytiger.titi.mvp.presenter.gametools


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle

import com.tinytiger.titi.mvp.contract.gametools.GameDetailContract
import com.tinytiger.titi.mvp.model.gametools.GameDetailModel

class GameDetailPresenter : BasePresenter<GameDetailContract.View>(), GameDetailContract.Presenter {


    private val mModel: GameDetailModel by lazy {
        GameDetailModel()
    }


    override fun getGameInfo(game_id: String, user_id: String) {
        mRootView?.showLoading()
        val disposable = mModel.getGameInfo(game_id, user_id)
            .subscribe({ bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        showGameInfoData(bean.data)
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

    //is_follow=0 关注 is_follow=1 取消关注
    override fun GameFollow(game_id: String, is_follow: Int) {
        // mRootView?.showLoading()
        val disposable = mModel.onGameFollow(game_id, is_follow)
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        showGameFollow(if (is_follow == 0) 1 else 0, 0)
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

    override fun getGameInfoCommentList(
        game_id: String,
        user_id: String,
        star_class: String,
        comment_list_status: Int,
        page: Int
    ) {
//        mRootView?.showLoading()
        val disposable =
            mModel.getGameInfoCommentList(game_id, user_id, star_class, comment_list_status, page)
                .subscribe({ bean ->
                    mRootView?.apply {
                        if (bean.code == 200) {
                            showGameInfoCommentList(bean.data)
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


    override fun doFollow(user_id: String, is_follow: Int) {
        //   mRootView?.showLoading()
        val disposable = mModel.doFollow(user_id, is_follow)
            .subscribe({ bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        val isFollow = if (is_follow == 0) 1 else 0
                        showFollowStatus(isFollow, user_id)
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

    override fun likeAssessOrTag(game_id: String, assess_id: String, is_like: Int) {
        val disposable = mModel.likeAssessOrTag(game_id, assess_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        val like = if (is_like == 0) 1 else 0
                        mRootView?.showlikeAssess(0, bean.msg, like)
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

    override fun addAppointment(game_id: String) {
        val disposable = mModel.addAppointment(game_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        addAppointSuccess()
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

}