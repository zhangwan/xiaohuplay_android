package com.tinytiger.titi.mvp.presenter.Video


import com.netease.nim.uikit.common.UserUtil
import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.event.UserStatusEvent
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.video.VideoContract
import com.tinytiger.titi.mvp.model.video.VideoModel
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 视频 Presente
*/
class VideoPresenter : BasePresenter<VideoContract.View>(), VideoContract.Presenter {


    private val mModel: VideoModel by lazy {
        VideoModel()
    }


    override fun getContentInfo(content_id: String) {
//        mRootView?.showLoading()
        val disposable = mModel.getContentInfo(content_id)
            .subscribe({ bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        if (bean.data != null) {
                            mRootView?.showContentInfo(bean.data)
                        }
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

    override fun indexComment(content_id: String, page: Int, comment_type: Int) {
//        mRootView?.showLoading()
        val disposable = mModel.indexComment(content_id, page, comment_type)
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
                mRootView?.showErrorMsg(
                    ExceptionHandle.handleException(t),
                    ExceptionHandle.errorCode
                )
            })
        addSubscription(disposable)
    }


    override fun indexCommentDetail(content_id: String, comment_id: Int, page: Int) {
        val disposable = mModel.indexCommentDetail(content_id, comment_id, page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
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

    override fun addComment(content_id: String, comment_id: Int, content: String) {
        val disposable = mModel.addComment(content_id, comment_id, content)
            .subscribe({ bean ->

                mRootView?.apply {

                    if (bean.code == 200) {
                        mRootView?.showResult()
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

    override fun getIntroContentList(content_id: String) {
        val disposable = mModel.getIntroContentList(content_id)
            .subscribe({ bean ->

                mRootView?.apply {

                    if (bean.code == 200) {
                        if (bean.data != null) {
                            mRootView?.showIntroContentList(bean.data)
                        }
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


    override fun addLike(content_id: String) {
        val disposable = mModel.addContentLike(content_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        // mRootView?.showResult()
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

    override fun cancelLike(content_id: String) {
        val disposable = mModel.cancelContentLike(content_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        // showResult()
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


    override fun addCollect(content_id: String) {
        val disposable = mModel.addContentCollect(content_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        //showErrorMsg("收藏成功",bean.code)
                        //  showResult()
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

    override fun cancelCollect(content_id: String) {
        val disposable = mModel.cancelContentCollect(content_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        // showErrorMsg("取消收藏成功",bean.code)
                        //  showResult()
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


    override fun addCommentLike(is_like: Int, content_id: String, comment_id: Int) {

        val disposable = mModel.addCommentLike(is_like, content_id, comment_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
//                        val like = if (is_like == 1) -1 else 1
//                        mRootView?.showLikeStatus(like, comment_id)
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

    override fun delComment(content_id: String, comment_id: Int) {
        val disposable = mModel.delComment(content_id, comment_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showDelComment(comment_id)
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


    /**
     * 拉黑 取消拉黑
     * @param is_black Boolean  -1 拉黑  1 取消拉黑
     * @param user_id String
     */
    override fun addBlack(is_black: Int, user_id: String, netease_id: String?) {
        val disposable = mModel.addBlack(is_black, user_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        if (is_black == -1) {
                            mRootView?.showErrorMsg("拉黑成功", bean.code)
                            UserUtil.addFromBlack(netease_id)
                            EventBus.getDefault().post(UserStatusEvent(user_id, 1))
                        } else {
                            EventBus.getDefault().post(UserStatusEvent(user_id, -1))
                            mRootView?.showErrorMsg("取消拉黑成功", bean.code)
                            UserUtil.removeFromBlack(netease_id)
                        }
                        val black = if (is_black != 1) 1 else -1
                        mRootView?.showBlackStatus(black, user_id)
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


    override fun doFollow(is_mutual: Int, user_id: String) {
        val disposable = mModel.doFollow(is_mutual, user_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showMutual(bean.data.is_mutual)
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