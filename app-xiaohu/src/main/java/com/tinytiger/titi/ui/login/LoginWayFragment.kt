package com.tinytiger.titi.ui.login

import android.os.Bundle
import android.view.View
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.event.MainLoginEvent
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.mvp.contract.login.LoginContract
import com.tinytiger.titi.mvp.presenter.LoginPresenter
import com.tinytiger.titi.ui.web.WebActivity
import com.tinytiger.titi.utils.CommonUtils
import com.tinytiger.titi.utils.net.NetworkType
import com.tinytiger.titi.utils.net.NetworkUtil
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.fragment_login_way.*
import org.greenrobot.eventbus.EventBus

/**
 * @author lmq001
 * @date 2020/07/21 09:50
 * @copyright 小虎互联科技
 * @doc 其他登录方式
 */
class LoginWayFragment : BaseFragment(), LoginContract.View {

    //是否显示其他登录方式
    private var showOtherLogin = false
    private var qqtype = true
    private var app_agreement_url = ""

    private val mPresenter by lazy { LoginPresenter() }


    companion object {
        fun getInstance(showOtherLogin: Boolean = false): LoginWayFragment {
            val fragment = LoginWayFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.showOtherLogin = showOtherLogin
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_login_way

    override fun initView() {
        mPresenter.attachView(this)
        tv_other_login.visibility = if (showOtherLogin) View.VISIBLE else View.INVISIBLE
        ll_login_way.visibility = if (showOtherLogin) View.VISIBLE else View.INVISIBLE

        tv_login_wechat.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (NetworkUtil.getNetworkType(activity!!) == NetworkType.NETWORK_NO) {
                showToast(getString(R.string.error_network))
                return@setOnClickListener
            }
            startAuth(1)
        }
        tv_login_qq.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (NetworkUtil.getNetworkType(activity!!) == NetworkType.NETWORK_NO) {
                showToast(getString(R.string.error_network))
                return@setOnClickListener
            }
            startAuth(2)
        }
        //协议
        tv_login_info.setOnClickListener {
            if (app_agreement_url.isEmpty()) {
                mPresenter.getAgreement()
            } else {
                WebActivity.actionStart(activity!!, app_agreement_url)
            }
        }
    }

    override fun start() {

    }


    private fun startAuth(type: Int) {
        if (type == 1) {
            UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, mAuthListener)
            //TCAgent.onEvent(this, "登录页面", "微信登录")
        } else {
            UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.QQ, mAuthListener)
            //  TCAgent.onEvent(this, "登录页面", "QQ登录")
        }
    }

    private val mAuthListener = object : UMAuthListener {
        override fun onComplete(media: SHARE_MEDIA?, p1: Int, map: MutableMap<String, String>?) {
            if (media == null) {
                dismissLoading()
                showToast("登录失败")
                return
            }
            if (map == null) {
                dismissLoading()
                showToast("登录失败")
                return
            }

            when (media) {
                SHARE_MEDIA.WEIXIN -> {
                    val genders = map["gender"]
                    val unionid = map["unionid"]
                    val name = map["name"]
                    val iconurl = map["iconurl"]
                    if (genders != null && unionid != null && name != null && iconurl != null) {
                        val gender = CommonUtils.getGenderNum(genders)
                        mPresenter.onOtherLogin(1, unionid, name, iconurl, gender)
                    } else {
                        dismissLoading()
                        showToast("获取微信信息失败")
                    }
                }
                SHARE_MEDIA.QQ -> {
                    val unionid = map["openid"]
                    val genders = map["gender"]
                    val name = map["name"]
                    val iconurl = map["iconurl"]
                    if (genders != null && unionid != null && name != null && iconurl != null) {
                        val gender = CommonUtils.getGenderNum(genders)
                        mPresenter.onOtherLogin(2, unionid, name, iconurl, gender)
                    } else {
                        dismissLoading()
                        showToast("获取QQ信息失败")
                    }
                }
                else -> {
                    //showToast("登录成功")
                }
            }
        }

        override fun onCancel(p0: SHARE_MEDIA?, p1: Int) {
            dismissLoading()
        }

        override fun onError(p0: SHARE_MEDIA?, p1: Int, throwable: Throwable?) {
            if (p1 == 2 && qqtype) {
                qqtype = false
                startAuth(2)
            } else {
                showToast(throwable?.message!!)
                dismissLoading()
            }
        }

        override fun onStart(p0: SHARE_MEDIA?) {
            showLoading()
        }
    }

    override fun showCountdown() {
    }

    /**
     * 登录成功
     */
    override fun showLoginSuccess(type: Int) {
        dismissLoading()
        EventBus.getDefault().post(MainLoginEvent(true))
        AppManager.getAppManager().finishActivity(LoginActivity::class.java)
        activity!!.finish()
    }

    /**
     * 登录或注册
     * @param type 1 验证码注册 2 微信 3  QQ
     */
    override fun showLoginRegister(type: Int) {
        dismissLoading()
        when (type) {
            1 -> {
//                EventBus.getDefault().post(MainLoginEvent(0, true))
//                FirstPasswordActivity.actionStart(activity!!)
//                LoginAgentUtils.setLoginPhone()
//                activity!!.finish()
            }
            2 -> {
                BindPhoneActivity.actionStart(activity!!)
            }
            3 -> {
                BindPhoneActivity.actionStart(activity!!)
            }
        }
    }

    override fun showAgreement(url: String) {
        app_agreement_url = url
        WebActivity.actionStart(activity!!, app_agreement_url)
    }

    override fun showLoading() {
        LoadingUtils.getInstance().setTimeOut(30 * 1000).show(activity)
    }

    override fun dismissLoading() {
        LoadingUtils.getInstance().dismiss()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}