package com.tinytiger.titi.mvp.presenter.news



import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.base.BasePresenter

import com.tinytiger.titi.mvp.contract.news.NewsContract

import com.tinytiger.titi.mvp.model.news.NewsModel


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 下午 3:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页数据处理
 */
class NewsPresenter : BasePresenter<NewsContract.View>(), NewsContract.Presenter {

    private val mDetailModel: NewsModel by lazy {
        NewsModel()
    }

    override fun loadContentInfo(content_id: String) {
        mRootView?.showLoading()
        val disposable = mDetailModel.getContentInfo(content_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code==200){
                        getNewsInfo(issue)
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


    /**
     * 点赞
     */
    fun loadLike(is_like:Int,content_id:String){
        if (is_like== -1){
            addContentLike(content_id)
        }else{
            canceContentLike(content_id)
        }
    }
    fun addContentLike(content_id:String) {
         mRootView?.showLoading()
        val disposable = mDetailModel.addContentLike(content_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getNewsLike(1)
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
    fun canceContentLike(content_id:String) {
         mRootView?.showLoading()
        val disposable = mDetailModel.canceContentLike(content_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getNewsLike(-1)
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

    /**
     * 收藏
     */
    fun loadCollect(is_collect:Int,content_id:String){
        if (is_collect==1){
            cancelContentCollect(content_id)
        }else{
            addContentCollect(content_id)
        }
    }

    fun addContentCollect(content_id:String) {
          mRootView?.showLoading()
        val disposable = mDetailModel.addContentCollect(content_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getNewsCollect(1)
                        //getUserFollow(position,uid,issue.data.is_mutual)
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
    fun cancelContentCollect(content_id:String) {
        mRootView?.showLoading()
        val disposable = mDetailModel.cancelContentCollect(content_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getNewsCollect(-1)
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

    fun addNewsComment(content_id:String,content:String) {
        mRootView?.showLoading()
        val disposable = mDetailModel.addNewsComment(content_id,content)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getNewsText()
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

    /**
     * 关注
     */
    fun follow(uid:String,is_mutual:Int){
        if (is_mutual==-1){
            doFollow(uid)
        }else{
            cancelFans(uid)
        }
    }
    fun doFollow(uid:String) {
        mRootView?.showLoading()
        val disposable = mDetailModel.doFollow(uid)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getUserFollow(issue.data.is_mutual)
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

    fun cancelFans(uid:String) {
        mRootView?.showLoading()
        val disposable = mDetailModel.cancelFans(uid)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getUserFollow(issue.data.is_mutual)
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
}