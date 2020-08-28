package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.mine.HomepageContract
import com.tinytiger.titi.mvp.model.mine.HomepageModel

/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 我的 Presenter
*/
class HomepagePresenter : BasePresenter<HomepageContract.View>(), HomepageContract.Presenter {

    private val mModel: HomepageModel by lazy {
        HomepageModel()
    }


    override fun getHomepageInfo(user_id:String) {
        mRootView?.showLoading()
        val disposable = mModel.getHomepageInfo(user_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showHomepageInfo(bean.data)
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

    override fun doFollow(is_mutual:Int,user_id:String) {
        mRootView?.showLoading()
        val disposable = mModel.doFollow(is_mutual,user_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
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




    override fun changeBackgroundImg(background_img: String) {
        mRootView?.showLoading()
        val disposable = mModel.changeBackgroundImg(background_img)
            .subscribe({bean->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showResult()
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


    override fun getHomepageWorks(user_id: String, page: Int) {

        val disposable = mModel.getHomepageWorks(user_id, page)
            .subscribe({ bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showContentList(bean.data)
                    }else{
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

    override fun getHomepageLike(user_id: String, page: Int) {

        val disposable = mModel.getHomepageLike(user_id, page)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showContentList(bean.data)
                    }else{
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


    override fun loadQiniuToken() {
//        mRootView?.showLoading()
        val disposable = mModel.getQiniuToken()
            .subscribe({ issue ->
                    mRootView?.apply {
                        dismissLoading()
                        if (issue.code==200){
                            getQiniuToken(issue.data.qiniu_token)
                        }else{
                            getErrorCode(issue)
                        }
                    }

            }, { t ->
                mRootView?.apply {
                    showErrorMsg(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }


    override   fun addBlack(is_black:Int,user_id: String) {
        mRootView?.showLoading()
        val disposable = mModel.addBlack(is_black,user_id)
            .subscribe({issue ->

                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        val black = if(is_black == 0) 1 else 0
                        showBlackStatus(black)
                    }else{
                        dismissLoading()
                        getErrorCode(issue)
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
  

    fun indexWorks( page: Int) {
        mRootView?.showLoading()
        val disposable = mModel.indexWorks(page)
            .subscribe({ issue ->

                mRootView?.apply {
                    if (issue.code==200){
                        showContentList(issue.data)
                    }else{
                        dismissLoading()
                        getErrorCode(issue)
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