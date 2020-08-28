package com.tinytiger.titi.mvp.presenter.detail


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.detail.PlayContract
import com.tinytiger.titi.mvp.model.detail.PlayModel



/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 视频 Presente
*/
class PlayPresenter : BasePresenter<PlayContract.View>(), PlayContract.Presenter {

    private val mModel: PlayModel by lazy {
        PlayModel()
    }

    override fun getContentInfo(content_id:String) {
        val disposable = mModel.getContentInfo(content_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        if(bean.data!=null){
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

    override  fun getIntroContentList(content_id: String) {
        val disposable = mModel.getIntroContentList(content_id)
            .subscribe({bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        if(bean.data!=null){
                            mRootView?.showIntroContentList(bean.data)
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


    override  fun addContentLike(is_like:Int, content_id: String) {
        val disposable = mModel.addContentLike(is_like,content_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        val  like = if(is_like==1) -1 else 1
                        mRootView?.showContentLike(like)
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




    override  fun addContentCollect(is_collect:Int,content_id: String) {
        val disposable = mModel.addContentCollect(is_collect,content_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showContentCollect(1)
                        showErrorMsg("收藏成功",bean.code)
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

    override fun doFollow(is_mutual:Int,user_id:String) {
//        mRootView?.showLoading()
        val disposable = mModel.doFollow(is_mutual,user_id)
            .subscribe({bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showMutualStatus(bean.data.is_mutual)
                    } else {
                        getErrorCode(bean)
                    }
                }

            }, { t ->
//                mRootView?.dismissLoading()
                mRootView?.showErrorMsg(
                    ExceptionHandle.handleException(t),
                    ExceptionHandle.errorCode

                )
            })
        addSubscription(disposable)
    }

}