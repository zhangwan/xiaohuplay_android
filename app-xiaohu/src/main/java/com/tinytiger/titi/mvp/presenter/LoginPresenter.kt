package com.tinytiger.titi.mvp.presenter


import android.content.Context
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.login.LoginContract
import com.tinytiger.titi.mvp.model.LoginModel


/**
 *
 * @author zhw_luke
 * @date 2019/10/25 0025 上午 11:37
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 登录
 */
class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {


    private val mModel: LoginModel by lazy {
        LoginModel()
    }

    var mContext: Context? = null
    override fun sendCode(phone: String) {
        //  mRootView?.showLoading()
        val disposable = mModel.sendVerificationCode(phone)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showCountdown()
                    } else {
                        getErrorCode(bean)
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


    override fun recoverPassword(phone: String, code: String) {
        mRootView?.showLoading()
        val disposable = mModel.recoverPassword(phone, code)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        MyUserData.setToken(bean.data.token)
                        showLoginSuccess(0)
                    } else {
                        getErrorCode(bean)
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

    override fun onCodeLogin(phone: String, code: String) {
        mRootView?.showLoading()
        val disposable = mModel.verificationCodeLogin(phone, code)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        MyUserData.setUserInfo(bean.data.user_info)
                        if (bean.data.action_type == 1) {
                            mRootView?.showLoginSuccess(SpUtils.getInt(R.string.bind_type, 1))
                        } else {
                            mRootView?.showLoginRegister(1)
                        }
                    } else {
                        getErrorCode(bean)
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


    override fun onPasswordLogin(phone: String, password: String) {
        mRootView?.showLoading()
        val disposable = mModel.onPasswordLogin(phone, password)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        if (issue.data.action_type == 1) {
                            MyUserData.setUserInfo(issue.data.user_info)
                            mRootView?.showLoginSuccess(3)
                        }
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
     * 第三方登录
     * @param bind_type Int 1 微信  2 qq
     * @param unionid String
     * @param nickname String
     * @param avatar String
     * @param gender String
     */
    override fun onOtherLogin(
        bind_type: Int,
        unionid: String,
        nickname: String,
        avatar: String,
        gender: String
    ) {
        mRootView?.showLoading()
        val disposable = mModel.otherLogin(bind_type, unionid, nickname, avatar, gender)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        MyUserData.setUserInfo(bean.data.user_info)
                        showLoginSuccess(bind_type)
                    } else if (bean.code == 3007) {
                        MyUserData.setOtherInfo(bind_type, unionid, nickname, avatar, gender)
                        showLoginRegister(2)
                    } else {
                        getErrorCode(bean)
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


    override fun onBindPhone(
        bind_type: Int,
        unionid: String,
        nickname: String,
        avatar: String,
        gender: String,
        phone: String,
        verification_code: String
    ) {
        mRootView?.showLoading()
        val disposable =
            mModel.bindPhone(bind_type, unionid, nickname, avatar, gender, phone, verification_code)
                .subscribe({ bean ->
                    mRootView?.apply {
                        dismissLoading()
                        if (bean.code == 200) {
                            MyUserData.setUserInfo(bean.data.user_info)
                            mRootView?.showLoginSuccess(bean.data.action_type)
                        } else {
                            getErrorCode(bean)
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

    fun getAgreement() {
        mRootView?.showLoading()
        val disposable = mModel.getAgreement()
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        showAgreement(issue.data.app_agreement_url)
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

    override fun verificationPhone(verificationCode: String) {
        mRootView?.showLoading()
        val disposable = mModel.verificationPhone(verificationCode)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showLoginSuccess(0)
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
                        ToastUtils.toshort(BaseApp._instance, "绑定成功")
                        showLoginSuccess(0)
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