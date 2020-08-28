package com.tinytiger.titi.mvp.presenter.center

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.event.AttentionEvent
import com.tinytiger.titi.mvp.contract.center.MineCollectContract
import com.tinytiger.titi.mvp.contract.center.MineGameContract
import com.tinytiger.titi.mvp.model.center.MineCenterModel
import org.greenrobot.eventbus.EventBus

class MineCollectsPresenter : BasePresenter<MineCollectContract.View>(), MineCollectContract.Presenter {


    private val mModel: MineCenterModel by lazy {
        MineCenterModel()
    }


    override fun getUserGameAmwayCollectList(page: Int) {
        val disposable = mModel.getUserGameAmwayCollectList(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        mRootView?.showAmwayCollectList(bean.data)
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


    override fun collectList(page: Int) {
        // mRootView?.showLoading()
        val disposable = mModel.collectList(page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        mRootView?.showNewsCollectList(bean.data)
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


    override fun getUserWikiCollectList(page: Int) {
        // mRootView?.showLoading()
        val disposable = mModel.getUserTemContentCollectList(page)
            .subscribe({ bean ->
                if (bean.code == 200) {
                    mRootView?.showUserWikiCollectList(bean.data)
                } else {
                    mRootView?.showErrorMsg(bean.msg, bean.code)
                }
                mRootView?.dismissLoading()
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

    fun batchCancelCollectGameAssess(uid: String) {
        mRootView?.showLoading()
        val disposable = mModel.batchCancelCollectGameAssess(uid)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        batchCancelCollect()
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

    fun batchCancelCollectNews(uid: String) {
        mRootView?.showLoading()
        val disposable = mModel.batchCancelCollectNews(uid)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        batchCancelCollect()
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

    fun batchCancelCollectGameWiki(uid: String) {
        mRootView?.showLoading()
        val disposable = mModel.batchCancelCollectGameWiki(uid)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        batchCancelCollect()
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
     * 收藏游戏百科
     */
     fun collectGameWiki(wiki_id:String,is_collect:Int) {

        mRootView?.showLoading()
        val disposable = mModel.collectWiki(wiki_id,is_collect)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        if (is_collect==1){
                            batchCancelCollect()
                        }else{
                            batchCancelCollect()
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
    /**
     * 我的收藏-获取我收藏的帖子列表
     *
     */
     fun getPostList(page: Int){
//        mRootView?.showLoading()
        val disposable = mModel.getPostList(page)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        showPostCollectList(issue)
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
    fun likePost(post_id:String) {
        // mRootView?.showLoading()
        val disposable = mModel.likePost(post_id)
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
    fun batchCancelCollectNode(uid: String) {
        mRootView?.showLoading()
        val disposable = mModel.batchCancelCollectNode(uid)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        batchCancelCollect()
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

    fun doFollow(is_mutual: Int, user_id: String) {
        mRootView?.showLoading()
        val disposable = mModel.doFollow(is_mutual, user_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        EventBus.getDefault().post(AttentionEvent(user_id, bean.data.is_mutual))
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