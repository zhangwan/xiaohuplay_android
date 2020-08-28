package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.mine.FriendContract
import com.tinytiger.titi.mvp.model.mine.FriendModel

/**
 *
 * @author zhw_luke
 * @date 2019/11/23 0023 上午 10:55
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
class FriendPresenter : BasePresenter<FriendContract.View>(), FriendContract.Presenter {

    private val mModel: FriendModel by lazy {
        FriendModel()
    }

    /**
     * 获取关注列表
     */
    fun loadFollow(uid:String,page:Int,keyWords:String){
        if (uid.isEmpty()){
            indexFollow(page,keyWords)
        }else{
            indexFollow(uid,page,keyWords)
        }
    }


    fun indexFollow(uid:String,page:Int,keyWords:String) {
       // mRootView?.showLoading()
        val disposable = mModel.indexFollow(uid,page,keyWords)
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code==200){
                        getFriendList(issue)
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

    override fun indexFollow(page: Int,keyWords:String) {
       // mRootView?.showLoading()
        val disposable = mModel.indexFollow(page,keyWords)
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code==200){
                        getFriendList(issue)
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
     * 获取粉丝列表
     */
    fun loadFans(uid:String,page:Int,keyWords:String){
        if (uid.isEmpty()){
            indexFans(page,keyWords)
        }else{
            indexFans(uid,page,keyWords)
        }
    }

    fun indexFans(uid:String,page:Int,keyWords:String) {
       // mRootView?.showLoading()
        val disposable = mModel.indexFans(uid,page,keyWords)
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code==200){
                        getFriendList(issue)
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

    override fun indexFans(page: Int,keyWords:String) {
      //  mRootView?.showLoading()
        val disposable = mModel.indexFans(page,keyWords)
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code==200){
                        getFriendList(issue)
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
     * 关注与取消关注
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
        val disposable = mModel.doFollow(uid)
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code==200){
                        getUserFollow(position,uid,issue.data.is_mutual)
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

     fun cancelFans(position:Int,uid:String) {
        mRootView?.showLoading()
        val disposable = mModel.cancelFans(uid)
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code==200){
                        getUserFollow(position,uid,-1)
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




}