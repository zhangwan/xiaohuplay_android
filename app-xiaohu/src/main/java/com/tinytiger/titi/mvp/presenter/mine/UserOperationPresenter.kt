package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.mine.UserOperationContract
import com.tinytiger.titi.mvp.model.mine.UserOperationModel


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 设置 Presenter
*/
class UserOperationPresenter : BasePresenter<UserOperationContract.View>(),
    UserOperationContract.Presenter {


    private val mModel: UserOperationModel by lazy {
        UserOperationModel()
    }


    override fun getAccountSecurityData() {
        mRootView?.showLoading()
        val disposable = mModel.getAccountSecurityData()
            .subscribe({ bean ->

                mRootView?.apply {
                    if (bean.code==200){
                        showUserInfo(bean.data.user_info)
                    }else{
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

    override fun onResetPassword(password: String, confirm_password: String) {
        mRootView?.showLoading()
        val disposable = mModel.resetPassword(password, confirm_password)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showResult()
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


    override fun sendCode(phone: String) {
        mRootView?.showLoading()
        val disposable = mModel.sendVerificationCode(phone)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        showCountdown()
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

    override fun verificationPhone(verificationCode: String, password: String) {
        mRootView?.showLoading()
        val disposable = mModel.verificationPhone(verificationCode, password)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showResult()
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

    override fun recoverPassword(verificationCode: String, password: String) {
        mRootView?.showLoading()
        val disposable = mModel.recoverPassword(verificationCode, password)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showResult()
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

    override fun changePhone(phone: String, verificationCode: String) {
        mRootView?.showLoading()
        val disposable = mModel.changePhone(phone, verificationCode)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showResult()
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

    fun modifyPassword(password: String) {
        mRootView?.showLoading()
        val disposable = mModel.modifyPassword(password)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showResult()
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


    fun verificationPhone(verificationCode: String) {
        mRootView?.showLoading()
        val disposable = mModel.verificationPhone(verificationCode)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showResult()
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