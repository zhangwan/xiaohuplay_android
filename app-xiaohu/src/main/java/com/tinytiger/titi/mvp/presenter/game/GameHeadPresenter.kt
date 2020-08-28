package com.tinytiger.titi.mvp.presenter.game


import com.tinytiger.common.base.BasePresenter

import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.game.GameHeadContract
import com.tinytiger.titi.mvp.model.game.GameHeadModel

/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 下午 3:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 头条数据
 */
class GameHeadPresenter : BasePresenter<GameHeadContract.View>(), GameHeadContract.Presenter {

    private val mDetailModel: GameHeadModel by lazy {
        GameHeadModel()
    }

    override fun loadHeadLineList(page:Int) {

        val disposable = mDetailModel.indexContent(page)
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code == 200) {
                       getHeadLineList(issue)
                    } else {
                        getErrorCode(issue)
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

    /**
     * 关注
     */
    fun follow(position:Int, uid:String,is_mutual:Int){
        if (is_mutual==-1){
            doFollow(position,uid)
        }else{
            cancelFans(position,uid)
        }
    }
     fun doFollow(position:Int,uid:String) {
        mRootView?.showLoading()
        val disposable = mDetailModel.doFollow(uid)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getUserFollow(position,uid,issue.data.is_mutual)
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

     fun cancelFans(position:Int,uid:String) {
        mRootView?.showLoading()
        val disposable = mDetailModel.cancelFans(uid)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getUserFollow(position,uid,issue.data.is_mutual)
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
     * 点赞
     */
    fun loadLike(type:Int,position:Int,content_id:String){
        if (type==1){
            addContentLike(position,content_id)
        }else{
            canceContentLike(position,content_id)
        }
    }

    fun addContentLike(position:Int,content_id:String) {
       // mRootView?.showLoading()
        val disposable = mDetailModel.addContentLike(content_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                       // getUserFollow(position,uid,issue.data.is_mutual)
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
    fun canceContentLike(position:Int,content_id:String) {
        // mRootView?.showLoading()
        val disposable = mDetailModel.canceContentLike(content_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){

                        // getUserFollow(position,uid,issue.data.is_mutual)
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
    fun loadCollect(is_collect:Int,position:Int,content_id:String){
        if (is_collect==1){
            cancelContentCollect(position,content_id)
        }else{
            addContentCollect(position,content_id)
        }
    }

    fun addContentCollect(position:Int,content_id:String) {
      //  mRootView?.showLoading()
        val disposable = mDetailModel.addContentCollect(content_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getUserCollect(position,1)
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
    fun cancelContentCollect(position:Int,content_id:String) {
        //  mRootView?.showLoading()
        val disposable = mDetailModel.cancelContentCollect(content_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getUserCollect(position,-1)
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

}