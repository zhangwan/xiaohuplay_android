package com.tinytiger.titi.mvp.presenter.news


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.event.UserStatusEvent
import com.tinytiger.common.net.exception.ExceptionHandle

import com.tinytiger.titi.mvp.contract.video.VideoContract

import com.tinytiger.titi.mvp.model.video.VideoModel
import com.netease.nim.uikit.common.UserUtil
import com.tinytiger.titi.mvp.contract.news.NewsInfoContract
import com.tinytiger.titi.mvp.model.news.NewsInfoModel
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/3/11 0011 下午 2:51
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc
 */
class NewsInfoPresenter : BasePresenter<NewsInfoContract.View>(), NewsInfoContract.Presenter {

    private val mModel: NewsInfoModel by lazy {
        NewsInfoModel()
    }


    override fun getContentInfo(content_id:String) {
        val disposable = mModel.getContentInfo(content_id)
            .subscribe({bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        showContentInfo(bean.data)
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


    override  fun addLike(content_id: String) {
        val disposable = mModel.addContentLike(content_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                      //  showLike(1)
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

    override  fun cancelLike(content_id: String) {
        val disposable = mModel.cancelContentLike(content_id)
            .subscribe({bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                      //  showLike(-1)
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


    override  fun addCollect(content_id: String) {
        val disposable = mModel.addContentCollect(content_id)
            .subscribe({bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                       // showErrorMsg("收藏成功",bean.code)
                      // showCollect(1)
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

    override  fun cancelCollect(content_id: String) {
        val disposable = mModel.cancelContentCollect(content_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                      //  showErrorMsg("取消收藏成功",bean.code)
                       // showCollect(-1)
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