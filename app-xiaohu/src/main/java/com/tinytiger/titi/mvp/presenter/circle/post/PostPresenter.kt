package com.tinytiger.titi.mvp.presenter.circle.post


import android.app.Activity
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.data.circle.post.PostData
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.CircleAgentUtils
import com.tinytiger.titi.event.AttentionEvent
import com.tinytiger.titi.mvp.contract.circle.post.PostContract

import com.tinytiger.titi.mvp.model.circle.post.PostModel
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 下午 3:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索数据处理
 */
class PostPresenter : BasePresenter<PostContract.View>(), PostContract.Presenter {

    private val mModel: PostModel by lazy {
        PostModel()
    }

    override fun getPostInfo(activity:Activity,post_id:String) {
        val disposable = mModel.getPostInfo(post_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getPostDetailsBean(issue)
                    }else{
                        ToastUtils.show(activity,issue.msg)
                        activity.finish()
                     //   getErrorCode(issue)
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

    fun allComments(post_id:String,comment_type: Int,page: Int) {
        if (page==1){
            mRootView?.showLoading()
        }
        val disposable = mModel.allComments(post_id,comment_type,page)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getPostListBean(issue)
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

    fun doFollow(is_mutual:Int,user_id:String) {
        mRootView?.showLoading()
        val disposable = mModel.doFollow(is_mutual,user_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        EventBus.getDefault().post(AttentionEvent(user_id, bean.data.is_mutual))
                        showMutual(bean.data.is_mutual)
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


    fun addComment(post_id:String,comment_id: String?, content: String) {
        mRootView?.showLoading()
        val disposable = mModel.addComment(post_id,comment_id,content)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        addCommentTxt(content, bean.data.last_insert_comment_id)
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
    fun delComment(comment_id: String) {
        mRootView?.showLoading()
        val disposable = mModel.delComment(comment_id)
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

    fun replyAdoption(item: PostData, post_id: String) {
        mRootView?.showLoading()
        val disposable = mModel.replyAdoption(post_id,item.id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        replyAdoption(item,bean.data.participate_num)
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



    fun subCommentList(comment_id: String,page: Int) {
        val disposable = mModel.subCommentList(comment_id,page)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getPostListBean(issue)
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

    fun collectPost(circle_id: String,post_id: String) {
        mRootView?.showLoading()
        val disposable = mModel.collectPost(post_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        CircleAgentUtils.setCirclePostInfo(circle_id, post_id, 5)
                        showCollect(issue.data.is_collect)
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