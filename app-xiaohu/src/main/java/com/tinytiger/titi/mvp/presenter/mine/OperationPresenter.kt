package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.titi.mvp.contract.mine.OperationContract
import com.tinytiger.titi.mvp.model.mine.OperationModel
import com.tinytiger.common.net.exception.ExceptionHandle

/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 设置 Presenter
*/
class OperationPresenter : BasePresenter<OperationContract.View>(), OperationContract.Presenter {



    private val mModel: OperationModel by lazy {
        OperationModel()
    }

    override fun getAccountSecurityData() {
        mRootView?.showLoading()
        val disposable = mModel.getAccountSecurityData()
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showUserInfo(bean.data.user_info)
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

    override fun getUserInfo() {
        mRootView?.showLoading()
        val disposable = mModel.getUserInfo()
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200){
                        showUserInfo(bean.data)
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


    override fun onModifyAvatar(avatar:String) {
        mRootView?.showLoading()
        val disposable = mModel.modifyAvatar(avatar)
            .subscribe({bean ->
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



    override fun setNoviceUserProfile(avatar: String?,nickname:String?,gender:String?) {
        mRootView?.showLoading()
        val disposable = mModel.setNoviceUserProfile(avatar,nickname,gender)
            .subscribe({bean ->

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

    override fun onModifyGender(gender: String) {
        mRootView?.showLoading()
        val disposable = mModel.modifyGender(gender)
            .subscribe({bean ->
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

    override fun onModifyNickname(nickname: String) {
        mRootView?.showLoading()
        val disposable = mModel.modifyNickname(nickname)
            .subscribe({bean ->
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

    override fun onModifyBirthday(birthday: String) {
        mRootView?.showLoading()
        val disposable = mModel.modifyBirthday(birthday)
            .subscribe({bean ->
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

    override fun onModifyCity(district: String) {
        mRootView?.showLoading()
        val disposable = mModel.modifyCity(district)
            .subscribe({bean ->
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
    override fun onModifyResume(desc: String) {
        mRootView?.showLoading()
        val disposable = mModel.modifyResume(desc)
            .subscribe({bean ->
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


    override fun bindSocialPlatform( type:Int,unionid: String,nickname: String) {
        mRootView?.showLoading()
        val disposable = mModel.bindSocialPlatform(type,unionid,nickname)
            .subscribe({bean ->
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
    fun unBindSocialPlatform(type:Int) {
        mRootView?.showLoading()
        val disposable = mModel.unBindSocialPlatform(type)
            .subscribe({bean ->
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

    override fun loadQiniuToken() {
//        mRootView?.showLoading()
        val disposable = mModel.getQiniuToken()
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code==200){
                        getQiniuToken(issue.data.qiniu_token)
                    }else{
                        dismissLoading()
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

    fun getAreaList() {
        val disposable = mModel.getAreaList()
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code==200){
                        showCityList(bean.data)
                    }else{
                        getErrorCode(bean)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    showErrorMsg(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }


}